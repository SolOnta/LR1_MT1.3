import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String text = getText(scanner);
        scanner.nextLine();
        System.out.println("Введіть підрядок: ");
        String pattern = scanner.nextLine();

        System.out.println("Оберіть метод пошуку: 1 - Простий перебір, 2 - Рабін-Карп");
        int methodChoice = scanner.nextInt();
        System.out.println("Введіть кількість потоків: ");
        int numThreads = scanner.nextInt();
        scanner.close();

        if (methodChoice == 1) {
            executeSearch(text, pattern, numThreads, true);
        } else if (methodChoice == 2) {
            executeSearch(text, pattern, numThreads, false);
        } else {
            System.out.println("Невідомий метод пошуку.");
        }
    }

    private static String getText(Scanner scanner) {
        System.out.println("Оберіть джерело тексту: 1 - введення вручну, 2 - згенерувати");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {
            System.out.println("Введіть рядок: ");
            return scanner.nextLine();
        } else {
            System.out.println("Введіть довжину випадкового тексту: ");
            int length = scanner.nextInt();
            return RandomTextGenerator.generateRandomText(length);
        }
    }

    private static void executeSearch(String text, String pattern, int numThreads, boolean isBruteForce) {
        if (isBruteForce) {
            executeBruteForceSearch(text, pattern, numThreads);
        } else {
            executeRabinKarpSearch(text, pattern, numThreads);
        }
    }

    private static void executeBruteForceSearch(String text, String pattern, int numThreads) {
        long startTime = System.nanoTime();
        List<Integer> sequentialResult = SequentialSearch.findOccurrences(text, pattern);
        long sequentialTime = System.nanoTime() - startTime;
        System.out.println("Простий перебір (послідовний): " + sequentialResult);
        System.out.println("Час: " + sequentialTime / 1e6 + " мс");

        executeParallelSearch(text, pattern, numThreads, true);
    }

    private static void executeRabinKarpSearch(String text, String pattern, int numThreads) {
        long startTime = System.nanoTime();
        List<Integer> rkSequentialResult = RabinKarpSearch.findOccurrences(text, pattern);
        long rkSequentialTime = System.nanoTime() - startTime;
        System.out.println("Рабін-Карп (послідовний): " + rkSequentialResult);
        System.out.println("Час: " + rkSequentialTime / 1e6 + " мс");

        executeParallelSearch(text, pattern, numThreads, false);
    }

    private static void executeParallelSearch(String text, String pattern, int numThreads, boolean isBruteForce) {
        List<Integer> parallelOccurrences = Collections.synchronizedList(new ArrayList<>());
        Thread[] threads = new Thread[numThreads];
        int chunkSize = text.length() / numThreads;

        long startTime = System.nanoTime();
        for (int i = 0; i < numThreads; i++) {
            int start = i * chunkSize;
            int end = (i == numThreads - 1) ? text.length() : (i + 1) * chunkSize + (isBruteForce ? 0 : pattern.length() - 1);
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
    }
}