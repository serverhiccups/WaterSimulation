package com.hiccup01;

import java.awt.*;

// JSpriteVisual is the next-gen replacement for JSpriteCostume. It allows you to draw anything to the canvas as a sprite, not just an image
public interface JSpriteVisual {
	public JSpriteOffsetMode getOffsetMode();
	public void setOffsetMode(JSpriteOffsetMode m);
	public int getXOffset();
	public int getYOffset();
	public int getWidth();
	public int getHeight();
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
