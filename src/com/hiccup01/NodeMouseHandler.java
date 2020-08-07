package com.hiccup01;

import com.hiccup01.JSprite.*;

import javax.swing.*;

public class NodeMouseHandler implements JSpriteMouseHandler {
	Node self = null;
	NetworkManager networkManager = null;

	public NodeMouseHandler(Node self, NetworkManager networkManager) {
		this.self = self;
		this.networkManager = networkManager;
	}

	private int clamp(int val, int min, int max) {
		return Math.max(min, Math.min(max, val));
	}

	private void displayEditDialog() {
		Object[] options = {
				"Cancel",
				"Change " + this.self.type.toString() + " Capacity",
				"Delete this " + this.self.type.toString()
		};
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
					JOptionPane.showMessageDialog(this.networkManager.frame, "You cannot change the capacity of a Junction", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				String capacity = (String)JOptionPane.showInputDialog(this.networkManager.frame, "Please enter the new capacity:", "New Capacity", JOptionPane.QUESTION_MESSAGE, null, null, this.self.capacity);
				if(capacity == null || capacity.length() == 0) {
					JOptionPane.showMessageDialog(this.networkManager.frame, "Please enter a valid capacity (A whole number above 0)", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				int capNumber = 0;
				try {
					capNumber = Integer.parseInt(capacity);
					if(capNumber < 0) throw new Exception();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(this.networkManager.frame, "Please enter a valid capacity (A whole number above 0)", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				this.self.capacity = capNumber;
				return;
			case 2: // Delete this node
				int confirm = JOptionPane.showConfirmDialog(this.networkManager.frame, "Are you sure?", "Confirm", JOptionPane.YES_NO_OPTION);
				System.out.println("confirm option was " + confirm);
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
		this.displayEditDialog();
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
		System.err.println("Node got a mouse press");
		if(m.buttonType == JSpriteButtonType.SECONDARY) { // The user is trying to link pipes.
			System.err.println("Creating a PipePreview");
			return new JSpriteMouseEventDelegate(this.networkManager.getPipePreview(m.getX(JSpriteCoordinateType.VIRTUAL), m.getY(JSpriteCoordinateType.VIRTUAL), this.self));
		}
		return JSpriteMouseEventDelegate.COMPLETED;
	}

	@Override
	public JSpriteMouseEventDelegate mouseReleased(JSpriteMouseEvent m) {
		return null;
	}

	@Override
	public JSpriteMouseEventDelegate mouseDragged(JSpriteMouseEvent m) {
//		this.self.x = m.getX(JSpriteCoordinateType.VIRTUAL);
//		this.self.y = m.getY(JSpriteCoordinateType.VIRTUAL);
		if(m.buttonType == JSpriteButtonType.PRIMARY) {
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
		return JSpriteMouseEventDelegate.COMPLETED;
	}

	@Override
	public JSpriteMouseEventDelegate mouseMoved(JSpriteMouseEvent m) {
		return null;
	}
}
