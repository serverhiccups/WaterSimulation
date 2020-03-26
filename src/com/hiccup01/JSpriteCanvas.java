package com.hiccup01;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class JSpriteCanvas extends Canvas {
	private ArrayList<JSpriteContainer> spriteStack = new ArrayList<>();

	public JSpriteCanvas() {

	}

	private JSpriteContainer findSpriteById(int id) {
		for(JSpriteContainer j : this.spriteStack) {
			if(j.id == id) return j;
		}
		return null;
	}

	public int addSprite(JSprite sprite, int id) throws JSpriteException {
		if(this.findSpriteById(id) != null) throw new JSpriteException("A sprite with the id " + id + " already exists");
		spriteStack.add(new JSpriteContainer(id, sprite));
		return id;
	}

	public JSprite removeSprite(int id) throws JSpriteException {
		JSpriteContainer sprite = this.findSpriteById(id);
		if(sprite == null) throw new JSpriteException("Can't remove non-existant sprite with id " + id);
		this.spriteStack.remove(sprite);
		return sprite.sprite;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		for(JSpriteContainer c : this.spriteStack) {
			JSprite s = c.sprite;
			if(!s.visible) continue;
			JSpriteCostume costume = s.costumes.get(s.currentCostume);
			g.drawImage(costume.costume, s.xPosition - costume.xOffset, s.yPosition - costume.yOffset, null);
		}
		System.err.println("The canvas was painted.");
	}
}
