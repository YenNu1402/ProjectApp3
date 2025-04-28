package core;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class ScientificCalculator extends JFrame {
    private StringBuilder expression = new StringBuilder();
    private JTextField displayField;
    private JButton[] buttons;
    private String[] buttonLabels = {
        "On/Off", "Rad/Deg", "sin", "cos", "tan", "cot",
        "log10", "ln", "x^y", "π", "!", "logab",
        "7", "8", "9", "√", "C", "DEL",
        "4", "5", "6", "*", "+", "e",
        "1", "2", "3", "-", "/", "1/x",
        "0", ".", "%", "=", "Ans", ""
    };
    private int cols = 6;
    private int rows = 6;
    private double lastResult = 0;
    private boolean isRadians = true;
    private boolean isOn = true;
    private boolean isOperatorPressed = false;
    private boolean isPiPressed = false;
    private boolean isEPressed = false;

    public ScientificCalculator() {
        setTitle("Scientific Calculator");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        displayField = new JTextField();
        displayField.setEditable(false);
        displayField.setHorizontalAlignment(SwingConstants.RIGHT);
        displayField.setFont(new Font("Verdana", Font.PLAIN, 30));
        displayField.setBackground(new Color(0x1C1C1C));
        displayField.setForeground(Color.WHITE);
        displayField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(displayField, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(rows, cols, 5, 5));
        buttonPanel.setBackground(new Color(0x212121));

        buttons = new JButton[buttonLabels.length];
        for (int i = 0; i < buttonLabels.length; i++) {
            buttons[i] = new JButton(buttonLabels[i]);
            buttons[i].setFont(new Font("Arial", Font.PLAIN, 18));
            buttons[i].setFocusPainted(false);
            buttons[i].setBorderPainted(false);

            String command = buttonLabels[i];
            if (command.matches("[0-9\\.]")) {
                buttons[i].setBackground(new Color(0x424242));
                buttons[i].setForeground(Color.WHITE);
            } else if (command.matches("[+\\-*/=^%√]") || command.equals("1/x")) {
                buttons[i].setBackground(new Color(0x009688));
                buttons[i].setForeground(Color.WHITE);
            } else if (command.equals("C") || command.equals("DEL")) {
                buttons[i].setBackground(new Color(0xD32F2F));
                buttons[i].setForeground(Color.WHITE);
            } else if (command.equals("On/Off") || command.equals("Rad/Deg")) {
                buttons[i].setBackground(new Color(0x0288D1));
                buttons[i].setForeground(Color.WHITE);
            } else if (command.isEmpty()) {
                buttons[i].setBackground(new Color(0x212121));
                buttons[i].setEnabled(false);
            } else {
                buttons[i].setBackground(new Color(0x333333));
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
                    expression.setLength(0);
                    disableButtons();
                } else {
                    enableButtons();
                }
                return;
            }

            if (!isOn) return;

            try {
                switch (command) {
                    case "C":
                        expression.setLength(0);
                        displayField.setText("");
                        isOperatorPressed = false;
                        isPiPressed = false;
                        isEPressed = false;
                        break;

                    case "DEL":
                        if (expression.length() > 0) {
                            expression.setLength(expression.length() - 1);
                            displayField.setText(formatDisplay(expression.toString()));
                        }
                        isPiPressed = false;
                        isEPressed = false;
                        break;

                    case "+": case "*": case "/": case "x^y": case "logab": case "%":
                        if (isOperatorPressed && !expression.toString().trim().endsWith("-")) {
                            displayField.setText("Syntax ERROR");
                            return;
                        }
                        expression.append(" ").append(command.equals("x^y") ? "^" : command).append(" ");
                        displayField.setText(formatDisplay(expression.toString()));
                        isOperatorPressed = true;
                        isPiPressed = false;
                        isEPressed = false;
                        break;

                    case "-":
                        // Allow unary minus at start or after another operator
                        if (expression.length() == 0 || expression.toString().trim().matches(".*[+\\-*/%^]$")) {
                            expression.append("-");
                        } else {
                            if (isOperatorPressed) {
                                displayField.setText("Syntax ERROR");
                                return;
                            }
                            expression.append(" - ");
                        }
                        displayField.setText(formatDisplay(expression.toString()));
                        isOperatorPressed = true;
                        isPiPressed = false;
                        isEPressed = false;
                        break;

                    case "!": case "sin": case "cos": case "tan": case "cot": 
                    case "√": case "log10": case "ln": case "1/x":
                        handleFunction(command);
                        break;

                    case "=":
                        handleEquals();
                        break;

                    case "π":
                        if (expression.length() > 0 && expression.toString().trim().matches(".*[0-9]$")) {
                            expression.append(" * π");
                        } else {
                            expression.append("π");
                        }
                        displayField.setText(formatDisplay(expression.toString()));
                        isPiPressed = true;
                        isEPressed = false;
                        break;

                    case "e":
                        if (expression.length() > 0 && expression.toString().trim().matches(".*[0-9]$")) {
                            expression.append(" * e");
                        } else {
                            expression.append("e");
                        }
                        displayField.setText(formatDisplay(expression.toString()));
                        isPiPressed = false;
                        isEPressed = true;
                        break;

                    case "Ans":
                        expression.append(lastResult);
                        displayField.setText(formatDisplay(expression.toString()));
                        isPiPressed = false;
                        isEPressed = false;
                        break;

                    case "Rad/Deg":
                        isRadians = !isRadians;
                        displayField.setText(isRadians ? "Radian" : "Degree");
                        break;

                    default: // Numbers and decimal point
                        expression.append(command);
                        displayField.setText(formatDisplay(expression.toString()));
                        isOperatorPressed = false;
                        isPiPressed = false;
                        isEPressed = false;
                        break;
                }
            } catch (Exception ex) {
                displayField.setText("Math ERROR");
            }
        }
    }

    // Format the display to show π and e without * when appropriate
    private String formatDisplay(String expr) {
        return expr.replace(" * π", "π").replace(" * e", "e").replaceAll("\\s+", " ");
    }

    private void handleFunction(String func) {
        String error = CalculatorErrorHandler.checkExpressionError(expression.toString());
        if (error != null) {
            displayField.setText(error);
            return;
        }

        try {
            // Replace π and e with their numerical values for evaluation
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
                        displayField.setText(error);
                        return;
                    }
                    result = factorial((int) number);
                    displayText = (int) number + "! = " + result;
                    break;

                case "sin": case "cos": case "tan": case "cot":
                    double angle = isRadians ? number : Math.toRadians(number);
                    error = CalculatorErrorHandler.checkTrigonometricError(func, angle);
                    if (error != null) {
                        displayField.setText(error);
                        return;
                    }
                    result = calculateTrig(func, angle);
                    displayText = func + "(" + formatDisplay(expression.toString()) + ") = " + result;
                    break;

                case "√":
                    error = CalculatorErrorHandler.checkSquareRootError(number);
                    if (error != null) {
                        displayField.setText(error);
                        return;
                    }
                    result = Math.sqrt(number);
                    displayText = "√(" + formatDisplay(expression.toString()) + ") = " + result;
                    break;

                case "log10":
                    error = CalculatorErrorHandler.checkLogarithmError(number);
                    if (error != null) {
                        displayField.setText(error);
                        return;
                    }
                    result = Math.log10(number);
                    displayText = "log10(" + formatDisplay(expression.toString()) + ") = " + result;
                    break;

                case "ln":
                    error = CalculatorErrorHandler.checkLogarithmError(number);
                    if (error != null) {
                        displayField.setText(error);
                        return;
                    }
                    result = Math.log(number);
                    displayText = "ln(" + formatDisplay(expression.toString()) + ") = " + result;
                    break;

                case "1/x":
                    error = CalculatorErrorHandler.checkInverseError(number);
                    if (error != null) {
                        displayField.setText(error);
                        return;
                    }
                    result = 1 / number;
                    displayText = "1/(" + formatDisplay(expression.toString()) + ") = " + result;
                    break;

                default:
                    displayField.setText("Syntax ERROR");
                    return;
            }

            lastResult = result;
            expression.setLength(0);
            expression.append(result);
            displayField.setText(displayText);
            isOperatorPressed = false;
            isPiPressed = false;
            isEPressed = false;
        } catch (NumberFormatException e) {
            displayField.setText("Math ERROR");
        }
    }

    private void handleEquals() {
        String error = CalculatorErrorHandler.checkExpressionError(expression.toString());
        if (error != null) {
            displayField.setText(error);
            return;
        }

        try {
            // Replace π and e with their numerical values for evaluation
            String evalExpression = expression.toString()
                .replace("π", String.valueOf(Math.PI))
                .replace("e", String.valueOf(Math.E));
            String[] parts = evalExpression.trim().split("\\s+");

            // Handle single number or constant
            if (parts.length == 1) {
                double result = Double.parseDouble(parts[0]);
                displayField.setText(formatDisplay(expression.toString()) + " = " + result);
                lastResult = result;
                expression.setLength(0);
                expression.append(result);
                isOperatorPressed = false;
                isPiPressed = false;
                isEPressed = false;
                return;
            }

            // Handle binary operation
            if (parts.length < 3) {
                displayField.setText("Syntax ERROR");
                return;
            }

            double a = Double.parseDouble(parts[0]);
            String op = parts[1];
            double b = Double.parseDouble(parts[2]);

            error = CalculatorErrorHandler.checkBasicOperationError(a, b, op);
            if (error != null) {
                displayField.setText(error);
                return;
            }

            double result = performCalculation(a, b, op);
            String displayText = formatDisplay(expression.toString()) + " = " + result;

            lastResult = result;
            expression.setLength(0);
            expression.append(result);
            displayField.setText(displayText);
            isOperatorPressed = false;
            isPiPressed = false;
            isEPressed = false;
        } catch (Exception e) {
            displayField.setText("Math ERROR");
        }
    }

    private double performCalculation(double a, double b, String op) {
        switch (op) {
            case "+": 
                return a + (op.equals("%") ? a * b / 100 : b);
            case "-": 
                return a - (op.equals("%") ? a * b / 100 : b);
            case "*": return a * b;
            case "/": return a / b;
            case "%": return a * b / 100;
            case "^": return Math.pow(a, b);
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
            default: throw new IllegalArgumentException("Invalid trig function");
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
        SwingUtilities.invokeLater(ScientificCalculator::new);
    }
}