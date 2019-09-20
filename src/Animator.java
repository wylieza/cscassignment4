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
        while(!wr.dropped()){
            if(wr.getSpeed()/100 <= (System.currentTimeMillis()-baseTime)){
                baseTime = System.currentTimeMillis();
                wr.setY(wr.getY()+1);
                w.repaint();
            }
        }

        s.missedWord();
        wr.resetWord();
        this.run();
        

    }
}