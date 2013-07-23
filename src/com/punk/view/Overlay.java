package com.punk.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SpringLayout;

import com.punk.model.Border;
import com.punk.model.Capturepoint;
import com.punk.model.CapturepointsUtil;
import com.punk.model.GuiOptions;
import com.punk.mumblelink.MumbleLink;
import com.punk.resources.Resources;
import com.punk.start.Start;

/**
 * @author Sander Schulenburg aka "Much"(schulenburgsander@gmail.com)
 */
public class Overlay extends Thread {

	// private final String ip = "83.247.54.28";

	private final String ip = "127.0.0.1";

	public enum Type {
		Text, Icons
	}

	public enum Size {
		SMALL, MEDIUM, LARGE
	}

	public enum Class {
		elementalist, engineer, guardian, mesmer, necromancer, ranger, thief, warrior
	}

	private SpringLayout overlayPanelSpringLayout = null;
	private JFrame overlayFrame = null;
	private RichJPanel overlayPanel = null;

	private CapturepointsUtil capUtil = null;
	private Border border = null;
	private Overlay.Type type = null;
	private Overlay.Size size = null;
	private Overlay.Class prof = Class.elementalist;
	private boolean showAll = true;
	private boolean showNames = true;
	private boolean showBackground = true;
	private boolean copyToClipboard = false;

	private MumbleLink mumbleLink;

	private JLabel labelPlayer = new JLabel();
	private HashMap<String, JLabel> players = new HashMap<String, JLabel>();

	private GuiOptions guiOptions = GuiOptions.getInstance();

	private Timer timer = null;

	private JProgressBar nextUpdateBar = new JProgressBar(0, 100);

	public Overlay(CapturepointsUtil capUtil, Border border, Overlay.Type type,
			Overlay.Size size) {
		this.capUtil = capUtil;
		this.border = border;
		this.type = type;
		this.size = size;

		mumbleLink = new MumbleLink();

		overlayFrame = new JFrame();
		overlayFrame.setUndecorated(true);
		overlayFrame.setSize(0, 0);
		overlayFrame.setLocationRelativeTo(null);
		overlayFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		overlayFrame.setAlwaysOnTop(true);
		overlayFrame.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
		overlayFrame.setLayout(new BorderLayout());
		overlayFrame.setVisible(false);

		overlayFrame.setFocusableWindowState(false);
		overlayFrame.setFocusable(false);
		overlayFrame.enableInputMethods(false);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		overlayFrame.setLocation((int) screenSize.getWidth() - getWidth(),
				(int) screenSize.getHeight() - getHeight());
		overlayFrame.setSize(getWidth(), getHeight());
		guiOptions.setxLocation(overlayFrame.getX());
		guiOptions.setyLocation(overlayFrame.getY());

		overlayPanelSpringLayout = new SpringLayout();
		overlayPanel = new RichJPanel(overlayPanelSpringLayout);
		overlayPanel.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
		overlayFrame.add(overlayPanel, BorderLayout.CENTER);

		nextUpdateBar.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
		nextUpdateBar.setForeground(new Color(1.0f, 0.8f, 0.0f, 0.5f));
		nextUpdateBar.setBorderPainted(false);
		overlayFrame.add(nextUpdateBar, BorderLayout.SOUTH);

		updateOverlayFrame();

		timer = new Timer();
		timer.schedule(new updateTimers(), 0, 1000);
	}

	private void applyPosition() {
		overlayFrame.setLocation(guiOptions.getxLocation(),
				guiOptions.getyLocation());
		overlayFrame.setSize(getWidth(), getHeight());
	}

	public int getWidth() {
		return (int) (500 * getSizeMultiplier());
	}

	public int getHeight() {
		switch (border) {
		case EB:
			return (int) (500 * getSizeMultiplier());

		default:
			return (int) (680 * getSizeMultiplier());
		}
	}

	private double getSizeMultiplier() {
		double sizeMultiplier = 1;
		switch (size) {
		case SMALL:
			sizeMultiplier = 0.60;
			break;

		case MEDIUM:
			sizeMultiplier = 0.75;
			break;

		case LARGE:
			sizeMultiplier = 1;
			break;
		}
		return sizeMultiplier;
	}

	public void setBorder(Border border) {
		clearOverlayFrame();

		this.border = border;

		updateOverlayFrame();
	}

	public Border getBorder() {
		return border;
	}

