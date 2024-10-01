package no.uib.inf102.wordle.controller.AI;

import no.uib.inf102.wordle.model.Dictionary;
import no.uib.inf102.wordle.model.word.WordleWord;
import no.uib.inf102.wordle.model.word.WordleWordList;

import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;

/**
 * This strategy finds the word within the possible words which has the highest
 * expected number of green matches, based on letter frequency (ignoring
 * position).
 */
public class FrequencyStrategy implements IStrategy {

    private Dictionary dictionary;
    private WordleWordList guesses;
    private Map<Character, Integer> letterFrequencies;

    public FrequencyStrategy(Dictionary dictionary) {
        this.dictionary = dictionary;
        reset();
    }

    @Override
    public String makeGuess(WordleWord feedback) {

        if (feedback == null) {
            return findGoodStarterWord();
        }
        guesses.eliminateWords(feedback);

        return getGuess();

    }

    @Override
    public void reset() {
        guesses = new WordleWordList(dictionary);
        letterFrequencies = new HashMap<>();
    }

    public String getGuess() {
        calculateFrequencies();

        String bestWord = null;
        int bestScore = -1;

        // Iterate through all possible guesses
        for (String word : guesses.possibleAnswers()) {
            int currentScore = scoreWord(word);

            if (currentScore > bestScore) {
                bestScore = currentScore;
                bestWord = word.toString();
            }
        }

        return bestWord;

    }

    private String findGoodStarterWord() {
        calculateFrequencies();
        String bestWord = null;
        int bestScore = 0;

        for (String word : dictionary.getAnswerWordsList()) {
            int score = 0;
            HashSet<Character> uniqueChars = new HashSet<>();
            for (char c : word.toCharArray()) {
                if (!uniqueChars.contains(c)) {
                    score += letterFrequencies.getOrDefault(c, 0);
                    uniqueChars.add(c);
                }
            }
            if (score > bestScore) {
                bestScore = score;
                bestWord = word;
            }
        }
        return bestWord;
    }

    private void calculateFrequencies() {
        letterFrequencies.clear();

        for (String word : guesses.possibleAnswers()) {

            for (int i = 0; i < word.length(); i++) {
                char letter = word.charAt(i);
                letterFrequencies.put(letter, letterFrequencies.getOrDefault(letter, 0) + 1);
            }
        }
    }

    private int scoreWord(String word) {
        String wordStr = word.toString();
        int score = 0;

        for (int i = 0; i < wordStr.length(); i++) {
            char letter = wordStr.charAt(i);
            score += letterFrequencies.getOrDefault(letter, 0);
        }

        return score;
    }
}
