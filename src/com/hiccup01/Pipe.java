package com.hiccup01;

import com.hiccup01.JSprite.JSprite;

public class Pipe {
	public Node source;
	public Node destination;
	public int maxCapacity;
	// Ideally this is calculated by our flow calculation code.
	public int flow;
	public JSprite sprite = null;

	Pipe(Node source, Node destination, int maxCapacity, int flow) {
		this.source = source;
		this.destination = destination;
		this.maxCapacity = maxCapacity;
		this.flow = flow;
	}
}
