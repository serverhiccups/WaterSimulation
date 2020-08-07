package com.hiccup01;

import com.hiccup01.JSprite.JSpriteMouseEvent;
import com.hiccup01.JSprite.JSpriteMouseEventDelegate;
import com.hiccup01.JSprite.JSpriteMouseHandler;

public class AddNodeButtonMouseHandler implements JSpriteMouseHandler {
	NetworkManager networkManager = null;
	NodeType type = null;

	AddNodeButtonMouseHandler(NetworkManager networkManager, NodeType type) {
		this.networkManager = networkManager;
		this.type = type;
	}

	public JSpriteMouseEventDelegate scrollEvent(int amount) {
		return null;
	}

	@Override
	public JSpriteMouseEventDelegate mouseClicked(JSpriteMouseEvent m) {
		this.networkManager.addNode(this.type);
		this.networkManager.updateView();
		return new JSpriteMouseEventDelegate(true);
	}

	public JSpriteMouseEventDelegate mouseEntered(JSpriteMouseEvent m) {
		return null;
	}

	public JSpriteMouseEventDelegate mouseExited(JSpriteMouseEvent m) {
		return null;
	}

	public JSpriteMouseEventDelegate mousePressed(JSpriteMouseEvent m) {
		return null;
	}

	public JSpriteMouseEventDelegate mouseReleased(JSpriteMouseEvent m) {
		return null;
	}

	public JSpriteMouseEventDelegate mouseDragged(JSpriteMouseEvent m) {
		return null;
	}

	public JSpriteMouseEventDelegate mouseMoved(JSpriteMouseEvent m) {
		return null;
	}
}
