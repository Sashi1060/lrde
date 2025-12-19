package com.lrde.ui;

import com.lrde.QueryParser;
import com.lrde.command.Command;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class DatabaseGUI extends JFrame {

    private JTextArea inputArea;
    private JTextArea outputArea;
    private QueryParser parser;

    public DatabaseGUI() {
        parser = new QueryParser();
        setTitle("Lightweight Relational Database Engine");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JLabel headerLabel = new JLabel("Enter SQL Commands:");
        headerLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(headerLabel, BorderLayout.NORTH);

        // Center - Split Pane
        inputArea = new JTextArea();
        inputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane inputScroll = new JScrollPane(inputArea);
        inputScroll.setBorder(new javax.swing.border.TitledBorder("Input"));

        outputArea = new JTextArea();
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        outputArea.setEditable(false);
        JScrollPane outputScroll = new JScrollPane(outputArea);
        outputScroll.setBorder(new javax.swing.border.TitledBorder("Output"));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, inputScroll, outputScroll);
        splitPane.setDividerLocation(250);
        add(splitPane, BorderLayout.CENTER);

        // Bottom - Buttons
        JPanel buttonPanel = new JPanel();
        JButton executeButton = new JButton("Execute");
        JButton clearButton = new JButton("Clear");

        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeCommand();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputArea.setText("");
                outputArea.setText("");
            }
        });

        buttonPanel.add(executeButton);
        buttonPanel.add(clearButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void executeCommand() {
        String input = inputArea.getText().trim();
        if (input.isEmpty())
            return;

        // Support multiple lines/commands if needed, but for now just parse the whole
        // text as one or split by newline?
        // The parser currently handles one command. Let's split by newlines or just
        // take the whole thing if it's one command.
        // For simplicity, let's assume one command per execution or split by newlines
        // and execute each.

        String[] lines = input.split("\n");
        StringBuilder output = new StringBuilder();

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty())
                continue;

            output.append("> ").append(line).append("\n");
            try {
                Command command = parser.parse(line);
                String result = command.execute();
                if (result != null) {
                    output.append(result).append("\n");
                }
            } catch (Exception e) {
                output.append("Error: ").append(e.getMessage()).append("\n");
            }
            output.append("\n");
        }

        outputArea.append(output.toString());
        // Scroll to bottom
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }

    public static void launch() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DatabaseGUI().setVisible(true);
            }
        });
    }
}
