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
        setSize(600, 500); // Set your preferred size
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // ONLY ONE ACTION LISTENER HERE
        btnProcess.addActionListener(e -> {
            // 1. Get and trim the text
            String rawId = txtAccountId.getText().trim();
            String rawAmount = txtAmount.getText().trim();

            // 2. Initial Empty Check
            if (rawId.isEmpty() || rawAmount.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Wait! One of the boxes is empty.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return; // Stop the code here
            }

            try {
                // 3. Convert to numbers
                int id = Integer.parseInt(rawId);
                double amount = Double.parseDouble(rawAmount);
                String type = cbTransactionType.getSelectedItem().toString();

                // 4. Call the database logic
                String result = BankingOperations.processTransaction(id, type, amount);

                // 5. Handle the result
                if (result.startsWith("Transaction Successful")) {
                    JOptionPane.showMessageDialog(this, result, "Success", JOptionPane.INFORMATION_MESSAGE);

                    // Clear the fields ONLY after the successful message is acknowledged
                    txtAmount.setText("");
                    txtAccountId.setText("");
                } else {
                    // This handles things like "Insufficient Funds" or "Account Not Found"
                    JOptionPane.showMessageDialog(this, result, "Transaction Failed", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers (no letters or symbols).", "Format Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Back Button
        btnBack.addActionListener(e -> {
            if (parentFrame != null) parentFrame.setVisible(true);
            this.dispose();
        });
    }
}