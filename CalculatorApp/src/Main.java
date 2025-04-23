package src;

import ui.CalculatorUI;
import history.HistoryManager;

public class Main {
    public static void main(String[] args) {
        HistoryManager historyManager = new HistoryManager();
        CalculatorUI calculatorUI = new CalculatorUI(historyManager);
        calculatorUI.setVisible(true);
    }
}