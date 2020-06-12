package com.hiccup01.JSprite;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class JSpriteText implements JSpriteVisual {

	private String text = "";
	private Color c = new Color(0, 0,0);
	private int xOffset;
	private int yOffset;
	private int width = 0;
	private int height = 0;
	private JSpriteOffsetMode offsetMode = JSpriteOffsetMode.CENTER;
	private Font font = new Font("Helvetica", Font.PLAIN, 12);
	private AffineTransform at = new AffineTransform();
	private FontRenderContext frc = new FontRenderContext(at, true, true);
	private LineMetrics lm;

	public JSpriteText() {
		this("");
	}

	public JSpriteText(String text) {
		this.text = text;
		this.updateOffsets();
	}

	public Color getColour() {
		return this.c;
	}

	public void setColour(Color c) {
		this.c = c;
	}

	public Font getFont() {
		return this.font;
	}

	public void setFont(Font f) {
		this.font = f;
		this.updateOffsets();
	}

	public String getText() {
		return this.text;
	}

	public void setText(String t) {
		this.text = t;
		this.updateOffsets();
	}

	private void updateOffsets() {
		Rectangle2D r = this.font.getStringBounds(this.text, this.frc);
		this.lm = this.font.getLineMetrics(this.text, this.frc);
		this.height = (int)lm.getHeight();
		this.width = (int)r.getWidth();
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
		return this.width;
	}

	@Override
	public int getHeight() {
		return this.height;
	}

	@Override
	public void draw(Graphics g, int x, int y) {
		g.setFont(this.font);
		g.setColor(this.c);
		// We add this ascent (distance between the ascender line and the baseline) here because the x, y coords are the baseline of the leftmost character.
		g.drawString(this.text, x, y + (int)this.lm.getAscent());
	}

	@Override
	public boolean isInBounds(int x, int y) {
		if(x < 0 || y < 0) return false;
		if(x >= this.getWidth() || y >= this.getHeight()) return false;
		return true;
	}
}
