package org.openjfx;

import javax.management.RuntimeErrorException;
import java.util.*;

class Edge{
    private int from, to;
    private double weight;
    Edge(int from, int to, double weight){
        this.from = from;
        this.to = to;
        this.weight = weight;
    }
    int getFrom(){
        return from;
    }

    int getTo(){
        return to;
    }

    double getWeight(){
        return weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return from == edge.from &&
                to == edge.to &&
                Double.compare(edge.weight, weight) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
};

public class myGraph {
    private int nodes;
    private int start;
    private int end;
    private ArrayList<List<Edge>> adj;
    Set<Edge> st;
    myGraph(int n, int start, int end){
        nodes = n;
        this.start = start;
        this.end = end;
        st = new HashSet<>();
        adj = new ArrayList<>();
        for(int i = 0; i < n+1; i++){
            adj.add(i, new ArrayList<>());
        }
    }
    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    int getNumberOfNodes(){
        return nodes;
    }
    void addEdge(Edge e){
        if(!checkInRange(e.getFrom())  || !checkInRange(e.getTo())){
            throw new RuntimeErrorException(new Error("Nodes number is not between 1 to " + nodes + ": " + e.getFrom() + " " + e.getTo()));
        }
        if(st.contains(e)){
            throw new RuntimeErrorException(new Error("Edge has been inserted before, Duplicate Edge!"));
        }
        if(e.getTo() == start ){
            throw new RuntimeErrorException(new Error("Adding Edge pointing to Input Node"));
        }
        if(e.getFrom() == end ){
            throw new RuntimeErrorException(new Error("Adding Edge pointing from Output Node"));
        }
        adj.get(e.getFrom()).add(e);
        st.add(e);
    }
    List<Edge> getAdj(int node){
        if(!checkInRange(node)){
            throw new RuntimeErrorException(new Error(node + "is not in between 1 and" + nodes));
        }
        return adj.get(node);
    }
    boolean checkInRange(int x){
        if(x < 1 || x > nodes){
            return false;
        }
        return true;
    }


}
