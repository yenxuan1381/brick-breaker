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
package main.java.controller;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

import main.java.controller.BallController;
import main.java.controller.BrickController;
import main.java.controller.PlayerController;
import main.java.model.ball.Ball;
import main.java.model.ball.RubberBall;
import main.java.model.brick.Brick;
import main.java.model.brick.Crack;
import main.java.model.player.Player;
import main.java.model.wall.LevelFactory;
import main.java.model.wall.Wall;

/**
 * Objects of this class creates a wall of bricks
 * 
 * @author Emily
 *
 */

public class WallController {

	private Random rnd;
//	private Rectangle area;
//
//	private BrickController[] bricks;
//	private BallController ball;
//
	private BrickController[][] levels;
//	private int level;

//	private Point startPoint;
//	private int brickCount;
//	private int ballCount;
//	private boolean ballLost;
	private LevelFactory levelFac;
	private Wall wallModel;

	/**
	 * Constructor to create a wall class
	 * 
	 * @param drawArea            the area of the wall
	 * @param brickCount          the amount of bricks
	 * @param lineCount           the amount of lines
	 * @param brickDimensionRatio the ratio of the brick shape
	 * @param ballPos             the coordinates of the point of the ball
	 */

	public WallController(Rectangle drawArea, int brickCount, int lineCount, double brickDimensionRatio, Point ballPos) {

//		this.startPoint = new Point(ballPos);
//		wallModel = new Wall(drawArea, ballPos);

		levelFac = new LevelFactory();

		levels = levelFac.makeLevels(drawArea, brickCount, lineCount, brickDimensionRatio);
//		level = 0;

//		ballCount = 10;
//		ballLost = false;

		rnd = new Random();
//		setPlayer((Point) ballPos.clone(), 150, 10, drawArea);
		wallModel = new Wall(drawArea, ballPos);
		makeBall(ballPos);
//		setPlayer((Point) ballPos.clone(), 150, 10, drawArea);

		int speedX, speedY;
		do {
			speedX = rnd.nextInt(10) - 5;
		} while (speedX == 0);
		do {
			speedY = -rnd.nextInt(7);
		} while (speedY == 0);

		getBall().setSpeed(speedX, speedY);
		
//		initSpeed();

		wallModel.setPlayer((Point) ballPos.clone(), 150, 10, drawArea);

//		area = drawArea;
	}
	
//	public void initSpeed() {
//		int speedX, speedY;
//		do {
//			speedX = rnd.nextInt(10) - 5;
//		} while (speedX == 0);
//		do {
//			speedY = -rnd.nextInt(7);
//		} while (speedY == 0);
//
//		getBall().setSpeed(speedX, speedY);
//	}

	/**
	 * Method to create a ball at a specific position
	 * 
	 * @param ballPos The coordinates of the point of the ball
	 */

	private void makeBall(Point2D ballPos) {
		wallModel.setBall(new RubberBall(ballPos));
	}

	/**
	 * Method to move the player and the ball object
	 */

	public void move() {
		getPlayer().move();
		getBall().move();
	}

	/**
	 * Method to find the impact made by the ball
	 * <li>if impact made between ball and player, ball change direction
	 * <li>if impact made between ball and wall, the amount of bricks decreases
	 */

	public void findImpacts() {
		if (getPlayer().impact(getBall())) {
			getBall().reverseY();
		} else if (impactWall()) {
			/*
			 * for efficiency reverse is done into method impactWall because for every brick
			 * program checks for horizontal and vertical impacts
			 */
//			brickCount--;
			wallModel.setScore(getScore() + 50);
			wallModel.setBrickCount(getBrickCount()-1);
		} else if (impactBorder()) {
			getBall().reverseX();
		} else if (getBall().getPosition().getY() < wallModel.getArea().getY()) {
			getBall().reverseY();
		} else if (getBall().getPosition().getY() > wallModel.getArea().getY() + wallModel.getArea().getHeight()) {
//			ballCount--;
			wallModel.setBallCount(getBallCount()-1);
//			ballLost = true;
			wallModel.setBallLost(true);
		}
	}

	/**
	 * Method to determine and set the direction of the ball if impact made between
	 * the ball and the wall
	 * 
	 * @return True if impact is made, False if no impact is made
	 */

	private boolean impactWall() {
		for (BrickController br : getBricks()) {
			switch (br.findImpact(getBall())) {

			case UP_IMPACT:
				getBall().reverseY();
				return br.setImpact(getBall().getDown(), Crack.UP);
			case DOWN_IMPACT:
				getBall().reverseY();
				return br.setImpact(getBall().getUp(), Crack.DOWN);
			case LEFT_IMPACT:
				getBall().reverseX();
				return br.setImpact(getBall().getRight(), Crack.RIGHT);
			case RIGHT_IMPACT:
				getBall().reverseX();
				return br.setImpact(getBall().getLeft(), Crack.LEFT);
			case NO_IMPACT:
				continue;

			}
		}
		return false;
	}

