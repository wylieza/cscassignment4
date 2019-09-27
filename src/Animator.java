import java.util.concurrent.atomic.*;

public class Animator extends Thread{

    WordPanel w;
    WordRecord wr;
    Score s;
    long baseTime;
    int speedDivisor;
    

    public Animator(WordRecord wr, WordPanel w, Score s){
        this.w = w;
        this.wr = wr;
        this.s = s;
        speedDivisor = 30;
    }

    public void run(){

                
        baseTime = System.currentTimeMillis();
        while(!wr.dropped()){
            if(!Score.live){
                break; //Game ended by user, kill thread
            }

            if(wr.getSpeed()/speedDivisor <= (System.currentTimeMillis()-baseTime)){ //Check if it is time to increment y position
                baseTime = System.currentTimeMillis();
                if(wr.setY(wr.getY()+1)){ //Returns if word is dropped or not
                    s.missedWord();
                    Score.wordsLeft.getAndDecrement();
                }                
            }                        
        }

        //Syncronized because if two words both drop at the same time we could have a race condition
        boolean resetWord = false;
        synchronized(Score.wordsLeft){
            if(Score.wordsLeft.get() >= WordApp.noWords && Score.live){
                wr.resetWord();
                resetWord = true;
            }
        }
        if (resetWord){
                this.run();              
        }else{
            wr.destroy();
            w.repaint();
        }

    }
}