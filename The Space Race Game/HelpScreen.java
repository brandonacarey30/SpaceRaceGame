/**
 * HelpScreen is for display
 */
import java.awt.*;

import javax.swing.ImageIcon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import ScreenManagement.*;

public class HelpScreen {

    public HelpScreen() {
    }
    
    //help screen comment
     public static SoundPlayer music;
     public static ScreenManager HelpScreen;
     public static Image bgImage;
     public static Image title;
     public static int setState = 0;
     public static int wind = 0;
     public static int GameState =1;
     public static String tankColors[] = {"Green", "Red", "Blue", "Yellow", "White"};
     public static int p1Color = 0;
     public static int p2Color = 1;
     public static int time=40; // the amount of time for each players turn. Can be changed from the control/option screen from the main screen.
     public static DataClass dc = new DataClass();
     public static JTextArea Name1 = new JTextArea();
     public static String player1Name="Player 1";
     public static String player2Name="Player 2";
     
     
       private static final DisplayMode POSSIBLE_MODES[] = {
        new DisplayMode(800, 600, 32, 0),
        new DisplayMode(800, 600, 24, 0),
        new DisplayMode(800, 600, 16, 0),
        new DisplayMode(640, 480, 32, 0),
        new DisplayMode(640, 480, 24, 0),
        new DisplayMode(640, 480, 16, 0)
    };


    private static Image loadImage(String fileName) {
        return new ImageIcon(fileName).getImage();
    }
     
     public static void run() {
    	HelpScreen = new ScreenManager();;
        try {
            DisplayMode displayMode =
            HelpScreen.findFirstCompatibleMode(POSSIBLE_MODES);
            HelpScreen.setFullScreen(displayMode);
            Name1.setEditable(true);
            Name1.setAlignmentX(500);
            Name1.setAlignmentY(500);
            Name1.setEnabled(true);
            HelpScreen.getFullScreenWindow().add(Name1);
            Window windowStart = HelpScreen.getFullScreenWindow();
            windowStart.addKeyListener(new KeyClass());
            loadImages();
            animationLoop();
            // create another animationloop in StartScreen.Java which will display Start selection
            //After user makes selections and begins game, return Start=1 to intiate Game phase.
        	}
     finally {
    	 HelpScreen.restoreScreen();
        }
    }
    
    public static void loadImages() {
        //loads images into buffer
         bgImage = loadImage("images/helpScreen.jpg");
         title = loadImage("images/title.png");
		//add animation here for start screen
    }
    
        public static void animationLoop() 
        {
        	while(setState == 0)
        	{
        		Graphics2D g = HelpScreen.getGraphics();
        		draw(g);
        		g.dispose();
        		HelpScreen.update();
        		
           
        		// take a nap to catch up garage stuff
        		try {
        			Thread.sleep(20);
        		}
        		catch (InterruptedException ex) { }
        	}
        	if (setState == 1)
        	{
        		setState = 0;
        		MainScreen ms = new MainScreen();
        		HelpScreen.restoreScreen();
        		ms.run();
        	}
        
     }
     
