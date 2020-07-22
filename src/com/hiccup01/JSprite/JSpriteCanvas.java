package com.hiccup01.JSprite;

import java.awt.*;
import java.util.ArrayList;

public class JSpriteCanvas extends Canvas {
	public ArrayList<JSpriteContainer> spriteStack = new ArrayList<>();
	public JSpriteMouseEventHandler eventHandler;
	public JSpriteMouseHandler defaultHandler;
	public boolean debugMode = false;
	public int virtualX = 0;
	public int virtualY = 0;

	public JSpriteCanvas() {
		this.eventHandler = new JSpriteMouseEventHandler(this);
		this.addMouseListener(this.eventHandler);
		this.addMouseMotionListener(this.eventHandler);
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
		if(sprite == null) throw new JSpriteException("Can't remove non-existent sprite with id " + id);
		this.spriteStack.remove(sprite);
		return sprite.sprite;
	}

	public void sendToBack(int id) throws JSpriteException {
		JSpriteContainer sprite = this.findSpriteById(id);
		if(sprite == null) throw new JSpriteException("Can't move non-existent sprite with id " + id);
		this.spriteStack.remove(sprite);
		this.spriteStack.add(0, sprite);
	}

	public void sendToFront(int id) throws JSpriteException {
		JSpriteContainer sprite = this.findSpriteById(id);
		if(sprite == null) throw new JSpriteException("Can't move non-existent sprite with id " + id);
		this.spriteStack.remove(sprite);
		this.spriteStack.add(this.spriteStack.size(), sprite);
	}

	public void scrollX(int amount) {
		this.virtualX += amount;
	}

	public void scrollY(int amount) {
		this.virtualY += amount;
	}

	public void scroll(int x, int y) {
		this.scrollX(x);
		this.scrollY(y);
		this.repaint();
	}

	public void setDefaultMouseHandler(JSpriteMouseHandler h) {
		this.defaultHandler = h;
	}

	@Override
	public void paint(Graphics g) {
		if (this.debugMode) System.err.println("------------------------------");
		for(JSpriteContainer c : this.spriteStack) {
			JSprite s = c.sprite;
			if(!s.visible) continue;
			if (this.debugMode) System.err.println("Currently painting sprite: " + s + ", id: " + c.id);
			JSpriteVisual visual = s.getVisual(s.getCurrentVisual());
			switch (s.getCoordinateType()) {
				case VIRTUAL:
					visual.draw(g, s.xPosition - visual.getXOffset() - this.virtualX, s.yPosition - visual.getYOffset() - this.virtualY);
					if(this.debugMode) {
						g.setColor(Color.red);
						g.drawRect(s.xPosition - this.virtualX, s.yPosition - this.virtualY, 1, 1);
						g.setColor(Color.green);
						g.drawRect(s.xPosition - visual.getXOffset() - this.virtualX, s.yPosition - visual.getYOffset() - this.virtualY, 1, 1);
					}
				case PHYSICAL:
					visual.draw(g, s.xPosition - visual.getXOffset(), s.yPosition - visual.getYOffset());
					if(this.debugMode) {
						g.setColor(Color.red);
						g.drawRect(s.xPosition, s.yPosition, 1, 1);
						g.setColor(Color.green);
						g.drawRect(s.xPosition - visual.getXOffset(), s.yPosition - visual.getYOffset(), 1, 1);
					}
			}
		}
		// Delete ephemeral sprites.
		this.spriteStack.removeIf(c -> c.sprite.ephemeral);
	}
}
