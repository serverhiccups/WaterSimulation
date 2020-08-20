package com.hiccup01;

import com.hiccup01.JSprite.*;
import com.hiccup01.MaxFlow.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;

/**
 * The network manager is the heart of the program. It handles all of the application data and drawing to the screen.
 */
public class NetworkManager {
	// Set a minimum sprite number, so that our ids don't clash with those of other UI elements.
	final int MIN_SPRITE_NUMBER = 999;

	private ArrayList<Node> nodeList = new ArrayList<>();
	private ArrayList<Pipe> pipeList = new ArrayList<>();
	public PipePreview pipePreview = null; // The preview of the pipe being drawn.
	public JSpriteCanvas canvas = null; // The canvas for the network to be drawn on.
	private int highestSpriteId = MIN_SPRITE_NUMBER; // initialise the highest sprite number.
	private BufferedImage arrowImage = null; // The arrow that we use to show the direction of the pipes.
	public JFrame frame = null; // The frame that our canvas is in, so that we can properly position dialog windows.
	private FlowAlgorithm algorithm = null; // The network flow algorithm to use.

	final int DEFAULT_X = 500;
	final int DEFAULT_Y = 500;

	/**
	 * Create a NetworkManager.
	 * @param c This canvas for the network manager to draw to.
	 * @param frame The JFrame that the canvas is contained in.
	 * @param algorithm The flow algorithm.
	 * @throws Exception Thrown if the arrow image cannot be read from the jar.
	 */
	public NetworkManager(JSpriteCanvas c, JFrame frame, FlowAlgorithm algorithm) throws Exception {
		this.canvas = c;
		this.frame = frame;
        this.arrowImage = ImageIO.read(getClass().getResource("/icons/arrow.png")); // Read our image of an arrow from the jar.
		this.algorithm = algorithm;
		this.init();
	}

	public void init() {
		while(this.nodeList.size() > 0) {
			this.removeNode(this.nodeList.get(0));
		}
	}

	/**
	 * Add a node to the simulation.
	 * @param type The type of the node (one of junction, source or sink).
	 */
	public void addNode(NodeType type) {
		this.nodeList.add(new Node(DEFAULT_X, DEFAULT_Y, 250, type));
		this.updateView();

	}

	/**
	 * Removes a node from the simulation.
	 * @param n The node to remove.
	 * @return The node that was removed.
	 */
	public Node removeNode(Node n) {
		ArrayList<Pipe> pipesToRemove = new ArrayList<>(); // The list of pipes that we need to remove because they connect to this node.
		for(Pipe p : this.pipeList) {
			if(p.source == n || p.destination == n) { // We need to remove this pipe, because one of its end s would be empty.
				// We have to do this silly workaround instead of remove things directly in the loop, because we can't modify an array that we are iterating.
				pipesToRemove.add(p);
			}
		}
		for(Pipe p : pipesToRemove) { // Remove the pipes that need removing.
			this.removePipe(p);
		}
		this.nodeList.remove(n); // Remove the node from the node list.
		try {
			this.canvas.removeSprite(n.spriteContainer.id);
		} catch (JSpriteException e) {
			System.err.println("Failed to remove a node");
		}
		this.updateView();
		return n;
	}

	/**
	 * Add a pipe to the simulation.
	 * @param source The source of the pipe.
	 * @param destination The destination of the pipe.
	 * @param maxCapacity The maximum capacity of the pipe.
	 */
	public void addPipe(Node source, Node destination, int maxCapacity) {
		if(this.pipeExists(source, destination)) { // Don't try to add a pipe that already exists.
			this.updateView();
			return;
		}
		this.pipeList.add(new Pipe(source, destination, maxCapacity, 0)); // Add the pipe to the pipe list.
		this.updateView();
	}

