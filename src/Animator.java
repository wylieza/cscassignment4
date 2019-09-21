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
            if(wr.getSpeed()/20 <= (System.currentTimeMillis()-baseTime)){
            //if(wr.getSpeed()/20 <= (System.currentTimeMillis()-baseTime)){
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
        if(WordApp.wordsLeft.get() >= WordApp.noWords){
            wr.resetWord();
            this.run();       
        }else{
            wr.destroy();
            if (WordApp.wordsLeft.get() == 0){
                //END Game
                System.out.println("Game over");
            }
        }
    }
}