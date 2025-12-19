package com.lrde.command;

import com.lrde.Database;
import com.lrde.Row;
import com.lrde.Table;
import java.util.List;

public class InsertCommand implements Command {
    private String tableName;
    private List<Object> values;

    public InsertCommand(String tableName, List<Object> values) {
        this.tableName = tableName;
        this.values = values;
    }

    @Override
    public String execute() throws Exception {
        Database db = Database.getInstance();
        Table table = db.getTable(tableName);
        if (table == null) {
            throw new IllegalArgumentException("Table not found: " + tableName);
        }

        Row row = new Row(values);
        table.insert(row);
        return "1 row inserted into " + tableName + ".";
    }
}
