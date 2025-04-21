package ui;


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

