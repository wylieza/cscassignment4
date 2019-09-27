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
        if(Score.live){
            Score.live = false;
        }else{
            System.out.println("No game to end!");
        }
    }

    public void quitAction(){
        System.out.println("Quit button pressed");
		System.exit(0);
    }

    public void guess(String text){
        //System.out.println("Guess: " + text);
        for (int i = 0; i < words.length; i++){
            if (words[i].matchWord(text)){ //Checks if word is not dropped and if text matches -> resets the word if matched
                score.caughtWord(text.length()); //Increase the score
                //No locking is required below
                if(Score.wordsLeft.getAndDecrement() <= words.length){ //Only generate another word if the wordsLeft to fall exceeds the current number of falling words
                    words[i].destroy();
                }                    
                break; //Only match for the first occurance, exit the loop
            }
        }
    }
}