// FinanceManagerPanel.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FinanceManagerPanel extends JPanel {
    private final DefaultListModel<Transaction> transactionListModel;
    private final JList<Transaction> transactionList;
    private final JTextField amountField;
    private final JTextField dateField;
    private final JComboBox<String> typeComboBox;
    private final JComboBox<String> categoryComboBox;
    private final JLabel totalIncomeLabel;
    private final JLabel totalExpenseLabel;
    private double totalIncome = 0.0;
    private double totalExpense = 0.0;

    public FinanceManagerPanel() {
        setLayout(new BorderLayout());

        // Panel for entering transactions
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(5, 2, 10, 10));

        // ComboBox for transaction type
        typeComboBox = new JComboBox<>(new String[]{"Income", "Expense"});
        inputPanel.add(new JLabel("Type:"));
        inputPanel.add(typeComboBox);

        // ComboBox for categories
        categoryComboBox = new JComboBox<>(new String[]{"Food", "Transport", "Salary", "Entertainment", "Other"});
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryComboBox);

        // Text field for amount
        amountField = new JTextField();
        inputPanel.add(new JLabel("Amount:"));
        inputPanel.add(amountField);

        // Text field for date
        dateField = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        inputPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        inputPanel.add(dateField);

        // Button to add a transaction
        JButton addButton = new JButton("Add Transaction");
        addButton.addActionListener(new AddTransactionListener());
        inputPanel.add(addButton);

        // Panel for the transaction list
        transactionListModel = new DefaultListModel<>();
        transactionList = new JList<>(transactionListModel);
        JScrollPane transactionScrollPane = new JScrollPane(transactionList);

        // Panel for totals
        JPanel totalPanel = new JPanel();
        totalPanel.setLayout(new GridLayout(2, 2));
        totalIncomeLabel = new JLabel("Total Income: $0.0");
        totalExpenseLabel = new JLabel("Total Expense: $0.0");
        totalPanel.add(totalIncomeLabel);
        totalPanel.add(totalExpenseLabel);

        // Panel for export button
        JPanel exportPanel = new JPanel();
        JButton exportButton = new JButton("Export to CSV");
        exportButton.addActionListener(new ExportListener());
        exportPanel.add(exportButton);

        // Add components to the main panel
        add(inputPanel, BorderLayout.NORTH);
        add(transactionScrollPane, BorderLayout.CENTER);
        add(totalPanel, BorderLayout.SOUTH);
        add(exportPanel, BorderLayout.EAST);
    }

    // Listener for adding transactions
    private class AddTransactionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String type = (String) typeComboBox.getSelectedItem();
            String category = (String) categoryComboBox.getSelectedItem();
            double amount;
            String date = dateField.getText();

            try {
                amount = Double.parseDouble(amountField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid amount. Please enter a valid number.");
                return;
            }

            // Create and add the transaction
            Transaction transaction = new Transaction(type, category, amount, date);
            transactionListModel.addElement(transaction);

            // Update totals
            if ("Income".equals(type)) {
                totalIncome += amount;
            } else {
                totalExpense += amount;
            }
            updateTotals();

            // Clear input fields
            amountField.setText("");
            dateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        }
    }

    // Listener for exporting to CSV
    private class ExportListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try (PrintWriter writer = new PrintWriter(new File("transactions.csv"))) {
                writer.println("Type,Category,Amount,Date");
                for (int i = 0; i < transactionListModel.size(); i++) {
                    writer.println(transactionListModel.getElementAt(i).toString());
                }
                JOptionPane.showMessageDialog(null, "Transactions exported successfully to transactions.csv");
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "Error exporting to CSV: " + ex.getMessage());
            }
        }
    }

    // Update total income and expense labels
    private void updateTotals() {
        totalIncomeLabel.setText("Total Income: $" + totalIncome);
        totalExpenseLabel.setText("Total Expense: $" + totalExpense);
    }
}