	/**
	 * Remove a pipe from the simulation.
	 * @param p The pipe to remove.
	 * @return The pipe that was removed.
	 */
	public Pipe removePipe(Pipe p) {
		this.pipeList.remove(p); // Remove the pipe from the pipe list.
		try {
			this.canvas.removeSprite(p.spriteContainer.id); // Remove the pipe from the canvas.
		} catch (Exception e) {
			System.err.println("Failed to remove a pipe.");
		}
		this.updateView();
		return p;
	}

	/**
	 * Checks whether or not a pipe exists between two nodes.
	 * @param source The first end of the pipe.
	 * @param destination The second end of the pipe.
	 * @return True if the pipe exists.
	 */
	public boolean pipeExists(Node source, Node destination) {
		for(Pipe p : this.pipeList) {
			if((p.source == source && p.destination == destination) || (p.source == destination && p.destination == source)) return true;
		}
		return false;
	}

	/**
	 * Finds the pipe between the source and the destination.
	 * @param source The source.
	 * @param destination The destination.
	 * @return The pipe, if one exists, or null if one doesn't.
	 */
	public Pipe pipeBetween(Node source, Node destination) {
		for(Pipe p : this.pipeList) {
			if((p.source == source && p.destination == destination)) return p;
		}
		return null;
	}

	/**
	 * Draws the canvas based on the data.
	 */
	public void updateView() {
		this.updateView(this.canvas);
	}

	private int nextSpriteId() {
		return ++this.highestSpriteId;
	}

	/**
	 * Creates a PipePreview, or returns it if one has already been created.
	 * @param startX The starting X position.
	 * @param startY the starting Y position.
	 * @param startNode The starting node.
	 * @return The PipePreview that was created.
	 */
	public JSprite getPipePreview(int startX, int startY, Node startNode){
		if(pipePreview != null) return pipePreview.spriteContainer.sprite; // Return the pipe preview if it's already been created.
		this.pipePreview = new PipePreview(startX, startY, startX, startY, startNode); // Create the PipePreview.
		// Create the sprite for the PipePreview.
		this.pipePreview.spriteContainer = new JSpriteContainer(this.nextSpriteId(), new JSprite(0, 0, new JSpriteLine(0, 100, 5)));
		((JSpriteLine) this.pipePreview.spriteContainer.sprite.getVisual(0)).setColour(Color.BLACK);
		// Add the PipePreviewMouseHandler.
		this.pipePreview.spriteContainer.sprite.addMouseHandler(new PipePreviewMouseHandler(this.pipePreview, this));
//		System.out.println("PipePreview sprite is " + this.pipePreview.spriteContainer.sprite);
		return pipePreview.spriteContainer.sprite;
	}

	/**
	 * Turns a PipePreview into a real Pipe.
	 * @param x The X position of the end of the pipe preview.
	 * @param y The Y position of the end of the pipe preview.
	 */
	public void landPreview(int x, int y) {
		ArrayList<JSpriteContainer> containers = this.canvas.eventHandler.findSpriteContainersAt(x, y); // Get the list of all the sprites under the cursor.
		Node target = null;
		for(JSpriteContainer c : containers) { // Find the first (ie. topmost) one that is a node.
			target = this.nodeAssociatedWith(c.id); // Is this sprite a node?
			if(target != null) {
				this.addPipe(this.pipePreview.startNode, target, 100);
			} else continue;
		}
		try {
			this.canvas.removeSprite(this.pipePreview.spriteContainer.id); // Remove the PipePreview sprite from the canvas.
		} catch (Exception e) {
			System.err.println("Failed to remove the PipePreview from the canvas");
		}
		this.pipePreview = null; // Clear the PipePreview.
	}

	/**
	 * Find the Node associated with a certain sprite id.
	 * @param id The id of the node to find.
	 * @return The node with that id if one was found. null if one wasn't.
	 */
	public Node nodeAssociatedWith(int id) {
		if(id <= 999) return null; // It can't be a sprite created by the NetworkManager.
		for(Node n : this.nodeList) {
			if(n.spriteContainer == null) continue;
			if(n.spriteContainer.id == id) return n;
		}
		return null;
	}

