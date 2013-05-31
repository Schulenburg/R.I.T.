package com.punk.resources;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

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

    public static boolean load() {
        try {
            IMAGE_CAMP_RED = new ImageIcon(Resources.class.getResource("/com/punk/resources/images/camp_red.png"));
            IMAGE_CAMP_BLUE = new ImageIcon(Resources.class.getResource("/com/punk/resources/images/camp_blue.png"));
            IMAGE_CAMP_GREEN = new ImageIcon(Resources.class.getResource("/com/punk/resources/images/camp_green.png"));
            IMAGE_CAMP_NEUTRAL = new ImageIcon(Resources.class.getResource("/com/punk/resources/images/camp_neutral.png"));
            IMAGE_TOWER_RED = new ImageIcon(Resources.class.getResource("/com/punk/resources/images/tower_red.png"));
            IMAGE_TOWER_BLUE = new ImageIcon(Resources.class.getResource("/com/punk/resources/images/tower_blue.png"));
            IMAGE_TOWER_GREEN = new ImageIcon(Resources.class.getResource("/com/punk/resources/images/tower_green.png"));
            IMAGE_TOWER_NEUTRAL = new ImageIcon(Resources.class.getResource("/com/punk/resources/images/tower_neutral.png"));
            IMAGE_KEEP_RED = new ImageIcon(Resources.class.getResource("/com/punk/resources/images/keep_red.png"));
            IMAGE_KEEP_BLUE = new ImageIcon(Resources.class.getResource("/com/punk/resources/images/keep_blue.png"));
            IMAGE_KEEP_GREEN = new ImageIcon(Resources.class.getResource("/com/punk/resources/images/keep_green.png"));
            IMAGE_KEEP_NEUTRAL = new ImageIcon(Resources.class.getResource("/com/punk/resources/images/keep_neutral.png"));
            IMAGE_CASTLE_RED = new ImageIcon(Resources.class.getResource("/com/punk/resources/images/castle_red.png"));
            IMAGE_CASTLE_BLUE = new ImageIcon(Resources.class.getResource("/com/punk/resources/images/castle_blue.png"));
            IMAGE_CASTLE_GREEN = new ImageIcon(Resources.class.getResource("/com/punk/resources/images/castle_green.png"));
            IMAGE_CASTLE_NEUTRAL = new ImageIcon(Resources.class.getResource("/com/punk/resources/images/castle_neutral.png"));

            IMAGE_MAP_EB = ImageIO.read(Resources.class.getResource("/com/punk/resources/images/map_eb.png"));
            IMAGE_MAP_BORDER = ImageIO.read(Resources.class.getResource("/com/punk/resources/images/map_borderland.png"));

            return true;
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
        return false;
    }

    public static void dispose() {
    }
}

