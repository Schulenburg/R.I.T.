package com.punk.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.punk.resources.Resources;

public class NavigationOverlay {
	private JFrame overlayFrame = null;
	private RichJLabel labelArrow = null;
	private RichJLabel labelDistance = null;

	private Timer timer = null;
	private double angle = 0;
	private double distance = 0;

	private DecimalFormat df = new DecimalFormat("#.##");

	public NavigationOverlay() {
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
		overlayFrame.setLocation((int) screenSize.getWidth() / 2 - 32,
				(int) screenSize.getHeight() - 200);
		overlayFrame.setSize(100, 100);
		overlayFrame.setVisible(true);

		labelArrow = new RichJLabel("", 0);
		overlayFrame.add(labelArrow);

		labelDistance = new RichJLabel(df.format(distance / 39.3700787)
				+ " Meters", 0);
		labelDistance.setForeground(Color.ORANGE);
		labelDistance.setRightShadow(1, 1, Color.BLACK);

		overlayFrame.add(labelDistance);

		labelArrow.setVisible(true);
		labelDistance.setVisible(true);

		timer = new Timer();
		timer.schedule(new updateLocation(), 0, 150);
	}

	private class updateLocation extends TimerTask {
		public void run() {
			if (distance < 0) {
				overlayFrame.setVisible(false);
				return;
			}
			ImageIcon icon = Resources.IMAGE_ARROW;
			int w = icon.getIconWidth();
			int h = icon.getIconHeight();
			int type = BufferedImage.TYPE_INT_ARGB;
			BufferedImage image = new BufferedImage(h, w, type);
			Graphics2D g2 = image.createGraphics();
			double x1 = (h - w) / 2.0;
			double y1 = (w - h) / 2.0;
			AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
			at.rotate(Math.toRadians(angle), w / 2.0, h / 2.0);
			g2.drawImage(icon.getImage(), at, labelArrow);
			g2.dispose();
			icon = new ImageIcon(image);
			labelArrow.setIcon(icon);
			labelDistance.setText(df.format(distance / 39.3700787) + " Meters");
			overlayFrame.repaint();
			overlayFrame.setFocusableWindowState(false);
			overlayFrame.setVisible(true);
		}
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

}
