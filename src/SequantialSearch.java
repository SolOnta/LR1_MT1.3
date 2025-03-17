import java.util.*;

class SequentialSearch {
    public List<Integer> findOccurrences(String text, String pattern) {
        List<Integer> occurrences = new ArrayList<>();
        for (int i = 0; i <= text.length() - pattern.length(); i++) {
            if (text.substring(i, i + pattern.length()).equals(pattern)) {
                occurrences.add(i);
            }
        }
        return occurrences;
    }
}