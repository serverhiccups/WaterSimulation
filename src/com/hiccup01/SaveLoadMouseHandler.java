package com.hiccup01;

import com.hiccup01.JSprite.JSpriteMouseEvent;
import com.hiccup01.JSprite.JSpriteMouseEventDelegate;
import com.hiccup01.JSprite.JSpriteMouseHandler;

import javax.swing.*;
import java.io.File;

public class SaveLoadMouseHandler implements JSpriteMouseHandler {
    private boolean isLoad;
    private NetworkManager manager = null;

    public SaveLoadMouseHandler(NetworkManager manager, boolean isLoad) {
        this.manager = manager;
        this.isLoad = isLoad;
    }

    @Override
    public JSpriteMouseEventDelegate scrollEvent(int amount) {
        return null;
    }

    @Override
    public JSpriteMouseEventDelegate mouseClicked(JSpriteMouseEvent m) {
        if(isLoad) { // We are loading
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Select a network to load");
            int selection = chooser.showOpenDialog(this.manager.canvas);

            if(selection == JFileChooser.APPROVE_OPTION) {
                File loadLocation = chooser.getSelectedFile();
                try {
                    this.manager.deserialise(loadLocation.getAbsolutePath());
                    this.manager.updateView();
                } catch (Exception e) {
                    System.err.println("Failed to load. Did you choose a valid network?");
                }
            }
        } else { // We are saving
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Where should I save");
            int selection = chooser.showSaveDialog(this.manager.frame);

            if(selection == JFileChooser.APPROVE_OPTION) {
                File saveLocation = chooser.getSelectedFile();
                try {
                    this.manager.serialise(saveLocation.getAbsolutePath());
                    this.manager.updateView();
                } catch (Exception e) {
                    System.err.println("Failed to save. Did you choose a write protected location?");
                }
            }
        }
        return JSpriteMouseEventDelegate.COMPLETED;
    }

    @Override
    public JSpriteMouseEventDelegate mouseEntered(JSpriteMouseEvent m) {
        return null;
    }

    @Override
    public JSpriteMouseEventDelegate mouseExited(JSpriteMouseEvent m) {
        return null;
    }

    @Override
    public JSpriteMouseEventDelegate mousePressed(JSpriteMouseEvent m) {
        return null;
    }

    @Override
    public JSpriteMouseEventDelegate mouseReleased(JSpriteMouseEvent m) {
        return null;
    }

    @Override
    public JSpriteMouseEventDelegate mouseDragged(JSpriteMouseEvent m) {
        return null;
    }

    @Override
    public JSpriteMouseEventDelegate mouseMoved(JSpriteMouseEvent m) {
        return null;
    }
}
