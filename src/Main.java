import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RandomTextGenerator textGenerator = new RandomTextGenerator(); // Використовуємо генератор
        DataManager dataManager = new DataManager(textGenerator); // Передаємо генератор у DataManager

        String text = getText(scanner, dataManager);

        System.out.println("Введіть підрядок для пошуку: ");
        String pattern = scanner.nextLine();

        System.out.println("Оберіть метод пошуку: 1 - Простий перебір, 2 - Рабін-Карп");
        int methodChoice = scanner.nextInt();

        System.out.println("Введіть кількість потоків: ");
        int numThreads = scanner.nextInt();
        scanner.nextLine();

        boolean isBruteForce = (methodChoice == 1);
        executeSearch(text, pattern, numThreads, isBruteForce, dataManager, scanner);

        scanner.close();
    }

    private static String getText(Scanner scanner, DataManager dataManager) {
        System.out.println("Оберіть джерело тексту: 1 - введення вручну, 2 - з файлу, 3 - згенерувати випадково");
        int choice = scanner.nextInt();
        scanner.nextLine();

        String text = "";
        if (choice == 1) {
            System.out.println("Введіть текст: ");
            text = scanner.nextLine();
        } else if (choice == 2) {
            System.out.println("Введіть назву файлу для зчитування: ");
            String filename = scanner.nextLine();
            text = dataManager.readFromFile(filename);
            if (text == null) {
                System.out.println("Не вдалося зчитати текст з файлу.");
                System.exit(1);
            }
        } else if (choice == 3) {
            System.out.println("Введіть довжину випадкового тексту: ");
            int length = scanner.nextInt();
            scanner.nextLine();
            text = dataManager.generateRandomText(length);
            System.out.println("Збережемо текст у файл? (y/n)");
            if (scanner.nextLine().equalsIgnoreCase("y")) {
                System.out.println("Введіть назву файлу для збереження: ");
                String filename = scanner.nextLine();
                dataManager.saveToFile(filename, text);
            }
        } else {
            System.out.println("Невідомий вибір.");
            System.exit(1);
        }
        return text;
    }

    private static void executeSearch(String text, String pattern, int numThreads, boolean isBruteForce, DataManager dataManager, Scanner scanner) {
        SequentialSearch sequentialSearch = new SequentialSearch();
        long startTime = System.nanoTime();
        List<Integer> sequentialResult = sequentialSearch.findOccurrences(text, pattern);
        long sequentialTime = System.nanoTime() - startTime;
        System.out.println((isBruteForce ? "Простий перебір" : "Рабін-Карп") + " (послідовний): " + sequentialResult);
        System.out.println("Час: " + sequentialTime / 1e6 + " мс");

        executeParallelSearch(text, pattern, numThreads, isBruteForce, dataManager, scanner);
    }

    private static void executeParallelSearch(String text, String pattern, int numThreads, boolean isBruteForce, DataManager dataManager, Scanner scanner) {
        List<Integer> parallelOccurrences = Collections.synchronizedList(new ArrayList<>());
        Thread[] threads = new Thread[numThreads];

        int chunkSize = text.length() / numThreads;
        int remainder = text.length() % numThreads;

        long startTime = System.nanoTime();
        for (int i = 0; i < numThreads; i++) {
            int start = i * chunkSize + Math.min(i, remainder);
            int end = (i + 1) * chunkSize + Math.min(i + 1, remainder);

            if (i != numThreads - 1) {
                end = Math.min(end + pattern.length() - 1, text.length());
            }

            threads[i] = new Thread(isBruteForce ?
                    new ParallelSearch(text, pattern, start, end, parallelOccurrences) :
                    new ParallelRabinKarp(text, pattern, start, end, parallelOccurrences));
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long parallelTime = System.nanoTime() - startTime;
        Collections.sort(parallelOccurrences);
        System.out.println((isBruteForce ? "Простий перебір" : "Рабін-Карп") + " (паралельний): " + parallelOccurrences);
        System.out.println("Час: " + parallelTime / 1e6 + " мс");


        System.out.println("Зберегти результати у файл? (y/n)");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            System.out.println("Введіть назву файлу для збереження результатів: ");
            String filename = scanner.nextLine();
            dataManager.saveResultsToFile(filename, parallelOccurrences);
        } else {
            dataManager.printResults(parallelOccurrences);
        }
    }
}
