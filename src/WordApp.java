import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;


import java.util.Scanner;
import java.util.concurrent.*;
//model is separate from the view.
import java.util.concurrent.atomic.AtomicInteger;

public class WordApp {
//shared variables
	static int noWords=4;
	static int totalWords;

	static int frameX=1000;
	static int frameY=600;
	static int yLimit=480;

	static WordDictionary dict = new WordDictionary(); //use default dictionary, to read from file eventually

	static Tracker tracker;
	static Animator[] animators;
	static WordRecord[] words;
	static volatile boolean done;  //must be volatile
	static 	Score score = new Score();

	static volatile boolean live; //Boolean to say if game is running
	static AtomicInteger wordsLeft;

	static WordPanel w;

	//Declare updatable labels globally
	static JLabel caught;
	static JLabel missed;
	static JLabel scr;
	
	
	
	public static void setupGUI(int frameX,int frameY,int yLimit) {
		// Frame init and dimensions
		JFrame frame = new JFrame("WordGame"); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(frameX, frameY);
		
		JPanel g = new JPanel();
		g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS)); 
		g.setSize(frameX,frameY);

		
		w = new WordPanel(words,yLimit);
		w.setSize(frameX,yLimit+100);
		g.add(w);
		
		
		JPanel txt = new JPanel();
		txt.setLayout(new BoxLayout(txt, BoxLayout.LINE_AXIS)); 
		caught =new JLabel("Caught: " + score.getCaught() + "    ");
		missed =new JLabel("Missed:" + score.getMissed()+ "    ");
		scr =new JLabel("Score:" + score.getScore()+ "    ");    
		txt.add(caught);
		txt.add(missed);
		txt.add(scr);
		
	
		//[snip]
		System.out.println("GUI setup run...");
		

		final JTextField textEntry = new JTextField("",20);
		textEntry.addActionListener(new ActionListener()
			{
			public void actionPerformed(ActionEvent evt) {
				String text = textEntry.getText();
				//[snip]
				for (int i = 0; i < words.length && live; i++){
					if (words[i].matchWord(text)){
						score.caughtWord(text.length());
						if(wordsLeft.getAndDecrement() <= noWords){ //Check for OB1E
							words[i].destroy();
						}
						break; //Only match the first if two of same on display
					}
				}

				System.out.println(text); 															//Testing
				
				textEntry.setText("");
				textEntry.requestFocus();
			}
		});
	
	txt.add(textEntry);
	txt.setMaximumSize( txt.getPreferredSize() );
	g.add(txt);
		
		JPanel b = new JPanel();
		b.setLayout(new BoxLayout(b, BoxLayout.LINE_AXIS)); 
		JButton startB = new JButton("Start");;
		
			// add the listener to the jbutton to handle the "pressed" event
			startB.addActionListener(new ActionListener()
			{
			public void actionPerformed(ActionEvent e)
			{
				//[snip]
				System.out.println("Start Button Pressed");							//testing START
				if(!live){
					live = true;
					tracker.start();
					for (int i=0;i<noWords;i++) {
						words[i].setEnabled(true); //Prevent cheating
						animators[i].start();
					}
				}

				textEntry.requestFocus();  //return focus to the text entry field
			}
			});
		JButton endB = new JButton("End");;
			
				// add the listener to the jbutton to handle the "pressed" event
				endB.addActionListener(new ActionListener()
				{
				public void actionPerformed(ActionEvent e)
				{
					//[snip]
					System.out.println("End button pressed, 'halt the game'"); 									//testing
				}
				});
		
		b.add(startB);
		b.add(endB);
		
		g.add(b);
		
		frame.setLocationRelativeTo(null);  // Center window on screen.
		frame.add(g); //add contents to window
		frame.setContentPane(g);     
		//frame.pack();  // don't do this - packs it into small space
		frame.setVisible(true);
		
		
	}

	
public static String[] getDictFromFile(String filename) {
		String [] dictStr = null;
		try {
			Scanner dictReader = new Scanner(new FileInputStream(filename));
			int dictLength = dictReader.nextInt();
			//System.out.println("read '" + dictLength+"'");

			dictStr=new String[dictLength];
			for (int i=0;i<dictLength;i++) {
				dictStr[i]=new String(dictReader.next());
				//System.out.println(i+ " read '" + dictStr[i]+"'"); //for checking
			}
			dictReader.close();
		} catch (IOException e) {
			System.err.println("Problem reading file " + filename + " default dictionary will be used");
		}
		return dictStr;

	}

	public static void updateMissed(String update){
		WordApp.missed.setText(update);
	}

	public static void updateCaught(String update){
		WordApp.caught.setText(update);
	}

	public static void updateScore(String update){
		WordApp.scr.setText(update);
	}

	public static void main(String[] args) {
		
		//deal with command line arguments
		if(args.length == 3){
			totalWords=Integer.parseInt(args[0]);  //total words to fall
			noWords=Integer.parseInt(args[1]); // total words falling at any point
			assert(totalWords>=noWords); // this could be done more neatly
			String[] tmpDict=getDictFromFile(args[2]); //file of words
			if (tmpDict!=null)
				dict= new WordDictionary(tmpDict);
		}else{
			totalWords = 20;
			noWords = 5;
		}
		
		WordRecord.dict=dict; //set the class dictionary for the words.
		
		words = new WordRecord[noWords];  //shared array of current words
		animators = new Animator[noWords];
		
		//[snip]
		live = false; //Set the game to not running until start pressed

		setupGUI(frameX, frameY, yLimit);  
		//Start WordPanel thread - for redrawing animation

		int x_inc=(int)frameX/noWords;
		//initialize shared array of current words

		wordsLeft = new AtomicInteger(totalWords);

		for (int i=0;i<noWords;i++) {
			words[i]=new WordRecord(dict.getNewWord(),i*x_inc,yLimit);
			animators[i] = new Animator(words[i], w, score);
		}
		
		tracker = new Tracker(w, score);
			

	}

}
