package com.punk.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.punk.model.Border;
import com.punk.model.CapturepointsUtil;
import com.punk.model.GuiOptions;

/**
 * @author Sander Schulenburg aka "Much"(schulenburgsander@gmail.com)
 */
public class GUI {

	private final String credits = "Created by: Much, WhiteFang";
	private final Dimension screenSize = Toolkit.getDefaultToolkit()
			.getScreenSize();

	private GuiOptions guiOptions = GuiOptions.getInstance();
	private Overlay overlay = null;
	private JFrame frame = null;
	private Border[] comboBorderArray = { Border.EB, Border.RED, Border.GREEN,
			Border.BLUE };
	private Overlay.Size[] comboOverlaySizeArray = { Overlay.Size.LARGE,
			Overlay.Size.MEDIUM, Overlay.Size.SMALL, Overlay.Size.VERY_SMALL };
	private Overlay.Type[] comboOverlayTypeArray = { Overlay.Type.Icons,
			Overlay.Type.Text };

	private JButton btnOverlayWvw;
	private JComboBox<Border> comboBoxBorder;
	private JComboBox<Overlay.Type> comboBoxOverlayType;
	private JComboBox<Overlay.Size> comboBoxOverlaySize;
	private JCheckBox checkBoxShowAll;
	private JCheckBox checkBoxCopyToClipboard;
	private JCheckBox checkBoxShowNames;
	private JCheckBox checkBoxShowBackground;
	private JLabel labelBackgroundAlpha;
	private JSlider sliderBackgroundAlpha;
	private JLabel labelOverlayX;
	private JSlider sliderOverlayX;
	private JLabel labelOverlayY;
	private JSlider sliderOverlayY;
	private JButton btnDashboard;
	private JButton btnSaveSettings;

	public GUI(CapturepointsUtil capUtil) {
		frame = new JFrame("R.I.T.");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setSize(600, 150);
		JPanel components = new JPanel();
		components.setLayout(new GridLayout(0, 4));

		overlay = new Overlay(capUtil, Border.EB, Overlay.Type.Icons,
				Overlay.Size.LARGE);
		overlay.start();

		addMenu(components);
		components.setVisible(true);
		frame.add(components, BorderLayout.CENTER);

		frame.add(new JLabel(credits), BorderLayout.SOUTH);

		frame.setVisible(true);
	}

	private void addMenu(JPanel components) {
		btnOverlayWvw = new JButton("Show overlay");
		comboBoxBorder = new JComboBox<Border>(comboBorderArray);
		comboBoxOverlaySize = new JComboBox<Overlay.Size>(comboOverlaySizeArray);
		comboBoxOverlayType = new JComboBox<Overlay.Type>(comboOverlayTypeArray);
		checkBoxShowAll = new JCheckBox("Show all", true);
		checkBoxCopyToClipboard = new JCheckBox("Copy to clipboard", false);
		checkBoxShowNames = new JCheckBox("Show Names", true);
		checkBoxShowBackground = new JCheckBox("Show Map", true);
		labelBackgroundAlpha = new JLabel("Map Alpha:");
		sliderBackgroundAlpha = new JSlider(0, 100, 75);
		btnDashboard = new JButton("Dashboard");
		btnSaveSettings = new JButton("Save settings");

		labelOverlayX = new JLabel("Map horizontal location:");
		sliderOverlayX = new JSlider(0, (int) screenSize.getWidth()
				- overlay.getWidth(), (int) screenSize.getWidth()
				- overlay.getWidth());
		labelOverlayY = new JLabel("Map vertical location:");
		sliderOverlayY = new JSlider(0, (int) screenSize.getHeight()
				- overlay.getHeight(), (int) screenSize.getHeight()
				- overlay.getHeight());

		components.add(btnOverlayWvw);
		components.add(comboBoxBorder);
		components.add(comboBoxOverlayType);
		components.add(comboBoxOverlaySize);

		components.add(checkBoxShowAll);
		components.add(checkBoxCopyToClipboard);
		components.add(checkBoxShowNames);
		components.add(checkBoxShowBackground);

		components.add(labelBackgroundAlpha);
		components.add(new JLabel("0"));
		components.add(sliderBackgroundAlpha);
		components.add(new JLabel("100"));

		components.add(labelOverlayX);
		components.add(new JLabel("0"));
		components.add(sliderOverlayX);
		components.add(new JLabel((int) screenSize.getWidth() + ""));

		components.add(labelOverlayY);
		components.add(new JLabel("0"));
		components.add(sliderOverlayY);
		components.add(new JLabel((int) screenSize.getHeight() + ""));

		components.add(btnDashboard);
		components.add(btnSaveSettings);
		components.add(new JLabel(""));
		components.add(new JLabel(""));

		btnOverlayWvw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (overlay != null) {
					overlay.toggleOverlay();
					if (overlay.isVisible()) {
						btnOverlayWvw.setText("Hide overlay");
					} else {
						btnOverlayWvw.setText("Show overlay");
					}
				}
			}
		});

		comboBoxBorder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (overlay != null) {
					overlay.setBorder((Border) comboBoxBorder.getSelectedItem());
				}
			}
		});

		comboBoxOverlaySize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (overlay != null) {
					overlay.setSize((Overlay.Size) comboBoxOverlaySize
							.getSelectedItem());
					sliderOverlayX.setMaximum((int) screenSize.getWidth()
							- overlay.getWidth());
					sliderOverlayX.setValue((int) screenSize.getWidth()
							- overlay.getWidth());
					sliderOverlayY.setMaximum((int) screenSize.getHeight()
							- overlay.getHeight());
					sliderOverlayY.setValue((int) screenSize.getHeight()
							- overlay.getHeight());
				}
			}
		});

		comboBoxOverlayType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (overlay != null) {
					overlay.setType((Overlay.Type) comboBoxOverlayType
							.getSelectedItem());
				}
			}
		});

		checkBoxShowAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (overlay != null) {
					overlay.toggleShowAll();
				}
			}
		});

		checkBoxCopyToClipboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (overlay != null) {
					overlay.toggleCopyToClipboard();
				}
			}
		});

		checkBoxShowNames.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (overlay != null) {
					overlay.toggleShowNames();
				}
			}
		});

		checkBoxShowBackground.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (overlay != null) {
					overlay.toggleShowBackground();
				}
			}
		});

		sliderBackgroundAlpha.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (overlay != null) {
					overlay.setBackgroundAlpha(sliderBackgroundAlpha.getValue());
				}
			}
		});

		sliderOverlayX.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (overlay != null) {
					overlay.clearOverlayFrame();
					guiOptions.setxLocation(sliderOverlayX.getValue());
					overlay.updateOverlayFrame();
				}
			}
		});

		sliderOverlayY.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (overlay != null) {
					overlay.clearOverlayFrame();
					guiOptions.setyLocation(sliderOverlayY.getValue());
					overlay.updateOverlayFrame();
				}
			}
		});
	}
}
