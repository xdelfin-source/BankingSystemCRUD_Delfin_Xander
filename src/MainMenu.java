import javax.swing.*;

public class MainMenu {
    private JPanel mainPanel;
    private JButton customerAccountManagementButton;
    private JButton transactionManagementButton;
    private JButton transactionHistoryButton;
    private JFrame frame;

    public MainMenu(JFrame frame) {
        this.frame = frame;

        // 1. Customer & Account Management
        customerAccountManagementButton.addActionListener(e -> {
            new CustomerAccountWindow(frame).setVisible(true);
            frame.setVisible(false);
        });

        // 2. Transaction Management (Deposits/Withdrawals)
        transactionManagementButton.addActionListener(e -> {
            new TransactionWindow(frame).setVisible(true);
            frame.setVisible(false);
        });

        // 3. Transaction History - FIXED TO REFRESH DATA
        transactionHistoryButton.addActionListener(e -> {
            // Create the window instance
            HistoryWindow historyWin = new HistoryWindow(frame);

            // Call the refresh method to pull the latest transactions from SQL
            historyWin.refreshHistory();

            // Now show it
            historyWin.setVisible(true);
            frame.setVisible(false);
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Simple Banking System - Main Menu");
        MainMenu menu = new MainMenu(frame);

        frame.setContentPane(menu.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 450);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}