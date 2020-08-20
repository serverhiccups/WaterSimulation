package com.hiccup01;

import java.io.Serializable;

/**
 * Represents the type of a Node. One of junction, source or sink.
 */
public enum NodeType implements Serializable {
	SOURCE,
	SINK,
	JUNCTION;

	/**
	 * Turn the NodeType into an easily readable label.
	 * @return The String representation of the node type.
	 */
	public String toString() {
		switch (this) {
			case SINK:
				return "Sink";
			case SOURCE:
				return "Source";
			case JUNCTION:
			default:
				return "Junction";
		}
	}
}
