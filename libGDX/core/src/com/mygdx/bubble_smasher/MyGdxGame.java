package com.mygdx.bubble_smasher;

import net.dermetfan.utils.libgdx.graphics.Box2DSprite;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;



public class MyGdxGame extends ApplicationAdapter {
	
	
	boolean isFirstBallGone = false;
	
	ShapeRenderer shapeRenderer;
	
	GameState gameState = GameState.Start;
	
	//  HUD
	private BitmapFont lifeText, scoreText, hintText;
	private int life = 3;
	private int score = 0;
	private int tempScore = 0;
	
	// SPRITES and Textures CREATED HERE
	SpriteBatch batch, spriteBatch;
	Texture sky, hills, grass;
	Texture robot;
	Texture ball;
	TextureRegion ballReg;
	TextureRegion ready;
	TextureRegion gameOver;
	Texture bullet;
	TextureRegion bulletReg;
	Box2DSprite ball2DSprite, robot2DSprite, bullet2DSprite, smallBall2DSprite;
	
	Rectangle rectRobot = new Rectangle();
	Rectangle rectBall = new Rectangle();
	Rectangle rectBullet = new Rectangle();
	Rectangle rectSmallBall1 = new Rectangle();
	Rectangle rectSmallBall2 = new Rectangle();
	
	// ROBOT 
	float robotSpeed = 200f;
	float robotX;
	float robotY;
	
	//Bullet
	int bulletcounted;
	
	
	// For Bouncing ball!
	 Ball bouncingBall, bouncingBall1, bouncingBall2;	
	 Body bodyBall, bodyBall1, bodyBall2; 
	 Fixture ballFixture; 
	 BodyDef bodyDef;
	 FixtureDef fixtureDef;
	 World world, antiWorld;
     Box2DDebugRenderer debugRenderer;  
     
     static final float BOX_STEP=1/60f;  
     static final int BOX_VELOCITY_ITERATIONS=8;  
     static final int BOX_POSITION_ITERATIONS=3;  
     static final float WORLD_TO_BOX=0.01f;  
     static final float BOX_WORLD_TO=100f; 
     boolean firstHitWall = true;
     
     
     private static final int VIRTUAL_WIDTH = 640;
     private static final int VIRTUAL_HEIGHT = 480;
     private static final float ASPECT_RATIO = (float)VIRTUAL_WIDTH/(float)VIRTUAL_HEIGHT;

     private Camera camera;
     private Rectangle viewport;

     
     
	@Override
	public void create () {
		
		shapeRenderer = new ShapeRenderer();
		
		
		batch = new SpriteBatch();
		spriteBatch = new SpriteBatch();
		sky = new Texture("sky.jpg");
		hills = new Texture("hills.png");
		grass = new Texture("grass.png");
		robot = new Texture("robot_small.png");
		ball = new Texture("ball.png");
		ballReg = new TextureRegion(ball);
		bullet = new Texture("bullet.png");
		bulletReg = new TextureRegion(bullet);
		
		
		ready = new TextureRegion(new Texture("ready.png"));
		gameOver = new TextureRegion(new Texture("gameover.png"));
		
		
		// HUD initiliazed
		lifeText = new BitmapFont();
		lifeText.setColor(Color.RED);
		
		scoreText = new BitmapFont(); 
		scoreText.setColor(Color.RED);
		
		hintText = new BitmapFont(); 
		hintText.setColor(Color.RED);
		
		
		// Sprites
        ball2DSprite = new Box2DSprite(ball);
        robot2DSprite =  new Box2DSprite(robot);
        bullet2DSprite = new Box2DSprite(bullet);
        
        
		//SCALE
      //  robot2DSprite.scale(1);
          
		
        antiWorld = new World(new Vector2(0, 4000), true);  
        
		// Bouncing BALL World created
		world = new World(new Vector2(0, -40), true);  
		camera = new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);         
   //     camera.position.set(camera.viewportWidth / 2 , camera.viewportHeight / 2 , 0f);  
   //     camera.update();
        
      
      //Ground body  
        BodyDef groundBodyDef = new BodyDef();  
        groundBodyDef.position.set(new Vector2(0, 10));  
        Body groundBody = world.createBody(groundBodyDef);  
        PolygonShape groundBox = new PolygonShape();  
        groundBox.setAsBox((camera.viewportWidth) * 2, 10.0f);  
        groundBody.createFixture(groundBox, 0.0f);  
        
