package com.hiccup01.MaxFlow;

public class Edge {
	public int src;
	public int dest;
	public int maxFlow;
	public int flow;
	public boolean isReverse;
	public Edge rev;

	public Edge(int src, int dest, int maxFlow, int flow, boolean isReverse, Edge rev) {
		this.src = src;
		this.dest = dest;
		this.maxFlow = maxFlow;
		this.flow = flow;
		this.isReverse = isReverse;
		this.rev = rev;
	}
}
