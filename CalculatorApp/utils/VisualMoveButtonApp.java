package utils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VisualMoveButtonApp {
    private int xPos = 150; // Vị trí ban đầu của hình chữ nhật

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VisualMoveButtonApp().createAndShowGUI());
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Di Chuyển Trái Phải");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLUE);
                g.fillRect(xPos, 100, 50, 50); // Vẽ hình chữ nhật
            }
        };

        JButton leftButton = new JButton("Trái");
        JButton rightButton = new JButton("Phải");

        leftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xPos -= 10; // Di chuyển sang trái
                panel.repaint(); // Vẽ lại giao diện
            }
        });

        rightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xPos += 10; // Di chuyển sang phải
                panel.repaint(); // Vẽ lại giao diện
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(leftButton);
        buttonPanel.add(rightButton);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
}

