package com.hiccup01.JSprite;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.ListIterator;

public class JSpriteMouseEventHandler implements MouseListener, MouseMotionListener {
	JSpriteCanvas canvas;
	private JSprite currentSprite;
	private JSprite primaryPressed;
	private JSprite secondaryPressed;
	private JSprite tertiaryPressed;

	public JSpriteMouseEventHandler(JSpriteCanvas c) {
		this.canvas = c;
	}

	// x and y are virtual coordinates
	public JSprite findSpriteAt(int x, int y) {
//		System.err.println("Finding sprite at " + x + ", " + y);
		ListIterator<JSpriteContainer> li = this.canvas.spriteStack.listIterator(this.canvas.spriteStack.size());
		while(li.hasPrevious()) {
			JSpriteContainer c = li.previous();
			JSprite s = c.sprite;
			JSpriteVisual visual = s.getVisual(s.getCurrentVisual());
			if(visual.isInBounds(x - s.xPosition + visual.getXOffset(), y - s.yPosition + visual.getYOffset()) && s.visible) {
				return s;
			}
		}
		return null;
	}

	public ArrayList<JSpriteContainer> findSpriteContainersAt(int x, int y) {
		ArrayList<JSpriteContainer> list = new ArrayList<>();

		ListIterator<JSpriteContainer> li = this.canvas.spriteStack.listIterator(this.canvas.spriteStack.size());
		while(li.hasPrevious()) {
			JSpriteContainer c = li.previous();
			JSprite s = c.sprite;
			JSpriteVisual visual = s.getVisual(s.getCurrentVisual());
			if(visual.isInBounds(x - s.xPosition + visual.getXOffset(), y - s.yPosition + visual.getYOffset()) && s.visible) {
				list.add(c);
			}
		}
		return list;
	}

	private void createEnterExitEvents(MouseEvent e) {
		JSprite oldCurrent = this.currentSprite;
		JSprite newCurrent = this.findSpriteAt(e.getX() + this.canvas.virtualX, e.getY() + this.canvas.virtualY);
		if(oldCurrent != newCurrent) {
			this.deliverEvent(JSpriteMouseEventType.MOUSE_EXIT, e, oldCurrent);
			this.deliverEvent(JSpriteMouseEventType.MOUSE_ENTER, e, newCurrent);
			this.currentSprite = newCurrent;
		} else return;
	}

	private JSpriteMouseEvent transformMouseEvent(MouseEvent m, JSpriteMouseEventType t, int relativeToX, int relativeToY) {
		JSpriteButtonType b = this.transformButtonType(m.getButton());
		int pX = m.getX();
		int pY = m.getY();
		int vX = pX + this.canvas.virtualX;
		int vY = pY + this.canvas.virtualY;
		int rX = vX - relativeToX;
		int rY = vY - relativeToY;
		return new JSpriteMouseEvent(b, JSpriteCoordinateType.RELATIVE, m.getClickCount(), m.isControlDown(), m.isAltDown(), m.isShiftDown(), m.isPopupTrigger(), pX, pY, rX, rY, vX, vY);
	}

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

	private JSprite cascadeEvent(JSprite s, JSpriteMouseEventType t, JSpriteMouseEvent e) {
		ListIterator<JSpriteMouseHandler> li = s.mouseHandlers.listIterator(s.mouseHandlers.size());
		while(li.hasPrevious()) {
			JSpriteMouseHandler next = li.previous();
			//					System.err.println("Delivering move event to sprite");
			JSpriteMouseEventDelegate status = switch (t) {
				case MOUSE_CLICK -> next.mouseClicked(e);
				case MOUSE_ENTER -> next.mouseEntered(e);
				case MOUSE_EXIT -> next.mouseExited(e);
				case MOUSE_PRESS -> next.mousePressed(e);
				case MOUSE_RELEASE -> next.mouseReleased(e);
				case MOUSE_MOVE -> next.mouseMoved(e);
				case MOUSE_DRAG -> next.mouseDragged(e);
			};
			if(status == null) status = new JSpriteMouseEventDelegate(false);
			if(status.isComplete()) {
//				if(e.buttonType == JSpriteButtonType.SECONDARY) System.err.println("Got a COMPLETE delegate status on a secondary press");
				return s;
			}
			if(status.getDelegate() != null) {
				System.err.println("A delegation happened!");
				this.cascadeEvent(status.getDelegate(), t, e);
				return status.getDelegate();
			}
		}
		return s;
	}

