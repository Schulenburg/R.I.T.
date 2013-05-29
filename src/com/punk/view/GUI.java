package com.punk.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;

import com.punk.model.Border;
import com.punk.model.CapturepointsUtil;

/**
 * @author Sander Schulenburg aka "Much"(schulenburgsander@gmail.com)
 */
public class GUI {

	private Overlay overlay = null;
	private JFrame frame = null;
	private CapturepointsUtil capUtil = null;
	private Border[] comboBorderArray = { Border.EB, Border.RED, Border.GREEN, Border.BLUE };
	private Overlay.Size[] comboOverlaySizeArray = { Overlay.Size.LARGE, Overlay.Size.MEDIUM, Overlay.Size.SMALL, Overlay.Size.VERY_SMALL };
	private Overlay.Type[] comboOverlayTypeArray = { Overlay.Type.Icons, Overlay.Type.Text };

	private JButton btnOverlayWvw;
	private JComboBox<Border> comboBoxBorder;
	private JComboBox<Overlay.Type> comboBoxOverlayType;
	private JComboBox<Overlay.Size> comboBoxOverlaySize;
	private JCheckBox checkBoxShowAll;
	private JCheckBox checkBoxAlwaysOnTop;
	private JCheckBox checkBoxShowNames;

	public GUI(CapturepointsUtil capUtil) {
		frame = new JFrame("R.I.T.");
		frame.setLayout(new GridLayout(0, 4));
		frame.setSize(600, 80);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.capUtil = capUtil;

		addMenu(frame);
		frame.setVisible(true);

        overlay = new Overlay(capUtil, Border.EB, Overlay.Type.Icons, Overlay.Size.LARGE);
        overlay.start();
	}

	private void addMenu(JFrame frame) {
		btnOverlayWvw = new JButton("Show overlay");
		comboBoxBorder = new JComboBox<Border>(comboBorderArray);
		comboBoxOverlaySize = new JComboBox<Overlay.Size>(comboOverlaySizeArray);
		comboBoxOverlayType = new JComboBox<Overlay.Type>(comboOverlayTypeArray);
		checkBoxShowAll = new JCheckBox("Show all", true);
        checkBoxAlwaysOnTop = new JCheckBox("Always On Top", true);
        checkBoxShowNames = new JCheckBox("Show Names", true);

		frame.add(btnOverlayWvw);
		frame.add(comboBoxBorder);
		frame.add(comboBoxOverlayType);
		frame.add(comboBoxOverlaySize);

		frame.add(checkBoxShowAll);
		frame.add(checkBoxAlwaysOnTop);
		frame.add(checkBoxShowNames);

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
					overlay.setSize((Overlay.Size) comboBoxOverlaySize.getSelectedItem());
				}
			}
		});

		comboBoxOverlayType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (overlay != null) {
					overlay.setType((Overlay.Type) comboBoxOverlayType.getSelectedItem());
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

        checkBoxAlwaysOnTop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (overlay != null) {
					overlay.toggleAlwaysOnTop();
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
	}
}