      //LEFT WALL body  
        BodyDef groundBodyDef2 = new BodyDef();  
        groundBodyDef2.position.set(new Vector2(0, 0));  
        Body groundBody2 = world.createBody(groundBodyDef2);  
        PolygonShape groundBox2 = new PolygonShape();  
        groundBox2.setAsBox( 10.0f, (camera.viewportWidth) * 2);  
        groundBody2.createFixture(groundBox2, 0.0f);  
        
      //Rýght WALL body  
        BodyDef groundBodyDef3 = new BodyDef();  
        groundBodyDef3.position.set(new Vector2(camera.viewportWidth, 0));  
        Body groundBody3 = world.createBody(groundBodyDef3);  
        PolygonShape groundBox3 = new PolygonShape();  
        groundBox3.setAsBox( 10.0f, (camera.viewportWidth) * 2);  
        groundBody3.createFixture(groundBox3, 0.0f);  
        
     
      
        
        resetWorld();
        
        
        debugRenderer = new Box2DDebugRenderer();  
        
		
		
	}
	
	@Override
	public void resize(int width, int height) {
		// calculate new viewport
        float aspectRatio = (float)width/(float)height;
        float scale = 1f;
        Vector2 crop = new Vector2(0f, 0f);
        
        if(aspectRatio > ASPECT_RATIO)
        {
            scale = (float)height/(float)VIRTUAL_HEIGHT;
            crop.x = (width - VIRTUAL_WIDTH*scale)/2f;
        }
        else if(aspectRatio < ASPECT_RATIO)
        {
            scale = (float)width/(float)VIRTUAL_WIDTH;
            crop.y = (height - VIRTUAL_HEIGHT*scale)/2f;
        }
        else
        {
            scale = (float)width/(float)VIRTUAL_WIDTH;
        }

        float w = VIRTUAL_WIDTH*scale;
        float h = VIRTUAL_HEIGHT*scale;
        viewport = new Rectangle(crop.x, crop.y, w, h);
	}
	
	CircleShape dynamicCircle; 
	private void resetWorld() {
		
		firstHitWall = true;
		
		
		 //Dynamic Body  
			bodyDef = new BodyDef();  
         	bodyDef.type = BodyType.DynamicBody;  
        //bodyDef.angularVelocity = -10;
         	
         if(!isDestroyedBall1 && !isDestroyedBall2)	
        	bodyDef.position.set(camera.viewportWidth / 2f, camera.viewportHeight/1.5f); 
         else if(flagSmallBallOverlaps)
        	 bodyDef.position.set(camera.viewportWidth / 2f, camera.viewportHeight/1.5f); 
         else
        	 bodyDef.position.set(-10, -10);  

        
        	dynamicCircle = new CircleShape();  
         	dynamicCircle.setRadius(20f);   
        
        
        	fixtureDef = new FixtureDef();  
        	fixtureDef.shape = dynamicCircle;  
        	fixtureDef.density = 10.0f;  
        	fixtureDef.friction = 0.0f;  
        	fixtureDef.restitution = 1;   
        
       
			bodyBall = world.createBody(bodyDef);  
	        
			// set Sprite to BODY
	        bodyBall.createFixture(fixtureDef).setUserData(new FixtureSprite(ball2DSprite));
	        
	        // CREATE BALL
	        bouncingBall = new Ball(bodyBall.getPosition().x, bodyBall.getPosition().y, ballReg, bodyBall);
	        
	        
	        dynamicCircle.dispose();      
	}
	
	boolean bodyLock=true;
	private Ball resetNewWorld(int collidePosition) {
		
		firstHitWall = true;
		
		 //Dynamic Body  
		bodyDef = new BodyDef();  
     	bodyDef.type = BodyType.DynamicBody;  
  
    	bodyDef.position.set(bouncingBall.position.x + collidePosition, bouncingBall.position.y);  
    	
    	dynamicCircle = new CircleShape();  
     	dynamicCircle.setRadius(10f);   
    
    
    	fixtureDef = new FixtureDef();  
    	fixtureDef.shape = dynamicCircle;  
    	fixtureDef.density = 10.0f;  
    	fixtureDef.friction = 0.0f;  
    	fixtureDef.restitution = 1;
    	
    	Body bodyBall = world.createBody(bodyDef);  
    	
    	// set Sprite to BODY
        bodyBall.createFixture(fixtureDef).setUserData(new FixtureSprite(ball2DSprite));
        
        // Each BALL get their BODY
        if(bodyLock){
        	bodyBall1 = bodyBall;
        	bodyLock = false;
        }
        else{
        	bodyBall2 = bodyBall;
        	bodyLock = true;
        }
        
        
        dynamicCircle.dispose();   
        
        // CREATE BALL
        Ball newBouncingBall = new Ball(bodyBall.getPosition().x, bodyBall.getPosition().y, ballReg, bodyBall);
        return newBouncingBall;
	}
	
	
	

	
	
	@Override
    public void dispose() {
        batch.dispose();
        spriteBatch.dispose();
        lifeText.dispose();
        scoreText.dispose();
    }
	
	boolean isPlaying = true;
	@Override
	public void render () {
		
		// update camera
        camera.update();
        
     // set viewport
        Gdx.gl.glViewport((int) viewport.x, (int) viewport.y,
                          (int) viewport.width, (int) viewport.height);
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		
		
		if(!isFirstBallGone)
			giveImpulse();
		else
			giveImpulseSmallBalls();

		
		
		updateWorld();
		
		
		if(isPlaying)
			drawWorld();
		else{
			drawGameOverScreen();
			
			if(Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.ENTER)){
				isPlaying=true;
				// restart
				world.destroyBody(bouncingBall.ballBody);
				bouncingBall.ballBody.setUserData(null);
				bouncingBall.ballBody = null;		
				resetWorld();
			}
		}
		
