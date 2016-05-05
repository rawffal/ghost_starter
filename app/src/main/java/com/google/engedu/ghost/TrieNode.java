package com.google.engedu.ghost;

import android.util.Log;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;


public class TrieNode {
    private HashMap<String, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {
        HashMap<String, TrieNode> node = children;
        for (int i = 0; i < s.length(); i++) {
            char character = s.charAt(i);
            String sCharacter = "" + character;
            if (!node.containsKey(sCharacter)) {
                node.put(sCharacter, new TrieNode());
            }
            node = node.get(sCharacter).children;
        }
        node.put("/0", new TrieNode());
    }

    public boolean isWord(String s) {
        HashMap<String, TrieNode> node = children;
        for (int i = 0; i < s.length(); i++) {
            if (node.isEmpty()) {
                return false;
            }
            char character = s.charAt(i);
            String sCharacter = "" + character;
            if (node.containsKey(sCharacter)) {
                node = node.get(sCharacter).children;
            } else {
                return false;
            }
        }
        return node.containsKey("/0");
    }

    public String getAnyWordStartingWith(String s) {

        HashMap<String, TrieNode> node = children;

        for (int i = 0; i < s.length(); i++) {
            char character = s.charAt(i);
            String s_c = "" + character;
            if (node.containsKey(s_c)) {
                node = node.get(s_c).children;
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append(s);

        while (!isWord(sb.toString()) || sb.length() == 1) {
            Set key = node.keySet();
            String[] array =  node.keySet().toArray(new String[key.size()]);
            int position = new Random().nextInt(array.length);
            sb.append(array[position]);
            node = node.get(array[position]).children;
        }
        return sb.toString();
    }

    public String getGoodWordStartingWith(String s) {
        return null;
    }
}
