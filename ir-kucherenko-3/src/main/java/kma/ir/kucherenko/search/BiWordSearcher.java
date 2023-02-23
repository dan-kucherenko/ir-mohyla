package kma.ir.kucherenko.search;

import kma.ir.kucherenko.BooleanSearch;
import kma.ir.kucherenko.biword.BiWord;
import kma.ir.kucherenko.biword.BiWordIndex;

import java.util.*;

public class BiWordSearcher extends BooleanSearch<BiWord, Set<Integer>> {
    private final BiWordIndex biWordIndex;

    public BiWordSearcher(BiWordIndex biWordIndex) {
        this.biWordIndex = biWordIndex;
        this.books = biWordIndex.getBooksFolder();
    }

    @Override
    protected String[] validateQuery(String query) {
        return new String[0];
    }

    @Override
    public String[] search(String query) {
        Set<Integer> curSet = new LinkedHashSet<>();
        Set<Integer> prevSet;
        String[] split = query.split("\\s");
        BiWord[] pairs = new BiWord[split.length - 1];
        if (split.length > 1) {
            for (int i = 0; i < pairs.length; i++)
                pairs[i] = new BiWord(split[i].replaceAll("\\W+", "").toLowerCase(), split[i + 1].replaceAll("\\W+", "").toLowerCase());
        }
        for (BiWord pair : pairs) {
            if (biWordIndex.getIndex().containsKey(pair)) {
                prevSet = curSet;
                curSet = biWordIndex.getIndex().get(pair);
                if (curSet.isEmpty() || prevSet.isEmpty())
                    continue;
                curSet = new LinkedHashSet<>(makeOperation("and", curSet, prevSet));
            }
        }
        ArrayList<Integer> res = new ArrayList<>(curSet);
        return convertIntegerToBookName(res);
    }
}
