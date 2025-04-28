package ui;

import java.awt.*;
import javax.swing.*;

public class ThemeManager {
    private static final Color LIGHT_BG_COLOR = Color.WHITE;
    private static final Color DARK_BG_COLOR = Color.DARK_GRAY;
    private static final Color LIGHT_TEXT_COLOR = Color.BLACK;
    private static final Color DARK_TEXT_COLOR = Color.WHITE;

    public static void applyTheme(JFrame frame, boolean isDarkMode) {
        Color bgColor = isDarkMode ? DARK_BG_COLOR : LIGHT_BG_COLOR;
        Color fgColor = isDarkMode ? DARK_TEXT_COLOR : LIGHT_TEXT_COLOR;

        frame.getContentPane().setBackground(bgColor);
        applyThemeToComponents(frame.getContentPane(), bgColor, fgColor);
    }

    private static void applyThemeToComponents(Container container, Color bgColor, Color fgColor) {
        for (Component component : container.getComponents()) {
            if (component instanceof JTextField || component instanceof JTextArea) {
                component.setBackground(bgColor);
                component.setForeground(fgColor);
            } else if (component instanceof JButton) {
                // Giữ màu nút như trong CalculatorUI
                String text = ((JButton) component).getText();
                if (text.matches("[0-9\\.]")) {
                    component.setBackground(new Color(0x424242));
                    component.setForeground(Color.WHITE);
                } else if (text.matches("[+\\-*/=^%√]") || text.equals("1/x")) {
                    component.setBackground(new Color(0x009688));
                    component.setForeground(Color.WHITE);
                } else if (text.equals("C") || text.equals("DEL")) {
                    component.setBackground(new Color(0xD32F2F));
                    component.setForeground(Color.WHITE);
                } else if (text.equals("On/Off") || text.equals("Rad/Deg") || text.equals("Theme")) {
                    component.setBackground(new Color(0x0288D1));
                    component.setForeground(Color.WHITE);
                } else {
                    component.setBackground(new Color(0x333333));
                    component.setForeground(Color.WHITE);
                }
            } else if (component instanceof JPanel) {
                component.setBackground(bgColor);
                applyThemeToComponents((JPanel) component, bgColor, fgColor);
            } else if (component instanceof JScrollPane) {
                component.setBackground(bgColor);
                applyThemeToComponents((JScrollPane) component, bgColor, fgColor);
            }
        }
    }
}