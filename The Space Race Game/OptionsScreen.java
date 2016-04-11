
/**
 * HelpScreen is for display
 */
import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import ScreenManagement.*;


public class OptionsScreen {

    public OptionsScreen() {
    }
     public static SoundPlayer music;
     public static ScreenManager HelpScreen;
     public static Image bgImage;
     public static Image title;
     public static int setState = 0;
     public static String tankColors[] = {"Green", "Red", "Blue", "Yellow", "White"};
     public static int p1Color = 0;
     public static int p2Color = 1;
     public static int GameState =1;
     public static DataClass dc = new DataClass();
     public static int wind = 0;
     public static JTextArea Name1 = new JTextArea();
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
        	/*//Initializes music for options screen
        	music = new SoundPlayer();
			music.load("doom_1.mid");
			music.play();*/
        	
            DisplayMode displayMode =
            HelpScreen.findFirstCompatibleMode(POSSIBLE_MODES);
            HelpScreen.setFullScreen(displayMode);
            //Name1.setEditable(true);
            Name1.setAlignmentX(0);
            Name1.setAlignmentY(0);
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
        // loads images into buffer
         bgImage = loadImage("images/optionScreen.jpg");
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
     	  // g.drawImage(title, 170,120,null);
		  g.setColor(Color.BLUE);
		  g.drawString("Press N to change your name.", 260, 238);
		  g.setColor(Color.black);
		  

		  g.drawString("Player one choose color with up/down arrows", 260, 268);
		  g.drawString("Player two choose color with left/right arrows", 260, 293);
		  g.drawString("Player one color = " + tankColors[p1Color], 260, 318);
		  g.drawString("Player two color = " + tankColors[p2Color], 260, 343);
		  g.setColor(Color.blue);
		  g.drawString("Press 'W' to cycle wind options:", 260, 378);
		  g.setColor(Color.black);
		  if(wind==2)
			  g.drawString("Wind is on and constant.", 260, 403);
		  else if(wind==3)
			  g.drawString("Wind is completely randomized.", 260, 403);
		  else if(wind==1)
			  g.drawString("Wind is randomized after every shot.", 260, 403);
		  else
			  g.drawString("Wind is Off.", 260, 403);  
		  g.setColor(Color.blue);
		  g.drawString("Press 'G' to change game dynamics.", 260, 438);
		  g.setColor(Color.black);
		  if(GameState==1)
		  g.drawString("Game is turn based.", 260, 463);
		  else
		  g.drawString("Game is dynamic.", 260, 463);  
		  g.setColor(Color.blue);
		  g.drawString("Press Esc to return to the main menu.", 285, 518);
     }//John Danz 10/21/08: made Esc the universal "bring me back to the main menu button", 
     //except if you are at the main menu then you exit the game
     
      public static class KeyClass extends KeyAdapter
      {
    
	       public void keyPressed(KeyEvent e) 
	       {
	    	   int keyCode = e.getKeyCode();
	
			   //if (keyCode == KeyEvent.VK_ESCAPE) 
			  // {
				//   System.exit(-1);
			  // }
			   /*else*/ if (keyCode == KeyEvent.VK_ESCAPE)
			   {
				   DataClass.setP1Tank(p1Color);
				   DataClass.setP2Tank(p2Color);
				   DataClass.setwind(wind);
				   DataClass.setGameState(GameState);
				   setState = 1;
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
			   else if (keyCode == KeyEvent.VK_N) {
				   String player1Name = JOptionPane.showInputDialog("Player 1: Please enter your name.",true); 
				   String player2Name = JOptionPane.showInputDialog("Player 2: Please enter your name.",true);
				   
			   }
			}
	    }
     
      	
}