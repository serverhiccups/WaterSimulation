package com.hiccup01;

import com.hiccup01.JSprite.JSpriteContainer;

/**
 * PipePreview is what you see when you are creating a pipe, but haven't actually connected it yet.
 */
public class PipePreview {
	public int startX; // Where does it start? This never changes.
	public int startY;
	public int endX; // Were does it end?
	public int endY;
	public Node startNode; // The node the start of the preview is connected to.
	public JSpriteContainer spriteContainer = null; // The sprite that represents the pipe preview.
	public boolean onCanvas = false;

	public PipePreview(int startX, int startY, int endX, int endY, Node startNode) {
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.startNode = startNode;
	}

	/**
	 * Sets the end of the PipePreview
	 * @param x The X position of the end.
	 * @param y The Y position of the end.
	 */
	public void setEnd(int x, int y) {
		this.endX = x;
		this.endY = y;
	}
}
