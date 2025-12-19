package com.lrde;

import java.util.ArrayList;
import java.util.List;

public class Row {
    private List<Object> values;

    public Row() {
        this.values = new ArrayList<>();
    }

    public Row(List<Object> values) {
        this.values = new ArrayList<>(values);
    }

    public Object get(int index) {
        return values.get(index);
    }

    public void add(Object value) {
        values.add(value);
    }
    
    public List<Object> getValues() {
        return values;
    }

    @Override
    public String toString() {
        return values.toString();
    }
}
