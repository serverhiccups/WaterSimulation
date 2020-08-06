package com.hiccup01;

import com.hiccup01.JSprite.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class NetworkManager {
	// Set a minimum sprite number, so that our ids don't clash with those of other UI elements.
	final int MIN_SPRITE_NUMBER = 999;

	private ArrayList<Node> nodeList = new ArrayList<>();
	private ArrayList<Pipe> pipeList = new ArrayList<>();
	public PipePreview pipePreview = null;
	public JSpriteCanvas canvas = null;
	private int highestSpriteId = MIN_SPRITE_NUMBER;
	private BufferedImage arrowImage = null;
	public JFrame frame = null;

	final int DEFAULT_X = 500;
	final int DEFAULT_Y = 500;

	public NetworkManager(JSpriteCanvas c, JFrame frame) throws Exception {
		this.canvas = c;
		this.frame = frame;
		this.arrowImage = ImageIO.read(new File("icons/arrow.png"));
	}

	// Deserialise from a string.
//	public NetworkManager(String s);

	public void addNode() {
		this.nodeList.add(new Node(DEFAULT_X, DEFAULT_Y, NodeType.JUNCTION));
		this.updateView();

	}

	public void addPipe(Node source, Node destination, int maxCapacity) {
		if(this.pipeExists(source, destination)) {
			this.updateView();
			return;
		}
		System.out.println("Connected Node " + source + " to node " + destination);
		this.pipeList.add(new Pipe(source, destination, maxCapacity, 0));
		this.updateView();
	}

	public Pipe removePipe(Pipe p) {
		this.pipeList.remove(p);
		try {
			this.canvas.removeSprite(p.spriteContainer.id);
		} catch (Exception e) {
			System.err.println("Failed to remove a pipe.");
		}
		this.updateView();
		return p;
	}

	public boolean pipeExists(Node source, Node destination) {
		for(Pipe p : this.pipeList) {
			if((p.source == source && p.destination == destination) || (p.source == destination && p.destination == source)) return true;
		}
		return false;
	}

	public void updateView() {
		this.updateView(this.canvas);
	}

	private int nextSpriteId() {
		return ++this.highestSpriteId;
	}

	public JSprite getPipePreview(int startX, int startY, Node startNode){
		if(pipePreview != null) return pipePreview.spriteContainer.sprite;
//		System.err.println("startX: " + startX + " startY: " + startY);
		this.pipePreview = new PipePreview(startX, startY, startX, startY, startNode);
		this.pipePreview.spriteContainer = new JSpriteContainer(this.nextSpriteId(), new JSprite(0, 0, new JSpriteLine(0, 100, 5)));
		((JSpriteLine) this.pipePreview.spriteContainer.sprite.getVisual(0)).setColour(Color.BLACK);
		this.pipePreview.spriteContainer.sprite.addMouseHandler(new PipePreviewMouseHandler(this.pipePreview, this));
//		System.out.println("PipePreview sprite is " + this.pipePreview.spriteContainer.sprite);
		return pipePreview.spriteContainer.sprite;
	}

	public void landPreview(int x, int y) {
		ArrayList<JSpriteContainer> containers = this.canvas.eventHandler.findSpriteContainersAt(x, y);
		Node target = null;
		for(JSpriteContainer c : containers) {
			target = this.nodeAssociatedWith(c.id);
			if(target != null) {
				this.addPipe(this.pipePreview.startNode, target, 100);
			} else continue;
		}
		try {
			this.canvas.removeSprite(this.pipePreview.spriteContainer.id);
		} catch (Exception e) {
			System.err.println("Failed to remove the PipePreview from the canvas");
		}
		this.pipePreview = null;
	}

	public Node nodeAssociatedWith(int id) {
		if(id <= 999) return null;
		for(Node n : this.nodeList) {
			if(n.spriteContainer == null) continue;
			if(n.spriteContainer.id == id) return n;
		}
		return null;
	}

	private void updateView(JSpriteCanvas c) {
		// Nodes
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
				label.setColour(Color.white);
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
				sprite = n.spriteContainer.sprite;
			}
			// Set the state of the sprite based on the data
			sprite.xPosition = n.x;
			sprite.yPosition = n.y;
		}
		for(Pipe p : this.pipeList) {
			JSprite sprite = null;
			if(p.spriteContainer == null) { // A new sprite needs to be created.
				p.spriteContainer = new JSpriteContainer(this.nextSpriteId(), new JSprite(0, 0, new JSpriteVisualStack()));
				sprite = p.spriteContainer.sprite;
				sprite.getVisual(sprite.getCurrentVisual()).setOffsetMode(JSpriteOffsetMode.CENTER);
				((JSpriteVisualStack) sprite.getVisual(sprite.getCurrentVisual())).pushLayer(new JSpriteLine(0, 0, 7));
				((JSpriteVisualStack) sprite.getVisual(sprite.getCurrentVisual())).top().setOffsetMode(JSpriteOffsetMode.CENTER);
				((JSpriteVisualStack) sprite.getVisual(sprite.getCurrentVisual())).pushLayer(new JSpriteCostume(this.arrowImage));
				((JSpriteVisualStack) sprite.getVisual(sprite.getCurrentVisual())).top().setOffsetMode(JSpriteOffsetMode.CENTER);
				((JSpriteVisualStack) sprite.getVisual(sprite.getCurrentVisual())).pushLayer(new JSpriteRectangle(54, 24, Color.black));
				((JSpriteVisualStack) sprite.getVisual(sprite.getCurrentVisual())).top().setOffsetMode(JSpriteOffsetMode.CENTER);
				((JSpriteVisualStack) sprite.getVisual(sprite.getCurrentVisual())).pushLayer(new JSpriteRectangle(50, 20, Color.white));
				((JSpriteVisualStack) sprite.getVisual(sprite.getCurrentVisual())).top().setOffsetMode(JSpriteOffsetMode.CENTER);
				JSpriteText capacity = new JSpriteText("");
				capacity.setFont(new Font("Helvetica", Font.PLAIN, 11));
				capacity.setOffsetMode(JSpriteOffsetMode.CENTER);
				((JSpriteVisualStack) sprite.getVisual(sprite.getCurrentVisual())).pushLayer(capacity);
				sprite.addMouseHandler(new PipeMouseHandler(p, this));
				try {
					c.addSprite(sprite, p.spriteContainer.id);
				} catch (Exception e) {
					System.err.println("Failed to add a pipe to the canvas");
				}
			} else {
				sprite = p.spriteContainer.sprite;
			}
			JSpriteLine line = (JSpriteLine)((JSpriteVisualStack) sprite.getVisual(0)).getLayer(0);
			int[] points = line.alignToPoints(p.source.x, p.source.y, p.destination.x, p.destination.y);
//			System.out.println("Stack size is x: " + sprite.getVisual(sprite.getCurrentVisual()).getWidth() + " y: " + sprite.getVisual(sprite.getCurrentVisual()).getHeight());
//			System.out.println("Line size is x: " + line.getWidth() + " y: " + line.getHeight());
//			System.out.println("Points are x: " + points[0] + " y: " + points[1]);
//			System.out.println("Source is x: " + p.source.x + " y: " + p.source.y);
			sprite.xPosition = points[0];
			sprite.yPosition = points[1];
			((JSpriteCostume)((JSpriteVisualStack) sprite.getVisual(0)).getLayer(1)).setRotation(-1 * line.getRotation() - (1 * Math.PI / 4));
			((JSpriteText)((JSpriteVisualStack) sprite.getVisual(0)).top()).setText(Integer.toString(p.maxCapacity));
			try {
				c.sendToBack(p.spriteContainer.id);
			} catch (Exception e) {

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
