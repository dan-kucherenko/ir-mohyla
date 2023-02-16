package kma.ir.kucherenko;

import java.io.File;
import java.security.spec.InvalidParameterSpecException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BooleanSearch {
    private final File[] books;
    private final Map<String, Set<Integer>> invertedIndexMap;

    public BooleanSearch(InvertedIndex ii) {
        this.books = ii.getBooks();
        invertedIndexMap = ii.getSortedTermDoc();
    }

    private String[] validateQuery(String query) {
        try {
            Pattern p = Pattern.compile("^(not\\s)?\\w+(\\s(and|or)\\s(not\\s)?\\w+)?()?$");
            query = query.toLowerCase();
            Matcher m = p.matcher(query);
            if (!m.matches())
                throw new InvalidParameterSpecException("Invalid request syntax");
            return query.split("\\s");
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        }
        return new String[0];
    }

    public String[] booleanSearch(String query) {
        String[] updatedQuery = validateQuery(query);
        if (updatedQuery.length == 0)
            return new String[0];
        String operator = null;
        Set<Integer> setPrev = null, setCur = null;
        for (String queryWord : updatedQuery) {
            setPrev = setCur;
            if (queryWord.equals("and") || queryWord.equals("not") || queryWord.equals("or")) {
                operator = queryWord;
                continue;
            }
            setCur = invertedIndexMap.get(queryWord);
        }
        if ((setPrev == null || setCur == null) && !operator.equals("not"))
            return new String[0];
        return convertIntegerToBookName(makeOperation(operator, setPrev, setCur));
    }

    private String[] convertIntegerToBookName(List<Integer> booksIndex) {
        String[] booksName = new String[booksIndex.size()];
        for (int i = 0; i < booksIndex.size(); i++)
            booksName[i] = books[booksIndex.get(i) - 1].getName();
        return booksName;
    }

    private List<Integer> intersect(Set<Integer> s1, Set<Integer> s2) {
        List<Integer> listS1 = new ArrayList<>(s1);
        List<Integer> listS2 = new ArrayList<>(s2);
        List<Integer> resultList = new ArrayList<>();
        int i = 0, j = 0;
        while (i != listS1.size() && j != listS2.size()) {
            if (listS1.get(i) < listS2.get(j))
                i++;
            else if (listS1.get(i) > listS2.get(j))
                j++;
            else {
                resultList.add(listS1.get(i));
                i++;
                j++;
            }
        }
        return resultList;
    }

    private List<Integer> union(Set<Integer> s1, Set<Integer> s2) {
        Set<Integer> res = new LinkedHashSet<>(s1);
        res.addAll(s2);
        return new ArrayList<>(res);
    }

    private List<Integer> opNot(Set<Integer> s1, Set<Integer> s2) {
        s1.removeAll(s2);
        return new ArrayList<>(s1);
    }

    private List<Integer> makeOperation(String operator, Set<Integer> setPrev, Set<Integer> setCur) {

        switch (operator) {
            case "and" -> {
                return intersect(setPrev, setCur);
            }
            case "or" -> {
                return union(setPrev, setCur);
            }
            default -> {
                setPrev = new LinkedHashSet<>();
                for (int i = 0; i < books.length; i++)
                    setPrev.add(i + 1);
                return opNot(setPrev, setCur);
            }
        }
    }
}

