package com.punk.view;

import java.awt.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;

import com.punk.model.Border;
import com.punk.model.Capturepoint;
import com.punk.model.CapturepointsUtil;
import com.punk.resources.Resources;

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

    private SpringLayout overlayFrameSpringLayout = null;
	private JFrame overlayFrame = null;
    private RichJPanel overlayPanel = null;

	private CapturepointsUtil capUtil = null;
	private Border border = null;
    private Overlay.Type type = null;
    private Overlay.Size size = null;
	private boolean showAll = true;
	private boolean showNames = true;

    private boolean requestOnTop = false;

    Timer timer = null;

	public Overlay(CapturepointsUtil capUtil, Border border, Overlay.Type type, Overlay.Size size) {
		this.capUtil = capUtil;
		this.border = border;
        this.type = type;
        this.size = size;

        overlayFrameSpringLayout = new SpringLayout();

		overlayFrame = new JFrame();
		overlayFrame.setUndecorated(true);
		overlayFrame.setSize(0, 0);
		overlayFrame.setLocationRelativeTo(null);
		overlayFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		overlayFrame.setAlwaysOnTop(true);
		overlayFrame.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
		applyPosition();
        overlayPanel = new RichJPanel(overlayFrameSpringLayout);
        overlayPanel.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
        overlayFrame.add(overlayPanel);
		overlayFrame.setVisible(false);

		updateOverlayFrame();

		timer = new Timer();
		timer.schedule(new updateTimers(), 0, 1000);
	}

    private void applyPosition() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        overlayFrame.setLocation((int)screenSize.getWidth() - getWidth(), (int)screenSize.getHeight() - getHeight() - 40);
        overlayFrame.setSize(getWidth(), getHeight());
    }

    private int getWidth() {
        return (int)(550 * getSizeMultiplier());
    }

    private int getHeight() {
        switch (border) {
            case EB:
                return (int)(500 * getSizeMultiplier());

            default:
                return (int)(680 * getSizeMultiplier());
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

    private void clearOverlayFrame() {
        ArrayList<Capturepoint> capturepoints = capUtil.getCapturepoints(this.border);
        for (Capturepoint capturepoint : capturepoints) {
            overlayPanel.remove(capturepoint.getOverlay());
        }
    }

	private void updateOverlayFrame() {
        double sizeMultiplier = getSizeMultiplier();

        switch (border) {
            case EB:
                overlayPanel.setBackgroundImage(Resources.IMAGE_MAP_EB, sizeMultiplier);
                break;

            default:
                overlayPanel.setBackgroundImage(Resources.IMAGE_MAP_BORDER, sizeMultiplier);
                break;
        }

        ArrayList<Capturepoint> capturepoints = capUtil.getCapturepoints(this.border);
        for (Capturepoint capturepoint : capturepoints) {
			if ((capturepoint.getServer() != Color.GRAY && capturepoint.getRiTime() > 0) || showAll) {
                capturepoint.createOverlay(type, showNames);
                JPanel overlay = capturepoint.getOverlay();

                overlayPanel.add(overlay);

                switch (type) {
                    case Icons:
                        overlayFrameSpringLayout.putConstraint(SpringLayout.WEST, overlay, (int) ((double) capturepoint.getTop() * sizeMultiplier), SpringLayout.WEST, overlayPanel);
                        overlayFrameSpringLayout.putConstraint(SpringLayout.NORTH, overlay, (int) ((double) capturepoint.getLeft() * sizeMultiplier), SpringLayout.NORTH, overlayPanel);
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
                overlayPanel.setLayout(overlayFrameSpringLayout);
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
        for (Capturepoint cap : capUtil.getCapturepoints(border)) {
            if (cap.getServer() != Color.GRAY && cap.getRiTime() > 0 || showAll) {
                cap.getOverlay().setVisible(true);
            } else {
                cap.getOverlay().setVisible(false);
            }
        }
        if (!overlayFrame.isVisible()) return;

        if (requestOnTop) {
            overlayFrame.setFocusableWindowState(false);
            overlayFrame.setVisible(true);
        } else {
            overlayFrame.setFocusableWindowState(true);
        }
        overlayFrame.repaint();

        requestOnTop = !requestOnTop;
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

    public void toggleAlwaysOnTop() {
        overlayFrame.setAlwaysOnTop(!overlayFrame.isAlwaysOnTop());
    }

    public void toggleShowNames() {
        clearOverlayFrame();

        showNames = !showNames;

        updateOverlayFrame();
    }
}
