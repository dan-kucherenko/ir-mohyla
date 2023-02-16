package kma.ir.kucherenko;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class IncidenceMatrix {
    private final StringBuilder resSB;

    private final Map<String, Set<String>> termDocSet;
    private final byte[][] matrix;
    private final File[] books;

    public IncidenceMatrix(TermDocument termDocument) {
        this.resSB = new StringBuilder();
        this.termDocSet = termDocument.getTermDoc();
        this.books = new File(termDocument.getPath()).listFiles();
        this.matrix = new byte[termDocSet.size()][Objects.requireNonNull(books).length];
        createIncidenceMatrix();
    }

    private void createIncidenceMatrix() {
        String[] termsKeys = termDocSet.keySet().toArray(new String[0]);
        for (int i = 0; i < termsKeys.length; i++) {
            resSB.append(termsKeys[i]).append(": ");
            for (int j = 0; j < Objects.requireNonNull(books).length; j++) {
                if (termDocSet.get(termsKeys[i]).contains(books[j].getName()))
                    matrix[i][j] = 1;
                else
                    matrix[i][j] = 0;
            }
            resSB.append(Arrays.toString(matrix[i])).append('\n');
        }
    }

    public void writeIncidenceMatrix(String fileName) {
        new File("src/main/additional_files/").mkdirs();
        File dictionary = new File("src/main/additional_files/" + fileName);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(dictionary));
        ) {
            bufferedWriter.write(resSB.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
