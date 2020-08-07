package com.hiccup01;

public enum NodeType {
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
