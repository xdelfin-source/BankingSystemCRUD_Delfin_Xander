import javax.swing.*;

public class TransactionWindow extends JFrame {
    private JPanel mainPanel;
    private JTextField txtAccountId;
    private JComboBox<String> cbTransactionType;
    private JTextField txtAmount;
    private JButton btnProcess;
    private JButton btnBack;
    private JFrame parentFrame;

    public TransactionWindow(JFrame parent) {
        this.parentFrame = parent;

        setTitle("Transaction Management");
        setContentPane(mainPanel);
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        btnProcess.addActionListener(e -> {
            String rawId = txtAccountId.getText().trim();
            String rawAmount = txtAmount.getText().trim();

            if (rawId.isEmpty() || rawAmount.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Wait! One of the boxes is empty.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int id = Integer.parseInt(rawId);
                double amount = Double.parseDouble(rawAmount);
                String type = cbTransactionType.getSelectedItem().toString();

                String result = BankingOperations.processTransaction(id, type, amount);

                if (result.startsWith("Transaction Successful")) {
                    JOptionPane.showMessageDialog(this, result, "Success", JOptionPane.INFORMATION_MESSAGE);

                    txtAmount.setText("");
                    txtAccountId.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, result, "Transaction Failed", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers (no letters or symbols).", "Format Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnBack.addActionListener(e -> {
            if (parentFrame != null) parentFrame.setVisible(true);
            this.dispose();
        });
    }
}