package com.lrde;

import com.lrde.command.Command;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--gui")) {
            com.lrde.ui.DatabaseGUI.launch();
            return;
        }

        Scanner scanner = new Scanner(System.in);
        QueryParser parser = new QueryParser();

        System.out.println("Lightweight Relational Database Engine");
        System.out.println("Type 'exit' to quit.");
        System.out.println("Run with '--gui' to start the graphical interface.");

        while (true) {
            System.out.print("lrde> ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            if (input.isEmpty()) {
                continue;
            }

            try {
                Command command = parser.parse(input);
                String result = command.execute();
                if (result != null) {
                    System.out.println(result);
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }
}
