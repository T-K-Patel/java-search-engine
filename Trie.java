import java.util.*;

class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    private final String punctuationsRexExp = "[\\p{Punct}]+";

    public void insert(String phrase, int docId) {
        // remove punctuatuins and then insert
        String[] words = phrase.replaceAll(punctuationsRexExp, "").split("\\s+");
        for (int i = 0; i < words.length; i++) {
            TrieNode node = root;
            for (char c : words[i].toCharArray()) {
                node.children.putIfAbsent(c, new TrieNode());
                node = node.children.get(c);
                node.docIdWordIndMap.putIfAbsent(docId, new HashSet<>());
                node.docIdWordIndMap.get(docId).add(i);
            }
            node.isEnd = true;
        }
    }

    public Set<Integer> search(String phrase) {
        Set<Integer> result = new HashSet<>();
        // Remove punctuations and then search
        String[] words = phrase.replaceAll(punctuationsRexExp, "").split("\\s+");
        Map<Integer, List<Set<Integer>>> docIdWordIndMap = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                if (!node.children.containsKey(c)) {
                    return new HashSet<>();
                }
                node = node.children.get(c);
            }
            if (i < words.length - 1 && !node.isEnd) {
                return new HashSet<>();
            }
            Set<Integer> docIdSet = node.docIds();
            if (i != 0)
                docIdSet.retainAll(docIdWordIndMap.keySet());
            Map<Integer, List<Set<Integer>>> temp = new HashMap<>();
            for (int docId : docIdSet) {
                List<Set<Integer>> list = docIdWordIndMap.getOrDefault(docId, new ArrayList<>());
                list.add(node.docIdWordIndMap.get(docId));
                temp.put(docId, list);
            }
            docIdWordIndMap = temp;
        }
        for (int docId : docIdWordIndMap.keySet()) {
            if (hasConsecutive(docIdWordIndMap.get(docId))) {
                result.add(docId);
            } else {
            }
        }
        return result;
    }

    // Check if the list of sets has consecutive elements in consecutive sets
    private boolean hasConsecutive(List<Set<Integer>> listOfSets) {
        if (listOfSets == null || listOfSets.size() <= 1) {
            return true;
        }
        for (int x : listOfSets.get(0)) {
            boolean consecutive = true;
            for (int i = 1; i < listOfSets.size(); i++) {
                if (!listOfSets.get(i).contains(x + i)) {
                    consecutive = false;
                    break;
                }
            }
            if (consecutive) {
                return true;
            }
        }
        return false;
    }
}
