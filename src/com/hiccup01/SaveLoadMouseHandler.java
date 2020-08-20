package com.hiccup01;

import com.hiccup01.JSprite.JSpriteMouseEvent;
import com.hiccup01.JSprite.JSpriteMouseEventDelegate;
import com.hiccup01.JSprite.JSpriteMouseHandler;

import javax.swing.*;
import java.io.File;

/**
 * SaveLoadMouseHandler handles the GUI for saving and loading.
 */
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
            JFileChooser chooser = new JFileChooser(); // Create the file chooser
            chooser.setDialogTitle("Select a network to load"); // Set the title.
            int selection = chooser.showOpenDialog(this.manager.canvas); // Open the dialog

            if(selection == JFileChooser.APPROVE_OPTION) { // If we didn't cancel.
                File loadLocation = chooser.getSelectedFile(); // Get the file to load.
                try {
                    this.manager.deserialise(loadLocation.getAbsolutePath()); // Deserialise the network from the file.
                    this.manager.updateView(); // Show the new network.
                } catch (Exception e) {
                    System.err.println("Failed to load. Did you choose a valid network?");
                }
            }
        } else { // We are saving
            JFileChooser chooser = new JFileChooser(); // Create the file chooser.
            chooser.setDialogTitle("Where should I save"); // Set the title.
            int selection = chooser.showSaveDialog(this.manager.frame);

            if(selection == JFileChooser.APPROVE_OPTION) { // If we didn't cancel
                File saveLocation = chooser.getSelectedFile(); // Get the file path to save to.
                try {
                    this.manager.serialise(saveLocation.getAbsolutePath()); // Serialise into path.
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
