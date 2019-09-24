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
        speedDivisor = 100;
        //repaintBusy = new AtomicBoolean(false);

        System.out.println("Animator created");
    }

    public void run(){
        
        baseTime = System.currentTimeMillis();
        while(!wr.dropped() && wr.enabled()){
            if(!WordApp.live){
                break; //Game ended by user, kill thread
            }

            
            if(wr.getSpeed()/speedDivisor <= (System.currentTimeMillis()-baseTime)){ //use 20 for good speed
                baseTime = System.currentTimeMillis();
                wr.setY(wr.getY()+1);
                //if(repaintBusy.compareAndSet(false, true)){
                //w.repaint(); //Consider doing this in the track (like an FPS vibe?) -MUCH BETTER!!!!
                //    repaintBusy.set(false);
                //}
                
            }            

            /*
            try{
                wr.setY(wr.getY()+1);
                w.repaint();
                sleep(wr.getSpeed()/(speedDivisor));
            }catch(Exception e){
                System.out.println("Sleep exception: " + e.toString());
            }
            */
            
        }

        if(wr.enabled()){
            s.missedWord();
            Tracker.wordsLeft.getAndDecrement();
        }

        //TODO: Syncronize this
        if(Tracker.wordsLeft.get() >= WordApp.noWords && WordApp.live){
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