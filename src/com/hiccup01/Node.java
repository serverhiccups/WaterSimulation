package com.hiccup01;

import com.hiccup01.JSprite.JSprite;
import com.hiccup01.JSprite.JSpriteContainer;

public class Node {
	public int x = 0;
	public int y = 0;
	public int capacity = 0; // Ignored if type is NodeType.Junction
	public NodeType type = NodeType.JUNCTION;
	public JSpriteContainer spriteContainer = null;

	public Node(int x, int y, int capacity, NodeType type) {
		this.x = x;
		this.y = y;
		this.capacity = capacity;
		this.type = type;
	}
}
