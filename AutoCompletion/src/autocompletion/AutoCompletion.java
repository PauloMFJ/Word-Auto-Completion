package autocompletion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class: AutoCompletion.java
 * Description: Auto completion class used to combine a trie data 
 *  structure and a dictionary finder to take a list of words and
 *  queries to form a list of matches.
 * Author: Paulo Jorge.
 */
public class AutoCompletion {
    /**
     * Method used to take a list of words. Query them
     *  with a set of queries, process them and return a list of 
     *  matches alongside the probability of each word matching.
     * @param words Array list of type string containing words to 
     *  query.
     * @param queries Array list of type string containing queries to
     *  query words with.
     * @return Returns array list of type string containing all 
     *  matches alongside their respective probability of matching.
     */
    public static ArrayList<String> wordAutoCompletion (
            ArrayList<String> words, ArrayList<String> queries) {
        // Form new dictionary using all words from input file
        MakeDictionary mD = new MakeDictionary();
        mD.formDictionary(words);

        // Form new auto completion trie based on formed dictionary
        AutoCompletionTrie trie = new AutoCompletionTrie();
        for (MakeDictionary.DictionaryWord word : mD.getDictionary())
            trie.add(word.getWord(), word.getFrequency());            
       
        // Initialize list used to store the query results
        ArrayList<String> queryResults = new ArrayList();

        // Loop all prefix queries
        for (String prefix : queries) {
            int total = 0;
            List<QueryWord> matches = new ArrayList();

            // If prefix is a word increase total count and add
            boolean isLeaf = false;
            if ((total = trie.getFrequency(prefix)) > 0) {
                matches.add(new QueryWord(prefix, total));
                isLeaf = true;
            }

            // Create sub trie rooted at each query prefix and 
            // get all words
            AutoCompletionTrie subTrie = trie.getSubTrie(prefix);

            // If sub trie is not null get all words
            if (subTrie != null) {
                // Loop all words in sub trie
                for (Object suffix : subTrie.getAllWords()) {
                    // Get and increase total count
                    int wordFrequency = subTrie.getFrequency(
                            (String)suffix);
                    total += wordFrequency;

                    // Create and add new object containing word info 
                    // to list
                    matches.add(new QueryWord((prefix + suffix), 
                            wordFrequency));
                }
            }

            // Loop all words and calculate probability
            for (QueryWord word : matches)
                word.calculateProbability(total);

            // Sort words list by probability
            Collections.sort(matches);

            String result = new String();
            // If prefix is not a word add result string
            if (!isLeaf)
                result = prefix + ",";

            // Loop upto 5 results
            for (int i = 0; i < (matches.size() < 5 
                    ? matches.size() : 5); i++)
                // Concatenate word to result string
                result += matches.get(i).toString() + ",";

            // Add to return list
            queryResults.add(result);
        }

        return queryResults;
    }
    
    private static class QueryWord implements Comparable<QueryWord>  {
        private final String word;
        // Frequency count of this word within trie
        private int frequency = 0;
        // Probability of occurences
        private float probability = 0;

        /**
         * Constructor method used to initialise a new word to query.
         * @param word String of word to store.
         */
        public QueryWord(String word) {
            this.word = word;
        }

            /**
         * Constructor method used to initialise a new word to query.
         * @param word String of word to store
         * @param frequency Integer frequency count of word.
         */
        public QueryWord(String word, int frequency) {
            this.word = word;
            this.frequency = frequency;
        }

        /**
         * Method used to get and return this word.
         * @return String containing word.
         */
        public String getWord() {
            return this.word;
        }

        /**
         * Method to get and return the frequency count of this word.
         * @return Integer count of word.
         */
        public int getFrequency() {
            return this.frequency;
        }

        /**
         * Method used to get and return the probability that this word
         *  occurred.
         * @return Float value of probability.
         */
        public float getProbability() {
            return this.probability;
        }

        /**
         * Mutator method used to set this nodes frequency count to
         *  the passed in frequency value.
         * @param frequency Integer frequency count to set as.
         */
        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }

        /**
         * Method used to calculate the probability that this word 
         *  occurred in a query by dividing the frequency by passed 
         *  in total.
         * @param total Integer total to calculate.
         */
        public void calculateProbability(int total) {
            this.probability = (float)getFrequency() / total;
        }

        /**
         * Comparator method used to compare this object to a passed in 
         *  object by the probability of the stored word.
         * @param b QueryWord object to compare against.
         * @return Integer value of compare result:
         *  1  = b has greater frequency.
         *  0  = b has equal frequency.
         *  -1 = b has lower frequency.
         */
        @Override
        public int compareTo(QueryWord b) {
            return getProbability() < b.getProbability() ? 1 
                    : (getProbability() > b.getProbability() ? -1 : 0);
        }

        /**
         * Method to return details of this query word in a formatted 
         *  string.
         * @return Formatted string of dictionary word. 
         */
        @Override
        public String toString() {
            // New string builder
            StringBuilder str = new StringBuilder();

            // Append information
            str.append(getWord()).append(",");
            str.append(getProbability());

            // Return formatted string
            return str.toString();
        }
    }
    
    /**
     * Project main containing tests.
     * @param args 
     */
    public static void main(String[] args) {
        try {
            // Get and store words/queries
            ArrayList<String> words = MakeDictionary
                    .readWordsFromCSV("lotr.csv");
            ArrayList<String> queries = MakeDictionary
                    .readWordsFromCSV("lotrQueries.csv");
            
            // Get and save query results to file
            MakeDictionary.saveCollectionToFile(
                    wordAutoCompletion(words, queries), 
                    "lotrMatches.csv");
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }
}