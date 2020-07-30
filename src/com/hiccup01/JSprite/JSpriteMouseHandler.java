package com.hiccup01.JSprite;

public interface JSpriteMouseHandler {
	JSpriteCoordinateType coordinateType = JSpriteCoordinateType.RELATIVE;
	JSpriteMouseEventDelegate scrollEvent(int amount);
	JSpriteMouseEventDelegate mouseClicked(JSpriteMouseEvent m);
	JSpriteMouseEventDelegate mouseEntered(JSpriteMouseEvent m);
	JSpriteMouseEventDelegate mouseExited(JSpriteMouseEvent m);
	JSpriteMouseEventDelegate mousePressed(JSpriteMouseEvent m);
	JSpriteMouseEventDelegate mouseReleased(JSpriteMouseEvent m);
	JSpriteMouseEventDelegate mouseDragged(JSpriteMouseEvent m);
	JSpriteMouseEventDelegate mouseMoved(JSpriteMouseEvent m);
}
