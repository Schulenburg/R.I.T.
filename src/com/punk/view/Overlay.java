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
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SpringLayout;

import com.punk.model.Border;
import com.punk.model.Capturepoint;
import com.punk.model.CapturepointsUtil;
import com.punk.model.GuiOptions;
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
		VERY_SMALL, SMALL, MEDIUM, LARGE
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

	private GuiOptions guiOptions = GuiOptions.getInstance();

	Timer timer = null;

	JProgressBar nextUpdateBar = new JProgressBar(0, 100);

	public Overlay(CapturepointsUtil capUtil, Border border, Overlay.Type type,
			Overlay.Size size) {
		this.capUtil = capUtil;
		this.border = border;
		this.type = type;
		this.size = size;

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
		case VERY_SMALL:
			sizeMultiplier = 0.33;
			break;

		case SMALL:
			sizeMultiplier = 0.5;
			break;

		case MEDIUM:
			sizeMultiplier = 0.66;
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
				capturepoint.createOverlay(type, showNames);
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
				cap.tickRit();
			}

			for (Capturepoint cap : capUtil.getCapturepoints(Border.GREEN)) {
				cap.tickRit();
			}

			for (Capturepoint cap : capUtil.getCapturepoints(Border.BLUE)) {
				cap.tickRit();
			}

			for (Capturepoint cap : capUtil.getCapturepoints(Border.EB)) {
				cap.tickRit();
			}

			updateCapturePoints();

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
