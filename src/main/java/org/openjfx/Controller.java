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
    private Button Set;
    @FXML
    private Button Reset;
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
    private TextField Input_Node;
    @FXML
    private TextField Output_Node;
    @FXML
    private TextField Overall_Delta;
    @FXML
    private TableView Forward_Paths;
    @FXML
    private TableView Non_Touching_Loops;
    @FXML
    private TableView Delta_Values;
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
    public void Set_Graph(){
        int nodes, start, end;
        try{
            nodes = Integer.parseInt(Nodes.getText());
            start = Integer.parseInt(Input_Node.getText());
            end = Integer.parseInt(Output_Node.getText());
            if(nodes < 0 || start < 1 || start > nodes || end < 1 || end > nodes){
                throw new Exception("Please check that number of nodes is positive and start and end nodes are between 1 and nodes");
            }
            graph = new myGraph(nodes, start, end);
            graphViewer.drawGraph(graph, root);
            Add_Edge.setDisable(false);
            Solve.setDisable(false);
            Reset.setDisable(false);
            From.setDisable(false);
            To.setDisable(false);
            Weight.setDisable(false);
            Set.setDisable(true);
        }catch(RuntimeErrorException e){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Error");
            errorAlert.setContentText(e.getTargetError().getMessage());
            errorAlert.showAndWait();
        }catch (Exception e){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Error");
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
        }

    }
    private <T> TableColumn<T, ?> getTableColumnByName(TableView<T> tableView, String name) {
        for (TableColumn<T, ?> col : tableView.getColumns())
            if (col.getText().equals(name)) return col ;
        return null ;
    }
    public void Solve(){
        Solver solver = new Solver(graph);
        double ans = solver.solve();
        Transfer_Answer.setText(String.valueOf(ans));
        ArrayList<Path> forwardPaths = solver.getForwardPaths();
        Forward_Paths.getItems().clear();
        Delta_Values.getItems().clear();
        getTableColumnByName(Forward_Paths, "ID").setCellValueFactory(new PropertyValueFactory<>("first"));
        getTableColumnByName(Forward_Paths, "Path").setCellValueFactory(new PropertyValueFactory<>("second"));
        getTableColumnByName(Delta_Values, "ID").setCellValueFactory(new PropertyValueFactory<>("first"));
        getTableColumnByName(Delta_Values, "Delta Value").setCellValueFactory(new PropertyValueFactory<>("second"));
        for(int i = 0; i < forwardPaths.size(); i++){
            Forward_Paths.getItems().add(new Entry(i, forwardPaths.get(i).getPath()));
            Delta_Values.getItems().add(new Entry(i, solver.getDeterminant(forwardPaths.get(i).getMask())));
        }
        Loops.getItems().clear();
        getTableColumnByName(Loops, "ID").setCellValueFactory(new PropertyValueFactory<>("first"));
        getTableColumnByName(Loops, "Path").setCellValueFactory(new PropertyValueFactory<>("second"));
        Map<Integer, Path> loops = solver.getLoops();
        int it = 0;
        for (Map.Entry<Integer, Path> entry: loops.entrySet()) {
            Loops.getItems().add(new Entry(it, entry.getValue().getPath()));
            it++;
        }
        Non_Touching_Loops.getItems().clear();
        getTableColumnByName(Non_Touching_Loops, "ID").setCellValueFactory(new PropertyValueFactory<>("first"));
        getTableColumnByName(Non_Touching_Loops, "Combination").setCellValueFactory(new PropertyValueFactory<>("second"));
        Map<Integer,ArrayList<Combination<Integer, Double>>> comb = solver.getCombinations();
        it = 0;
        for (Map.Entry<Integer,ArrayList<Combination<Integer, Double>>> entry: comb.entrySet()) {
            for(Combination<Integer, Double> c: entry.getValue()){
                Non_Touching_Loops.getItems().add(new Entry(it, c.getCombination()));
                it++;
            }
        }
        Overall_Delta.setText(String.valueOf(solver.getDeterminant(0)));
        graphViewer.drawGraph(graph, root);

    }
    public void setRoot(AnchorPane root){
        this.root = root;
    }
    public void reset(){
        Add_Edge.setDisable(true);
        Solve.setDisable(true);
        Reset.setDisable(true);
        From.setDisable(true);
        To.setDisable(true);
        Weight.setDisable(true);
        Set.setDisable(false);
        From.setText("");
        To.setText("");
        Weight.setText("");
        Overall_Delta.setText("");
        Transfer_Answer.setText("");
        Forward_Paths.getItems().clear();
        Loops.getItems().clear();
        Delta_Values.getItems().clear();
        Non_Touching_Loops.getItems().clear();
        graph = new myGraph(0, 0 ,0);
        graphViewer.drawGraph(graph, root);
    }
}
