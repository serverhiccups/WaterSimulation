package com.hiccup01.JSprite;

public class JSpriteMouseEventDelegate {
	JSpriteMouseEventDelegateStatus status = null;
	JSprite delegate = null;

	public static JSpriteMouseEventDelegate COMPLETED = new JSpriteMouseEventDelegate(true);
	public static JSpriteMouseEventDelegate CONTINUE = new JSpriteMouseEventDelegate(false);

	public JSpriteMouseEventDelegate(boolean status) {
		if(status) {
			this.status = JSpriteMouseEventDelegateStatus.COMPLETED;
		} else this.status = JSpriteMouseEventDelegateStatus.CONTINUE;
	}

	public JSpriteMouseEventDelegate(JSprite delegate) {
		this.status = JSpriteMouseEventDelegateStatus.DELEGATED;
		this.delegate = delegate;
	}

	public boolean isComplete() {
		return this.status == JSpriteMouseEventDelegateStatus.COMPLETED;
	}

	public boolean isContinued() {
		return this.status == JSpriteMouseEventDelegateStatus.CONTINUE;
	}

	public JSprite getDelegate() {
		return this.delegate;
	}
}
