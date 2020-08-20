package com.hiccup01;

import com.hiccup01.JSprite.JSpriteContainer;

import java.io.Serializable;

public class Pipe implements Serializable {
	public Node source;
	public Node destination;
	public int maxCapacity;
	// Ideally this is calculated by our flow calculation code.
	public int flow;
	public transient JSpriteContainer spriteContainer = null; // transient mean that this field won't be serialised.

	Pipe(Node source, Node destination, int maxCapacity, int flow) {
		this.source = source;
		this.destination = destination;
		this.maxCapacity = maxCapacity;
		this.flow = flow;
	}
}
