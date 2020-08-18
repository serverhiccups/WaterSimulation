package com.hiccup01.JSprite;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * The visual that represents an image.
 */
public class JSpriteCostume implements JSpriteVisual {
	BufferedImage costume; // The image.
	private int xOffset; // These offsets allow us to have per-costume offsets that allow us to have sprite x and y be the center.
	private int yOffset;
	private JSpriteOffsetMode offsetMode = JSpriteOffsetMode.CENTER; // The offset mode.
	private double rotation = 0; // How much to rotate the costume in radians.
	// If fullHitbox is on, we count anything within the bounding box the the image to be inBounds.
	public boolean fullHitbox = false;

	/**
	 * Creates a JSpriteCostume from a file path of an image.
	 * @param filename The path of the file.
	 * @throws IOException // Thrown if the file cannot be found.
	 */
	public JSpriteCostume(String filename) throws IOException {
		this.costume = ImageIO.read(getClass().getResource(filename)); // Read the image using ImageIO.
		this.updateOffsets();
	}

	/**
	 * Creates a JSpriteCostume from a BufferedImage.
	 * @param image The image.
	 */
	public JSpriteCostume(BufferedImage image) {
		this.costume = image;
		this.updateOffsets();
	}

	/**
	 * Gets the current offset mode.
	 * @return The offset mode.
	 */
	public JSpriteOffsetMode getOffsetMode() {
		return this.offsetMode;
	}

	/**
	 * Sets the current offset mode.
	 * @param offsetMode The offset mode to change to.
	 */
	public void setOffsetMode(JSpriteOffsetMode offsetMode) {
		this.offsetMode = offsetMode;
		this.updateOffsets();
	}

	/**
	 * Gets the current rotation.
	 * @return The current rotation in radians.
	 */
	public double getRotation() {
		return this.rotation;
	}

	/**
	 * Sets the current rotation.
	 * @param rotation The rotation to change to.
	 */
	public void setRotation(double rotation) {
		this.rotation = rotation;
		this.updateOffsets();
	}

	/**
	 * Draw the costume.
	 * @param g The graphics object to draw to.
	 * @param x The X position.
	 * @param y The Y position.
	 */
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

	/**
	 * Is a point in bounds of the sprites. If rotation != 0 then it never is. if fullHitbox is on it only checks the sprite boundaries.
	 * @param x The X position to check.
	 * @param y The Y position to check.
	 * @return
	 */
	@Override
	public boolean isInBounds(int x, int y) {
		// It's too complex and not necessary for me to figure out how to make the code work for rotated images.
		if(this.getRotation() != 0) return false; // Abandon ship
		if(x < 0 || y < 0) return false; // Out of bounds
		if(x > this.getWidth() - 1 || y > this.getHeight() - 1) return false; // Out of bounds.
		if(this.fullHitbox) return true; // fullHitbox means we don't have to care what's actually in the image.
		if(((this.costume.getRGB(x, y) >> 24) & 0xFF) == 0){ // Is the pixel clicked on invisible (alpha 255).
//			System.err.println("because rgb " + this.costume.getRGB(x, y));
			return false; // Then we're not in bounds.
		}
		return true; // Then we must be in bounds.
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
