package com.hiccup01;

import com.hiccup01.JSprite.*;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    public static void main(String[] args) throws Exception {
        Main gui = new Main();
        gui.doGui();
    }

    public void doGui() throws Exception {
        this.setTitle("JSprite Test");
        this.getContentPane().setPreferredSize(new Dimension(600, 400));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        JSpriteCanvas canvas = new JSpriteCanvas();
        canvas.debugMode = true;
        JSpriteText t = new JSpriteText("Hello, World!");
        t.setColour(Color.pink);
        t.setFont(new Font("Papyrus", Font.ITALIC, 32));
        JSprite myRectangle = new JSprite(0, 0, t);
        myRectangle.getVisual(0).setOffsetMode(JSpriteOffsetMode.TOP_RIGHT);
        canvas.addSprite(myRectangle, 0);
        this.add(canvas);
        this.pack();
        this.toFront();
        this.setVisible(true);
    }
}
