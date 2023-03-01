package kma.ir.kucherenko.trie;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.security.spec.InvalidParameterSpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Trie {
    private final TrieNode root;

    public Trie() {
        this.root = new TrieNode();
    }

    public TrieNode getRoot() {
        return root;
    }

    public void insert(String word) {
        TrieNode curNode = root;
        for (int depth = 0; depth < word.length(); depth++) {
            char curChar = word.charAt(depth);
            TrieNode node = curNode.getChildren().get(curChar);
            if (node == null) {
                node = new TrieNode(curChar);
                curNode.getChildren().put(node.getNodeCharacter(), node);
            }
            curNode = node;
        }
        curNode.setHasChildren(true);
    }

    public List<String> searchTrie(String query) {
        List<String> results = new ArrayList<>();
        try {
            if (query.charAt(query.length() - 1) != '*')
                throw new InvalidParameterException("Invalid query");

        TrieNode curNode = getRoot();
        query = query.substring(0, query.length() - 1);
        // Traverse to the node that represents the last character of the prefix
        for (int i = 0; i < query.length(); i++) {
            char curChar = query.charAt(i);
            TrieNode node = curNode.getChildren().get(curChar);
            if (node == null)
                return results;  // Prefix not found
            curNode = node;
        }
        // Traverse the subtree rooted at the last node of the prefix and collect all words
        collectWords(curNode, new StringBuilder(query), results);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    private void collectWords(TrieNode node, StringBuilder prefix, List<String> results) {
        if (node == null)
            return;
        if (!node.hasChild())
            results.add(prefix.toString());
        for (Map.Entry<Character, TrieNode> entry : node.getChildren().entrySet()) {
            char ch = entry.getKey();
            TrieNode child = entry.getValue();
            prefix.append(ch);
            collectWords(child, prefix, results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    public void writeToFile(String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        writeNode(root, writer, "");
        writer.close();
    }

    private void writeNode(TrieNode node, BufferedWriter writer, String prefix) throws IOException {
        if (!node.hasChild()) {
            writer.write(prefix);
            writer.newLine();
        }
        for (Map.Entry<Character, TrieNode> entry : node.getChildren().entrySet()) {
            char ch = entry.getKey();
            TrieNode child = entry.getValue();
            writeNode(child, writer, prefix + ch);
        }
    }
}
