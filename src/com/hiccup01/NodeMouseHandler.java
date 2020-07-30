package com.hiccup01;

import com.hiccup01.JSprite.*;

public class NodeMouseHandler implements JSpriteMouseHandler {
	Node self = null;
	NetworkManager networkManager = null;

	public NodeMouseHandler(Node self, NetworkManager networkManager) {
		this.self = self;
		this.networkManager = networkManager;
	}

	private int clamp(int val, int min, int max) {
		return Math.max(min, Math.min(max, val));
	}

	@Override
	public JSpriteMouseEventDelegate scrollEvent(int amount) {
		return null;
	}

	@Override
	public JSpriteMouseEventDelegate mouseClicked(JSpriteMouseEvent m) {
		return null;
	}

	@Override
	public JSpriteMouseEventDelegate mouseEntered(JSpriteMouseEvent m) {
		return null;
	}

	@Override
	public JSpriteMouseEventDelegate mouseExited(JSpriteMouseEvent m) {
		return null;
	}

	@Override
	public JSpriteMouseEventDelegate mousePressed(JSpriteMouseEvent m) {
		System.err.println("Node got a mouse press");
		if(m.buttonType == JSpriteButtonType.SECONDARY) {
			System.err.println("Creating a PipePreview");
			return new JSpriteMouseEventDelegate(this.networkManager.getPipePreview(m.getX(JSpriteCoordinateType.VIRTUAL), m.getY(JSpriteCoordinateType.VIRTUAL)));
		}
		return null;
	}

	@Override
	public JSpriteMouseEventDelegate mouseReleased(JSpriteMouseEvent m) {
		return null;
	}

	@Override
	public JSpriteMouseEventDelegate mouseDragged(JSpriteMouseEvent m) {
//		this.self.x = m.getX(JSpriteCoordinateType.VIRTUAL);
//		this.self.y = m.getY(JSpriteCoordinateType.VIRTUAL);
		if(m.buttonType == JSpriteButtonType.PRIMARY) {
			this.self.x = this.clamp(m.getX(JSpriteCoordinateType.VIRTUAL), 38, 800 - 38);
			this.self.y = this.clamp(m.getY(JSpriteCoordinateType.VIRTUAL), 48 + 38, 640 - 38);
			try {
				// This might be hella slow
				this.networkManager.canvas.sendToFront(this.self.spriteContainer.id);
			} catch (Exception e) {
				System.err.println("Failed to send the currently dragged junction to the front");
			}
			this.networkManager.updateView();
			return JSpriteMouseEventDelegate.COMPLETED;
		}
		return JSpriteMouseEventDelegate.COMPLETED;
	}

	@Override
	public JSpriteMouseEventDelegate mouseMoved(JSpriteMouseEvent m) {
		return null;
	}
}
