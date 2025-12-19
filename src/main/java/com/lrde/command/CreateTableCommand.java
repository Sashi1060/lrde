package com.lrde.command;

import com.lrde.Column;
import com.lrde.Database;
import com.lrde.Table;
import java.util.List;

public class CreateTableCommand implements Command {
    private String tableName;
    private List<Column> columns;

    public CreateTableCommand(String tableName, List<Column> columns) {
        this.tableName = tableName;
        this.columns = columns;
    }

    @Override
    public String execute() throws Exception {
        Database db = Database.getInstance();
        if (db.tableExists(tableName)) {
            throw new IllegalArgumentException("Table already exists: " + tableName);
        }
        Table table = new Table(tableName, columns);
        db.createTable(table);
        return "Table " + tableName + " created.";
    }
}
