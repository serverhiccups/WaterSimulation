package com.hiccup01;

public class JSpriteMouseHandlerContainer {
	JSpriteCoordinateType coordinateType;
	JSpriteMouseHandler mouseHandler;

	public JSpriteMouseHandlerContainer(JSpriteCoordinateType c, JSpriteMouseHandler m) {
		this.coordinateType = c;
		this.mouseHandler = m;
	}
}
