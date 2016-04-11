/**
 * Main Screen.java is the Main screen for the Tank wars game
 * User's will be able to select different options, view high scores, or play the game
 */
import java.awt.*;

import javax.swing.ImageIcon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import ScreenManagement.*;


public class MainScreen {

    public MainScreen() {
    }
    
    public static ScreenManager StartScreen;
    public static int setStart=0;
    public static Image bgImage;
    public static Image title;
    
    //Variables for testing purposes:
    protected static Boolean testMode = false;
    protected static Boolean testVar = false;
    protected static int testLoop = 1;
    public static MusicPlayer music;
    public static SoundPlayer music1;
    protected static ScreenManager testScreen1, testScreen2;
    
    
    //End of variables for testing purposes.    
    
    private static final DisplayMode POSSIBLE_MODES[] = {
        new DisplayMode(800, 600, 32, 0),
//        new DisplayMode(800, 600, 24, 0),
//        new DisplayMode(800, 600, 16, 0),
//        new DisplayMode(640, 480, 32, 0),
//        new DisplayMode(640, 480, 24, 0),
//        new DisplayMode(640, 480, 16, 0),
        new DisplayMode(1024, 768, 32, 0)
    };

    private static Image loadImage(String fileName) {
        return new ImageIcon(fileName).getImage();
    }
     
    public static void run() {
      StartScreen = new ScreenManager();;
        try {
          //Initializes music for main screen
          //music = new MusicPlayer("Flux-Pavillion-Bass-Cannon.wav");
          music1 = new SoundPlayer();
          //music1.load("doom_1.mid");
          music1.play();
      //music.play();
          
            DisplayMode displayMode =
            StartScreen.findFirstCompatibleMode(POSSIBLE_MODES);
            StartScreen.setFullScreen(displayMode);
            Window windowStart = StartScreen.getFullScreenWindow();
            windowStart.addKeyListener(new KeyClass());
            loadImages();
            animationLoop();
            
            // create another animationloop in StartScreen.Java which will display Start selection
            //After user makes selections and begins game, return Start=1 to intiate Game phase.
            
            //Added for testing purposes.
            if (testMode == true)
            {
              testVar = true;
              System.out.println("Path is clear.");
          }
            //End added for testing purposes.
        }
     finally {
            StartScreen.restoreScreen();
        }
    }
    
    public static void loadImages() {
        //Added for testing purposes.     
      if (testMode == true && testVar == false)
          return;
      //End added for testing purposes.
      
      // loads images into buffer
         bgImage = loadImage("images/MainScreen.jpg");
          //title = loadImage("images/title.png");
    //add animation here for start screen
    }
    
    public static void animationLoop() {
//      Added for testing purposes.     
      if (testMode == true && testVar == false)
          return;
      //End added for testing purposes.
      
          while (setStart == 0) { //run loop till user makes a selection
          
               // draw and update the screen
            Graphics2D g = StartScreen.getGraphics();
            
            //Added for testing purposes.
            if (testMode == true)
            {
              if (testLoop == 1)
                testScreen1 = StartScreen;
              else
                testScreen2 = StartScreen;
              testLoop--;
            }
            //End added for testing purposes.
            
              draw(g);
          
            g.dispose();
            StartScreen.update();
            
            // take a nap to catch up garage stuff
            try {
                Thread.sleep(20);
            }
            catch (InterruptedException ex) { }
            
//          Added lines for testing purposes.
            if (testMode == true && testLoop == 0)
              break;
            //End of added lines.
            
        }
          ///////////////////////////////////
        ////Added by Ryan Crean  3/21/8////
          ////Redirects to the HelpScreen////
        ///////////////////////////////////////
        if(setStart == 2)
        {
            setStart = 0;
          HelpScreen hs = new HelpScreen();
          StartScreen.restoreScreen();
          hs.run();
        }
      ///////////////////////////////////////
        ///////////////////////////////////
        

        ///////////////////////////////////
        ////Added by Ryan Crean  3/21/8////
        ////Redirects to the HelpScreen////
      ///////////////////////////////////////
        else if(setStart == 3)
        {
          setStart = 0;
          OptionsScreen os = new OptionsScreen();
          StartScreen.restoreScreen();
        os.run();
        }
      ///////////////////////////////////////
        ///////////////////////////////////
        else if (setStart == 4)
        {
          setStart = 0;
          music1.stop();
          TutorialScreen ts = new TutorialScreen();
          StartScreen.restoreScreen();
          ts.run();
          
        }
        
        else if (setStart == 6)
        {
          setStart = 0;
          music1.stop();
          StoryScreen st = new StoryScreen();
          StartScreen.restoreScreen();
          st.run();
        }
    }
    
    public static synchronized void draw(Graphics g) {
      //Added for testing purposes.
      if (testMode == true)
        testVar = false;
      //End added for testing purpsoses.
      
      g.drawImage(bgImage, 0, 0, null);
        g.drawImage(title, 170,120,null);
      Font f = g.getFont();
      g.setFont(new Font("SansSerif", Font.BOLD, 15));
   /************    
      g.setColor(Color.black);
          g.drawString("Press Space Bar To Start",325,400);
          g.setFont(f);
          g.setFont(new Font("SansSerif", Font.BOLD, 15));
          g.drawString("Press 'T' To Open Tutorial",325,435);
          g.setFont(f);
          //g.drawString("Press O for Options",260,448);
          g.setColor(Color.black);
      g.setFont(new Font("SansSerif", Font.BOLD, 15));
        g.drawString("Press 'S' To View Story",325,470);
        g.setFont(f);
      
        g.setColor(Color.black);
      g.setFont(new Font("SansSerif", Font.BOLD, 15));
          g.drawString("Press Esc To Exit",325,505);
          g.setFont(f);
          
          
          //Added for testing purposes.
          if (testMode == true)
          {   
            testVar = true;
          }
  ****************/         //edited by Jim Webb Oct 23, 2014         
     }
     
    public static class KeyClass extends KeyAdapter
    {
       public void keyPressed(KeyEvent e) 
       {
        int keyCode = e.getKeyCode();

    //try to create a new object with user interaction for next goal.
        if (keyCode == KeyEvent.VK_SPACE) 
        { 
          music1.stop();
          music = new MusicPlayer("Super Smash Bros Brawl Theme Song.wav");
          music.play();
              //music1.stop();
              //music1.close();
            
            //music = new SoundPlayer();
            //music.load("doom_1.mid");
            //music.play();
            setStart = 1;
          
        }
        else if (keyCode == KeyEvent.VK_ESCAPE) 
          {
          System.exit(-1);
          music1.stop();
          
      }       
          ///////////////////////////////////
          ////Added by Ryan Crean  3/21/8////
      //setState = 2 to Redirect to HelpScreen///
        ///////////////////////////////////////
       /*else if (keyCode == KeyEvent.VK_H)
       {
          System.out.println("H key pressed");
          music1.stop();
            music1.close();
          setStart = 2;
       }*/
        
       else if (keyCode == KeyEvent.VK_T)
       {
         System.out.println("T key pressed");
         music1.stop();
         music1.play();
         setStart = 4;
         
       }
        
       else if (keyCode == KeyEvent.VK_S) 
       {
         System.out.println("S key Pressed");
         music1.stop();
         music1.play();
         setStart = 6;
       }
       
      }
    }
    
    public void setTestMode(Boolean test)
    {
      testMode = test; 
    }

}
