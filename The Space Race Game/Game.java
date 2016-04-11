import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import ScreenManagement.*;
import java.io.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import  sun.audio.*;

//Game class_
public class Game implements KeyListener {


	public static void main(String args[]) {
		Game tankGame = new Game();
		tankGame.run();
		DataClass.readFile();
	}


	protected static final DisplayMode POSSIBLE_MODES[] = {
        	//New DisplayMode(1280, 800, 32, 0),  
        	//Support for higher Resolutions, maybe? 
        	//Would need lots of additional resizing.
        	
		new DisplayMode(800, 600, 32, 0),
        	new DisplayMode(800, 600, 24, 0),
        	new DisplayMode(800, 600, 16, 0),
    	};
    
    	// Testing 
	protected boolean testingUpdate = true;
	protected boolean testingDraw = false;

	//Variable Base
	public static DataClass dc = new DataClass();
	protected ScreenManager screen; // creates object to control screen
	protected SoundPlayer soundShot;
	protected SoundPlayer tankHurt; // adds sound indicator when tank is hit
	protected SoundPlayer clap;
	protected Image wImage; // image for win screen
	protected Image bgImage; //  image
	protected Image TankImage; // tank's image
	protected Image terrainTexture;
	protected Image headerBar; //top header image
	protected Image healthIcon; // health icon
	protected Image healthBar; // health bar full
	protected Image powerBar; // power bar full
	protected Image powerIcon; //power bar up arrow icon
	protected Image weaponIndicatorBG; //  for weapon indicator
	protected Image weaponNormal;
	protected Image RedTurret;
	protected Image weaponHersco;
	protected Image weaponNuclear;
    
	//Used to count shots before un-paralyzed (Start of un-paralyzed)
	int PARALYZED = 10; //edit this to change amount of shots until un-paralyzed
	int shotcounter = PARALYZED;
	int shotcounter2 = PARALYZED;
	
    // Tanks
    static Tank Tank1 = new Tank();
    static Tank Tank2 = new Tank();
    
	/*
	Projectiles -This needs to be added in we did not have time
	Projectile shot1= new Projectile();//player 1 shot
	Projectile shot2= new Projectile();//player 2 shot
	*/    

    //**Objects in Game
    protected Sprite Shot; // displays weapon 1 for player 1 in the top left corner
    protected Sprite SecondShot; //displays weapon 2 for player 1 in the top left corner
    protected Sprite Shot2; // displays weapon 1 for player 2 in the top right corner 
    protected Sprite SecondShot2; // displays weapon 2 for player 2 in the top right corner
    protected Sprite ThirdShot;
    protected Sprite ThirdShot2;  
    protected Sprite BOOM; // explosion
    protected Sprite TankBoom; //tank explosion
    protected Sprite TankShot; //tanks shot
    protected Sprite TankShot2;
    protected Sprite SecondaryTankShot; //second tank shot
    protected Sprite SecondaryTankShot2;
    protected Sprite ThirdTankShot;//HEEEERSCOOOO BOMB
    protected Sprite ThirdTankShot2;


	//Necessary Global Variables////
	public int restartgame = 0; //added by Ryan Kleckner 4/8/08
	public boolean firstfall = true;//sees if the tank is falling for the first time
	public boolean firstfall2 = true;//sees if the tank is falling for the first time
	public boolean PauseMenuOpen = false;  // added 11/05/08 by Devin Barna
	public boolean ControlsOverlayOpen = false;

	/* GameState
	 * Used to determine what type of gameplay currently enabled.
    	 * 0=dynamic
	 * 1=turn based
    	 * 3=restart game
	 */
	public int GameSTATE=1;
	public boolean GameSTATEchanged = true;
    
	// (needed in order to change GameSTATE during the game)
    public static int turn=1;
    //TEST
    //if turn =1 player 1 turn
    //if turn =2 player 2 turn


	/* hitTest */
	//=0 -> shot is reset
	//=2 -> shot hit ground or player sprite.
	public int hitTest = 0;
	public int hitTest2 = 0;

	/* tankshoot */
	//=1 -> shot was just fired
	//<1 -> nothing besides that a shot was not just fired. need to fix this.
	public int tankshoot=0;
	public int tankshoot2=0;


	/* Gravity & Wind */
	//Gravity
	public float Gravf;
	
	//Wind
	public int windcount=0;//determines if the wind needs to be changed
	public float Windf = .00f;
	public int WindVar=0;


	public static int start=0;
	public boolean TankCreated = false; //Gate to detect if a tank was randomly placed onto the map or not
	
	/* Holder and Counters */
	int a=0;

    
	//////////////////////
	///Dynamic Ground variables////
   	public int basex=0;
    public int basey=900;
    public int topx=0;
	public Integer topy[] = new Integer[9500]; 
	public int AirStrikeAmmo1 = 1;
	public int AirStrikeAmmo2 = 1;
    	
    public int sleepTime = 25;
    	
    // moving and jumping
    public int maxMove = 100; 
    public int tankMove = maxMove;
    public boolean moving = false;
    public boolean jumpUp = false; 
    public boolean jumpFall = false;
    public int maxJump = 25; 
    public int currJump = 0;
    public int fuel = 110;
    public int curFuel = fuel;  
    public int fuelDepletion = 2;
    
    // test suite
    public int ptLevel = 1, ptX = 0, ptY = 0;
    protected boolean testing = false;
    // used when a method doesn't set any checkable values
    protected boolean check = true;
    protected boolean caseChecks[] = new boolean[7];
    
     //added by Ryan Kleckner 4/14/08

  	int p1secondShots = 2;
  	int p2secondShots = 2;


	public String randomTerrain() {
		/*
		* Author: Ryan K, 2008
		* Refactored by: Chris I, 2013
		*/

		String[] nums = {"1", "2", "3", "4", "5"};
		int randomNumber = (int)(Math.random() * nums.length);
		return nums[randomNumber];
	}

	String levelNumber = randomTerrain();
	
	int freqValue;
	int terrainNum;
	int firstNum = 900;;
	int secondNum = 950;;


	public void ChangeLevel(String num) {
		firstNum = 200;
		secondNum = 950;	
		freqValue = 0;
		terrainNum = 0;
		basex=0;
		basey=900;
		topx=0;
		topy = new Integer[9500]; 
		topy[basex]= null;
		levelNumber = num; //set levelNumber to the same number you want to play/test
  	}

	public void loadImages() {
        	// loads images into buffer
    		bgImage = loadImage("images/GameBackground.png"); // Background Image
    		headerBar = loadImage("images/headerBarBlue.png"); // Header Image
    		healthIcon = loadImage("images/healthIcon.png"); // Health Icon
    		healthBar = loadImage("images/healthBar.png"); // Health Bar full
    		powerBar = loadImage("images/powerBar.png"); // Power Bar full
    		powerIcon = loadImage("images/uparrowicon.png"); //power icon
    		wImage = loadImage("images/john_win.jpg");
    	
      		// Weapon Indicator Images
    		weaponIndicatorBG = loadImage("images/weaponIndicatorBG.png"); // BG
    		weaponNormal = loadImage("images/weaponArrow.png");

    		weaponHersco = loadImage("images/BarragesmallUSSR.png");
    		weaponNuclear = loadImage("images/FlamingArrow.png");

		TankImage = loadImage(DataClass.getP1Tank());
		Image TankImage2 = loadImage(DataClass.getP2Tank());
		Image Explode = loadImage("images/explosion.gif");

		/* Weapons */
		
		//Player 1 - Regular
		Image Bullet = loadImage("images/Arrow.gif");
		Image Bullet2 = loadImage("images/Arrow.gif");
		Image Bullet3 = loadImage("images/Arrow.gif");
		
		//Player 2 -Regular
		Image P2Bullet = loadImage("images/Arrow.gif");
		Image P2Bullet2 = loadImage("images/Arrow.gif");
		Image P2Bullet3 = loadImage("images/Arrow.gif");
				
		//Player 1 - Special
		Image bomb = loadImage("images/FlamingArrow.png");
		Image bomb2 = loadImage("images/FlamingArrow.png");
		Image bomb3 = loadImage("images/FlamingArrow.png");
		Image bomb4 = loadImage("images/FlamingArrow.png");
		Image bomb5 = loadImage("images/FlamingArrow.png");
		
		//Player 2 - Special
		Image P2bomb = loadImage("images/FlamingArrow.png");
		Image P2bomb2 = loadImage("images/FlamingArrow.png");
		Image P2bomb3 = loadImage("images/FlamingArrow.png");
		Image P2bomb4 = loadImage("images/FlamingArrow.png");
		Image P2bomb5 = loadImage("images/FlamingArrow.png");
		
		//Barrage
		Image hersco = loadImage("images/weaponUSSR.png");
		Image smallhersco = loadImage("images/BarragesmallUSA.png");   //No idea what this does...
		
		//Weapon Collision
		Image lilBoom = loadImage("images/lilboom.gif");
		//Initiate Sounds
		soundShot = new SoundPlayer();
		tankHurt = new SoundPlayer();
        	// create sprites
		

		Animation TankStill = new Animation();
		TankStill.addFrame(TankImage,300);

		Animation TankStill2 = new Animation();
		TankStill2.addFrame(TankImage2,300);
		Animation TankHit = new Animation();
		TankHit.addFrame(lilBoom,450);
        
		Animation blowUp = new Animation();
		blowUp.addFrame(Explode,300);
        
		Animation bul = new Animation();
		bul.addFrame(Bullet,300);
		bul.addFrame(Bullet2,200);
        
		Animation bul2 = new Animation();
		bul2.addFrame(bomb, 300);
        
		Animation shoot = new Animation();
		shoot.addFrame(Bullet,200);
		shoot.addFrame(Bullet2, 200);
		shoot.addFrame(Bullet3,200);  
			
		Animation P2shoot = new Animation();
		P2shoot.addFrame(P2Bullet,200);
		P2shoot.addFrame(P2Bullet2, 200);
		P2shoot.addFrame(P2Bullet3,200);  
        
		Animation shoot2 = new Animation();
        	shoot2.addFrame(bomb,200);
        	shoot2.addFrame(bomb2,200);
        	shoot2.addFrame(bomb3,200);
        	shoot2.addFrame(bomb4,200);
        	shoot2.addFrame(bomb5,200);
        
		Animation P2shoot2 = new Animation();
        	P2shoot2.addFrame(P2bomb,200);
        	P2shoot2.addFrame(P2bomb2,200);
        	P2shoot2.addFrame(P2bomb3,200);
        	P2shoot2.addFrame(P2bomb4,200);
        	P2shoot2.addFrame(P2bomb5,200);
 
		Animation shoot3 = new Animation();
		shoot3.addFrame(hersco, 200);       

        	Animation bul3 = new Animation();
        	bul3.addFrame(smallhersco, 200);
        
		// Take the Image Frames and Create and Object(Sprite). Sprites have Attributes for locating its position, movement, and State. See Sprite.Java
		Tank1.setTankSprite(new Sprite(TankStill));
		Tank2.setTankSprite(new Sprite(TankStill2));
		Shot = new Sprite(bul);
		SecondShot = new Sprite(bul2);
		Shot2 = new Sprite(bul);
		SecondShot2 = new Sprite(bul2);
		ThirdShot = new Sprite(bul3);
		ThirdShot2 = new Sprite(bul3);
		TankShot = new Sprite(shoot);
		TankShot2 = new Sprite(P2shoot);
		SecondaryTankShot = new Sprite(shoot2);
		SecondaryTankShot2 = new Sprite(P2shoot2);
		ThirdTankShot = new Sprite(shoot3);
		ThirdTankShot2 = new Sprite(shoot3);
		TankBoom = new Sprite(TankHit);
		BOOM = new Sprite(blowUp);
	
	} //end loadImages()


	private Image loadImage(String fileName) {
		return new ImageIcon(fileName).getImage();
	}

	public void TestTurret(Sprite tank) {
		tank.setY(100f);
	}

	public void run() {
		String x = DataClass.getStart();
		if (x.compareTo("0")==0) {	
			MainScreen.run();	 //open up main screen
    		}
    	
		//else continue with game data...
        	screen = new ScreenManager();

		//try block and finally block added by Ryan Kleckner
		//to allow the game to be re-run upon the user
		//pressing the's' key
 		//4/8/08
 		try {
                	DisplayMode displayMode =
                	screen.findFirstCompatibleMode(POSSIBLE_MODES);
                	screen.setFullScreen(displayMode);
                	Window window = screen.getFullScreenWindow();
                	window.addKeyListener(this);
                	window.addMouseListener(new MouseClass());
                	loadImages();
                	CountdownTimer.startCounter();  //starts the timer
                	while (restartgame < 1) {
				animationLoop();
			}
   		} finally {
			System.out.println("Escaping");
			System.out.println("RESTART Variable: "+ restartgame);
   			//is only called when restartgame is set to 1 to exit the animationloop
   			//and restart the game
   		        screen.restoreScreen();//restores active screen
   			Game.main(null);//calls Game.java's main class to start a new game
   		}
   			
 			// for testing: since there is no other value to check here
 			if (testing)
 				check = true;
	} //end run()

