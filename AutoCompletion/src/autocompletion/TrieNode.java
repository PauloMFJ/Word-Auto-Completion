package autocompletion;

/**
 * Class: TrieNode.java
 * Description: Trie node class used to create a individual trie 
 *  node object.
 * Author: Paulo Jorge.
 */
public class TrieNode {
    // Character stored in node
    private char character;
    // Parent node
    private TrieNode parent;    
    // List of offspring nodes
    private final TrieNode[] offsprings;
    private final int MAX_OFFSPRINGS = 26;
    // Flag used to represent if node is at the end of the word
    private boolean isLeaf = false;
    // Frequency count of this word within trie
    private int frequency = 0;

    /**
     * Default constructor method used to initialise a new empty trie 
     *  node object.
     */
    public TrieNode() { 
        this.offsprings = new TrieNode[MAX_OFFSPRINGS];
    }
    
    /**
     * Constructor method used to initalise a new trie node
     *  containing a character and parent node.
     * @param character Trie node character.
     * @param parent TrieNode parent object for this node.
     */
    public TrieNode(char character, TrieNode parent) {
        this.offsprings = new TrieNode[MAX_OFFSPRINGS];
        this.character = character;
        this.parent = parent;
    }
    
    /**
     * Accessor method used to get and return this nodes character.
     * @return Char of this trie node.
     */
    public char getCharacter() {
        return this.character;
    }
    
    /**
     * Accessor method used to get and return this nodes offsprings.
     * @return Array of type TrieNode containing all offspring objects.
     */
    public TrieNode[] getOffsprings() {
        return this.offsprings;
    }
    
    /**
     * Accessor method to get and return the leaf state of this node.
     * @return Boolean true if is leaf, false otherwise.
     */
    public Boolean getIsLeaf() {
        return this.isLeaf;
    }
    
    /**
     * Method used to get and return this nodes parent node.
     * @return Parent TrieNode object.
     */
    public TrieNode getParent() {
        return this.parent;
    }
    
    /**
     * Accessor method to get and return the frequency count for the
     *  number of times this word has been added.
     * @return Integer frequency count.
     */
    public int getFrequency() {
        return this.frequency;
    }
    
    /**
     * Mutator method used to add a new offspring character to this 
     *  trie node.
     * @param index Integer index position of offspring to add.
     * @return New offspring object of type TrieNode added to this 
     *  trie node.
     */
    public TrieNode addOffspring(int index) {
        this.offsprings[index] = new TrieNode((char)(index + 'a'), 
                this);
        return this.offsprings[index];
    }
    
    /**
     * Mutator method to set the leaf state of this trie node.
     * @param isLeaf Boolean true if this is a leaf, false otherwise.
     */
    public void isLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
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
     * Method used to clear the contents of this node, excluding
     *  its offsprings.
     */
    public void clear() {
        this.character = Character.UNASSIGNED;
        this.parent = null;
        this.isLeaf = false;
    }
}