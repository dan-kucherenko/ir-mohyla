package kma.ir.kucherenko.trie;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class TrieNode {
    private Map<Character, TrieNode> children;
    private boolean hasChildren;
    private char nodeChar;

    public TrieNode() {
        this.children = new HashMap<>();
        this.hasChildren = false;
    }

    public TrieNode(char nodeChar) {
        this.children = new HashMap<>();
        this.hasChildren = false;
        this.nodeChar = nodeChar;
    }

    public boolean hasChild() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public Map<Character, TrieNode> getChildren() {
        return children;
    }

    public char getNodeCharacter() {
        return nodeChar;
    }
}

public class Trie {
    private final TrieNode root;

    public Trie() {
        this.root = new TrieNode();
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
