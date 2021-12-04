package main.java.controller;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;

import main.java.model.ball.Ball;
import main.java.view.BallView;

/**
 * This abstract class Ball allows other class to implement its method
 * @author Emily
 *
 */

abstract public class BallController {

    private Shape ballFace;

    private Point2D up;
    private Point2D down;
    private Point2D left;
    private Point2D right;
    
    private Ball ballModel;
    private BallView ballView;
    
    /**
     * Constructor of Ball class
     * 
     * @param center The coordinates of the center of the ball
     * @param radius The radius of the ball
     * @param inner The color code for the inner color of the ball object
     * @param border The color code for the border color of the ball object
     */
    
    public BallController(Point2D center,int radius,Color inner,Color border){

    	ballModel = new Ball(center, radius);
        ballFace = makeBall(center,radius);
        ballView = new BallView(inner, border);

    }
    
    /**
     * Abstract method to create the shape of the ball object
     * @param center The coordinates of the center of the ball
     * @param radius The radius of the ball
     * @return The shape of the ball object
     */

    public abstract Shape makeBall(Point2D center,int radius);
    
    /**
     * Method to move the ball object 
     */

    public void move(){
        RectangularShape tmp = (RectangularShape) ballFace;
        getPosition().setLocation((getPosition().getX() + getSpeedX()),(getPosition().getY() + getSpeedY()));
        double w = tmp.getWidth();
        double h = tmp.getHeight();

        tmp.setFrame((getPosition().getX() -(w / 2)),(getPosition().getY() - (h / 2)),w,h);
        setPoints(w,h);


        ballFace = tmp;
    }
    
    /**
     * Setter to set the speed of the ball
     * @param x The horizontal speed of the ball
     * @param y The vertical speed of the ball
     */

    public void setSpeed(int x,int y){
        ballModel.setXSpeed(x);
        ballModel.setYSpeed(y);
    }
    
    /**
     * Setter to set horizontal speed of the ball
     * @param s The horizontal speed of the ball
     */

    public void setXSpeed(int s){
        ballModel.setXSpeed(s);
    }
    
    /**
     * Setter to set the vertical speed of the ball
     * @param s The vertical speed of the ball
     */
    
    public void setYSpeed(int s){
        ballModel.setYSpeed(s);
    }
    
    /**
     * Method to reverse the horizontal speed of the ball (Move the ball in opposite direction horizontally)
     */

    public void reverseX(){
        int xSpeed = getSpeedX() * -1;
        ballModel.setXSpeed(xSpeed);
    }
    
    /**
     * Method to reverse the vertical speed of the ball (Move the ball in opposite direction vertically)
     */

    public void reverseY(){
        int ySpeed = getSpeedY() * -1;
        ballModel.setYSpeed(ySpeed);
    }
    
    /**
     * Getter for the border color of the ball object
     * @return Color code of the border color of the ball object
     */

    public Color getBorderColor(){
        return ballView.getBorderColor();
    }
    
    /**
     * Getter for the inner Color of the ball object
     * @return Color code of the inner color of the ball object 
     */

    public Color getInnerColor(){
        return ballView.getInnerColor();
    }
    
    /**
     * Getter for the position of the ball object
     * @return the coordinates of the ball's position
     */

    public Point2D getPosition(){
        return ballModel.getPosition();
    }
    
    /**
     * Getter for the shape of the ball object
     * @return the shape of the ball 
     */
    
    public Shape getBallFace(){
        return ballFace;
    }
    
    /**
     * Method to move the ball to point p
     * @param p coordinates of the point for the ball to move to
     */

    public void moveTo(Point p){
        getPosition().setLocation(p);

        RectangularShape tmp = (RectangularShape) ballFace;
        double w = tmp.getWidth();
        double h = tmp.getHeight();

        tmp.setFrame((getPosition().getX() -(w / 2)),(getPosition().getY() - (h / 2)),w,h);
        ballFace = tmp;
    }
    
    /**
     * Setter to set set the points of the ball
     * @param width The width of the ball
     * @param height The height of the ball
     */

    private void setPoints(double width,double height){
        getUp().setLocation(getPosition().getX(),getPosition().getY()-(height / 2));
        getDown().setLocation(getPosition().getX(),getPosition().getY()+(height / 2));

        getLeft().setLocation(getPosition().getX()-(width / 2),getPosition().getY());
        getRight().setLocation(getPosition().getX()+(width / 2),getPosition().getY());
    }
    
    /**
     * Getter for the horizontal speed of the ball 
     * @return the horizontal speed of the ball
     */

    public int getSpeedX(){
        return ballModel.getSpeedX();
    }
    
    /**
     * Getter for the vertical speed of the ball
     * @return the vertical speed of the ball
     */

    public int getSpeedY(){
        return ballModel.getSpeedY();
    }
    
    public Point2D getUp() {
		return ballModel.getUp();
	}

	public Point2D getDown() {
		return ballModel.getDown();
	}

	public Point2D getLeft() {
		return ballModel.getLeft();
	}

	public Point2D getRight() {
		return ballModel.getRight();
	}



}