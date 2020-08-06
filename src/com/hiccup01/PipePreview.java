package com.hiccup01;

import com.hiccup01.JSprite.JSpriteContainer;

public class PipePreview {
	public int startX;
	public int startY;
	public int endX;
	public int endY;
	public Node startNode;
	public JSpriteContainer spriteContainer = null;
	public boolean onCanvas = false;

	public PipePreview(int startX, int startY, int endX, int endY, Node startNode) {
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.startNode = startNode;
	}

	public void setEnd(int x, int y) {
		this.endX = x;
		this.endY = y;
	}
}