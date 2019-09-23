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
        speedDivisor = 10;
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
                w.repaint();
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
            WordApp.wordsLeft.getAndDecrement();
        }

        //TODO: Syncronize this
        if(WordApp.wordsLeft.get() >= WordApp.noWords && WordApp.live){
            wr.resetWord();
            this.run();       
        }else{
            wr.destroy();
            System.out.println("Animator killed");
            //Tracker will track when all words destroyed and end the game when nesesseary
        }        
    }
}