	public int getBorderId() {
		switch (border) {
		case EB:
			return 38;
		case RED:
			return 94;
		case BLUE:
			return 96;
		case GREEN:
			return 95;
		}
		return -1;
	}

	public int getBorderObjectCount() {
		switch (border) {
		case EB:
			return 22;
		case RED:
			return 13;
		case BLUE:
			return 13;
		case GREEN:
			return 13;
		}
		return 0;
	}

	public Size getSize() {
		return size;
	}

	public void clearOverlayFrame() {
		ArrayList<Capturepoint> capturepoints = capUtil
				.getCapturepoints(this.border);
		for (Capturepoint capturepoint : capturepoints) {
			overlayPanel.remove(capturepoint.getOverlay());
		}
	}

	public void updateOverlayFrame() {
		double sizeMultiplier = getSizeMultiplier();

		if (showBackground) {
			switch (border) {
			case EB:
				overlayPanel.setBackgroundImage(Resources.IMAGE_MAP_EB,
						sizeMultiplier, guiOptions.getBackgroundAlpha());
				break;
			default:
				overlayPanel.setBackgroundImage(Resources.IMAGE_MAP_BORDER,
						sizeMultiplier, guiOptions.getBackgroundAlpha());
				break;
			}
		} else {
			overlayPanel.setBackgroundImage(null, 0, 0);
		}

		ArrayList<Capturepoint> capturepoints = capUtil
				.getCapturepoints(this.border);
		for (Capturepoint capturepoint : capturepoints) {
			if ((capturepoint.getServer() != Color.GRAY && capturepoint
					.getRiTime() > 0) || showAll) {
				capturepoint
						.createOverlay(type, showNames, getSizeMultiplier());
				JPanel overlay = capturepoint.getOverlay();

				overlayPanel.add(overlay);

				switch (type) {
				case Icons:
					overlayPanelSpringLayout
							.putConstraint(
									SpringLayout.HORIZONTAL_CENTER,
									overlay,
									(int) ((double) capturepoint.getTop() * sizeMultiplier),
									SpringLayout.WEST, overlayPanel);
					overlayPanelSpringLayout
							.putConstraint(
									SpringLayout.NORTH,
									overlay,
									(int) ((double) capturepoint.getLeft() * sizeMultiplier),
									SpringLayout.NORTH, overlayPanel);
					break;
				default:
					break;
				}
			}
		}

		applyPosition();

		overlayFrame.repaint();
	}

	public void setSize(Size size) {
		clearOverlayFrame();

		this.size = size;

		updateOverlayFrame();
	}

	public void setProf(Class prof) {
		this.prof = prof;
	}

	public ImageIcon getProfIcon(String prof) {
		switch (prof) {
		case "elementalist":
			return getProfIcon(Class.elementalist);
		case "engineer":
			return getProfIcon(Class.engineer);
		case "guardian":
			return getProfIcon(Class.guardian);
		case "mesmer":
			return getProfIcon(Class.mesmer);
		case "necromancer":
			return getProfIcon(Class.necromancer);
		case "ranger":
			return getProfIcon(Class.ranger);
		case "thief":
			return getProfIcon(Class.thief);
		case "warrior":
			return getProfIcon(Class.warrior);
		}
		return null;
	}

	public ImageIcon getProfIcon(Class prof) {
		switch (prof) {
		case elementalist:
			return Resources.IMAGE_CLASS_ELEMENTALIST;
		case engineer:
			return Resources.IMAGE_CLASS_ENGINEER;

		case guardian:
			return Resources.IMAGE_CLASS_GUARDIAN;

		case mesmer:
			return Resources.IMAGE_CLASS_MESMER;

		case necromancer:
			return Resources.IMAGE_CLASS_NECROMANCER;

		case ranger:
			return Resources.IMAGE_CLASS_RANGER;

		case thief:
			return Resources.IMAGE_CLASS_THIEF;

		case warrior:
			return Resources.IMAGE_CLASS_WARRIOR;

		}

		return null;

	}

	public void setType(Type type) {
		clearOverlayFrame();

		this.type = type;

		switch (type) {
		case Icons:
			overlayPanel.setLayout(overlayPanelSpringLayout);
			break;

		case Text:
			overlayPanel.setLayout(new GridLayout(0, 2));
			break;
		}

		updateOverlayFrame();
	}

