package no.uib.inf102.wordle.model.word;

import java.util.List;
import java.util.Random;

import no.uib.inf102.wordle.model.Dictionary;

/**
 * This class represents an answer to a Wordle puzzle.
 * 
 * The answer must be one of the words in the LEGAL_WORDLE_LIST.
 */
public class WordleAnswer {

    private final String WORD;

    private Dictionary dictionary;

    private static Random random = new Random();

    /**
     * Creates a WordleAnswer object with a random word from the answer word list
     */
    public WordleAnswer(Dictionary dictionary) {
        this(random, dictionary);
    }

    /**
     * Creates a WordleAnswer object with a random word from the answer word list
     * using a specified random object.
     * This gives us the opportunity to set a seed so that tests are repeatable.
     */
    public WordleAnswer(Random random, Dictionary dictionary) {
        this(getRandomWordleAnswer(random, dictionary), dictionary);
    }

    /**
     * Creates a WordleAnswer object with a given word.
     * 
     * @param answer
     */
    public WordleAnswer(String answer, Dictionary dictionary) {
        this.WORD = answer.toLowerCase();
        this.dictionary = dictionary;
    }

    /**
     * Gets a random wordle answer
     * 
     * @param random
     * @return
     */
    private static String getRandomWordleAnswer(Random random, Dictionary dictionary) {
        List<String> possibleAnswerWords = dictionary.getAnswerWordsList();
        int randomIndex = random.nextInt(possibleAnswerWords.size());
        String newWord = possibleAnswerWords.get(randomIndex);
        return newWord;
    }

    /**
     * Guess the Wordle answer. Checks each character of the word guess and gives
     * feedback on which that is in correct position, wrong position and which is
     * not in the answer word.
     * This is done by updating the AnswerType of each WordleCharacter of the
     * WordleWord.
     * 
     * @param wordGuess
     * @return wordleWord with updated answertype for each character.
     */
    public WordleWord makeGuess(String wordGuess) {
        if (!dictionary.isLegalGuess(wordGuess))
            throw new IllegalArgumentException("The word '" + wordGuess + "' is not a legal guess");

        WordleWord guessFeedback = matchWord(wordGuess, WORD);
        return guessFeedback;
    }

    /**
     * Generates a WordleWord showing the match between <code>guess</code> and
     * <code>answer</code>
     * 
     * @param guess
     * @param answer
     * @return
     */

    // n - number of words in the list allWords
    // m - number of words in the list possibleWords
    // k - number of letters in the wordle words
    public static WordleWord matchWord(String guess, String answer) {
        // Check validity of guess, if guess is not valid throw exception
        int wordLength = answer.length();

        if (guess.length() != wordLength) {
            throw new IllegalArgumentException("Guess and answer must have the same number of letters but guess = "
                    + guess + " and answer = " + answer);
        }
        // creates feedback, char and boolean array
        AnswerType[] feedback = new AnswerType[wordLength];
        char[] answerArray = answer.toCharArray();
        boolean[] matched = new boolean[wordLength];

        // Going through the guess giving feedback on matching words
        // O(k)
        // veryfies the correct letter in the right position
        for (int i = 0; i < wordLength; i++) {
            if (guess.charAt(i) == answerArray[i]) {
                feedback[i] = AnswerType.CORRECT;
                matched[i] = true;
            }
        }
        // finds the misplaced letters
        for (int i = 0; i < wordLength; i++) { // O(k)
            if (feedback[i] == null) { // worst case O(k^2)
                boolean misplaced = false;
                for (int j = 0; j < wordLength; j++) { // O(k)

                    if (!matched[j] && guess.charAt(i) == answerArray[j]) {
                        feedback[i] = AnswerType.MISPLACED;
                        matched[j] = true;
                        misplaced = true;
                        break;
                    }
                }
                // makes all non matched letters wrong
                if (!misplaced) {
                    feedback[i] = AnswerType.WRONG;
                }
            }
        }
        // returns new WordleWord object with updated feedback
        return new WordleWord(guess, feedback);
    }

}
