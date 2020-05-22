package com.hiccup01;

import java.awt.event.MouseEvent;

public interface JSpriteMouseHandler {
	JSpriteCoordinateType coordinateType = JSpriteCoordinateType.RELATIVE;
	boolean scrollEvent(int amount);
	boolean mouseClicked(JSpriteMouseEvent m);
	boolean mouseEntered(JSpriteMouseEvent m);
	boolean mouseExited(JSpriteMouseEvent m);
	boolean mousePressed(JSpriteMouseEvent m);
	boolean mouseReleased(JSpriteMouseEvent m);
	boolean mouseDragged(JSpriteMouseEvent m);
	boolean mouseMoved(JSpriteMouseEvent m);
}
