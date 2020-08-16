package com.hiccup01.JSprite;

/**
 * Holds a JSprite and it's id together.
 */
public class JSpriteContainer {
	public int id;
	public JSprite sprite;
	public JSpriteContainer(int id, JSprite sprite) {
		this.id = id;
		this.sprite = sprite;
	}
}
