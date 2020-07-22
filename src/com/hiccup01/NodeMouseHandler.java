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

	private int clamp(int val, int min, int max) {
		return Math.max(min, Math.min(max, val));
	}

	@Override
	public boolean mouseDragged(JSpriteMouseEvent m) {
//		this.self.x = m.getX(JSpriteCoordinateType.VIRTUAL);
//		this.self.y = m.getY(JSpriteCoordinateType.VIRTUAL);
		this.self.x = this.clamp(m.getX(JSpriteCoordinateType.VIRTUAL), 38, 800 - 38);
		this.self.y = this.clamp(m.getY(JSpriteCoordinateType.VIRTUAL), 48 + 38, 640 - 38);
		try {
			// This might be hella slow
			this.networkManager.canvas.sendToFront(this.self.spriteContainer.id);
		} catch (Exception e) {
			System.err.println("Failed to send the currently dragged junction to the front");
		}
		this.networkManager.updateView();
		return true;
	}

	@Override
	public boolean mouseMoved(JSpriteMouseEvent m) {
		return false;
	}
}
