package com.punk.view;

import javax.swing.*;
import java.awt.*;

public class RichJPanel extends JPanel {
    private Image backgroundImage = null;
    private double sizeMultiplier = 0;

    public RichJPanel(LayoutManager layout) {
        super(layout);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponents(g);

        Graphics2D g2 = (Graphics2D)g;

        if (backgroundImage != null) {
            Composite oriAc = g2.getComposite();
            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f);
            g2.setComposite(ac);
            g2.drawImage(backgroundImage, 0, 0, (int)(backgroundImage.getWidth(null) * sizeMultiplier), (int)(backgroundImage.getHeight(null) * sizeMultiplier), null);
            g2.setComposite(oriAc);
        }
    }

    public Image getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(Image backgroundImage, double sizeMultiplier) {
        this.backgroundImage = backgroundImage;
        this.sizeMultiplier = sizeMultiplier;
    }
}
