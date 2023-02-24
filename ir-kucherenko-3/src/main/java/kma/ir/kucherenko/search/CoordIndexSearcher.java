package kma.ir.kucherenko.search;

import kma.ir.kucherenko.BooleanSearch;
import kma.ir.kucherenko.coord_inverted.CoordInverted;

import java.lang.reflect.Array;
import java.security.spec.InvalidParameterSpecException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CoordIndexSearcher extends BooleanSearch<String, HashMap<Integer, TreeSet<Integer>>> {
    private final CoordInverted coordInverted;
    private final LinkedHashSet<Integer> resBookIndexes;


    public CoordIndexSearcher(CoordInverted coordInverted) {
        this.coordInverted = coordInverted;
        this.indexMap = coordInverted.getIndex();
        this.resBookIndexes = new LinkedHashSet<>();
        this.books = coordInverted.getBooksFolder();
    }

    @Override
    protected String[] validateQuery(String query) {
        try {
            Pattern p = Pattern.compile("^\\w+(\\s/\\d+\\s\\w+)+$");
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

    @Override
    public String[] search(String query) {
        String[] updatedQuery = validateQuery(query);
        if (updatedQuery.length == 0)
            return new String[0];
        String firstWord = updatedQuery[0];
        Map<Integer, TreeSet<Integer>> mapOfWord1 = coordInverted.getIndex().get(firstWord);
        for (int i = 2; i < updatedQuery.length; i += 2) {
            String secondWord = updatedQuery[i];
            int gap = Integer.parseInt(updatedQuery[i-1].replace("/", ""));
            Map<Integer, TreeSet<Integer>> mapOfWord2 = coordInverted.getIndex().get(updatedQuery[i]);
            for (int bookIndex : mapOfWord2.keySet()) {
                if (mapOfWord1.containsKey(bookIndex))
                    addBookByGap(firstWord, secondWord, bookIndex, gap);
            }
        }
        ArrayList<Integer> resList = new ArrayList<>(resBookIndexes);
        return  convertIntegerToBookName(resList);
    }

    private void addBookByGap(String firstWord, String secondWord, int bookIndex, int gap){
        TreeSet<Integer> positionsOfFirstWord = coordInverted.getIndex().get(firstWord).get(bookIndex);
        TreeSet<Integer> positionsOfSecondWord = coordInverted.getIndex().get(secondWord).get(bookIndex);

        for(int position : positionsOfSecondWord){
            boolean containsPos = positionsOfFirstWord.contains(position - gap);
            if (containsPos){
                resBookIndexes.add(bookIndex);
                break;
            } else
                resBookIndexes.remove(bookIndex);
        }
    }
}