	public void animationLoop() {
        	long startTime = System.currentTimeMillis();
        	long currTime = startTime;

        	while (restartgame < 1) { //run loop infinitely till user exits.
          		//***************************************************************
          		//added restartgame variable Ryan K 4/8/08
          		//***************************************************************
			
			long elapsedTime = System.currentTimeMillis() - currTime;
			currTime += elapsedTime;

			// update the sprites
			update(elapsedTime);
			
			// draw and update the screen            
			Graphics2D g = screen.getGraphics();            
			draw(g);
		
			// drawFade(g, currTime-startTime);
			g.dispose();
			screen.update();
			
			// break to do tests
			if (testing) {
				check = true;
				break;
			}
			
            		// take a nap for garbagee
            		try {
				Thread.sleep(sleepTime);
			}
			catch (InterruptedException ex) { }
		
		} //end while(restartgame < 1)

	} //end animationLoop()


	//---------------------------------------------------------------------------v


	public void calculateDamage(Sprite bullet) {
		Sprite player1 = Tank1.getTankSprite();
		Sprite player2 = Tank2.getTankSprite();
		return;
	}


	public float calculateWind(int windVar, int tankshoot) {
		final float windBase = .00f+.004f;
		float wind = .00f;
		if (windVar == 1) {
			if (tankshoot==1 || tankshoot == 2) {
				wind = windBase*(float)Math.random();
				if (Math.random()>.5) {
					wind = wind*-1f;
				}
			}
		} else if (windVar == 0) {
		} else if (windVar==2 && wind==.00f) {
			wind = windBase*(float)Math.random();
			if (Math.random() > .5) {
				wind = wind*-.1f;
			}
		} else if (windVar == 3) {
			if (windcount < 20+(int)20*Math.random()) {
				windcount++;
			}
		}

		return wind;
	} //end calculateWind()

	//---------------------------------------------------------------------------^

	public void update(long elapsedTime)  {
    		// checks if a jump needs to be ended
		if (jumpUp) {
			if (currJump > maxJump) {
				currJump = 0;
				jumpUp = false;
				jumpFall = true;
			} else {
				currJump+=1;
			}
		} // needed for point test


		//Check if any shots have been fired and have traveled off the screen. If so, they are no longer needed and are reset.
		if (TankShot.isActive() && TankShot.outOfBounds()) {
			TankShot.resetShot();
			//curFuel=110; //resets fuel
			hitTest = 0;
		} else if (SecondaryTankShot.isActive() && SecondaryTankShot.outOfBounds()) {
			SecondaryTankShot.resetShot();
			//curFuel=110; //resets fuel
			hitTest = 0;
		}
		
		if (TankShot2.isActive() && TankShot2.outOfBounds()) {
			TankShot2.resetShot();
			//curFuel=110; //resets fuel
			hitTest2 = 0;
		} else if (SecondaryTankShot2.isActive() && SecondaryTankShot2.outOfBounds()) {
			SecondaryTankShot2.resetShot();
			//curFuel=110; //resets fuel
			hitTest2 = 0;
		}
       
		//To create gravity, there needs to be a decay on the velocity at a constant rate.
       		//The decay must be at the correct interval to create a natural trajectory for the shot.
   		if (GameSTATE != 3 && GameSTATEchanged == false) {
			GameSTATE = DataClass.getGameState();
   		}
		
		String x = DataClass.getGravity();
		Gravf = Float.valueOf(x.trim()).floatValue();
		WindVar = DataClass.getWind();

       		if (WindVar==1) {
       			if (tankshoot==1||tankshoot2==1) {
       				Windf=.001f+.004f*(float)Math.random();
       				if (Math.random()>.5) {
					Windf=Windf*-1f;
   				}
			}
		}
       		else if (WindVar==0) {
   			Windf=.00f;
		}
   		else if (Windf==.00f&&WindVar==2) {
   				    Windf=.001f+.004f*(float)Math.random();
				    if(Math.random()>.5)Windf=Windf*-1f;
   		}
   		else if (WindVar==3) {
   				if(windcount<20+(int)20*Math.random())
   					windcount++;
   				else
   				{
   					windcount=0;
   					Windf=.001f+.004f*(float)Math.random();
       				if(Math.random()>.5)Windf=Windf*-1f;
   				}
   		}

	if(TankShot.isActive())
       		{
       		    TankShot.setVelocityX(TankShot.getVelocityX() - Windf); //WIND
       		    TankShot.setVelocityY(TankShot.getVelocityY() + (Gravf));	//Gravity
       		}
       	else if(SecondaryTankShot.isActive())
       		{
       		    SecondaryTankShot.setVelocityX(SecondaryTankShot.getVelocityX() - 0); //WIND
       		    SecondaryTankShot.setVelocityY(SecondaryTankShot.getVelocityY() + (0));	//Gravity
       		}
       		
       	if(TankShot2.isActive())
   			{
       		    TankShot2.setVelocityX(TankShot2.getVelocityX() - Windf); //WIND
       		    TankShot2.setVelocityY(TankShot2.getVelocityY() + (Gravf));	//Gravity
   			}
       	else if(SecondaryTankShot2.isActive())
   			{
       		    SecondaryTankShot2.setVelocityX(SecondaryTankShot2.getVelocityX() - 0); //WIND
       		    SecondaryTankShot2.setVelocityY(SecondaryTankShot2.getVelocityY() + (0));	//Gravity
   			}


	//---------------------------------------------------------------------------v


	if (TankShot.isActive()) {
		if (ShotCollision(Tank1.getTankSprite(), TankShot,1)) {
			Tank1.decreaseHealth(10);
			hitTest=2;
			tankHurt.load("Sounds/tankhit.mid");
    		    	tankHurt.play();
		} if (ShotCollision(Tank2.getTankSprite(), TankShot,1)) {
			//Tank2.setHealth(Tank2.getHealth()-10);//subtract 10
			Tank2.decreaseHealth(10);
			hitTest=2;
			tankHurt.load("Sounds/tankhit.mid");
    		    	tankHurt.play();

		}
	} else if (SecondaryTankShot.isActive()) {
		if (ShotCollision(Tank1.getTankSprite(), SecondaryTankShot,1)) {
			Tank1.setHealth(Tank1.getHealth()-20); // subtract 20
			hitTest=2;
			tankHurt.load("Sounds/tankhit.mid");
    		    	tankHurt.play();

		} if (ShotCollision(Tank2.getTankSprite(), SecondaryTankShot,1)) {
			Tank2.setHealth(Tank2.getHealth()-20); // subtract 20
			hitTest=2;
			tankHurt.load("Sounds/tankhit.mid");
    		    	tankHurt.play();
	   	}
       } else if (ThirdTankShot.isActive()) {
		if (ShotCollision(Tank1.getTankSprite(), ThirdTankShot,1)) {
			Tank1.setHealth(Tank1.getHealth()-30); // subtract 30
			hitTest=2;
			tankHurt.load("Sounds/tankhit.mid");
    		    	tankHurt.play();
		}
      	   if (ShotCollision(Tank2.getTankSprite(), ThirdTankShot,1)) {
      		   Tank2.setHealth(Tank2.getHealth()-30); // subtract 30
      		   hitTest=2;
			tankHurt.load("Sounds/tankhit.mid");
    		    	tankHurt.play();
  	   		}
       }       
	//------------------------------------------------------------------------------


       if(TankShot2.isActive())
       	
        {
        curFuel=fuel;      		
            if (ShotCollision(Tank1.getTankSprite(), TankShot2,2)) 
            {
                Tank1.setHealth(Tank1.getHealth()-10); // subtract 10 points off player 1's health if hit
        		hitTest2=2;
        		tankHurt.load("Sounds/tankhit.mid");
    		    	tankHurt.play();
            }
        	if (ShotCollision(Tank2.getTankSprite(), TankShot2,2)) {
        		 Tank2.setHealth(Tank2.getHealth()-10); //subtract 10
        		 hitTest2=2;
        		 tankHurt.load("Sounds/tankhit.mid");
    		    	tankHurt.play();
        	}
        }
        else if (SecondaryTankShot2.getState()==1){
        	 if (ShotCollision(Tank1.getTankSprite(), SecondaryTankShot2,2)) {
        		 Tank1.setHealth(Tank1.getHealth()-20); // subtract 20
        		 hitTest2=2;
        		 tankHurt.load("Sounds/tankhit.mid");
    		    	tankHurt.play();
        	 }
        	 if (ShotCollision(Tank2.getTankSprite(), SecondaryTankShot2,2)) {
        		 Tank2.setHealth(Tank2.getHealth()-20); // subtract 20
        		 hitTest2=2;
        		 tankHurt.load("Sounds/tankhit.mid");
    		    	tankHurt.play();
    	     }  	   
        }
        else if(ThirdTankShot2.getState()==1)
        {
    		if (ShotCollision(Tank1.getTankSprite(), ThirdTankShot2,2)) {
                Tank1.setHealth(Tank1.getHealth()-30); // subtract 30
        		hitTest2=2;
        		tankHurt.load("Sounds/tankhit.mid");
    		    	tankHurt.play();
        	}
        	if (ShotCollision(Tank2.getTankSprite(), ThirdTankShot2,2)) {
                Tank2.setHealth(Tank2.getHealth()-30); // subtract 30
        		hitTest2=2;
        		tankHurt.load("Sounds/tankhit.mid");
    		    	tankHurt.play();
    	   		}
         }
       
		// update objects on the screen bring the current time with them
		//(Can be used for timing events.
		Tank1.getTankSprite().update(elapsedTime);
		Tank2.getTankSprite().update(elapsedTime);
		Shot.update(elapsedTime);
		SecondShot.update(elapsedTime);
		Shot2.update(elapsedTime);
		SecondShot2.update(elapsedTime);
		TankShot.update(elapsedTime);
		SecondaryTankShot.update(elapsedTime);
		TankShot2.update(elapsedTime);
		SecondaryTankShot2.update(elapsedTime);
		ThirdTankShot.update(elapsedTime);
		ThirdTankShot2.update(elapsedTime);
	
	} //end Update()

	public boolean ShotCollision(Sprite tank, Sprite shot, int player) {
		/*
     		 * ShotCollision()
    		 * Author: Eric Marcarelli
    		 * Checks if a tank has been hit. Based on original collision 
    		 * logic between tanks and missile.	
    		 */
		boolean ret = false; 
		int PlayerLowX = ((Math.round(tank.getX())));
       			int PlayerLowY = ((Math.round(tank.getY())));
        		int PlayerHighX = ((Math.round(tank.getX())) + (tank.getWidth()));
        		int PlayerHighY = ((Math.round(tank.getY())) + (tank.getHeight()));
    			int ShotLowX = ((Math.round(shot.getX())));
       			int ShotLowY = ((Math.round(shot.getY())));
        		int ShotHighX = ((Math.round(shot.getX())) + (shot.getWidth()));
 		
 			//added by Ryan Kleckner to make the second shot path act like a homing missile 4/29/08
 			if (SecondaryTankShot.isActive() && Math.round(SecondaryTankShot.getX()) >= Math.round(Tank2.getTankSprite().getX())) {
        			SecondaryTankShot.setVelocityX(0f);
        			SecondaryTankShot.setVelocityY(0.5f);
        		}
        
			if (SecondaryTankShot2.isActive() && Math.round(SecondaryTankShot2.getX()) <= Math.round(Tank1.getTankSprite().getX())) {
        			SecondaryTankShot2.setVelocityX(0f);
        			SecondaryTankShot2.setVelocityY(0.5f);
        		}
        
 			if (ShotLowX > PlayerLowX||ShotHighX>PlayerLowX) {        
        			if(ShotLowY> PlayerLowY) {	       
         				if (ShotHighX < PlayerHighX||ShotLowX<PlayerHighX) {         				
         					if (ShotLowY < PlayerHighY) {
         						
        						ret = true;		
         					}
        				}
        			}
        		}
		return ret; 
    		}
    
    /*
     * rotate() 
     * Written by Eric Marcarelli
     * Returns a rotated BufferedImage from original Image and angle.
     */
    
	public BufferedImage rotateImage(Image image, double angle) {
  		// Create the BufferedImage
  		BufferedImage img = new BufferedImage(100,100,BufferedImage.TYPE_INT_ARGB);
  		Graphics bg = img.getGraphics();
  		bg.drawImage(image, 0, 0, null);
  		bg.dispose();
 		
 		// Rotate the image
 		BufferedImage rotated = new BufferedImage(100,100,BufferedImage.TYPE_INT_ARGB);
 		Graphics2D g2d = rotated.createGraphics();
 		AffineTransform rot = new AffineTransform();
 		rot.rotate(angle, 19, 14);
 		g2d.transform(rot);
 		g2d.drawImage(img, 0, 0, null);
 		g2d.dispose();
 		
 		return rotated; 
	}

