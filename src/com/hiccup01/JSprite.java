package com.hiccup01;

import java.util.HashMap;

public class JSprite {
	public Boolean visible = true;
	public int xPosition;
	public int yPosition;
	private HashMap<Integer, JSpriteVisual> visuals = new HashMap<>();
	int currentVisual;
	public JSprite(int x, int y, JSpriteCostume firstVisual) {
		visuals.put(0, firstVisual);
		this.currentVisual = 0;
		this.xPosition = x;
		this.yPosition = y;
	}

	public void setCurrentVisual(int id) {
		if(visuals.containsKey(id)) this.currentVisual = id;
	}

	public int getCurrentVisual() {
		return this.currentVisual;
	}

	public JSpriteVisual getVisual(int id) {
		return visuals.get(id);
	}

	public void setVisual(int id, JSpriteVisual v) {
		this.visuals.put(id, v);
	}
}
