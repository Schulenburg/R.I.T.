package com.punk.model;

import com.punk.view.RichJLabel;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Sander Schulenburg aka "Much"(schulenburgsander@gmail.com)
 */
public class Capturepoint {
    public static final Color RED = new Color(242, 109, 125);
    public static final Color BLUE = new Color(0, 174, 239);
    public static final Color GREEN = new Color(141, 198, 63);
    public static final Color GRAY = new Color(149, 149, 149);

	private int id;
	private String name;
	private int points;
	private Color server;
	private int riTime;
	private final int riBufferTime = 8;
	private boolean initialized = false;

	private JPanel panelOverlay = null;
	private RichJLabel labelOverlayName = null;
	private RichJLabel labelOverlayTimer = null;

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
		riTime = 300 - riBufferTime;
	}

	private void createOverlay() {
		panelOverlay = new JPanel();
		panelOverlay.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));

		labelOverlayName = new RichJLabel(name, 1);
		labelOverlayName.setForeground(server);
        labelOverlayName.setRightShadow(1, 1, Color.BLACK);
		panelOverlay.add(labelOverlayName);

		labelOverlayTimer = new RichJLabel(getTimeAsString(riTime), 1);
		labelOverlayTimer.setForeground(server);
        labelOverlayTimer.setRightShadow(1, 1, Color.BLACK);
		panelOverlay.add(labelOverlayTimer);

		panelOverlay.setVisible(true);
	}

	@SuppressWarnings("unused")
	private String getColorName(Color server) {
		if (server.equals(Capturepoint.RED)) {
			return "Red";
		} else if (server.equals(Capturepoint.BLUE)) {
			return "Blue";
		} else if (server.equals(Capturepoint.GREEN)) {
			return "Green";
		} else {
			return "Gray";
		}
	}

	public JPanel getOverlay() {
		return panelOverlay;
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
