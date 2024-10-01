package no.uib.inf102.wordle.controller.AI;

import no.uib.inf102.wordle.model.Dictionary;
import no.uib.inf102.wordle.model.word.WordleWord;
import no.uib.inf102.wordle.model.word.WordleWordList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This strategy finds the word within the possible words which has the highest
 * expected number of green matches.
 */
public class MyStrategy implements IStrategy {

    private Dictionary dictionary;
    private WordleWordList guesses;

    public MyStrategy(Dictionary dictionary) {
        this.dictionary = dictionary;
        reset();
    }

    /**
     * Makes a guess based on the feedback. If feedback is null, it selects
     * the best starter word. Otherwise it elimates incompatible words and
     * returns the best guess
     * 
     * @param feedback The feedback from the previous guess.
     * @return The next guess word.
     */
    @Override
    public String makeGuess(WordleWord feedback) {
        // takes the best starter word if theirs no feedback
        if (feedback == null) {

            return getBestFirstGuess(guesses, dictionary.WORD_LENGTH);
        }
        // Eliminates words that are not compatible with the feedback
        guesses.eliminateWords(feedback);
        // Returns the best guess
        return getGuess(getWordScore(guesses));
    }

    /**
     * Gets the word with the highest score based on the word-score map
     *
     * @param wordScore A map where keys are words and values are their scores.
     * @return The word with the highest score.
     */
    private String getGuess(Map<String, Integer> wordScore) {
        // returns a set of map.entry objects of key-value pair from map,
        // converts the sets into a steam
        // find the max value
        // returns the key for the max value
        return wordScore.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
    }

    /**
     * calculates the score for each word in the word-list based on the feedback of
     * uniqe letters in the list.
     *
     * @param list The list of possible words.
     * @return A map where each word is associated with its score.
     */
    private Map<String, Integer> getWordScore(WordleWordList list) {
        // creates a map of character integers
        Map<Character, Integer> characterFrequency = new HashMap<>();
        // iterates over possiblewords calculation frequency of unique character
        for (String word : list.possibleAnswers()) {
            // using set to track unique character since words with same character give less
            // informaton
            Set<Character> uniqueChars = new HashSet<>();
            for (char c : word.toCharArray()) {
                if (uniqueChars.add(c)) {
                    // calculates frequency of unique character
                    characterFrequency.put(c, characterFrequency.getOrDefault(c, 0) + 1);
                }
            }
        }
        // creates a map of word and score
        Map<String, Integer> wordScores = new HashMap<>();
        for (String word : list.possibleAnswers()) {
            // using scoring system to determin good words
            int score = 0;
            Set<Character> uniqueChars = new HashSet<>();
            for (char c : word.toCharArray()) {
                if (uniqueChars.add(c)) {
                    // adds frequency of unique character
                    score += characterFrequency.getOrDefault(c, 0);
                }
            }
            wordScores.put(word, score);
        }

        return wordScores;
    }

    @Override
    public void reset() {
        guesses = new WordleWordList(dictionary);
    }

    /**
     * Calculates the best first guess from the list of possible words based on
     * letter frequencies.
     * The method calculates the score of each word by summing the frequency of its
     * unique letters and subtracing for duplicate letters.
     * It selects the word with the highest total score as the best first guess.
     *
     * @param wordList The list of possible words to evaluate.git ca
     * @param k        The length of the Wordle words.
     * @return The best first guess, which is the word with the highest score.
     */
    private String getBestFirstGuess(WordleWordList wordList, int k) {
        HashMap<Character, Integer>[] frequencyIndex = getHighestFrequencyLetters(wordList, k);
        // implements bestGuess
        String bestGuess = null;
        // implements maxScore
        int maxScore = 0;
        // iterates over possible words giving each word a score based on the frequency
        // of each letter
        for (String word : wordList.possibleAnswers()) {
            int score = 0;
            // Using the same technique as in getWordScore
            Set<Character> uniqueChars = new HashSet<>();
            int index = 0;
            for (char c : word.toCharArray()) {
                if (uniqueChars.add(c)) {
                    score += frequencyIndex[index].getOrDefault(c, 0);
                } else {
                    // Penalize duplicate letters
                    score -= frequencyIndex[index].getOrDefault(c, 0) / 2;
                }
                index++;
            }

            // Select the word with the highest score
            if (score > maxScore) {
                maxScore = score;
                bestGuess = word;
            }
        }

        return bestGuess;
    }

    /**
     * Calculates the frequency of each letter at each position in the word
     * Returns a array of hashmaps, where each hashmap conatins the frequency of
     * characters for each position
     * 
     * @param wordList The list of possible words.
     * @param k        The length of the Wordle words.
     * @return An array of hashmaps, where each hashmap contains character
     *         frequencies for each position.
     */
    private HashMap<Character, Integer>[] getHighestFrequencyLetters(WordleWordList wordList, int k) {
        // Creates an array of size k and maps each index to a hashmap
        HashMap<Character, Integer>[] frequencyIndex = new HashMap[k];
        // Creates a hashmap for each index
        for (int i = 0; i < k; i++) {
            frequencyIndex[i] = new HashMap<>();
        }
        // Iterates over possible words
        for (String word : wordList.possibleAnswers()) {
            for (int i = 0; i < k; i++) {
                char c = word.charAt(i);
                // updates frequency index adding up the frequency of each character in each
                // index
                frequencyIndex[i].put(c, frequencyIndex[i].getOrDefault(c, 0) + 1);
            }
        }

        return frequencyIndex;
    }
}
