import javax.swing.*;

public class MainMenu {
    private JPanel mainPanel;
    private JButton customerAccountManagementButton;
    private JButton transactionManagementButton;
    private JButton transactionHistoryButton;
    private JFrame frame;

    public MainMenu(JFrame frame) {
        this.frame = frame;

        customerAccountManagementButton.addActionListener(e -> {
            new CustomerAccountWindow(frame).setVisible(true);
            frame.setVisible(false);
        });

        transactionManagementButton.addActionListener(e -> {
            new TransactionWindow(frame).setVisible(true);
            frame.setVisible(false);
        });

        transactionHistoryButton.addActionListener(e -> {
            HistoryWindow historyWin = new HistoryWindow(frame);

            historyWin.refreshHistory();

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