package ui;

import javax.swing.*;
import java.awt.*;

public class ThemeManager {
    
    private static final Color LIGHT_BG_COLOR = Color.WHITE;
    private static final Color DARK_BG_COLOR = Color.DARK_GRAY;
    private static final Color LIGHT_TEXT_COLOR = Color.BLACK;
    private static final Color DARK_TEXT_COLOR = Color.WHITE;

    public static void applyTheme(JFrame frame, boolean isDarkMode) {
        if (isDarkMode) {
            frame.getContentPane().setBackground(DARK_BG_COLOR);
            for (Component component : frame.getContentPane().getComponents()) {
                if (component instanceof JTextField) {
                    component.setBackground(DARK_BG_COLOR);
                    component.setForeground(DARK_TEXT_COLOR);
                } else if (component instanceof JButton) {
                    component.setBackground(DARK_BG_COLOR);
                    component.setForeground(DARK_TEXT_COLOR);
                }
            }
        } else {
            frame.getContentPane().setBackground(LIGHT_BG_COLOR);
            for (Component component : frame.getContentPane().getComponents()) {
                if (component instanceof JTextField) {
                    component.setBackground(LIGHT_BG_COLOR);
                    component.setForeground(LIGHT_TEXT_COLOR);
                } else if (component instanceof JButton) {
                    component.setBackground(LIGHT_BG_COLOR);
                    component.setForeground(LIGHT_TEXT_COLOR);
                }
            }
        }
    }
}
