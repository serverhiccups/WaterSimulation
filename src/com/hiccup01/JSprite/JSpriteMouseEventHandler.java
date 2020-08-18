package com.hiccup01.JSprite;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * The JSpriteMouseEventHandler class handles all the mouse events on the JSpriteCanvas and delivers them to the appropriate sprites.
 */
public class JSpriteMouseEventHandler implements MouseListener, MouseMotionListener {
	JSpriteCanvas canvas; // The canvas that the sprites are on.
	private JSprite currentSprite; // The sprite the mouse is current over.
	private JSprite primaryPressed; // The sprite the mouse was over when the primary mouse button was pressed.
	private JSprite secondaryPressed; // The sprite the mouse was over when the secondary mouse button was pressed.
	private JSprite tertiaryPressed; // The sprite the mouse was over when the tertiary mouse button was pressed.

	/**
	 * Creates a JSpriteMouseEventHandler.
	 * @param c The canvas.
	 */
	public JSpriteMouseEventHandler(JSpriteCanvas c) {
		this.canvas = c;
	}

	/**
	 * Finds the topmost sprite visible at a given position.
     * The coordinates are in the 'virtual' coordinate space.
	 * @param x The X position.
	 * @param y The Y position.
	 * @return The sprite if one was found, or null if one wasn't.
	 */
	public JSprite findSpriteAt(int x, int y) { // x and y are virtual coordinates.
	    // We have use a list iterator in order to iterate through the list in reverse.
		ListIterator<JSpriteContainer> li = this.canvas.spriteStack.listIterator(this.canvas.spriteStack.size());
		while(li.hasPrevious()) { // While there's another sprite to check.
			JSpriteContainer c = li.previous(); // Get it.
			JSprite s = c.sprite;
			JSpriteVisual visual = s.getVisual(s.getCurrentVisual()); // Get its visual
			// If we're in bounds, we have found the sprite.
			if(visual.isInBounds(x - s.xPosition + visual.getXOffset(), y - s.yPosition + visual.getYOffset()) && s.visible) {
				return s;
			}
		}
		return null; // We didn't find anything.
	}

	/**
	 * Finds all the sprites, in their sprite containers, at a given position.
	 * This is method is similar to findSpriteAt, but this one returns *all* the sprites.
	 * @param x The X position.
	 * @param y The Y position.
	 * @return An ArrayList of the the sprites we found, in their sprite containers. Empty if we didn't find anything.
	 */
	public ArrayList<JSpriteContainer> findSpriteContainersAt(int x, int y) {
		ArrayList<JSpriteContainer> list = new ArrayList<>(); // Create the list that we are going to return.
		ListIterator<JSpriteContainer> li = this.canvas.spriteStack.listIterator(this.canvas.spriteStack.size()); // Reverse iterator.
		while(li.hasPrevious()) { // While there's another sprite to check.
			JSpriteContainer c = li.previous(); // Get it.
			JSprite s = c.sprite;
			JSpriteVisual visual = s.getVisual(s.getCurrentVisual()); // Get its visual.
			// We we're in bounds, add the sprite to the list.
			if(visual.isInBounds(x - s.xPosition + visual.getXOffset(), y - s.yPosition + visual.getYOffset()) && s.visible) {
				list.add(c);
			}
		}
		return list; // Return the list.
	}

	/**
	 * Creates enter and exit events for the sprites. The mouseEnter and mouseExit events we receive on the canvas are just for the edges of the canvas, so we have to create our own that work on sprite boundaries.
	 * @param e The mouseEvent to use for the mouse position.
	 */
	private void createEnterExitEvents(MouseEvent e) {
		JSprite oldCurrent = this.currentSprite; // The sprite we were just on.
		// The sprite that we are on now.
		JSprite newCurrent = this.findSpriteAt(e.getX() + this.canvas.virtualX, e.getY() + this.canvas.virtualY);
		if(oldCurrent != newCurrent) { // If they aren't the same, we must have exited one sprite and moved to another.
			// Deliver the events.
			this.deliverEvent(JSpriteMouseEventType.MOUSE_EXIT, e, oldCurrent);
			this.deliverEvent(JSpriteMouseEventType.MOUSE_ENTER, e, newCurrent);
			this.currentSprite = newCurrent; // Change the sprite that were just on.
		} else return;
	}

