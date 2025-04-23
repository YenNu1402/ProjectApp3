package ui;

<<<<<<< HEAD
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
                "C", 
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
=======

import java.awt.*;
import java.awt.datatransfer.*;
import javax.script.*;
import javax.swing.*;

public class CalculatorUI extends JFrame {
    private JTextField inputField;

    public CalculatorUI() {
        setTitle("Biểu thức Calculator");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.PLAIN, 24));
        inputField.setHorizontalAlignment(JTextField.RIGHT);

        JPanel buttonPanel = new JPanel(new GridLayout(6, 4, 5, 5));
        String[] buttons = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "=", "+",
            "(", ")", "√", "C",
            "Copy", "Paste", "", ""
        };

        for (String text : buttons) {
            if (text.isEmpty()) {
                buttonPanel.add(new JLabel());
                continue;
            }

            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.BOLD, 18));
            button.addActionListener(e -> onButtonClick(e.getActionCommand()));
            buttonPanel.add(button);
        }

        setLayout(new BorderLayout(10, 10));
        add(inputField, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
    }

    private void onButtonClick(String command) {
        switch (command) {
            case "=":
                calculateResult();
                break;
            case "C":
                inputField.setText("");
                break;
            case "√":
                inputField.setText(inputField.getText() + "Math.sqrt(");
                break;
            case "Copy":
                copyToClipboard();
                break;
            case "Paste":
                pasteFromClipboard();
                break;
            default:
                inputField.setText(inputField.getText() + command);
        }
    }

    private void calculateResult() {
        try {
            String expression = inputField.getText();
            expression = expression.replaceAll("√", "Math.sqrt");
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
            Object result = engine.eval(expression);
            inputField.setText(result.toString());
        } catch (Exception e) {
            inputField.setText("Lỗi");
        }
    }

    private void copyToClipboard() {
        String text = inputField.getText();
        StringSelection selection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }

    private void pasteFromClipboard() {
        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            String pastedText = (String) clipboard.getData(DataFlavor.stringFlavor);
            inputField.setText(inputField.getText() + pastedText);
        } catch (Exception e) {
            inputField.setText("Lỗi Paste");
        }
    }
}

>>>>>>> MT-5
