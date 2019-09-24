import java.util.concurrent.atomic.*;

public class Animator extends Thread{

    //public static AtomicBoolean repaintBusy; //Only call repaint if no other processes are currently refreshing
    WordPanel w;
    WordRecord wr;
    Score s;
    long baseTime;
    int speedDivisor;

    public Animator(WordRecord wr, WordPanel w, Score s){
        this.w = w;
        this.wr = wr;
        this.s = s;
        speedDivisor = 10;
        //repaintBusy = new AtomicBoolean(false);

        System.out.println("Animator created");
    }

    public void run(){
        
        baseTime = System.currentTimeMillis();
        while(!wr.dropped()){
            if(!Tracker.live){
                break; //Game ended by user, kill thread
            }

            if(wr.getSpeed()/speedDivisor <= (System.currentTimeMillis()-baseTime)){ //Check if it is time to increment y position
                baseTime = System.currentTimeMillis();
                if(wr.setY(wr.getY()+1)){ //Returns if word is dropped or not [Setting and checking if the word has dropped must be synchonized, else case: word caught and then destroyed, then anim. scans...will result in counting a miss CONFIRMED]
                    s.missedWord();
                    Tracker.wordsLeft.getAndDecrement();
                }                
            }                        
        }

        //TODO: Syncronize this
        if(Tracker.wordsLeft.get() >= WordApp.noWords && Tracker.live){
            wr.resetWord();
            this.run();       
        }else{
            wr.destroy();
            w.repaint(); //This must be here, because tracker may die before last thread dies at the end of the game...
            System.out.println("Animator killed");
            //Tracker will track when all words destroyed and end the game when nesesseary
        }        
    }
}