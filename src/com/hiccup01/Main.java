package com.hiccup01;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

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
        myRectangle.setVisual(1, new JSpriteCostume("red-circle.png"));
        myRectangle.getVisual(0).setOffsetMode(JSpriteOffsetMode.TOP_RIGHT);
        myRectangle.getVisual(1).setOffsetMode(JSpriteOffsetMode.TOP_RIGHT);
        myRectangle.addMouseHandler(new JSpriteMouseHandler() {
            @Override
            public boolean scrollEvent(int amount) {
                return false;
            }

            @Override
            public boolean mouseClicked(JSpriteMouseEvent m) {
                System.err.println("Mouse clicked");
                return true;
            }

            @Override
            public boolean mouseEntered(JSpriteMouseEvent m) {
            	System.err.println("the mouse has entered");
            	return true;
            }

            @Override
            public boolean mouseExited(JSpriteMouseEvent m) {
                myRectangle.setCurrentVisual(0);
                canvas.repaint();
                return true;
            }

            @Override
            public boolean mousePressed(JSpriteMouseEvent m) {
                System.err.println("Mouse pressed");
                myRectangle.setCurrentVisual(1);
                canvas.repaint();
                return true;
            }

            @Override
            public boolean mouseReleased(JSpriteMouseEvent m) {
                myRectangle.setCurrentVisual(0);
                canvas.repaint();
                return true;
            }

            @Override
            public boolean mouseDragged(JSpriteMouseEvent m) {
                return false;
            }

            @Override
            public boolean mouseMoved(JSpriteMouseEvent m) {
                return false;
            }
        });
        canvas.addSprite(myRectangle, 0);
        this.add(canvas);
        this.pack();
        this.toFront();
        this.setVisible(true);
    }
}
