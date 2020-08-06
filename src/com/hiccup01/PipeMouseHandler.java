package com.hiccup01;

import com.hiccup01.JSprite.*;

import javax.swing.*;

public class PipeMouseHandler implements JSpriteMouseHandler {
	Pipe self = null;
	NetworkManager networkManager = null;

	public PipeMouseHandler(Pipe self, NetworkManager networkManager) {
		this.self = self;
		this.networkManager = networkManager;
	}

	private boolean onText(JSpriteMouseEvent m) {
		int x = m.getX(JSpriteCoordinateType.RELATIVE);
		int y = m.getY(JSpriteCoordinateType.RELATIVE);
		System.out.println("x is " + x + " and y is " + y);
		JSpriteVisualStack stack = ((JSpriteVisualStack)this.self.spriteContainer.sprite.getVisual(this.self.spriteContainer.sprite.getCurrentVisual()));
		int width = stack.getWidth();
		int height = stack.getHeight();
		// The box is 54x24 pixels.
		int boxX = (width / 2) - (54 / 2);
		int boxY = (height / 2) - (24 / 2);
		if((x >= boxX && x <= boxX + 54) && (y >= boxY && y <= boxY + 24)) {
			return true;
		}
		return false;
	}

	private void displayEditDialog() {
		Object[] options = {
				"Cancel",
				"Change Capacity",
				"Delete this pipe"
		};
		int choice = JOptionPane.showOptionDialog(this.networkManager.frame, "What would you like to do?", "Edit Pipe", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		System.out.println("Option number " + choice);
		switch (choice) {
			case 0: // Cancel
				return;
			case 1: // Change Capacity
				String capacity = (String)JOptionPane.showInputDialog(this.networkManager.frame, "Please enter the new capacity:", "New Capacity", JOptionPane.QUESTION_MESSAGE, null, null, this.self.maxCapacity);
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
				this.self.maxCapacity = capNumber;
				return;
			case 2: // Delete this pipe
				int confirm = JOptionPane.showConfirmDialog(this.networkManager.frame, "Are you sure?", "Confirm", JOptionPane.YES_NO_OPTION);
				System.out.println("confirm option was " + confirm);
				if(confirm == 0) { // Yes
					this.networkManager.removePipe(this.self);
				} else return;
		}
	}

	@Override
	public JSpriteMouseEventDelegate scrollEvent(int amount) {
		return null;
	}

	@Override
	public JSpriteMouseEventDelegate mouseClicked(JSpriteMouseEvent m) {
		if(this.onText(m)) {
			this.displayEditDialog();
			this.networkManager.updateView();
			return JSpriteMouseEventDelegate.COMPLETED;
		}
		return JSpriteMouseEventDelegate.CONTINUE;
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