	/**
	 * Calculate the flows of every node on the network.
	 */
	private void calculateFlow() {
		this.algorithm.reset(); // Reset from the last time we calculated.
		for(Pipe p : this.pipeList) { // Add every pipe as a vertex to the algorithm's representation.
			this.algorithm.connect(this.nodeList.indexOf(p.source), this.nodeList.indexOf(p.destination), p.maxCapacity);
		}
		ArrayList<Sink> sinks = new ArrayList<>();
		ArrayList<Source> sources = new ArrayList<>();
		for(Node n : this.nodeList) { // Add the sources and sinks from our network the the algorithm's
			if(n.type == NodeType.SINK) sinks.add(new Sink(this.nodeList.indexOf(n), n.capacity));
			if(n.type == NodeType.SOURCE) sources.add(new Source(this.nodeList.indexOf(n), n.capacity));
		}
		// Tell the algoritm about the sources and sinks.
		this.algorithm.setSinks(sinks);
		this.algorithm.setSources(sources);
		try { // Try to calculate the flows.
			java.util.List<java.util.List<Edge>> output = this.algorithm.calculate(); // Calculate the graph.
			for(java.util.List<Edge> v : output) { // For every node.
				for(Edge e : v) { // For every vertex.
					if(e.src >= this.nodeList.size() || e.dest >= this.nodeList.size()) {
						continue; // Ignore the source and sink elements created by the algorithm.
					}
					try {
						this.pipeBetween(this.nodeList.get(e.src), this.nodeList.get(e.dest)).flow = e.flow; // Find the pipe between those two nodes and set the flow.
					} catch(NullPointerException error) {
						// I think that this occurs when there's a mismatch between our network and the network the algorithm has.
						System.err.println("We got a NullPointer Exception:");
						System.err.println("e.src: " + e.src + " e.dest: " + e.dest);
						error.printStackTrace();
					}
				}
			}
		} catch (MaxFlowException e) {
			// This most likely happened becauese the user's network didn't have a connected source or sink.
			System.err.println("An error occured while trying to calculate the flow of the network: " + e.toString());
		}
	}

