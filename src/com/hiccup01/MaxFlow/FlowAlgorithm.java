package com.hiccup01.MaxFlow;

import java.util.ArrayList;
import java.util.List;

/**
 * An interface to make it simple to try different flow algorithms.
 */
public interface FlowAlgorithm {
	void connect(int src, int dest, int maxFlow);
	void disconnect(int src, int dest);
	void reset();
	void setSources(ArrayList<Source> sources);
	void setSinks(ArrayList<Sink> sinks);
	List<List<Edge>> calculate() throws MaxFlowException;
}
