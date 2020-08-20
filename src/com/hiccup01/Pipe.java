package com.hiccup01;

import com.hiccup01.JSprite.JSpriteContainer;

import java.io.Serializable;

/**
 * Pipe represents a connection between two nodes.
 */
public class Pipe implements Serializable {
	public Node source;
	public Node destination;
	public int maxCapacity; // How much is the maximum amount of water that can flow through this pipe.
	public int flow; // Ideally this is calculated by our flow calculation code.
	public transient JSpriteContainer spriteContainer = null; // transient means that this field won't be serialised.

	Pipe(Node source, Node destination, int maxCapacity, int flow) {
		this.source = source;
		this.destination = destination;
		this.maxCapacity = maxCapacity;
		this.flow = flow;
	}
}
