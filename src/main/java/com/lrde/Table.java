package com.lrde;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Table {
    private String name;
    private List<Column> columns;
    private List<Row> rows;
    private Map<String, Integer> columnIndices;

    public Table(String name, List<Column> columns) {
        this.name = name;
        this.columns = columns;
        this.rows = new ArrayList<>();
        this.columnIndices = new LinkedHashMap<>();
        for (int i = 0; i < columns.size(); i++) {
            columnIndices.put(columns.get(i).getName(), i);
        }
    }

    public String getName() {
        return name;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void insert(Row row) {
        validateRow(row);
        rows.add(row);
    }

    private void validateRow(Row row) {
        if (row.getValues().size() != columns.size()) {
            throw new IllegalArgumentException(
                    "Row column count mismatch. Expected " + columns.size() + ", got " + row.getValues().size());
        }

        for (int i = 0; i < columns.size(); i++) {
            Object value = row.get(i);
            DataType type = columns.get(i).getType();

            if (value == null)
                continue; // Assuming nullable for now, or we could enforce non-null

            switch (type) {
                case INT:
                    if (!(value instanceof Integer)) {
                        try {
                            Integer.parseInt(value.toString());
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException("Invalid type for column " + columns.get(i).getName()
                                    + ". Expected INT, got " + value.getClass().getSimpleName());
                        }
                    }
                    break;
                case STRING:
                    if (!(value instanceof String)) {
                        throw new IllegalArgumentException("Invalid type for column " + columns.get(i).getName()
                                + ". Expected STRING, got " + value.getClass().getSimpleName());
                    }
                    break;
                case BOOLEAN:
                    if (!(value instanceof Boolean)) {
                        // strict boolean check
                        if (!"true".equalsIgnoreCase(value.toString()) && !"false".equalsIgnoreCase(value.toString())) {
                            throw new IllegalArgumentException("Invalid type for column " + columns.get(i).getName()
                                    + ". Expected BOOLEAN, got " + value.getClass().getSimpleName());
                        }
                    }
                    break;
            }
        }
    }

    public List<Row> getRows() {
        return rows;
    }

    public int getColumnIndex(String columnName) {
        return columnIndices.getOrDefault(columnName, -1);
    }

    public Column getColumn(String columnName) {
        int index = getColumnIndex(columnName);
        if (index == -1)
            return null;
        return columns.get(index);
    }
}
