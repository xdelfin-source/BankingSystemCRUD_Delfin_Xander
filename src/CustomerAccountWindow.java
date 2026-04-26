import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CustomerAccountWindow extends JFrame {
    private JFrame parentFrame; // 1. Added to store the Main Menu
    private JPanel mainPanel;
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JComboBox<String> cbAccountType;
    private JButton btnAdd;
    private JTextField txtSearch;
    private JTable accountTable;
    private JButton deleteAccountButton;
    private JButton btnBack;

    public CustomerAccountWindow(JFrame parent) {
        this.parentFrame = parent;

        setTitle("Customer & Account Management");
        setContentPane(mainPanel);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        btnAdd.addActionListener(e -> {
            Object selectedType = cbAccountType.getSelectedItem();
            if (selectedType == null) {
                JOptionPane.showMessageDialog(this, "Please select an Account Type!");
                return;
            }

            String fName = txtFirstName.getText();
            String lName = txtLastName.getText();
            String email = txtEmail.getText();
            String phone = txtPhone.getText();
            String type = selectedType.toString();

            if (BankingOperations.addCustomerAccount(fName, lName, email, phone, type)) {
                JOptionPane.showMessageDialog(this, "Account Created Successfully!");
                refreshTable("");
                txtFirstName.setText(""); txtLastName.setText("");
                txtEmail.setText(""); txtPhone.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Error: Check database connection.");
            }
        });

        deleteAccountButton.addActionListener(e -> {
            int selectedRow = accountTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select an account from the table first!");
                return;
            }

            int accountId = (int) accountTable.getValueAt(selectedRow, 0);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete Account ID: " + accountId + "?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (BankingOperations.deleteAccount(accountId)) {
                    JOptionPane.showMessageDialog(this, "Account Deleted!");
                    refreshTable("");
                } else {
                    JOptionPane.showMessageDialog(this, "Could not delete. Account may have transaction history.");
                }
            }
        });

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updateSearch(); }
            public void removeUpdate(DocumentEvent e) { updateSearch(); }
            public void changedUpdate(DocumentEvent e) { updateSearch(); }
            private void updateSearch() {
                refreshTable(txtSearch.getText());
            }
        });

        btnBack.addActionListener(e -> {
            if (parentFrame != null) {
                parentFrame.setVisible(true);
            }
            this.dispose();
        });

        SwingUtilities.invokeLater(() -> refreshTable(""));
    }

    private void refreshTable(String searchKeyword) {
        if (accountTable != null) {
            accountTable.setModel(BankingOperations.getAccountsTableModel(searchKeyword));
        }
    }
}