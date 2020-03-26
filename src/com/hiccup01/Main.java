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
        JSprite myRectangle = new JSprite(100, 100, new JSpriteCostume("rect.png"));
        canvas.addSprite(myRectangle, 0);
        this.add(canvas);
        this.pack();
        this.toFront();
        this.setVisible(true);
        ActionListener p = new ActionListener() {
            int i = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                System.err.println("The timer was fired");
                switch (i % 3) {
                    case 0:
                        myRectangle.costumes.get(0).setOffsetMode(JSpriteCenterMode.CENTER);
                        break;
                    case 1:
                        myRectangle.costumes.get(0).setOffsetMode(JSpriteCenterMode.TOP_RIGHT);
                        break;
                    case 2:
                        myRectangle.costumes.get(0).setOffsetMode(JSpriteCenterMode.BOTTOM_LEFT);
                        break;
                }
                canvas.repaint();
                this.i++;
            }
        };
        new Timer(1000, p).start();
    }
}
