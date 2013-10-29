package com.punk.view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Icon;
import javax.swing.JLabel;

public class RichJLabel extends JLabel {

	private static final long serialVersionUID = 1L;
	private int tracking;
	private int left_x, left_y, right_x, right_y;
	private Color left_color, right_color;
	private int alpha = 100;

	public RichJLabel(String text, int tracking) {
		super(text, JLabel.CENTER);

		this.tracking = tracking;
	}

	public RichJLabel(String text, Icon icon, int tracking) {
		super(text, icon, JLabel.CENTER);

		this.tracking = tracking;
	}

	public void setLeftShadow(int x, int y, Color color) {
		left_x = x;
		left_y = y;
		left_color = color;
	}

	public void setRightShadow(int x, int y, Color color) {
		right_x = x;
		right_y = y;
		right_color = color;
	}

	public Dimension getPreferredSize() {
		String text = getText();
		FontMetrics fm = this.getFontMetrics(getFont());

		int w = fm.stringWidth(text);
		w += (text.length() - 1) * tracking;
		w += left_x + right_x;
		int h = fm.getHeight();
		h += left_y + right_y;

		if (this.getIcon() != null) {
			w += this.getIcon().getIconWidth();
			h += this.getIcon().getIconHeight() + this.getIconTextGap();
		}

		return new Dimension(w, h);
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				((float) alpha / 100));
		g2.setComposite(ac);

		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		char[] chars = getText().toCharArray();

		FontMetrics fm = this.getFontMetrics(getFont());

		int h = fm.getAscent();
		int x = 0;

		if (this.getIcon() != null) {
			this.getIcon().paintIcon(this, g2, x, 0);
			h += this.getIcon().getIconHeight() + this.getIconTextGap();
		}

		for (int i = 0; i < chars.length; i++) {
			char ch = chars[i];
			int w = fm.charWidth(ch) + tracking;

			g2.setColor(left_color);
			g2.drawString("" + chars[i], x - left_x, h - left_y);

			g2.setColor(right_color);
			g2.drawString("" + chars[i], x + right_x, h + right_y);

			g2.setColor(getForeground());
			g2.drawString("" + chars[i], x, h);

			x += w;
		}

		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}
}
