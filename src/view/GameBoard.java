/*
 *  Brick Destroy - A simple Arcade video game
 *   Copyright (C) 2017  Filippo Ranza
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package view;

import javax.swing.*;

import main.HighScore;
import model.Ball;
import model.Brick;
import model.Player;
import model.RubberBall;
import model.SpecialBrick;
import model.Wall;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;
import java.util.Timer;


/**
 * Objects of this class extend JComponenet and implements KeyListener, MouseListener and MouseMotionListener methods
 * @author Emily
 *
 */

public class GameBoard extends JComponent implements KeyListener,MouseListener,MouseMotionListener {

    private static final String CONTINUE = "Continue";
    private static final String RESTART = "Restart";
    private static final String EXIT = "Exit";
    private static final String PAUSE = "Pause Menu";
    private static final int TEXT_SIZE = 30;
    private static final Color MENU_COLOR = new Color(0,255,0);


    private static final int DEF_WIDTH = 600;
    private static final int DEF_HEIGHT = 450;

    private static final Color BG_COLOR = Color.BLACK;

    private javax.swing.Timer gameTimer;

    private Wall wall;

    private String message;

    private boolean showPauseMenu;

    private Font menuFont;

    private Rectangle continueButtonRect;
    private Rectangle exitButtonRect;
    private Rectangle restartButtonRect;
    private int strLen;
    //private int score;
    private Double r;
    private java.util.List<Ball> balls;
    private java.util.List<Brick> bricks;
    private int speedBoost;


    private DebugConsole debugConsole;
    
    /**
     * Constructor to create the game board
     * @param owner JFrame owner
     */
    
    public GameBoard(JFrame owner){
        super();

        strLen = 0;
        showPauseMenu = false;

        menuFont = new Font("Monospaced",Font.PLAIN,TEXT_SIZE);


        this.initialize();
        message = "";
        wall = new Wall(new Rectangle(0,0,DEF_WIDTH,DEF_HEIGHT),30,3,6/2,new Point(300,430));

        debugConsole = new DebugConsole(owner,wall,this);
        
        //initialize the first level
        wall.nextLevel();
        
        //score = 99999;

        bricks = new ArrayList<Brick>();
        
        gameTimer = new javax.swing.Timer(10,e ->{

        	wall.move();
            wall.findImpacts();
            message = String.format("Bricks: %d %nBalls %d",wall.getBrickCount(),wall.getBallCount());
 
            
            for(Brick br : wall.getBricks()) {
            	
            	// If a new special brick is broken, unlock cheat mode
            	if (br.getClass() == SpecialBrick.class && br.isBroken()) {
            		
            		if(!bricks.contains(br)) {
            			bricks.add(br);
            			
            			Random rand = new Random();
            			r = rand.nextDouble();
            			
            			// 70% trigger odd bounce, 30% Speed boost
            			if(r < 0.7) {
            				oddBounce();
            			}
            			else {
            				superSpeedBall();
            			}

            		}		
            	}
            }
            
            
            if(wall.isBallLost()){
                if(wall.ballEnd()){
                    wall.wallReset();
                    message = "Game over";
                }
                wall.ballReset();
                gameTimer.stop();
            }
            else if(wall.isDone()){
                if(wall.hasLevel()){
                    message = "Go to Next Level";
                    gameTimer.stop();
                    
                    //saveHighScore(score);
                    
                    wall.ballReset();
                    wall.wallReset();
                    wall.nextLevel();
                    
                }
                else{
                    message = "ALL WALLS DESTROYED";
                    //saveHighScore(score);
                    gameTimer.stop();
                }
            }

            //score--;
            repaint();
        });

    }
    
    /**
     * Method to apply speed boost to the ball 
     */
    private void superSpeedBall() {
    	
    	speedBoost = 2;
    	
    	int speedX = wall.getBall().getSpeedX();
    	int speedY = wall.getBall().getSpeedY();
    	
    	if(speedX > 0) {
    		speedX += speedBoost;
    	}
    	else {
    		speedX -= speedBoost;
    	}
    	
    	speedY += speedBoost;
        
        wall.getBall().setSpeed(speedX,speedY);
        wall.getPlayer().move(speedBoost);
	}
    
