import java.util.concurrent.atomic.AtomicInteger;

//Seperated from the 'score' class so as to comply with MVC design

public class Tracker extends Thread{

    private int lastMissedWords;
	private int lastCaughtWords;
    private int lastGameScore;

    private int latestMissedWords;
	private int latestCaughtWords;
    private int latestGameScore;

    private int lastWordsLeft;

    private int framesPerSec;
    private long lastTime;

    private boolean showCompletedMessage;

    WordPanel w;
    Score s;

    public Tracker(WordPanel w, Score s){
        this.w = w;
        this.s = s;
        showCompletedMessage = false;

        framesPerSec = 60;
        lastTime = System.currentTimeMillis();

        //Set last values to force update after game restart event
        lastMissedWords = -1;
        lastCaughtWords = -1;
        lastGameScore = -1;
    }

    public void run(){
        while(Score.live){

            //Gather coherent data
            do { //Loop through until sure of a coherent set of results
                lastWordsLeft = Score.wordsLeft.get();
                latestMissedWords = s.getMissed();
                latestCaughtWords = s.getCaught();
                latestGameScore = s.getScore();
            }while(Score.wordsLeft.get() != lastWordsLeft);

            //Update based on the coherent data
            if (lastWordsLeft <= 0){ //Check if the number of words left is zero
                showCompletedMessage = true;
                //Dont break here, but allow one last sweep through to perform final updates
            }

            if(lastMissedWords < latestMissedWords){
                lastMissedWords = latestMissedWords; //This must happend before the update and then the update must happen on the local variable so we don't 'miss' a score
                WordApp.updateMissed("Missed:" + lastMissedWords + "    ");
                w.repaint();
            }
            if(lastCaughtWords < latestCaughtWords){
                lastCaughtWords = latestCaughtWords;
                WordApp.updateCaught("Caught: " + lastCaughtWords + "    ");
                w.repaint();
            }
            if(lastGameScore < latestGameScore){
                lastGameScore = latestGameScore;
                WordApp.updateScore("Score:" + lastGameScore + "    "); 
                w.repaint();
            }

            if(showCompletedMessage){
                //After the last sweep of updates, break to display completed message
                break;
            }

            if((System.currentTimeMillis()-lastTime) > 1000/framesPerSec){
                w.repaint();
                lastTime = System.currentTimeMillis();
            }

        }
        w.repaint();
        Score.live = false;
        if(showCompletedMessage){ //Only show message if the user completed the game
            w.displayDialog("Nice Work!\nMissed Words: " + lastMissedWords + "\nCaught Words: " + lastCaughtWords + "\nScore: " + lastGameScore);
        }else{
            System.out.println("Game ended by user!");
        }

    }
    
}


		
		