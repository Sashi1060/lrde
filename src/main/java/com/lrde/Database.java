package com.lrde;

import java.util.HashMap;
import java.util.Map;

public class Database {
    private static Database instance;
    private Map<String, Table> tables;

    private Database() {
        tables = new HashMap<>();
    }

    public static synchronized Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public void createTable(Table table) {
        if (tables.containsKey(table.getName())) {
            throw new IllegalArgumentException("Table already exists: " + table.getName());
        }
        tables.put(table.getName(), table);
    }

    public Table getTable(String name) {
        return tables.get(name);
    }
    
    public boolean tableExists(String name) {
        return tables.containsKey(name);
    }
    
    // For testing/reset
    public void clear() {
        tables.clear();
    }
}
