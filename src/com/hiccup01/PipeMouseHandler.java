package com.hiccup01;

import com.hiccup01.JSprite.*;

public class PipeMouseHandler implements JSpriteMouseHandler {
	Pipe self = null;
	NetworkManager networkManager = null;

	public PipeMouseHandler(Pipe self, NetworkManager networkManager) {
		this.self = self;
		this.networkManager = networkManager;
	}

	private boolean onText(JSpriteMouseEvent m) {
		int x = m.getX(JSpriteCoordinateType.RELATIVE);
		int y = m.getY(JSpriteCoordinateType.RELATIVE);
		System.out.println("x is " + x + " and y is " + y);
		JSpriteVisualStack stack = ((JSpriteVisualStack)this.self.spriteContainer.sprite.getVisual(this.self.spriteContainer.sprite.getCurrentVisual()));
		int width = stack.getWidth();
		int height = stack.getHeight();
		// The box is 54x24 pixels.
		int boxX = (width / 2) - (54 / 2);
		int boxY = (height / 2) - (24 / 2);
		if((x >= boxX && x <= boxX + 54) && (y >= boxY && y <= boxY + 24)) {
			return true;
		}
		return false;
	}

	@Override
	public JSpriteMouseEventDelegate scrollEvent(int amount) {
		return null;
	}

	@Override
	public JSpriteMouseEventDelegate mouseClicked(JSpriteMouseEvent m) {
		if(this.onText(m)) {
			System.out.println("Got a click on the text");
			return JSpriteMouseEventDelegate.COMPLETED;
		}
		return JSpriteMouseEventDelegate.CONTINUE;
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
		return null;
	}

	@Override
	public JSpriteMouseEventDelegate mouseReleased(JSpriteMouseEvent m) {
		return null;
	}

	@Override
	public JSpriteMouseEventDelegate mouseDragged(JSpriteMouseEvent m) {
		return null;
	}

	@Override
	public JSpriteMouseEventDelegate mouseMoved(JSpriteMouseEvent m) {
		return null;
	}
}
