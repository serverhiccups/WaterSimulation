package com.hiccup01;

import com.hiccup01.JSprite.*;

/**
 * PipePreviewMouseHandler is the logic for the pipe preview.
 */
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
	public JSpriteMouseEventDelegate mousePressed(JSpriteMouseEvent m) { // This shouldn't ever be called. It's useful if something breaks thoug.
		this.self.setEnd(m.getX(JSpriteCoordinateType.VIRTUAL), m.getY(JSpriteCoordinateType.VIRTUAL));
		this.networkManager.updateView();
		return JSpriteMouseEventDelegate.COMPLETED;
	}

	@Override
	public JSpriteMouseEventDelegate mouseReleased(JSpriteMouseEvent m) { // We need to 'land' the preview and turn it into a real pipe.
		System.err.println("Attempting to land the preview");
        this.networkManager.landPreview(this.self.endX, this.self.endY);
		this.networkManager.updateView();
		return JSpriteMouseEventDelegate.COMPLETED;
	}

	@Override
	public JSpriteMouseEventDelegate mouseDragged(JSpriteMouseEvent m) { // We need to move the preview to be under the mouse.
		System.err.println("Preview got a drag event");
		int x = this.clamp(m.getX(JSpriteCoordinateType.VIRTUAL), 0, 800); 	// | Make the preview within the bounds of the window.
		int y = this.clamp(m.getY(JSpriteCoordinateType.VIRTUAL), 48, 640);	// |
		this.self.setEnd(x, y); // Set the end of the preview.
		this.networkManager.updateView(); // Update the window.
		return JSpriteMouseEventDelegate.COMPLETED;
	}

	@Override
	public JSpriteMouseEventDelegate mouseMoved(JSpriteMouseEvent m) {
		return null;
	}
}
