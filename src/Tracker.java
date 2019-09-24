import javax.swing.JOptionPane;
import java.util.concurrent.atomic.AtomicInteger;

//Seperated from the 'score' class so as to comply with MVC design

public class Tracker extends Thread{

    private int lastMissedWords;
	private int lastCaughtWords;
    private int lastGameScore;

    private int latestMissedWords;
	private int latestCaughtWords;
    private int latestGameScore;

    static volatile boolean live; //Boolean to say if game is in 'running' state //MOVE TO TRACKER
	/*Running state is activated after the start button is pressed
	* Running state is deactivated when END button is pressed?
	*/

    public static AtomicInteger wordsLeft; //MOVE TO TRACKER

    private int framesPerSec;
    private long lastTime;

    private boolean showCompletedMessage;

    WordPanel w;
    Score s;

    public Tracker(WordPanel w, Score s){
        this.w = w;
        this.s = s;
        showCompletedMessage = false;

        framesPerSec = 30;
        lastTime = System.currentTimeMillis();

        //Set last values to force update after game restart event
        lastMissedWords = -1;
        lastCaughtWords = -1;
        lastGameScore = -1;
    }

    public void run(){
        while(live){

            //Synchonize these lines:
            latestMissedWords = s.getMissed();
	        latestCaughtWords = s.getCaught();
            latestGameScore = s.getScore();

            //Update based on the coherent data

            if (wordsLeft.get() == 0){ //Check if the number of words left is zero
                System.out.println("All words finnished falling");
                showCompletedMessage = true;
                //Dont break here, but allow one last sweep through to perform final updates
            }

            if(lastMissedWords < latestMissedWords){
                lastMissedWords = latestMissedWords; //This must happend before the update and then the update must happen on the local variable so we don't 'miss' a score
                WordApp.updateMissed("Missed:" + lastMissedWords + "    ");
                System.out.println("Update missed Word counter diplay!");
                w.repaint();
            }
            if(lastCaughtWords < latestCaughtWords){
                lastCaughtWords = latestCaughtWords;
                WordApp.updateCaught("Caught: " + lastCaughtWords + "    ");
                System.out.println("Update display caught counter");
                w.repaint();
            }
            if(lastGameScore < latestGameScore){
                lastGameScore = latestGameScore;
                WordApp.updateScore("Score:" + lastGameScore + "    "); 
                System.out.println("Current Score: " + lastGameScore);
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
        live = false;
        //End of game procedure... Was 'END' button pressed or did all words fall?
        if(showCompletedMessage){
            JOptionPane.showMessageDialog(null, "Nice Work!\nMissed Words: " + lastMissedWords + "\nCaught Words: " + lastCaughtWords + "\nScore: " + lastGameScore);
        }else{
            System.out.println("Game ended by user!");
        }
        System.out.println("Tracker killed");

    }
    
}


		
		