import java.util.*;

public class SearchEngine {
    private Trie trie;
    private List<String> documents;

    public SearchEngine() {
        trie = new Trie();
        documents = new ArrayList<>();
    }

    // Add a document to the search engine
    public void addDocument(String document) {
        int docId = documents.size() + 1;
        documents.add(document);
        trie.insert(document.toLowerCase().trim(), docId);
    }

    public static boolean containsString(String[] array, String target) {
        for (String str : array) {
            if (str.equals(target)) {
                return true;
            }
        }
        return false;
    }

    // Search for documents containing the given word or phrase
    public List<String> search(String query) {
        String[] parts = query.replaceAll("\\s+", "").split("\\s*(\\&\\&|\\|\\|)\\s*");
        if (containsString(parts, "")) {
            System.out.println("Invalid Query");
            return new ArrayList<>();
        }
        Set<Integer> result = evaluateQuery(query.toLowerCase());
        List<String> foundDocuments = new ArrayList<>();
        for (int docId : result) {
            foundDocuments.add(documents.get(docId - 1));
        }
        return foundDocuments;
    }

    // Evaluate complex logical query
    private Set<Integer> evaluateQuery(String query) {
        if (query.contains("||")) {
            String[] terms = query.split("\\|\\|");
            Set<Integer> result = new HashSet<>();
            for (String term : terms) {
                result.addAll(evaluateQuery(term.trim()));
            }
            return result;
        } else if (query.contains("&&")) {
            String[] terms = query.split("&&");
            Set<Integer> result = new HashSet<>(evaluateQuery(terms[0].trim()));
            for (int i = 1; i < terms.length; i++) {
                result.retainAll(evaluateQuery(terms[i].trim()));
            }
            return result;
        } else {
            return trie.search(query.trim());
        }
    }

    void printQuery(String query) {
        List<String> foundDocuments = search(query);
        System.out.println("Query: " + query);
        for (String document : foundDocuments) {
            System.out.println(document);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        SearchEngine searchEngine = new SearchEngine();

        List<String> documents = Documents.getDocuments();
        for (String document : documents) {
            searchEngine.addDocument(document);
        }

        System.out.println(documents.size() + " documents added to the search engine.");

        /**
         * Simple Word Searches
         */
        searchEngine.printQuery("Trie");
        searchEngine.printQuery("structure");
        searchEngine.printQuery("efficient");
        searchEngine.printQuery("search");
        searchEngine.printQuery("engines");

        /**
         * Word Searches with Logical Operators:
         */
        searchEngine.printQuery("Trie && structure");
        searchEngine.printQuery("Trie || structure");
        searchEngine.printQuery("efficient && search");
        searchEngine.printQuery("search || engines");
        searchEngine.printQuery("prefix && searches");

        /**
         * Simple Phrase Searches:
         */

        searchEngine.printQuery("Trie data structure");
        searchEngine.printQuery("prefix searches");
        searchEngine.printQuery("search engines");
        searchEngine.printQuery("fast retrieval");
        searchEngine.printQuery("document IDs");

        /**
         * Phrase Searches with Logical Operators:
         */
        searchEngine.printQuery("Trie data structure && search engines");
        searchEngine.printQuery("prefix searches || document IDs");
        searchEngine.printQuery("fast retrieval && prefix searches");
        searchEngine.printQuery("search engines || document IDs");
        searchEngine.printQuery("Trie data structure || fast retrieval");
    }
}
