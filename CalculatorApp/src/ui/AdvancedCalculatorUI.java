package ui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdvancedCalculatorUI {
    private JFrame frame;
    private JTextField displayField;
    private JPanel buttonPanel;
    private boolean isDarkMode = false;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdvancedCalculatorUI().createUI());
    }

    public void createUI() {
        frame = new JFrame("Calculator UI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 500);
        frame.setLocationRelativeTo(null);

        displayField = new JTextField();
        displayField.setFont(new Font("Arial", Font.PLAIN, 24));
        displayField.setHorizontalAlignment(JTextField.RIGHT);
        displayField.setEditable(false);
        displayField.setFocusable(true);

        frame.setLayout(new BorderLayout(5, 5));
        frame.add(displayField, BorderLayout.NORTH);

        createButtons();
        createMenu();

        frame.setVisible(true);
        setupKeyboardShortcuts();
    }

    private void createButtons() {
        buttonPanel = new JPanel(new GridLayout(4, 4, 5, 5));
        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+"
        };

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.BOLD, 20));
            button.addActionListener(e -> displayField.setText(displayField.getText() + text));
            buttonPanel.add(button);
        }

        frame.add(buttonPanel, BorderLayout.CENTER);
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();

        // Dark/Light
        JMenu themeMenu = new JMenu("Dark/Ligh");
        JMenuItem toggleTheme = new JMenuItem("Toggle Dark/Light Mode");
        toggleTheme.addActionListener(e -> toggleTheme());
        themeMenu.add(toggleTheme);

        // Font
        JMenu fontMenu = new JMenu("Font");
        JMenuItem chooseFont = new JMenuItem("Choose Font");
        chooseFont.addActionListener(e -> chooseFont());
        fontMenu.add(chooseFont);

        // Color
        JMenu colorMenu = new JMenu("Color");
        JMenuItem chooseColor = new JMenuItem("Choose Background Color");
        chooseColor.addActionListener(e -> chooseColor());
        colorMenu.add(chooseColor);

        menuBar.add(themeMenu);
        menuBar.add(fontMenu);
        menuBar.add(colorMenu);
        frame.setJMenuBar(menuBar);
    }

    private void toggleTheme() {
        isDarkMode = !isDarkMode;

        Color bg = isDarkMode ? Color.DARK_GRAY : Color.WHITE;
        Color fg = isDarkMode ? Color.WHITE : Color.BLACK;

        displayField.setBackground(bg);
        displayField.setForeground(fg);

        for (Component comp : buttonPanel.getComponents()) {
            if (comp instanceof JButton) {
                comp.setBackground(bg);
                comp.setForeground(fg);
            }
        }

        buttonPanel.setBackground(bg);
        frame.getContentPane().setBackground(bg);
    }

    private void chooseFont() {
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        String font = (String) JOptionPane.showInputDialog(frame, "Choose Font", "Font Selector",
                JOptionPane.PLAIN_MESSAGE, null, fonts, "Arial");

        if (font != null) {
            Font selectedFont = new Font(font, Font.PLAIN, 24);
            displayField.setFont(selectedFont);
            for (Component comp : buttonPanel.getComponents()) {
                if (comp instanceof JButton) {
                    comp.setFont(selectedFont);
                }
            }
        }
    }

    private void chooseColor() {
        Color color = JColorChooser.showDialog(frame, "Choose Background Color", frame.getBackground());
        if (color != null) {
            displayField.setBackground(color);
            for (Component comp : buttonPanel.getComponents()) {
                if (comp instanceof JButton) {
                    comp.setBackground(color);
                }
            }
            buttonPanel.setBackground(color);
            frame.getContentPane().setBackground(color);
        }
    }

    private void setupKeyboardShortcuts() {
        KeyAdapter adapter = new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char ch = e.getKeyChar();
                if ("0123456789+-*/.= ".indexOf(ch) >= 0) {
                    displayField.setText(displayField.getText() + ch);
                }
            }
        };
        displayField.addKeyListener(adapter);
    }
}