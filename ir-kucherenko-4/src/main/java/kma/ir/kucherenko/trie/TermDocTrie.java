package kma.ir.kucherenko.trie;

import kma.ir.kucherenko.TermDocument;

import java.io.IOException;

public class TermDocTrie {
    private final Trie trie;

    public TermDocTrie(TermDocument termDoc) {
        this.trie = new Trie();
        buildTrie(termDoc);
    }

    private void buildTrie(TermDocument termDoc) {
        for (String term : termDoc.getDictionary())
            trie.insert(term);
    }

    public void write(String fileName) throws IOException {
        trie.writeToFile(fileName);
    }
}
