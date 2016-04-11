


import java.awt.DisplayMode;


import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import ScreenManagement.ScreenManager;
import java.awt.*;

/* refer to the original TutorialScreen.java in order to find notes and comments on this page. 
 * This page and all of the others 2-8 are different variations of the TutorialScreen.java . 
 * The only thing that is changed between them is the different files, sources, and variables they use and create.
 */
public class TutorialScreen2a {
	

    public TutorialScreen2a(){
    }
	 public static ScreenManager TutorialScreen2a;
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
     	TutorialScreen2a = new ScreenManager();;
         try {
             DisplayMode displayMode =
             TutorialScreen2a.findFirstCompatibleMode(POSSIBLE_MODES);
             TutorialScreen2a.setFullScreen(displayMode);
             TutorialScreen2a.getFullScreenWindow();
             Window windowStart = TutorialScreen2a.getFullScreenWindow();
             windowStart.addKeyListener(new KeyClass());
             loadImages();
             animationLoop();
         }
         finally {
        	 TutorialScreen2a.restoreScreen();
         }
     }
     
	public static void loadImages() {
        //loads images into buffer
         bgImage = loadImage("images/2.5controls2.5.jpg");
         title = loadImage("images/title.png");
        }
	
	
    public static void animationLoop() 
    {
    	while(setState == 0)
    	{
    		
    		Graphics2D g = TutorialScreen2a.getGraphics();
    		draw(g);
    		g.dispose();
    		TutorialScreen2a.update();
    		
    		try {
    			Thread.sleep(20);
    		}
    		catch (InterruptedException ex) { }
    	}
    	if (setState == 1)
    	{
    		setState = 0;
    		MainScreen ms = new MainScreen();
    		TutorialScreen2a.restoreScreen();
    		ms.run();
    	}
    	else if(setState == 2)
    	{
    		TutorialScreen3 ts3 = new TutorialScreen3();
        	TutorialScreen2a.restoreScreen();
        	ts3.run();
    	}
    	 /* else if(setState== 3){
    		TutorialScreen ts = new TutorialScreen();
    		TutorialScreen2a.restoreScreen();
    		ts.run(); 										//This will make the slide go to its previous slide, does not currently work, makes screen flash.
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
  	
  			   if (keyCode == KeyEvent.VK_ESCAPE) // when the escape button is pressed.
  			   {
  				   DataClass.setP1Tank(p1Color);
  				   DataClass.setP2Tank(p2Color);
  				   DataClass.setwind(wind);
  				   DataClass.setGameState(GameState);
  				 
  				   setState = 1; // when the setstate = 1, a new the game will go back to/create a new main screen.
  				   
  			   }
  			   else if(keyCode == KeyEvent.VK_RIGHT)
  				   {
  				   		setState =2;
  				   }
  			  else if(keyCode == KeyEvent.VK_LEFT){
  				   setState = 3;
  			   } 
  			   
}}}
