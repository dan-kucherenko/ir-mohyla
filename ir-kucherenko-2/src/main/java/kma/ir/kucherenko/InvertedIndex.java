package kma.ir.kucherenko;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class InvertedIndex {
    private final Map<String, Set<String>> sortedTermDoc;

    public InvertedIndex(TermDocument termDocument) {
        sortedTermDoc = new TreeMap<>();
        sortedTermDoc.putAll(termDocument.getTermDoc());
    }

    public void writeInvertedIndex(String fileName) {
        new File("src/main/additional_files/").mkdirs();
        File dictionary = new File("src/main/additional_files/" + fileName);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(dictionary));
        ) {
            bufferedWriter.write(String.valueOf(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String[] books = null;
        for (Map.Entry<String, Set<String>> map : sortedTermDoc.entrySet()) {
            books = map.getValue().toArray(new String[0]);
            sb.append(map.getKey()).append(':').append(map.getValue().size()).append(':');
            for (int i = 0; i < books.length; i++) {
                if (map.getValue().contains(books[i]))
                    sb.append(i + 1);
                if (i != books.length - 1 && map.getValue().contains(books[i + 1]))
                    sb.append(',');
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
