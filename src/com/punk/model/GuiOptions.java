package com.punk.model;

import com.punk.start.Start.Border;
import com.punk.start.Start.TrackMode;
import com.punk.start.Start.TrackUnit;

public class GuiOptions {

	private static GuiOptions instance = null;

	private int xLocation;
	private int yLocation;
	private int backgroundAlpha = 100;

	private Border border = Border.EB;

	private boolean change = false;

	private String channel = "Public";
	private String nickname = "";

	private boolean sharingLocation = false;

	private TrackMode trackMode = TrackMode.Camera;
	private TrackUnit trackUnit = TrackUnit.Units;

	private boolean autoBorderSwap = false;

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

	public Border getBorder() {
		return border;
	}

	public void setBorder(Border border) {
		this.border = border;
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

	public boolean isSharingLocation() {
		return sharingLocation;
	}

	public void setSharingLocation(boolean sharingLocation) {
		this.sharingLocation = sharingLocation;
	}

	public TrackMode getTrackMode() {
		return trackMode;
	}

	public void setTrackMode(TrackMode trackMode) {
		this.trackMode = trackMode;
	}

	public TrackUnit getTrackUnit() {
		return trackUnit;
	}

	public void setTrackUnit(TrackUnit trackUnit) {
		this.trackUnit = trackUnit;
	}

	public boolean isAutoSwapBorder() {
		return autoBorderSwap;
	}

	public void setAutoSwapBorder(boolean autoBorderSwap) {
		this.autoBorderSwap = autoBorderSwap;
	}

}
