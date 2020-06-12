package com.hiccup01.MaxFlow;

import java.util.ArrayList;

public interface FlowAlgorithm {
	void connect(int src, int dest, int maxFlow);
	void disconnect(int src, int dest);
	void setSources(ArrayList<Source> sources);
	void setSinks(ArrayList<Sink> sinks);
	void calculate() throws MaxFlowException;
}
