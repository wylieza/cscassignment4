public class Animator extends Thread{

    WordPanel w;
    WordRecord wr;
    Score s;
    long baseTime;

    public Animator(WordRecord wr, WordPanel w, Score s){
        this.w = w;
        this.wr = wr;
        this.s = s;
    }

    public void run(){
        baseTime = System.currentTimeMillis();
        while(!wr.dropped() && wr.enabled()){
            if(!WordApp.live){
                break; //Game ended by user, kill thread
            }
            if(wr.getSpeed()/30 <= (System.currentTimeMillis()-baseTime)){ //use 20 for good speed
                baseTime = System.currentTimeMillis();
                wr.setY(wr.getY()+1);
                w.repaint();
            }
        }

        if(wr.enabled()){
            s.missedWord();
            WordApp.wordsLeft.getAndDecrement();
        }

        //TODO: Syncronize this
        if(WordApp.wordsLeft.get() >= WordApp.noWords && WordApp.live){
            wr.resetWord();
            this.run();       
        }else{
            wr.destroy();
            //Tracker will track when all words destroyed and end the game when nesesseary
        }
        System.out.println("Animator killed");
    }
}