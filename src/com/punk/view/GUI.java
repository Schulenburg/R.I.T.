package com.punk.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import com.punk.model.CapturepointsUtil;
import com.punk.model.GuiOptions;
import com.punk.start.Start.Border;
import com.punk.start.Start.TrackMode;
import com.punk.start.Start.TrackUnit;
import com.punk.view.Overlay.Size;

/**
 * @author Sander Schulenburg aka "Much"(schulenburgsander@gmail.com)
 */
public class GUI {

	private JFrame frame;

	private final String credits = "Created by: Much, WhiteFang";
	private final Dimension screenSize = Toolkit.getDefaultToolkit()
			.getScreenSize();
	private GraphicsEnvironment ge = GraphicsEnvironment
			.getLocalGraphicsEnvironment();

	private GuiOptions guiOptions = GuiOptions.getInstance();
	private Overlay overlay = null;
	private Border[] comboBorderArray = { Border.EB, Border.RED, Border.GREEN,
			Border.BLUE };
	private Overlay.Size[] comboOverlaySizeArray = { Overlay.Size.LARGE,
			Overlay.Size.MEDIUM, Overlay.Size.SMALL };
	private TrackMode[] trackModeArray = { TrackMode.Camera, TrackMode.Avatar,
			TrackMode.Map };
	private TrackUnit[] trackUnitArray = { TrackUnit.Units, TrackUnit.Meters,
			TrackUnit.Feet };

	private JButton btnOverlayWvw;
	private JComboBox<Border> comboBoxBorder;
	private JComboBox<Overlay.Size> comboBoxOverlaySize;
	private JComboBox<String> comboBoxScreens;

	private JCheckBox checkBoxShowBackground;
	private JCheckBox checkBoxShowNames;
	private JCheckBox checkBoxCopyToClipboard;
	private JCheckBox checkBoxAutoBorder;

	private JLabel labelBackgroundAlpha;
	private JSlider sliderBackgroundAlpha;
	private JLabel labelBackgroundAlphaCurrent;

	private JLabel labelOverlayX;
	private JSlider sliderOverlayX;
	private JLabel labelOverlayXCurrent;

	private JLabel labelOverlayY;
	private JSlider sliderOverlayY;
	private JLabel labelOverlayYCurrent;

	private JLabel labelChannel;
	private JTextField txtChannel;

	private JLabel labelNickname;
	private JTextField txtNickname;
	private JLabel labelNicknameLimit;

	private JComboBox<String> comboBoxTrackPlayer;
	private JButton btnRefreshTrackPlayers;
	private JComboBox<TrackMode> comboBoxTrackMode;
	private JComboBox<TrackUnit> comboBoxTrackUnit;

	private JButton btnUpdateTrackSettings;
	private JLabel labelSharing;

	private Timer checkGuiOptionsTimer;

	public GUI(CapturepointsUtil capUtil) {
		overlay = new Overlay(capUtil, Border.EB, Overlay.Type.Icons,
				Overlay.Size.LARGE);
		overlay.start();

		JPanel components = new JPanel();
		components.setLayout(new GridLayout(0, 4));
		addMenu(components);
		components.setVisible(true);

		frame = new JFrame("R.I.T.");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setSize(650, 300);
		frame.add(components, BorderLayout.CENTER);
		frame.add(new JLabel(credits), BorderLayout.SOUTH);
		frame.setVisible(true);

		setHotkeys();

		checkGuiOptionsTimer = new Timer();
		checkGuiOptionsTimer.schedule(new checkGuiOptions(), 0, 5000);
		btnRefreshTrackPlayers.doClick();
	}

