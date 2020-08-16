package com.hiccup01.JSprite;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The root of all evil. JSprite is a container that holds visuals and sprite information.
 */
public class JSprite {
	// Ephemeral sprites are deleted after one paint operation
	public Boolean ephemeral = false;
	public Boolean visible = true; // Can we see the sprite? Also makes mouse ignore the sprite.
	// The position of the sprite.
	public int xPosition;
	public int yPosition;
	private HashMap<Integer, JSpriteVisual> visuals = new HashMap<>(); // The visuals belonging to this sprite.
	public ArrayList<JSpriteMouseHandler> mouseHandlers = new ArrayList<>(); // The mouse handlers that are called when appropriate.
	int currentVisual; // Which visual are we currently we using.
	private JSpriteCoordinateType coordinateType = JSpriteCoordinateType.VIRTUAL; // The coordinate type that this sprite is using for the x and y position.

	/**
	 * Creates a JSprite.
	 * @param x The X position.
	 * @param y The Y position.
	 * @param firstVisual The first visual to use.
	 */
	public JSprite(int x, int y, JSpriteVisual firstVisual) {
		visuals.put(0, firstVisual);
		this.currentVisual = 0;
		this.xPosition = x;
		this.yPosition = y;
	}

	/**
	 * Sets the current visual.
	 * @param id The visual to use.
	 */
	public void setCurrentVisual(int id) {
		if(visuals.containsKey(id)) this.currentVisual = id;
	}

	/**
	 * Gets the current visual.
	 * @return The current visual.
	 */
	public int getCurrentVisual() {
		return this.currentVisual;
	}

	/**
	 * Gets a visual.
	 * @param id Which visual to get.
	 * @return The visual.
	 */
	public JSpriteVisual getVisual(int id) {
		return visuals.get(id);
	}

	/**
	 * Sets a visual.
	 * @param id The id of the visual to set.
	 * @param v the visual.
	 */
	public void setVisual(int id, JSpriteVisual v) {
		this.visuals.put(id, v);
	}

	/**
	 * Gets the coordinate type the sprite is using.
	 * @return The coordinate type.
	 */
	public JSpriteCoordinateType getCoordinateType() {
		return coordinateType;
	}

	/**
	 * Sets the coordinate type. Doesn't do anything if the type is JSpriteCoordinateType.RELATIVE because that doesn't make sense.
	 * @param coordinateType The coordinate type.
	 */
	public void setCoordinateType(JSpriteCoordinateType coordinateType) {
		if(coordinateType != JSpriteCoordinateType.RELATIVE) this.coordinateType = coordinateType;
	}

	/**
	 * Adds a mouse handler to use to the stack.
	 * @param m The mouse handler.
	 */
	public void addMouseHandler(JSpriteMouseHandler m) {
		this.mouseHandlers.add(m);
	}

	/**
	 * Removes the mouse handler at the top of the stack.
	 */
	public void removeMouseHandler() {
		this.mouseHandlers.remove(this.mouseHandlers.size() - 1);
	}

	/**
	 * Gets the upper left X of the current visual.
	 * @return The upper left X.
	 */
	public int getUpperLeftX() {
		return this.getVisual(this.getCurrentVisual()).getXOffset();
	}

	/**
	 * Gets the upper left Y of the current visual.
	 * @return The upper left Y.
	 */
	public int getUpperLeftY() {
		return this.getVisual(this.getCurrentVisual()).getYOffset();
	}
}
