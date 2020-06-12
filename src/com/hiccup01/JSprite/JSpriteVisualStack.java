package com.hiccup01.JSprite;

import java.awt.*;
import java.util.ArrayList;

public class JSpriteVisualStack implements JSpriteVisual {

	private ArrayList<JSpriteVisual> stack = new ArrayList<>();
	private int xOffset;
	private int yOffset;
	private JSpriteOffsetMode offsetMode = JSpriteOffsetMode.CENTER;

	public JSpriteVisualStack() {
		this.updateOffsets();
	}

	public JSpriteVisualStack(ArrayList<JSpriteVisual> stack) {
		this.stack.addAll(stack);
		this.updateOffsets();
	}

	public void pushLayer(JSpriteVisual v) {
		this.stack.add(v);
		this.updateOffsets();
	}

	public JSpriteVisual popLayer() {
		JSpriteVisual top = this.stack.remove(this.stack.size() - 1);
		this.updateOffsets();
		return top;
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
		int maxWidth = 0;
		for(JSpriteVisual v : this.stack) {
			if(v.getWidth() > maxWidth) maxWidth = v.getWidth();
		}
		return maxWidth;
	}

	@Override
	public int getHeight() {
		int maxHeight = 0;
		for(JSpriteVisual v : this.stack) {
			if(v.getHeight() > maxHeight) maxHeight = v.getHeight();
		}
		return maxHeight;
	}

	@Override
	public void draw(Graphics g, int x, int y) {
		for(JSpriteVisual v : this.stack) {
			v.draw(g, x, y);
		}
	}

	@Override
	public boolean isInBounds(int x, int y) {
		if(x < 0 || y < 0) return false;
		if(x >= this.getWidth() || y >= this.getHeight()) return false;
		for(JSpriteVisual v : this.stack) {
			if(v.isInBounds(x, y)) return true;
		}
		return false;
	}
}
