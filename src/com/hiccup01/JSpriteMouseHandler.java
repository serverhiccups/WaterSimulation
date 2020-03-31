package com.hiccup01;

import java.awt.event.MouseEvent;

public interface JSpriteMouseHandler {
	JSpriteCoordinateType coordianateType = JSpriteCoordinateType.RELATIVE;
	boolean scrollEvent(int amount);
	boolean mouseClicked(MouseEvent m);
	boolean mouseEntered(MouseEvent m);
	boolean mouseExited(MouseEvent m);
	boolean mousePressed(MouseEvent m);
	boolean mouseReleased(MouseEvent m);
	boolean mouseDragged(MouseEvent m);
	boolean mouseMoved(MouseEvent m);
}
