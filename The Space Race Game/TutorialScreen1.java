/*
import java.awt.DisplayMode;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import ScreenManagement.ScreenManager;
import java.awt.*;

public class TutorialScreen1 {
	

    public TutorialScreen1(){
    }
	 public static ScreenManager TutorialScreen1;
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
     	TutorialScreen1 = new ScreenManager();;
         try {
             DisplayMode displayMode =
             TutorialScreen1.findFirstCompatibleMode(POSSIBLE_MODES);
             TutorialScreen1.setFullScreen(displayMode);
             TutorialScreen1.getFullScreenWindow();
             Window windowStart = TutorialScreen1.getFullScreenWindow();
             windowStart.addKeyListener(new KeyClass());
             loadImages();
             animationLoop();
         }
         finally {
        	 TutorialScreen1.restoreScreen();
         }
     }
     
	public static void loadImages() {
        //loads images into buffer
         bgImage = loadImage("images/tank2_8.jpg");
         title = loadImage("images/title.png");
        }
	
	
    public static void animationLoop() 
    {
    	while(setState == 0)
    	{
    		
    		Graphics2D g = TutorialScreen1.getGraphics();
    		draw(g);
    		g.dispose();
    		TutorialScreen1.update();
    		
    		try {
    			Thread.sleep(20);
    		}
    		catch (InterruptedException ex) { }
    	}
    	if (setState == 1)
    	{
    		setState = 0;
    		//MainScreen ms = new MainScreen();
    		TutorialScreen1.restoreScreen();
    		//ms.run();
    	}
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
  			   else if(keyCode == KeyEvent.VK_SPACE)
  				   {
  				 TutorialScreen1 ts = new TutorialScreen1();
  	        	// TutorialScreen1.restoreScreen();
  	        	 ts.run();
  				   }
  			   
}}}

*/