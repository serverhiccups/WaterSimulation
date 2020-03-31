package com.hiccup01;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ListIterator;

public class JSpriteMouseEventHandler implements MouseListener, MouseMotionListener {
	JSpriteCanvas canvas;

	public JSpriteMouseEventHandler(JSpriteCanvas c) {
		this.canvas = c;
	}

	private JSprite findSpriteAt(int x, int y) {
		System.err.println("Finding sprite at " + x + ", " + y);
		ListIterator<JSpriteContainer> li = this.canvas.spriteStack.listIterator(this.canvas.spriteStack.size());
		while(li.hasPrevious()) {
			JSpriteContainer c = li.previous();
			JSprite s = c.sprite;
			JSpriteVisual visual = s.getVisual(s.getCurrentVisual());
			if(visual.isInBounds(x - s.xPosition + visual.getXOffset(), y - s.yPosition - visual.getYOffset())) {
				return s;
			}
		}
		return null;
	}

	private JSpriteMouseEvent transformMouseEvent(MouseEvent m, JSpriteMouseEventType t, int relativeToX, int relativeToY) {
		JSpriteButtonType b;
		switch (m.getButton()) {
			case MouseEvent.NOBUTTON:
				b = JSpriteButtonType.NONE;
				break;
			case MouseEvent.BUTTON1:
				b = JSpriteButtonType.PRIMARY;
				break;
			case MouseEvent.BUTTON2:
				b = JSpriteButtonType.SECONDARY;
				break;
			case MouseEvent.BUTTON3:
				b = JSpriteButtonType.TERTIARY;
				break;
			default:
				b = JSpriteButtonType.NONE;
		}
		int pX = m.getX();
		int pY = m.getY();
		int vX = pX + this.canvas.virtualX;
		int vY = pY + this.canvas.virtualY;
		int rX = vX - relativeToX;
		int rY = vY - relativeToY;
		return new JSpriteMouseEvent(b, JSpriteCoordinateType.RELATIVE, m.getClickCount(), m.isControlDown(), m.isAltDown(), m.isShiftDown(), m.isPopupTrigger(), pX, pY, rX, rY, vX, vY);
	}

	private void deliverEvent(JSpriteMouseEventType t, MouseEvent m) {
//		System.err.println("Delivering an event");
		JSprite at = this.findSpriteAt(m.getX() + this.canvas.virtualX, m.getY() + this.canvas.virtualY);
		if(at == null) {
//			System.err.println("Using the default handler");
			if(this.canvas.defaultHandler != null) {
				switch (t) {
					case MOUSE_CLICK:
						this.canvas.defaultHandler.mouseClicked(m);
						break;
					case MOUSE_ENTER:
						this.canvas.defaultHandler.mouseEntered(m);
						break;
					case MOUSE_EXIT:
						this.canvas.defaultHandler.mouseExited(m);
						break;
					case MOUSE_PRESS:
						this.canvas.defaultHandler.mousePressed(m);
						break;
					case MOUSE_RELEASE:
						this.canvas.defaultHandler.mouseReleased(m);
						break;
					case MOUSE_MOVE:
						this.canvas.defaultHandler.mouseMoved(m);
						break;
					case MOUSE_DRAG:
						this.canvas.defaultHandler.mouseDragged(m);
						break;
				}
				return;
			}
			return;
		}
		JSpriteMouseEvent e = this.transformMouseEvent(m, t, at.xPosition, at.yPosition);
		ListIterator<JSpriteMouseHandler> li = at.mouseHandlers.listIterator(at.mouseHandlers.size());
		while(li.hasPrevious()) {
			JSpriteMouseHandler next = li.previous();
			boolean status = false;
			switch (t) {
				case MOUSE_CLICK:
					status = next.mouseClicked(m);
					break;
				case MOUSE_ENTER:
					status = next.mouseEntered(m);
					break;
				case MOUSE_EXIT:
					status = next.mouseExited(m);
					break;
				case MOUSE_PRESS:
					status = next.mousePressed(m);
					break;
				case MOUSE_RELEASE:
					status = next.mouseReleased(m);
					break;
				case MOUSE_MOVE:
					status = next.mouseMoved(m);
					break;
				case MOUSE_DRAG:
					next.mouseDragged(m);
					break;
			}
			if(status) break;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		this.deliverEvent(JSpriteMouseEventType.MOUSE_CLICK, e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.deliverEvent(JSpriteMouseEventType.MOUSE_PRESS, e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		this.deliverEvent(JSpriteMouseEventType.MOUSE_RELEASE, e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		this.deliverEvent(JSpriteMouseEventType.MOUSE_ENTER, e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		this.deliverEvent(JSpriteMouseEventType.MOUSE_EXIT, e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		this.deliverEvent(JSpriteMouseEventType.MOUSE_DRAG, e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		this.deliverEvent(JSpriteMouseEventType.MOUSE_MOVE, e);
	}
}
