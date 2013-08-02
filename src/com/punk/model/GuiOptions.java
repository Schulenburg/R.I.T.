package com.punk.model;

public class GuiOptions {

	private static GuiOptions instance = null;

	private int xLocation;
	private int yLocation;
	private int backgroundAlpha = 100;

	private boolean change = false;

	private String channel = "Public";
	private String nickname = "Guest";
	private boolean track = false;

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

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public boolean isTrack() {
		return track;
	}

	public void setTrack(boolean track) {
		this.track = track;
	}

}
