import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; 

import javax.swing.Timer;

public class CountdownTimer {
	  
	    static int counter = HelpScreen.time;  //the counter for the timer, this is what the timer starts at
	    private final static int COUNTER_DELAY=1000; //delays each tick of timer 1000 milliseconds
	    static Timer counterTimer;
	    
	    public static void paintComponent(Graphics g)
	    {
	    	if (counterTimer.isRunning())
	    	{
	    		counter--;  //counts down the timer by one
	    		if (counter==0)
	    		{
	    			Game.turn=(2/Game.turn);  //switches to other player's turn
	    			counterTimer.restart();  //restarts the timer
	    			counter=HelpScreen.time;//sets the timer to 10 to begin and count down again
	    		}
	    	}
	    }
	    
	    public static  void startCounter() {
	        if (counterTimer==null) {
	        	counter=HelpScreen.time;
	        	counterTimer=new Timer(COUNTER_DELAY, new TimeHandler()); //creates the timer
	        	counterTimer.start();    //starts the timer
	        }
	        else {
	           if (!counterTimer.isRunning())
	           {
	        	   counterTimer.restart();
	        	   counter=HelpScreen.time;
	           }
	        } 
	        
	    }
	    
	    public void stopCounter() {
	        counterTimer.stop();
	    }
	    
	    private static class TimeHandler implements ActionListener
	    {
	    	public void actionPerformed(ActionEvent actionEvent)
	    	{
	    		paintComponent(null);  //basically runs the timer 
	    	}
	    }
	}
 
 


