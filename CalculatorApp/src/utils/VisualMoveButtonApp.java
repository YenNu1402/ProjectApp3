package utils;

import java.awt.*;
import javax.swing.*;

public class VisualMoveButtonApp {
    private int xPos = 150; // Vị trí ban đầu của hình chữ nhật
    private JFrame frame;

    public void show() {
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        frame = new JFrame("Move Rectangle");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Đóng cửa sổ này mà không thoát ứng dụng
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLUE);
                g.fillRect(xPos, 100, 50, 50); // Vẽ hình chữ nhật
            }
        };

        JButton leftButton = new JButton("Left");
        JButton rightButton = new JButton("Right");

        leftButton.addActionListener(e -> {
            xPos -= 10; // Di chuyển sang trái
            panel.repaint();
        });

        rightButton.addActionListener(e -> {
            xPos += 10; // Di chuyển sang phải
            panel.repaint();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(leftButton);
        buttonPanel.add(rightButton);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
}