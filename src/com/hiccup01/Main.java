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
        JSprite myRectangle = new JSprite(100, 100, new JSpriteCostume("rect.png"));
        myRectangle.getVisual(0).setOffsetMode(JSpriteOffsetMode.CENTER);
        JSpriteMouseHandler mouseHandle = new JSpriteMouseHandler() {
            @Override
            public boolean scrollEvent(int amount) {
                return false;
            }

            @Override
            public boolean mouseClicked(JSpriteMouseEvent m) {
                return false;
            }

            @Override
            public boolean mouseEntered(JSpriteMouseEvent m) {
                System.err.println("Entering the sprite");
                return true;
            }

            @Override
            public boolean mouseExited(JSpriteMouseEvent m) {
                return false;
            }

            @Override
            public boolean mousePressed(JSpriteMouseEvent m) {
                return false;
            }

            @Override
            public boolean mouseReleased(JSpriteMouseEvent m) {
                return false;
            }

            @Override
            public boolean mouseDragged(JSpriteMouseEvent m) {
	            return false;
            }

            @Override
            public boolean mouseMoved(JSpriteMouseEvent m) {
                myRectangle.xPosition = m.getX(JSpriteCoordinateType.VIRTUAL);
                myRectangle.yPosition = m.getY(JSpriteCoordinateType.VIRTUAL);
                canvas.repaint();
                return true;
//	            return false;
            }
        };
        myRectangle.addMouseHandler(mouseHandle);
        canvas.setDefaultMouseHandler(new JSpriteMouseHandler() {

            @Override
            public boolean scrollEvent(int amount) {
                return false;
            }

            @Override
            public boolean mouseClicked(JSpriteMouseEvent m) {
                return false;
            }

            @Override
            public boolean mouseEntered(JSpriteMouseEvent m) {
                System.err.println("Mouse entered canvas");
                return true;
            }

            @Override
            public boolean mouseExited(JSpriteMouseEvent m) {
                return false;
            }

            @Override
            public boolean mousePressed(JSpriteMouseEvent m) {
                return false;
            }

            @Override
            public boolean mouseReleased(JSpriteMouseEvent m) {
                return false;
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
