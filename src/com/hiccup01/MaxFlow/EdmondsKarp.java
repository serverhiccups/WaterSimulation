package com.hiccup01.MaxFlow;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * This is an implementation of Edmonds' and Karp's max flow algorithm.
 * Based on pseudo-code from https://en.wikipedia.org/wiki/Edmonds%E2%80%93Karp_algorithm and code from https://en.wikibooks.org/wiki/Algorithm_Implementation/Graphs/Maximum_flow/Edmonds-Karp.
 */
public class EdmondsKarp implements FlowAlgorithm {
	ArrayList<Junction> mutableNetwork; // mutableNetwork is the network that we let the user modify.
	ArrayList<Source> sources; // The ids and flow capacity of the sources in the network
	ArrayList<Sink> sinks; // The ids and flow capacity of the sinks in the network.
	List<List<Edge>> builtNetwork; // builtNetwork is our adjacency list. It contains the extra edges for multiple sources and sinks.
	private int sourceId; // The id of the 'true' source node.
	private int sinkId; // The id of the 'true' sink node.
	private int maxNode; // The maximum id of the ~user supplied~ network.

	/**
	 * Creates a EdmondsKarp object.
	 */
	public EdmondsKarp() {
		this.reset(); // Reset on creation
	}

	/**
	 * Resets the network to its initial state.
	 */
	public void reset() {
		// Clear all the lists.
		this.mutableNetwork = new ArrayList<>();
		this.sources = new ArrayList<>();
		this.sinks = new ArrayList<>();
		this.builtNetwork = new ArrayList<>();
		this.maxNode = 0; // Make the maxNode zero
	}

	/**
	 * Connects together two nodes in the network.
	 * @param src The source node.
	 * @param dest The destination node.
	 * @param maxFlow The maximum capacity of the pipe.
	 */
	public void connect(int src, int dest, int maxFlow) {
		this.mutableNetwork.add(new Junction(src, dest, maxFlow));
	}

	/**
	 * Disconnects two nodes
	 * @param src The source node.
	 * @param dest The destination node.
	 */
	public void disconnect(int src, int dest) {
		for(int i = 0; i < this.mutableNetwork.size(); i++) {
			Junction j = this.mutableNetwork.get(i);
			if(j.src == src && j.dest == dest) this.mutableNetwork.remove(i); // If it has the right source and destination it must be the right node.
			i--;
		}
	}

	/**
	 * Tell the algorithm which nodes are sources.
	 * @param sources The list of sources.
	 */
	public void setSources(ArrayList<Source> sources) {
		if(sources == null) {
			this.sources = new ArrayList<>();
		} else this.sources = sources;

	}

	/**
	 * Tell the algorithm which nodes are sinks.
	 * @param sinks The list of sinks.
	 */
	public void setSinks(ArrayList<Sink> sinks) {
		if(sinks == null) {
			this.sinks = new ArrayList<>();
		} else this.sinks = sinks;
	}

	/**
	 * Add a junction to the builtNetwork.
	 * This is a helper function that makes transforming the graph easier.
	 * @param j The Junction to add.
	 */
	private void addJunction(Junction j) { // Helper to make dealing with reverse edges less painful.
		// Each edge needs to have a reference to it's corresponding reverse edge
		// The 'reverse edges' aren't actually pipes, they are used for back flow / back pressure.
		this.builtNetwork.get(j.src).add(new Edge(j.src, j.dest, j.maxFlow, 0, false, null)); // Create the edge.
		this.builtNetwork.get(j.dest).add(new Edge(j.dest, j.src, j.maxFlow, 0, true, this.builtNetwork.get(j.src).get(this.builtNetwork.get(j.src).size() - 1))); // Create the second edge with a reference to the first edge.
		this.builtNetwork.get(j.src).get(this.builtNetwork.get(j.src).size() - 1).rev = this.builtNetwork.get(j.dest).get(this.builtNetwork.get(j.dest).size() - 1); // Give the first edge a reference to the second edge.
	}

	/**
	 * Builds the builtNetwork that the algorithm can actually process from the mutableNetwork.
	 * @throws MaxFlowException Thrown if there aren't any sources or sinks.
	 */
	private void buildNetwork() throws MaxFlowException {
		this.maxNode = 0;
		// Find the maximum id of the nodes that we were given.
		for (Junction j : this.mutableNetwork) { // We have to use a for loop here because Java doesn't allow lambda expressions to capture outside variables.
			if (j.src > maxNode) maxNode = j.src;
			if (j.dest > maxNode) maxNode = j.dest;
		}
		System.err.println("maxNode is " + maxNode);

		this.builtNetwork = new ArrayList<>(); // Clear the builtNetwork array.
		// Add a list for each node.
		for(int i = 0; i < maxNode + 3; i++) this.builtNetwork.add(new ArrayList<>()); // One for 0 indexing plus one each for the source and sink
		System.err.println("builtNetwork size is " + this.builtNetwork.size());

		for(Junction j : this.mutableNetwork) { // Add edges for all the corresponding Junctions.
			this.addJunction(j);
		}
		this.sources.removeIf(source -> source.id > this.maxNode); // | Remove invalid sources and sinks.
		this.sinks.removeIf(sink -> sink.id > this.maxNode);       // |
		if(sources.size() == 0) throw new MaxFlowException("There are no sources"); // | Check that we have enough sources and sinks.
		if(sinks.size() == 0) throw new MaxFlowException("There are no sinks");     // |


		// These are the ids of the 'true' sources and sinks. We have to have these because the algorithm only supports one source and sink.
		this.sourceId = maxNode + 1;
		this.sinkId = maxNode + 2;
		// Add the junctions to connect the the 'true' sources and sinks.
		for(Source s : this.sources) {
			this.addJunction(new Junction(this.sourceId, s.id, s.flow));
		}
		for(Sink s: this.sinks) {
			this.addJunction(new Junction(s.id, this.sinkId, s.flow));
		}
	}

	public List<List<Edge>> calculate() throws MaxFlowException {
		this.buildNetwork(); // Build the network that we need.
		int flow = 0; // flow is a measure of maximum network flow. ie. How much flow is there between all the sources and sinks.
		while(true) { // We'll break out of this loop once we meet the termination conditions.
			// BFS to find the parents of every node.
			Queue<Integer> queue = new LinkedList<>();
			ArrayList<Edge> parents = new ArrayList<>();
			for(int i = 0; i < this.builtNetwork.size(); i++) parents.add(null);
			queue.add(this.sourceId);
			int current;
			while(queue.size() > 0) {
				current = queue.poll();
				for(Edge v : this.builtNetwork.get(current)) { // For every vertex on the node that we are currently visiting
					if(parents.get(v.dest) == null && v.dest != this.sourceId && v.maxFlow > v.flow) { // See if we shoud set that node's parent.
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

			for(Edge v = parents.get(this.sinkId); v != null; v = parents.get(v.src)) { // Increase the flow on every edge on that augmenting path by that maximum flow.
				v.flow += df;
				v.rev.flow += df;
			}

			flow += df; // Increase the overall flow.
		}
		for(List<Edge> v : this.builtNetwork) { // Remove the extra nodes that we needed that we don't want in the output.
			v.removeIf(edge -> edge.isReverse);
			v.removeIf(edge -> edge.src > this.maxNode);
			v.removeIf(edge -> edge.dest > this.maxNode);
		}
		System.out.println(flow);
		return this.builtNetwork; // Return the processed result.
	}
}
