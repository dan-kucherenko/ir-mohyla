package kma.ir.kucherenko.permuterm;

import kma.ir.kucherenko.TermDocument;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public List<String> makeVariations(String term) {
        StringBuilder variations = new StringBuilder(term + '$');
        List<String> termVars = new ArrayList<>();
        termVars.add(variations.toString());
        for (int i = 0; i < term.length(); i++) {
            variations.append(term.charAt(i));
            variations.deleteCharAt(0);
            termVars.add(variations.toString());
        }
        permuTermIndex.put(termVars, term);
        return termVars;
    }

    public List<String> permuTermWildCardSearch(String query) {
        List<String> results = new ArrayList<>();
        try {
            if (!matchesPattern(query))
                throw new InvalidParameterException("Invalid request syntax");
            if (query.charAt(0) == '*' && query.charAt(query.length() - 1) == '*')
                results = jokerOnBothSides(query);
            else if (query.charAt(query.length() - 1) == '*')
                results = jokerAtTheEnd(query);
            else if (query.charAt(0) == '*')
                results = jokerAtTheStart(query);
            else {
                int wildcardIndex = query.indexOf('*');
                results = jokerInTheMiddle(query, wildcardIndex);
            }
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        }
        return results;
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

    private List<String> jokerOnBothSides(String query) {
        List<String> results = new ArrayList<>();
        String searchKey = query.substring(1, query.length() - 1);
        for (Map.Entry<List<String>, String> entry : permuTermIndex.entrySet()) {
            List<String> termVars = entry.getKey();
            for (String var : termVars)
                if (var.startsWith(searchKey) && !var.equals(termVars.get(0)))
                    results.add(entry.getValue());
        }
        return results;
    }

    private List<String> jokerAtTheStart(String query) {
        List<String> results = new ArrayList<>();
        String searchKey = query.substring(1);
        for (Map.Entry<List<String>, String> entry : permuTermIndex.entrySet()) {
            List<String> termVars = entry.getKey();
            String lastTermVar = termVars.get(termVars.size() - 1);
            if (lastTermVar.endsWith(searchKey))
                results.add(entry.getValue());
        }
        return results;
    }

    private List<String> jokerInTheMiddle(String query, int wildcardIndex) {
        List<String> results = new ArrayList<>();
        String firstPart = query.substring(0, wildcardIndex);
        String secondPart = query.substring(wildcardIndex + 1);
        for (Map.Entry<List<String>, String> entry : permuTermIndex.entrySet()) {
            List<String> termVars = entry.getKey();
            String firstTermVar = termVars.get(0);
            String lastTermVar = termVars.get(termVars.size() - 1);
            if (firstTermVar.startsWith(firstPart) && lastTermVar.endsWith(secondPart))
                results.add(entry.getValue());
        }
        return results;
    }

    private List<String> jokerAtTheEnd(String query) {
        List<String> results = new ArrayList<>();
        String searchKey = query.substring(0, query.length() - 1);
        for (Map.Entry<List<String>, String> entry : permuTermIndex.entrySet())
            if (entry.getKey().get(0).startsWith(searchKey))
                results.add(entry.getValue());
        return results;
    }

    private boolean matchesPattern(String query) {
        Pattern p = Pattern.compile("^(\\*)?\\w+(\\*)?\\w+(\\*)?$");
        query = query.toLowerCase();
        Matcher m = p.matcher(query);
        return m.matches();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<List<String>, String> map : permuTermIndex.entrySet())
            sb.append(map.getKey()).append(':').append(map.getValue()).append('\n');
        return sb.toString();
    }
}
