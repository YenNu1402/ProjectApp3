package ui;

import core.CalculatorErrorHandler;
import history.HistoryManager;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

public class CalculatorUI extends JFrame {
    private StringBuilder expression = new StringBuilder();
    private JTextField display;
    private JTextField searchField;
    private JTextField deleteField;
    private JButton searchButton;
    private JButton deleteButton;
    private HistoryManager historyManager;
    private JTextArea historyArea;
    private boolean isRadians = true;
    private boolean isDarkMode = false;
    private double lastResult = 0;
    private boolean isOperatorPressed = false;
    private boolean isPiPressed = false;
    private boolean isEPressed = false;

    public CalculatorUI(HistoryManager historyManager) {
        this.historyManager = historyManager;
        setupUI();
    }

    private void setupUI() {
        setTitle("Scientific Calculator");
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Display field
        display = new JTextField();
        display.setEditable(true); // Cho phép chỉnh sửa
        display.setFont(new Font("Arial", Font.PLAIN, 24));
        display.setHorizontalAlignment(JTextField.RIGHT);
        add(display, BorderLayout.NORTH);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(7, 6, 5, 5));
        String[] buttons = {
            "On/Off", "Rad/Deg", "sin", "cos", "tan", "cot",
            "log10", "ln", "x^y", "π", "!", "logab",
            "7", "8", "9", "/", "C", "DEL",
            "4", "5", "6", "*", "+", "e",
            "1", "2", "3", "-", "/", "1/x",
            "0", ".", "%", "=", "Ans", "Theme",
            "Left", "Right", "", "", "", "" // Thêm nút Left, Right
        };

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.BOLD, 18));
            if (text.isEmpty()) {
                button.setEnabled(false);
                button.setVisible(false);
            } else {
                button.addActionListener(new ButtonClickListener());
            }
            styleButton(button, text);
            buttonPanel.add(button);
        }
        add(buttonPanel, BorderLayout.CENTER);

        // History and control panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel controlPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        searchField = new JTextField();
        searchButton = new JButton("Search");
        deleteField = new JTextField();
        deleteButton = new JButton("Delete #");
        JButton clearHistoryButton = new JButton("Clear History");

        controlPanel.add(new JLabel("Search:"));
        controlPanel.add(searchField);
        controlPanel.add(searchButton);
        controlPanel.add(clearHistoryButton);
        controlPanel.add(new JLabel("Delete index:"));
        controlPanel.add(deleteField);
        controlPanel.add(deleteButton);
        controlPanel.add(new JLabel(""));

        searchButton.addActionListener(e -> searchHistory());
        deleteButton.addActionListener(e -> deleteHistoryLine());
        clearHistoryButton.addActionListener(e -> clearHistory());

        historyArea = new JTextArea(8, 20);
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(historyArea);

        bottomPanel.add(controlPanel, BorderLayout.NORTH);
        bottomPanel.add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Menu bar
        createMenu();

        loadHistory();
        ThemeManager.applyTheme(this, isDarkMode);
        setupKeyboardShortcuts();
    }

    private void styleButton(JButton button, String command) {
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        if (command.matches("[0-9\\.]")) {
            button.setBackground(new Color(0x424242));
            button.setForeground(Color.WHITE);
        } else if (command.matches("[+\\-*/=^%√]") || command.equals("1/x")) {
            button.setBackground(new Color(0x009688));
            button.setForeground(Color.WHITE);
        } else if (command.equals("C") || command.equals("DEL")) {
            button.setBackground(new Color(0xD32F2F));
            button.setForeground(Color.WHITE);
        } else if (command.equals("On/Off") || command.equals("Rad/Deg") || command.equals("Theme") || 
                   command.equals("Left") || command.equals("Right")) {
            button.setBackground(new Color(0x0288D1));
            button.setForeground(Color.WHITE);
        } else if (command.isEmpty()) {
            button.setBackground(getBackground());
        } else {
            button.setBackground(new Color(0x333333));
            button.setForeground(Color.WHITE);
        }
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();

        // Theme menu
        JMenu themeMenu = new JMenu("Theme");
        JMenuItem toggleTheme = new JMenuItem("Toggle Dark/Light Mode");
        toggleTheme.addActionListener(e -> {
            isDarkMode = !isDarkMode;
            ThemeManager.applyTheme(this, isDarkMode);
        });
        themeMenu.add(toggleTheme);

        // Font menu
        JMenu fontMenu = new JMenu("Font");
        JMenuItem chooseFont = new JMenuItem("Choose Font");
        chooseFont.addActionListener(e -> chooseFont());
        fontMenu.add(chooseFont);

        // Color menu
        JMenu colorMenu = new JMenu("Color");
        JMenuItem chooseColor = new JMenuItem("Choose Background Color");
        chooseColor.addActionListener(e -> chooseColor());
        colorMenu.add(chooseColor);

        menuBar.add(themeMenu);
        menuBar.add(fontMenu);
        menuBar.add(colorMenu);
        setJMenuBar(menuBar);
    }

    private void chooseFont() {
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        String font = (String) JOptionPane.showInputDialog(this, "Choose Font", "Font Selector",
                JOptionPane.PLAIN_MESSAGE, null, fonts, "Arial");

        if (font != null) {
            Font selectedFont = new Font(font, Font.PLAIN, 24);
            display.setFont(selectedFont);
            historyArea.setFont(new Font(font, Font.PLAIN, 14));
            for (Component comp : getContentPane().getComponents()) {
                if (comp instanceof JPanel) {
                    for (Component btn : ((JPanel) comp).getComponents()) {
                        if (btn instanceof JButton) {
                            btn.setFont(new Font(font, Font.BOLD, 18));
                        }
                    }
                }
            }
        }
    }

    private void chooseColor() {
        Color color = JColorChooser.showDialog(this, "Choose Background Color", getBackground());
        if (color != null) {
            display.setBackground(color);
            historyArea.setBackground(color);
            getContentPane().setBackground(color);
            for (Component comp : getContentPane().getComponents()) {
                if (comp instanceof JPanel) {
                    comp.setBackground(color);
                    for (Component btn : ((JPanel) comp).getComponents()) {
                        if (btn instanceof JButton && !btn.getBackground().equals(new Color(0x424242))
                                && !btn.getBackground().equals(new Color(0x009688))
                                && !btn.getBackground().equals(new Color(0xD32F2F))
                                && !btn.getBackground().equals(new Color(0x0288D1))
                                && !btn.getBackground().equals(new Color(0x333333))) {
                            btn.setBackground(color);
                        }
                    }
                }
            }
        }
    }

    private void setupKeyboardShortcuts() {
        KeyAdapter adapter = new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char ch = e.getKeyChar();
                if ("0123456789+-*/.= ".indexOf(ch) >= 0) {
                    // Thêm ký tự tại vị trí con trỏ
                    int caretPos = display.getCaretPosition();
                    expression.insert(caretPos, ch);
                    display.setText(formatDisplay(expression.toString()));
                    display.setCaretPosition(caretPos + 1);
                    isOperatorPressed = "+-*/".indexOf(ch) >= 0;
                    isPiPressed = false;
                    isEPressed = false;
                } else if (ch == '\n') { // Enter key
                    handleEquals();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    moveCaretLeft();
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    moveCaretRight();
                }
            }
        };
        display.addKeyListener(adapter);
    }

    private void moveCaretLeft() {
        int caretPos = display.getCaretPosition();
        if (caretPos > 0) {
            display.setCaretPosition(caretPos - 1);
        }
    }

    private void moveCaretRight() {
        int caretPos = display.getCaretPosition();
        if (caretPos < display.getText().length()) {
            display.setCaretPosition(caretPos + 1);
        }
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            String error;

            if (command.equals("Left")) {
                moveCaretLeft();
                return;
            }

            if (command.equals("Right")) {
                moveCaretRight();
                return;
            }

            if (command.equals("On/Off")) {
                togglePower();
                return;
            }

            if (command.equals("Theme")) {
                isDarkMode = !isDarkMode;
                ThemeManager.applyTheme(CalculatorUI.this, isDarkMode);
                return;
            }

            if (command.equals("Rad/Deg")) {
                isRadians = !isRadians;
                display.setText(isRadians ? "Radian" : "Degree");
                return;
            }

            if (command.equals("C")) {
                expression.setLength(0);
                display.setText("");
                isOperatorPressed = false;
                isPiPressed = false;
                isEPressed = false;
                return;
            }

            if (command.equals("DEL")) {
                int caretPos = display.getCaretPosition();
                if (caretPos > 0 && expression.length() > 0) {
                    expression.deleteCharAt(caretPos - 1);
                    display.setText(formatDisplay(expression.toString()));
                    display.setCaretPosition(caretPos - 1);
                }
                isPiPressed = false;
                isEPressed = false;
                return;
            }

            if (command.matches("[0-9\\.]")) {
                int caretPos = display.getCaretPosition();
                expression.insert(caretPos, command);
                display.setText(formatDisplay(expression.toString()));
                display.setCaretPosition(caretPos + 1);
                isOperatorPressed = false;
                isPiPressed = false;
                isEPressed = false;
                return;
            }

            if (command.equals("=")) {
                handleEquals();
                return;
            }

            if (command.equals("sin") || command.equals("cos") || command.equals("tan") || command.equals("cot")
                    || command.equals("√") || command.equals("log10") || command.equals("ln") || command.equals("1/x")
                    || command.equals("!")) {
                handleFunction(command);
                return;
            }

            if (command.equals("π")) {
                int caretPos = display.getCaretPosition();
                if (expression.length() > 0 && expression.toString().trim().matches(".*[0-9]$")) {
                    expression.insert(caretPos, " * π");
                    display.setCaretPosition(caretPos + 4);
                } else {
                    expression.insert(caretPos, "π");
                    display.setCaretPosition(caretPos + 1);
                }
                display.setText(formatDisplay(expression.toString()));
                isPiPressed = true;
                isEPressed = false;
                return;
            }

            if (command.equals("e")) {
                int caretPos = display.getCaretPosition();
                if (expression.length() > 0 && expression.toString().trim().matches(".*[0-9]$")) {
                    expression.insert(caretPos, " * e");
                    display.setCaretPosition(caretPos + 4);
                } else {
                    expression.insert(caretPos, "e");
                    display.setCaretPosition(caretPos + 1);
                }
                display.setText(formatDisplay(expression.toString()));
                isPiPressed = false;
                isEPressed = true;
                return;
            }

            if (command.equals("Ans")) {
                int caretPos = display.getCaretPosition();
                expression.insert(caretPos, String.valueOf(lastResult));
                display.setText(formatDisplay(expression.toString()));
                display.setCaretPosition(caretPos + String.valueOf(lastResult).length());
                isPiPressed = false;
                isEPressed = false;
                return;
            }

            // Operators
            if (command.equals("+") || command.equals("*") || command.equals("/") || command.equals("x^y")
                    || command.equals("logab") || command.equals("%")) {
                if (isOperatorPressed && !expression.toString().trim().endsWith("-")) {
                    display.setText("Syntax ERROR");
                    return;
                }
                String op = command.equals("x^y") ? "^" : command.equals("logab") ? "logab" : command;
                int caretPos = display.getCaretPosition();
                expression.insert(caretPos, " " + op + " ");
                display.setText(formatDisplay(expression.toString()));
                display.setCaretPosition(caretPos + op.length() + 2);
                isOperatorPressed = true;
                isPiPressed = false;
                isEPressed = false;
                return;
            }

            if (command.equals("-")) {
                int caretPos = display.getCaretPosition();
                if (expression.length() == 0 || expression.toString().trim().matches(".*[+\\-*/%^]$")) {
                    expression.insert(caretPos, "-");
                    display.setCaretPosition(caretPos + 1);
                } else {
                    if (isOperatorPressed) {
                        display.setText("Syntax ERROR");
                        return;
                    }
                    expression.insert(caretPos, " - ");
                    display.setCaretPosition(caretPos + 3);
                }
                display.setText(formatDisplay(expression.toString()));
                isOperatorPressed = true;
                isPiPressed = false;
                isEPressed = false;
            }
        }
    }

    private String formatDisplay(String expr) {
        return expr.replace(" * π", "π").replace(" * e", "e").replaceAll("\\s+", " ");
    }

    private void handleFunction(String func) {
        String error = CalculatorErrorHandler.checkExpressionError(expression.toString());
        if (error != null) {
            display.setText(error);
            return;
        }

        try {
            String evalExpression = expression.toString()
                    .replace("π", String.valueOf(Math.PI))
                    .replace("e", String.valueOf(Math.E));
            double number = evalExpression.trim().isEmpty() ? lastResult : Double.parseDouble(evalExpression.trim());
            double result = 0;
            String displayText;

            switch (func) {
                case "!":
                    error = CalculatorErrorHandler.checkFactorialError(number);
                    if (error != null) {
                        display.setText(error);
                        return;
                    }
                    result = factorial((int) number);
                    displayText = (int) number + "! = " + result;
                    break;
                case "sin":
                case "cos":
                case "tan":
                case "cot":
                    double angle = isRadians ? number : Math.toRadians(number);
                    error = CalculatorErrorHandler.checkTrigonometricError(func, angle);
                    if (error != null) {
                        display.setText(error);
                        return;
                    }
                    result = calculateTrig(func, angle);
                    displayText = func + "(" + formatDisplay(expression.toString()) + ") = " + result;
                    break;
                case "√":
                    error = CalculatorErrorHandler.checkSquareRootError(number);
                    if (error != null) {
                        display.setText(error);
                        return;
                    }
                    result = Math.sqrt(number);
                    displayText = "√(" + formatDisplay(expression.toString()) + ") = " + result;
                    break;
                case "log10":
                    error = CalculatorErrorHandler.checkLogarithmError(number);
                    if (error != null) {
                        display.setText(error);
                        return;
                    }
                    result = Math.log10(number);
                    displayText = "log10(" + formatDisplay(expression.toString()) + ") = " + result;
                    break;
                case "ln":
                    error = CalculatorErrorHandler.checkLogarithmError(number);
                    if (error != null) {
                        display.setText(error);
                        return;
                    }
                    result = Math.log(number);
                    displayText = "ln(" + formatDisplay(expression.toString()) + ") = " + result;
                    break;
                case "1/x":
                    error = CalculatorErrorHandler.checkInverseError(number);
                    if (error != null) {
                        display.setText(error);
                        return;
                    }
                    result = 1 / number;
                    displayText = "1/(" + formatDisplay(expression.toString()) + ") = " + result;
                    break;
                default:
                    display.setText("Syntax ERROR");
                    return;
            }

            lastResult = result;
            historyManager.addHistory(func + "(" + expression.toString() + ")", String.valueOf(result));
            expression.setLength(0);
            expression.append(result);
            display.setText(displayText);
            loadHistory();
            isOperatorPressed = false;
            isPiPressed = false;
            isEPressed = false;
        } catch (NumberFormatException e) {
            display.setText("Math ERROR");
        }
    }

    private void handleEquals() {
        String error = CalculatorErrorHandler.checkExpressionError(expression.toString());
        if (error != null) {
            display.setText(error);
            return;
        }

        try {
            String evalExpression = expression.toString()
                    .replace("π", String.valueOf(Math.PI))
                    .replace("e", String.valueOf(Math.E));
            String[] parts = evalExpression.trim().split("\\s+");

            if (parts.length == 1) {
                double result = Double.parseDouble(parts[0]);
                String displayText = formatDisplay(expression.toString()) + " = " + result;
                display.setText(displayText);
                historyManager.addHistory(expression.toString(), String.valueOf(result));
                lastResult = result;
                expression.setLength(0);
                expression.append(result);
                loadHistory();
                isOperatorPressed = false;
                isPiPressed = false;
                isEPressed = false;
                return;
            }

            if (parts.length < 3) {
                display.setText("Syntax ERROR");
                return;
            }

            double a = Double.parseDouble(parts[0]);
            String op = parts[1];
            double b = Double.parseDouble(parts[2]);

            error = CalculatorErrorHandler.checkBasicOperationError(a, b, op);
            if (error != null) {
                display.setText(error);
                return;
            }

            double result = performCalculation(a, b, op);
            String displayText = formatDisplay(expression.toString()) + " = " + result;

            historyManager.addHistory(expression.toString(), String.valueOf(result));
            lastResult = result;
            expression.setLength(0);
            expression.append(result);
            display.setText(displayText);
            loadHistory();
            isOperatorPressed = false;
            isPiPressed = false;
            isEPressed = false;
        } catch (Exception e) {
            display.setText("Math ERROR");
        }
    }

    private double performCalculation(double a, double b, String op) {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/": return a / b;
            case "^": return Math.pow(a, b);
            case "%": return a * b / 100;
            case "logab": return Math.log(b) / Math.log(a);
            default: throw new IllegalArgumentException("Invalid operator");
        }
    }

    private double calculateTrig(String func, double angle) {
        switch (func) {
            case "sin": return Math.sin(angle);
            case "cos": return Math.cos(angle);
            case "tan": return Math.tan(angle);
            case "cot": return 1 / Math.tan(angle);
            default: throw new IllegalArgumentException("Invalid function");
        }
    }

    private long factorial(int n) {
        if (n < 0) return -1;
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
            if (result < 0) return -1;
        }
        return result;
    }

    private String formatResult(double result) {
        if (Double.isInfinite(result) || Double.isNaN(result)) return "Math ERROR";
        if (result == (long) result) return String.valueOf((long) result);
        return String.format("%.10f", result).replaceAll("0+$", "").replaceAll("\\.$", "");
    }

    private void togglePower() {
        if (display.isEnabled()) {
            display.setText("");
            expression.setLength(0);
            display.setEnabled(false);
            for (Component comp : getContentPane().getComponents()) {
                if (comp instanceof JPanel) {
                    for (Component btn : ((JPanel) comp).getComponents()) {
                        if (btn instanceof JButton && !((JButton) btn).getText().equals("On/Off")) {
                            btn.setEnabled(false);
                        }
                    }
                }
            }
        } else {
            display.setEnabled(true);
            for (Component comp : getContentPane().getComponents()) {
                if (comp instanceof JPanel) {
                    for (Component btn : ((JPanel) comp).getComponents()) {
                        btn.setEnabled(true);
                    }
                }
            }
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
            JOptionPane.showMessageDialog(this, "Please enter a valid index to delete!");
        }
    }

    private void clearHistory() {
        historyManager.clearHistory();
        loadHistory();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HistoryManager historyManager = new HistoryManager();
            new CalculatorUI(historyManager).setVisible(true);
        });
    }
}