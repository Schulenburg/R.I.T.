package com.punk.view;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;

import javax.swing.JPanel;

public class RichJPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private Image backgroundImage = null;
	private double sizeMultiplier = 0;
	private int alpha = 100;

	public RichJPanel(LayoutManager layout) {
		super(layout);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponents(g);

		Graphics2D g2 = (Graphics2D) g;

		if (backgroundImage != null) {
			Composite oriAc = g2.getComposite();
			AlphaComposite ac = AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, ((float) alpha / 100));
			g2.setComposite(ac);
			g2.drawImage(backgroundImage, 0, 0,
					(int) (backgroundImage.getWidth(null) * sizeMultiplier),
					(int) (backgroundImage.getHeight(null) * sizeMultiplier),
					null);
			g2.setComposite(oriAc);
		}
	}

	public Image getBackgroundImage() {
		return backgroundImage;
	}

	public void setBackgroundImage(Image backgroundImage,
			double sizeMultiplier, int alpha) {
		this.backgroundImage = backgroundImage;
		this.sizeMultiplier = sizeMultiplier;
		this.alpha = alpha;
	}
}
