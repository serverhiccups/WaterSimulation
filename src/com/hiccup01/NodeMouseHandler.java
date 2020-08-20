package com.hiccup01;

import com.hiccup01.JSprite.*;

import javax.swing.*;

/**
 * NodeMouseHandler handles what happens when you use your mouse on a node.
 */
public class NodeMouseHandler implements JSpriteMouseHandler {
	Node self = null; // The node itself.
	NetworkManager networkManager = null;

	public NodeMouseHandler(Node self, NetworkManager networkManager) {
		this.self = self;
		this.networkManager = networkManager;
	}

	private int clamp(int val, int min, int max) {
		return Math.max(min, Math.min(max, val));
	}

	/**
	 * Show a series of dialogs that lets the user edit this Node.
	 */
	private void displayEditDialog() {
		Object[] options = {
				"Cancel",
				"Change " + this.self.type.toString() + " Capacity",
				"Delete this " + this.self.type.toString()
		};
		// Show the command selection dialog.
		int choice = JOptionPane.showOptionDialog(this.networkManager.frame,
				"What would you like to do?",
				"Edit " + this.self.type.toString(),
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null, options, options[0]);
		switch(choice) {
			case 0: // Cancel
				return;
			case 1: // Change capacity
				if(this.self.type == NodeType.JUNCTION) {
					// You can't change the capacity of a junction, because junctions don't have capacity.
					JOptionPane.showMessageDialog(this.networkManager.frame, "You cannot change the capacity of a Junction", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				// Ask the user for a capacity.
				String capacity = (String)JOptionPane.showInputDialog(this.networkManager.frame, "Please enter the new capacity:", "New Capacity", JOptionPane.QUESTION_MESSAGE, null, null, this.self.capacity);
				if(capacity == null || capacity.length() == 0) { // Make sure that the user entered something.
					JOptionPane.showMessageDialog(this.networkManager.frame, "Please enter a valid capacity (A whole number above 0)", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				int capNumber = 0;
				try {
					capNumber = Integer.parseInt(capacity); // Try to turn what the user entered into a capacity.
					if(capNumber < 0) throw new Exception();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(this.networkManager.frame, "Please enter a valid capacity (A whole number above 0)", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				this.self.capacity = capNumber; // The the node capacity.
				return;
			case 2: // Delete this node
				// Make sure the user wants to delete this node.
				int confirm = JOptionPane.showConfirmDialog(this.networkManager.frame, "Are you sure?", "Confirm", JOptionPane.YES_NO_OPTION);
				if(confirm == 0) { // Yes
					this.networkManager.removeNode(this.self);
				} else return;
		}
	}

	@Override
	public JSpriteMouseEventDelegate scrollEvent(int amount) {
		return null;
	}

	@Override
	public JSpriteMouseEventDelegate mouseClicked(JSpriteMouseEvent m) {
		this.displayEditDialog(); // Show the command dialog.
		this.networkManager.updateView();
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
		if(m.buttonType == JSpriteButtonType.SECONDARY) { // Right click means that the user is trying to link pipes.
			return new JSpriteMouseEventDelegate(this.networkManager.getPipePreview(m.getX(JSpriteCoordinateType.VIRTUAL), m.getY(JSpriteCoordinateType.VIRTUAL), this.self));
		}
		return JSpriteMouseEventDelegate.COMPLETED;
	}

	@Override
	public JSpriteMouseEventDelegate mouseReleased(JSpriteMouseEvent m) {
		return null;
	}

	@Override
	public JSpriteMouseEventDelegate mouseDragged(JSpriteMouseEvent m) { // Allow the user to move the node.
		this.self.x = this.clamp(m.getX(JSpriteCoordinateType.VIRTUAL), 38, 800 - 38);
		this.self.y = this.clamp(m.getY(JSpriteCoordinateType.VIRTUAL), 48 + 38, 640 - 38);
		try {
			// This might be hella slow
			this.networkManager.canvas.sendToFront(this.self.spriteContainer.id);
		} catch (Exception e) {
			System.err.println("Failed to send the currently dragged junction to the front");
		}
		this.networkManager.updateView();
		return JSpriteMouseEventDelegate.COMPLETED;
	}

	@Override
	public JSpriteMouseEventDelegate mouseMoved(JSpriteMouseEvent m) {
		return null;
	}
}
