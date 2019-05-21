package autocompletion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;

/**
 * Class: MakeDictionary.java
 * Description: Class used to define a dictionary finder algorithm used 
 *  to read from file, create a dictionary of unique words and 
 *  then save to file.
 * Author: Paulo Jorge.
 */
public class MakeDictionary {
 private final ArrayList<DictionaryWord> dictionary;
    
    /**
     * Constructor method used to initialise a new dictionary 
     *  finder object.
     */
    public MakeDictionary() {
        this.dictionary = new ArrayList();
    } 
    
    /**
     * Reads all the words in a comma separated text document 
     *  into an Array.
     * @param file String location of file to read from.
     * @return Array list containing string of words read.
     * @throws java.io.FileNotFoundException
     */   
    public static ArrayList<String> readWordsFromCSV(String file) 
            throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(file));
        scanner.useDelimiter(" |\n|,");
        ArrayList<String> words = new ArrayList();
        String str;
        while(scanner.hasNext()) {
            str = scanner.next();
            str = str.trim();
            str = str.toLowerCase();
            words.add(str);
        }
        return words;
    }
    
    /**
     * Method used to save a collection type to file.
     * @param c Collection to save.
     * @param file File name to save to.
     * @throws IOException 
     */
    public static void saveCollectionToFile(Collection<?> c, 
            String file) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        try (PrintWriter printWriter = new PrintWriter(fileWriter)) {
            for(Object w : c){
                printWriter.println(w.toString());
            }
        }
    }
     
    /**
     * Method used to take a list of words, and return a dictionary
     *  array containing all unique words, and an array of the 
     *  associated frequency count of each word.
     * @param words Array list of type string containing words to 
     *  add to form a new dictionary with.
     */
    public void formDictionary (
            ArrayList<String> words) {    
        // Clear previous dictionary
        this.dictionary.clear();
        
        // Loop all words in list
        for (int i = 0; i < words.size(); i++) {
            // Get current word
            String c = words.get(i);
            
            boolean exists = false;
            int x = 0;
            // Loop until word has been found or iterator has reached 
            // the end of the dictionary
            while (!exists && x < this.dictionary.size()) {
                if (this.dictionary.get(x).getWord().equals(c))
                    exists = true;
                // If word is not found increment next position
                else
                    x++;
            }
            
            // Increment word count of word if it is not unique
            if (exists)
                this.dictionary.get(x).incrementFrequency();
            // Else add it to dictionary
            else
                this.dictionary.add(new DictionaryWord(c));
        }
    }
    
    /**
     * Accessor method used to get and return the current formed 
     *  dictionary.
     * @return Array list of type DictionaryWord containing all words
     *  currently formed.
     */
    public ArrayList<DictionaryWord> getDictionary() {
        return this.dictionary;
    }
    
    /**
     * Method used to write a list of words and associated frequency to
     *  file.
     * @param file Filename to save to.
     * @throws java.io.IOException 
     */
    public void saveToFile(String file) throws IOException {
        // Sort words alphabetically
        Collections.sort(this.dictionary);
        
        // Write to file
        saveCollectionToFile(this.dictionary, file);
    }
    
    public class DictionaryWord implements Comparable<DictionaryWord> {
        private final String word;
        // Default word frequency count to 1
        private int frequency = 1;

        /**
         * Constructor method used to create a new dictionary word 
         *  object.
         * @param word String containing word.
         */
        public DictionaryWord(String word) {
            this.word = word;
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
         * Method used to increment the count of this word found.
         */
        public void incrementFrequency() {
            this.frequency++;
        }

        /**
         * Method to compare this object to a second object and return
         *  the ascending order.
         * @param b Second word object to compare against.
         * @return Integer value of compare result: 
         *         0       = object is equal to b.
         *         -Number = object is less than b.
         *         +Number = object is greater than b.
         */
        @Override
        public int compareTo(DictionaryWord b) {
            return getWord().compareTo(b.getWord());
        }

        /**
         * Method to return details of this dictionary word in a 
         *  formatted string.
         * @return Formatted string of dictionary word. 
         */
        @Override
        public String toString() {
            // New string builder
            StringBuilder string = new StringBuilder();

            // Append information
            string.append(getWord()).append(",");
            string.append(getFrequency());

            // Convert StringBuilder to string
            return string.toString();
        }
    }
}