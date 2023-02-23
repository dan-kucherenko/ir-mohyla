package kma.ir.kucherenko;

import java.io.File;
import java.security.spec.InvalidParameterSpecException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class BooleanSearch<T1, T2> {
    protected File[] books;
    protected Map<T1, T2> indexMap;

    protected abstract String[] validateQuery(String query);

    public abstract String[] search(String query);

    protected String[] convertIntegerToBookName(List<Integer> booksIndex) {
        String[] booksName = new String[booksIndex.size()];
        for (int i = 0; i < booksIndex.size(); i++)
            booksName[i] = books[booksIndex.get(i) - 1].getName();
        return booksName;
    }

    protected List<Integer> intersect(Set<Integer> s1, Set<Integer> s2) {
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

    protected List<Integer> union(Set<Integer> s1, Set<Integer> s2) {
        Set<Integer> res = new LinkedHashSet<>(s1);
        res.addAll(s2);
        return new ArrayList<>(res);
    }

    protected List<Integer> opNot(Set<Integer> s1, Set<Integer> s2) {
        s1.removeAll(s2);
        return new ArrayList<>(s1);
    }

    protected List<Integer> makeOperation(String operator, Set<Integer> setPrev, Set<Integer> setCur) {

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
