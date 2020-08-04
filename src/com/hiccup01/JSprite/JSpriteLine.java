package com.hiccup01.JSprite;

import java.awt.*;
import java.awt.geom.Line2D;

public class JSpriteLine implements JSpriteVisual {
	private Color c = Color.black;
	private float thickness = 1;
	// rotation is in radians.
	private double rotation = 0;
	private float length = 1;
	private int xOffset;
	private int yOffset;
	// x and y displacements are the distance from the "primary" corner and the other corner;
	private int xDisplacement;
	private int yDisplacement;
	private JSpriteOffsetMode offsetMode = JSpriteOffsetMode.CENTER;

	public JSpriteLine(double rotation, float length, float thickness) {
		this.rotation = rotation;
		this.length = length;
		this.thickness = thickness;
		this.updateOffsets();
	}

	/**
	 * Aligns the line along two points.
	 * WARNING: DO NOT USE THIS METHOD FOR OFFSET MODES OTHER THAN JSpriteOffsetMode.CENTER.
	 * @return The x and y to move the sprite to in an int[].
	 */
	public int[] alignToPoints(int startX, int startY, int endX, int endY) {
		this.length = (float) Math.hypot(Math.abs(startX - endX), Math.abs(startY - endY));
		this.rotation = -1 * Math.atan2(endY - startY, endX - startX);
		this.updateOffsets();
		return new int[]{(startX + endX) / 2, (startY + endY) / 2};
	}

	public double getRotation() {
		return this.rotation;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
		this.updateOffsets();
	}

	public float getLength() {
		return this.length;
	}

	public void setLength(float length) {
		this.length = length;
		this.updateOffsets();
	}

	public float getThickness() {
		return this.thickness;
	}

	public void setThickness(float t) {
		this.thickness = t;
	}

	public Color getColour() {
		return this.c;
	}

	public void setColour(Color c) {
		this.c = c;
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
		this.xDisplacement = (int) (this.length * Math.sin(this.rotation));
		this.yDisplacement = (int) (this.length * Math.cos(this.rotation));
	}

	@Override
	public JSpriteOffsetMode getOffsetMode() {
		return this.offsetMode;
	}

	@Override
	public void setOffsetMode(JSpriteOffsetMode m) {
		this.offsetMode = m;
		this.updateOffsets();
	}

	@Override
	public int getXOffset() {
		return this.xOffset;
	}

	@Override
	public int getYOffset() {
		return this.yOffset;
	}

	@Override
	public int getWidth() {
		return (int) Math.abs(this.length * Math.cos(this.rotation));
	}

	@Override
	public int getHeight() {
		return (int) Math.abs(this.length * Math.sin(this.rotation));
	}

	@Override
	public void draw(Graphics g, int x, int y) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(c);
		g2d.setStroke(new BasicStroke(this.thickness));
		if((this.xDisplacement <= 0 && this.yDisplacement > 0) || (this.xDisplacement > 0 && this.yDisplacement <= 0)) { // If in the second or forth quadrant.
			g2d.drawLine(x, y, x + this.getWidth(), y + this.getHeight());
		} else { // If in the first or third quadrant
			g2d.drawLine(x, y + this.getHeight(), x + this.getWidth(), y);
		}
		if(this.c == Color.RED) {
			System.out.println("Drew the line");
			System.out.println("x: " + x + " y: " + y);
		}
	}

	@Override
	public boolean isInBounds(int x, int y) {
//		if(this.c == Color.PINK) System.out.println("bounds check x: " + x + " y: " + y);
		if(x < 0 || y < 0) return false;
		if(x >= this.getWidth() || y >= this.getHeight()) return false;
		if((this.xDisplacement <= 0 && this.yDisplacement > 0) || (this.xDisplacement > 0 && this.yDisplacement <= 0)) { // If in the second or forth quadrant.
			// Equation is y = mx
			// therefore x = y/m
			// angle = tan^-1(this.height() / this.width())
			double angle = Math.atan((float)this.getHeight() / (float)this.getWidth());
			double opAngle = 90 - angle;
			double xDist = Math.abs(x - ((float)y / ((float)this.getHeight() / (float)this.getWidth())));
			double A = (xDist * Math.sin(opAngle) / Math.sin(180 - 45 - opAngle));
//			if(this.c == Color.PINK) System.err.println("A is " + A);
			if(A > this.thickness / 2) return false;
			return true;
		} else { // If in the first or third quadrant
			// Equation is y = mx + c where c is the visual height.
			// therefore x = (y - c)/m
			// angle = tan^-1(this.height() / this.width())
			double angle = Math.atan((float)this.getHeight() / (float)this.getWidth());
			double opAngle = 90 - angle;
			double xDist = Math.abs(x - ((float)(y - this.getHeight()) / ( -1 * (float)this.getHeight() / (float)this.getWidth())));
			double A = (xDist * Math.sin(opAngle) / Math.sin(180 - 45 - opAngle));
//			if(this.c == Color.PINK) System.err.println("A is " + A);
			if(A > this.thickness / 2) return false;
			return true;
		}
	}
}
