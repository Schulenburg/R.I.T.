package com.punk.model;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.punk.resources.Resources;
import com.punk.start.Start;
import com.punk.view.Overlay;
import com.punk.view.RichJLabel;

/**
 * @author Sander Schulenburg aka "Much"(schulenburgsander@gmail.com)
 */
public class Capturepoint {
	public static final Color RED = new Color(242, 109, 125);
	public static final Color BLUE = new Color(0, 174, 239);
	public static final Color GREEN = new Color(141, 198, 63);
	public static final Color GRAY = new Color(149, 149, 149);

	public enum Type {
		Castle, Keep, Tower, Camp
	}

	private int id;
	private String name;
	private int points;
	private Type type;
	private Point location;

	private Color server;
	private int riTime;
	private final int riBufferTime = (int) ((double) Start.API_REFRESH_DELAY / 2000);
	private boolean initialized = false;

	private JPanel panelOverlay = null;
	private JLabel labelOverlayIcon = null;
	private RichJLabel labelOverlayName = null;
	private RichJLabel labelOverlayTimer = null;

	public Capturepoint(int id, String name, int points, Type type,
			Point location) {
		this.id = id;
		this.name = name;
		this.points = points;
		this.type = type;
		this.location = location;

		server = Color.GRAY;
		riTime = 0;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getPoints() {
		return points;
	}

	public Color getServer() {
		return server;
	}

	public void setServer(Color server) {
		this.server = server;
	}

	public int getRiTime() {
		return riTime;
	}

	public void setRiTime(int riTime) {
		this.riTime = riTime;
	}

	public void tickRit() {
		if (panelOverlay == null)
			return;

		if (riTime > 0) {
			riTime = riTime - 1;
		}
		labelOverlayTimer.setText(getTimeAsString(riTime));
		if (labelOverlayTimer.getForeground() != server) {
			labelOverlayTimer.setForeground(server);
			if (labelOverlayName != null) {
				labelOverlayName.setForeground(server);
			}
			if (labelOverlayIcon != null) {
				changeIcon();
			}
		}
	}

	private void changeIcon() {
		if (server.equals(Capturepoint.RED)) {
			switch (type) {
			case Castle:
				labelOverlayIcon.setIcon(Resources.IMAGE_CASTLE_RED);
				break;

			case Keep:
				labelOverlayIcon.setIcon(Resources.IMAGE_KEEP_RED);
				break;

			case Tower:
				labelOverlayIcon.setIcon(Resources.IMAGE_TOWER_RED);
				break;

			case Camp:
				labelOverlayIcon.setIcon(Resources.IMAGE_CAMP_RED);
				break;
			}
		} else if (server.equals(Capturepoint.BLUE)) {
			switch (type) {
			case Castle:
				labelOverlayIcon.setIcon(Resources.IMAGE_CASTLE_BLUE);
				break;

			case Keep:
				labelOverlayIcon.setIcon(Resources.IMAGE_KEEP_BLUE);
				break;

			case Tower:
				labelOverlayIcon.setIcon(Resources.IMAGE_TOWER_BLUE);
				break;

			case Camp:
				labelOverlayIcon.setIcon(Resources.IMAGE_CAMP_BLUE);
				break;
			}
		} else if (server.equals(Capturepoint.GREEN)) {
			switch (type) {
			case Castle:
				labelOverlayIcon.setIcon(Resources.IMAGE_CASTLE_GREEN);
				break;

			case Keep:
				labelOverlayIcon.setIcon(Resources.IMAGE_KEEP_GREEN);
				break;

			case Tower:
				labelOverlayIcon.setIcon(Resources.IMAGE_TOWER_GREEN);
				break;

			case Camp:
				labelOverlayIcon.setIcon(Resources.IMAGE_CAMP_GREEN);
				break;
			}
		} else {
			switch (type) {
			case Castle:
				labelOverlayIcon.setIcon(Resources.IMAGE_CASTLE_NEUTRAL);
				break;

			case Keep:
				labelOverlayIcon.setIcon(Resources.IMAGE_KEEP_NEUTRAL);
				break;

			case Tower:
				labelOverlayIcon.setIcon(Resources.IMAGE_TOWER_NEUTRAL);
				break;

			case Camp:
				labelOverlayIcon.setIcon(Resources.IMAGE_CAMP_NEUTRAL);
				break;
			}
		}
	}

	public boolean getInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public void flip(Color server) {
		this.server = server;
		riTime = 300 - riBufferTime;
	}

	public void createOverlay(Overlay.Type type, boolean showNames) {
		panelOverlay = new JPanel();
		panelOverlay.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));

		switch (type) {
		case Icons:
			panelOverlay.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.anchor = GridBagConstraints.CENTER;

			labelOverlayIcon = new JLabel(Resources.IMAGE_CAMP_NEUTRAL);
			changeIcon();
			c.gridx = 0;
			c.gridy = 0;
			panelOverlay.add(labelOverlayIcon, c);

			if (showNames) {
				labelOverlayName = new RichJLabel(name, 0);
				labelOverlayName.setForeground(server);
				labelOverlayName.setRightShadow(1, 1, Color.BLACK);
				c.gridy++;
				panelOverlay.add(labelOverlayName, c);
			}

			labelOverlayTimer = new RichJLabel(getTimeAsString(riTime), 0);
			labelOverlayTimer.setForeground(server);
			labelOverlayTimer.setRightShadow(1, 1, Color.BLACK);
			c.gridy++;
			panelOverlay.add(labelOverlayTimer, c);
			break;

		case Text:
			if (showNames) {
				labelOverlayName = new RichJLabel(name, 0);
				labelOverlayName.setForeground(server);
				labelOverlayName.setRightShadow(1, 1, Color.BLACK);
				panelOverlay.add(labelOverlayName);
			}

			labelOverlayTimer = new RichJLabel(getTimeAsString(riTime), 0);
			labelOverlayTimer.setForeground(server);
			labelOverlayTimer.setRightShadow(1, 1, Color.BLACK);
			panelOverlay.add(labelOverlayTimer);
			break;
		}

		panelOverlay.setVisible(true);
	}

	public JPanel getOverlay() {
		return panelOverlay;
	}

	public int getTop() {
		return (int) location.getY();
	}

	public int getLeft() {
		return (int) location.getX();
	}

	private String getTimeAsString(int time) {
		if (time == 0) {
			return "";
		}
		int seconds = time % 60;
		int minutes = time / 60;
		if (seconds < 10) {
			return minutes + ":0" + seconds;
		}
		return minutes + ":" + seconds;
	}
}
