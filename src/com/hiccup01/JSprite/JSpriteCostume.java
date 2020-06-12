package com.hiccup01.JSprite;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class JSpriteCostume implements JSpriteVisual {
	BufferedImage costume;
	private int xOffset; // These offsets allow us to have per-costume offsets that allow us to have sprite x and y be the center.
	private int yOffset;
	private JSpriteOffsetMode offsetMode = JSpriteOffsetMode.CENTER;
	public JSpriteCostume(String filename) throws IOException {
		this.costume = ImageIO.read(new File(filename));
		this.updateOffsets();
	}

	public JSpriteCostume(BufferedImage image) {
		this.costume = image;
		this.updateOffsets();
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
		g.drawImage(this.costume, x, y, null);
	}

	@Override
	public boolean isInBounds(int x, int y) {
		if(x < 0 || y < 0) return false;
		if(x > this.getWidth() - 1 || y > this.getHeight() - 1) return false;
		if(((this.costume.getRGB(x, y) >> 24) & 0xFF) == 0){
			System.err.println("because rgb " + this.costume.getRGB(x, y));
			return false;
		}
		return true;
	}

	@Override
	public int getWidth() {
		return this.costume.getWidth(null);
	}

	@Override
	public int getHeight() {
		return this.costume.getHeight(null);
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
