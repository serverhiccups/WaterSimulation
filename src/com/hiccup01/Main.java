package com.hiccup01;

import com.hiccup01.JSprite.*;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    public static void main(String[] args) throws Exception {
        Main gui = new Main();
        // This allows us to call non-static methods.
        gui.doGui();
    }

    public void doGui() throws Exception {
        this.setTitle("Water Simulation");
        this.getContentPane().setPreferredSize(new Dimension(800, 640));
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Main UI Elements
        JSpriteCanvas canvas = new JSpriteCanvas();
//        canvas.debugMode = true;
        JSprite addButton = new JSprite(8, 8, new JSpriteCostume("icons/add.png"));
        addButton.getVisual(addButton.getCurrentVisual()).setOffsetMode(JSpriteOffsetMode.TOP_RIGHT);
//        ((JSpriteCostume)addButton.getVisual(0)).setRotation(Math.PI / 4);
        canvas.addSprite(addButton, 0);
        JSprite separator = new JSprite(0, 48, new JSpriteLine(0, 800, 1));
        separator.getVisual(separator.getCurrentVisual()).setOffsetMode(JSpriteOffsetMode.TOP_RIGHT);
        canvas.addSprite(separator, 1);
//        JSprite background = new JSprite(0, 49, new JSpriteRectangle(800, 640 - 49));
//        canvas.addSprite(background, 2);

        // Network Manager
        NetworkManager manager = new NetworkManager(canvas);
//        manager.addNode();

        addButton.addMouseHandler(new AddNodeButtonMouseHandler(manager));

        // Setup the window
        this.add(canvas);
        this.pack();
        canvas.createBufferStrategy(2); // Prevent the canvas from flashing
        this.toFront();
        this.setVisible(true);
        this.repaint();
    }
}
