package ui;
import javax.swing.*;
import java.awt.*;

public class SimpleCalculatorUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SimpleCalculatorUI().createUI());
    }

    private void createUI() {
        JFrame frame = new JFrame("Simple Calculator UI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 400);
        frame.setLocationRelativeTo(null); // căn giữa cửa sổ

        // Khung chính
        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Ô hiển thị (giống như màn hình máy tính)
        JTextField displayField = new JTextField();
        displayField.setFont(new Font("Arial", Font.PLAIN, 24));
        displayField.setHorizontalAlignment(JTextField.RIGHT);
        displayField.setEditable(false);
        mainPanel.add(displayField, BorderLayout.NORTH);

        // Bảng nút
        JPanel buttonPanel = new JPanel(new GridLayout(4, 4, 5, 5));
        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+"
        };

        for (String text : buttons) {
            JButton btn = new JButton(text);
            btn.setFont(new Font("Arial", Font.BOLD, 20));
            buttonPanel.add(btn);
        }

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }
}
