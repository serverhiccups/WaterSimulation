package com.hiccup01;

import java.util.HashMap;
import java.util.Map;

public class JSprite {
	public Boolean visible = true;
	public int xPosition;
	public int yPosition;
	public HashMap<Integer, JSpriteCostume> costumes = new HashMap<>();
	int currentCostume;
	public JSprite(int x, int y, JSpriteCostume firstCostume) {
		costumes.clear();
		costumes.put(0, firstCostume);
		currentCostume = 0;
		this.xPosition = x;
		this.yPosition = y;
	}

	public void setCostume(int id) {
		this.currentCostume = id;
	}
}
