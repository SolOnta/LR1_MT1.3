import java.util.List;

class ParallelRabinKarp implements Runnable {
    private  String text;
    private  String pattern;
    private  int start;
    private  int end;
    private  List<Integer> occurrences;

    public ParallelRabinKarp(String text, String pattern, int start, int end, List<Integer> occurrences) {
        this.text = text;
        this.pattern = pattern;
        this.start = start;
        this.end = end;
        this.occurrences = occurrences;
    }

    @Override
    public void run() {
        RabinKarpSearch RKsearch = new RabinKarpSearch();
        List<Integer> localOccurrences = RKsearch.findOccurrences(text.substring(start, end), pattern);
        synchronized (occurrences) {
            for (int index : localOccurrences) {
                occurrences.add(index + start);
            }
        }
    }
}