	private void addMenu(JPanel components) {
		btnOverlayWvw = new JButton("Show overlay");
		comboBoxBorder = new JComboBox<Border>(comboBorderArray);
		comboBoxOverlaySize = new JComboBox<Overlay.Size>(comboOverlaySizeArray);
		String[] monitorNames = new String[ge.getScreenDevices().length];
		for (int i = 0; i < ge.getScreenDevices().length; i++) {
			monitorNames[i] = ge.getScreenDevices()[i].getIDstring();
		}
		comboBoxScreens = new JComboBox<String>(monitorNames);

		checkBoxCopyToClipboard = new JCheckBox("Copy to clipboard", false);
		checkBoxShowNames = new JCheckBox("Show Names", false);
		checkBoxShowBackground = new JCheckBox("Show Map", true);
		checkBoxAutoBorder = new JCheckBox("Auto border swap", false);

		labelBackgroundAlpha = new JLabel("Map Alpha:");
		sliderBackgroundAlpha = new JSlider(0, 100, 100);
		labelBackgroundAlphaCurrent = new JLabel("100");

		labelOverlayX = new JLabel("Map horizontal location:");
		sliderOverlayX = new JSlider(0, (int) screenSize.getWidth()
				- overlay.getWidth(), (int) screenSize.getWidth()
				- overlay.getWidth());
		labelOverlayXCurrent = new JLabel(sliderOverlayX.getMaximum() + "");

		labelOverlayY = new JLabel("Map vertical location:");
		sliderOverlayY = new JSlider(0, (int) screenSize.getHeight()
				- overlay.getHeight(), (int) screenSize.getHeight()
				- overlay.getHeight());
		labelOverlayYCurrent = new JLabel(sliderOverlayY.getMaximum() + "");

		labelChannel = new JLabel("Channel");
		txtChannel = new JTextField("Public");

		labelNickname = new JLabel("Nickname");
		txtNickname = new JTextField("");
		labelNicknameLimit = new JLabel("Optional (5 char max)");

		labelSharing = new JLabel();
		btnUpdateTrackSettings = new JButton("Update settings");
		btnRefreshTrackPlayers = new JButton("Refresh list");
		comboBoxTrackPlayer = new JComboBox<String>();
		comboBoxTrackMode = new JComboBox<TrackMode>(trackModeArray);
		comboBoxTrackUnit = new JComboBox<TrackUnit>(trackUnitArray);

		components.add(btnOverlayWvw);
		components.add(comboBoxBorder);
		components.add(comboBoxOverlaySize);
		components.add(comboBoxScreens);

		components.add(checkBoxShowNames);
		components.add(checkBoxShowBackground);
		components.add(checkBoxCopyToClipboard);
		components.add(checkBoxAutoBorder);

		components.add(labelBackgroundAlpha);
		components.add(new JLabel("0 - 100"));
		components.add(sliderBackgroundAlpha);
		components.add(labelBackgroundAlphaCurrent);

		components.add(labelOverlayX);
		components.add(new JLabel("0 - " + (int) screenSize.getWidth()));
		components.add(sliderOverlayX);
		components.add(labelOverlayXCurrent);

		components.add(labelOverlayY);
		components.add(new JLabel("0 - " + (int) screenSize.getHeight()));
		components.add(sliderOverlayY);
		components.add(labelOverlayYCurrent);

		components.add(new JLabel());
		components.add(new JLabel());
		components.add(new JLabel());
		components.add(new JLabel());

		components.add(labelChannel);
		components.add(txtChannel);
		components.add(new JLabel());
		components.add(new JLabel());

		components.add(labelNickname);
		components.add(txtNickname);
		components.add(labelNicknameLimit);
		components.add(new JLabel());

		components.add(new JLabel("Track"));
		components.add(comboBoxTrackPlayer);
		components.add(btnRefreshTrackPlayers);
		components.add(new JLabel());

		components.add(new JLabel("Track mode"));
		components.add(comboBoxTrackMode);
		components.add(comboBoxTrackUnit);
		components.add(new JLabel());

		components.add(new JLabel());
		components.add(btnUpdateTrackSettings);
		components.add(labelSharing);
		components.add(new JLabel());

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
					if (comboBoxBorder.getSelectedItem() != guiOptions
							.getBorder()) {
						overlay.setBorder((Border) comboBoxBorder
								.getSelectedItem());
						btnRefreshTrackPlayers.doClick();
					}
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

		comboBoxScreens.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (overlay != null) {
					GraphicsDevice gd = ge.getScreenDevices()[comboBoxScreens
							.getSelectedIndex()];
					Point screenTopLeft = gd.getDefaultConfiguration()
							.getBounds().getLocation();

					int width = gd.getDisplayMode().getWidth();
					int height = gd.getDisplayMode().getHeight();

					int xLocation = screenTopLeft.x + width;
					int yLocation = screenTopLeft.y + height;

					guiOptions.setxLocation(xLocation);
					guiOptions.setyLocation(yLocation);

					labelOverlayX.setText("0 - " + width);
					labelOverlayY.setText("0 - " + height);

					labelOverlayXCurrent.setText(xLocation + "");
					labelOverlayYCurrent.setText(yLocation + "");

					sliderOverlayX.setMinimum(screenTopLeft.x);
					sliderOverlayY.setMinimum(screenTopLeft.y);

					sliderOverlayX.setMaximum(xLocation - overlay.getWidth());
					sliderOverlayY.setMaximum(yLocation - overlay.getHeight());

					sliderOverlayX.setValue(xLocation - overlay.getWidth());
					sliderOverlayY.setValue(yLocation - overlay.getHeight());

					overlay.setLocation(xLocation, yLocation);
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

		checkBoxAutoBorder.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				guiOptions.setAutoSwapBorder(checkBoxAutoBorder.isSelected());
			}
		});

		sliderBackgroundAlpha.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (overlay != null) {
					overlay.setBackgroundAlpha(sliderBackgroundAlpha.getValue());
					labelBackgroundAlphaCurrent.setText(sliderBackgroundAlpha
							.getValue() + "");
				}
			}
		});

		sliderOverlayX.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (overlay != null) {
					overlay.clearOverlayFrame();
					guiOptions.setxLocation(sliderOverlayX.getValue());
					labelOverlayXCurrent.setText(sliderOverlayX.getValue() + "");
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
					labelOverlayYCurrent.setText(sliderOverlayY.getValue() + "");
					overlay.updateOverlayFrame();
				}
			}
		});

		txtNickname.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent arg0) {
			}

			public void keyReleased(KeyEvent arg0) {
				if (txtNickname.getText().length() > 5) {
					txtNickname.setText(txtNickname.getText().substring(0, 5));
				}

			}

			public void keyPressed(KeyEvent arg0) {
			}
		});

		comboBoxTrackMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				guiOptions.setTrackMode((TrackMode) comboBoxTrackMode
						.getSelectedItem());
			}
		});

		comboBoxTrackUnit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				guiOptions.setTrackUnit((TrackUnit) comboBoxTrackUnit
						.getSelectedItem());
			}
		});

		btnUpdateTrackSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				guiOptions.setNickname(txtNickname.getText());
				guiOptions.setChannel(txtChannel.getText());
				overlay.setTargetPlayer((String) comboBoxTrackPlayer
						.getSelectedItem());
			}
		});

		btnRefreshTrackPlayers.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String currentTrack = (String) comboBoxTrackPlayer
						.getSelectedItem();
				comboBoxTrackPlayer.removeAllItems();
				comboBoxTrackPlayer.addItem("<Don't track>");
				for (String s : overlay.getPlayersInMap()) {
					comboBoxTrackPlayer.addItem(s);
				}

				if (currentTrack != null && !currentTrack.isEmpty()) {
					for (int i = 0; i < comboBoxTrackPlayer.getItemCount(); i++) {
						if (currentTrack == comboBoxTrackPlayer.getItemAt(i)) {
							comboBoxTrackPlayer.setSelectedIndex(i);
							i = comboBoxTrackPlayer.getItemCount() + 1;
						}
					}
				}
			}
		});

		comboBoxTrackPlayer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				overlay.setTargetPlayer((String) comboBoxTrackPlayer
						.getSelectedItem());
			}
		});

	}

	private void setHotkeys() {

		JIntellitype jintel = JIntellitype.getInstance();
		jintel.registerHotKey(1, JIntellitype.MOD_ALT, (int) 'M');
		jintel.registerHotKey(2, JIntellitype.MOD_ALT, (int) 'N');
		jintel.registerHotKey(3, JIntellitype.MOD_ALT, (int) 'B');

		jintel.registerHotKey(4, JIntellitype.MOD_ALT, (int) KeyEvent.VK_LEFT);
		jintel.registerHotKey(5, JIntellitype.MOD_ALT, (int) KeyEvent.VK_RIGHT);
		jintel.registerHotKey(6, JIntellitype.MOD_ALT, (int) KeyEvent.VK_UP);
		jintel.registerHotKey(7, JIntellitype.MOD_ALT, (int) KeyEvent.VK_DOWN);

		jintel.registerHotKey(8, JIntellitype.MOD_ALT, (int) 'L');

		jintel.registerHotKey(9, JIntellitype.MOD_ALT, (int) 'C');

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
				case 8:
					checkBoxCopyToClipboard
							.setSelected(!checkBoxCopyToClipboard.isSelected());
					overlay.toggleCopyToClipboard();
					break;
				case 9:
					overlay.setWaypointTime(180);
					break;
				}
			}
		});
	}

	private class checkGuiOptions extends TimerTask {
		public void run() {
			if (guiOptions.getBorder() != comboBoxBorder.getSelectedItem()) {
				comboBoxBorder.setSelectedItem(guiOptions.getBorder());
			}
			if (guiOptions.isSharingLocation()) {
				labelSharing.setText("Sharing location");
				labelSharing.setForeground(Color.GREEN);
			} else {
				labelSharing.setText("Only own location");
				labelSharing.setForeground(Color.RED);
			}
		}
	}
}
