package com.punk.model;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Sander Schulenburg aka "Much"(schulenburgsander@gmail.com)
 */
public class Capturepoint {

	private int id;
	private String name;
	private int points;
	private Color server;

	private int riTime;

	private boolean initialized = false;

	private JPanel panelOverlay = null;
	private JLabel labelOverlayName = null;
	private JLabel labelOverlayTimer = null;

	public Capturepoint(int id, String name, int points) {
		this.id = id;
		this.name = name;
		this.points = points;
		server = Color.GRAY;
		riTime = 0;

		createOverlay();
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
		if (riTime > 0) {
			riTime = riTime - 1;
		}
		labelOverlayTimer.setText(getTimeAsString(riTime));
		if (labelOverlayName.getForeground() != server) {
			labelOverlayTimer.setForeground(server);
			labelOverlayName.setForeground(server);
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
		riTime = 300;
	}

	private void createOverlay() {
		panelOverlay = new JPanel();
		panelOverlay.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));

		labelOverlayName = new JLabel(name);
		labelOverlayName.setForeground(server);
		panelOverlay.add(labelOverlayName);

		labelOverlayTimer = new JLabel(getTimeAsString(riTime));
		labelOverlayTimer.setForeground(server);
		panelOverlay.add(labelOverlayTimer);

		panelOverlay.setVisible(true);
	}

	@SuppressWarnings("unused")
	private String getColorName(Color server) {
		if (server.equals(Color.RED)) {
			return "Red";
		} else if (server.equals(Color.BLUE)) {
			return "Blue";
		} else if (server.equals(Color.GREEN)) {
			return "Green";
		} else {
			return "Gray";
		}
	}

	public JPanel getOverlay() {
		return panelOverlay;
	}

	private String getTimeAsString(int time) {
		int seconds = time % 60;
		int minutes = time / 60;
		if (seconds < 10) {
			return minutes + ":0" + seconds;
		}
		return minutes + ":" + seconds;
	}
}
