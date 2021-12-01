package main.java.model.brick;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.Random;

import main.java.controller.BrickController;
import main.java.model.ball.Ball;
import main.java.view.BrickView;

/**
 * This abstract class Brick allows other class to implement its method
 * @author Emily
 *
 */

public class Brick  {

    /**
     * Objects of this class represent the cracks of the brick
     * @author Emily
     *
     */

    protected static Random rnd;

    private String name;
    Shape brickFace;

	BrickController bController;
    BrickView bView;
    
   
    /**
     * Constructor for the brick object
     * @param name Name of the brick object
     * @param pos Position of the brick object
     * @param size Size of the brick object
     * @param border Color of the border of the brick object
     * @param inner Inner color of the brick object
     * @param strength Strength of the brick object
     */

    public Brick(String name){
    	
        setRnd(new Random());
        this.setName(name);
        
    }
    

    /**
     * Setter to set the random value
     * @param rnd random value
     */

	public static void setRnd(Random rnd) {
		Brick.rnd = rnd;
	}
	
	/**
	 * Getter to get the randome value
	 * @return rnd randome value
	 */

	public static Random getRnd() {
		return rnd;
	}

	/**
	 * Setter to set the name
	 * @param name name
	 */

	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Getter to get the brickFace
	 * @return
	 */
	
	public Shape getBrickFace() {
		return brickFace;
	}

//	public void setBrickFace(Shape brickFace) {
//		this.brickFace = brickFace;
//	}



}





