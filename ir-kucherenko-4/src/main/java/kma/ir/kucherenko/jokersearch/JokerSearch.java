package kma.ir.kucherenko.jokersearch;

import kma.ir.kucherenko.permuterm.PermuTerm;
import kma.ir.kucherenko.trie.Trie;
import kma.ir.kucherenko.trie.TrieNode;
import kma.ir.kucherenko.trigram.TriGram;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JokerSearch {
    private final PermuTerm permuTerm;
    private final TriGram triGram;
    private final Trie trie;
    private final List<String> results;


    public JokerSearch(PermuTerm permuTerm, TriGram triGram, Trie trie) {
        this.permuTerm = permuTerm;
        this.triGram = triGram;
        this.trie = trie;
        results = new ArrayList<>();
    }

    public List<String> search(String query) {
        if (query.charAt(query.length() - 1) == '*')
           return searchByEndingJoker(query);
        return null;
    }

    private List<String> searchByEndingJoker(String query) {
        TrieNode curNode = trie.getRoot();
        query = query.substring(0, query.length()-1);
        // "Search" the Trie to the end of the prefix
        for (int i = 0; i < query.length(); i++) {
            char curChar = query.charAt(i);
            if (!curNode.getChildren().containsKey(curChar))
                return results;
            curNode = curNode.getChildren().get(curChar);
        }
        // If the prefix is a complete word, add it to the results
        if (curNode.hasChild())
            results.add(query);
        depthFirstSearch(curNode, new StringBuilder(query), results);
        return results;
    }

    private void depthFirstSearch(TrieNode node, StringBuilder prefix, List<String> results) {
        if (node.hasChild())
            results.add(prefix.toString());
        for (Map.Entry<Character, TrieNode> entry : node.getChildren().entrySet()) {
            char ch = entry.getKey();
            TrieNode child = entry.getValue();
            prefix.append(ch);
            depthFirstSearch(child, prefix, results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }
}
