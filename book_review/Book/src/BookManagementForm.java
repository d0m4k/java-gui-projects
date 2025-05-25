import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;

import utils.BookDataManager;

public class BookManagementForm extends JFrame {
    private JTextField txtBookID, txtTitle, txtAuthor, txtReview, txtPrice;
    private DefaultTableModel tableModel;
    private JTable table;

    public BookManagementForm() {
        setTitle("Book Management");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top panel for form
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Book Details"));

        txtBookID = new JTextField();
        txtTitle = new JTextField();
        txtAuthor = new JTextField();
        txtReview = new JTextField();
        txtPrice = new JTextField();

//        formPanel.add(new JLabel("Book ID:"));
//        formPanel.add(txtBookID);
        formPanel.add(new JLabel("Title:"));
        formPanel.add(txtTitle);
        formPanel.add(new JLabel("Author:"));
        formPanel.add(txtAuthor);
        formPanel.add(new JLabel("Book Review:"));
        formPanel.add(txtReview);
        formPanel.add(new JLabel("Price:"));
        formPanel.add(txtPrice);

        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton btnAdd = new JButton("Add");
        JButton btnClear = new JButton("Clear");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        formPanel.add(buttonPanel);

        // Table to display books
        String[] columns = {"Book ID", "Title", "Author", "Review", "Price"};
        ArrayList<String[]> books = (ArrayList<String[]>) BookDataManager.loadBooks();
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(50);
        
        for (String[] book : books) {
        	System.out.print(book);
            tableModel.addRow(book);
        }
        JScrollPane tableScroll = new JScrollPane(table);
        
        add(formPanel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);

        // Event Handlers
        btnAdd.addActionListener(e -> {
            String[] data = {
            	String.valueOf(tableModel.getRowCount() + 1),
                txtTitle.getText(),
                txtAuthor.getText(),
                txtReview.getText(),
                txtPrice.getText()
            };
            tableModel.addRow(data);
            saveTableDataToFile();
            clearFields();
        });

        btnDelete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                tableModel.removeRow(selectedRow);
                saveTableDataToFile(); 
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Select a row to delete.");
            }
        });

        btnUpdate.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
//                tableModel.setValueAt(txtBookID.getText(), selectedRow, 0);
                tableModel.setValueAt(txtTitle.getText(), selectedRow, 1);
                tableModel.setValueAt(txtAuthor.getText(), selectedRow, 2);
                tableModel.setValueAt(txtReview.getText(), selectedRow, 3);
                tableModel.setValueAt(txtPrice.getText(), selectedRow, 4);
                saveTableDataToFile();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Select a row to update.");
            }
        });
        btnClear.addActionListener(e -> clearFields());
        

        // Fill form on row click
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
//                txtBookID.setText(tableModel.getValueAt(selectedRow, 0).toString());
                txtTitle.setText(tableModel.getValueAt(selectedRow, 1).toString());
                txtAuthor.setText(tableModel.getValueAt(selectedRow, 2).toString());
                txtReview.setText(tableModel.getValueAt(selectedRow, 3).toString());
                txtPrice.setText(tableModel.getValueAt(selectedRow, 4).toString());
            }
        });

        setVisible(true);
    }
    
    private void saveTableDataToFile() {
        ArrayList<String[]> books = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String[] book = new String[5];
            for (int j = 0; j < 5; j++) {
                book[j] = tableModel.getValueAt(i, j).toString();
            }
            books.add(book);
        }
//        System.out.println(Abooks));
        BookDataManager.saveBooks(books);
    }


    private void clearFields() {
//        txtBookID.setText("");
        txtTitle.setText("");
        txtAuthor.setText("");
        txtReview.setText("");
        txtPrice.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BookManagementForm::new);
    }
}