//		debugRenderer.render(world, camera.combined);  
		
		
		world.step(BOX_STEP, BOX_VELOCITY_ITERATIONS, BOX_POSITION_ITERATIONS);  
		
 // System.out.println();
 // System.out.println(Gdx.graphics.getWidth() + " ***" + Gdx.graphics.getHeight());
  
	
	
		
}
	
	
	
	private void drawGameOverScreen() {
		batch.begin();

		batch.draw(sky, 0, 0);
		batch.draw(hills, -100, -30);
		batch.draw(grass, 0, -20);
		
		batch.draw(gameOver, camera.viewportWidth / 2 - gameOver.getRegionWidth() / 2, camera.viewportHeight / 2 - gameOver.getRegionHeight() / 2);
		
		scoreText.draw(batch, "Your Score is " + tempScore, camera.viewportWidth/4, camera.viewportHeight/4); 
		
		hintText.draw(batch, "Click Space or Enter to Restart Game", camera.viewportWidth /4, camera.viewportHeight/4 - 20 ); 
		
		batch.end();
	}
		
	
	Bullet bulletFired;
	boolean isFired = false;
	boolean lock = true;
	
	private void drawWorld() {
		
		batch.begin();
		
		
		batch.draw(sky, 0, 0);
		batch.draw(hills, -100, -30);
		batch.draw(grass, 0, -20);
		
		//DRAW ROBOT
		batch.draw(robot2DSprite, (int)robotX, (int)robotY);
		
		// Draw BULLETS
		if (Gdx.input.isKeyPressed(Keys.SPACE) && isFired == false ){
		
			isFired = true;
			
			bulletY = robot2DSprite.getHeight() - bullet2DSprite.getHeight();
			bulletX = robot2DSprite.getWidth()/4 + robotX;
			bulletFired = new Bullet(robotX, bulletY, bulletReg);
			bulletFired.image = bullet2DSprite;
			bulletcounted++;
		
		}
		if(isFired==true){
			batch.draw(bulletFired.image, bulletX, bulletY);
			// Movement and Collision of Bullet
			bulletController();
		}
		
		
		// DRAW HUD
		lifeText.draw(batch, "Life= " + life, 20, camera.viewportHeight-30);   
		scoreText.draw(batch, "Score= " + score, 20, camera.viewportHeight-50); 
		
		// DRAW BALLS
		FixtureSprite.draw(batch, world);
		
		
		if(gameState == GameState.Start) {
			batch.draw(ready, camera.viewportWidth / 2 - ready.getRegionWidth() / 2, camera.viewportHeight / 2 - ready.getRegionHeight() / 2);
		}
		
		batch.end();
		
		
			
		
		// DRAW HUD READY AND GAMEOVER SCREENS
/*		spriteBatch.begin();	
		spriteBatch.setProjectionMatrix(camera.combined);
		
		// DRAW BALLS
	//	FixtureSprite.draw(spriteBatch, world);
	
		
		if(gameState == GameState.GameOver) {
			spriteBatch.draw(gameOver, Gdx.graphics.getWidth() / 2 - gameOver.getRegionWidth() / 2, Gdx.graphics.getHeight() / 2 - gameOver.getRegionHeight() / 2);
		}
		if(gameState == GameState.GameOver || gameState == GameState.Running) {
			//lifeText.draw(spriteBatch, "" + life, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 60);
		}
		
		spriteBatch.end();
	*/	
		
		
//		debugDraw();
		
		
		
	}
	

	private void updateWorld(){
		
		if(Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.LEFT)) {
			if(gameState == GameState.Start) {
				gameState = GameState.Running;
			}
			if(gameState == GameState.Running) {
				//planeVelocity.set(PLANE_VELOCITY_X, PLANE_JUMP_IMPULSE);
			}
			if(gameState == GameState.GameOver) {
				gameState = GameState.Start;
			}
		}
		
		
		//ROBOT MOVEMENTS
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) 
			robotX += Gdx.graphics.getDeltaTime() * robotSpeed;
		if (Gdx.input.isKeyPressed(Keys.LEFT)) 
			robotX -= Gdx.graphics.getDeltaTime() * robotSpeed;
		
		if(robotX < -20)
			robotX = -20;
		if(robotX > camera.viewportWidth - robot2DSprite.getWidth() + 20)
			robotX = camera.viewportWidth - robot2DSprite.getWidth() + 20;
		
		
		
	
		// COLLISION RECT OF ROBOT
		rectRobot.set(robotX, robotY, robot2DSprite.getWidth(), robot2DSprite.getHeight());
		rectRobot.width = robot2DSprite.getWidth()*0.6f;
		rectRobot.x += robot2DSprite.getWidth()/6f;
		
		if(!isFirstBallGone)
			collisionOfBall();
		else 
			collisionOfSmallBalls();
		
	}
	
	
	
	private void giveImpulse(){
		
		// set Body to BALL
				bouncingBall.position = bodyBall.getPosition();
				
				// GIVE LEFT IMPULSE TO BALL
				if(firstHitWall){
					bouncingBall.ballBody.applyLinearImpulse(-500, -500, bouncingBall.position.x, bouncingBall.position.y, true);
				}
				// GIVE RIGHT IMPULSE TO BALL IF COLLIDES TO LEFT WALL
				if(bouncingBall.position.x <= 32){
					bouncingBall.ballBody.applyLinearImpulse(500, -500, bouncingBall.position.x, bouncingBall.position.y, true);
					
					firstHitWall = false;
				}
				// GIVE LEFT IMPULSE TO BALL IF COLLIDES TO RIGHT WALL
				if( bouncingBall.position.x >= 590){
					bouncingBall.ballBody.applyLinearImpulse(-500, -500, bouncingBall.position.x, bouncingBall.position.y, true);
				}
	}
	
	boolean isDestroyedBall1 = false;
	boolean isDestroyedBall2 = false;
	private void giveImpulseSmallBalls(){
		
		if(!isDestroyedBall1){
			// set Body to BALLs
			bouncingBall1.position = bodyBall1.getPosition();
		
			// GIVE LEFT IMPULSE TO  LEFT BALL
			if(firstHitWall){
				bouncingBall1.ballBody.applyLinearImpulse(-1000, -500, bouncingBall1.position.x, bouncingBall1.position.y, true);
			}
			// GIVE RIGHT IMPULSE TO BALL IF COLLIDES TO LEFT WALL
			if(bouncingBall1.position.x <= 32){
				bouncingBall1.ballBody.applyLinearImpulse(1000, -500, bouncingBall1.position.x, bouncingBall1.position.y, true);
			
				firstHitWall = false;
			}
			// GIVE LEFT IMPULSE TO BALL IF COLLIDES TO RIGHT WALL
			if( bouncingBall1.position.x >= 590){
				bouncingBall1.ballBody.applyLinearImpulse(-1000, -500, bouncingBall1.position.x, bouncingBall1.position.y, true);
			}
		}
		if(!isDestroyedBall2){
			
			// set Body to BALLs
			bouncingBall2.position = bodyBall2.getPosition();
		// GIVE Right IMPULSE TO Right BALL
				if(firstHitWall){
					bouncingBall2.ballBody.applyLinearImpulse(1000, -500, bouncingBall2.position.x, bouncingBall2.position.y, true);
				}
				// GIVE RIGHT IMPULSE TO BALL IF COLLIDES TO LEFT WALL
				if(bouncingBall2.position.x <= 32){
					bouncingBall2.ballBody.applyLinearImpulse(1000, -500, bouncingBall2.position.x, bouncingBall2.position.y, true);
						
				}
				// GIVE LEFT IMPULSE TO BALL IF COLLIDES TO RIGHT WALL
				if( bouncingBall2.position.x >= 590){
					bouncingBall2.ballBody.applyLinearImpulse(-1000, -500, bouncingBall2.position.x, bouncingBall2.position.y, true);
					
					firstHitWall = false;
				}
		}
		
	}
	
	
	
		private void collisionOfBall(){
			// COLLISION RECT OF BALL
			rectBall.set(bouncingBall.position.x - ball2DSprite.getWidth() / 14, bouncingBall.position.y - ball2DSprite.getHeight()/14, ball2DSprite.getWidth()/4, ball2DSprite.getHeight()/4);
			rectBall.height = (ball2DSprite.getHeight()/4) *0.4f;
			rectBall.width = (ball2DSprite.getWidth()/4) * 0.6f;
			
			
			// COLLISION OF BALL AND ROBOT
					if(rectRobot.overlaps(rectBall)){
									 
						if(lock){
							life--;							
							lock = false;
						}
						
						if (life == 0) {
							gameState = GameState.GameOver;		 isPlaying = false;	
							life = 3;
						}	
						// restart Ball when you FAILED
						world.destroyBody(bouncingBall.ballBody);
						bouncingBall.ballBody.setUserData(null);
						bouncingBall.ballBody = null;		
						resetWorld();
						
						
					}else {
						lock = true;
					}
		}
	
		
	boolean flagSmallBallOverlaps = false;
		private void collisionOfSmallBalls(){
			
			// COLLISION RECT OF small BALLs
			rectSmallBall1.set(bouncingBall1.position.x -10, bouncingBall1.position.y -10, ball2DSprite.getWidth()/4, ball2DSprite.getHeight()/4);
			rectSmallBall1.height = (ball2DSprite.getHeight()/4) *0.4f;
			rectSmallBall1.width = (ball2DSprite.getWidth()/4) * 0.3f;
			
			rectSmallBall2.set(bouncingBall2.position.x - 10, bouncingBall2.position.y - 10, ball2DSprite.getWidth()/4, ball2DSprite.getHeight()/4);
			rectSmallBall2.height = (ball2DSprite.getHeight()/4) *0.4f;
			rectSmallBall2.width = (ball2DSprite.getWidth()/4) * 0.3f;
				
			// COLLISION OF small BALLs AND ROBOT
			if (rectRobot.overlaps(rectSmallBall1) || rectRobot.overlaps(rectSmallBall2)) {
				flagSmallBallOverlaps = true;
				if(lock){
					life--;
					lock = false;
				}
				//GAMEOVER SCREEN
				if (life == 0) {
					gameState = GameState.GameOver;			isPlaying = false;
					life = 3;
				}	
				
				
				// restart Ball when you FAILED
				if (!isDestroyedBall1) {
						
					world.destroyBody(bouncingBall1.ballBody);
					bouncingBall1.ballBody.setUserData(null);
					bouncingBall1.ballBody = null;	
				}
				if(!isDestroyedBall2){
					
					world.destroyBody(bouncingBall2.ballBody);
					bouncingBall2.ballBody.setUserData(null);
					bouncingBall2.ballBody = null;	
				}
				isFirstBallGone = false;
				isDestroyedBall1 = false;
				isDestroyedBall2 = false;
				resetWorld();
				
				
			}else
				lock=true;
			flagSmallBallOverlaps = false;
			
			
			
			// BULLET DESTROYS small BALLs
			if (rectBullet.overlaps(rectSmallBall1) && bulletID != bulletcounted ) {				
				
				score += 40;
				isDestroyedBall1 = true;
				world.destroyBody(bouncingBall1.ballBody);
				bouncingBall1.ballBody.setUserData(null);
				bouncingBall1.ballBody = null;	
				resetWorld();
				
			}
			if (rectBullet.overlaps(rectSmallBall2) && bulletID != bulletcounted ) {
				
				score += 40;
				isDestroyedBall2 = true;
				world.destroyBody(bouncingBall2.ballBody);
				bouncingBall2.ballBody.setUserData(null);
				bouncingBall2.ballBody = null;	
				resetWorld();
				
			}
			
			//YOU WIN
			if(isDestroyedBall1 && isDestroyedBall2){
						
				isPlaying = false;
				isFired = false;
				robotX = -20;
				life = 4;
				tempScore = score;
				score = 0;
			}
			
		}
	
	
	
		float bulletX;
		float bulletY;
		int bulletID;
		private void bulletController() {
		
			// FIRE ABOVE MOVEMENT
			bulletY += Gdx.graphics.getDeltaTime() * robotSpeed;
			// SET RECT AROUND BULLET
			rectBullet.set(bulletX + bullet2DSprite.getWidth()/4, bulletY+ bullet2DSprite.getHeight()/4-4, bullet2DSprite.getWidth()/2, bullet2DSprite.getHeight()/2);
			
			// DELETE WHEN BULLET GOES TO SPACE
			if(camera.viewportHeight < bulletY)
				isFired = false;
			
			// BULLET DESTROYS BALL 2 PIECES
			if (rectBullet.overlaps(rectBall) && !isFirstBallGone) {
				
				score += 20;
				
			//	isFired = false;
				isFirstBallGone = true;
				
				bulletID = bulletcounted;
				
				// restart Ball when you Destroyed it
				world.destroyBody(bouncingBall.ballBody);
				bouncingBall.ballBody.setUserData(null);
				bouncingBall.ballBody = null;		
				bouncingBall1 = resetNewWorld(-20);
				bouncingBall2 = resetNewWorld(20);
				
			}
			
			  
		}
		
	
		private void debugDraw() {
			
			//shapeRenderer.setProjectionMatrix(camera.combined);
			shapeRenderer.begin(ShapeType.Line);		
			rectRobot.set(robotX, robotY, robot2DSprite.getWidth(), robot2DSprite.getHeight());
			rectRobot.width = robot2DSprite.getWidth()*0.6f;
			rectRobot.x += robot2DSprite.getWidth()/6f;
			shapeRenderer.rect(rectRobot.x, rectRobot.y, rectRobot.width, rectRobot.height); 
			
		
			if(isFirstBallGone){
			rectSmallBall1.set(bouncingBall1.position.x -10, bouncingBall1.position.y -10, ball2DSprite.getWidth()/4, ball2DSprite.getHeight()/4);
			rectSmallBall1.height = (ball2DSprite.getHeight()/4) *0.4f;
			rectSmallBall1.width = (ball2DSprite.getWidth()/4) * 0.3f;
			
			shapeRenderer.rect(rectSmallBall1.x, rectSmallBall1.y, rectSmallBall1.width, rectSmallBall1.height);
			
			rectSmallBall2.set(bouncingBall2.position.x - 10, bouncingBall2.position.y - 10, ball2DSprite.getWidth()/4, ball2DSprite.getHeight()/4);
			rectSmallBall2.height = (ball2DSprite.getHeight()/4) *0.4f;
			rectSmallBall2.width = (ball2DSprite.getWidth()/4) * 0.3f;
			
			shapeRenderer.rect(rectSmallBall2.x, rectSmallBall2.y, rectSmallBall2.width, rectSmallBall2.height);
			
			}
			else {
				
				rectBall.set(bouncingBall.position.x - ball2DSprite.getWidth() / 14, bouncingBall.position.y - ball2DSprite.getHeight()/14, ball2DSprite.getWidth()/4, ball2DSprite.getHeight()/4);
				rectBall.height = (ball2DSprite.getHeight()/4) *0.4f;
				rectBall.width = (ball2DSprite.getWidth()/4) * 0.6f;
				
				shapeRenderer.rect(rectBall.x, rectBall.y, rectBall.width, rectBall.height);
			}
			
			rectBullet.set(bulletX + bullet2DSprite.getWidth()/4, bulletY+ bullet2DSprite.getHeight()/4-4, bullet2DSprite.getWidth()/2, bullet2DSprite.getHeight()/2);
			shapeRenderer.rect(rectBullet.x, rectBullet.y, rectBullet.width, rectBullet.height);
			
			
			shapeRenderer.end();
		}



		
	

	
	
	
	static enum GameState {
		Start, Running, GameOver
	}
	
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		super.pause();
		
		if (isPlaying) {
			//System.out.println("PLAY");
			isPlaying = false;
		}
		else{
			//System.out.println("PAUSE");
			isPlaying = true;
			
		}
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		super.resume();
	}
	
	
}
