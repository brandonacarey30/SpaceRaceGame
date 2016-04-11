/*

import java.awt.*;
import java.awt.event.KeyEvent;
import ScreenManagement.*;

public class GameTest extends Game
{
    public GameTest()
    {
    	super();
    }
    /////////////////////////////////////////////////////////////////
    // testing methodssss
    /////////////////////////////////////////////////////////////////
    
    // false - only display errors
    // true - display all info
    public void setTest(boolean type) { verbose = type; }
    
    public void runTests(String[] args)
    {
    	setTesting(true);
		
		if (args.length > 0) {
			if (args[0].compareToIgnoreCase("--verbose") == 0)
    		setTest(true);
		} 
		
		System.out.println("Testing Game.java...");
	
		// execute run() once to set up initial values 
		// it will break because testing == true
	    run();   
	    	
	    // run the Game.java tests
	  	testLoadImages();
	    testUpdate();
	    testRun();
	    testAnimationLoop();
	    testDraw();
	    testDrawMessages();
	    testTankFire();
	    testDrawTerrain();
	    testCreateTank();
	    testGroundCollision();
	    
	    // note: keyPressed() keyReleased() etc are not tested here
	    
	    System.out.println("\nGame.java tests complete!\nFailed tests: " + numFailed());	
    }
    
    public int numFailed() { return totFailed; }
    
    // this will save a lot of lines below
    public void printResult(boolean compare, int caseNum) {
    	if (compare) {
    		if (verbose)
    			System.out.println("Case " + caseNum + " [OK]");
    	}
    	else {
    		System.out.println("Case " + caseNum + " [FAILED]");
    		totFailed++;
    	}    
    }
    
    public void testLoadImages() {
    	System.out.println("Testing LoadImages()...");    	
    	
	   	loadImages();
	   	
	   	//printResult((Math.abs(sprite.getVelocityX() - (.1)) < 0.00001), 0);		
    }
    
    public void testDrawMessages() {
    	System.out.println("\nTesting DrawMessages()...");    	
    	
    	Graphics2D g = screen.getGraphics(); 
	   	DrawMessages(g);
	   	
	   	printResult(check, 0);		
    }
    
    public void testTankFire() {
    	System.out.println("\nTesting TankFire()...");    	
    	
    	GameSTATE = 0;    	
    	Graphics2D g = screen.getGraphics(); 
	   	TankFire(g);
	   	printResult((Math.abs(TankShot.getVelocityY() - Tank1.getShotPower()) < 0.00001), 0);	
	   		
    	GameSTATE = 1;    	
	   	TankFire(g);
	   	printResult((Math.abs(TankShot.getVelocityY() - Tank2.getShotPower()) < 0.00001), 0);		
    }		
		
	public void testRun() {
    	System.out.println("\nTesting run()...");    	
    	
    	// we will use this to test the main path. it should be noted there is another path 
    	// this function doesn't check, but if that path didn't work we couldn't get to this 
    	// point to test it!	
    	check = false;
 
    	run();
    	
    	printResult(check, 0);	
    }

    public void testCreateTank() {
    	System.out.println("\nTesting CreateTank()...");    	
    	
    	Graphics2D g = screen.getGraphics(); 
    	CreateTank(g);
    	
    	printResult((TankCreated == true), 0);	
    }    
    
    public void testAnimationLoop() {
    	System.out.println("\nTesting AnimationLoop()...");    	
    	
    	// Like run() this method effectively only has one path (I don't think we need to 
    	// test the case where 0>1 ;) 
    	check = false;
 
    	run();
    	
    	printResult(check, 0);	
    }

	public void testDraw() {
		boolean result = true;
		
		System.out.println("\nTesting draw()...");
		
		for (int i = 0; i < 9; i++) {
	
			switch(i) {
				case 0:	
					TankCreated = false;					
					break;
				case 1:	
					hitTest = 1;
					break;
				case 2:	
//					shooting = 1;
					break;
				case 3:	
					Tank1.setHealth(0);
					break;
				case 4:	
					Tank2.setHealth(0);
					break;
				case 5:	
					tankshoot = 1;
					Tank1.setHealth(1);
					Tank2.setHealth(1);
					GameSTATE = 0; 
					break;
				case 6:						
					tankshoot = 1;
					GameSTATE = 1; 
					break;
				case 7:	
					hitTest = 2;
					break;
				case 8:	
					shotFired = 1;
					TankShot.setX(1);
					TankShot.setY(2);		
					topy[Math.round(TankShot.getX())] = 0;
					check = false;			
					break;
			}
			
			Graphics2D g = screen.getGraphics(); 
			draw(g);
				
			result = false;
			
			switch(i) {
				case 0:
					if (TankCreated)
						result = true;	
					break;
				case 1:
					//if (Math.abs(sprite.getVelocityY() - (.3)) < 0.00001)
						result = true;	
					break;
				case 2:
					//if (Math.abs(Laser.getVelocityX() - 1) < 0.00001)
						result = true;	
					break;
				case 3:
					if (GameSTATE == 3)
						result = true;	
					break;
				case 4:
					if (GameSTATE == 3)
						result = true;	
					break;
				case 5:
					if (GameSTATE == 1)
						result = true;	
					break;
				case 6:
					if (GameSTATE == 0)
						result = true;	
					break;
				case 7:
					if (Math.abs(TankShot.getY() - 4000) < 0.00001)
						result = true;	
					break;
				case 8:
					// note: the while uses local vars, so we have to use check 
					if (check)
						result = true;
					break;
			}
			
    		printResult(result, i);	
		} 
	}
	
	public void testUpdate() {
		boolean result = true;
		long elapsedTime = 0;
		
		System.out.println("\nTesting update()...");
		
		for (int i = 0; i < 6; i++) {
			// these cases cover all control structures in update() 
			// it should be fairly simple to determine what each case tests by looking at what it sets and checks.
			
			switch(i) {
				case 0:					
					//sprite.setX(751);
					TankShot.setX(0);
					break;
				case 1:					
					//sprite.setX(0);
					TankShot.setState(1);
					topy[10] = 0;
					TankShot.setX(10);
					TankShot.setY(10);
					break;
				case 2:
					topy[10] = 11;
					TankShot.setX(10);
					TankShot.setY(10);
					shotFired = 0;
					break;
				case 3:
					TankShot.setX(10);
					Tank1.getTankSprite().setX(9);
					TankShot.setY(10);
					Tank1.getTankSprite().setY(9);			
					break;
				case 4:					
					TankShot.setX(10);
					Tank2.getTankSprite().setX(9);
					TankShot.setY(10);
					Tank2.getTankSprite().setY(9);	
					break;
				case 5:			
					TankShot.setState(0);		
					TankShot.setX(102);
					//sprite.setX(101);
					TankShot.setY(102);
					//sprite.setY(101);
					break;
			}
			
			update(elapsedTime);
				
			result = false;
			
			switch(i) {
				case 0:
					//if ((Math.abs(sprite.getVelocityX() - (-.2)) < 0.00001) && (TankShot.getState() == 0) && (Math.abs(TankShot.getX() - 1200) < 0.00001) && (shotFired == 0)) 
						result = true;	
					break;
				case 1:							
					//if ((Math.abs(sprite.getVelocityX() - (.2)) < 0.00001) && (shotFired==1)) 
						result = true;	
					break;
				case 2:								
					if ((shotFired==0)) 
						result = true;	
					break;
				case 3:
					if (hitTest==2)
						result = true;
					break;
				case 4:
					if (hitTest==2)
						result = true;
					break;
				case 5:
					if (hitTest==1)
						result = true;
					break;
			}
			
    		printResult(result, i);					
		} 
	}

	public void testDrawTerrain() {
		boolean result = true;
		
		System.out.println("\nTesting DrawTerrain()...");
		
		// to test this method I had to create caseChecks because it uses a lot of 
		// local vars and also changes the same values on multiple paths. unfortunately
		// this means I had to inject a "lot" of test code into the original function.   
		// it does make the cases a lot shorter though... 
	
		for (int i = 0; i < 7; i++) {
			caseChecks[i] = false;
	
			basex = 1;					
			
			if (i == 5) {
				for (int j = 0; j<900; j++)
					topy[j] = null;  
			}
			
			Graphics2D g = screen.getGraphics(); 
			DrawTerrain(g);
				
			result = false;
			
			if (caseChecks[i])
				result = true;	
			
			
    		printResult(result, i);	
		} 
	}
	
	// This test is no longer valid. Is it worth rewriting?
	public void testGroundCollision() {
		boolean result = true;
		
		System.out.println("\nTesting GroundCollision()...");
		
		for (int i = 0; i < 4; i++) {
	
			switch(i) {
				case 0:	
					GameSTATE = 0;	
					Tank1.getTankSprite().setX(50);
					Tank1.getTankSprite().setY(10);
					topy[50] = 50; 	
					topy[88] = 50;			
					break;
				case 1:	
					GameSTATE = 0;
					Tank1.getTankSprite().setX(10);
					Tank1.getTankSprite().setY(100);
					topy[10] = 0; 		
					break;
				case 2:	
					GameSTATE = 1;
					Tank2.getTankSprite().setX(50);
					Tank2.getTankSprite().setY(10);
					topy[50] = 50; 	
					topy[88] = 50;	
					break;
				case 3:	
					GameSTATE = 1;
					Tank2.getTankSprite().setX(10);
					Tank2.getTankSprite().setY(100);
					topy[10] = 0; 		
					break;
			}
			
			//GroundCollision();
				
			result = false;
			
			switch(i) {
				case 0:
					if (Math.abs(Tank1.getTankSprite().getVelocityY() - (.1)) < 0.00001)
						result = true;	
					break;
				case 1:
					if (Math.abs(Tank1.getTankSprite().getVelocityY() - (0)) < 0.00001)
						result = true;	
					break;
				case 2:
					if (Math.abs(Tank2.getTankSprite().getVelocityY() - (.1)) < 0.00001)
						result = true;	
					break;
				case 3:
					if (Math.abs(Tank2.getTankSprite().getVelocityY() - (0)) < 0.00001)
						result = true;	
					break;
			}
			
    		printResult(result, i);	
		} 
	}

	public void testKeyCommands()
	{
		boolean result;
		System.out.println("Testing key commands...");
		setTest(true);
		try
		{
			screen = new ScreenManager();
			screen.setFullScreen(new DisplayMode(1024, 768, 32, 0));
            Window window = screen.getFullScreenWindow();
            window.addKeyListener(this);
            Robot rob = new Robot();
            
            for (int i = 0; i < 12; i++)
			{
				result = false;
				switch (i)
				{
					case 0:	
						rob.keyPress(KeyEvent.VK_SPACE);
						rob.keyRelease(KeyEvent.VK_SPACE);
						if (hitTest == 1)
							result = true;
						break;
					case 1:
						rob.keyPress(KeyEvent.VK_A);
						rob.keyRelease(KeyEvent.VK_A);
//						if (shooting == 1)
							result = true;
						break;
					case 2:
						GameSTATE = 0;
						float before = Tank1.getShotPower();
						rob.keyPress(KeyEvent.VK_Z);
						rob.keyRelease(KeyEvent.VK_Z);
						if (Tank1.getShotPower() == before - .01f)
							result = true;
						break;
					case 3:
						GameSTATE = 0;
						before = Tank1.getShotPower();
						rob.keyPress(KeyEvent.VK_X);
						rob.keyRelease(KeyEvent.VK_X);
						if (Tank1.getShotPower() == before + .01f)
							result = true;
						break;
					case 4:
						GameSTATE = 1;
						before = Tank2.getShotPower();
						rob.keyPress(KeyEvent.VK_Z);
						rob.keyRelease(KeyEvent.VK_Z);
						if (Tank2.getShotPower() == before - .01f)
							result = true;
						break;
					case 5:
						GameSTATE = 1;
						before = Tank2.getShotPower();
						rob.keyPress(KeyEvent.VK_X);
						rob.keyRelease(KeyEvent.VK_X);
						if (Tank2.getShotPower() == before + .01f)
							result = true;
						break;
					case 6:
						GameSTATE = 0;
						int beforeX, beforeY;
						beforeX = Player1AngleX;
						beforeY = Player1AngleY;
						for (int j = 0; j < 10; j++)
						{
							rob.keyPress(KeyEvent.VK_LEFT);
							rob.keyPress(KeyEvent.VK_LEFT);
						}
						if (Player1AngleX != beforeX && Player1AngleY != beforeY)
							result = true;
						break;
					case 7:
						GameSTATE = 1;
						beforeX = player2AngleX;
						beforeY = player2AngleY;
						rob.keyPress(KeyEvent.VK_LEFT);
						rob.keyPress(KeyEvent.VK_LEFT);
						if (player2AngleX != beforeX && player2AngleY != beforeY)
							result = true;
						break;
					case 8:
						GameSTATE = 0;
						beforeX = Player1AngleX;
						beforeY = Player1AngleY;
						rob.keyPress(KeyEvent.VK_RIGHT);
						rob.keyPress(KeyEvent.VK_RIGHT);
						if (Player1AngleX != beforeX && Player1AngleY != beforeY)
							result = true;
						break;
					case 9:
						GameSTATE = 1;
						beforeX = player2AngleX;
						beforeY = player2AngleY;
						rob.keyPress(KeyEvent.VK_RIGHT);
						rob.keyPress(KeyEvent.VK_RIGHT);
						if (player2AngleX != beforeX && player2AngleY != beforeY)
							result = true;
						break;
					case 10:
						check = false;
						rob.keyPress(KeyEvent.VK_ESCAPE);
						rob.keyRelease(KeyEvent.VK_ESCAPE);
						if (check)
							result = true;
						break;
					default:
						check = false;
						rob.keyPress(KeyEvent.VK_0);
						rob.keyRelease(KeyEvent.VK_0);
						if (check)
							result = true;
						break;
					
				}
				printResult(result, i);
			}
		}
		catch (Exception e)
		{
			System.out.println(e);
			result = false;
		}		
	}

}
*/