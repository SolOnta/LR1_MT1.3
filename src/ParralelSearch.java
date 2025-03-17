import java.util.List;

class ParallelSearch implements Runnable {
    private String text;
    private String pattern;
    private int start;
    private int end;
    private List<Integer> occurrences;

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