    public synchronized void draw(Graphics g) {
    
    	g.drawImage(bgImage, 0, 0, null);  // draws background
    	                
	DrawTerrain(g); // draws a flat surface for the tanks to land on. Use this function to implement a dynamic terrain generation

        if (TankCreated != true) // if no tank has been created, then create one.
		{
    		CreateTank(g);
            Tank1.setTurretAngleX(Math.round(Tank1.getTankSprite().getX() + (int)(Tank1.getTankSprite().getWidth()*.5)));
            Tank1.setTurretAngleY(Math.round(Tank1.getTankSprite().getY() - (int)(Tank1.getTankSprite().getHeight())));
    		Tank2.setTurretAngleX(Math.round(Tank2.getTankSprite().getX() + (int)(Tank2.getTankSprite().getWidth()*.5)));
    		Tank2.setTurretAngleY(Math.round(Tank2.getTankSprite().getY() - (int)(Tank2.getTankSprite().getHeight())));
    		TankCreated = true;
		}
        
        // Update the Selected Weapon Indicator and Ammo
    	updateWeaponIndicator(g);
        
        // using new GroundCollision parameters -- Eric M.	
    	GroundCollision(Tank1.getTankSprite(), 1);
    	GroundCollision(Tank2.getTankSprite(), 2); // check if there is ground underneath a tank, if not let it fall. This function is crudely done.
    	// creating a center of gravity for the objects and doing a better check on when a tank should fall can be changed in this function.
        	 
        // Tanks are drawn with rotateImage() -- Eric M. 	 
        	 
		//redraw tank in its new position
        g.drawImage(rotateImage(Tank1.getTankSprite().getImage(), Tank1.getTankSlant()), Math.round(Tank1.getTankSprite().getX()), Math.round(Tank1.getTankSprite().getY()), null);
            
        //player 2 tank image
        g.drawImage(rotateImage(Tank2.getTankSprite().getImage(), Tank2.getTankSlant()), Math.round(Tank2.getTankSprite().getX()), Math.round(Tank2.getTankSprite().getY()), null);
        // draw shot if it exists
        //draws the shot that is being used in the top left hand corner of the screen
        
        	if(Tank1.getWeapon2() == 0)
        	{
        		g.drawImage(Shot.getImage(), 8, 10, null);
        	}
        	else if(Tank1.getWeapon2() == 1)
        	{
        		g.drawImage(SecondShot.getImage(), 0, 0, null);
        	}
        	else if(Tank1.getWeapon2() == 2)
        	{
        		g.drawImage(ThirdShot.getImage(), 0, 0, null);
        	}
        	if(Tank2.getWeapon2() == 0)
        	{
        		g.drawImage(Shot2.getImage(), 516, 10, null);
        	}
        	else if(Tank2.getWeapon2() == 1)
        	{
        		g.drawImage(SecondShot2.getImage(), 508, 0, null);
        	}
        	else if(Tank2.getWeapon2() == 2)//Can't display till image is smaller.
        	{
        		g.drawImage(ThirdShot2.getImage(), 508, 0, null);
        	}        	
  
        
        //  Draw the tank's turret. 19 = 1/2 centering the position of the turret on the tank. 3 = making the turret at the correct height of the tank.
		// THIS IS TO CHANGE WHERE THE TURRET IS CONNECTED TO THE TANK.....
        g.setColor(Color.white);
        g.drawLine(Math.round(Tank1.getTankSprite().getX())+(int)(Tank1.getTankSprite().getWidth()*.55),Math.round(Tank1.getTankSprite().getY()+5),Tank1.getTurretAngleX(),Tank1.getTurretAngleY()+5);
		g.drawLine(Math.round(Tank2.getTankSprite().getX())+(int)(Tank2.getTankSprite().getWidth()*.45),Math.round(Tank2.getTankSprite().getY()+5),Tank2.getTurretAngleX(),Tank2.getTurretAngleY()+5);
		int y = Math.round(Tank1.getTankSprite().getY());
		for(int i = Math.round(Tank1.getTankSprite().getX()); i == 30000; i=i+50){
		
			g.drawLine(i, y, i+50,y+50 );
			y+=50;
			
		}	
		DrawMessages(g); // draw messages such as tank health, power..ect
		
		//checks if Player 1's health is 0, this could be made into a function.
		checkHealth(Tank1.getHealth(), Tank1.getTankSprite(), g);
		checkHealth(Tank2.getHealth(), Tank2.getTankSprite(), g);
        
			//checks if bullet hit ground if it did creates hole and sets state=0
			if (TankShot.getState()== 1)// if a shot has been fired   
      			fireShot(TankShot, g, 1,1);
			else if (SecondaryTankShot.getState()== 1) // if a shot has been fired
        		fireShot(SecondaryTankShot, g, 2,1);
			else if(ThirdTankShot.getState()==1)
				fireShot(ThirdTankShot, g, 3,1);
			if (TankShot2.getState()== 1)// if a shot has been fired   
      			fireShot(TankShot2, g, 1,2);
			else if (SecondaryTankShot2.getState()== 1) // if a shot has been fired
        		fireShot(SecondaryTankShot2, g, 2,2);
			else if(ThirdTankShot2.getState()==1)
				fireShot(ThirdTankShot2, g, 3,2);
				


			//if player 1 fired checks to see what he fired and fires that shot.
     	 if (tankshoot == 1)
     	 	 //curFuel=110;
         	{
         	if(Tank1.getWeapon2() == 1 && Tank1.getSecondWeaponAmmo()> 0)//if w2 and ammo
				{
					TankFire2(g,1);
					Tank1.setSecondWeaponAmmo(Tank1.getSecondWeaponAmmo()-1);
				}
			else if(Tank1.getWeapon2() == 1 && Tank1.getSecondWeaponAmmo() == 0)//check to see if something is in air
				{
					Tank1.setWeapon2(0);
					TankShot.setState(1);
					SecondaryTankShot.resetShot();
					hitTest = 0;
					
				}
			if(Tank1.getWeapon2() == 2 && Tank1.getThirdWeaponAmmo()> 0)//if w3 and ammo
			{
				TankFire3(g,1);
				Tank1.setThirdWeaponAmmo(Tank1.getThirdWeaponAmmo()-1);
			}
         	else if(Tank1.getWeapon2() == 2 && Tank1.getThirdWeaponAmmo() == 0)//check to see if something is in air
			{
				Tank1.setWeapon2(0);
				TankShot.setState(1);
				ThirdTankShot.resetShot();
				//curFuel=110; //resets fuel
				hitTest = 0;	
			}         	
			 if (Tank1.getWeapon2()==0)//if no ammo for w2 or w3 or if w1 then fire
         		{
         			TankFire(g,1);
         		}
            tankshoot++;
         	}

     	
     	
     	if (tankshoot2 == 1)
     		//curFuel=fuel;
      	{
     		 if(Tank2.getWeapon2() == 1 && Tank2.getSecondWeaponAmmo()> 0)
				{
					TankFire2(g,2);
					Tank2.setSecondWeaponAmmo(Tank2.getSecondWeaponAmmo()-1);
				}
			else if(Tank2.getWeapon2() == 1 && Tank2.getSecondWeaponAmmo() == 0)//check to see if something is in air
				{
					Tank2.setWeapon2(0);
					TankShot2.setState(1);
					SecondaryTankShot2.resetShot();
					//curFuel=110; //resets fuel
					hitTest2 = 0;
				}
     		 
			if(Tank2.getWeapon2() == 2 && Tank2.getThirdWeaponAmmo()> 0)
				{
					TankFire3(g,2);
					Tank2.setThirdWeaponAmmo(Tank2.getThirdWeaponAmmo()-1);
				}
			else if(Tank2.getWeapon2() == 2 && Tank2.getThirdWeaponAmmo() == 0)//check to see if something is in air
				{
					Tank2.setWeapon2(0);
					TankShot2.setState(1);
					ThirdTankShot2.resetShot();
					//curFuel=110; //resets fuel
					hitTest2 = 0;


				}			
			if (Tank2.getWeapon2()==0)
      		{
      			TankFire(g,2);
      		}
			tankshoot2++;
      	}
     	
     	
     	
        
        //if the shot is currently in the air, update the image of the shot.
        if (TankShot.isActive()) {
        curFuel=fuel;
		g.setColor(Color.black);
		g.drawImage(TankShot.getImage(), Math.round(TankShot.getX()), Math.round(TankShot.getY()), null); 
   	} else if (SecondaryTankShot.isActive()) {
		g.setColor(Color.black);
		g.drawImage(SecondaryTankShot.getImage(), Math.round(SecondaryTankShot.getX()), Math.round(SecondaryTankShot.getY()),null);
       	} else if (ThirdTankShot.isActive()) {
		g.setColor(Color.black);
		g.drawImage(ThirdTankShot.getImage(), Math.round(ThirdTankShot.getX()-66), Math.round(ThirdTankShot.getY()-150),null);
	     	  	
    	}
        if (TankShot2.getState()==1 )
 		{
       	     	    g.setColor(Color.black);
       	     	    g.drawImage(TankShot2.getImage(), Math.round(TankShot2.getX()), Math.round(TankShot2.getY()), null); 
       	     	
 		}
       	else if(SecondaryTankShot2.getState()==1)
       	{
       				g.setColor(Color.black);
       	     	  	g.drawImage(SecondaryTankShot2.getImage(), Math.round(SecondaryTankShot2.getX()), Math.round(SecondaryTankShot2.getY()),null);
       	     	
       	}       
       	else if(ThirdTankShot2.getState()==1)
    	{
				g.setColor(Color.black);
	     	  	g.drawImage(ThirdTankShot2.getImage(), Math.round(ThirdTankShot2.getX()-66), Math.round(ThirdTankShot2.getY()-150),null);
	     	  	
    	} 
  

      //tried making this more efficient and tank 2 shot always explodes right out of the turret.  <- Whose coment is this?
       	if (hitTest == 2) // The shot has hit the ground or tank
 		{

       			if(TankShot.getState()==1) //what state does the '1' signify? 
       			{
	     	  		g.drawImage(TankBoom.getImage(), Math.round(TankShot.getX()), (Math.round(TankShot.getY())), null);
       	       		TankShot.resetShot();
       	       		//curFuel=110; //resets fuel
				hitTest = 0;
       			}
       			else if(SecondaryTankShot.getState()==1)
       			{
	     	  		g.drawImage(TankBoom.getImage(), Math.round(SecondaryTankShot.getX()), (Math.round(SecondaryTankShot.getY())), null);       		
       	       		SecondaryTankShot.resetShot();
       	       		//curFuel=110; //resets fuel
				hitTest = 0;
       			}
       			else if(ThirdTankShot.getState()==1)
       			{
	     	  		g.drawImage(TankBoom.getImage(), Math.round(ThirdTankShot.getX()), (Math.round(ThirdTankShot.getY())), null);
       	       		ThirdTankShot.resetShot();
       	       		//curFuel=110; //resets fuel
				hitTest = 0;
       			}       					       			
 		}
       	if(hitTest2==2)
       	{
       		
       			if(TankShot2.getState()==1)
       			{
    	     		g.drawImage(TankBoom.getImage(), Math.round(TankShot2.getX()), (Math.round(TankShot2.getY())), null);
       	       		TankShot2.resetShot();
       	       		//curFuel=110; //resets fuel
				hitTest2 = 0;

       			}
       			else if(SecondaryTankShot2.getState()==1)
       			{
    	     		g.drawImage(TankBoom.getImage(), Math.round(SecondaryTankShot2.getX()), (Math.round(SecondaryTankShot2.getY())), null);
       	       		SecondaryTankShot2.resetShot();
       	       		//curFuel=110; //resets fuel
				hitTest2 = 0;
       			}
       			else if(ThirdTankShot2.getState()==1)
       			{
    	     		g.drawImage(TankBoom.getImage(), Math.round(ThirdTankShot2.getX()), (Math.round(ThirdTankShot2.getY())), null);
       	       		ThirdTankShot2.resetShot();
       	       		//curFuel=110; //resets fuel
				hitTest2 = 0;

       			}       			       			
       	}
       	
       	if(TankShot.getState()==1)
        {      		
     	   if (ShotCollision(Tank1.getTankSprite(), TankShot,1)) 
     	   {
     		    FloatHealth.startCounter();
     		   	g.setColor(Color.red);
     		   	g.setFont(new Font("OCRAStd",Font.BOLD,36));
     			g.drawString("-10",(int)Tank1.getTankSprite().getX(),FloatHealth.health1counter);  
     		      
     	   }
     	   if (ShotCollision(Tank2.getTankSprite(), TankShot,1)) 
		   {
     			FloatHealth2.start2Counter();
      		   	g.setColor(Color.red);
     		   	g.setFont(new Font("OCRAStd",Font.BOLD,36));
         		g.drawString("-10",(int)Tank2.getTankSprite().getX(),FloatHealth2.health2counter);
     	   }
        }
        else if (SecondaryTankShot.getState()==1)
		{
     	   if (ShotCollision(Tank1.getTankSprite(), SecondaryTankShot,1)) 
		   {
     		    FloatHealth.startCounter();
     		   	g.setColor(Color.red);
     		   	g.setFont(new Font("OCRAStd",Font.BOLD,36));
     		    g.drawString("-20",(int)Tank1.getTankSprite().getX(),FloatHealth.health1counter);
     	   }
     	   if (ShotCollision(Tank2.getTankSprite(), SecondaryTankShot,1)) 
		   {
     		    FloatHealth2.start2Counter();
     		   	g.setColor(Color.red);
     		   	g.setFont(new Font("OCRAStd",Font.BOLD,36));
     		    g.drawString("-20",(int)Tank2.getTankSprite().getX(),FloatHealth2.health2counter);
     		   
     	   }
        }
        else if(ThirdTankShot.getState()==1)
        {
        	if (ShotCollision(Tank1.getTankSprite(), ThirdTankShot,1)) {
       		    FloatHealth.startCounter();
     	     	g.setColor(Color.red);
     		   	g.setFont(new Font("OCRAStd",Font.BOLD,36));
     		    g.drawString("-30",(int)Tank1.getTankSprite().getX(),FloatHealth.health1counter);
       		   
       	   }
       	   if (ShotCollision(Tank2.getTankSprite(), ThirdTankShot,1)) {
       		     FloatHealth2.start2Counter();
          		 g.setColor(Color.red);
     		   	 g.setFont(new Font("OCRAStd",Font.BOLD,36));
     		     g.drawString("-30",(int)Tank2.getTankSprite().getX(),FloatHealth2.health2counter);
       		   
       	   }
        }
        /*else if(FifthTankShot.getState()==1)
        {
        	if (ShotCollision(Tank1.getTankSprite(), FifthTankShot,1)) {
       		    FloatHealth.startCounter();
     		    g.setColor(Color.red);
     		   	g.setFont(new Font("OCRAStd",Font.BOLD,36));
     		    g.drawString("-50",(int)Tank1.getTankSprite().getX(),FloatHealth.health1counter);
       		   
       	   }
       	   if (ShotCollision(Tank2.getTankSprite(), FifthTankShot,1)) {
       		    FloatHealth2.start2Counter();
     		    g.setColor(Color.red);
     		   	g.setFont(new Font("OCRAStd",Font.BOLD,36));
     		    g.drawString("-50",(int)Tank2.getTankSprite().getX(),FloatHealth2.health2counter);
       		   
       	   }
        }*/
        
        if(TankShot2.getState()==1)
        {      		
         	 if (ShotCollision(Tank1.getTankSprite(), TankShot2,2)) 
         	 {
         		 FloatHealth.startCounter();
        		 g.setColor(Color.red);
     		   	 g.setFont(new Font("OCRAStd",Font.BOLD,36));
         		 g.drawString("-10",(int)Tank1.getTankSprite().getX(),FloatHealth.health1counter); 
         		 
         	 }
         	 if (ShotCollision(Tank2.getTankSprite(), TankShot2,2)) {
         		 FloatHealth2.start2Counter();
     		   	 g.setColor(Color.red);
     		   	 g.setFont(new Font("OCRAStd",Font.BOLD,36));
				 g.drawString("-10",(int)Tank2.getTankSprite().getX(),FloatHealth2.health2counter);
         		 
         	 }
          }
          else if (SecondaryTankShot2.getState()==1){
         	 if (ShotCollision(Tank1.getTankSprite(), SecondaryTankShot2,2)) {
         		 FloatHealth.startCounter();
     		   	 g.setColor(Color.red);
     		   	 g.setFont(new Font("OCRAStd",Font.BOLD,36));
     		 	 g.drawString("-20",(int)Tank1.getTankSprite().getX(),FloatHealth.health1counter);
         		 
         	 }
         	 if (ShotCollision(Tank2.getTankSprite(), SecondaryTankShot2,2)) {
         		 FloatHealth2.start2Counter();
     		     g.setColor(Color.red);
     		   	 g.setFont(new Font("OCRAStd",Font.BOLD,36));
     		   	 g.drawString("-20",(int)Tank2.getTankSprite().getX(),FloatHealth2.health2counter);
         		 
     	    }  	   
          }
          else if(ThirdTankShot2.getState()==1)
          {
     		   if (ShotCollision(Tank1.getTankSprite(), ThirdTankShot2,2)) {
         		    FloatHealth.startCounter();
     		   	    g.setColor(Color.red);
     		   	    g.setFont(new Font("OCRAStd",Font.BOLD,36));
     		   	    g.drawString("-30",(int)Tank1.getTankSprite().getX(),FloatHealth.health1counter);
         		   
         	   }
         	   if (ShotCollision(Tank2.getTankSprite(), ThirdTankShot2,2)) {
         		    FloatHealth2.start2Counter();
     		   	    g.setColor(Color.red);
     		   	    g.setFont(new Font("OCRAStd",Font.BOLD,36));
     		   	    g.drawString("-30",(int)Tank2.getTankSprite().getX(),FloatHealth2.health2counter);
         		   
     	   	   }
          }
          /*else if(FifthTankShot2.getState()==1)
          {
     		   if (ShotCollision(Tank1.getTankSprite(), FifthTankShot2,2)) {
         		    FloatHealth.startCounter();
     		   	   	g.setColor(Color.red);
     		     	g.setFont(new Font("OCRAStd",Font.BOLD,36));
     		   	    g.drawString("-50",(int)Tank1.getTankSprite().getX(),FloatHealth.health1counter);
         		   
         	   }
         	   if (ShotCollision(Tank2.getTankSprite(), FifthTankShot2,2)) {
         		   FloatHealth2.start2Counter();
     		   	   g.setColor(Color.red);
     		   	   g.setFont(new Font("OCRAStd",Font.BOLD,36));
     		   	   g.drawString("-50",(int)Tank2.getTankSprite().getX(),FloatHealth2.health2counter);
         		   
     	   	   }
         	 
          }*/
    }//end of Draw phase...

