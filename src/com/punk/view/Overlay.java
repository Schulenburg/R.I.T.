package com.punk.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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

import com.melloware.jintellitype.JIntellitype;
import com.punk.model.Capturepoint;
import com.punk.model.CapturepointsUtil;
import com.punk.model.GuiOptions;
import com.punk.mumblelink.MumbleLink;
import com.punk.resources.Resources;
import com.punk.start.Start;
import com.punk.start.Start.Border;

/**
 * @author Sander Schulenburg aka "Much"(schulenburgsander@gmail.com)
 */
public class Overlay extends Thread {

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
	private boolean isMouseOnOverlay = false;

	private CapturepointsUtil capUtil = null;
	private Border border = null;
	private Size size = null;
	private Class prof = Class.elementalist;
	private boolean showNames = false;
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
		nextUpdateBar.setForeground(Capturepoint.GRAY);
		nextUpdateBar.setBorderPainted(false);
		overlayFrame.add(nextUpdateBar, BorderLayout.SOUTH);

		updateOverlayFrame();

		timer = new Timer();
		timer.schedule(new updateTimers(), 0, 1000);

		JIntellitype jintel = JIntellitype.getInstance();
		jintel.registerHotKey(1, JIntellitype.MOD_ALT, (int) '1');
	}

	public void setLocation(int x, int y) {
		overlayFrame.setLocation(x - overlayFrame.getWidth(),
				y - overlayFrame.getHeight());
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
		switch (size) {
		case SMALL:
			return 0.60;
		case MEDIUM:
			return 0.75;
		default:
			return 1;
		}

	}

	public void setBorder(Border border) {
		clearOverlayFrame();

		this.border = border;
		switch (border) {
		case GREEN:
			nextUpdateBar.setForeground(Capturepoint.GREEN);
			break;
		case BLUE:
			nextUpdateBar.setForeground(Capturepoint.BLUE);
			break;
		case RED:
			nextUpdateBar.setForeground(Capturepoint.RED);
			break;
		default:
			nextUpdateBar.setForeground(Capturepoint.GRAY);
			break;
		}

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
				.getCapturepoints(border);

		for (Capturepoint capturepoint : capturepoints) {
			capturepoint.createOverlay(showNames, getSizeMultiplier());
			JPanel overlay = capturepoint.getOverlay();
			overlayPanel.add(overlay);

			overlayPanelSpringLayout.putConstraint(
					SpringLayout.HORIZONTAL_CENTER, overlay,
					(int) ((double) capturepoint.getTop() * sizeMultiplier),
					SpringLayout.WEST, overlayPanel);
			overlayPanelSpringLayout.putConstraint(SpringLayout.NORTH, overlay,
					(int) ((double) capturepoint.getLeft() * sizeMultiplier),
					SpringLayout.NORTH, overlayPanel);
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
				+ mumbleLink.getMapId() + "," + prof.toString() + ","
				+ guiOptions.getNickname() + "," + guiOptions.getChannel();
		Socket socket = null;

		if (guiOptions.isTrack()) {
			try {
				socket = new Socket(Start.ip, 11111);
			} catch (UnknownHostException uhe) {
				// Server Host unreachable
				socket = null;
			} catch (IOException ioe) {
				// Cannot connect to port on given server host
				socket = null;
			}
		}

		if (socket == null || socket.isClosed()) {
			handleLocalLocation(playerX, playerZ);
		} else {
			handleNetworkLocation(socket, data);
		}
	}

	private void handleLocalLocation(double playerX, double playerZ) {
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
					labelPlayer, locationZ, SpringLayout.NORTH, overlayPanel);
		}
		overlayPanel.repaint();
	}

	private void handleNetworkLocation(Socket socket, String data) {
		ObjectInputStream in = null;
		PrintWriter out = null;

		try {
			in = new ObjectInputStream(socket.getInputStream());
			out = new PrintWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			out.println(data);
			out.flush();

			try {
				@SuppressWarnings("unchecked")
				HashMap<String, JLabel> playersServer = (HashMap<String, JLabel>) in
						.readObject();
				for (String key : players.keySet()) {
					if (!playersServer.containsKey(key)) {
						overlayPanel.remove(players.get(key));
						players.remove(key);
					}
				}

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
			out.println("Quit");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
				in.close();
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		overlayPanel.remove(labelPlayer);
		for (String key : players.keySet()) {
			overlayPanel.remove(players.get(key));
			if (Integer
					.parseInt(players.get(key).getToolTipText().split(",")[2]) == getBorderId()) {
				if (players.get(key).getToolTipText().split(",")[5]
						.equals(guiOptions.getChannel())) {

					double pX = Double.parseDouble(players.get(key)
							.getToolTipText().split(",")[0]);
					double pZ = Double.parseDouble(players.get(key)
							.getToolTipText().split(",")[1]);

					int x = (int) (((getWidth()) / getMapWidth()) * pX)
							+ (getWidth() / 2);
					int z = (int) (((getHeight()) / getMapHeight()) * pZ * -1)
							+ (getHeight() / 2);

					players.get(key).setText(
							players.get(key).getToolTipText().split(",")[4]);

					players.get(key).setFont(
							new Font(Font.SANS_SERIF, Font.BOLD, 10));

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
		}

		overlayPanel.repaint();
	}

	private void updateCapturePoints() {
		String currentTimers = "Timers:";
		String timersRed = " Red: ";
		String timersBlue = " | Blue: ";
		String timersGreen = " | Green: ";
		for (Capturepoint cap : capUtil.getCapturepoints(border)) {
			if (cap.getRiTime() > 0 && copyToClipboard) {
				if (cap.getServer() == Capturepoint.RED) {
					timersRed += cap.getChatcode() + " = " + cap.getTimer()
							+ " ";
				} else if (cap.getServer() == Capturepoint.BLUE) {
					timersBlue += cap.getChatcode() + " = " + cap.getTimer()
							+ " ";
				} else if (cap.getServer() == Capturepoint.GREEN) {
					timersGreen += cap.getChatcode() + " = " + cap.getTimer()
							+ " ";
				}
			}
		}
		if (copyToClipboard) {
			if (timersRed.equals(" Red: ")) {
				timersRed += "None";
			}
			if (timersBlue.equals(" | Blue: ")) {
				timersBlue += "None";
			}
			if (timersGreen.equals(" | Green: ")) {
				timersGreen += "None";
			}
			currentTimers += timersRed + timersBlue + timersGreen
					+ " //Powered by: R.I.T.";
			try {
				StringSelection stringSelection = new StringSelection(
						currentTimers);
				Clipboard clipboard = Toolkit.getDefaultToolkit()
						.getSystemClipboard();
				clipboard.setContents(stringSelection, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (!overlayFrame.isVisible())
			return;

		updateNextAPICall();

		overlayFrame.setFocusableWindowState(false);
		overlayFrame.setVisible(true);
		overlayFrame.repaint();
	}

	private void updateNextAPICall() {
		Start.nextAPICall--;
		int size = 100 / 15 * Start.nextAPICall;
		nextUpdateBar.setValue(size);
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

	public void run() {
		while (overlayFrame != null) {
			if (overlayFrame.isVisible()) {

				PointerInfo a = MouseInfo.getPointerInfo();
				Point b = a.getLocation();
				int x = (int) b.getX();
				int y = (int) b.getY();

				isMouseOnOverlay = x > overlayFrame.getLocation().x
						&& x < overlayFrame.getLocation().x + getWidth()
						&& y > overlayFrame.getLocation().y
						&& y < overlayFrame.getLocation().y + getHeight();

				if (isMouseOnOverlay) {
					overlayFrame.setSize(0, 0);
				} else {
					overlayFrame.setSize(getWidth(), getHeight());
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

}