   /**
    * Method to allow the ball to bounce oddly 
    */
    private void oddBounce() {
    	
    	balls = new ArrayList<>();
    	
    	Point2D p = wall.getBall().getPosition();
    	
    	balls.add(wall.getBall());
    	
    	Random rnd = new Random();

    	Ball ballA = new RubberBall(p);
    	Ball ballB = new RubberBall(p);
    	
    	balls.add(ballA);
    	balls.add(ballB);
    	
    	for(Ball b: balls) {
    		
    		b.makeBall(p, 10);
  
            int speedX,speedY;
            do{
                speedX = rnd.nextInt(7) - 3;
            }while(speedX == 0);
            do{
                speedY = -rnd.nextInt(5);
            }while(speedY == 0);

            b.setSpeed(speedX,speedY);
            
            b.move();
            wall.findImpacts();
    	} 	
    	
    }

	/**
     * Method to initialize the variables
     */

    private void initialize(){
        this.setPreferredSize(new Dimension(DEF_WIDTH,DEF_HEIGHT));
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }
    
//    /**
//     * Method to save the highscore into the highscore.txt file
//     * @param score The score
//     */
//    
//    private void saveHighScore(int score) {
//    	int[] highscores = HighScore.loadScores();
//    	int [] updatedHighScores = new int[10];
//    	
//    	for(int i = 0; i < highscores.length; i++) {
//    		// Checks if the current score is greater than the score being checked
//            if (score > highscores[i]) {
//                // Sets the updated score in the parallel array
//                updatedHighScores[i] = score;
//                // Bumps the rest of the scores down
//                for (int x = i+1; x < updatedHighScores.length; x++) {
//                    updatedHighScores[x] = highscores[x-1];
//                }
//                break;
//            } else {
//                updatedHighScores[i] = highscores[i];
//            }
//            
//            HighScore.saveScores(updatedHighScores);
//            
//    	}
//    }
    

    /**
     * Method to paint the graphics
     */

    public void paint(Graphics g){

        Graphics2D g2d = (Graphics2D) g;

        clear(g2d);

        g2d.setColor(Color.BLUE);
        g2d.drawString(message,250,225);

        drawBall(wall.getBall(),g2d);
         
  
        //for bricks in the wall, 
        for(Brick b : wall.getBricks())
        	// if brick is not broken, draw the brick
            if(!b.isBroken())
                drawBrick(b,g2d);

        drawPlayer(wall.getPlayer(),g2d);

        if(showPauseMenu)
            drawMenu(g2d);


        Toolkit.getDefaultToolkit().sync();
    }
    
    /**
     * Method to clear the graphics
     * @param g2d graphics
     */
    
    private void clear(Graphics2D g2d){
        Color tmp = g2d.getColor();
        g2d.setColor(BG_COLOR);
        g2d.fillRect(0,0,getWidth(),getHeight());
        g2d.setColor(tmp);
    }
    
    /**
     * Method to draw the bricks
     * @param brick Brick object
     * @param g2d Graphics
     */

    private void drawBrick(Brick brick,Graphics2D g2d){
        Color tmp = g2d.getColor();

        g2d.setColor(brick.getInnerColor());
        g2d.fill(brick.getBrick());

        g2d.setColor(brick.getBorderColor());
        g2d.draw(brick.getBrick());


        g2d.setColor(tmp);
    }
    
    /**
     * Method to draw the ball
     * @param ball Ball object
     * @param g2d Graphics
     */

    private void drawBall(Ball ball,Graphics2D g2d){
        Color tmp = g2d.getColor();

        Shape s = ball.getBallFace();

        g2d.setColor(ball.getInnerColor());
        g2d.fill(s);

        g2d.setColor(ball.getBorderColor());
        g2d.draw(s);

        g2d.setColor(tmp);
    }
    
    /**
     * Method to draw the player
     * @param p Player object
     * @param g2d Graphics
     */

    private void drawPlayer(Player p,Graphics2D g2d){
        Color tmp = g2d.getColor();

        Shape s = p.getPlayerFace();
        g2d.setColor(Player.INNER_COLOR);
        g2d.fill(s);

        g2d.setColor(Player.BORDER_COLOR);
        g2d.draw(s);

        g2d.setColor(tmp);
    }

