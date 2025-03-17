import java.util.*;

class RabinKarpSearch {
    private static final int PRIME = 101;

    public  List<Integer> findOccurrences(String text, String pattern) {
        List<Integer> occurrences = new ArrayList<>();
        int patternLength = pattern.length();
        int textLength = text.length();
        int patternHash = hash(pattern, patternLength);
        int textHash = hash(text, patternLength);

        for (int i = 0; i <= textLength - patternLength; i++) {
            if (patternHash == textHash && text.substring(i, i + patternLength).equals(pattern)) {
                occurrences.add(i);
            }
            if (i < textLength - patternLength) {
                textHash = recalculateHash(text, i, i + patternLength, textHash, patternLength);
            }
        }
        return occurrences;
    }

    private int hash(String str, int length) {
        int hash = 0;
        for (int i = 0; i < length; i++) {
            hash += str.charAt(i) * Math.pow(PRIME, i);
        }
        return hash;
    }

    private int recalculateHash(String text, int oldIndex, int newIndex, int oldHash, int patternLength) {
        int newHash = oldHash - text.charAt(oldIndex);
        newHash /= PRIME;
        newHash += text.charAt(newIndex) * Math.pow(PRIME, patternLength - 1);
        return newHash;
    }
}
