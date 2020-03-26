package com.hiccup01;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

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
        JSprite myRectangle = new JSprite(0, 0, new JSpriteCostume("rect.png"));
        canvas.addSprite(myRectangle, 0);
        this.add(canvas);
        this.pack();
        this.toFront();
        this.setVisible(true);
        ActionListener p = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.err.println("The timer was fired");
                myRectangle.xPosition += 1;
                myRectangle.yPosition += 1;
                myRectangle.visible = !myRectangle.visible;
                canvas.repaint();
            }
        };
        new Timer(10, p).start();
    }
}
