package org.openjfx;

public class Entry<T,V>{
    private T first;
    private V second;

    public T getFirst() {
        return first;
    }

    public V getSecond() {
        return second;
    }

    Entry(T first, V second){
        this.first = first;
        this.second = second;
    }

}