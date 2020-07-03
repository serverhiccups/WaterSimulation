package com.hiccup01;

import com.hiccup01.JSprite.*;

import java.awt.*;
import java.util.ArrayList;

public class NetworkManager {
	// Set a minimum sprite number, so that our ids don't clash with those of other UI elements.
	final int MIN_SPRITE_NUMBER = 999;

	private ArrayList<Node> nodeList = new ArrayList<>();
	private ArrayList<Pipe> pipeList = new ArrayList<>();
	private JSpriteCanvas canvas = null;
	private int highestSpriteId = MIN_SPRITE_NUMBER;

	final int DEFAULT_X = 500;
	final int DEFAULT_Y = 500;

	public NetworkManager(JSpriteCanvas c) {
		this.canvas = c;
	}

	// Deserialise from a string.
//	public NetworkManager(String s);

	public void addNode() {
		this.nodeList.add(new Node(DEFAULT_X, DEFAULT_Y, NodeType.JUNCTION));
		this.updateView();

	}

	public void addPipe(Node source, Node destination, int maxCapacity) {
		this.pipeList.add(new Pipe(source, destination, maxCapacity, 0));
		this.updateView();
	}

	public void updateView() {
		this.updateView(this.canvas);
	}

	private int nextSpriteId() {
		return ++this.highestSpriteId;
	}

	private void updateView(JSpriteCanvas c) {
		for(Node n : this.nodeList) {
			JSprite sprite = null;
			if(n.spriteContainer == null) { // A sprite needs to be created.
				n.spriteContainer = new JSpriteContainer(this.nextSpriteId(), new JSprite(n.x, n.y, new JSpriteVisualStack()));
				sprite = n.spriteContainer.sprite;
				// Static Assets for the sprites. Can be switched between based of data at paint-time.
				sprite.getVisual(sprite.getCurrentVisual()).setOffsetMode(JSpriteOffsetMode.TOP_RIGHT);
				((JSpriteVisualStack) sprite.getVisual(sprite.getCurrentVisual())).pushLayer(new JSpriteCircle(25, Color.blue));
				((JSpriteVisualStack) sprite.getVisual(sprite.getCurrentVisual())).top().setOffsetMode(JSpriteOffsetMode.TOP_RIGHT);
				JSpriteText label = new JSpriteText("");
				label.setOffsetMode(JSpriteOffsetMode.CENTER);
				switch (n.type) {
					case JUNCTION:
						label.setText("Junction");
						break;
					case SINK:
						label.setText("Sink");
						break;
					case SOURCE:
						label.setText("Source");
						break;
				}
				((JSpriteVisualStack) sprite.getVisual(sprite.getCurrentVisual())).pushLayer(label);
				try {
					c.addSprite(sprite, n.spriteContainer.id);
				} catch (Exception e) {
					System.err.println("Failed to add a node to the canvas");
				}
			} else {
				// Set the state of the sprite based on the data
				sprite = n.spriteContainer.sprite;
				sprite.xPosition = n.x;
				sprite.yPosition = n.y;
			}
		}
	}

//	public String serialise();
}
