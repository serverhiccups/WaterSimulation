package com.hiccup01.JSprite;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class JSpriteCostume implements JSpriteVisual {
	BufferedImage costume;
	private int xOffset; // These offsets allow us to have per-costume offsets that allow us to have sprite x and y be the center.
	private int yOffset;
	private JSpriteOffsetMode offsetMode = JSpriteOffsetMode.CENTER;
	private double rotation = 0;
	// If fullHitbox is on, we count anything within the bounding box the the image to be inBounds.
	public boolean fullHitbox = false;
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

	public double getRotation() {
		return this.rotation;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
		this.updateOffsets();
	}

	@Override
	public void draw(Graphics g, int x, int y) {
		Graphics2D g2d = (Graphics2D)g;
//		g.drawImage(this.costume, x, y, null);
		AffineTransform t = new AffineTransform();
		t.translate(x, y);
		t.rotate(this.getRotation(), (this.getWidth() / 2), (this.getHeight() / 2));
//		AffineTransform transform = AffineTransform.getRotateInstance(this.getRotation(), (this.getWidth() / 2), (this.getHeight() / 2));
//		transform.translate(x, y);
		g2d.drawImage(this.costume, t, null);
//		g2d.drawImage(this.costume, AffineTransform.getRotateInstance(this.getRotation(), 0, 0), null);
	}

	@Override
	public boolean isInBounds(int x, int y) {
		// It's too complex and not necessary for me to figure out how to make the code work for rotated images.
		if(this.getRotation() != 0) return false; // Abandon ship
		if(x < 0 || y < 0) return false;
		if(x > this.getWidth() - 1 || y > this.getHeight() - 1) return false;
		if(this.fullHitbox) return true;
		if(((this.costume.getRGB(x, y) >> 24) & 0xFF) == 0){
//			System.err.println("because rgb " + this.costume.getRGB(x, y));
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