     public static synchronized void draw(Graphics g) {
    	 g.drawImage(bgImage, 0, 0, null);
		  g.setColor(Color.black);
		  Font f = g.getFont();
		  g.setFont(new Font("SansSerif", Font.BOLD, 12));
		  g.drawString("Controls:",235,250);
		  g.setFont(f);
		  g.setColor(Color.blue);
		  g.setFont(new Font("SansSerif", Font.BOLD, 12));
          g.drawString("P1:",406,250);
          g.setFont(f);
		  g.setColor(Color.black);
          g.drawString("Turret Movement:",235,275);
          g.drawString("W, S",406,275);
          g.drawString("Power Adjustment:",235,300);
          g.drawString("Q, E",406,300);
          g.drawString("Tank Movement:",235,325);
          g.drawString("A, D",406,325);
          g.drawString("Cycle Weapons:",235,350);
          g.drawString("SHIFT",406,350);
		  g.drawString("Fire:",235,375);
		  g.drawString("Ctrl",406,375);
		  g.setColor(Color.RED);
		  g.setFont(new Font("SansSerif", Font.BOLD, 12));
          g.drawString("P2:",511,250);
          g.setFont(f);
		  g.setColor(Color.black);
          g.drawString("Up/Down Arrows",511,275);
   	   	  g.drawString("< and >",511,300);
   	   	  g.drawString("Left/Right Arrows",511,325);
   	   	  g.drawString("SHIFT",511,350);
		  g.drawString("Ctrl",511,375); 
   	   	  g.setColor(Color.black);
		  g.drawString("Cycle Wind: W", 235, 420);
		  g.setColor(Color.black);
		  
		  if(wind==2)
			  g.drawString("Wind is on and constant.", 470, 420);
		  else if(wind==3)
			  g.drawString("Wind is completely randomized.", 430, 420);
		  else if(wind==1)
			  g.drawString("Wind is randomized after every shot.", 410, 420);
		  else
			  g.drawString("Wind is Off.", 511, 420);  
		  
		  g.setColor(Color.black);
		  g.drawString("P1 Tank Color: Up/Down Arrows", 235, 440);
		  g.drawString("P2 Tank Color: Left/Right Arrows", 235, 460);
		  g.setColor(Color.black);
		  g.drawString("" + tankColors[p1Color], 511, 440);
		  g.drawString("" + tankColors[p2Color], 511, 460);
		 
		  g.setColor(Color.black);
		  g.drawString("Cycle Timer: < > ", 235, 480);
		  g.setColor(Color.black);
		  g.drawString(""+ time + " seconds", 511,480);
		  
		  g.setColor(Color.black);
		  g.drawString("Press Esc to Return to the Main Menu.",310,525); 

		  //g.drawString("Press any key to return to the main menu.", 275, 575);
     //John Danz 10/21/08: made Esc the universal "bring me back to the main menu button", 
     //except if you are at the main menu then you exit the game
      }
     
      public static class KeyClass extends KeyAdapter
      {
    
	       public void keyPressed(KeyEvent e) 
	       {
	    	   int keyCode = e.getKeyCode();
	
			   if (keyCode == KeyEvent.VK_ESCAPE) 
			   {
				   DataClass.setP1Tank(p1Color);
				   DataClass.setP2Tank(p2Color);
				   DataClass.setwind(wind);
				   DataClass.setGameState(GameState);
				   setState = 1;
			   }
			   else if (keyCode == KeyEvent.VK_W)
			   {
				   if(wind<3)
				   {
					   wind++;
				   }
				   else
				   {
					   wind=0;
				   }
			   }
			   else if (keyCode == KeyEvent.VK_G)
			   {
				   if(GameState==0)
				   {
					   GameState=1;
				   }
				   else
				   {
					   GameState=0;
				   }
			   }
			   else if (keyCode == KeyEvent.VK_LEFT)
			   {
				   if(p2Color > 0)
					   p2Color--;
			   }
			   else if (keyCode == KeyEvent.VK_RIGHT)
			   {
				   if(p2Color < tankColors.length - 1)
					   p2Color++;
			   }
			   else if (keyCode == KeyEvent.VK_UP)
			   {
				   if(p1Color < tankColors.length - 1)
					   p1Color++;
			   }
			   else if (keyCode == KeyEvent.VK_DOWN)
			   {
				   if(p1Color > 0)
					   p1Color--;
			   }
			   else if (keyCode==KeyEvent.VK_COMMA)
			   {
				   if(time>5)
				   {
					   time--;
				   }
			   }
			   else if (keyCode==KeyEvent.VK_PERIOD)
			   {
				   if(time<20)
				   {
					   time++;
				   }
			   }
			}
	    }
}