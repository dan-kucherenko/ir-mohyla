package kma.ir.kucherenko;

import kma.ir.kucherenko.permuterm.PermuTerm;
import kma.ir.kucherenko.threegram.TriGram;
import kma.ir.kucherenko.trie.TermDocTrie;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        TermDocument termDocument = new TermDocument("src/main/collection");

//        TermDocTrie termDocTrie = new TermDocTrie(termDocument);
//        termDocTrie.write("trie.txt");

//        PermuTerm permuTerm = new PermuTerm(termDocument);
//        permuTerm.createPermuTerm();
//        permuTerm.write("permuterm_index.txt");

        TriGram triGram = new TriGram();
        triGram.createTriGram(termDocument);
        triGram.write("trigram_index.txt");
    }
}