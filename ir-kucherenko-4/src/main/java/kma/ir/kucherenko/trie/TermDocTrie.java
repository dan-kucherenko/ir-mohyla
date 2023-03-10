package kma.ir.kucherenko.trie;

import kma.ir.kucherenko.TermDocument;

import java.io.IOException;

public class TermDocTrie {
    private final Trie trie;

    public TermDocTrie(TermDocument termDoc) {
        this.trie = new Trie();
        createTrie(termDoc);
    }

    public Trie getTrie() {
        return trie;
    }

    private void createTrie(TermDocument termDoc) {
        for (String term : termDoc.getDictionary())
            trie.insert(term);
    }

    public void write(String fileName) throws IOException {
        trie.writeToFile(fileName);
    }
}