    //Used to tell when to play sound... if not included sound repeats
    boolean EndingActive = false;
    private void checkHealth(int health, Sprite player, Graphics g)//makes end of game screen.
    {
    	if (health <= 0)
    	{
    		int playerNumber = 2;
    		if (player.equals(Tank2.getTankSprite()))
    			playerNumber = 1;
    		Color c = new Color(1.0f, 1.0f, 1.0f, 0.6f);
    		g.setColor(c);
 			g.fillRoundRect(260, 205, 250, 60, 15, 15); 
 			g.setColor(Color.black);
    		g.drawImage(BOOM.getImage(), Math.round(player.getX() - 14), Math.round(player.getY() - 75), null);
    		g.drawString("GAME OVER: PLAYER " + playerNumber + " WINS",300,230);
            g.drawString("Press '0' to play again", 310, 250);
             
             //Adds sounds after game
             if(playerNumber==1){ // Player 1 wins
            	 String Ending[] = {"Sounds/winner11.wav", "Sounds/winner11.wav"};
                 int ran = (int)Math.floor((Math.random() * Ending.length));
                 g.drawImage(wImage, 0, 0, null);
                String EndingNumber = Ending[ran];
            	
             }
             else //Player 2 Wins
             {
            	 String Ending[] = {"Sounds/winner22.wav", "Sounds/winner22.wav"};
                 int ran = (int)Math.floor((Math.random() * Ending.length));
                String EndingNumber = Ending[ran];	 
            	 if(EndingActive == false){
                 
             }
    	}
          	}
    }
    
  //Creates the indentation if it hits land otherwise leaves Shot alone.
    private void fireShot(Sprite shot, Graphics g, int weapon, int player)
    {   

		if(shot.getX() > 0)//inside the screen
		{	
			if(topy[Math.round(shot.getX())] < Math.round(shot.getY()))//above the top of the terrain
			{	    		
				
			CreateHole(TankShot,1, player);
  			CreateHole(TankShot2,1, player);

  			}
		}
		
		
    }
    private void CreateHole(Sprite shot,int weapon, int player)
    {
			
			soundShot.close(); //resets sound after being shot
			if(player==1) hitTest = 2;//says the object hit
			else hitTest2 = 2;
		    int counter3=Math.round(shot.getX())-14; //Left/Right Starting Square pos adjustment on hole
			int holderSin=0;
			int holderCos=1;
			double groundHolder=180;	

			while (groundHolder <= 359)//creates hole starting at 180 degrees and going to 360
			{
				holderSin = (int)(Math.floor((Math.sin(groundHolder/57.3))*weapon*5));//uses a length of 10 for hole
				if (counter3>3)
				{
					topy[counter3] = topy[counter3]- holderSin;//reduces terrain
				}//depth of square //

				//creates an unequal cirlce        				
				if (holderCos==3)
				{
					counter3++;
					holderCos=0;	
				}

				holderCos++;
				groundHolder++;	
			}
			
    }
	


	/*
	Deprecated code. Replaced by resetShot() in Sprite.java
	Author: Chris I
		
	private Sprite resetShot(Sprite shot,int player) {
		shot.setState(0);
		shot.setVelocityX(0f);
		shot.setVelocityY(0f);
		if (player==1) {
			hitTest = 0;
		} else {
			hitTest2 = 0;
		}
    		shot.setX(1000f);
    		shot.setY(4000f);
    		
		return shot;
	}*/
	
    //If true can change terrain
    //Switches to false when first shot is fired
    boolean terrainChange = true;
    
    //KEY COMMANDS
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        
        // control and n together ends game
      if ((keyCode == KeyEvent.VK_N) && (e.isControlDown())) {
    		Tank1.setHealth(0);
    		Tank2.setHealth(0);
    		hitTest = 1;
    		hitTest2=1;
      }	
      
      /*if (keyCode == KeyEvent.VK_M) {
    	 MainScreen a = new MainScreen();
         screen.restoreScreen();
         a.animationLoop();
         a.run();
  		
	  }*/

	  if (keyCode == KeyEvent.VK_P) {
  		if (PauseMenuOpen == false)// open pause menu
  		{   
  			PauseMenuOpen = true;
  			CountdownTimer.counterTimer.stop();
  		}
		else {
			PauseMenuOpen = false; // close pause menu
			CountdownTimer.counterTimer.restart();
		}
      }
	  if (keyCode == KeyEvent.VK_U) {
	  		if (ControlsOverlayOpen == false) // if controls overlay isnt open
	  		{   
	  			ControlsOverlayOpen = true; // open the controls overlay
	  		}
			else {
				ControlsOverlayOpen = false; // close controls overlay
			}
	      }
      if (keyCode == KeyEvent.VK_Q) {
    	  if (PauseMenuOpen == true) {
			   if(WindVar<3)
			   {
				   WindVar++;
			   }
			   else
			   {
				   WindVar=0;
			   }
    	  }
    	  DataClass.setwind(WindVar);
      }  
      if (keyCode == KeyEvent.VK_G && PauseMenuOpen){
    	  if (GameSTATE == 0)
    		  GameSTATE = 1;
    	  else if (GameSTATE == 1)
    		  GameSTATE = 0;
    	  GameSTATEchanged = true;
      }
      if (keyCode == KeyEvent.VK_0) {
    	  if (PauseMenuOpen == true){ // open pause menu
    		  restartgame = 0;}
    	  else if (PauseMenuOpen == false){
    		  restartgame=0;
    	  Tank1.setHealth(100);
  		  Tank2.setHealth(100);
  		  Tank1.setSecondWeaponAmmo(2);
  		  Tank1.setThirdWeaponAmmo(1);
  		  Tank2.setSecondWeaponAmmo(2);
		  Tank2.setThirdWeaponAmmo(1);
		  curFuel = 110;  }

      }
      
