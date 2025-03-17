import java.util.*;

// Клас для послідовного пошуку підрядка
class SequentialSearch {
    public static List<Integer> findOccurrences(String text, String pattern) {
        List<Integer> occurrences = new ArrayList<>();
        for (int i = 0; i <= text.length() - pattern.length(); i++) {
            if (text.substring(i, i + pattern.length()).equals(pattern)) {
                occurrences.add(i);
            }
        }
        return occurrences;
    }
}