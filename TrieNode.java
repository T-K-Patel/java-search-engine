import java.util.*;

class TrieNode {
    Map<Character, TrieNode> children;
    // map of docId to set of word indices in the document
    Map<Integer, Set<Integer>> docIdWordIndMap;
    boolean isEnd;

    public TrieNode() {
        children = new HashMap<>();
        docIdWordIndMap = new HashMap<>();
        isEnd = false;
    }

    public Set<Integer> docIds() {
        return docIdWordIndMap.keySet();
    }
}
