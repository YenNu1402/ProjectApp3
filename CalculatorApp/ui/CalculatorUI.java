package ui;

import history.HistoryManager;
import javax.script.ScriptEngineManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class CalculatorUI extends JFrame {
    private JTextField display;
    private JTextField searchField;
    private JTextField deleteField;
    private JButton searchButton;
    private JButton deleteButton;
    private HistoryManager historyManager;
    private JTextArea historyArea;

    public CalculatorUI(HistoryManager historyManager) {
        this.historyManager = historyManager;
        setupUI();
    }

    private void setupUI() {
        setTitle("Calculator");
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Display input
        display = new JTextField();
        display.setEditable(false);
        display.setFont(new Font("Arial", Font.PLAIN, 24));
        add(display, BorderLayout.NORTH);

        // Calculator buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4));
        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+",
                "C", "History","<",">"
        };

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.BOLD, 18));
            button.addActionListener(new ButtonClickListener());
            buttonPanel.add(button);
        }
        add(buttonPanel, BorderLayout.CENTER);

        // History + Search/Delete Panel
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // Search and Delete controls
        JPanel controlPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        searchField = new JTextField();
        searchButton = new JButton("Search");
        deleteField = new JTextField();
        deleteButton = new JButton("Delete #");

        controlPanel.add(new JLabel("Search:"));
        controlPanel.add(searchField);
        controlPanel.add(searchButton);
        controlPanel.add(new JLabel("Delete index:"));
        controlPanel.add(deleteField);
        controlPanel.add(deleteButton);

        searchButton.addActionListener(e -> searchHistory());
        deleteButton.addActionListener(e -> deleteHistoryLine());

        // History area
        historyArea = new JTextArea(8, 20);
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(historyArea);

        bottomPanel.add(controlPanel, BorderLayout.NORTH);
        bottomPanel.add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        loadHistory();
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            switch (command) {
                case "C":
                    display.setText("");
                    break;
                case "History":
                    loadHistory();
                    break;
                case "=":
                    String expression = display.getText();
                    String result = evaluateExpression(expression);
                    display.setText(result);
                    historyManager.addHistory(expression, result);
                    loadHistory(); // Tự động hiện lịch sử mới
                    break;
                default:
                    display.setText(display.getText() + command);
                    break;
            }
        }
    }

    private String evaluateExpression(String expression) {
        try {
            return String.valueOf(new ScriptEngineManager().getEngineByName("JavaScript").eval(expression));
        } catch (Exception e) {
            return "Error";
        }
    }

    private void loadHistory() {
        List<String> historyList = historyManager.getHistory();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < historyList.size(); i++) {
            sb.append(i + 1).append(". ").append(historyList.get(i)).append("\n");
        }
        historyArea.setText(sb.toString());
    }

    private void searchHistory() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadHistory();
            return;
        }

        List<String> results = historyManager.searchHistory(keyword);
        StringBuilder sb = new StringBuilder();
        for (String entry : results) {
            sb.append(entry).append("\n");
        }
        historyArea.setText(sb.toString());
    }

    private void deleteHistoryLine() {
        try {
            int index = Integer.parseInt(deleteField.getText().trim()) - 1;
            historyManager.deleteHistoryAt(index);
            loadHistory();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số thứ tự hợp lệ để xoá!");
        }
    }
}
