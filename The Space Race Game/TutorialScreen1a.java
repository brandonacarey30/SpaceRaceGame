// JavaScript Document
import java.awt.DisplayMode;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import ScreenManagement.ScreenManager;
import java.awt.*;

public class TutorialScreen1a {
	/* refer to the original TutorialScreen.java in order to find notes and comments on this page. 
	 * This page and all of the others 2-8 are different variations of the TutorialScreen.java . 
	 * The only thing that is changed between them is the different files, sources, and variables they use and create.
	 */

    public TutorialScreen1a(){
    }
	 public static ScreenManager TutorialScreen10;
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
     	TutorialScreen10 = new ScreenManager();;
         try {
             DisplayMode displayMode =
             TutorialScreen10.findFirstCompatibleMode(POSSIBLE_MODES);
             TutorialScreen10.setFullScreen(displayMode);
             TutorialScreen10.getFullScreenWindow();
             Window windowStart = TutorialScreen10.getFullScreenWindow();
             windowStart.addKeyListener(new KeyClass());
             loadImages();
             animationLoop();
         }
         finally {
        	 TutorialScreen10.restoreScreen();
         }
     }
     
	public static void loadImages() {
        //loads images into buffer
         bgImage = loadImage("images/1.5rules1.5.jpg");
         title = loadImage("images/title.png");
        }
	
	
    public static void animationLoop() 
    {
    	while(setState == 0)
    	{
    		
    		Graphics2D g = TutorialScreen10.getGraphics();
    		draw(g);
    		g.dispose();
    		TutorialScreen10.update();
    		
    		try {
    			Thread.sleep(20);
    		}
    		catch (InterruptedException ex) { }
    	}
    	if (setState == 1)
    	{
    		setState = 0;
    		MainScreen ms = new MainScreen();
    		TutorialScreen10.restoreScreen();
    		ms.run();
    	}
    	else if(setState == 2)
    	{
    		TutorialScreen2a ts2a = new TutorialScreen2a();
        	TutorialScreen10.restoreScreen();
        	ts2a.run();
    	}
    /*	else if(setState== 3){
    		TutorialScreen5 ts5 = new TutorialScreen5();
    		TutorialScreen10.restoreScreen();                      // This will make the tutorial loop back to the previous screen. Currently flickers and does not stop.
    		ts5.run(); 
    	} */
    }
        private static void draw(Graphics2D g) {
        	 g.drawImage(bgImage, 0, 0, null);
		
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
  			   else if(keyCode == KeyEvent.VK_RIGHT)
  				   {
  				   		setState =2;
  				   }
  			   else if(keyCode == KeyEvent.VK_LEFT){
  				   setState = 3;
  			   } 
  			   
}}}
