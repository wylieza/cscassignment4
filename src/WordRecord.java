//This class is a signle word to be displayed. It can have its position, text etc all set and read

public class WordRecord {
	private String text;
	private  int x;
	private int y;
	private int maxY;
	private boolean dropped;
	
	private int fallingSpeed;
	private static int maxWait=1500;
	private static int minWait=100;

	public static WordDictionary dict;
	

	
	WordRecord() {
		text="";
		x=0;
		y=0;	
		maxY=300;
		dropped=false;
		fallingSpeed=(int)(Math.random() * (maxWait-minWait)+minWait); 
	}
	
	WordRecord(String text) {
		this();
		this.text=text;
	}
	
	WordRecord(String text,int x, int maxY) {
		this(text);
		this.x=x;
		this.maxY=maxY;
	}

	public synchronized boolean setY(int y) { //Modified
		this.y=y;
		if (y>maxY) {
			y=maxY;
			dropped=true;
		}else{
			return false;
		}
		return dropped;
	}
	
	public synchronized  void setX(int x) {
		this.x=x;
	}
	
	public synchronized  void setWord(String text) {
		this.text=text;
	}

	public synchronized  String getWord() {
		return text;
	}
	
	public synchronized  int getX() {
		return x;
	}	
	
	public synchronized  int getY() {
		return y;
	}
	
	public synchronized  int getSpeed() {
		return fallingSpeed;
	}

	public synchronized void setPos(int x, int y) {
		setY(y);
		setX(x);
	}
	public synchronized void resetPos() {
		setY(0);
	}

	public synchronized void resetWord() {
		resetPos();
		text=dict.getNewWord();
		dropped=false;
		fallingSpeed=(int)(Math.random() * (maxWait-minWait)+minWait); 
	}
	
	public synchronized boolean matchWord(String typedText) {
		if (typedText.equals(this.text) && !dropped) {
			resetWord();
			return true;
		}
		else
			return false;
	}
	

	public synchronized  void drop(int inc) {
		setY(y+inc);
	}
	
	public synchronized  boolean dropped() {
		return dropped;
	}

	public synchronized void destroy(){
		dropped = true;
		y = 0;
	}

}
