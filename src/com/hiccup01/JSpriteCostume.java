package com.hiccup01;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class JSpriteCostume {
	Image costume;
	public JSpriteCostume(String filename) throws IOException {
		this.costume = ImageIO.read(new File(filename));
	}

	public JSpriteCostume(Image image) {
		this.costume = image;
	}
}