	private JSprite deliverEvent(JSpriteMouseEventType t, MouseEvent m) {
		JSprite at = this.findSpriteAt(m.getX() + this.canvas.virtualX, m.getY() + this.canvas.virtualY);
		return this.deliverEvent(t, m, at);
	}

	private JSprite deliverEvent(JSpriteMouseEventType t, MouseEvent m, JSprite at) {
//		System.err.println("Delivering an event");
		if(at == null) {
//			System.err.println("Using the default handler");
			if(this.canvas.defaultHandler != null) {
				JSpriteMouseEvent e = this.transformMouseEvent(m, t, 0, 0);
				switch (t) {
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
//						System.err.println("Delivering a move event to the default handler");
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
		JSpriteMouseEvent e = this.transformMouseEvent(m, t, at.xPosition - at.getVisual(at.getCurrentVisual()).getXOffset(), at.yPosition - at.getVisual(at.getCurrentVisual()).getYOffset());
		return this.cascadeEvent(at, t, e);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		this.deliverEvent(JSpriteMouseEventType.MOUSE_CLICK, e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		System.err.println("getButton is " + e.getButton());
		JSpriteButtonType b = this.transformButtonType(e.getButton());
		switch (b) {
			case PRIMARY:
				this.primaryPressed = this.deliverEvent(JSpriteMouseEventType.MOUSE_PRESS, e);
				break;
			case SECONDARY:
				System.err.println("Creating a secondary event");
				this.secondaryPressed = this.deliverEvent(JSpriteMouseEventType.MOUSE_PRESS, e);
				System.out.println("Target delegate for secondary press was " + this.secondaryPressed);
				break;
			case TERTIARY:
				this.tertiaryPressed = this.deliverEvent(JSpriteMouseEventType.MOUSE_PRESS, e);
				break;
			default:
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		JSpriteButtonType b = this.transformButtonType(e.getButton());
		JSpriteMouseEvent m;
		switch (b) {
			case PRIMARY:
//				m = this.transformMouseEvent(e, JSpriteMouseEventType.MOUSE_RELEASE, this.primaryPressed.getUpperLeftX(), this.primaryPressed.getUpperLeftY());
//				this.cascadeEvent(this.primaryPressed, JSpriteMouseEventType.MOUSE_RELEASE, m);
				this.deliverEvent(JSpriteMouseEventType.MOUSE_RELEASE, e, this.primaryPressed);
				break;
			case SECONDARY:
//				m = this.transformMouseEvent(e, JSpriteMouseEventType.MOUSE_RELEASE, this.secondaryPressed.getUpperLeftX(), this.secondaryPressed.getUpperLeftY());
//				this.cascadeEvent(this.secondaryPressed, JSpriteMouseEventType.MOUSE_RELEASE, m);
				this.deliverEvent(JSpriteMouseEventType.MOUSE_RELEASE, e, this.secondaryPressed);
				break;
			case TERTIARY:
//				m = this.transformMouseEvent(e, JSpriteMouseEventType.MOUSE_RELEASE, this.tertiaryPressed.getUpperLeftX(), this.tertiaryPressed.getUpperLeftY());
//				this.cascadeEvent(this.tertiaryPressed, JSpriteMouseEventType.MOUSE_RELEASE, m);
				this.deliverEvent(JSpriteMouseEventType.MOUSE_RELEASE, e, this.tertiaryPressed);
				break;
			default:
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// Because this fires on canvas boundaries and not on sprite boundaries, I'm not sure how useful this is.
//		this.deliverEvent(JSpriteMouseEventType.MOUSE_ENTER, e);
		this.createEnterExitEvents(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// Because this fires on canvas boundaries and not on sprite boundaries, I'm not sure how useful this is.
//		this.deliverEvent(JSpriteMouseEventType.MOUSE_EXIT, e);
		this.createEnterExitEvents(e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
//		System.err.println("handler got a drag event");
		JSpriteButtonType b = this.transformButtonType(e.getButton());
		switch (b) {
			case PRIMARY:
				this.deliverEvent(JSpriteMouseEventType.MOUSE_DRAG, e, this.primaryPressed);
				break;
			case SECONDARY:
//				System.out.println("Delivering a drag to " + this.secondaryPressed);
				this.deliverEvent(JSpriteMouseEventType.MOUSE_DRAG, e, this.secondaryPressed);
				break;
			case TERTIARY:
				this.deliverEvent(JSpriteMouseEventType.MOUSE_DRAG, e, this.tertiaryPressed);
				break;
			default:
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