	/**
	 * Transforms the AWT MouseEvents that we receive, to easier to use JSpriteMouseEvents.
	 * @param m The AWT MouseEvent.
	 * @param t The type of the event (eg. click, mouse move, etc.).
	 * @param relativeToX The X position of the sprite that this event is relative to.
	 * @param relativeToY The Y positon of the sprite that this event is relative to.
	 * @return The JSpriteMouseEvent that was created.
	 */
	private JSpriteMouseEvent transformMouseEvent(MouseEvent m, JSpriteMouseEventType t, int relativeToX, int relativeToY) {
	    // Transform the button type of the event.
		JSpriteButtonType b = this.transformButtonType(m.getButton());
		// Physical X and Y
		int pX = m.getX();
		int pY = m.getY();
		// Virtual X and Y
		int vX = pX + this.canvas.virtualX;
		int vY = pY + this.canvas.virtualY;
		int rX = vX - relativeToX;
		// Relative X and Y
		int rY = vY - relativeToY;
		return new JSpriteMouseEvent(b, JSpriteCoordinateType.RELATIVE, m.getClickCount(), m.isControlDown(), m.isAltDown(), m.isShiftDown(), m.isPopupTrigger(), pX, pY, rX, rY, vX, vY);
	}

	/**
	 * Tranforms an integer button number in to a JSpriteButtonType.
	 * @param button The AWT id of the button.
	 * @return The JSpriteButtonType of the button.
	 */
	private JSpriteButtonType transformButtonType(int button) {
		switch (button) {
			case MouseEvent.BUTTON1:
				return JSpriteButtonType.PRIMARY;
			case MouseEvent.BUTTON3: // Button 3 is actually right click
//				System.err.println("Converting to a secondary event");
				return JSpriteButtonType.SECONDARY;
			case MouseEvent.BUTTON2: // Button 2 is middle click
				return JSpriteButtonType.TERTIARY;
			default:
				return JSpriteButtonType.NONE;
		}
	}

