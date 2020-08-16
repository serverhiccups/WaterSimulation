package com.hiccup01.JSprite;

import java.awt.*;
import java.util.ArrayList;

/**
 * The canvas that sprites are draw to.
 */
public class JSpriteCanvas extends Canvas {
	public ArrayList<JSpriteContainer> spriteStack = new ArrayList<>(); // All the sprites on the screen, in the order to be draw.
	public JSpriteMouseEventHandler eventHandler; // The event handler that handles all the mouse events on the canvas.
	public JSpriteMouseHandler defaultHandler; // The default handler to be called if no sprite ones are.
	public boolean debugMode = false; // Is he canvas in debug mode?
	// The virtual scrolling amount.
	public int virtualX = 0;
	public int virtualY = 0;

	/**
	 * Creates a JSpriteCanvas.
	 */
	public JSpriteCanvas() {
		this.eventHandler = new JSpriteMouseEventHandler(this);
		this.addMouseListener(this.eventHandler);
		this.addMouseMotionListener(this.eventHandler);
	}

	/**
	 * Finds the sprite with a certain id. Returns null if none could be found.
	 * @param id The id.
	 * @return The sprite.
	 */
	private JSpriteContainer findSpriteById(int id) {
		for(JSpriteContainer j : this.spriteStack) { // For every sprite in the stack.
			if(j.id == id) return j; // Does it have the id we want?
		}
		return null;
	}

	/**
	 * Adds a sprite to the canvas.
	 * @param sprite The sprite to add.
	 * @param id The id of the new sprite.
	 * @return The id of the new sprite.
	 * @throws JSpriteException Thrown if a sprite with the certain id already exists.
	 */
	public int addSprite(JSprite sprite, int id) throws JSpriteException {
		if(this.findSpriteById(id) != null) throw new JSpriteException("A sprite with the id " + id + " already exists"); // Is there already a sprite with the certain id?
		spriteStack.add(new JSpriteContainer(id, sprite)); // Add the sprite to the stack.
		return id;
	}

	/**
	 * Removes a sprite from the canvas.
	 * @param id The id of the sprite to remove.
	 * @return The sprite that was just removed.
	 * @throws JSpriteException Thrown if a sprite with that id doesn't exist.
	 */
	public JSprite removeSprite(int id) throws JSpriteException {
		JSpriteContainer sprite = this.findSpriteById(id); // Get the sprite.
		if(sprite == null) throw new JSpriteException("Can't remove non-existent sprite with id " + id); // If it doesn't exist.
		this.spriteStack.remove(sprite); // Remove the sprite.
		return sprite.sprite; // Return the removed sprite.
	}

	/**
	 * Sends a sprite to the back.
	 * @param id The id of the sprite to send to the back.
	 * @throws JSpriteException Thrown if a sprite with that id doesn't exist.
	 */
	public void sendToBack(int id) throws JSpriteException {
		JSpriteContainer sprite = this.findSpriteById(id); // Get the sprite
		if(sprite == null) throw new JSpriteException("Can't move non-existent sprite with id " + id); // If it doesn't exist.
		this.spriteStack.remove(sprite); // Remove the sprite.
		this.spriteStack.add(0, sprite); // Add add it to the top.
	}

	/**
	 * Sends a sprite to the front.
	 * @param id The id of the sprite to send to the front.
	 * @throws JSpriteException Thrown if a sprite with that id doesn't exist.
	 */
	public void sendToFront(int id) throws JSpriteException {
		JSpriteContainer sprite = this.findSpriteById(id); // Get the sprite.
		if(sprite == null) throw new JSpriteException("Can't move non-existent sprite with id " + id); // If it doesn't exist.
		this.spriteStack.remove(sprite); // Remove the sprite.
		this.spriteStack.add(this.spriteStack.size(), sprite); // And add it to the back.
	}

	/**
	 * Scroll the canvas horizontally a certain amount.
	 * @param amount The amount to scroll.
	 */
	public void scrollX(int amount) {
		this.virtualX += amount;
	}

	/**
	 * Scroll the canvas vertically a certain amount.
	 * @param amount The amount to scroll.
	 */
	public void scrollY(int amount) {
		this.virtualY += amount;
	}

	/**
	 * Scroll the canvas a certain amount.
	 * @param x The amount to scroll horizontally.
	 * @param y The amount to scroll vertically.
	 */
	public void scroll(int x, int y) {
		this.scrollX(x);
		this.scrollY(y);
		this.repaint();
	}

	/**
	 * Set the default mouse handler of the canvas.
	 * @param h The MouseHandler.
	 */
	public void setDefaultMouseHandler(JSpriteMouseHandler h) {
		this.defaultHandler = h;
	}

	/**
	 * Paint the canvas.
	 * @param g The graphics object to paint to to.
	 */
	@Override
	public void paint(Graphics g) {
		if (this.debugMode) System.err.println("------------------------------"); // Print a line if we're in debug mode.
		for(JSpriteContainer c : this.spriteStack) { // For every sprite container in the stack.
			JSprite s = c.sprite; // Get the sprite.
			if(!s.visible) continue; // Skip invisible sprites.
			if (this.debugMode) System.err.println("Currently painting sprite: " + s + ", id: " + c.id); // Say which sprite we're painting if we're in debug mode.
			JSpriteVisual visual = s.getVisual(s.getCurrentVisual()); // Get the current visual of the sprite.
			switch (s.getCoordinateType()) { // Find out which coordinate system the sprite is using.
				case VIRTUAL:
					visual.draw(g, s.xPosition - visual.getXOffset() - this.virtualX, s.yPosition - visual.getYOffset() - this.virtualY); // Draw the sprite's visual onto the canvas.
					if(this.debugMode) { // Draw some rectangles that show the center and the x and y of the sprite.
						g.setColor(Color.red);
						g.drawRect(s.xPosition - this.virtualX, s.yPosition - this.virtualY, 1, 1);
						g.setColor(Color.green);
						g.drawRect(s.xPosition - visual.getXOffset() - this.virtualX, s.yPosition - visual.getYOffset() - this.virtualY, 1, 1);
					}
				case PHYSICAL:
					visual.draw(g, s.xPosition - visual.getXOffset(), s.yPosition - visual.getYOffset()); // Draw the sprite's visual to the canvas.
					if(this.debugMode) { // Draw some reactangles that show the center and the x and y of the sprite.
						g.setColor(Color.red);
						g.drawRect(s.xPosition, s.yPosition, 1, 1);
						g.setColor(Color.green);
						g.drawRect(s.xPosition - visual.getXOffset(), s.yPosition - visual.getYOffset(), 1, 1);
					}
			}
		}
		// Delete ephemeral sprites.
		this.spriteStack.removeIf(c -> c.sprite.ephemeral);
	}
}
