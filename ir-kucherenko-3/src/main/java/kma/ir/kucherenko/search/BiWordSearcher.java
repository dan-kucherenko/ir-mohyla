package kma.ir.kucherenko.search;

import kma.ir.kucherenko.BooleanSearch;
import kma.ir.kucherenko.biword.BiWord;
import kma.ir.kucherenko.biword.BiWordIndex;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

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
        Set<Integer> resSet = new LinkedHashSet<>();
        String[] split = query.split(" ");
        BiWord[] pairs = new BiWord[split.length - 1];
        if (split.length > 1) {
            for (int i = 0; i < pairs.length; i++)
                pairs[i] = new BiWord(split[i], split[i + 1]);
        }
        for (BiWord pair : pairs) {
            if (biWordIndex.getIndex().containsKey(pair)) {
                resSet.addAll(biWordIndex.getIndex().get(pair));
            }
        }
        ArrayList<Integer> res = new ArrayList<>(resSet);
        return convertIntegerToBookName(res);
    }
}
