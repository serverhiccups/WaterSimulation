package com.hiccup01;

import com.hiccup01.JSprite.*;

import javax.swing.*;

/**
 * PipeMouseHandler handles what happens when you click on a pipe to edit it.
 */
public class PipeMouseHandler implements JSpriteMouseHandler {
	Pipe self = null;
	NetworkManager networkManager = null;

	public PipeMouseHandler(Pipe self, NetworkManager networkManager) {
		this.self = self;
		this.networkManager = networkManager;
	}

	/**
	 * Checks whether or not the coordinates (in relative space) are on the text part of the line.
	 * @param m The mouse event to check.
	 * @return Whether or not the event was on the text.
	 */
	private boolean onText(JSpriteMouseEvent m) {
		int x = m.getX(JSpriteCoordinateType.RELATIVE); // Get the coordinates.
		int y = m.getY(JSpriteCoordinateType.RELATIVE);
		// stack is the visual belonging the pipe.
		JSpriteVisualStack stack = ((JSpriteVisualStack)this.self.spriteContainer.sprite.getVisual(this.self.spriteContainer.sprite.getCurrentVisual()));
		int width = stack.getWidth();
		int height = stack.getHeight();
		// The box is 54x24 pixels. boxX and boxY are for the top left corner.
		int boxX = (width / 2) - (54 / 2);
		int boxY = (height / 2) - (24 / 2);
		if((x >= boxX && x <= boxX + 54) && (y >= boxY && y <= boxY + 24)) { // Check the bounds.
			return true;
		}
		return false;
	}

	/**
	 * Shows a dialog to edit this pipe.
	 */
	private void displayEditDialog() {
		Object[] options = {
				"Cancel",
				"Change Capacity",
				"Delete this pipe"
		};
		// Show the option dialog.
		int choice = JOptionPane.showOptionDialog(this.networkManager.frame, "What would you like to do?", "Edit Pipe", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		switch (choice) { // Do something based on the choice.
			case 0: // Cancel
				return;
			case 1: // Change Capacity
				String capacity = (String)JOptionPane.showInputDialog(this.networkManager.frame, "Please enter the new capacity:", "New Capacity", JOptionPane.QUESTION_MESSAGE, null, null, this.self.maxCapacity);
				if(capacity == null || capacity.length() == 0) { // Make sure that they entered something.
					JOptionPane.showMessageDialog(this.networkManager.frame, "Please enter a valid capacity (A whole number above 0)", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				int capNumber = 0;
				try {
					capNumber = Integer.parseInt(capacity); // Try to turn what they entered into an integer
					if(capNumber < 0) throw new Exception();
				} catch (Exception e) { // What they entered wasn't valid.
					// Tell the user about it.
					JOptionPane.showMessageDialog(this.networkManager.frame, "Please enter a valid capacity (A whole number above 0)", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				this.self.maxCapacity = capNumber;
				return;
			case 2: // Delete this pipe
				// Make the user confirm their action.
				int confirm = JOptionPane.showConfirmDialog(this.networkManager.frame, "Are you sure?", "Confirm", JOptionPane.YES_NO_OPTION);
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
		if(this.onText(m)) { // If they clicked on the text box.
			this.displayEditDialog(); // Show the edit dialog.
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
