import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

class DataManager {
    private final RandomTextGenerator textGenerator;

    public DataManager(RandomTextGenerator textGenerator) {
        this.textGenerator = textGenerator;
    }

    public String generateRandomText(int length) {
        return textGenerator.generateRandomText(length);
    }


    public void saveToFile(String filename, String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(data);
            System.out.println("Дані збережено у файл: " + filename);
        } catch (IOException e) {
            System.err.println("Помилка збереження у файл: " + e.getMessage());
        }
    }


    public String readFromFile(String filename) {
        try {
            return new String(Files.readAllBytes(Paths.get(filename)));
        } catch (IOException e) {
            System.err.println("Помилка читання файлу: " + e.getMessage());
            return null;
        }
    }


    public void saveResultsToFile(String filename, List<Integer> results) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (int result : results) {
                writer.write(result + "\n");
            }
            System.out.println("Результати збережено у файл: " + filename);
        } catch (IOException e) {
            System.err.println("Помилка збереження результатів: " + e.getMessage());
        }
    }


    public void printResults(List<Integer> results) {
        System.out.println("Результати пошуку: " + results);
    }
}
