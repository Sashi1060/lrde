package com.lrde.command;

import com.lrde.Column;
import com.lrde.DataType;
import com.lrde.Database;
import com.lrde.Row;
import com.lrde.Table;

import java.util.List;
import java.util.stream.Collectors;

public class SelectCommand implements Command {
    private String tableName;
    private String whereCol;
    private String whereOp;
    private String whereVal;

    public SelectCommand(String tableName, String whereCol, String whereOp, String whereVal) {
        this.tableName = tableName;
        this.whereCol = whereCol;
        this.whereOp = whereOp;
        this.whereVal = whereVal;
    }

    @Override
    public String execute() throws Exception {
        Database db = Database.getInstance();
        Table table = db.getTable(tableName);
        if (table == null) {
            throw new IllegalArgumentException("Table not found: " + tableName);
        }

        List<Row> resultRows = table.getRows();

        if (whereCol != null) {
            int colIndex = table.getColumnIndex(whereCol);
            if (colIndex == -1) {
                throw new IllegalArgumentException("Column not found: " + whereCol);
            }
            Column col = table.getColumn(whereCol);

            resultRows = resultRows.stream()
                    .filter(row -> evaluateCondition(row.get(colIndex), col.getType(), whereOp, whereVal))
                    .collect(Collectors.toList());
        }

        return formatResult(table.getColumns(), resultRows);
    }

    private boolean evaluateCondition(Object val, DataType type, String op, String targetVal) {
        if (val == null)
            return false; // Simple null handling

        String valStr = val.toString();

        // Basic string comparison for all types for now, can be improved
        int compareResult = 0;

        if (type == DataType.INT) {
            try {
                int v1 = Integer.parseInt(valStr);
                int v2 = Integer.parseInt(targetVal);
                compareResult = Integer.compare(v1, v2);
            } catch (NumberFormatException e) {
                return false; // Type mismatch in query
            }
        } else {
            compareResult = valStr.compareTo(targetVal);
        }

        switch (op) {
            case "=":
                return compareResult == 0;
            case "!=":
                return compareResult != 0;
            case ">":
                return compareResult > 0;
            case "<":
                return compareResult < 0;
            case ">=":
                return compareResult >= 0;
            case "<=":
                return compareResult <= 0;
            default:
                throw new IllegalArgumentException("Unknown operator: " + op);
        }
    }

    private String formatResult(List<Column> columns, List<Row> rows) {
        StringBuilder sb = new StringBuilder();
        // Header
        for (int i = 0; i < columns.size(); i++) {
            sb.append(columns.get(i).getName()).append("\t");
        }
        sb.append("\n");

        // Rows
        for (Row row : rows) {
            for (Object val : row.getValues()) {
                sb.append(val).append("\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