	/**
	 * Transforms the buttons currently pressed down from a MouseEvent into a JSpriteButtonType.
	 * @param e The MouseEvent.
	 * @return The JSpriteButtonType of the button.
	 */
	private JSpriteButtonType transformPressedButtonType(MouseEvent e) {
		// Is the primary button down?
	    if((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == MouseEvent.BUTTON1_DOWN_MASK) {
			return JSpriteButtonType.PRIMARY;
		} else if((e.getModifiersEx() & MouseEvent.BUTTON2_DOWN_MASK) == MouseEvent.BUTTON2_DOWN_MASK) { // Button 2 is middle click
			return JSpriteButtonType.TERTIARY;
		} else if((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) == MouseEvent.BUTTON3_DOWN_MASK) { // Button 3 is actually right click
			return JSpriteButtonType.SECONDARY;
		}
	    return JSpriteButtonType.NONE;
	}

	/**
	 * Calls the appropriate handlers for a sprite on a mouse event.
	 * @param s The sprite the event is for.
	 * @param t The type of the mouse event.
	 * @param e The mouse event itself.
	 * @return The sprite that the event was called on eventually.
	 */
	private JSprite cascadeEvent(JSprite s, JSpriteMouseEventType t, JSpriteMouseEvent e) {
		// Reverse iterator through the sprite's mouse handlers.
		ListIterator<JSpriteMouseHandler> li = s.mouseHandlers.listIterator(s.mouseHandlers.size());
		// While there's another handelr to call.
		while(li.hasPrevious()) {
			JSpriteMouseHandler next = li.previous(); // Get it.
			//					System.err.println("Delivering move event to sprite");
			// Call the appropriate handler for the kind of event.
			JSpriteMouseEventDelegate status = null;
			switch (t) {
				case MOUSE_CLICK:
					status = next.mouseClicked(e);
					break;
				case MOUSE_ENTER:
					status = next.mouseEntered(e);
					break;
				case MOUSE_EXIT:
					status = next.mouseExited(e);
					break;
				case MOUSE_PRESS:
					status = next.mousePressed(e);
					break;
				case MOUSE_RELEASE:
					status = next.mouseReleased(e);
					break;
				case MOUSE_MOVE:
					status = next.mouseMoved(e);
					break;
				case MOUSE_DRAG:
					status = next.mouseDragged(e);
					break;
			}
			if(status == null) status = new JSpriteMouseEventDelegate(false); // If status is null, we can assume that the handler would want the .CONTINUE enum variant.
			if(status.isComplete()) { // If the handler says we're done, stop calling handlers.
//				if(e.buttonType == JSpriteButtonType.SECONDARY) System.err.println("Got a COMPLETE delegate status on a secondary press");
				return s; // Return the sprite.
			}
			if(status.getDelegate() != null) { // The mouse handler wanted us to 'delegate' the event to a different sprite.
				return this.cascadeEvent(status.getDelegate(), t, e); // Recursively cascade the event on that sprite instead.
			}
		}
		return s;
	}

	/**
	 * A helper method a deliverEvent for when you don't know which sprite the event was on.
	 * @param t The type of the event.
	 * @param m The AWT MouseEvent.
	 * @return The JSprite the event was called on.
	 */
	private JSprite deliverEvent(JSpriteMouseEventType t, MouseEvent m) {
		JSprite at = this.findSpriteAt(m.getX() + this.canvas.virtualX, m.getY() + this.canvas.virtualY); // Find he spriet at that position.
		return this.deliverEvent(t, m, at);
	}

	private JSprite deliverEvent(JSpriteMouseEventType t, MouseEvent m, JSprite at) {
//		System.err.println("Delivering an event");
		if(at == null) { // If there's no sprite where are trying to deliver the event, try to use the canvas's default handler.
//			System.err.println("Using the default handler");
			if(this.canvas.defaultHandler != null) { // Make sure there is a default handler.
				// Create the JSpriteMouseEvent we need.
				JSpriteMouseEvent e = this.transformMouseEvent(m, t, 0, 0);
				switch (t) { // Deliver the appropriate type of event.
					case MOUSE_CLICK:
						this.canvas.defaultHandler.mouseClicked(e);
						break;
					case MOUSE_ENTER:
						this.canvas.defaultHandler.mouseEntered(e);
						break;
					case MOUSE_EXIT:
						this.canvas.defaultHandler.mouseExited(e);
						break;
					case MOUSE_PRESS:
						this.canvas.defaultHandler.mousePressed(e);
						break;
					case MOUSE_RELEASE:
						this.canvas.defaultHandler.mouseReleased(e);
						break;
					case MOUSE_MOVE:
						this.canvas.defaultHandler.mouseMoved(e);
						break;
					case MOUSE_DRAG:
						this.canvas.defaultHandler.mouseDragged(e);
						break;
				}
				return at;
			}
			return at;
		}
		// Ww're not using the default handler, so create the mouse event on the sprite that we are using.
		JSpriteMouseEvent e = this.transformMouseEvent(m, t, at.xPosition - at.getVisual(at.getCurrentVisual()).getXOffset(), at.yPosition - at.getVisual(at.getCurrentVisual()).getYOffset());
		// Cascade the event to the sprite.
		return this.cascadeEvent(at, t, e);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		this.deliverEvent(JSpriteMouseEventType.MOUSE_CLICK, e); // Deliver the click event.
	}

	@Override
	public void mousePressed(MouseEvent e) {
	    // Figure out which button was pressed.
		JSpriteButtonType b = this.transformButtonType(e.getButton());
		// We have to remember which sprite the button was pressed on, so that we can deliver the corresponding mouseReleased event.
		switch (b) {
			case PRIMARY:
				this.primaryPressed = this.deliverEvent(JSpriteMouseEventType.MOUSE_PRESS, e);
				break;
			case SECONDARY:
				this.secondaryPressed = this.deliverEvent(JSpriteMouseEventType.MOUSE_PRESS, e);
				break;
			case TERTIARY:
				this.tertiaryPressed = this.deliverEvent(JSpriteMouseEventType.MOUSE_PRESS, e);
				break;
			default:
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	    // Here we ignore the position of the actual release, because we are deliverign the event to the sprite that was originally clicked on.
		JSpriteButtonType b = this.transformButtonType(e.getButton());
		JSpriteMouseEvent m;
		switch (b) {
			case PRIMARY:
				this.deliverEvent(JSpriteMouseEventType.MOUSE_RELEASE, e, this.primaryPressed);
				break;
			case SECONDARY:
				this.deliverEvent(JSpriteMouseEventType.MOUSE_RELEASE, e, this.secondaryPressed);
				break;
			case TERTIARY:
				this.deliverEvent(JSpriteMouseEventType.MOUSE_RELEASE, e, this.tertiaryPressed);
				break;
			default:
				System.err.println("Mouse release default");
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// Because this fires on canvas boundaries and not on sprite boundaries, I'm not sure how useful this is.
		this.createEnterExitEvents(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// Because this fires on canvas boundaries and not on sprite boundaries, I'm not sure how useful this is.
		this.createEnterExitEvents(e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		JSpriteButtonType b = this.transformPressedButtonType(e);
		switch (b) {
			case PRIMARY:
				this.deliverEvent(JSpriteMouseEventType.MOUSE_DRAG, e, this.primaryPressed);
				break;
			case SECONDARY:
				this.deliverEvent(JSpriteMouseEventType.MOUSE_DRAG, e, this.secondaryPressed);
				break;
			case TERTIARY:
				System.out.println("Tertiary drag?");
				this.deliverEvent(JSpriteMouseEventType.MOUSE_DRAG, e, this.tertiaryPressed);
				break;
			default:
				System.out.println("Default case?????");
		}
		this.createEnterExitEvents(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// Here we have to do some extra processing here so that we can create mouseEntered and mouseExited events that work on sprite boundaries.
		this.createEnterExitEvents(e);
		this.deliverEvent(JSpriteMouseEventType.MOUSE_MOVE, e);
	}
}
