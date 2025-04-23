package core;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class ScientificCalculator1 extends JFrame {
    private JTextField displayField;
    private JButton[] buttons;
    private String[] buttonLabels = {
        "On/Off", "Rad/Deg", "sin", "cos", "tan", "cot",
        "log10", "ln", "x^y","π",   "!", "logab",
        "7", "8", "9","√","C", "DEL", 
        "4", "5", "6", "*", "+", "e",
        "1", "2", "3", "-", "/", "1/x",
        "0", ".", "%", "=", "Ans", "" // Thêm nút Ans, để lại 1 nút trống
    };
    private int cols = 6;
    private int rows = 6;
    private double lastResult; // Lưu kết quả trước đó cho nút Ans
    private boolean isRadians = true;
    private boolean isOn = true;
    private boolean afterResult = false;

    public ScientificCalculator1() {
        setTitle("Scientific Calculator");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Phần hiển thị kết quả
        displayField = new JTextField();
        displayField.setEditable(false);
        displayField.setHorizontalAlignment(SwingConstants.RIGHT);
        displayField.setFont(new Font("Verdana", Font.PLAIN, 30));
        displayField.setBackground(new Color(0x1C1C1C));
        displayField.setForeground(Color.WHITE);
        displayField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(displayField, BorderLayout.NORTH);

        // Tạo panel cho các nút
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(rows, cols, 5, 5));
        buttonPanel.setBackground(new Color(0x212121));

        // Tạo các nút
        buttons = new JButton[buttonLabels.length];
        for (int i = 0; i < buttonLabels.length; i++) {
            buttons[i] = new JButton(buttonLabels[i]);
            buttons[i].setFont(new Font("Arial", Font.PLAIN, 18));
            buttons[i].setFocusPainted(false);
            buttons[i].setBorderPainted(false);

            // Tùy chỉnh màu sắc theo loại nút
            String command = buttonLabels[i];
            if (command.matches("[0-9\\.]")) {
                buttons[i].setBackground(new Color(0x424242)); // Màu xám cho số
                buttons[i].setForeground(Color.WHITE);
            } else if (command.matches("[+\\-*/=^%√]") || command.equals("1/x")) {
                buttons[i].setBackground(new Color(0x009688)); // Màu xanh teal cho toán tử
                buttons[i].setForeground(Color.WHITE);
            } else if (command.equals("C") || command.equals("DEL")) {
                buttons[i].setBackground(new Color(0xD32F2F)); // Màu đỏ cho C và DEL
                buttons[i].setForeground(Color.WHITE);
            } else if (command.equals("On/Off") || command.equals("Rad/Deg")) {
                buttons[i].setBackground(new Color(0x0288D1)); // Màu xanh dương cho On/Off và Rad/Deg
                buttons[i].setForeground(Color.WHITE);
            } else if (command.isEmpty()) {
                buttons[i].setBackground(new Color(0x212121)); // Màu nền cho nút trống
                buttons[i].setEnabled(false);
            } else {
                buttons[i].setBackground(new Color(0x333333)); // Màu xám đậm cho hàm
                buttons[i].setForeground(Color.WHITE);
            }

            buttons[i].addActionListener(new ButtonClickListener());
            buttonPanel.add(buttons[i]);
        }

        add(buttonPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals("On/Off")) {
                isOn = !isOn;
                if (!isOn) {
                    displayField.setText("");
                    disableButtons();
                } else {
                    enableButtons();
                }
                return;
            }

            if (!isOn) return;

            try {
                if (command.matches("[0-9\\.]")) {
                    if (afterResult) {
                        displayField.setText("");
                        afterResult = false;
                    }
                    displayField.setText(displayField.getText() + command);
                } else if (command.equals("C")) {
                    displayField.setText("");
                    afterResult = false;
                } else if (command.equals("DEL")) {
                    String currentText = displayField.getText();
                    if (!currentText.isEmpty()) {
                        displayField.setText(currentText.substring(0, currentText.length() - 1));
                    }
                } else if (command.equals("+") || command.equals("-") || command.equals("*") || command.equals("/") || command.equals("x^y") || command.equals("logab")) {
                    String current = displayField.getText().trim();
                    String[] parts = current.split("\\s+");
                    String displayOp = command.equals("x^y") ? "^" : command;
                    if (parts.length == 3) {
                        try {
                            double a = Double.parseDouble(parts[0]);
                            String op = parts[1];
                            double b = Double.parseDouble(parts[2]);
                            double result = performCalculation(a, b, op);
                            displayField.setText(String.valueOf(result) + " " + displayOp + " ");
                        } catch (Exception ex) {
                            displayField.setText(current + " " + displayOp + " ");
                        }
                    } else {
                        displayField.setText(current + " " + displayOp + " ");
                    }
                } else if (command.equals("=")) {
                    String current = displayField.getText().trim();
                    String[] parts = current.split("\\s+");
                    if (parts.length == 3) {
                        try {
                            double a = Double.parseDouble(parts[0]);
                            String op = parts[1];
                            double b = Double.parseDouble(parts[2]);
                            double result = performCalculation(a, b, op);
                            displayField.setText(String.valueOf(result));
                            lastResult = result;
                            afterResult = true;
                        } catch (Exception ex) {
                            displayField.setText("Error");
                        }
                    } else {
                        displayField.setText("Error");
                    }
                } else if (command.equals("sin") || command.equals("cos") || command.equals("tan") || command.equals("cot")) {
                    try {
                        double number = Double.parseDouble(displayField.getText());
                        double result = calculateTrig(command, number);
                        displayField.setText(String.valueOf(result));
                        lastResult = result;
                        afterResult = true;
                    } catch (NumberFormatException ex) {
                        displayField.setText("Error");
                    }
                } else if (command.equals("log10")) {
                    try {
                        double number = Double.parseDouble(displayField.getText());
                        if (number > 0) {
                            double result = Math.log10(number);
                            displayField.setText(String.valueOf(result));
                            lastResult = result;
                            afterResult = true;
                        } else {
                            displayField.setText("Error");
                        }
                    } catch (NumberFormatException ex) {
                        displayField.setText("Error");
                    }
                } else if (command.equals("ln")) {
                    try {
                        double number = Double.parseDouble(displayField.getText());
                        if (number > 0) {
                            double result = Math.log(number);
                            displayField.setText(String.valueOf(result));
                            lastResult = result;
                            afterResult = true;
                        } else {
                            displayField.setText("Error");
                        }
                    } catch (NumberFormatException ex) {
                        displayField.setText("Error");
                    }
                } else if (command.equals("!")) {
                    try {
                        double number = Double.parseDouble(displayField.getText());
                        int intNumber = (int) number;
                        if (intNumber >= 0) {
                            long result = factorial(intNumber);
                            displayField.setText(String.valueOf(result));
                            lastResult = result;
                            afterResult = true;
                        } else {
                            displayField.setText("Error");
                        }
                    } catch (NumberFormatException ex) {
                        displayField.setText("Error");
                    }
                } else if (command.equals("√")) {
                    try {
                        double number = Double.parseDouble(displayField.getText());
                        if (number >= 0) {
                            double result = Math.sqrt(number);
                            displayField.setText(String.valueOf(result));
                            lastResult = result;
                            afterResult = true;
                        } else {
                            displayField.setText("Error");
                        }
                    } catch (NumberFormatException ex) {
                        displayField.setText("Error");
                    }
                } else if (command.equals("1/x")) {
                    try {
                        double number = Double.parseDouble(displayField.getText());
                        if (number != 0) {
                            double result = 1 / number;
                            displayField.setText(String.valueOf(result));
                            lastResult = result;
                            afterResult = true;
                        } else {
                            displayField.setText("Error");
                        }
                    } catch (NumberFormatException ex) {
                        displayField.setText("Error");
                    }
                } else if (command.equals("%")) {
                    try {
                        double number = Double.parseDouble(displayField.getText());
                        double result = number / 100;
                        displayField.setText(String.valueOf(result));
                        lastResult = result;
                        afterResult = true;
                    } catch (NumberFormatException ex) {
                        displayField.setText("Error");
                    }
                } else if (command.equals("π")) {
                    displayField.setText(String.valueOf(Math.PI));
                    lastResult = Math.PI;
                    afterResult = true;
                } else if (command.equals("e")) {
                    displayField.setText(String.valueOf(Math.E));
                    lastResult = Math.E;
                    afterResult = true;
                } else if (command.equals("Ans")) {
                    displayField.setText(String.valueOf(lastResult));
                    afterResult = true;
                } else if (command.equals("Rad/Deg")) {
                    isRadians = !isRadians;
                    displayField.setText(isRadians ? "Radian" : "Degree");
                }
            } catch (Exception ex) {
                displayField.setText("Error");
            }
        }
    }

    private double performCalculation(double a, double b, String op) {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/": 
                if (b == 0) throw new ArithmeticException("Divide by zero");
                return a / b;
            case "^": return Math.pow(a, b);
            case "logab": 
                if (a > 0 && a != 1 && b > 0) {
                    return Math.log(b) / Math.log(a);
                } else {
                    throw new ArithmeticException("Invalid logab");
                }
            default: return 0;
        }
    }

    private double calculateTrig(String func, double angle) {
        try {
            if (!isRadians) {
                angle = Math.toRadians(angle);
            }
            switch (func) {
                case "sin": return Math.sin(angle);
                case "cos": return Math.cos(angle);
                case "tan": return Math.tan(angle);
                case "cot":
                    double tanVal = Math.tan(angle);
                    if (tanVal == 0) throw new ArithmeticException("Cot undefined");
                    return 1 / tanVal;
                default: return 0;
            }
        } catch (ArithmeticException ex) {
            displayField.setText("Error");
            return 0;
        }
    }

    private long factorial(int n) {
        if (n < 0) throw new IllegalArgumentException("Negative number");
        if (n == 0) return 1;
        return n * factorial(n - 1);
    }

    private void disableButtons() {
        for (JButton button : buttons) {
            if (!button.getText().equals("On/Off")) {
                button.setEnabled(false);
            }
        }
        displayField.setEnabled(false);
    }

    private void enableButtons() {
        for (JButton button : buttons) {
            if (!button.getText().isEmpty()) {
                button.setEnabled(true);
            }
        }
        displayField.setEnabled(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ScientificCalculator1());
    }
}