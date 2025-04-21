import ui.CalculatorUI;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            CalculatorUI calc = new CalculatorUI();
            calc.setVisible(true);
        });
    }
}