     	if (keyCode == KeyEvent.VK_T) {
     		//Set Y sets the tanks to start on the ground
     		//Therefore not falling and losing health

			//If a shot is fired, can't change terrain
			if (terrainChange == true)
			{
				Tank1.getTankSprite().setY(599);
				Tank2.getTankSprite().setY(599);
				if (levelNumber == "1")
				{
					firstNum = 300;
					secondNum = 500;
					ChangeLevel("2");
				}
				else if (levelNumber == "2")
					ChangeLevel("3");
				else if (levelNumber == "3")
				{
					firstNum = 600;
					secondNum = 800;
					ChangeLevel("4");
				}
				else if (levelNumber == "4")
					ChangeLevel("5");
				else if (levelNumber == "5")
					ChangeLevel("1");
				//JumpUp resets the turrets look
				jumpUp = true;
			}
		}
     	if(!PauseMenuOpen){
     		if(GameSTATE==0) {
     			if (keyCode == KeyEvent.VK_C && Tank1.getWeapon2() == 0) {//add weapon
          		  Tank1.setWeapon2(1);
     			}
     			else if (keyCode == KeyEvent.VK_C && Tank1.getWeapon2() == 1) {
     				Tank1.setWeapon2(2);
     			}
     			else if (keyCode == KeyEvent.VK_C && Tank1.getWeapon2() == 2) {
     				Tank1.setWeapon2(0);
     			}     			
     			else {
     				
     			}
        	  
        	  
    		  if (keyCode == KeyEvent.VK_E) {
    				Tank1.setShotPower(-.01f + Tank1.getShotPower());
    				if (Tank1.getShotPower() <= -1) {
    					Tank1.setShotPower(-1);
    				}
    		  }
    			 
    			  
    		  if (keyCode == KeyEvent.VK_Q) {
    				Tank1.setShotPower(.01f + Tank1.getShotPower());
    				if (Tank1.getShotPower() >= -.01) {
    					Tank1.setShotPower((float)-.04);
    				}
    		  }
    			
    		  if (keyCode == KeyEvent.VK_W) {
               	    Tank1.increaseAngle();		
              }
    		  
    		  if (keyCode == KeyEvent.VK_S) { 
    	      		Tank1.decreaseAngle();		
    	      }
    		  
    		  if (keyCode == KeyEvent.VK_A)  {	
    		 		//if(Tank1.getMovesLeft()>0) {
    			 		if (Tank1.getTankSprite().getX() >= 3){ 
    			 			//System.out.println("Player 1'S tank fuel is at: " + curFuel);
    			 			//curFuel -= fuelDepletion; //Takes fuel away at a constant rate while the tank is moving.
    			 			moving=true;
    			 			Tank1.setMovesLeft(Tank1.getMovesLeft()-3); 
    			 			Tank1.getTankSprite().setX(Tank1.getTankSprite().getX()-3); 
    			 			jumpUp = false;
    				    }
    			 	//}
    		  }
    		  // all 6-7 hard coded values in movement seem to be irrelevant and have no change when altered.
    		  if (keyCode == KeyEvent.VK_D) {
    			 	//if(Tank1.getMovesLeft()>0) {
    			 		if (Tank1.getTankSprite().getX()+38 <= 796 ) 
    			 			{moving=true;
    			 				Tank1.setMovesLeft(Tank1.getMovesLeft()-3); 
    			 				Tank1.getTankSprite().setX(Tank1.getTankSprite().getX()+3); 
    			 				jumpUp = false;
    				    }
    			 	//}
    		  }
    		  //tucksauce
    		  if (keyCode == KeyEvent.VK_SPACE)
    		  {
    			  shotcounter2++;
    			  terrainChange = false;
    			  //Makes sure tank is no longer paralyzed
    			  if(shotcounter2 > PARALYZED){
    	      		if (TankShot.getState() == 0 && SecondaryTankShot.getState() == 0 && ThirdTankShot.getState()==0)
    	      		{

    					if(Tank1.getWeapon2()== 0){
    						TankShot.setState(1);
    					    soundShot.load("Sounds/bowShot.mid");
    		    			//soundShot.play();
    		    			}
    					else if(Tank1.getWeapon2()== 1)
    					{
    					    soundShot.load("Sounds/bowShot.mid");
    		    			soundShot.play();
    						SecondaryTankShot.setState(1);
    					}
    					else if(Tank1.getWeapon2()==2)
    					{
    						ThirdTankShot.setState(1);
    					}
    					tankshoot = 1;
    				}
    			    e.consume(); //erases the key event.
    	      }
    		  }
         	
        	  if (keyCode == KeyEvent.VK_K) { // add weapon
        		  Tank2.setWeapon2(0);
          	  }
        	  
        	  if (keyCode == KeyEvent.VK_L) { //add weapon
        		  Tank2.setWeapon2(1);
        	  }
        	  
        	  if (keyCode == KeyEvent.VK_SEMICOLON) {
        		  Tank2.setWeapon2(2);
        	  }
        	  
        	  if (keyCode == KeyEvent.VK_QUOTE) {
        		  Tank2.setWeapon2(3);
        	  }
        	  if (keyCode == KeyEvent.VK_P) {
        		  Tank2.setWeapon2(4);
        	  }
        	  
        	  if (keyCode == KeyEvent.VK_PERIOD) {
    				Tank2.setShotPower(-.01f + Tank2.getShotPower());
    				if (Tank2.getShotPower() <= -1) {
    					Tank2.setShotPower(-1);
    				}
    		  }
        	  
        	  if (keyCode == KeyEvent.VK_COMMA) {
    				Tank2.setShotPower(.01f + Tank2.getShotPower());
    				if (Tank2.getShotPower() >= -.01) {
    					Tank2.setShotPower((float)-.01);
    				}
    		  }
        	  
        	  if (keyCode == KeyEvent.VK_DOWN) { 
             	    Tank2.increaseAngle();		
              }
        	  
        	  if (keyCode == KeyEvent.VK_UP) {
    	      		Tank2.decreaseAngle();		
    	      }
        	  
        	  if (keyCode == KeyEvent.VK_LEFT) {	
    		 		//if(Tank2.getMovesLeft()>0) {
    			 		if (Tank2.getTankSprite().getX() >= 3){
    			 			moving=true;
    			 			Tank2.setMovesLeft(Tank2.getMovesLeft()-3);
    			 			Tank2.getTankSprite().setX(Tank2.getTankSprite().getX()-3);
    			 			jumpUp = false;
    				    }
    			 	//}
    		  }
        	  // Guess: the '3' above and the '796' below are boundaries for movement.
        	  // The second to last line is the only line that differs: +- 3.  Must be directions of movement. 
        	  if (keyCode == KeyEvent.VK_RIGHT) {
    			 	//if(Tank2.getMovesLeft()>0) {
    			 		if (Tank2.getTankSprite().getX()+38 <= 796 )
    			 			{moving=true;
    			 				Tank2.setMovesLeft(Tank2.getMovesLeft()-3);
    			 				Tank2.getTankSprite().setX(Tank2.getTankSprite().getX()+3);
    			 				jumpUp = false;
    				    }
    			 	//}
    		  }
        	  
        	  
        	     if (keyCode == KeyEvent.VK_M)
              {
                  shotcounter++;
                  terrainChange = false;
                  System.out.println(shotcounter);
                  
                  //Makes sure tank is no longer paralyzed
                  if(shotcounter > PARALYZED){
                      
                      if (TankShot2.getState() == 0 && SecondaryTankShot2.getState() == 0 && ThirdTankShot2.getState()==0){


                        if(Tank2.getWeapon2()== 0)
                        {
                            TankShot2.setState(1);
                            soundShot.load("Sounds/bowShot.mid");
                            soundShot.play();
                        }
                        else if(Tank2.getWeapon2()== 1)
                        {
                            soundShot.load("Sounds/bowShot.mid");
                            //soundShot.play();
                            SecondaryTankShot2.setState(1);
                        }
                        else if(Tank2.getWeapon2()== 2)
                        {
                            ThirdTankShot2.setState(1);
                        }
                        tankshoot2 = 1;
                    }
                  e.consume(); //erases the key event.
              } 
             }
        }
             else if (GameSTATE==1)      
            {
                // Player 1 key to cycle weapons = 'z' 
                if (turn==1) {
                    if (keyCode == KeyEvent.VK_R && Tank1.getWeapon2() == 0) {//add weapon
                        Tank1.setWeapon2(1);    
                    }
                    else if (keyCode == KeyEvent.VK_R && Tank1.getWeapon2() == 1) {
                        Tank1.setWeapon2(2);
                    }
                    else if (keyCode == KeyEvent.VK_R && Tank1.getWeapon2() == 2) {
                        Tank1.setWeapon2(0);
                    } 
                    else {
                        
                    }
                    
                    // Player 1 power bar increase controls = 'x'
                    if (keyCode == KeyEvent.VK_X) {
                          Tank1.setShotPower(-.01f + Tank1.getShotPower());
                          if (Tank1.getShotPower() <= -1) {
                              Tank1.setShotPower(-1);
                          }
                    }
                    // Player 1 power bar decrease controls = 'z'
                    if (keyCode == KeyEvent.VK_Z) {
                          Tank1.setShotPower(.01f + Tank1.getShotPower());
                          if (Tank1.getShotPower() >= -.01) {
                              Tank1.setShotPower((float)-.04);
                          }
                    }
                    // Player 1 move turret contols  
                    if (keyCode == KeyEvent.VK_W) {
                             Tank1.increaseAngle();        
                    }
                    
                    if (keyCode == KeyEvent.VK_S) { 
                            Tank1.decreaseAngle();        
                    }
                    // Player 1 move sprite
                    if ((keyCode == KeyEvent.VK_A) && (curFuel > 0)) {    
                           //if(Tank1.getMovesLeft()>0) {
                               if (Tank1.getTankSprite().getX() >= 3){
    			 				   curFuel -= fuelDepletion; //Takes fuel away at a constant rate while the tank is moving.
    			 				   System.out.println("Player 1'S tank fuel is at: " + curFuel);
                                   moving=true;
                                   Tank1.setMovesLeft(Tank1.getMovesLeft()-4);
                                   Tank1.getTankSprite().setX(Tank1.getTankSprite().getX()-3);
                                   jumpUp = false;
                              }
                           //}
                    }
                    
                    if ((keyCode == KeyEvent.VK_D) && (curFuel > 0)) {
                           //if(Tank1.getMovesLeft()>0) {
                               if (Tank1.getTankSprite().getX()<345 ){
                               		   curFuel -= fuelDepletion; //Takes fuel away at a constant rate while the tank is moving.
                               		   System.out.println("Player 1'S tank fuel is at: " + curFuel);
                               		   moving=true;
                                       Tank1.setMovesLeft(Tank1.getMovesLeft()-4); 
                                       Tank1.getTankSprite().setX(Tank1.getTankSprite().getX()+4);
                                       jumpUp = false;
                              }
                           //}
                    }
                    // player 1 shoot key
                    if (keyCode == KeyEvent.VK_SPACE)
                    {
                        shotcounter2++;
                        terrainChange = false;
                        //Makes sure tank is no longer paralyzed
                        if(shotcounter2 > PARALYZED){
                            if (TankShot.getState() == 0 && SecondaryTankShot.getState() == 0 && ThirdTankShot.getState()==0)
                            {

                              if(Tank1.getWeapon2()== 0){
                                  TankShot.setState(1);
                                  soundShot.load("Sounds/bowShot.mid");
                                  //soundShot.play();
                                  }
                              else if(Tank1.getWeapon2()== 1)
                              {
                                  soundShot.load("Sounds/bowShot.mid");
                                  //soundShot.play();
                                  SecondaryTankShot.setState(1);
                              }
                              else if(Tank1.getWeapon2()==2)
                              {
                                  ThirdTankShot.setState(1);
                              }
                              tankshoot = 1;
                              
                          }
                          
                    }
                        
                      turn=(2/turn); //switches to the other player's turn
                      CountdownTimer.counterTimer.restart(); //restarts the timer
                      CountdownTimer.counter=HelpScreen.time; //sets the timer to 30
                    }
                   
                }
                else if (turn==2) {
                    // player 2 cycle weapon controls
                    if (keyCode == KeyEvent.VK_R && Tank2.getWeapon2() == 0) {//add weapon
                        Tank2.setWeapon2(1);
                    }
                    else if (keyCode == KeyEvent.VK_R && Tank2.getWeapon2() == 1) {
                        Tank2.setWeapon2(2);
                    }
                    else if (keyCode == KeyEvent.VK_R && Tank2.getWeapon2() == 2) {
                        Tank2.setWeapon2(0);
                    } 
                    else {
                    }
                    // Player 2 power bar controls
                    if (keyCode == KeyEvent.VK_X) {
                          Tank2.setShotPower(-.01f + Tank2.getShotPower());
                          if (Tank2.getShotPower() <= -1) {
                              Tank2.setShotPower(-1);
                          }
                    }
                    
                    if (keyCode == KeyEvent.VK_Z) {
                          Tank2.setShotPower(.01f + Tank2.getShotPower());
                          if (Tank2.getShotPower() >= -.01) {
                              Tank2.setShotPower((float)-.01);
                          }
                    }
                    // player 2 turret movement controls
                    if (keyCode == KeyEvent.VK_S) { 
                           Tank2.increaseAngle();        
                    }
                    
                    if (keyCode == KeyEvent.VK_W) {
                            Tank2.decreaseAngle();        
                    }
                    // player 2 movement controsl
                    if ((keyCode == KeyEvent.VK_A) && (curFuel > 0)) {    
                           //if(Tank2.getMovesLeft()>0) {
                               if (Tank2.getTankSprite().getX() >395){
                                   moving=true;
                                   curFuel -= fuelDepletion; //Takes fuel away at a constant rate while the tank is moving.
    			 				   System.out.println("Player 2'S tank fuel is at: " + curFuel);
                                   Tank2.setMovesLeft(Tank2.getMovesLeft()-3);
                                   Tank2.getTankSprite().setX(Tank2.getTankSprite().getX()-3);
                                   jumpUp = false;
                              }
                           //}
                    }
                    
                    if ((keyCode == KeyEvent.VK_D) && (curFuel > 0)) {
                           //if(Tank2.getMovesLeft()>0) {
                               if (Tank2.getTankSprite().getX()<= 796 )
                                   {moving=true;
                                   curFuel -= fuelDepletion; //Takes fuel away at a constant rate while the tank is moving.
    			 				   System.out.println("Player 2'S tank fuel is at: " + curFuel);
                                       Tank2.setMovesLeft(Tank2.getMovesLeft()-3);
                                       Tank2.getTankSprite().setX(Tank2.getTankSprite().getX()+3);
                                       jumpUp = false;
                              }
                           //}
                    }
                    
                    // player 2 fire key
                    if (keyCode == KeyEvent.VK_SPACE)
                    {
                        shotcounter++;
                        terrainChange = false;
                        System.out.println(shotcounter);
                        
                        //Makes sure tank is no longer paralyzed
                        if(shotcounter > PARALYZED){
                            
                            if (TankShot2.getState() == 0 && SecondaryTankShot2.getState() == 0 && ThirdTankShot2.getState()==0){


                              if(Tank2.getWeapon2()== 0)
                              {
                                  TankShot2.setState(1);
                                  soundShot.load("Sounds/bowShot.mid");
                                  //soundShot.play();
                              }
                              else if(Tank2.getWeapon2()== 1)
                              {
                                  soundShot.load("Sounds/bowShot.mid");
                                  soundShot.play();
                                  SecondaryTankShot2.setState(1);
                              }
                              else if(Tank2.getWeapon2()== 2)
                              {
                                  ThirdTankShot2.setState(1);
                              }
                              /*else if(Tank2.getWeapon2()== 4)
                              {
                                  FifthTankShot2.setState(1);
                              }*/
                              tankshoot2 = 1;
                              
                          }
                        
                    } 
                      turn=(2/turn);  //switches the other player's turn
                      CountdownTimer.counterTimer.restart(); //restarts the timer
                      CountdownTimer.counter=HelpScreen.time; //sets the timer to 30
                      
                   }
                }
            }


            	  
		if (GameSTATE == 3) 
			{
				restartgame = 1;
			}
        	

						
			    
	    //commented out for non jumping version
/*	    if (keyCode == KeyEvent.VK_UP) {
			if (!jumpUp && !jumpFall) {
				currJump = 0;
				jumpUp = true;
				if (GameSTATE == 0) {					
					Tank1.getTankSprite().setVelocityY(-.2f);
					Tank1.setTankSlant(0);
				}
				else if (GameSTATE == 1) {
					Tank2.getTankSprite().setVelocityY(-.2f);
					Tank2.setTankSlant(0);
				}
			}
	    }
*/	    	 		
	 		
        // exit the program
        if (keyCode == KeyEvent.VK_ESCAPE) {
             System.exit(-1);
        }
        else {
        	System.out.println("You Pressed: " + KeyEvent.getKeyText(keyCode) ); //print out which key the user presses.
            e.consume(); //erases the key event.
        }}
  }

    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        
        
        if (keyCode == KeyEvent.VK_LEFT)
        {
			moving = false;
			//jump up resets turret after moving need to add a method that
			//rests the turret constantly while moving jump up doesn't work
			jumpUp = true;
        }
		if (keyCode == KeyEvent.VK_RIGHT) 
		{
			moving = false;
			//jump up resets turret after moving need to add a method that
			//rests the turret constantly while moving jump up doesn't work
			jumpUp = true;
		}
        
      	    		 e.consume(); //erases the key event.
    }

    public void keyTyped(KeyEvent e) {
// takes the input and eats it

        e.consume();
    }
    ///////////////////////////////////////
    ///Moved to KeyPressed for enter key///
    ////////Ryan Crean 4/3/08//////////////
    ///////////////////////////////////////
    
    public class MouseClass extends MouseAdapter{
    
    	public void mousePressed(MouseEvent e)
    		{/*
    		int MouseCode = e.getButton();
    	
    		if (GameSTATE != 3) // FIXED ABILITY TO NOT SHOOT AFTER GAME OVER - A.G
    			{
    			if (MouseCode == MouseEvent.BUTTON1)
    				{System.out.println("You pressed the Left Button ");
    				if (TankShot.getState() == 0 && SecondaryTankShot.getState() == 0)
    					{tankshoot = 1;}
    				}
    			if (MouseCode == MouseEvent.BUTTON3)
    				{System.out.println("You pressed the Right Button");}	
    			}*/
   		 	}
    	}
    
    //This method draws the in-game display and menus.
    public void DrawMessages(Graphics g)
    {	
    	g.drawImage(headerBar, 0, 0, null); // header background
    	
    	// Player 1 Health Bar
    	int healthBar1X = 35;
    	int healthBar1Y = 29;
    	
    	int healthBar1Value = (int) (110 - (Tank1.getHealth()*1.1));
    	
    	g.drawImage(healthIcon, 5, 28, null); // health icon
    	g.drawImage(healthBar, healthBar1X, healthBar1Y, null); // health bar
    	// Draws rectangle that covers Player 1 Health Bar when P1 loses health
    	g.drawRect(healthBar.getWidth(null)+(healthBar1X-1)-healthBar1Value,
    			healthBar1Y+1, healthBar1Value - 1, 20);
    	g.fillRect(healthBar.getWidth(null)+(healthBar1X-1)-healthBar1Value,
    			healthBar1Y+1, healthBar1Value - 1, 20);
    	
    	// Timer in the middle
    	g.setFont(new Font("SansSerif", Font.BOLD, 18));
    	g.setColor(Color.RED);
    	if(GameSTATE==1&&turn==1){
    		g.drawString(Integer.toString(CountdownTimer.counter), (int)Tank1.getTankSprite().getX(), (int)((Tank1.getTankSprite().getY())-50));
    	}
    	if(GameSTATE==1&&turn==2){
    		g.drawString(Integer.toString(CountdownTimer.counter), (int)Tank2.getTankSprite().getX(), (int)((Tank2.getTankSprite().getY())-50));
    	}
    	
    	// Tell user to press U for help
    	if(ControlsOverlayOpen==false) { // controls overlay isnt open
    		g.setFont(new Font("SansSerif", Font.BOLD, 13));
    		g.setColor(Color.WHITE);
    		g.drawString("Press 'U' for help!", 360, 585);
    	}
    	
    	// Player 1 Power Bar
    	
    	int powerBar1X = (int)Tank1.getTankSprite().getX();
    	int powerBar1Y = (int)((Tank1.getTankSprite().getY())+50);
    	
    	int powerBar1Value = (int) (Tank1.getShotPower() * -110);
    	 if (GameSTATE==1&&turn==1){ // If Player 1's Turn
    		 g.drawImage(powerIcon, powerBar1X-30, powerBar1Y, null); //power icon
    	 	g.drawImage(powerBar, powerBar1X, powerBar1Y, null); // power bar
    	 	// Draws rectangle that covers Player 1 Power Bar
    	 	g.setColor(Color.white);
    	 	g.drawRect(powerBar1X + powerBar1Value, powerBar1Y, 
    			powerBar1X - (powerBar1X-111) - powerBar1Value, 22);
    	 	g.fillRect(powerBar1X + powerBar1Value, powerBar1Y, 
    			powerBar1X - (powerBar1X-111) - powerBar1Value, 22);
    	 }
    	
    	// Player 2 Health Bar
    	int healthBar2X = 524;
    	int healthBar2Y = 29;
    	
    	int healthBar2Value = (int) (110 - (Tank2.getHealth()*1.1));
    	
    	g.drawImage(healthIcon, 494, 28, null); // health icon
    	g.drawImage(healthBar, healthBar2X, healthBar2Y, null); // health bar
    	// Draws rectangle that covers Player 2 Health Bar when P2 loses health
    	g.drawRect(healthBar.getWidth(null)+(healthBar2X-1)-healthBar2Value,
    			healthBar2Y+1, healthBar2Value - 1, 20);
    	g.fillRect(healthBar.getWidth(null)+(healthBar2X-1)-healthBar2Value,
    			healthBar2Y+1, healthBar2Value - 1, 20);
    	
    	// Player 2 Power Bar
    	int powerBar2X = (int)Tank2.getTankSprite().getX();
    	int powerBar2Y = (int)((Tank2.getTankSprite().getY())+50);
    	
    	int powerBar2Value = (int) (Tank2.getShotPower() * -110);
    	
    	 if (GameSTATE==1&&turn==2){ // If Player 2's Turn
    		 g.drawImage(powerIcon, powerBar2X-30, powerBar2Y, null); //power icon
    	 	g.drawImage(powerBar, powerBar2X, powerBar2Y, null); // power bar
    	 	// Draws rectangle that covers Player 2 Power Bar
    	 	g.drawRect(powerBar2X + powerBar2Value, powerBar2Y, 
    	 			powerBar2X - (powerBar2X-111) - powerBar2Value, 22);
    	 	g.fillRect(powerBar2X + powerBar2Value, powerBar2Y, 
    	 			powerBar2X - (powerBar2X-111) - powerBar2Value, 22);
    	 }
    	int textAlignLeft = 230;
		g.setFont(new Font("SansSerif", Font.BOLD, 13));
		g.setColor(Color.WHITE);
		
		//Player One Display
		if(GameSTATE==0||GameSTATE==1){
		g.drawString("Player 1", 5, 15);
		// g.drawString("  "+Tank1.getHealth() + "%", 97,  15);
         // g.drawString("Power: "+ Math.round(Tank1.getShotPower() * -100), 155 ,15);//displays power
         // g.drawString("Angle: "+ (int)Tank1.getAngle(),229 ,15);//displays angle 
         
         // Player Two Display
         g.drawString("Player 2", 743, 15);
         // g.drawString("  "+Tank2.getHealth() + "%",605, 15);
         //g.drawString("Power: "+ Math.round(Tank2.getShotPower() * -100),663,15);//displays power
         // g.drawString("Angle: "+ (int)Tank2.getAngle(),737,15);//displays angle

		}
         if(Windf>0f)
        	g.drawString("Wind: " + (int)(Windf*10000f) + " mph W" ,353, 15);
         else
        	g.drawString("Wind: " + (int)(Windf*-10000f) + " mph E" ,353, 15);
         
        
         if (GameSTATE==1&&turn==1) // If Player 1's Turn
         {
        	 g.setColor(Color.BLACK);
        	 // g.drawString("Player 1's turn", 410, 17);
        	 g.drawString("< < < < < < < <", 350, 75);
        	 /* g.clearRect(6, 61, 61, 14);                             // draws the timer
        	 g.setColor(Color.BLACK);                                // on the screen
        	 g.drawString("Timer: "+ CountdownTimer.counter, 8, 73); // for turn 1 */
         }
         else if (GameSTATE==1&&turn==2) // If Player 2's Turn
         {
        	 g.setColor(Color.BLACK);
        	 // g.drawString("Player 2's turn", 410, 17);
        	 g.drawString("> > > > > > > >", 350, 75);
        	 /* g.clearRect(737, 61, 61, 14);                               //draws the timer
        	 g.setColor(Color.BLACK);                                 //on the screen
        	 // g.drawString("Timer: "+CountdownTimer.counter, 739, 73); //for turn 2 */
         }
         
		 if (PauseMenuOpen == true) {
			Color c = new Color(1.0f, 1.0f, 1.0f, 0.6f);
			g.setColor(c);
			g.fillRoundRect(185, 100, 400, 350, 15, 17);
			g.setColor(Color.darkGray);
			Font f = g.getFont();
			g.setFont(new Font("SansSerif", Font.BOLD, 17));
			g.drawString("OPTIONS", textAlignLeft, 120);
			g.setFont(f);
			g.setColor(Color.darkGray);
			g.setFont(new Font("SansSerif", Font.BOLD, 14));
			g.drawString("Player One's Controls:", textAlignLeft, 140);
			g.setFont(f);
			g.setColor(Color.black);
			g.drawString("Press 'W' and 'S' keys to adjust your shot.", textAlignLeft, 155);
			g.drawString("Press 'Z' and 'X' to adjust power.", textAlignLeft, 170);
			g.drawString("Press 'A' and 'D' arrows to move left and right.", textAlignLeft, 185);
			g.drawString("Press 'R' to change weapons.",textAlignLeft, 200);
			g.drawString("Press 'SPACE' to fire.", textAlignLeft, 215);
			g.setColor(Color.darkGray);
			g.setFont(new Font("SansSerif", Font.BOLD, 14));
			g.drawString("Player Two's Controls:", textAlignLeft, 240);
			g.setFont(f);
			g.setColor(Color.black);
			g.drawString("Press 'W' and 'S' keys to adjust your shot.",
					textAlignLeft, 255);
			g.drawString("Press 'Z' and 'X' to adjust power", textAlignLeft, 270);
			g.drawString("Press 'A' and 'D' keys to move L/R.", textAlignLeft, 285);
			g.drawString("Press 'R' to change weapons.", textAlignLeft,300);
			g.drawString("Press 'SPACE' to fire.", textAlignLeft, 315);
			g.setColor(Color.darkGray);
			g.setFont(new Font("SansSerif", Font.BOLD, 12));
			g.drawString("Press 'Q' to cycle wind options:", textAlignLeft, 335);
			g.setFont(f);
			g.setColor(Color.black);

			if (WindVar == 2){
				g.drawString("Wind is on and constant.", textAlignLeft, 350);
			}else if (WindVar == 1){
				g.drawString("Wind is randomized after every shot.", textAlignLeft, 350);
			}else if (WindVar == 3){
				g.drawString("Wind is completely randomized.", textAlignLeft, 350);
			}else{
				g.drawString("Wind is Off.", textAlignLeft, 350);
			}
			g.setColor(Color.darkGray);
			g.setFont(new Font("SansSkerif", Font.BOLD, 12));
			g.drawString("Press 'G' to change Game Style:", textAlignLeft, 367);
			g.setFont(f);
			g.setColor(Color.black);

			if (GameSTATE==1) {
				g.drawString("Game is turn based.", textAlignLeft, 382);
			} 
			else {
				g.drawString("Game is dynamic.", textAlignLeft, 382);
			}
			// for testing because no values changed
			check = true;
			
			g.setColor(Color.DARK_GRAY);
			g.setFont(new Font("SansSerif", Font.BOLD, 14));
			g.drawString("Press 'T' to change terrain.", textAlignLeft, 410);
			g.drawString("Press 'R' to restart the game.", textAlignLeft, 425);
			g.drawString("Press Esc to end game.", textAlignLeft, 440);
		 }
		 if (ControlsOverlayOpen == true) {
			int controlsCol1X = 265;
			int controlsCol2X = 405;
			int controlsCol1Y = 480;
			int controlsMarginBottom = 5;
			
			Color c = new Color(0.74f, 0.59f, 0.21f, 0.3f);
			g.setColor(c);
			g.fillRect(250, 465, 320, 120);
			g.setColor(Color.white);
			Font f = g.getFont();
			g.setFont(new Font("Arial", Font.BOLD, 12));
			g.setFont(f);
			g.setColor(Color.white);
			g.drawString("Controls for both Player 1 and Player 2:", controlsCol1X, 
					controlsCol1Y);
			g.setFont(new Font("Arial", Font.PLAIN, 11));
			g.setFont(f);
			// Draw Player 1 Controls in Overlay Window
			g.drawString("AIM TURRET: Use'W' for up & 'S' for down", controlsCol1X,
					500);
			g.drawString("ADJUST POWER: Use 'X' & 'Z' to adjust power", controlsCol1X, 520);
			g.drawString("MOVE PLAYER: Use 'A' & 'D' to move L/R", controlsCol1X, 540);
			g.drawString("SPECIAL WEAPONS: Use 'R' to cycle weapons",controlsCol1X, 560);
			g.drawString("SHOOT: Use 'SPACE BAR' to shoot", controlsCol1X, 580);
			
			//Player 2's Controls
			g.setFont(new Font("Arial", Font.BOLD, 12));
			g.setFont(f);
			g.setColor(Color.white);
			/*g.drawString("Player Two's Controls:", controlsCol2X, 
					controlsCol1Y);
			g.setFont(new Font("Arial", Font.PLAIN, 11));
			g.setFont(f);
			
			g.drawString("Aim Turret: UP & DOWN", controlsCol2X,
					500);
			g.drawString("Adjust Power: , & .", controlsCol2X, 520);
			g.drawString("Move Tank: LEFT & RIGHT", controlsCol2X, 540);
			g.drawString("Special Weapons: C",controlsCol2X, 560);
			g.drawString("Shoot: SPACE", controlsCol2X, 580);*/
		 }
	}
    
    // Update/Draw the Selected Weapon Indicator
    public void updateWeaponIndicator(Graphics g) {
    	int currentWeapon = 0;
    	int currentAmmo = 0;
    	int tankX;
    	int tankY;
    	Tank currentTank;
    	String strAmmo;
    	
    	g.drawImage(weaponIndicatorBG, -4, 900, null); // P1 Background
    	g.drawImage(weaponIndicatorBG, 743, 60, null); // P2 Background
    	
    	for(int i=0; i<2; i++) { // for each of the two tanks
    		
    		if(i==0) {
    			currentTank = Tank1;
    			// Position Weapon Images for Player 1
    			tankX = 5;
    			tankY = 65;
    		} else {
    			//Position Weapon Images for Player 2
    			currentTank = Tank2;
    			tankX = 751;
    			tankY =65;
    		}
    		
    		currentWeapon = currentTank.getWeapon2();
    		
    		if(currentWeapon == 0) { // Normal
        		g.drawImage(weaponNormal, tankX, tankY, null);
        		currentAmmo = 1;
        	} else if (currentWeapon == 1) { // Nuclear 
        		g.drawImage(weaponNuclear, tankX, tankY, null);
        		currentAmmo = currentTank.getSecondWeaponAmmo();
        	} else /*(currentWeapon == 2)*/ { // Hersco
        		g.drawImage(weaponHersco, tankX, tankY, null);
        		currentAmmo = currentTank.getThirdWeaponAmmo();
        	}    		
    		if(currentWeapon==0) {
    			strAmmo = "x";
    		} else {
    			strAmmo = Integer.toString(currentAmmo); // Convert int to String
    		}
    		
    		if(currentTank==Tank1) { // For P1 side indicator
    			g.drawString(strAmmo, 40, 100);
    		} else { // it's P2's Indicator
    			g.drawString(strAmmo, 790, 100);
    		}
    	}
    }
 
