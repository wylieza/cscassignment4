import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;


import java.util.Scanner;
import java.util.concurrent.*;


public class WordApp {
	static int noWords=4;
	static int totalWords;

	static int frameX=1000;
	static int frameY=600;
	static int yLimit=480;

	static WordDictionary dict = new WordDictionary();

	static WordRecord[] words;
	static volatile boolean done;  //must be volatile
	static 	Score score = new Score();
	static ProcessAction action;	

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

		final JTextField textEntry = new JTextField("",20);
		textEntry.addActionListener(new ActionListener()
			{
			public void actionPerformed(ActionEvent evt) {
				String text = textEntry.getText();
				action.guess(text);

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
				if(!Score.live){
					(new Thread(w)).start();		
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
					action.endAction();
				}
				});

		JButton quitB = new JButton("Quit");;
	
		// add the listener to the jbutton to handle the "pressed" event
		quitB.addActionListener(new ActionListener()
		{
		public void actionPerformed(ActionEvent e)
		{			
			action.quitAction();
		}
		});
		
		b.add(startB);
		b.add(endB);
		b.add(quitB);
		
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

			dictStr=new String[dictLength];
			for (int i=0;i<dictLength;i++) {
				dictStr[i]=new String(dictReader.next());
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

	/**
	 * @param args The arguments for configuring the game
	 */
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

		action = new ProcessAction(w, words, score);
		
		Score.live = false; //Set the game to not running until start pressed

		setupGUI(frameX, frameY, yLimit);  
		//Start WordPanel thread - for redrawing animation

		int x_inc=(int)frameX/noWords;
		//initialize shared array of current words

		for (int i=0;i<noWords;i++) {
			words[i]=new WordRecord(dict.getNewWord(),i*x_inc,yLimit);
			words[i].destroy();
		}
			

	}

}
