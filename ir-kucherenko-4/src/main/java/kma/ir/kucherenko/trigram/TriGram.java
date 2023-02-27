package kma.ir.kucherenko.trigram;

import kma.ir.kucherenko.TermDocument;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TriGram {
    private final Map<String, List<String>> triGramIndex;

    public TriGram() {
        this.triGramIndex = new HashMap<>();
    }

    public void createTriGram(TermDocument termDocument) {
        for (String word : termDocument.getDictionary())
            addTriGram(word.toLowerCase());
    }

    private void addTriGram(String term) {
        StringBuilder str = new StringBuilder("$" + term + "$");
        for (int i = 0; i < str.length() - 2; i++) {
            if (triGramIndex.containsKey(str.substring(i, i + 3)))
                triGramIndex.get(str.substring(i, i + 3)).add(term);
            else {
                triGramIndex.put(str.substring(i, i + 3), new LinkedList<>());
                triGramIndex.get(str.substring(i, i + 3)).add(term);
            }
        }
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
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, List<String>> map : triGramIndex.entrySet())
            sb.append(map.getKey()).append(':').append(map.getValue()).append('\n');
        return sb.toString();
    }
}
