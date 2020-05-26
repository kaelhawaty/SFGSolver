package org.openjfx;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;

public class ThreadNotify extends Thread {
    SmartGraphPanel<String, String> graphView;
    ThreadNotify(SmartGraphPanel<String, String> graphView){
        this.graphView = graphView;
    }
    public void run(){
        while(graphView.getWidth() == 0 || graphView.getHeight() == 0){
        }
        graphView.init();
    }
}
