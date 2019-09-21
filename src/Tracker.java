public class Tracker extends Thread{

    private int lastMissedWords;
	private int lastCaughtWords;
	private int lastGameScore;

    WordPanel w;
    Score s;

    public Tracker(WordPanel w, Score s){
        this.w = w;
        this.s = s;
    }

    public void run(){
        while(WordApp.live){
            if(lastMissedWords < s.getMissed()){
                lastMissedWords = s.getMissed(); //This must happend before the update and then the update must happen on the local variable so we don't 'miss' a score
                WordApp.updateMissed("Missed:" + lastMissedWords + "    ");
                System.out.println("Missed Word!");
                w.repaint();
            }
            if(lastCaughtWords < s.getCaught()){
                lastCaughtWords = s.getCaught();
                WordApp.updateCaught("Caught: " + lastCaughtWords + "    ");
                System.out.println(lastCaughtWords);
                w.repaint();
            }
            if(lastGameScore < s.getScore()){
                lastGameScore = s.getScore();
                WordApp.updateScore("Score:" + lastGameScore + "    "); 
                System.out.println("Current Score " + lastGameScore);
                w.repaint();
            }

            if (WordApp.wordsLeft.get() == 0){
                System.out.println("Tracker end game here");
            }
        }
        //End of game procedure...?
    }
    
}


		
		