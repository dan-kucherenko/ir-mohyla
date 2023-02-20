package kma.ir.kucherenko.coord_inverted;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class InvertedIndex {
    private final Map<String, Set<Integer>> sortedTermDoc;
    private final File[] books;

    public InvertedIndex(TermDocument termDocument) {
        books = termDocument.getBooks();
        sortedTermDoc = new TreeMap<>();
        for (Map.Entry<String, Set<String>> map : termDocument.getTermDoc().entrySet())
            sortedTermDoc.put(map.getKey(), convertBooksToInteger(map.getValue(), termDocument.getPath()));
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

    private Set<Integer> convertBooksToInteger(Set<String> booksSet, String filepath) {
        Set<Integer> res = new LinkedHashSet<>();
        for (String book : booksSet)
            res.add(Arrays.asList(books).indexOf(new File(filepath + '/' + book)) + 1);
        return res;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Set<Integer>> map : sortedTermDoc.entrySet())
            sb.append(map.getKey()).append(':').append(map.getValue()).append('\n');
        return sb.toString();
    }
}
