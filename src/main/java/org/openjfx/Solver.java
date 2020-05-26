package org.openjfx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class Pair<T, V>{
    private T first;
    private V second;
    Pair(T first, V second){
        this.first = first;
        this.second = second;
    }
    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public V getSecond() {
        return second;
    }

    public void setSecond(V second) {
        this.second = second;
    }

}

class Path{
    StringBuilder s = new StringBuilder();
    int pathMask = 0;
    double pathGain = 1;
    Path( String s, int pathMask, double pathGain){
        this.s.append(s);
        this.pathMask = pathMask;
        this.pathGain = pathGain;
    }
    Path(){};
    void addNode(int node, double gain){
        char c = (char) ('0' + node);
        s.append(c);
        s.append("V ");
        pathGain *= gain;
        pathMask |= (1 << node);
    }

    String getPath(){
        return s.toString();
    }

    boolean intersects(int mask){
        return ((pathMask&mask) != 0);
    }

    double getGain(){
        return pathGain;
    }

    int getMask(){
        return pathMask;
    }

    void reversePath(){
        s = s.reverse();
    }
};


public class Solver {
    private myGraph myGraph;
    private ArrayList<Path> forwardPaths = new ArrayList<>();
    private Map<Integer, Path> loops = new HashMap<>();
    private Map<Integer,ArrayList<Pair<Integer, Double>>> combinations = new HashMap<>();
    Solver(myGraph myGraph){
        this.myGraph = myGraph;
    }

    void generateNonTouchingLoops(int index, int count, int mask, double gain, Object[] loops){
        if(index == loops.length){
            if(count != 0) {
                ArrayList<Pair<Integer, Double>> arr = combinations.getOrDefault(count, new ArrayList<>());
                arr.add(new Pair(mask, gain));
                combinations.put(count, arr);
            }
            return;
        }
        Path p = (Path) loops[index];
        if((p.getMask() & mask) == 0) {
            generateNonTouchingLoops(index+1, count + 1, p.getMask() | mask, p.getGain() * gain, loops);
        }
        generateNonTouchingLoops(index+1, count, mask, gain, loops);
    }

    void generatePathsAndLoops(int node, double gain,int mask, StringBuilder sb,
                               boolean[] visited, Pair<Integer, Double>[] par
    ){
        if(node == myGraph.getNumberOfNodes()-1){
            // forwardPath
            forwardPaths.add(new Path(sb.toString(), mask, gain));
            return;
        }
        if(visited[node]){
            //loop
            Path p = new Path();
            int curNode = node;
            p.addNode(node, 1);
            do{
                p.addNode(par[curNode].getFirst(), par[curNode].getSecond());
                curNode = par[curNode].getFirst();
            }while(curNode != node);
            p.reversePath();
            if(!loops.containsKey(p.getMask())) {
                loops.put(p.getMask(), p);
            }
            return;
        }
        visited[node] = true;
        for(Edge e : myGraph.getAdj(node)){
            sb.append(" V");
            sb.append((char)('0' + e.getTo()));
            Pair<Integer, Double> temp = par[e.getTo()];
            par[e.getTo()] = new Pair<>(node, e.getWeight());
            generatePathsAndLoops(e.getTo(), gain*e.getWeight(), mask | (1 << e.getTo()), sb, visited, par);
            par[e.getTo()] = temp;
            sb.setLength(sb.length()-3);

        }
        visited[node] = false;
    }
    double getDeterminant(int mask){
        double deter = 1;
        for(Map.Entry<Integer,ArrayList<Pair<Integer, Double>>> entry : combinations.entrySet()){
            boolean odd = ((entry.getKey()&1) != 0);
            double sum = 0;
            for(Pair<Integer, Double> pair: entry.getValue()){
                if((mask & pair.getFirst()) == 0){ // non intersecting
                    sum += pair.getSecond();
                }
            }
            deter += ((odd) ? (-sum) : sum);
        }
        return deter;
    }

    double solve(){
        int n = myGraph.getNumberOfNodes();
        StringBuilder curPath = new StringBuilder(" V0");
        boolean[] visited = new boolean[n];
        Arrays.fill(visited, false);
        Pair<Integer, Double>[] par = new Pair[n];

        generatePathsAndLoops(0, 1, 0, curPath, visited, par);
        Object[] paths =  loops.values().toArray();
        generateNonTouchingLoops(0, 0, 0, 1, paths);
        double generalDeter = getDeterminant(0);
        double ans = 0;
        for(Path path: forwardPaths){
            ans += path.getGain()*getDeterminant(path.getMask());
        }
        ans /= generalDeter;
        return ans;
    }

    ArrayList<Path> getForwardPaths(){
        return forwardPaths;
    }

    Map<Integer, Path> getLoops(){
        return loops;
    }
}
