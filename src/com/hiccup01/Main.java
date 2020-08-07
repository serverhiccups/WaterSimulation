package com.hiccup01;

import com.hiccup01.JSprite.*;
import com.hiccup01.MaxFlow.EdmondsKarp;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

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
        JSpriteCanvas canvas = new JSpriteCanvas(); // Canvas to display everything on.
//        canvas.debugMode = true;

        // Add Node Button
        JSprite addNodeButton = new JSprite(8, 8, new JSpriteCostume("icons/add.png"));
        addNodeButton.getVisual(addNodeButton.getCurrentVisual()).setOffsetMode(JSpriteOffsetMode.TOP_RIGHT);
        ((JSpriteCostume)addNodeButton.getVisual(addNodeButton.getCurrentVisual())).fullHitbox = true;
        canvas.addSprite(addNodeButton, 0);

        // Add Source Button
        JSprite addSourceButton = new JSprite(48, 8, new JSpriteVisualStack(new ArrayList<JSpriteVisual>(Arrays.asList(
                new JSpriteCircle(16, Color.red),
                new JSpriteText("So")
        ))));
        addSourceButton.getVisual(0).setOffsetMode(JSpriteOffsetMode.TOP_RIGHT);
        ((JSpriteText)((JSpriteVisualStack)addSourceButton.getVisual(0)).getLayer(1)).setFont(new Font("Helvetica", Font.BOLD, 14));
        canvas.addSprite(addSourceButton, 3);

        // Add Source Button
        JSprite addSinkButton = new JSprite(88, 8, new JSpriteVisualStack(new ArrayList<JSpriteVisual>(Arrays.asList(
                new JSpriteCircle(16, Color.green),
                new JSpriteText("Si")
        ))));
        addSinkButton.getVisual(0).setOffsetMode(JSpriteOffsetMode.TOP_RIGHT);
        ((JSpriteText)((JSpriteVisualStack)addSinkButton.getVisual(0)).getLayer(1)).setFont(new Font("Helvetica", Font.BOLD, 14));
        canvas.addSprite(addSinkButton, 4);

        // Separator
        JSprite separator = new JSprite(0, 48, new JSpriteLine(0, 800, 1));
        separator.getVisual(separator.getCurrentVisual()).setOffsetMode(JSpriteOffsetMode.TOP_RIGHT);

        canvas.addSprite(separator, 1);
//        JSprite background = new JSprite(0, 49, new JSpriteRectangle(800, 640 - 49));
//        canvas.addSprite(background, 2);

        // Network Manager
        NetworkManager manager = new NetworkManager(canvas, this, new EdmondsKarp());

        addNodeButton.addMouseHandler(new AddNodeButtonMouseHandler(manager, NodeType.JUNCTION));
        addSourceButton.addMouseHandler(new AddNodeButtonMouseHandler(manager, NodeType.SOURCE));
        addSinkButton.addMouseHandler(new AddNodeButtonMouseHandler(manager, NodeType.SINK));

        // Setup the window
        this.add(canvas);
        this.pack();
        canvas.createBufferStrategy(2); // Prevent the canvas from flashing
        this.toFront();
        this.setVisible(true);
        this.repaint();
    }
}