	/**
	 * Method to determine and set the direction of the ball if impact made between
	 * the ball and the border
	 * 
	 * @return True if impact is made, False if no impact is made
	 */

	private boolean impactBorder() {
		Point2D p = getBall().getPosition();
		return ((p.getX() < wallModel.getArea().getX()) || (p.getX() > (wallModel.getArea().getX() + wallModel.getArea().getWidth())));
	}

	/**
	 * Getter to get the number of bricks
	 * 
	 * @return the number of bricks
	 */

	public int getBrickCount() {
		return wallModel.getBrickCount();
	}

	/**
	 * Getter to get the number of balls left (lives)
	 * 
	 * @return the number of balls left
	 */

	public int getBallCount() {
		return wallModel.getBallCount();
	}

	/**
	 * Method to determine is the ball lost (no impact made)
	 * 
	 * @return True if the ball is lost, False if the ball is not lost
	 */

	public boolean isBallLost() {
		return wallModel.isBallLost();
	}

	/**
	 * Method to reset the coordinates of the points of the ball to the default
	 * starting position
	 */

	public void ballReset() {
		getPlayer().moveTo(wallModel.getStartPoint());
		getBall().moveTo(wallModel.getStartPoint());
//		initSpeed();
		int speedX, speedY;
		do {
			speedX = rnd.nextInt(10) - 5;
		} while (speedX == 0);
		do {
			speedY = -rnd.nextInt(7);
		} while (speedY == 0);

		getBall().setSpeed(speedX, speedY);
		wallModel.setBallLost(false);
	}

	/**
	 * Method to reset the wall to the default amount of bricks
	 */

	public void wallReset() {
		for (BrickController b : getBricks())
			b.repair();
		wallModel.setBrickCount(getBricks().length);
		wallModel.setBallCount(3);
	}

	/**
	 * Method to determine if the balls left is 0
	 * 
	 * @return True if the number of balls left is 0, False if the number of balls
	 *         left is > 0
	 */

	public boolean ballEnd() {
		return wallModel.getBallCount() == 0;
	}

	/**
	 * Method to determine if the game is done Game is done when the number of
	 * bricks left is 0
	 * 
	 * @return True if the number of bricks left is 0, False if the number of bricks
	 *         left is > 0
	 */

	public boolean isDone() {
		return wallModel.getBrickCount() == 0;
	}

	/**
	 * Method to move to the next level
	 */

	public void nextLevel() {
//		setBricks(levels[level++]);
		
		
		int nextLvl = getLevel();
		setBricks(levels[nextLvl]);
		nextLvl++;
		wallModel.setLevel(nextLvl);
		wallModel.setBrickCount(getBricks().length);
		
		wallModel.setStage(getStage() + 1);
	}

	/**
	 * Method to determine if the game has another level
	 * 
	 * @return True if the game has another level, False if the game is on the final
	 *         level
	 */

	public boolean hasLevel() {
		return getLevel() < levels.length;
	}

	/**
	 * Setter to set the horizontal speed of the ball
	 * 
	 * @param s Horizontal speed of the ball
	 */

	public void setBallXSpeed(int s) {
		getBall().setXSpeed(s);
	}

	/**
	 * Setter to set the vertical speed of the ball
	 * 
	 * @param s Vertical speed of the ball
	 */

	public void setBallYSpeed(int s) {
		getBall().setYSpeed(s);
	}

	/**
	 * Method to reset the amount of balls left to 3 balls
	 */

	public void resetBallCount() {
		wallModel.setBallCount(3);
	}

	/**
	 * Getter to get the player object
	 * 
	 * @return The player obejct
	 */

	public PlayerController getPlayer() {
		return PlayerController.getUniquePlayer();
	}

	/**
	 * Setter to set the player object
	 * 
	 * @param player The player object
	 */

//	public void setPlayer(Point ballPoint, int width, int height, Rectangle container) {
////		PlayerController.getUniquePlayer(ballPoint, width, height, container);
//		wallModel.setPlayer(ballPoint, width, height, container);
//	}

	/**
	 * Getter to get the ball object
	 * 
	 * @return The ball object
	 */

	public BallController getBall() {
		return wallModel.getBall();
	}

//	/**
//	 * Setter to set the ball object
//	 * 
//	 * @param ball The ball object
//	 */
//
//	public void setBall(BallController ball) {
//		wallModel.setBall(ball);
//	}

	/**
	 * Getter to get the array of brick objects
	 * 
	 * @return An array of brick objects
	 */

	public BrickController[] getBricks() {
		return wallModel.getBricks();
	}

	/**
	 * Setter to set the array of brick objects
	 * 
	 * @param bricks An Array of brick objects
	 */

	public void setBricks(BrickController[] bricks) {
		wallModel.setBricks(bricks);
	}
	
	public int getLevel() {
		return wallModel.getLevel();
	}

	public int getStage() {
		return wallModel.getStage();
	}

	public void setScore(int i) {
		wallModel.setScore(i);
		
	}

	public int getScore() {
		return wallModel.getScore();
	}

}