	private class updateTimers extends TimerTask {
		public void run() {
			for (Capturepoint cap : capUtil.getCapturepoints(Border.RED)) {
				cap.tickRit(getSizeMultiplier());
			}

			for (Capturepoint cap : capUtil.getCapturepoints(Border.GREEN)) {
				cap.tickRit(getSizeMultiplier());
			}

			for (Capturepoint cap : capUtil.getCapturepoints(Border.BLUE)) {
				cap.tickRit(getSizeMultiplier());
			}

			for (Capturepoint cap : capUtil.getCapturepoints(Border.EB)) {
				cap.tickRit(getSizeMultiplier());
			}

			updateCapturePoints();
			updatePlayerLocation();

		}
	}

	private double getMapWidth() {
		if (border == Border.EB) {
			return 73728.0;
		}
		return 61440.0;
	}

	private double getMapHeight() {
		if (border == Border.EB) {
			return 73728.0;
		}
		return 86016.0;
	}

	private void updatePlayerLocation() {
		double playerX = (mumbleLink.getfAvatarPosition()[0] * 39.3700787);

		double playerZ = (mumbleLink.getfAvatarPosition()[2] * 39.3700787);

		String playername = "";
		for (char c : mumbleLink.getIdentity()) {
			playername += c;
		}
		playername = playername.trim();
		String data = playername + "," + playerX + "," + playerZ + ","
				+ mumbleLink.getMapId() + "," + prof.toString();
		Socket s = null;

		// Create the socket connection to the MultiThreadedSocketServer port
		// 11111
		try {
			s = new Socket(ip, 11111);
		} catch (UnknownHostException uhe) {
			// Server Host unreachable
			s = null;
		} catch (IOException ioe) {
			// Cannot connect to port on given server host
			s = null;
		}

		if (s == null) {
			int locationX = (int) (((getWidth()) / getMapWidth()) * playerX)
					+ (getWidth() / 2);
			int locationZ = (int) (((getHeight()) / getMapHeight()) * (playerZ * -1))
					+ (getHeight() / 2);

			ImageIcon icon = new ImageIcon(
					getProfIcon(prof).getImage().getScaledInstance(
							(int) (Resources.IMAGE_CLASS_ELEMENTALIST
									.getIconWidth() * getSizeMultiplier()),
							(int) (Resources.IMAGE_CLASS_ELEMENTALIST
									.getIconHeight() * getSizeMultiplier()),
							Image.SCALE_SMOOTH));

			labelPlayer.setIcon(icon);

			for (String key : players.keySet()) {
				overlayPanel.remove(players.get(key));
			}
			overlayPanel.remove(labelPlayer);
			if (mumbleLink.getMapId() == getBorderId()) {
				overlayPanel.add(labelPlayer);
				overlayPanel.setComponentZOrder(labelPlayer, 0);
				overlayPanelSpringLayout.putConstraint(
						SpringLayout.HORIZONTAL_CENTER, labelPlayer, locationX,
						SpringLayout.WEST, overlayPanel);
				overlayPanelSpringLayout.putConstraint(SpringLayout.NORTH,
						labelPlayer, locationZ, SpringLayout.NORTH,
						overlayPanel);
			}
			overlayPanel.repaint();
		} else {

			ObjectInputStream in = null;
			PrintWriter out = null;

			try {
				// Create the streams to send and receive information
				in = new ObjectInputStream(s.getInputStream());
				out = new PrintWriter(new OutputStreamWriter(
						s.getOutputStream()));

				// Since this is the client, we will initiate the talking.
				// Send a string data and flush
				out.println(data);
				out.flush();
				// Receive the reply.

				try {
					HashMap<String, JLabel> playersServer = (HashMap<String, JLabel>) in
							.readObject();
					for (String key : playersServer.keySet()) {
						if (!players.containsKey(key)) {
							players.put(key, playersServer.get(key));
						} else {
							players.get(key).setToolTipText(
									playersServer.get(key).getToolTipText());
						}
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

				// Send the special string to tell server to quit.
				out.println("Quit");
				out.flush();
			} catch (IOException ioe) {
				System.out
						.println("Exception during communication. Server probably closed connection.");
			} finally {
				try {
					// Close the input and output streams
					out.close();
					in.close();
					// Close the socket before quitting
					s.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			overlayPanel.remove(labelPlayer);
			for (String key : players.keySet()) {
				overlayPanel.remove(players.get(key));

				if (Integer.parseInt(players.get(key).getToolTipText()
						.split(",")[2]) == getBorderId()) {

					double pX = Double.parseDouble(players.get(key)
							.getToolTipText().split(",")[0]);
					double pZ = Double.parseDouble(players.get(key)
							.getToolTipText().split(",")[1]);

					int x = (int) (((getWidth()) / getMapWidth()) * pX)
							+ (getWidth() / 2);
					int z = (int) (((getHeight()) / getMapHeight()) * pZ * -1)
							+ (getHeight() / 2);
					players.get(key).setFont(
							new Font(players.get(key).getFont().getName(),
									players.get(key).getFont().getStyle(), 10));
					ImageIcon icon = getProfIcon(players.get(key)
							.getToolTipText().split(",")[3]);

					players.get(key).setIcon(
							new ImageIcon(icon.getImage().getScaledInstance(
									(int) (16 * getSizeMultiplier()),
									(int) (16 * getSizeMultiplier()),
									Image.SCALE_SMOOTH)));

					overlayPanel.add(players.get(key));
					overlayPanel.setComponentZOrder(players.get(key), 0);
					overlayPanelSpringLayout.putConstraint(
							SpringLayout.HORIZONTAL_CENTER, players.get(key),
							x, SpringLayout.WEST, overlayPanel);
					overlayPanelSpringLayout.putConstraint(SpringLayout.NORTH,
							players.get(key), z, SpringLayout.NORTH,
							overlayPanel);
				}
			}

			overlayPanel.repaint();
		}
	}

	private void updateCapturePoints() {
		String currentTimers = "Timers: ";
		for (Capturepoint cap : capUtil.getCapturepoints(border)) {
			if (cap.getServer() != Color.GRAY && cap.getRiTime() > 0 || showAll) {
				cap.getOverlay().setVisible(true);
				if (cap.getRiTime() > 0 && copyToClipboard) {
					currentTimers += cap.getChatcode() + " = " + cap.getTimer()
							+ " ";
				}
			} else {
				cap.getOverlay().setVisible(false);
			}
		}
		if (copyToClipboard) {
			if (currentTimers.equals("Timers: ")) {
				currentTimers += "None";
			}
			try {
				StringSelection stringSelection = new StringSelection(
						currentTimers);
				Clipboard clipboard = Toolkit.getDefaultToolkit()
						.getSystemClipboard();
				clipboard.setContents(stringSelection, null);
			} catch (Exception e) {

			}
		}
		if (!overlayFrame.isVisible())
			return;

		updateNextAPICall();
		overlayFrame.repaint();
	}

	private void updateNextAPICall() {
		Start.nextAPICall--;
		int size = 100 / 15 * Start.nextAPICall;
		nextUpdateBar.setValue(size);
	}

	public void toggleShowAll() {
		this.showAll = !showAll;

		updateCapturePoints();
		updatePlayerLocation();
	}

	public void run() {
		while (overlayFrame != null) {
			if (overlayFrame.isVisible()) {

				PointerInfo a = MouseInfo.getPointerInfo();
				Point b = a.getLocation();
				int x = (int) b.getX();
				int y = (int) b.getY();

				boolean isMouseOnOverlay = x > overlayFrame.getLocation().x
						&& x < overlayFrame.getLocation().x + getWidth()
						&& y > overlayFrame.getLocation().y
						&& y < overlayFrame.getLocation().y + getHeight();

				if (isMouseOnOverlay) {
					// overlayFrame.setSize(0, 0);
				} else {
					// overlayFrame.setSize(getWidth(), getHeight());
				}
			} else {
				try {
					Thread.sleep(100);
				} catch (InterruptedException ignore) {
					ignore.printStackTrace();
				}
			}
		}
	}

	public boolean isVisible() {
		return overlayFrame.isVisible();
	}

	public void toggleOverlay() {
		overlayFrame.setVisible(!overlayFrame.isVisible());
	}

	public void toggleShowNames() {
		clearOverlayFrame();

		showNames = !showNames;

		updateOverlayFrame();
	}

	public void toggleCopyToClipboard() {
		copyToClipboard = !copyToClipboard;
	}

	public void toggleShowBackground() {
		clearOverlayFrame();

		showBackground = !showBackground;

		updateOverlayFrame();
	}

	public void setBackgroundAlpha(int backgroundTransparency) {
		clearOverlayFrame();

		guiOptions.setBackgroundAlpha(backgroundTransparency);

		updateOverlayFrame();
	}
}
