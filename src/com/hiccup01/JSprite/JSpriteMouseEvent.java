package com.hiccup01.JSprite;

import org.jetbrains.annotations.NotNull;

public class JSpriteMouseEvent {
	JSpriteButtonType buttonType;
	private JSpriteCoordinateType defaultCoordinateType;
	// Add methods for getting the position, etc. and the event type.
	int numberOfClicks;
	boolean isCtrl;
	boolean isAlt;
	boolean isShift;
	boolean isPopupTrigger;
	private int pX;
	private int pY;
	private int rX;
	private int rY;
	private int vX;
	private int vY;

	public JSpriteMouseEvent(JSpriteButtonType b, JSpriteCoordinateType c, int n, boolean isCtrl, boolean isAlt, boolean isShift, boolean isPopupTrigger, int pX, int pY, int rX, int rY, int vX, int vY) {
		this.buttonType = b;
		this.defaultCoordinateType = c;
		this.numberOfClicks = n;
		this.isCtrl = isCtrl;
		this.isAlt = isAlt;
		this.isShift = isShift;
		this.isPopupTrigger = isPopupTrigger;
		this.pX = pX;
		this.pY = pY;
		this.rX = rX;
		this.rY = rY;
		this.vX = vX;
		this.vY = vY;
	}

	public int getX(@NotNull JSpriteCoordinateType c) {
		switch (c) {
			case VIRTUAL:
				return this.vX;
			case PHYSICAL:
				return this.pX;
			case RELATIVE:
				return this.rX;
		}
		return this.rX;
	}

	public int getY(@NotNull JSpriteCoordinateType c) {
		switch (c) {
			case VIRTUAL:
				return this.vY;
			case PHYSICAL:
				return this.pY;
			case RELATIVE:
				return this.rY;
		}
		return this.rY;
	}

	public int getX() {
		return this.getX(this.defaultCoordinateType);
	}

	public int getY() {
		return this.getY(this.defaultCoordinateType);
	}
}
