package com.punk.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SpringLayout;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
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

	public enum Type {
		Text, Icons
	}

	public enum Size {
		SMALL, MEDIUM, LARGE
	}

	private SpringLayout overlayPanelSpringLayout = null;
	private JFrame overlayFrame = null;
	private RichJPanel overlayPanel = null;

	private CapturepointsUtil capUtil = null;
	private Border border = null;
	private Overlay.Type type = null;
	private Overlay.Size size = null;
	private boolean showAll = true;
	private boolean showNames = true;
	private boolean showBackground = true;
	private boolean copyToClipboard = false;

	private MumbleLink mumbleLink;
	private JLabel labelPlayer = new JLabel(Resources.IMAGE_CLASS_ELEMENTALIST);

	private GuiOptions guiOptions = GuiOptions.getInstance();

	Timer timer = null;

	JProgressBar nextUpdateBar = new JProgressBar(0, 100);

	public Overlay(CapturepointsUtil capUtil, Border border, Overlay.Type type,
			Overlay.Size size) {
		this.capUtil = capUtil;
		this.border = border;
		this.type = type;
		this.size = size;

		mumbleLink = new MumbleLink();

		JIntellitype jintel = JIntellitype.getInstance();
		jintel.registerHotKey(1, JIntellitype.MOD_ALT, (int) 'M');
		jintel.registerHotKey(2, JIntellitype.MOD_ALT, (int) 'N');
		jintel.registerHotKey(3, JIntellitype.MOD_ALT, (int) 'B');

		jintel.registerHotKey(4, JIntellitype.MOD_ALT, (int) KeyEvent.VK_LEFT);
		jintel.registerHotKey(5, JIntellitype.MOD_ALT, (int) KeyEvent.VK_RIGHT);
		jintel.registerHotKey(6, JIntellitype.MOD_ALT, (int) KeyEvent.VK_UP);
		jintel.registerHotKey(7, JIntellitype.MOD_ALT, (int) KeyEvent.VK_DOWN);
		jintel.addHotKeyListener(new HotkeyListener() {

			@Override
			public void onHotKey(int arg0) {
				switch (arg0) {
				case 1:
					nextBorder();
					break;
				case 2:
					nextScale();
					break;
				case 3:
					toggleOverlay();
					break;
				case 4:
					moveFrameLeft();
					break;
				case 5:
					moveFrameRight();
					break;
				case 6:
					moveFrameUp();
					break;
				case 7:
					moveFrameDown();
					break;
				}

			}
		});

		overlayFrame = new JFrame();
		overlayFrame.setUndecorated(true);
		overlayFrame.setSize(0, 0);
		overlayFrame.setLocationRelativeTo(null);
		overlayFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		overlayFrame.setAlwaysOnTop(true);
		overlayFrame.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
		overlayFrame.setLayout(new BorderLayout());
		overlayFrame.setVisible(false);

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

	private void nextBorder() {
		if (getBorder() == Border.EB) {
			setBorder(Border.RED);
		} else if (getBorder() == Border.RED) {
			setBorder(Border.GREEN);
		} else if (getBorder() == Border.GREEN) {
			setBorder(Border.BLUE);
		} else if (getBorder() == Border.BLUE) {
			setBorder(Border.EB);
		}
	}

	private void prevBorder() {
		if (getBorder() == Border.EB) {
			setBorder(Border.BLUE);
		} else if (getBorder() == Border.RED) {
			setBorder(Border.EB);
		} else if (getBorder() == Border.GREEN) {
			setBorder(Border.RED);
		} else if (getBorder() == Border.BLUE) {
			setBorder(Border.GREEN);
		}
	}

	public Size getSize() {
		return size;
	}

	private void nextScale() {
		if (getSize() == Size.LARGE) {
			setSize(Size.SMALL);
		} else if (getSize() == Size.MEDIUM) {
			setSize(Size.LARGE);
		} else if (getSize() == Size.SMALL) {
			setSize(Size.MEDIUM);
		}
	}

	private void prevScale() {
		if (getSize() == Size.LARGE) {
			setSize(Size.MEDIUM);
		} else if (getSize() == Size.MEDIUM) {
			setSize(Size.SMALL);
		} else if (getSize() == Size.SMALL) {
			setSize(Size.LARGE);
		}
	}

	private void moveFrameLeft() {
		clearOverlayFrame();
		guiOptions.setxLocation(guiOptions.getxLocation() - 10);
		updateOverlayFrame();
	}

	private void moveFrameRight() {
		clearOverlayFrame();
		guiOptions.setxLocation(guiOptions.getxLocation() + 10);
		updateOverlayFrame();
	}

	private void moveFrameUp() {
		clearOverlayFrame();
		guiOptions.setyLocation(guiOptions.getyLocation() - 10);
		updateOverlayFrame();
	}

	private void moveFrameDown() {
		clearOverlayFrame();
		guiOptions.setyLocation(guiOptions.getyLocation() + 10);
		updateOverlayFrame();
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
		int locationX = (int) (((getWidth()) / getMapWidth()) * playerX)
				+ (getWidth() / 2);

		double playerZ = (mumbleLink.getfAvatarPosition()[2] * 39.3700787);
		int locationZ = (int) (((getHeight()) / getMapHeight()) * (playerZ * -1))
				+ (getHeight() / 2);

		overlayPanel.remove(labelPlayer);
		overlayPanel.add(labelPlayer);
		overlayPanelSpringLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER,
				labelPlayer, locationX, SpringLayout.WEST, overlayPanel);
		overlayPanelSpringLayout.putConstraint(SpringLayout.NORTH, labelPlayer,
				locationZ, SpringLayout.NORTH, overlayPanel);
		overlayPanel.repaint();
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

		overlayFrame.setFocusableWindowState(false);
		overlayFrame.setVisible(true);
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
