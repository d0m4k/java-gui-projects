package utils;
import java.io.*;
import java.util.*;

public class DataManager {
    private static final String FILE_NAME = "datas.txt";

    // Save list of books to file
    public static void saveDatas(List<String[]> dataList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String[] data : dataList) {
                writer.write(String.join("#", data));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving books: " + e.getMessage());
        }
    }

    // Load books from file and return list
    public static List<String[]> loadDatas() {
        List<String[]> dataList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("#", -1); // Preserve empty fields
                if (data.length == 11) {
                    dataList.add(data);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No existing data file. A new one will be created.");
        } catch (IOException e) {
            System.err.println("Error loading books: " + e.getMessage());
        }
        return dataList;
    }
}
