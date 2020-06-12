package com.hiccup01.MaxFlow;

public class Junction {
	int src;
	int dest;
	int maxFlow;

	public Junction(int src, int dest, int maxFlow) {
		this.src = src;
		this.dest = dest;
		this.maxFlow = maxFlow;
	}
}
