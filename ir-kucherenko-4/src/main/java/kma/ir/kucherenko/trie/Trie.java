package kma.ir.kucherenko.trie;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            if (!matchesPattern(query))
                throw new InvalidParameterException("Invalid request syntax");
            TrieNode curNode = getRoot();
            query = query.substring(0, query.length() - 1);
            // Traverse to the node that represents the last character of the prefix
            for (int i = 0; i < query.length(); i++) {
                char curChar = query.charAt(i);
                TrieNode node = curNode.getChildren().get(curChar);
                if (node == null)
                    return results;
                curNode = node;
            }
            // Traverse the subtree rooted at the last node of the prefix and collect all words
            collectWords(curNode, new StringBuilder(query), results);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    private void collectWords(TrieNode node, StringBuilder sb, List<String> results) {
        if (node == null)
            return;
        if (!node.hasChild())
            results.add(sb.toString());
        for (Map.Entry<Character, TrieNode> entry : node.getChildren().entrySet()) {
            char ch = entry.getKey();
            TrieNode child = entry.getValue();
            sb.append(ch);
            collectWords(child, sb, results);
            sb.deleteCharAt(sb.length() - 1);
        }
    }

    private boolean matchesPattern(String query) {
        Pattern p = Pattern.compile("^\\w+\\*$");
        query = query.toLowerCase();
        Matcher m = p.matcher(query);
        return m.matches();
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
