public class ProcessAction{

    private WordRecord[] words;
    private Score score;
    private WordPanel w;



    public ProcessAction(WordPanel w, WordRecord[] words, Score score){
        this.words = words;
        this.score = score;
        this.w = w;
    }

    public void endAction(){
        if(Tracker.live){
            Tracker.live = false;
            System.out.println("End button pressed, game ended"); 	
        }else{
            System.out.println("No game to end!");
        }
    }

    public void quitAction(){
        System.out.println("End button pressed, 'QUIT the game'");
		System.exit(0);
    }

    public void guess(String text){
        System.out.println("Guess: " + text);
        if(Tracker.live){ //Guess can only be processed while if the game is live
            for (int i = 0; i < words.length; i++){
                if (words[i].matchWord(text)){ //Checks if word is not dropped and if text matches -> resets the word if matched [NEED TO prevent guessing until check we want a new word]
                    score.caughtWord(text.length()); //Increase the score
                    if(Tracker.wordsLeft.getAndDecrement() <= words.length){ //Only generate another word if the wordsLeft to fall exceeds the current number of falling words
                        words[i].destroy();
                    }
                    break; //Only match the first occurance, so exit the loop
                }
            }
        }
    }
}