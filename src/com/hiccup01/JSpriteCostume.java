package com.hiccup01;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class JSpriteCostume {
	Image costume;
	int xOffset; // These offsets allow us to have per-costume offsets that allow us to have sprite x and y be the center.
	int yOffset;
	private JSpriteCenterMode offsetMode = JSpriteCenterMode.CENTER;
	public JSpriteCostume(String filename) throws IOException {
		this.costume = ImageIO.read(new File(filename));
		this.updateOffsets();
	}

	public JSpriteCostume(Image image) {
		this.costume = image;
		this.updateOffsets();
	}

	public JSpriteCenterMode getOffsetMode() {
		return this.offsetMode;
	}

	public void setOffsetMode(JSpriteCenterMode offsetMode) {
		this.offsetMode = offsetMode;
		this.updateOffsets();
	}

	private void updateOffsets() {
		int height = this.costume.getHeight(null);
		int width = this.costume.getWidth(null);
		switch (this.offsetMode) {
			case CENTER:
				this.xOffset = width / 2;
				this.yOffset = height / 2;
				break;
			case TOP_RIGHT:
				this.xOffset = 0;
				this.yOffset = 0;
				break;
			case BOTTOM_LEFT:
				this.xOffset = width;
				this.yOffset = height;
				break;
		}
	}
}
