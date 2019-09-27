import java.util.concurrent.atomic.AtomicInteger;

public class Score {
	private int missedWords;
	private int caughtWords;
	private int gameScore;

	public static AtomicInteger wordsLeft;
	static volatile boolean live; //Boolean to say if game is in 'running' state
	
	Score() { 
		missedWords=0;
		caughtWords=0;
		gameScore=0;
	}
	
	public synchronized int getMissed() {
		return missedWords;
	}

	public synchronized int getCaught() {
		return caughtWords;
	}
	
	public synchronized int getTotal() {
		return (missedWords+caughtWords);
	}

	public synchronized int getScore() {
		return gameScore;
	}
	
	public synchronized void missedWord() {
		missedWords++;
	}

	public synchronized void caughtWord(int length) {
		caughtWords++;
		gameScore+=length;
	}

	public synchronized void resetScore() {
		caughtWords=0;
		missedWords=0;
		gameScore=0;
	}
}
