package com.hiccup01;

import com.hiccup01.JSprite.JSpriteMouseEvent;
import com.hiccup01.JSprite.JSpriteMouseHandler;

public class AddNodeButtonMouseHandler implements JSpriteMouseHandler {
	NetworkManager networkManager = null;

	AddNodeButtonMouseHandler(NetworkManager networkManager) {
		this.networkManager = networkManager;
	}

	@Override
	public boolean scrollEvent(int amount) {
		return false;
	}

	@Override
	public boolean mouseClicked(JSpriteMouseEvent m) {
		this.networkManager.addNode();
		this.networkManager.updateView();
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
		return false;
	}

	@Override
	public boolean mouseReleased(JSpriteMouseEvent m) {
		return false;
	}

	@Override
	public boolean mouseDragged(JSpriteMouseEvent m) {
		return false;
	}

	@Override
	public boolean mouseMoved(JSpriteMouseEvent m) {
		return false;
	}
}
