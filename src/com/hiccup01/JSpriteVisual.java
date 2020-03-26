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
}
