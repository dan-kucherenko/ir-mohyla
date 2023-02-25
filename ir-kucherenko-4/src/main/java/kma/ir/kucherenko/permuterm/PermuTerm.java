package kma.ir.kucherenko.permuterm;

import kma.ir.kucherenko.TermDocument;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class PermuTerm {
    private final Map<List<String>, String> permuTermIndex;
    private final TermDocument termDocument;

    public PermuTerm(TermDocument termDocument) {
        this.permuTermIndex = new HashMap<>();
        this.termDocument = termDocument;
    }

    public void createPermuTerm() {
        for (String term : termDocument.getDictionary())
            makeVariations(term);
    }

    private void makeVariations(String term) {
        StringBuilder variations = new StringBuilder(term + '$');
        List<String> termVars = new ArrayList<>();
        for (int i = 0; i < term.length(); i++) {
            variations.append(term.charAt(i));
            variations.deleteCharAt(0);
            termVars.add(variations.toString());
        }
        permuTermIndex.put(termVars, term);
    }

    public void write(String filePath) {
        new File("src/main/additional_files/").mkdirs();
        File dictionary = new File("src/main/additional_files/" + filePath);
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
        for (Map.Entry<List<String>, String> map : permuTermIndex.entrySet())
            sb.append(map.getKey()).append(':').append(map.getValue()).append('\n');
        return sb.toString();
    }
}
