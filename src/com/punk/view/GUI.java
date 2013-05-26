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
	private Border[] comboBorderArray = { Border.EB, Border.RED, Border.GREEN,
			Border.BLUE };

	private JButton btnOverlayWvw;
	private JComboBox<Border> comboBoxBorder;
	private JCheckBox checkBoxShowAll;
	private JCheckBox checkBoxAlwaysOnTop;

	public GUI(CapturepointsUtil capUtil) {
		frame = new JFrame("R.I.T.");
		frame.setLayout(new GridLayout(0, 4));
		frame.setSize(600, 60);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.capUtil = capUtil;

		addMenu(frame);
		frame.setVisible(true);
	}

	private void addMenu(JFrame frame) {
		btnOverlayWvw = new JButton("Show overlay");
		comboBoxBorder = new JComboBox<Border>(comboBorderArray);
		checkBoxShowAll = new JCheckBox("Show all", true);
        checkBoxAlwaysOnTop = new JCheckBox("Always On Top", true);

		frame.add(btnOverlayWvw);
		frame.add(comboBoxBorder);
		frame.add(checkBoxShowAll);
		frame.add(checkBoxAlwaysOnTop);

		btnOverlayWvw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (overlay == null) {
					overlay = new Overlay(capUtil, Border.EB);
					overlay.start();
				} else {
					overlay.toggleOverlay();
				}
				if (btnOverlayWvw.getText().startsWith("Show")) {
					btnOverlayWvw.setText("Hide overlay");
				} else {
					btnOverlayWvw.setText("Show overlay");
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
	}
}
