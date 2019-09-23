import javax.swing.JOptionPane;

//Seperated from the 'score' class so as to comply with MVC design

public class Tracker extends Thread{

    private int lastMissedWords;
	private int lastCaughtWords;
    private int lastGameScore;

    private int latestMissedWords;
	private int latestCaughtWords;
    private int latestGameScore;
    private int wordsLeft;

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
        while(WordApp.live){

            //Synchonize these lines:
            latestMissedWords = s.getMissed();
	        latestCaughtWords = s.getCaught();
            latestGameScore = s.getScore();
            wordsLeft = WordApp.wordsLeft.get();

            //Update based on the coherent data
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

            if (wordsLeft == 0){
                System.out.println("All words finnished falling");
                showCompletedMessage = true;
                break;
            }

            if((System.currentTimeMillis()-lastTime) > 1000/framesPerSec){
                w.repaint();
                lastTime = System.currentTimeMillis();
            }

        }
        w.repaint();
        //End of game procedure... Was 'END' button pressed or did all words fall?
        if(showCompletedMessage){
            JOptionPane.showMessageDialog(null, "Nice Work!\nMissed Words: " + lastMissedWords + "\nCaught Words: " + lastCaughtWords + "\nScore: " + lastGameScore);
        }else{
            System.out.println("Game ended by user!");
        }
        WordApp.live = false;
        System.out.println("Tracker killed");

    }
    
}


		
		