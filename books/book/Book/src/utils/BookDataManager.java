package utils;
import java.io.*;
import java.util.*;

public class BookDataManager {
    private static final String FILE_NAME = "book_datas.txt";

    // Save list of books to file
    public static void saveBooks(List<String[]> bookList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String[] book : bookList) {
                writer.write(String.join("#", book));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving books: " + e.getMessage());
        }
    }

    // Load books from file and return list
    public static List<String[]> loadBooks() {
        List<String[]> bookList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] book = line.split("#", -1); // Preserve empty fields
                if (book.length == 5) {
                    bookList.add(book);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No existing data file. A new one will be created.");
        } catch (IOException e) {
            System.err.println("Error loading books: " + e.getMessage());
        }
        return bookList;
    }
}
