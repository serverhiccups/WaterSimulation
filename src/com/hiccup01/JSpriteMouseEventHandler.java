package com.hiccup01;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
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
			case MouseEvent.BUTTON2:
				return JSpriteButtonType.SECONDARY;
			case MouseEvent.BUTTON3:
				return JSpriteButtonType.TERTIARY;
			default:
				return JSpriteButtonType.NONE;
		}
	}

	private void cascadeEvent(JSprite s, JSpriteMouseEventType t, JSpriteMouseEvent e) {
		ListIterator<JSpriteMouseHandler> li = s.mouseHandlers.listIterator(s.mouseHandlers.size());
		while(li.hasPrevious()) {
			JSpriteMouseHandler next = li.previous();
			boolean status = false;
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
//					System.err.println("Delivering move event to sprite");
					status = next.mouseMoved(e);
					break;
				case MOUSE_DRAG:
					status = next.mouseDragged(e);
					break;
			}
			if(status) break;
		}
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
		this.cascadeEvent(at, t, e);
		return at;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		this.deliverEvent(JSpriteMouseEventType.MOUSE_CLICK, e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		JSprite at = this.findSpriteAt(e.getX() + this.canvas.virtualX, e.getY() + this.canvas.virtualY);
		JSpriteButtonType b = this.transformButtonType(e.getButton());
		switch (b) {
			case PRIMARY:
				this.primaryPressed = at;
				break;
			case SECONDARY:
				this.secondaryPressed = at;
				break;
			case TERTIARY:
				this.tertiaryPressed = at;
				break;
			default:
		}
		this.deliverEvent(JSpriteMouseEventType.MOUSE_PRESS, e);
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
		this.createEnterExitEvents(e);
		this.deliverEvent(JSpriteMouseEventType.MOUSE_DRAG, e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// Here we have to do some extra processing here so that we can create mouseEntered and mouseExited events that work on sprite boundaries.
		this.createEnterExitEvents(e);
		this.deliverEvent(JSpriteMouseEventType.MOUSE_MOVE, e);
	}
}
