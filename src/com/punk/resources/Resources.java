package com.punk.resources;

import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Resources {
	public static ImageIcon IMAGE_CAMP_RED = null;
	public static ImageIcon IMAGE_CAMP_BLUE = null;
	public static ImageIcon IMAGE_CAMP_GREEN = null;
	public static ImageIcon IMAGE_CAMP_NEUTRAL = null;
	public static ImageIcon IMAGE_TOWER_RED = null;
	public static ImageIcon IMAGE_TOWER_BLUE = null;
	public static ImageIcon IMAGE_TOWER_GREEN = null;
	public static ImageIcon IMAGE_TOWER_NEUTRAL = null;
	public static ImageIcon IMAGE_KEEP_RED = null;
	public static ImageIcon IMAGE_KEEP_BLUE = null;
	public static ImageIcon IMAGE_KEEP_GREEN = null;
	public static ImageIcon IMAGE_KEEP_NEUTRAL = null;
	public static ImageIcon IMAGE_CASTLE_RED = null;
	public static ImageIcon IMAGE_CASTLE_BLUE = null;
	public static ImageIcon IMAGE_CASTLE_GREEN = null;
	public static ImageIcon IMAGE_CASTLE_NEUTRAL = null;

	public static Image IMAGE_MAP_EB = null;
	public static Image IMAGE_MAP_BORDER = null;

	public static ImageIcon IMAGE_CLASS_ELEMENTALIST = null;

	public static boolean load() {
		try {
			IMAGE_CAMP_RED = new ImageIcon(
					Resources.class
							.getResource("/com/punk/resources/images/map/camp_red.png"));
			IMAGE_CAMP_BLUE = new ImageIcon(
					Resources.class
							.getResource("/com/punk/resources/images/map/camp_blue.png"));
			IMAGE_CAMP_GREEN = new ImageIcon(
					Resources.class
							.getResource("/com/punk/resources/images/map/camp_green.png"));
			IMAGE_CAMP_NEUTRAL = new ImageIcon(
					Resources.class
							.getResource("/com/punk/resources/images/map/camp_neutral.png"));
			IMAGE_TOWER_RED = new ImageIcon(
					Resources.class
							.getResource("/com/punk/resources/images/map/tower_red.png"));
			IMAGE_TOWER_BLUE = new ImageIcon(
					Resources.class
							.getResource("/com/punk/resources/images/map/tower_blue.png"));
			IMAGE_TOWER_GREEN = new ImageIcon(
					Resources.class
							.getResource("/com/punk/resources/images/map/tower_green.png"));
			IMAGE_TOWER_NEUTRAL = new ImageIcon(
					Resources.class
							.getResource("/com/punk/resources/images/map/tower_neutral.png"));
			IMAGE_KEEP_RED = new ImageIcon(
					Resources.class
							.getResource("/com/punk/resources/images/map/keep_red.png"));
			IMAGE_KEEP_BLUE = new ImageIcon(
					Resources.class
							.getResource("/com/punk/resources/images/map/keep_blue.png"));
			IMAGE_KEEP_GREEN = new ImageIcon(
					Resources.class
							.getResource("/com/punk/resources/images/map/keep_green.png"));
			IMAGE_KEEP_NEUTRAL = new ImageIcon(
					Resources.class
							.getResource("/com/punk/resources/images/map/keep_neutral.png"));
			IMAGE_CASTLE_RED = new ImageIcon(
					Resources.class
							.getResource("/com/punk/resources/images/map/castle_red.png"));
			IMAGE_CASTLE_BLUE = new ImageIcon(
					Resources.class
							.getResource("/com/punk/resources/images/map/castle_blue.png"));
			IMAGE_CASTLE_GREEN = new ImageIcon(
					Resources.class
							.getResource("/com/punk/resources/images/map/castle_green.png"));
			IMAGE_CASTLE_NEUTRAL = new ImageIcon(
					Resources.class
							.getResource("/com/punk/resources/images/map/castle_neutral.png"));

			IMAGE_MAP_EB = ImageIO.read(Resources.class
					.getResource("/com/punk/resources/images/map/map_eb.png"));
			IMAGE_MAP_BORDER = ImageIO
					.read(Resources.class
							.getResource("/com/punk/resources/images/map/map_borderland.png"));

			IMAGE_CLASS_ELEMENTALIST = new ImageIcon(
					Resources.class
							.getResource("/com/punk/resources/images/classes/class_elementalist.png"));

			return true;
		} catch (Exception ignore) {
			ignore.printStackTrace();
		}
		return false;
	}

	public static void dispose() {
	}
}
