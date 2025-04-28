

import history.HistoryManager;
import javax.swing.SwingUtilities;
import ui.CalculatorUI;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                HistoryManager historyManager = new HistoryManager();
                
                CalculatorUI calculatorUI = new CalculatorUI(historyManager);

                calculatorUI.setVisible(true);
            } catch (Exception e) {
                System.err.println("Error initializing calculator: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}