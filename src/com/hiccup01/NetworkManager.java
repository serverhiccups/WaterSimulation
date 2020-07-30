package com.hiccup01;

import com.hiccup01.JSprite.*;

import java.awt.*;
import java.util.ArrayList;

public class NetworkManager {
	// Set a minimum sprite number, so that our ids don't clash with those of other UI elements.
	final int MIN_SPRITE_NUMBER = 999;

	private ArrayList<Node> nodeList = new ArrayList<>();
	private ArrayList<Pipe> pipeList = new ArrayList<>();
	public PipePreview pipePreview = null;
	public JSpriteCanvas canvas = null;
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

	public JSprite getPipePreview(int startX, int startY){
		if(pipePreview != null) return pipePreview.spriteContainer.sprite;
		System.err.println("startX: " + startX + " startY: " + startY);
		this.pipePreview = new PipePreview(startX, startY, startX, startY);
		this.pipePreview.spriteContainer = new JSpriteContainer(this.nextSpriteId(), new JSprite(0, 0, new JSpriteLine(0, 100, 5)));
		((JSpriteLine) this.pipePreview.spriteContainer.sprite.getVisual(0)).setColour(Color.PINK);
		this.pipePreview.spriteContainer.sprite.addMouseHandler(new PipePreviewMouseHandler(this.pipePreview, this));
		System.out.println("PipePreview sprite is " + this.pipePreview.spriteContainer.sprite);
		return pipePreview.spriteContainer.sprite;
	}

	private void updateView(JSpriteCanvas c) {
		for(Node n : this.nodeList) {
			JSprite sprite = null;
			if(n.spriteContainer == null) { // A sprite needs to be created.
				n.spriteContainer = new JSpriteContainer(this.nextSpriteId(), new JSprite(n.x, n.y, new JSpriteVisualStack()));
				sprite = n.spriteContainer.sprite;
				// Static Assets for the sprites. Can be switched between based of data at paint-time.
				sprite.getVisual(sprite.getCurrentVisual()).setOffsetMode(JSpriteOffsetMode.CENTER);
				((JSpriteVisualStack) sprite.getVisual(sprite.getCurrentVisual())).pushLayer(new JSpriteCircle(38, new Color(75, 75, 255)));
				((JSpriteVisualStack) sprite.getVisual(sprite.getCurrentVisual())).top().setOffsetMode(JSpriteOffsetMode.CENTER);
				((JSpriteVisualStack) sprite.getVisual(sprite.getCurrentVisual())).pushLayer(new JSpriteCircle(35, Color.blue));
				((JSpriteVisualStack) sprite.getVisual(sprite.getCurrentVisual())).top().setOffsetMode(JSpriteOffsetMode.CENTER);
				JSpriteText label = new JSpriteText("");
				label.setFont(new Font("Helvetica", Font.BOLD, 14));
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
				sprite.addMouseHandler(new NodeMouseHandler(n, this));
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
		// Pipe Preview
		if(this.pipePreview != null) {
			if(!this.pipePreview.onCanvas) {
				try {
					c.addSprite(this.pipePreview.spriteContainer.sprite, this.pipePreview.spriteContainer.id);
					this.pipePreview.onCanvas = true;
					System.err.println("Added the preview to the canvas");
//					this.nodeList.get(0).spriteContainer.sprite.visible = false;
				} catch (Exception e) {
					System.err.println("Failed to create a pipe preview");
				}
			}
			int[] point = ((JSpriteLine) this.pipePreview.spriteContainer.sprite.getVisual(this.pipePreview.spriteContainer.sprite.getCurrentVisual())).alignToPoints(this.pipePreview.startX, this.pipePreview.startY, this.pipePreview.endX, this.pipePreview.endY);
			this.pipePreview.spriteContainer.sprite.xPosition = point[0];
			this.pipePreview.spriteContainer.sprite.yPosition = point[1];
			try {
				c.sendToBack(this.pipePreview.spriteContainer.id);
			} catch (Exception e) {
				System.err.println("Failed to send the PipePreview to the back");
			}
			System.err.println("Updated pipe preview position");
		}
		this.canvas.repaint();
	}

//	public String serialise();
}
