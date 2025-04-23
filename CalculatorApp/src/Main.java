<<<<<<< HEAD
<<<<<<< HEAD
package src;

import ui.CalculatorUI;
import history.HistoryManager;

public class Main {
    public static void main(String[] args) {
        HistoryManager historyManager = new HistoryManager();
        CalculatorUI calculatorUI = new CalculatorUI(historyManager);
        calculatorUI.setVisible(true);
=======
import ui.AdvancedCalculatorUI;
public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(()-> new AdvancedCalculatorUI().createUI());
>>>>>>> MT-3
    }
}
=======
import ui.CalculatorUI;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            CalculatorUI calc = new CalculatorUI();
            calc.setVisible(true);
        });
    }
}

>>>>>>> MT-5
