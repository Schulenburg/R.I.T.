package com.punk.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayDeque;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.punk.model.GuiOptions;

public class BroadcastOverlay {
	private JFrame overlayFrame = null;
	private RichJLabel labelBroadcast = null;

	private Timer timer = null;

	private GuiOptions guiOptions = null;

	private ArrayDeque<String> messages;

	private static BroadcastOverlay instance = null;

	public static BroadcastOverlay getInstance() {
		if (instance == null) {
			instance = new BroadcastOverlay();
		}
		return instance;
	}

	protected BroadcastOverlay() {
		guiOptions = GuiOptions.getInstance();

		messages = new ArrayDeque<String>();

		overlayFrame = new JFrame();
		overlayFrame.setUndecorated(true);
		overlayFrame.setSize(0, 0);
		overlayFrame.setLocationRelativeTo(null);
		overlayFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		overlayFrame.setAlwaysOnTop(true);
		overlayFrame.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
		overlayFrame.setLayout(new GridLayout(0, 1));
		overlayFrame.setVisible(false);

		overlayFrame.setFocusableWindowState(false);
		overlayFrame.setFocusable(false);
		overlayFrame.enableInputMethods(false);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		overlayFrame.setLocation(30, 30);
		overlayFrame.setSize((int) screenSize.getWidth(), 20);
		overlayFrame.setVisible(true);

		labelBroadcast = new RichJLabel("", 0);
		labelBroadcast.setForeground(Color.ORANGE);
		labelBroadcast.setRightShadow(1, 1, Color.BLACK);
		labelBroadcast.setBackground(Color.WHITE);
		labelBroadcast.setHorizontalTextPosition(JLabel.CENTER);

		overlayFrame.add(labelBroadcast);

		labelBroadcast.setVisible(true);

		timer = new Timer();
		timer.schedule(new updateLocation(), 0, 5000);
	}

	public void pushMessage(String message) {
		messages.push(message);
	}

	private class updateLocation extends TimerTask {
		public void run() {
			if (messages.isEmpty()) {
				labelBroadcast.setText("");
			} else {
				labelBroadcast.setText(messages.pop());
			}

			overlayFrame.repaint();
			overlayFrame.setFocusableWindowState(false);
			overlayFrame.setVisible(guiOptions.isShowAnnouncements());
		}
	}
}
