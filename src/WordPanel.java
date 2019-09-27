import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

import java.util.concurrent.atomic.AtomicInteger;

public class WordPanel extends JPanel implements Runnable {
		public static volatile boolean done;
		private WordRecord[] words;
		private int noWords;
		private int maxY;

		static Tracker tracker;
		static Animator[] animators;

		
		public void paintComponent(Graphics g) {
		    int width = getWidth();
		    int height = getHeight();
		    g.clearRect(0,0,width,height);
		    g.setColor(Color.red);
		    g.fillRect(0,maxY-10,width,height);

		    g.setColor(Color.black);
		    g.setFont(new Font("Helvetica", Font.PLAIN, 26));
		   //draw the words
		   //animation must be added 
		    for (int i=0;i<noWords;i++){	    	
		    	//g.drawString(words[i].getWord(),words[i].getX(),words[i].getY());	
				g.drawString(words[i].getWord(),words[i].getX(),words[i].getY()-10);  //y-offset for skeleton so that you can see the words	

		    }
		   
		  }
		
		WordPanel(WordRecord[] words, int maxY) {
			this.words=words;
			noWords = words.length;
			done=false;
			this.maxY=maxY;		
		}

		public void displayDialog(String message){
			JOptionPane.showMessageDialog(null, message);
		}
		
		public void run() {
			Score.wordsLeft = new AtomicInteger(WordApp.totalWords);
			animators = new Animator[words.length];

			WordApp.score.resetScore(); //Reset for new game

			for (int i=0;i<words.length;i++) {
				words[i].resetWord(); //Reset for new game
				animators[i] = new Animator(words[i], this, WordApp.score); //Create animator for each word
			}
			
			tracker = new Tracker(this, WordApp.score); //Create the game tracker

			//Start up threads
			Score.live = true; //Must be set to true before starting threads

			tracker.start(); //Start the game tracker
			for (int i=0;i<noWords;i++) {
				animators[i].start(); //Start the word animation
			}
		}

	}


