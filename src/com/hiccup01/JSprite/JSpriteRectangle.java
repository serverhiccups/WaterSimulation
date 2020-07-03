package com.hiccup01.JSprite;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class JSpriteRectangle implements JSpriteVisual {
	private int xOffset; // These offsets allow us to have per-costume offsets that allow us to have sprite x and y be the center.
	private int yOffset;
	private int xSize;
	private int ySize;
	private Color c = Color.white;
	private JSpriteOffsetMode offsetMode = JSpriteOffsetMode.TOP_RIGHT;
	public JSpriteRectangle(int xSize, int ySize) {
		this.xSize = xSize;
		this.ySize = ySize;
		this.updateOffsets();
	}
	public JSpriteRectangle(int xSize, int ySize, Color c) {
		this(xSize, ySize);
		this.c = c;
	}

	public Color getColour() {
		return c;
	}

	public void setColour(Color c) {
		this.c = c;
	}

	public JSpriteOffsetMode getOffsetMode() {
		return this.offsetMode;
	}

	public void setOffsetMode(JSpriteOffsetMode offsetMode) {
		this.offsetMode = offsetMode;
		this.updateOffsets();
	}

	@Override
	public void draw(Graphics g, int x, int y) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(this.c);
		g2d.fillRect(x, y, this.xSize, this.ySize);
	}

	@Override
	public boolean isInBounds(int x, int y) {
		if(x < 0 || y < 0) return false;
		if(x > this.getWidth() - 1 || y > this.getHeight() - 1) return false;
		return true;
	}

	@Override
	public int getWidth() {
		return this.xSize;
	}

	@Override
	public int getHeight() {
		return this.ySize;
	}

	private void updateOffsets() {
		int height = this.getHeight();
		int width = this.getWidth();
		switch (this.getOffsetMode()) {
			case CENTER:
				this.xOffset = width / 2;
				this.yOffset = height / 2;
				break;
			case TOP_RIGHT:
				this.xOffset = 0;
				this.yOffset = 0;
				break;
			case BOTTOM_LEFT:
				this.xOffset = width;
				this.yOffset = height;
				break;
		}
	}

	@Override
	public int getXOffset() {
		return this.xOffset;
	}

	@Override
	public int getYOffset() {
		return this.yOffset;
	}
}
