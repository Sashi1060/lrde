package com.lrde;

import com.lrde.command.Command;
import com.lrde.command.CreateTableCommand;
import com.lrde.command.InsertCommand;
import com.lrde.command.SelectCommand;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryParser {

    public Command parse(String query) {
        List<String> tokens = tokenize(query);
        if (tokens.isEmpty()) {
            throw new IllegalArgumentException("Empty query");
        }

        String commandType = tokens.get(0).toUpperCase();

        switch (commandType) {
            case "CREATE":
                return parseCreate(tokens);
            case "INSERT":
                return parseInsert(tokens);
            case "SELECT":
                return parseSelect(tokens);
            default:
                throw new IllegalArgumentException("Unknown command: " + commandType);
        }
    }

    private List<String> tokenize(String query) {
        List<String> tokens = new ArrayList<>();
        // Regex to match words, quoted strings, symbols
        Pattern pattern = Pattern.compile("\"([^\"]*)\"|'([^']*)'|([a-zA-Z0-9_]+)|([(),=<>!*]+)");
        Matcher matcher = pattern.matcher(query);

        while (matcher.find()) {
            if (matcher.group(1) != null) {
                tokens.add(matcher.group(1)); // Double quoted string
            } else if (matcher.group(2) != null) {
                tokens.add(matcher.group(2)); // Single quoted string
            } else if (matcher.group(3) != null) {
                tokens.add(matcher.group(3)); // Word/Number
            } else if (matcher.group(4) != null) {
                tokens.add(matcher.group(4)); // Symbol
            }
        }
        return tokens;
    }

    private Command parseCreate(List<String> tokens) {
        // CREATE TABLE <name> (<col> <type>, ...)
        if (tokens.size() < 4 || !tokens.get(1).equalsIgnoreCase("TABLE")) {
            throw new IllegalArgumentException("Invalid CREATE TABLE syntax");
        }
        String tableName = tokens.get(2);
        if (!tokens.get(3).equals("(")) {
            throw new IllegalArgumentException("Expected '(' after table name");
        }

        List<Column> columns = new ArrayList<>();
        int i = 4;
        while (i < tokens.size() && !tokens.get(i).equals(")")) {
            if (tokens.get(i).equals(",")) {
                i++;
                continue;
            }
            String colName = tokens.get(i++);
            String colTypeStr = tokens.get(i++);
            DataType colType = DataType.valueOf(colTypeStr.toUpperCase());
            columns.add(new Column(colName, colType));

            if (i < tokens.size() && tokens.get(i).equals(",")) {
                i++;
            }
        }
        return new CreateTableCommand(tableName, columns);
    }

    private Command parseInsert(List<String> tokens) {
        // INSERT INTO <name> VALUES (<val>, ...)
        if (tokens.size() < 5 || !tokens.get(1).equalsIgnoreCase("INTO")) {
            throw new IllegalArgumentException("Invalid INSERT syntax");
        }
        String tableName = tokens.get(2);
        if (!tokens.get(3).equalsIgnoreCase("VALUES")) {
            throw new IllegalArgumentException("Expected VALUES");
        }
        if (!tokens.get(4).equals("(")) {
            throw new IllegalArgumentException("Expected '(' after VALUES");
        }

        List<Object> values = new ArrayList<>();
        int i = 5;
        while (i < tokens.size() && !tokens.get(i).equals(")")) {
            if (tokens.get(i).equals(",")) {
                i++;
                continue;
            }
            String val = tokens.get(i++);
            values.add(val); // Keep as string, validation happens in Table
        }
        return new InsertCommand(tableName, values);
    }

    private Command parseSelect(List<String> tokens) {
        // SELECT * FROM <name> [WHERE <col> <op> <val>]
        if (tokens.size() < 4 || !tokens.get(2).equalsIgnoreCase("FROM")) {
            throw new IllegalArgumentException("Invalid SELECT syntax");
        }
        // Assuming SELECT * for now
        String tableName = tokens.get(3);

        String whereCol = null;
        String whereOp = null;
        String whereVal = null;

        if (tokens.size() > 4 && tokens.get(4).equalsIgnoreCase("WHERE")) {
            if (tokens.size() < 8) {
                throw new IllegalArgumentException("Invalid WHERE clause");
            }
            whereCol = tokens.get(5);
            whereOp = tokens.get(6);
            whereVal = tokens.get(7);
        }

        return new SelectCommand(tableName, whereCol, whereOp, whereVal);
    }
}
