package com.hiccup01;

import com.hiccup01.JSprite.JSpriteCoordinateType;
import com.hiccup01.JSprite.JSpriteMouseEvent;
import com.hiccup01.JSprite.JSpriteMouseHandler;

public class NodeMouseHandler implements JSpriteMouseHandler {
	Node self = null;
	NetworkManager networkManager = null;

	public NodeMouseHandler(Node self, NetworkManager networkManager) {
		this.self = self;
		this.networkManager = networkManager;
	}

	@Override
	public boolean scrollEvent(int amount) {
		return false;
	}

	@Override
	public boolean mouseClicked(JSpriteMouseEvent m) {
		System.err.println("Clicked on");
		return true;
	}

	@Override
	public boolean mouseEntered(JSpriteMouseEvent m) {
		return false;
	}

	@Override
	public boolean mouseExited(JSpriteMouseEvent m) {
		return false;
	}

	@Override
	public boolean mousePressed(JSpriteMouseEvent m) {
		System.err.println("The mouse was pressed");
		return true;
	}

	@Override
	public boolean mouseReleased(JSpriteMouseEvent m) {
		return false;
	}

	@Override
	public boolean mouseDragged(JSpriteMouseEvent m) {
		this.self.x = m.getX(JSpriteCoordinateType.VIRTUAL);
		this.self.y = m.getY(JSpriteCoordinateType.VIRTUAL);
		this.networkManager.updateView();
		return true;
	}

	@Override
	public boolean mouseMoved(JSpriteMouseEvent m) {
		return false;
	}
}
