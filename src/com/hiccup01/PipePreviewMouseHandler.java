package com.hiccup01;

import com.hiccup01.JSprite.*;

public class PipePreviewMouseHandler implements JSpriteMouseHandler {
	PipePreview self = null;
	NetworkManager networkManager = null;

	public PipePreviewMouseHandler(PipePreview self, NetworkManager networkManager) {
		this.self = self;
		this.networkManager = networkManager;

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
		return null;
	}

	@Override
	public JSpriteMouseEventDelegate mouseDragged(JSpriteMouseEvent m) {
		System.err.println("Preview got a drag event");
		this.self.setEnd(m.getX(JSpriteCoordinateType.VIRTUAL), m.getY(JSpriteCoordinateType.VIRTUAL));
		this.networkManager.updateView();
		return JSpriteMouseEventDelegate.COMPLETED;
	}

	@Override
	public JSpriteMouseEventDelegate mouseMoved(JSpriteMouseEvent m) {
		return null;
	}
}
