package User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import utils.BookDataManager;

public class UserPortal extends JFrame {
    private JTextField searchField;
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private JTextArea bookDetailsArea;
//    private List<String[]> books;

    public UserPortal() {
        setTitle("User Book Portal");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top panel for search
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        searchField = new JTextField();
        JButton btnSearch = new JButton("Search");
        JButton btnReload = new JButton("Reload");
        topPanel.setBorder(BorderFactory.createTitledBorder("Search Book (Title or Author)"));
        topPanel.add(searchField, BorderLayout.CENTER);
        JPanel searchButtons = new JPanel();
        searchButtons.add(btnSearch);
        searchButtons.add(btnReload);
        topPanel.add(searchButtons, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Table to display books
        String[] columns = {"Book ID", "Title", "Author", "Review", "Price"};
        tableModel = new DefaultTableModel(columns, 0);
        bookTable = new JTable(tableModel);
        bookTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
//        bookTable.setSize(10, 10);
        JScrollPane tableScroll = new JScrollPane(bookTable);
        add(tableScroll, BorderLayout.CENTER);

        // Bottom panel for book details
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bookDetailsArea = new JTextArea(10, 20);
        bookDetailsArea.setEditable(false);
        bookDetailsArea.setLineWrap(true);
        bookDetailsArea.setWrapStyleWord(true);
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Book Review View"));
        bottomPanel.add(new JScrollPane(bookDetailsArea), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Load all books initially
        loadAllBooks();

        // Event: Search
        btnSearch.addActionListener(e -> searchBooks());

        // Event: Reload new books
        btnReload.addActionListener(e -> loadAllBooks());

        // Event: Click on row to view details
        bookTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                showBookDetails();
            }
        });

        setVisible(true);
    }

    private void loadAllBooks() {
        tableModel.setRowCount(0); // clear table
        List<String[]> books = BookDataManager.loadBooks();
        for (String[] book : books) {
            tableModel.addRow(book);
        }
        bookDetailsArea.setText("");
    }

    private void searchBooks() {
        String keyword = searchField.getText().toLowerCase().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a keyword to search.");
            return;
        }

        tableModel.setRowCount(0); // Clear table
        List<String[]> books = BookDataManager.loadBooks();
        for (String[] book : books) {
            String title = book[1].toLowerCase();
            String author = book[2].toLowerCase();
            if (title.contains(keyword) || author.contains(keyword)) {
                tableModel.addRow(book);
            }
        }
        bookDetailsArea.setText("");
    }

    private void showBookDetails() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow != -1) {
            StringBuilder sb = new StringBuilder();
            sb.append("Book ID: ").append(tableModel.getValueAt(selectedRow, 0)).append("\n");
            sb.append("Title: ").append(tableModel.getValueAt(selectedRow, 1)).append("\n");
            sb.append("Author: ").append(tableModel.getValueAt(selectedRow, 2)).append("\n");
            sb.append("Price: ").append(tableModel.getValueAt(selectedRow, 4)).append("\n\n");
            sb.append("Review: ").append(tableModel.getValueAt(selectedRow, 3)).append("\n");
            bookDetailsArea.setText(sb.toString());
            bookDetailsArea.setFont(new Font("SansSerif", Font.PLAIN, 16));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(UserPortal::new);
    }
}