	/**
	 * Turn all the data on this component.
	 * @param c The canvas to draw to.
	 */
	private void updateView(JSpriteCanvas c) {
		this.calculateFlow(); // Calculate the flow so that we can draw with it.

		// Nodes
		for(Node n : this.nodeList) { // Add the nodes to the canvas.
			JSprite sprite = null;
			if(n.spriteContainer == null) { // A sprite needs to be created for this node.
				n.spriteContainer = new JSpriteContainer(this.nextSpriteId(), new JSprite(n.x, n.y, new JSpriteVisualStack()));
				sprite = n.spriteContainer.sprite;
				// Static Assets for the sprites. Can be switched between based of data at paint-time.
				sprite.getVisual(sprite.getCurrentVisual()).setOffsetMode(JSpriteOffsetMode.CENTER);
				((JSpriteVisualStack) sprite.getVisual(sprite.getCurrentVisual())).pushLayer(new JSpriteCircle(38, new Color(75, 75, 255)));
				((JSpriteVisualStack) sprite.getVisual(sprite.getCurrentVisual())).top().setOffsetMode(JSpriteOffsetMode.CENTER);
				switch(n.type) { // Colour our node based on the type
					case JUNCTION:
						((JSpriteVisualStack) sprite.getVisual(sprite.getCurrentVisual())).pushLayer(new JSpriteCircle(35, Color.blue));
						break;
					case SOURCE:
						((JSpriteVisualStack) sprite.getVisual(sprite.getCurrentVisual())).pushLayer(new JSpriteCircle(35, Color.red));
						break;
					case SINK:
						((JSpriteVisualStack) sprite.getVisual(sprite.getCurrentVisual())).pushLayer(new JSpriteCircle(35, Color.green));
						break;
				}
				((JSpriteVisualStack) sprite.getVisual(sprite.getCurrentVisual())).top().setOffsetMode(JSpriteOffsetMode.CENTER);
				JSpriteText label = new JSpriteText("");
				label.setColour(Color.white);
				label.setFont(new Font("Helvetica", Font.BOLD, 14));
				label.setOffsetMode(JSpriteOffsetMode.CENTER);
				label.setText(n.type.toString()); // Label the node based on the type.
				((JSpriteVisualStack) sprite.getVisual(sprite.getCurrentVisual())).pushLayer(label);
				sprite.addMouseHandler(new NodeMouseHandler(n, this)); // Add a mouse handler to the new sprite.
				try {
					c.addSprite(sprite, n.spriteContainer.id); // Add the sprite to the canvas.
				} catch (Exception e) {
					System.err.println("Failed to add a node to the canvas");
				}
			} else {
				sprite = n.spriteContainer.sprite;
			}
			// A sprite has already been created.
			// Set the state of the sprite based on the data
			sprite.xPosition = n.x;
			sprite.yPosition = n.y;
		}
		// Pipes.
		for(Pipe p : this.pipeList) {
			JSprite sprite = null;
			if(p.spriteContainer == null) { // A new sprite needs to be created.
				// Create the new sprite.
				p.spriteContainer = new JSpriteContainer(this.nextSpriteId(), new JSprite(0, 0, new JSpriteVisualStack()));
				sprite = p.spriteContainer.sprite;
				sprite.getVisual(sprite.getCurrentVisual()).setOffsetMode(JSpriteOffsetMode.CENTER);
				// Set up the visuals fo the sprite.
				((JSpriteVisualStack) sprite.getVisual(sprite.getCurrentVisual())).pushLayer(new JSpriteLine(0, 0, 7));
				((JSpriteVisualStack) sprite.getVisual(sprite.getCurrentVisual())).top().setOffsetMode(JSpriteOffsetMode.CENTER);
				((JSpriteVisualStack) sprite.getVisual(sprite.getCurrentVisual())).pushLayer(new JSpriteCostume(this.arrowImage));
				((JSpriteVisualStack) sprite.getVisual(sprite.getCurrentVisual())).top().setOffsetMode(JSpriteOffsetMode.CENTER);
				((JSpriteVisualStack) sprite.getVisual(sprite.getCurrentVisual())).pushLayer(new JSpriteRectangle(54, 24, Color.black));
				((JSpriteVisualStack) sprite.getVisual(sprite.getCurrentVisual())).top().setOffsetMode(JSpriteOffsetMode.CENTER);
				((JSpriteVisualStack) sprite.getVisual(sprite.getCurrentVisual())).pushLayer(new JSpriteRectangle(50, 20, Color.white));
				((JSpriteVisualStack) sprite.getVisual(sprite.getCurrentVisual())).top().setOffsetMode(JSpriteOffsetMode.CENTER);
				((JSpriteVisualStack) sprite.getVisual(sprite.getCurrentVisual())).pushLayer(new JSpriteRectangle(0, 20, new Color(0, 222, 252)));
				((JSpriteVisualStack) sprite.getVisual(sprite.getCurrentVisual())).top().setOffsetMode(JSpriteOffsetMode.CENTER);
				JSpriteText capacity = new JSpriteText(""); // Label the sprite with the capacity.
				capacity.setFont(new Font("Helvetica", Font.PLAIN, 11));
				capacity.setOffsetMode(JSpriteOffsetMode.CENTER);
				((JSpriteVisualStack) sprite.getVisual(sprite.getCurrentVisual())).pushLayer(capacity);
				sprite.addMouseHandler(new PipeMouseHandler(p, this)); // Add the mouse handler
				try {
					c.addSprite(sprite, p.spriteContainer.id); // Add the sprite to the canvas.
				} catch (Exception e) {
					System.err.println("Failed to add a pipe to the canvas");
				}
			} else {
				sprite = p.spriteContainer.sprite;
			}
			JSpriteLine line = (JSpriteLine)((JSpriteVisualStack) sprite.getVisual(0)).getLayer(0); // Get the line visual from the sprite.
			int[] points = line.alignToPoints(p.source.x, p.source.y, p.destination.x, p.destination.y); // Align the line for the given location.
            // Move the sprite so that it is aligned.
			sprite.xPosition = points[0];
			sprite.yPosition = points[1];
			((JSpriteCostume)((JSpriteVisualStack) sprite.getVisual(0)).getLayer(1)).setRotation(-1 * line.getRotation() - (1 * Math.PI / 4)); // Set the rotation of the arrow according to the rotation of the line.
			((JSpriteText)((JSpriteVisualStack) sprite.getVisual(0)).top()).setText(Integer.toString(p.maxCapacity)); // Set the text based on the capacity.
			((JSpriteRectangle)((JSpriteVisualStack) sprite.getVisual(0)).getLayer(4)).setSize((int) (50 * ((float)p.flow / (float)p.maxCapacity)), 20); // Set the blue rectangle's size.
			try {
				c.sendToBack(p.spriteContainer.id); // Send the line to the back so that it is behind the nodes.
			} catch (Exception e) {

			}
		}
		// Pipe Preview
		if(this.pipePreview != null) {
			if(!this.pipePreview.onCanvas) { // We can't see the PipePreview.
				try {
					c.addSprite(this.pipePreview.spriteContainer.sprite, this.pipePreview.spriteContainer.id); // Add the PipePreview to the canvas.
					this.pipePreview.onCanvas = true;
					System.err.println("Added the preview to the canvas");
//					this.nodeList.get(0).spriteContainer.sprite.visible = false;
				} catch (Exception e) {
					System.err.println("Failed to create a pipe preview");
				}
			}
			// Align the pipe preview's line.
			int[] point = ((JSpriteLine) this.pipePreview.spriteContainer.sprite.getVisual(this.pipePreview.spriteContainer.sprite.getCurrentVisual())).alignToPoints(this.pipePreview.startX, this.pipePreview.startY, this.pipePreview.endX, this.pipePreview.endY);
			// Move the pipe preview so that it is aligned.
			this.pipePreview.spriteContainer.sprite.xPosition = point[0];
			this.pipePreview.spriteContainer.sprite.yPosition = point[1];
			try {
				c.sendToBack(this.pipePreview.spriteContainer.id); // Send the pipe preview to the back.
			} catch (Exception e) {
				System.err.println("Failed to send the PipePreview to the back");
			}
			System.err.println("Updated pipe preview position");
		}
		this.canvas.repaint(); // Repaint the JSpriteCanvas.
	}

	/**
	 * Serialise the data contained in the NetworkManager.
	 * @param path The path to write the serialised data to.
	 * @throws Exception Thrown if there is an error during serialising and writing.
	 */
	public void serialise(String path) throws Exception {
		FileOutputStream file = new FileOutputStream(path);
		ObjectOutputStream oos = new ObjectOutputStream(file);
		// Write the serialised objects.
		oos.writeObject(this.nodeList);
		oos.writeObject(this.pipeList);
		file.close();
	}

	/**
	 * Deserialise the data contained in a file onto the canvas.
	 * @param path The path to data to deserialise from.
	 * @throws Exception Thrown if there is an error deserialising or reading the data.
	 */
	public void deserialise(String path) throws Exception {
		this.init();
		FileInputStream file = new FileInputStream(path);
		ObjectInputStream ois = new ObjectInputStream(file);
		try {
			// Read the objects and appropriate cast them.
			ArrayList<Node> nodes = (ArrayList<Node>)ois.readObject();
			this.nodeList = nodes;
			ArrayList<Pipe> pipes = (ArrayList<Pipe>)ois.readObject();
			this.pipeList = pipes;
		} catch (Exception e) {
			throw e;
		}
		this.updateView();
	}
}
