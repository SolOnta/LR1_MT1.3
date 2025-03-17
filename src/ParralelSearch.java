import java.util.List;

class ParallelSearch implements Runnable {
    private final String text;
    private final String pattern;
    private final int start;
    private final int end;
    private final List<Integer> occurrences;

    public ParallelSearch(String text, String pattern, int start, int end, List<Integer> occurrences) {
        this.text = text;
        this.pattern = pattern;
        this.start = start;
        this.end = end;
        this.occurrences = occurrences;
    }

    @Override
    public void run() {
        for (int i = start; i <= end - pattern.length(); i++) {
            if (text.substring(i, i + pattern.length()).equals(pattern)) {
                synchronized (occurrences) {
                    occurrences.add(i);
                }
            }
        }
    }
}