//Creates and sets the Shot that is fired
    public void TankFire(Graphics g, int player)
    	{
    	float p1xveloc;
    	float p1yveloc;
    	float p2xveloc;
    	float p2yveloc;
    	if (player==1)
    		{
    		//TEST
    		//g.drawImage(TankShot.getImage(), xcoord, ycoord, null);
            
            //changing Player1AngleX and Player1AngleY does nothing
            
    	    g.drawImage(TankShot.getImage(), Tank1.getTurretAngleX(), Tank1.getTurretAngleY(), null);
    	    TankShot.setState(1);
            TankShot.setX(Tank1.getTurretAngleX());
            TankShot.setY(Tank1.getTurretAngleY());
        	p1xveloc=(float)((-1)*Tank1.getShotPower()*Math.cos(Tank1.getAngle()/57.3));
        	p1yveloc=(float)(Tank1.getShotPower()*Math.sin(Tank1.getAngle()/57.3));

            TankShot.setVelocityX(p1xveloc); 
            TankShot.setVelocityY(p1yveloc); 
            
            soundShot.load("Sounds/bowShot.mid");
    		soundShot.play();
    		
    		}
    		else
    			{
    			//g.drawImage(TankShot.getImage(), 400, 400, null);
    		 	g.drawImage(TankShot2.getImage(), Tank2.getTurretAngleX(), Tank2.getTurretAngleY(), null);
    		 	TankShot2.setState(1);
            	TankShot2.setX(Tank2.getTurretAngleX());
            	TankShot2.setY(Tank2.getTurretAngleY());
            	p2xveloc=(float)((-1)*Tank2.getShotPower()*Math.cos(Tank2.getAngle()/57.3));
            	p2yveloc=(float)(Tank2.getShotPower()*Math.sin(Tank2.getAngle()/57.3));

            	TankShot2.setVelocityX(p2xveloc); //.7 //ANGLE Adjustment
            	TankShot2.setVelocityY(p2yveloc); //.3 //POWER
    			
            	soundShot.load("Sounds/bowShot.mid");
        		soundShot.play();
        		
    			}
    	}
    
    
    
  //Creates and sets the Shot that is fired
    public void TankFire2(Graphics g,int player)
    	{
    	float p1xveloc;
    	float p1yveloc;
    	float p2xveloc;
    	float p2yveloc;
    	
    	soundShot.load("Sounds/bowShot.mid");
		soundShot.play();
		
		
    	if (player==1)
    			{
    	       	g.drawImage(SecondaryTankShot.getImage(), Tank1.getTurretAngleX(), Tank1.getTurretAngleY(), null);
            	SecondaryTankShot.setState(1);
            	SecondaryTankShot.setX(Tank1.getTurretAngleX());
            	SecondaryTankShot.setY(Tank1.getTurretAngleY());
            	p1xveloc=(float)((-1)*Tank1.getShotPower()*Math.cos(Tank1.getAngle()/57.3));
            	p1yveloc=(float)(Tank1.getShotPower()*Math.sin(Tank1.getAngle()/57.3));
            	
            	
            	SecondaryTankShot.setVelocityX(p1xveloc); //.7 //Player1AngleX Adjustment
            	SecondaryTankShot.setVelocityY(p1yveloc); //.3 //POWER
            	}
    	else
    			{
    		 	g.drawImage(SecondaryTankShot2.getImage(), Tank2.getTurretAngleX(), Tank2.getTurretAngleY(), null);
            	SecondaryTankShot2.setState(1);
            	SecondaryTankShot2.setX(Tank2.getTurretAngleX());
            	SecondaryTankShot2.setY(Tank2.getTurretAngleY());
            	p2xveloc=(float)((-1)*Tank2.getShotPower()*Math.cos(Tank2.getAngle()/57.3));
            	p2yveloc=(float)(Tank2.getShotPower()*Math.sin(Tank2.getAngle()/57.3));
            	
            	SecondaryTankShot2.setVelocityX(p2xveloc); //.7 //ANGLE Adjustment
            	SecondaryTankShot2.setVelocityY(p2yveloc); //.3 //POWER
            	}
    	}


    public void TankFire3(Graphics g,int player)
	{
	
	if (player==1)
			{
	       	g.drawImage(ThirdTankShot.getImage(), (int)(Tank2.getTankSprite().getX()), 0, null);
	       	ThirdTankShot.setState(1);
	       	ThirdTankShot.setX(Tank2.getTankSprite().getX());
	       	
	       	ThirdTankShot.setY(0);
	       	ThirdTankShot.setVelocityY(.2f);
        	}
	else
			{
			g.drawImage(ThirdTankShot2.getImage(),(int)(Tank1.getTankSprite().getX()), 0, null);
			ThirdTankShot2.setState(1);
			ThirdTankShot2.setX(Tank1.getTankSprite().getX());
			ThirdTankShot2.setY(0);
			ThirdTankShot2.setVelocityY(.2f);
        	}
	}    
    
        
    // testing note: to make it testable the caseChecks have been added
    public void DrawTerrain(Graphics g)
    	{	
    	double terrainHeight = Math.random()+3.42;
    	double terrainHeight2 = Math.random()+5;
    	int finalTerrain = Math.round((float)((terrainHeight2+terrainHeight)/2)*100);
    	terrainNum = finalTerrain;
    	int freq = Math.round((float)Math.random()*100);
    	
		int t=15;
		int tester=342;
		boolean up = true;
		double ang;
    	while (basex < 900)
    		{
    			if (t > 15)
    			{
    				if(t < 90 )
    				{
    			ang = (t/57.3);
    			tester = Math.abs((int)(Math.floor((Math.sin(ang))*freq)));
    			
    			//***********************************************************
    			//Added by Ryan K
    			freqValue = tester;
    			//***********************************************************
    			
    			caseChecks[0] = true;
    			//tester = Math.abs((int)(Math.floor((Math.sin(ang)*200))));
    			//changing 50 to higher # makes higher terrain and lower makes flatter
    			}
    		}
    		if (t > 90)
    			{
    			up=false;
    			caseChecks[1] = true;
    			}
    		if(t<15)
    		{up=true;}
    		if (topy[basex] == null)
    			{
    				//***********************************************************
    				//Added by Ryan K to call level function
    				//***********************************************************
    				level();
    				//***********************************************************	
    			} 
    		//Image terrainTexture = loadImage("images/terrainGreen.jpg");
    		// sets terrain color
    		//cloudy
    		if(levelNumber == "1" || levelNumber == "2" || levelNumber == "5")
            {
        		//Color terrainColor = new Color(0x009e00);//Terrain Color Adjustment
        		
    			g.setColor(Color.gray);
    			g.drawImage(terrainTexture, 0, 400, null);

            }
    		
    		//sunny
    		
    		if(levelNumber == "3" || levelNumber == "4")
            {
        		//Color terrainColor = new Color(0x5e3b1f);//Terrain Color Adjustment
        		//Image terrainTexture = loadImage("images/terrainGreen.jpg");
    			//g.drawImage(terrainTexture, 0, 400, null);
    			g.setColor(Color.gray);

            }
    		g.drawLine(basex,basey,topx,topy[basex]);
    		basex++;
    		topx++;
   			if (up==true)
   				{
   				t++;
   				caseChecks[2] = true;
   				}
   			if(up==false)
   				{
   				t--;
   				caseChecks[3] = true;
   				}    		
    		}
    	while (basex < 2000)
    		{
    		topy[basex]=342;//342
    		basex++;
    		caseChecks[4] = true;
    		}
    	basex=0;
    	basey=900;
    	topx=0;
    	// sets turret color
		g.setColor(Color.black);
    	caseChecks[6] = true;	
 		return;
      }
     
      // new DrawTerrain() is below
      // I've added the testing lines to it, but as it generates a different terrain I think it should be turned into a new 
      // function. 	
      
 		public void DrawTerrain2(Graphics g)
    	{	
    	String enter = "DrawTerrain has been called!!";
    	double terrainHeight = Math.random()+3.42;
    	double terrainHeight2 = Math.random()+5;
    	double abc = Math.random()*100;

    	
    	int freq = Math.round((float)Math.random()*100);
    	int q = 200;
		int t=15;
		int v=0;
		int tester=342;
		boolean up = true;
		double ang;
    	while (basex < 900)
    		{
    			if (t > 15)
    			{
    				if(t < 90 )
    				{
    			
    			ang = (t/57.3);//abc
    			tester = Math.abs((int)(Math.floor((Math.sin(ang))*freq)));
    			//tester = Math.abs((int)(Math.floor((Math.sin(ang)*200))));
    			//changing 50 to higher # makes higher terrain and lower makes flatter
    			caseChecks[0] = true;
    			}
    		}
    		if (t > 90)
    			{
    			up=false;
    			caseChecks[1] = true;
    			}
    		if(t<15)
    		{up=true;
    		}
    		int ycor = Tank1.getTurretAngleY();
    		
    		
    		if(GameSTATE==1&&turn== 1 ){
    			for(int xcor = Tank1.getTurretAngleX(); xcor<801; xcor+=50)
    			{
    				g.setColor(Color.BLACK);
    				g.drawLine(xcor, ycor, xcor+25, ycor+25);
    				ycor+=50;
			
				
    			}
    		}
    		if (topy[basex] == null)
    			{
    				//topy[basex]= finalTerrain-tester;//normal random terrain
    				topy[basex]= q++/2;//decline
    				//topy[basex]= r--/2;//incline
    				//topy[basex]= 400;//constant
    				caseChecks[5] = true;
    			} //342 - create a random terrain function from here.
    		g.setColor(Color.red);
    		g.drawLine(basex,basey,topx,topy[basex]);
    		basex++;
    		topx++;
   
   			v++;
   			if (up==true)
   				{
   					caseChecks[2] = true;
   					t++;
   					}
   			if(up==false)
   				{	
   					caseChecks[3] = true;
   					t--;
   				}    		
    		}
    	while (basex < 2000)
    		{
    		topy[basex]=342;//342
    		basex++;
    		caseChecks[4] = true;
    		}
    	basex=0;
    	basey=900;
    	topx=0;
    	g.setColor(Color.green);
    	
    	caseChecks[6] = true;
 		return; 
      }

   //*******************************************************
    
    /*****************************************************
    	*The following blocks of code correspond to the different levels
		*Ryan K
    	*****************************************************
		*/
    public void level() {
    	if(levelNumber == "1")//level 1
    		{
    			topy[basex]= terrainNum-freqValue;//normal random terrain
    		}
    	if(levelNumber == "2")//level 2
    		{    		   
    			topy[basex]= firstNum++/3;
    		}
   		if(levelNumber == "3")//level 3
    		{
   			int oo = 1080;
    		int ooo = 600;
    		for(int ko = 000; ko <= 400; ko++)
    			{
    			topy[ko] = oo--/2;
    			}
    				
    		for(int ko = 401; ko <= 801; ko++)
    			{
    			topy[ko] = ooo++/2;
    			}
    		topy[basex]= terrainNum-freqValue;
    		}
    	if(levelNumber == "4")//level 4
    		{
    			topy[basex]= secondNum--/2;//make random??
    		}
    				
    	if(levelNumber == "5")//level 5
    		{
    		topy[basex]= 400;
    		}
    		caseChecks[5] = true;
    }
   
    
    public void CreateTank(Graphics g)
  		{
  		  		
  		String call = "The Program Create Tank has been called!";
  		float rand = 0;
  		float posY = 0;
  		float p1rand = 0;
  		float p2rand = 0;
  		float finalrand = 0;
  		float p1y = 0;
  		float p2y = 0;
  		
		rand = Math.round((float)(Math.random()*200));
		p1rand = rand;
		Tank1.getTankSprite().setX(rand);
		
		posY=(float)(topy[(int)rand]-13);
  		Tank1.getTankSprite().setY(550);
  		p1y = posY;
  		
  	  	rand = Math.round((float)(Math.random()*300));
  	  	p2rand = rand;
   		rand = 700-rand;
  		
  		finalrand = p2rand-p1rand;
 
    		Tank2.getTankSprite().setX(rand);
		posY=(float)(topy[(int)rand]-13);
  		Tank2.getTankSprite().setY(550);
  		p2y = posY;
  	  	TankCreated = true;
  	  	
  	  	try {
  	  		if (testing) {
		  	  	FileWriter file = new FileWriter("output.doc", true);
		  		BufferedWriter out = new BufferedWriter(file);
		  		out.write("\n" + call + "\n");
		  		out.write("Player 1 Tank X has been set to " + p1rand + "\n");
		  		out.write("Player 1 Tank Y is set to " + p1y + "\n");
		  		out.write("Player 2 Tank X has been set to " + p2rand + "\n");
		  		out.write("Player 2 Tank Y is set to " + p2y + "\n");
		  		out.write("Distance between tanks is: " + finalrand + "\n");
		  	  	out.close();
  	  		}
  	  	}
  	  	catch(Exception e){
  	  		 System.err.println("Error: " + e.getMessage());
  	  	}
  		return;
  		}
    

  
	public void GroundCollision(Sprite tank, int player) 
	{
	    
	/*
  	 * GroundCollision()
  	 * By Eric Marcarelli, Alex Geden, Ryan Kleckner
  	 * Starts and stops the tank from falling, adjusts health, sets rotation 
  	 * angle for more realistic landings.
  	 */

  	 	final int xOffset = 20;		// Determines sprite's default position on the x axis. (+) moves sprite left. (-) moves sprite right.
  	 	final int yOffset = 60;		// Determines sprite's default position on the y axis. (+) moves sprite left. (-) moves sprite right.
  		double angle = 1;  
  		boolean falling = false; 
		int ptx = 0, opp = 0, adj = 0, highest = 1000;                          //1000 is an arbitrarly high value that will always be > tank.getY()
	
		// If the tank has fallen below ground (it moves multiple pixels between 
		// GroundCollision() calls), realign it with the center pixel. This is 
		// important for angle calculation below. 


			if (topy[(int)tank.getX()+xOffset]-yOffset < (int)tank.getY()) //Allows tank to go up inclines
				tank.setY(topy[(int)tank.getX()+xOffset]-yOffset);
			if(tank.getY()<585) //makes the tank so the tank can't go below the bottom.
			{
			// If the tank's center point is above the ground make the tank fall. Checks to see if it should still be falling
			if (topy[(int)tank.getX()+xOffset]-yOffset > (int)tank.getY())
				falling = true;
			}
		// Possibly add addional cases here that will stop it from falling for realism. 
		
		if (falling && !jumpUp) { 
			if (player == 1) {
				Tank1.fixTurret();	
			}
			else {
				Tank2.fixTurret();
			}
        		
			tank.setVelocityY(.1f);
			
			if (player == 1 && !firstfall )	{
				if (!moving && !jumpFall) 	
					Tank1.setHealth(Tank1.getHealth()-1);//originally 5 changed to make for a more balanced game
  	  			}  	
			if (player == 2&& !firstfall2) {					
				if (!moving && !jumpFall)
					Tank2.setHealth(Tank2.getHealth()-1);//originally 5 changed to make for a more balanced game
			
			}
		
		} 
		else if (!falling && !jumpUp) {
			if(player ==1) firstfall=false;
			if(player==2)firstfall2=false;
			
			tank.setVelocityY(0f);
			/*if (GameSTATE == (player-1)) {
				if (jumpFall) 
					jumpFall = false;
			}*/
		
			// Now that it stopped, calculate the rotation angle. This is done by 
			// forming a right triangle and calculating the angle using arctan(opposite/adjacent).						
						
			// We first determine if it will rotate to the left or right side. 
			// It makes the decision based on the ground at the x location of each side 
			// and the mid pt between middle and side. More cases may be necessary for 
			// additional realism. 
			
			// These loops find the "highest" (actually lowest on the game's y axis) ground
			// point within the range. It will use this to build the opposide side in the
			// triangle. The adjacent side is formed from that pt's x to the center point.
			
			if ((topy[(int)tank.getX()]-13 < (int)tank.getY()) && (topy[(int)tank.getX()+9]-13 < (int)tank.getY())) {
				// Rotating right
				for (int i = (int)(tank.getX()+13); i > tank.getX(); i--) {
					if (topy[i] < (tank.getY()+13))  {
						if (topy[i] < highest) {
							highest = topy[i];
							ptx = i;
						}
					}
				}
				
				opp = 13 - (topy[ptx] - (int)tank.getY());
				adj = 13 - (ptx - (int)tank.getX());								
			}
			
			
			
			if ((topy[(int)tank.getX()+38]-13 < (int)tank.getY()) && (topy[(int)tank.getX()+29]-14 < (int)tank.getY())) {
				// Rotating left
				
				for (int i = (int)(tank.getX()+13); i < (tank.getX()+38); i++) {
					if (topy[i] < (tank.getY()+13))  {
						if (topy[i] < highest) {
							highest = topy[i];
							ptx = i;
						}
					}
				}
			
				opp = 13 - (topy[ptx] - (int)tank.getY());
				adj = ptx - (int)tank.getX() - 13;	
				angle *= -1; 		
			}
			
			// Calculate the angle. Multiplying by the number will let us preserve the angle's sign. 
			if (adj != 0) 
				angle *= Math.atan((double)opp/adj); 
			else 
				angle = 0;
		    //commented out to try no jumping game play
			/*// If the tank's angle is too high and the tank is on a wall, make it slide down
			if (!((topy[(int)tank.getX()+38]-38) < (int)tank.getY()) && (angle > .8)) 
				tank.setX(tank.getX()+3);
			else if (!((topy[(int)tank.getX()]-38) < (int)tank.getY()) && (angle < -.8))
				tank.setX(tank.getX()-3);
			*/
			// If the tank lands at the bottom of a cliff and there is room, straighten it out 
			if (((topy[(int)tank.getX()+57]) > (int)tank.getY()) && (angle > 1.5)) 
				tank.setX(tank.getX()+14);
			
			if (tank.getX() > 57) { // fix 56px bug
				if (((topy[(int)tank.getX()-57]) > (int)tank.getY()) && (angle < -1.5)) 
					tank.setX(tank.getX()-13);
			}
			
			if (player == 1){
				Tank1.setTankSlant(angle);
				Tank1.fixTurret();
			}else{
				Tank2.setTankSlant(angle);
				Tank2.fixTurret();
			}
				
		}
		else if (jumpUp) {
			if (player == 1) {
  		    	Tank1.fixTurret();
  		    	}
			else {
				Tank2.fixTurret();

			}			
		}
				
		return;
	}


	
	public void setTesting(boolean t) {
		testing = t;
	}
	

	public void Restart() {
		restartgame=1;
	}	

	
	public static class FloatHealth {
		static int health1counter = (int)Tank1.getTankSprite().getY()-50;  
		private final static int HCOUNTER_DELAY=10000;
		static Timer health1Timer;

		public static void paintComponent(Graphics g) {
	    		if (health1Timer.isRunning()) {
	    			health1counter--;
	    			if (health1counter<=40) {
	    				health1Timer.stop();
				}
	    		}
	    	} //end paintComponent()
	    
		public static void startCounter() {
	        	if (health1Timer==null){
	        		health1counter = (int) Tank1.getTankSprite().getY()-50;
	        		health1Timer=new Timer(HCOUNTER_DELAY, new TimeHandler()); 
	        		health1Timer.start();    
	        }
	        else {
	           if (!health1Timer.isRunning())
	           {
	        	   health1Timer.restart();
	        	   
	           }
	        }
	       
	    } //end startCounter
	    
	    public static void stopCounter(){
	        health1Timer.stop();
	    }
	    private static class TimeHandler implements ActionListener
	    {
	    	public void actionPerformed(ActionEvent actionEvent)
	    	{
	    		paintComponent(null);   
	    	}
	    }
	    
	}
	
	public static class FloatHealth2 {
	
	    static int health2counter=(int)Tank2.getTankSprite().getY()-50;
	    private final static int H2COUNTER_DELAY=10000; 
	    static Timer health2Timer;
	    
	    public static void paintComponent(Graphics g)
	    {
	    	
	    	if (health2Timer.isRunning())
	    	{
	    		health2counter--;
	    		

	    		if (health2counter<=40)
	    			health2Timer.stop();
	    	}
	    }
	    
	    public static void start2Counter(){
	    	if (health2Timer==null)
		       {
		    	  health2counter=(int)Tank2.getTankSprite().getY()-50;
		    	  health2Timer=new Timer(H2COUNTER_DELAY, new TimeHandler());
		    	  health2Timer.start();
		       }
		       else
		       {
		    	   if (!health2Timer.isRunning())
		    		   health2Timer.restart();
		       }
	    }
	    public static void stopCounter(){
	        health2Timer.stop();
	    }
	    private static class TimeHandler implements ActionListener
	    {
	    	public void actionPerformed(ActionEvent actionEvent)
	    	{
	    		paintComponent(null);   
	    	}
	    }
	    /*public void setCurFuel(int fuel)
	    {
	    	curFuel= fuel;
	    }
	    public int getCurFuel()
	    {
	     	return curFuel;
	    }*/
	    
	
	}


}
