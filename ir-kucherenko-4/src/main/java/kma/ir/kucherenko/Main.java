package kma.ir.kucherenko;

import kma.ir.kucherenko.jokersearch.JokerSearch;
import kma.ir.kucherenko.permuterm.PermuTerm;
import kma.ir.kucherenko.trigram.TriGram;
import kma.ir.kucherenko.trie.TermDocTrie;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        TermDocument termDocument = new TermDocument("src/main/collection");

        TermDocTrie termDocTrie = new TermDocTrie(termDocument);
        termDocTrie.write("trie.txt");

        PermuTerm permuTerm = new PermuTerm(termDocument);
        permuTerm.createPermuTerm();
        permuTerm.write("permuterm_index.txt");

        TriGram triGram = new TriGram();
        triGram.createTriGram(termDocument);
        triGram.write("trigram_index.txt");

        JokerSearch jokerSearch = new JokerSearch(permuTerm, triGram, termDocTrie.getTrie());
        System.out.println(jokerSearch.search("hel*").toString());
    }
}