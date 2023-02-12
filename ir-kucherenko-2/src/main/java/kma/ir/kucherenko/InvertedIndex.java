package kma.ir.kucherenko;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class InvertedIndex {
    Map<String, Set<String>> sortedTermDoc;

    public InvertedIndex(TermDocument termDocument) {
        sortedTermDoc = new TreeMap<>();
        sortedTermDoc.putAll(termDocument.getTermDoc());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String[] books = null;
        for (Map.Entry<String, Set<String>> map : sortedTermDoc.entrySet()) {
            books = map.getValue().toArray(new String[0]);
            for (int i = 1; i < books.length; i++) {
                if (map.getValue().contains(books[i]))
                    sb.append(map.getKey()).append(':').append(map.getValue().size()).append(':').append(i);
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
