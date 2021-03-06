import java.awt.DisplayMode;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import ScreenManagement.ScreenManager;
import java.awt.*;

public class StoryScreen {
	

    public StoryScreen(){
    }
	 public static ScreenManager StoryScreen;
     public static Image title;
	 public static int p1Color = 0;
     public static int p2Color = 1;
     public static int wind = 0;
     public static int GameState =1;
     public static int setState = 0;
     public static Image bgImage;

     
     
	
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
     	StoryScreen = new ScreenManager();;
         try {
             DisplayMode displayMode =
             StoryScreen.findFirstCompatibleMode(POSSIBLE_MODES);
             StoryScreen.setFullScreen(displayMode);
             StoryScreen.getFullScreenWindow();
             Window windowStart = StoryScreen.getFullScreenWindow();
             windowStart.addKeyListener(new KeyClass());
             loadImages();
             animationLoop();
         }
         finally {
        	 StoryScreen.restoreScreen();
         }
     }
     
	public static void loadImages() {
        //loads images into buffer
         bgImage = loadImage("images/StoryScreen.jpg"); // load in the background picture
         title = loadImage("images/title.png");		
        }
	
	
    public static void animationLoop() 
    {
    	while(setState == 0)
    	{
    		
    		Graphics2D g = StoryScreen.getGraphics(); // sets up the background
    		draw(g); 									//puts the background on the screen.
    		g.dispose();								// gets rid of the background screen. 
    		StoryScreen.update();					// refresh the screen.
    		
    		try {
    			Thread.sleep(20);
    		}
    		catch (InterruptedException ex) { }
    	}
    	if (setState == 1)
    	{
    		setState = 0;
    		MainScreen ms = new MainScreen();
    		StoryScreen.restoreScreen(); 						// go back to the main screen. (new main screen, not the old one)
    		ms.run();
    	}
    	/*else if(setState == 2)
    	{
    		StoryScreen2 ts2 = new StoryScreen2(); 			//creation of the new slide.
        	StoryScreen.restoreScreen();
        	ts2.run();
    	}*/
    }
        private static void draw(Graphics2D g) {
        	 g.drawImage(bgImage, 0, 0, null);					  // draws the background screen. (the image seen)
		
	}
		public static class KeyClass extends KeyAdapter
        {
      
  	       public void keyPressed(KeyEvent e) 
  	       {
  	    	   int keyCode = e.getKeyCode();
  	
  			   if (keyCode == KeyEvent.VK_ESCAPE) 
  			   {
  				   DataClass.setP1Tank(p1Color);
  				   DataClass.setP2Tank(p2Color);                  //
  				   DataClass.setwind(wind);						  //	Setting up the main screen.
  				   DataClass.setGameState(GameState);
  				 
  				   setState = 1; // go back to the main screen.
  				   
  			   }
  			   /*else if(keyCode == KeyEvent.VK_RIGHT) //when right it pressed.
  				   {
  				setState =2; // set state to 2 which will go to the next slide
  				 }
  			 else if(keyCode == KeyEvent.VK_LEFT){ // when left is pressed.
				   setState = 3; // set the state to 3. This will make a new game start.
  			 }*/
  			   
}}}
