package com.hiccup01.JSprite;

import java.awt.*;

// JSpriteVisual is the next-gen replacement for JSpriteCostume. It allows you to draw anything to the canvas as a sprite, not just an image
public interface JSpriteVisual {
	/**
	 * Gets the current offset mode.
	 * @return The offset mode.
	 */
	public JSpriteOffsetMode getOffsetMode();

	/**
	 * Sets the current offset mode.
	 * @param m The offset mode to change to.
	 */
	public void setOffsetMode(JSpriteOffsetMode m);

	/**
	 * Gets the current X offset.
	 * @return The X offset.
	 */
	public int getXOffset();

	/**
	 * Gets the current Y offset.
	 * @return The Y offset.
	 */
	public int getYOffset();

	/**
	 * Gets the width of the current visual.
	 * @return The width.
	 */
	public int getWidth();

	/**
	 * Gets the hieght of the current visual.
	 * @return The height.
	 */
	public int getHeight();

	/**
	 * Draw the visual *at* the given X and Y.
	 * @param g The graphics object to draw to.
	 * @param x The X position.
	 * @param y The Y position.
	 */
	public void draw(Graphics g, int x, int y);

	/**
	 * X and Y are relative to the width and height of the sprite, with (0, 0) being top left.
	 * This means that offset modes are ignored.
	 * @param x The X position to check.
	 * @param y The Y position to check.
	 * @return Whether or not the provided point is in bounds.
	 */
	default boolean isInBounds(int x, int y) {
		if(x < 0 || y < 0) return false;
		if(x > this.getWidth() - 1 || y > this.getHeight() - 1) return false;
		return true;
	}
}
