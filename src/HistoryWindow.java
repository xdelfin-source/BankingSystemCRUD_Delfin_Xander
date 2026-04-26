import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class HistoryWindow extends JFrame {
    private JPanel mainPanel;
    private JTable historyTable;
    private JButton btnBack;
    private JTextField textField1;
    private JFrame parentFrame;

    public HistoryWindow(JFrame parent) {
        this.parentFrame = parent;

        setTitle("Full Transaction History");
        setContentPane(mainPanel);
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // 1. Initial Load
        refreshHistory();

        // 2. Search/Filter Logic
        textField1.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { update(); }
            public void removeUpdate(DocumentEvent e) { update(); }
            public void changedUpdate(DocumentEvent e) { update(); }

            private void update() {
                String id = textField1.getText().trim();
                // This calls the logic to filter the table
                historyTable.setModel(BankingOperations.getHistoryTableModel(id));
            }
        });

        // 3. Back Button Logic
        btnBack.addActionListener(e -> {
            if (parentFrame != null) {
                parentFrame.setVisible(true);
            }
            this.dispose();
        });
    }

    public void refreshHistory() {
        if (historyTable != null) {
            // Force the table to pull ALL data from the Transaction table
            historyTable.setModel(BankingOperations.getHistoryTableModel(""));
        }
    }
}