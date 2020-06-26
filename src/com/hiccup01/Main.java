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
        JSpriteLine lv = new JSpriteLine(-1 * Math.PI / (float)4, 200);
        lv.setThickness((float)10);
        JSprite line = new JSprite(0, 0, lv);
        line.addMouseHandler(new JSpriteMouseHandler() {
            @Override
            public boolean scrollEvent(int amount) {
                return false;
            }

            @Override
            public boolean mouseClicked(JSpriteMouseEvent m) {
                System.err.println("clicked on");
                return true;
            }

            @Override
            public boolean mouseEntered(JSpriteMouseEvent m) {
                return false;
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
        canvas.addSprite(line, 0);
        this.add(canvas);
        this.pack();
        this.toFront();
        this.setVisible(true);
    }
}
