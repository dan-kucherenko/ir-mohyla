package kma.ir.kucherenko;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class IncidenceMatrix {
    private final Map<String, Set<String>> termDocSet;
    private final int[][] matrix;
    private final File[] books;

    public IncidenceMatrix(TermDocument termDocument) {
        this.termDocSet = termDocument.getTermDoc();
        this.books = new File(termDocument.getPath()).listFiles();
        this.matrix = new int[termDocSet.size()][Objects.requireNonNull(books).length];
        createIncidenceMatrix();
    }

    private void createIncidenceMatrix() {
        String[] termsKeys = termDocSet.keySet().toArray(new String[0]);
        for (int i = 0; i < termsKeys.length; i++) {
            for (int j = 0; j < Objects.requireNonNull(books).length; j++) {
                if (termDocSet.get(termsKeys[i]).contains(books[j].getName()))
                    matrix[i][j] = 1;
                else
                    matrix[i][j] = 0;
            }
        }
    }

    public void writeIncidenceMatrix(String fileName) {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int[] ints : matrix)
            sb.append(Arrays.toString(ints)).append('\n');
        sb.append("\n]");
        new File("src/main/additional_files/").mkdirs();
        File dictionary = new File("src/main/additional_files/" + fileName);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(dictionary));
        ) {
            bufferedWriter.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
