package com.hiccup01;

import com.hiccup01.JSprite.*;

public class PipePreviewMouseHandler implements JSpriteMouseHandler {
	PipePreview self = null;
	NetworkManager networkManager = null;

	public PipePreviewMouseHandler(PipePreview self, NetworkManager networkManager) {
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
		System.out.println("We got the mouse press!");
//		this.self.setEnd(50, 50);
		this.self.setEnd(m.getX(JSpriteCoordinateType.VIRTUAL), m.getY(JSpriteCoordinateType.VIRTUAL));
		this.networkManager.updateView();
		return JSpriteMouseEventDelegate.COMPLETED;
	}

	@Override
	public JSpriteMouseEventDelegate mouseReleased(JSpriteMouseEvent m) {
		System.err.println("Attempting to land the preview");
//		this.networkManager.landPreview(m.getX(JSpriteCoordinateType.VIRTUAL), m.getY(JSpriteCoordinateType.VIRTUAL));
        this.networkManager.landPreview(this.self.endX, this.self.endY);
		this.networkManager.updateView();
		return JSpriteMouseEventDelegate.COMPLETED;
	}

	@Override
	public JSpriteMouseEventDelegate mouseDragged(JSpriteMouseEvent m) {
		System.err.println("Preview got a drag event");
		int x = this.clamp(m.getX(JSpriteCoordinateType.VIRTUAL), 0, 800);
		int y = this.clamp(m.getY(JSpriteCoordinateType.VIRTUAL), 48, 640);
		this.self.setEnd(x, y);
//		this.self.setEnd(m.getX(JSpriteCoordinateType.VIRTUAL), m.getY(JSpriteCoordinateType.VIRTUAL));
		this.networkManager.updateView();
		return JSpriteMouseEventDelegate.COMPLETED;
	}

	@Override
	public JSpriteMouseEventDelegate mouseMoved(JSpriteMouseEvent m) {
		return null;
	}
}