    /**
     * Method to draw the menu
     * @param g2d Graphics
     */
    
    private void drawMenu(Graphics2D g2d){
        obscureGameBoard(g2d);
        drawPauseMenu(g2d);
    }
    
    /**
     * Method to draw the game board
     * @param g2d Graphics
     */

    private void obscureGameBoard(Graphics2D g2d){

        Composite tmp = g2d.getComposite();
        Color tmpColor = g2d.getColor();

        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.55f);
        g2d.setComposite(ac);

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0,0,DEF_WIDTH,DEF_HEIGHT);

        g2d.setComposite(tmp);
        g2d.setColor(tmpColor);
    }
    
    
    /**
     * Method to draw the pause menu
     * @param g2d Graphics
     */

    private void drawPauseMenu(Graphics2D g2d){
        Font tmpFont = g2d.getFont();
        Color tmpColor = g2d.getColor();

        g2d.setFont(menuFont);
        g2d.setColor(MENU_COLOR);

        if(strLen == 0){
            FontRenderContext frc = g2d.getFontRenderContext();
            strLen = menuFont.getStringBounds(PAUSE,frc).getBounds().width;
        }

        int x = (this.getWidth() - strLen) / 2;
        int y = this.getHeight() / 10;

        g2d.drawString(PAUSE,x,y);

        x = this.getWidth() / 8;
        y = this.getHeight() / 4;


        if(continueButtonRect == null){
            FontRenderContext frc = g2d.getFontRenderContext();
            continueButtonRect = menuFont.getStringBounds(CONTINUE,frc).getBounds();
            continueButtonRect.setLocation(x,y-continueButtonRect.height);
        }

        g2d.drawString(CONTINUE,x,y);

        y *= 2;

        if(restartButtonRect == null){
            restartButtonRect = (Rectangle) continueButtonRect.clone();
            restartButtonRect.setLocation(x,y-restartButtonRect.height);
        }

        g2d.drawString(RESTART,x,y);

        y *= 3.0/2;

        if(exitButtonRect == null){
            exitButtonRect = (Rectangle) continueButtonRect.clone();
            exitButtonRect.setLocation(x,y-exitButtonRect.height);
        }

        g2d.drawString(EXIT,x,y);



        g2d.setFont(tmpFont);
        g2d.setColor(tmpColor);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        switch(keyEvent.getKeyCode()){
            case KeyEvent.VK_A:
                wall.getPlayer().moveLeft();
                break;
            case KeyEvent.VK_D:
                wall.getPlayer().moveRight();
                break;
            case KeyEvent.VK_ESCAPE:
                showPauseMenu = !showPauseMenu;
                repaint();
                gameTimer.stop();
                break;
            case KeyEvent.VK_SPACE:
                if(!showPauseMenu)
                    if(gameTimer.isRunning())
                        gameTimer.stop();
                    else
                        gameTimer.start();
                break;
            case KeyEvent.VK_F1:
                if(keyEvent.isAltDown() && keyEvent.isShiftDown())
                    debugConsole.setVisible(true);
            default:
                wall.getPlayer().stop();
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        wall.getPlayer().stop();
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();
        if(!showPauseMenu)
            return;
        if(continueButtonRect.contains(p)){
            showPauseMenu = false;
            repaint();
        }
        else if(restartButtonRect.contains(p)){
            message = "Restarting Game...";
            wall.ballReset();
            wall.wallReset();
            showPauseMenu = false;
            repaint();
        }
        else if(exitButtonRect.contains(p)){
            System.exit(0);
        }

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();
        if(exitButtonRect != null && showPauseMenu) {
            if (exitButtonRect.contains(p) || continueButtonRect.contains(p) || restartButtonRect.contains(p))
                this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            else
                this.setCursor(Cursor.getDefaultCursor());
        }
        else{
            this.setCursor(Cursor.getDefaultCursor());
        }
    }
    
    /**
     * Method to pause the game when focus is lost
     */

    public void onLostFocus(){
        gameTimer.stop();
        message = "Focus Lost";
        repaint();
    }

}
