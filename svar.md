# Runtime Analysis
For each method of the tasks give a runtime analysis in Big-O notation and a description of why it has this runtime.

**If you have implemented new methods not listed you must add these as well, e.g. any helper methods. You need to show how you analyzed any methods used by the methods listed below.**

The runtime should be expressed using these three parameters:
   * `n` - number of words in the list allWords
   * `m` - number of words in the list possibleWords
   * `k` - number of letters in the wordleWords


## Task 1 - matchWord
* `WordleAnswer::matchWord`: O(k^2)
    * The O(k^2) complexity comes from in the worst case (when none of the letters are in the correct positions), the inner loop will run k times for each of the k letters in the guess. The worst case time for this method wil therefore be O(k^2)

## Task 2 - EliminateStrategy
* `WordleWordList::eliminateWords`: O(m * k^2)
    * The dominating complexity in the eliminate word is the while loop which iterates over all possibleansers or m. Inside the loop it checks "(!WordleWord.isPossibleWord(answer, feedback))" where "isPosibleword" uses the "WordleWord otherFeedback = WordleAnswer.matchWord(feedback.getWordString(), word)" matchWord function which is stated earlier is 0(k^2). Worst case runtime is therefor 0(m * k^2)

## Task 3 - FrequencyStrategy
* `FrequencyStrategy::makeGuess`: O(m * k)
    * for guesses after the first one getGuess() dominates which is 0(m * k). This happens when the word list has reduced to m possibilities,
    The initial guess or when no feedback is provided findGoodStarterword() which is 0(n * k) dominates. 




# Task 4 - Make your own (better) AI
For this strategy is take inspiration from the frequencysrategy but changed it give a better guess.
While frequencyStrategy focuses on global letter frequencys and counts all characters when calculation score,
MyStrategy prioritizes unique character frequncies, reducing the impact of reapeating letter. Its prioritizing unique characters which i think-
made it allot better than the original, also using java streams to find words, made it a little more functional. I have some weard setup where the first guess uses a array of hashmaps or find frequency for each index. This an older verison of my code which had a lower accuracy rate than the one i have now,-
but for some reason it returns "slate" as the starter word which you could fin using a quick google search is actually the best starting word for wordle, so i use this part of the code for the first guess to ensure i always get "slate" giving me less avreage guesses. Should have tryed to implement it more cleanly, but its friday and echo teambuilding in a hour so this is what i got :D
