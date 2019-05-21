package autocompletion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * Class: AutoCompletionTrie.java
 * Description: Class used to define a new trie data structure
 *  that can be further manipulated and read from using built in 
 *  methods.
 * Author: Paulo Jorge.
 */
public class AutoCompletionTrie {
    // Trie root node object
    private final TrieNode root;

    /**
     * Default constructor used to initialise a new trie object.
     */
    public AutoCompletionTrie() {
        // Initialise new root node
        this.root = new TrieNode();
    }

    /**
     * Constructor used to initialise a new sub trie object rooted on
     *  passed in node.
     * @param node TrieNode object to create sub trie with.
     */
    public AutoCompletionTrie(TrieNode node) {
        // Initialise root on passed in node
        this.root = node;
        node.clear();
    }

    /**
     * Method to add a key to this trie.
     * @param key String containing key to insert.
     * @param frequency Integer frequency count of number of times
     *  the key exists to set leaf node as.
     * @return Boolean true if key was successfully added or false
     *  otherwise, if the word already exists in trie.
     */
    public boolean add(String key, int frequency) {
        // Store result of contains
        if (contains(key))
            return false;
            
        // Get root trie node
        TrieNode node = this.root;
        
        // Loop all characters in key
        for (char character : key.trim().toCharArray()) {
            // Index position of character
            int index = character - 'a';

            // Add offspring if it doesnt already exist
            if (node.getOffsprings()[index] == null)
                node = node.addOffspring(index);
            // Else, move current position to the characters index
            else
                node = node.getOffsprings()[index];
        }

        // Set node to leaf as true and return
        node.isLeaf(true);
        // Set nodes frequency
        node.setFrequency(frequency);
        
        // If the key already exists return false
        return true;
    }

    /**
     * Method to check if this trie contains a whole word.
     * @param key String containing word to check.
     * @return Boolean return true if the word passed in is found as a
     *  whole word, false otherwise if whole word is not found, not
     *  just a prefix.
     */
    public boolean contains(String key) {
        // Temp object containing key search result
        TrieNode node = this.root;

        // Loop all characters in key
        for (char character : key.trim().toCharArray()) {
            // Index position of character
            int index = character - 'a';

            // Update position of temp trie node if the current
            // character in key exists, or return null if not
            if (node.getOffsprings()[index] != null)
                node = node.getOffsprings()[index];
            // Return false if key is not within trie
            else
                return false;
        }

        // Return true if key is the leaf of a node else false
        return node.getIsLeaf();
    }

    /**
     * Method to return a breadth first search string.
     * @return String containing a breadth first search traversal.
     */
    public String outputBreadthFirstSearch() {
        // Initialise new queue and default on the root node
        Queue queue = new LinkedList();
        queue.add(this.root);

        // Initialise a new empty bfs string
        String bfs = new String();

        // Loop queue while not empty
        while (!queue.isEmpty()) {
            // Get queue head node
            TrieNode node = (TrieNode)queue.poll();
            
            // Concatenate node character to output string
            bfs += node.getCharacter();
                    
            // Loop all non-null offsprings
            for (TrieNode offspring : node.getOffsprings())
                if (offspring != null)
                    // Add offspring to queue
                    queue.add(offspring);                    
        }
        
        return bfs;
    }

    /**
     * Method to return a depth first search string.
     * @return String containing a output first search traversal.
     */
    public String outputDepthFirstSearch() {
        // Initialise new stack and default on the root node
        Stack<TrieNode> stack = new Stack();
        stack.push(this.root);

        // Initialise a new empty dfs string
        String dfs = new String();

        // Loop stack until empty
        while(!stack.isEmpty()) {
            // Pop node at top of stack
            TrieNode node = stack.pop();
            
            // Concatenate node character to output string
            dfs += node.getCharacter();
            
            // Loop all non-null offsprings
            for (TrieNode offspring : node.getOffsprings())
                if (offspring != null)
                    // Push offspring to queue
                    stack.push(offspring);
        }

        // Return reversed string using stringbuilder
        return new StringBuilder(dfs).reverse().toString();
    }

    /**
     * Method to get and return a new sub trie rooted at the passed in
     *  prefix.
     * @param prefix String prefix to get new sub trie from.
     * @return Object of type Trie rooted at the passed in prefix, or
     *  null if the prefix is not present in this trie.
     */
    public AutoCompletionTrie getSubTrie(String prefix) {
        // Temp object containing prefix search result
        TrieNode node = this.root;

        // Loop all characters in prefix
        for (char character : prefix.trim().toCharArray()) {
            // Index position of character
            int index = character - 'a';

            // Update position of temp trie node if the current
            // character in prefix exists, or return null if not
            if (node.getOffsprings()[index] != null)
                node = node.getOffsprings()[index];
            // Return null if key is not within trie
            else
                return null;
        }

        // Return true if key is the leaf of a node else false
        return new AutoCompletionTrie(node);
    }
    
    /**
     * Method to get and return all words stored in this trie data
     *  structure using a depth first search.
     * @return List of type String containing all words in this trie.
     */
    public List getAllWords() {
        // Initialise new stack and default on the root node
        Stack<TrieNode> stack = new Stack();
        stack.push(this.root);
        
        // Initialise a new list used to store words
        List words = new ArrayList();
        // Initialise a new empty string used to store current word
        String str = new String();
        
        // Loop stack until empty
        while(!stack.isEmpty()) {
            // Pop node at top of stack
            TrieNode node = stack.pop();
                        
            // Loop all non-null offsprings
            for (TrieNode offspring : node.getOffsprings())
                if (offspring != null)
                    // Push offspring to queue
                    stack.push(offspring);
            
            // Concatenate character to word string
            str += node.getCharacter();

            // If node is a whole word add word string to output list
            if (node.getIsLeaf())
                words.add(str);
            
            // Iterate back through trie until parents match
            while (!stack.isEmpty() 
                    && node != stack.peek().getParent()) {
                // Get current nodes parent
                node = node.getParent();

                // Trim last character from word string
                str = str.substring(0, str.length() - 1);
            }
        }
        
        return words;
    }
    
    /**
     * Method to get frequency count of passed in key.
     * @param key String containing word to check.
     * @return Integer frequency count of number of times a word exists
     *  in trie. Or 0 if key was not found as a whole word.
     */
    public int getFrequency(String key) {
        // Temp object containing key search result
        TrieNode node = this.root;

        // Loop all characters in key
        for (char character : key.trim().toCharArray()) {
            // Index position of character
            int index = character - 'a';

            // Update position of temp trie node if the current
            // character in key exists, or return null if not
            if (node.getOffsprings()[index] != null)
                node = node.getOffsprings()[index];
            // Return 0 if key is not within trie
            else
                return 0;
        }

        // Returns frequency count of word or 0 if key is not a leaf
        return node.getIsLeaf() ? node.getFrequency() : 0;
    }
}