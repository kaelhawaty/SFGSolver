package org.openjfx;

import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import javafx.scene.layout.AnchorPane;

import java.util.List;

public class GraphViewer {
    private boolean flag = false;
    void drawGraph(myGraph graph, AnchorPane frame){
        Digraph<String, String> g = new DigraphEdgeList<>();
        for(int i = 0; i < graph.getNumberOfNodes(); i++){
            g.insertVertex(String.valueOf(i));
        }
        for(int i = 0; i < graph.getNumberOfNodes(); i++){
            List<Edge> edges = graph.getAdj(i);
            for(Edge e: edges){
                g.insertEdge(String.valueOf(i), String.valueOf(e.getTo()), String.valueOf(e.getWeight()));
            }
        }
        SmartCircularSortedPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
        SmartGraphPanel<String, String> graphView = new SmartGraphPanel<>(g, strategy);
        graphView.setLayoutX(20);
        graphView.setLayoutY(70);
        graphView.setPrefWidth(550);
        graphView.setPrefHeight(400);
        graphView.setAutomaticLayout(true);
        if(!flag) {
            frame.getChildren().add(graphView);
        }else{
            frame.getChildren().set(frame.getChildren().size()-1, graphView);
        }
        ThreadNotify th = new ThreadNotify(graphView);
        th.start();
        flag = true;

    }
}
