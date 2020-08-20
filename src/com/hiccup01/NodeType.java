package com.hiccup01;

import java.io.Serializable;

public enum NodeType implements Serializable {
	SOURCE,
	SINK,
	JUNCTION;

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
