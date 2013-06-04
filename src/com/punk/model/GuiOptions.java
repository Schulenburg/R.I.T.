package com.punk.model;

public class GuiOptions {

	private static GuiOptions instance = null;

	private int xLocation;
	private int yLocation;
	private int backgroundAlpha = 75;

	private boolean change = false;

	protected GuiOptions() {

	}

	public static GuiOptions getInstance() {
		if (instance == null) {
			instance = new GuiOptions();
		}
		return instance;
	}

	public int getxLocation() {
		return xLocation;
	}

	public void setxLocation(int xLocation) {
		this.xLocation = xLocation;
		setChange(true);
	}

	public int getyLocation() {
		return yLocation;
	}

	public void setyLocation(int yLocation) {
		this.yLocation = yLocation;
		setChange(true);
	}

	public int getBackgroundAlpha() {
		return backgroundAlpha;
	}

	public void setBackgroundAlpha(int backgroundAlpha) {
		this.backgroundAlpha = backgroundAlpha;
		setChange(true);
	}

	public boolean getChange() {
		return change;
	}

	public void setChange(boolean change) {
		this.change = change;
	}
}
