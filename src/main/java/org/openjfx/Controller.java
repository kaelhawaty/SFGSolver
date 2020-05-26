package org.openjfx;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import javax.management.RuntimeErrorException;
import java.util.ArrayList;
import java.util.Map;

public class Controller {
    @FXML
    private Button Add_Edge;
    @FXML
    private Button Solve;
    @FXML
    private TextField Transfer_Answer;
    @FXML
    private TextField From;
    @FXML
    private TextField To;
    @FXML
    private TextField Weight;
    @FXML
    private TextField Nodes;
    @FXML
    private TableView Forward_Paths;
    @FXML
    private TableView Loops;
    private GraphViewer graphViewer = new GraphViewer();
    private myGraph graph;
    private AnchorPane root;
    public void addEdge() {
        int from , to;
        double weight;
        try{
            from = Integer.parseInt(From.getText());
            to = Integer.parseInt(To.getText());
            weight = Double.parseDouble(Weight.getText());
        }catch (Exception e){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Error");
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
            return;
        }
        if(graph == null){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Set the number of nodes");
            errorAlert.setContentText("Please set the number of nodes first");
            errorAlert.showAndWait();
            return;
        }
        try {
            graph.addEdge(new Edge(from, to, weight));
        }catch(RuntimeErrorException e){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Error");
            errorAlert.setContentText(e.getTargetError().getMessage());
            errorAlert.showAndWait();
            return;
        }
        graphViewer.drawGraph(graph, root);
    }
    public void setNodes(){
        graph = new myGraph(Integer.parseInt(Nodes.getText()));
        graphViewer.drawGraph(graph, root);
    }
    public void Solve(){
        Solver solver = new Solver(graph);
        double ans = solver.solve();
        Transfer_Answer.setText(String.valueOf(ans));
        ArrayList<Path> forwardPaths = solver.getForwardPaths();
        TableColumn<Pair<Integer, String>, Integer> ID = new TableColumn<>("ID");
        TableColumn<Pair<Integer, String>, String> pathS = new TableColumn<>("Path");
        ID.setCellValueFactory(new PropertyValueFactory<>("id"));
        pathS.setCellValueFactory(new PropertyValueFactory<>("path"));
        Forward_Paths.getColumns().setAll(ID, pathS);
        for(int i = 0; i < forwardPaths.size(); i++){
            Forward_Paths.getItems().add(new Entry(i, forwardPaths.get(i).getPath()));
        }
        TableColumn<Pair<Integer, String>, Integer> ID2 = new TableColumn<>("ID");
        TableColumn<Pair<Integer, String>, String> pathS2 = new TableColumn<>("Path");
        ID2.setCellValueFactory(new PropertyValueFactory<>("id"));
        pathS2.setCellValueFactory(new PropertyValueFactory<>("path"));
        Loops.getColumns().setAll(ID2, pathS2);
        Map<Integer, Path> loops = solver.getLoops();
        int it = 0;
        for (Map.Entry<Integer, Path> entry: loops.entrySet()) {
            Loops.getItems().add(new Entry(it, entry.getValue().getPath()));
            it++;
        }
        graphViewer.drawGraph(graph, root);

    }
    public void setRoot(AnchorPane root){
        this.root = root;
    }
    public void reset(){
        graph = new myGraph(0);
        graphViewer.drawGraph(graph, root);
    }
}
