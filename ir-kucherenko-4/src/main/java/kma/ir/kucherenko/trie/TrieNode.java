package kma.ir.kucherenko.trie;

import java.util.HashMap;
import java.util.Map;

public class TrieNode {
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
