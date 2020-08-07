package com.hiccup01.MaxFlow;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class EdmondsKarp implements FlowAlgorithm {
	ArrayList<Junction> mutableNetwork;
	ArrayList<Source> sources;
	ArrayList<Sink> sinks;
	List<List<Edge>> builtNetwork; // builtNetwork is our adjacency list.
	private int sourceId;
	private int sinkId;
	private int maxNode;

	public EdmondsKarp() {
		this.reset();
	}

	public void reset() {
		this.mutableNetwork = new ArrayList<>();
		this.sources = new ArrayList<>();
		this.sinks = new ArrayList<>();
		this.builtNetwork = new ArrayList<>();
		this.maxNode = 0;
	}

	public void connect(int src, int dest, int maxFlow) {
		this.mutableNetwork.add(new Junction(src, dest, maxFlow));
	}

	public void disconnect(int src, int dest) {
		for(int i = 0; i < this.mutableNetwork.size(); i++) {
			Junction j = this.mutableNetwork.get(i);
			if(j.src == src && j.dest == dest) this.mutableNetwork.remove(i);
			i--;
		}
	}

	public void setSources(ArrayList<Source> sources) {
		if(sources == null) {
			this.sources = new ArrayList<>();
		} else this.sources = sources;

	}

	public void setSinks(ArrayList<Sink> sinks) {
		if(sinks == null) {
			this.sinks = new ArrayList<>();
		} else this.sinks = sinks;
	}

	private void addJunction(Junction j) { // Helper to make dealing with reverse edges less painful.
		this.builtNetwork.get(j.src).add(new Edge(j.src, j.dest, j.maxFlow, 0, false, null));
		this.builtNetwork.get(j.dest).add(new Edge(j.dest, j.src, j.maxFlow, 0, true, this.builtNetwork.get(j.src).get(this.builtNetwork.get(j.src).size() - 1)));
		this.builtNetwork.get(j.src).get(this.builtNetwork.get(j.src).size() - 1).rev = this.builtNetwork.get(j.dest).get(this.builtNetwork.get(j.dest).size() - 1);
	}

	private void buildNetwork() throws MaxFlowException {
		this.maxNode = 0;
		for (Junction j : this.mutableNetwork) { // We have to use a for loop here because Java doesn't allow lambda expressions to capture outside variables.
			if (j.src > maxNode) maxNode = j.src;
			if (j.dest > maxNode) maxNode = j.dest;
		}
		System.err.println("maxNode is " + maxNode);
		this.builtNetwork = new ArrayList<>();
		for(int i = 0; i < maxNode + 3; i++) this.builtNetwork.add(new ArrayList<>()); // One for 0 indexing plus one each for the source and sink
		System.err.println("builtNetwork size is " + this.builtNetwork.size());
		for(Junction j : this.mutableNetwork) {
			this.addJunction(j);
		}
		final int mx = maxNode; // We have to define a *final* variable here because you cannot use non final variables in a lambda expression.
		this.sources.removeIf(source -> source.id > mx);
		this.sinks.removeIf(sink -> sink.id > mx);
		if(sources.size() == 0) throw new MaxFlowException("There are no sources");
		if(sinks.size() == 0) throw new MaxFlowException("There are no sinks");
		this.sourceId = maxNode + 1;
		this.sinkId = maxNode + 2;

		for(Source s : this.sources) {
			this.addJunction(new Junction(this.sourceId, s.id, s.flow));
		}
		for(Sink s: this.sinks) {
			this.addJunction(new Junction(s.id, this.sinkId, s.flow));
		}
	}

	public List<List<Edge>> calculate() throws MaxFlowException {
		this.buildNetwork();
		int flow = 0;
		while(true) {
			Queue<Integer> queue = new LinkedList<>();
			ArrayList<Edge> parents = new ArrayList<>();
			for(int i = 0; i < this.builtNetwork.size(); i++) parents.add(null);
			queue.add(this.sourceId);
			int current;
			while(queue.size() > 0) {
				current = queue.poll();
				for(Edge v : this.builtNetwork.get(current)) {
					if(parents.get(v.dest) == null && v.dest != this.sourceId && v.maxFlow > v.flow) {
						parents.set(v.dest, v);
						queue.add(v.dest);
					}
				}
			}

			if(parents.get(this.sinkId) == null) break; // No augmenting path found.

			int df = Integer.MAX_VALUE;
			for(Edge v = parents.get(this.sinkId); v != null; v = parents.get(v.src)) { // Finds maximum flow through given path.
				df = Math.min(df, v.maxFlow - v.flow);
			}

			for(Edge v = parents.get(this.sinkId); v != null; v = parents.get(v.src)) {
				v.flow += df;
				v.rev.flow += df;
			}

			flow += df;
		}
		for(List<Edge> v : this.builtNetwork) {
			v.removeIf(edge -> edge.isReverse);
			v.removeIf(edge -> edge.src > this.maxNode);
			v.removeIf(edge -> edge.dest > this.maxNode);
		}
		System.out.println(flow);
		return this.builtNetwork;
	}
}
