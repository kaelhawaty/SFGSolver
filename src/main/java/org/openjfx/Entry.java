package org.openjfx;

public class Entry{
    private int id;
    private String path;
    Entry(int id, String path){
        this.id = id;
        this.path = path;
    }
    public Integer getId() {
        return id;
    }
    public String getPath() {
        return path;
    }

}