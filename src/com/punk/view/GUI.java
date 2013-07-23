package com.punk.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import com.punk.model.Border;
import com.punk.model.CapturepointsUtil;
import com.punk.model.GuiOptions;
import com.punk.view.Overlay.Size;

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
			Overlay.Size.MEDIUM, Overlay.Size.SMALL };
	private Overlay.Type[] comboOverlayTypeArray = { Overlay.Type.Icons,
			Overlay.Type.Text };
	private Overlay.Class[] comboOverlayClassArray = {
			Overlay.Class.elementalist, Overlay.Class.engineer,
			Overlay.Class.guardian, Overlay.Class.mesmer,
			Overlay.Class.necromancer, Overlay.Class.ranger,
			Overlay.Class.thief, Overlay.Class.warrior };

	private JButton btnOverlayWvw;
	private JComboBox<Border> comboBoxBorder;
	private JComboBox<Overlay.Type> comboBoxOverlayType;
	private JComboBox<Overlay.Size> comboBoxOverlaySize;
	private JComboBox<Overlay.Class> comboBoxOverlayClass;
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
		frame.setSize(600, 200);
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
					if (overlay.getBorder() == Border.EB) {
						comboBoxBorder.setSelectedItem(Border.RED);
					} else if (overlay.getBorder() == Border.RED) {
						comboBoxBorder.setSelectedItem(Border.GREEN);
					} else if (overlay.getBorder() == Border.GREEN) {
						comboBoxBorder.setSelectedItem(Border.BLUE);
					} else if (overlay.getBorder() == Border.BLUE) {
						comboBoxBorder.setSelectedItem(Border.EB);
					}
					break;
				case 2:
					if (overlay.getSize() == Size.LARGE) {
						comboBoxOverlaySize.setSelectedItem(Size.SMALL);
					} else if (overlay.getSize() == Size.MEDIUM) {
						comboBoxOverlaySize.setSelectedItem(Size.LARGE);
					} else if (overlay.getSize() == Size.SMALL) {
						comboBoxOverlaySize.setSelectedItem(Size.MEDIUM);
					}
					break;
				case 3:
					btnOverlayWvw.doClick();
					break;
				case 4:
					sliderOverlayX.setValue(sliderOverlayX.getValue() - 10);
					break;
				case 5:
					sliderOverlayX.setValue(sliderOverlayX.getValue() + 10);
					break;
				case 6:
					sliderOverlayY.setValue(sliderOverlayY.getValue() - 10);
					break;
				case 7:
					sliderOverlayY.setValue(sliderOverlayY.getValue() + 10);
					break;
				}

			}
		});
	}

	private void addMenu(JPanel components) {
		btnOverlayWvw = new JButton("Show overlay");
		comboBoxBorder = new JComboBox<Border>(comboBorderArray);
		comboBoxOverlaySize = new JComboBox<Overlay.Size>(comboOverlaySizeArray);
		comboBoxOverlayType = new JComboBox<Overlay.Type>(comboOverlayTypeArray);
		comboBoxOverlayClass = new JComboBox<Overlay.Class>(
				comboOverlayClassArray);
		checkBoxShowAll = new JCheckBox("Show all", true);
		checkBoxCopyToClipboard = new JCheckBox("Copy to clipboard", false);
		checkBoxShowNames = new JCheckBox("Show Names", true);
		checkBoxShowBackground = new JCheckBox("Show Map", true);
		labelBackgroundAlpha = new JLabel("Map Alpha:");
		sliderBackgroundAlpha = new JSlider(0, 100, 75);
		btnDashboard = new JButton("Dashboard");
		btnDashboard.setVisible(false);
		btnSaveSettings = new JButton("Save settings");
		btnSaveSettings.setVisible(false);

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

		components.add(comboBoxOverlayClass);

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
					sliderOverlayY.setMaximum((int) screenSize.getHeight()
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

		comboBoxOverlayClass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (overlay != null) {
					overlay.setProf((Overlay.Class) comboBoxOverlayClass
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
