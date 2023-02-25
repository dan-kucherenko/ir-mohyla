package kma.ir.kucherenko;

import kma.ir.kucherenko.trie.TermDocTrie;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        TermDocument termDocument = new TermDocument("src/main/collection");
        TermDocTrie termDocTrie = new TermDocTrie(termDocument);
        termDocTrie.write("trie.txt");
    }
}