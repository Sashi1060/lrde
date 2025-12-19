package com.lrde.command;

public interface Command {
    /**
     * Executes the command.
     * 
     * @return A result string or null if no result.
     * @throws Exception if execution fails.
     */
    String execute() throws Exception;
}
