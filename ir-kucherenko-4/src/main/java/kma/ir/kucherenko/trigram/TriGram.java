package kma.ir.kucherenko.trigram;

import kma.ir.kucherenko.TermDocument;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public List<String> trigramSearch(String query) {
        List<String> result;
        matchesPattern(query);
        if (query.charAt(0) == '*')
            result = trigramJokerStart(query);
        else if (query.charAt(query.length() - 1) == '*')
            result = trigramJokerEnd(query);
        else
            result = trigramJokerMiddle(query);
        return result;
    }

    private List<String> trigramJokerStart(String query) {
        String trigramQuery = "$" + query.substring(1) + "$";
        Set<String> trigrams = createTrigramsForJoker(trigramQuery);
        Set<String> matchingTerms = new HashSet<>();
        for (String term : trigrams)
            if (term.toLowerCase().endsWith(query.substring(1).toLowerCase()))
                matchingTerms.add(term);
        return new ArrayList<>(matchingTerms);
    }

    private List<String> trigramJokerMiddle(String query) {
        String trigramQuery = "$" + query + "$";
        Set<String> trigrams = createTrigramsForJoker(trigramQuery);
        Set<String> matchingTerms = new HashSet<>();
        for (String term : trigrams)
            if (term.toLowerCase().matches(query.toLowerCase().replace("*", ".*")))
                matchingTerms.add(term);
        return new ArrayList<>(matchingTerms);
    }

    private List<String> trigramJokerEnd(String query) {
        String trigramQuery = "$" + query.substring(0, query.length() - 1) + "$";
        Set<String> trigrams = createTrigramsForJoker(trigramQuery);
        Set<String> matchingTerms = new HashSet<>();
        for (String term : trigrams)
            if (term.toLowerCase().startsWith(query.substring(0, query.length() - 1)))
                matchingTerms.add(term);
        return new ArrayList<>(matchingTerms);
    }

    private boolean matchesPattern(String query) {
        try {
            Pattern p = Pattern.compile("^((\\*)?\\w+)|\\w+(\\*)?\\w+(\\*)?$");
            query = query.toLowerCase();
            Matcher m = p.matcher(query);
            if (!m.matches())
                throw new InvalidParameterException("Invalid request syntax");
            return true;
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Set<String> createTrigramsForJoker(String trigramQuery) {
        Set<String> trigrams = new HashSet<>();
        for (int i = 0; i < trigramQuery.length() - 2; i++) {
            String trigram = trigramQuery.substring(i, i + 3);
            if (triGramIndex.containsKey(trigram)) {
                trigrams.addAll(triGramIndex.get(trigram));
            }
        }
        return trigrams;
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
        for (Map.Entry<String, List<String>> map : triGramIndex.entrySet())
            sb.append(map.getKey()).append(':').append(map.getValue()).append('\n');
        return sb.toString();
    }
}
