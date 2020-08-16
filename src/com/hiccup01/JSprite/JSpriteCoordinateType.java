package com.hiccup01.JSprite;

/**
 * The coordinate type of something.
 */
public enum JSpriteCoordinateType {
	PHYSICAL, // Relative to the top left of the screen.
	VIRTUAL, // Relative to the top left of the canvas (including scrolling).
	RELATIVE // Relative to the top left of the sprite.
}
