import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class BankingOperations {

    public static boolean addCustomerAccount(String firstName, String lastName, String email, String phone, String accountType) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            String custSql = "INSERT INTO Customer (first_name, last_name, email, phone_number) VALUES (?, ?, ?, ?)";
            PreparedStatement custStmt = conn.prepareStatement(custSql, Statement.RETURN_GENERATED_KEYS);
            custStmt.setString(1, firstName); custStmt.setString(2, lastName);
            custStmt.setString(3, email); custStmt.setString(4, phone);
            custStmt.executeUpdate();

            ResultSet rs = custStmt.getGeneratedKeys();
            int customerId = rs.next() ? rs.getInt(1) : 0;

            String accSql = "INSERT INTO Account (customer_id, account_type, balance) VALUES (?, ?, 0.00)";
            PreparedStatement accStmt = conn.prepareStatement(accSql);
            accStmt.setInt(1, customerId); accStmt.setString(2, accountType);
            accStmt.executeUpdate();

            conn.commit();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean deleteAccount(int accountId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM Account WHERE account_id = ?");
            stmt.setInt(1, accountId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static String processTransaction(int accountId, String type, double amount) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement checkStmt = conn.prepareStatement("SELECT balance FROM Account WHERE account_id = ?");
            checkStmt.setInt(1, accountId);
            ResultSet rsCheck = checkStmt.executeQuery();

            if (!rsCheck.next()) {
                return "Error: Account ID not found.";
            }

            double currentBalance = rsCheck.getDouble("balance");

            if (type.equals("Withdraw") && currentBalance < amount) {
                return "Error: Insufficient Funds.";
            }

            String updateQuery = type.equals("Deposit")
                    ? "UPDATE Account SET balance = balance + ? WHERE account_id = ?"
                    : "UPDATE Account SET balance = balance - ? WHERE account_id = ?";

            PreparedStatement pstmt = conn.prepareStatement(updateQuery);
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, accountId);
            pstmt.executeUpdate();

            String logQuery = "INSERT INTO Transaction (account_id, transaction_type, amount, transaction_date) VALUES (?, ?, ?, NOW())";
            PreparedStatement logStmt = conn.prepareStatement(logQuery);
            logStmt.setInt(1, accountId);
            logStmt.setString(2, type);
            logStmt.setDouble(3, amount);
            logStmt.executeUpdate();

            PreparedStatement getBalance = conn.prepareStatement("SELECT balance FROM Account WHERE account_id = ?");
            getBalance.setInt(1, accountId);
            ResultSet rs = getBalance.executeQuery();

            if (rs.next()) {
                double newBalance = rs.getDouble("balance");
                return String.format("Transaction Successful! New Balance: $%.2f", newBalance);
            }

            return "Success";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Database Error: " + e.getMessage();
        }
    }

    public static DefaultTableModel getAccountsTableModel(String search) {
        String[] columns = {"Account ID", "Customer ID", "First Name", "Last Name", "Type", "Balance"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT a.account_id, c.customer_id, c.first_name, c.last_name, a.account_type, a.balance " +
                    "FROM Account a JOIN Customer c ON a.customer_id = c.customer_id " +
                    "WHERE c.first_name LIKE ? OR a.account_id LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + search + "%"); stmt.setString(2, "%" + search + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("account_id"), rs.getInt("customer_id"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("account_type"), rs.getDouble("balance")});
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return model;
    }

    public static DefaultTableModel getHistoryTableModel(String accountId) {
        String[] columns = {"Trans ID", "Account ID", "Type", "Amount", "Date"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = accountId.isEmpty() ? "SELECT * FROM Transaction ORDER BY transaction_date DESC"
                    : "SELECT * FROM Transaction WHERE account_id = ? ORDER BY transaction_date DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            if (!accountId.isEmpty()) stmt.setInt(1, Integer.parseInt(accountId));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("transaction_id"),
                        rs.getInt("account_id"),
                        rs.getString("transaction_type"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("transaction_date")
                });
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return model;
    }
}