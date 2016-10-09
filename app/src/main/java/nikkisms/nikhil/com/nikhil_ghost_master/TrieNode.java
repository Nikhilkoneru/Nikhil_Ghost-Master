package nikkisms.nikhil.com.nikhil_ghost_master;

import java.util.HashMap;


public class TrieNode {
    private HashMap<Character, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String word) {
        HashMap<Character, TrieNode> child = children;

        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);

            TrieNode trieNode;

            if (child.containsKey(c)) {
                trieNode = child.get(c);
            } else {
                trieNode = new TrieNode();
                child.put(c, trieNode);
            }

            child = trieNode.children;

            if (i == (word.length() - 1)) {
                trieNode.isWord = true;
            }
        }
    }

    public boolean isWord(String word) {
        TrieNode trieNode = searchNode(word);

        if (trieNode != null && trieNode.isWord) {
            return true;
        } else {
            return false;
        }
    }

    public TrieNode searchNode(String word) {
        HashMap<Character, TrieNode> child = children;
        TrieNode trieNode = null;

        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);

            if (child.containsKey(c)) {
                trieNode = child.get(c);
                child = trieNode.children;
            } else {
                return null;
            }
        }

        return trieNode;
    }

    public String getAnyWordStartingWith(String prefix) {
        TrieNode trieNode = searchNode(prefix);
        String finalWord = prefix + "";

        HashMap<Character, TrieNode> child;

        if (trieNode == null) {
            return null;
        } else {
            while ( !(trieNode.isWord) ) {
                child = trieNode.children;
                Character nextChar = (Character) child.keySet().toArray()[0];
                finalWord += nextChar;
                trieNode = child.get(nextChar);
            }
        }

        return finalWord;
    }

    public String getGoodWordStartingWith(String word) {
        return null;
    }
}
