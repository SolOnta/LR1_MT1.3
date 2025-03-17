import java.util.List;

class ParallelRabinKarp implements Runnable {
    private final String text;
    private final String pattern;
    private final int start;
    private final int end;
    private final List<Integer> occurrences;

    public ParallelRabinKarp(String text, String pattern, int start, int end, List<Integer> occurrences) {
        this.text = text;
        this.pattern = pattern;
        this.start = start;
        this.end = end;
        this.occurrences = occurrences;
    }

    @Override
    public void run() {
        List<Integer> localOccurrences = RabinKarpSearch.findOccurrences(text.substring(start, end), pattern);
        synchronized (occurrences) {
            for (int index : localOccurrences) {
                occurrences.add(index + start);
            }
        }
    }
}