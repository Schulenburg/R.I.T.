package com.punk.model;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sander Schulenburg aka "Much"(schulenburgsander@gmail.com)
 */
public class CapturepointsUtil {

	private Map<Border, Map<Integer, Capturepoint>> capturePoints;
	private Map<Integer, Capturepoint> capturePointsEB;
	private Map<Integer, Capturepoint> capturePointsRed;
	private Map<Integer, Capturepoint> capturePointsGreen;
	private Map<Integer, Capturepoint> capturePointsBlue;

	public CapturepointsUtil() {

		capturePoints = new HashMap<Border, Map<Integer, Capturepoint>>();
		capturePointsEB = new HashMap<Integer, Capturepoint>();
		capturePointsRed = new HashMap<Integer, Capturepoint>();
		capturePointsGreen = new HashMap<Integer, Capturepoint>();
		capturePointsBlue = new HashMap<Integer, Capturepoint>();

		createCapturepoints();

		capturePoints.put(Border.EB, capturePointsEB);
		capturePoints.put(Border.RED, capturePointsRed);
		capturePoints.put(Border.GREEN, capturePointsGreen);
		capturePoints.put(Border.BLUE, capturePointsBlue);
	}

	public ArrayList<Capturepoint> getCapturepoints(Border border) {
		ArrayList<Capturepoint> points = new ArrayList<Capturepoint>();
		for (Capturepoint c : capturePoints.get(border).values()) {
			points.add(c);
		}
		return points;
	}

	public Capturepoint getCapturepoint(Border border, int id) {
		return capturePoints.get(border).get(id);
	}

	public void setOwner(int id, Color server, Border border) {
		Capturepoint point = capturePoints.get(border).get(id);
		if (point.getServer() != server) {
			point.setServer(server);
		}
	}

	public void switchOwner(int id, Color server, Border border) {
		Capturepoint point = capturePoints.get(border).get(id);
		if (point.getServer() != server) {
			if (!point.getInitialized()) {
				point.setServer(server);
				point.setInitialized(true);
			} else {
				point.flip(server);
			}
		}
	}

	private void createCapturepoints() {
		createMap();
	}

	private void createMap() {
		createEb();
		createBorderRed();
		createBorderGreen();
		createBorderBlue();
	}

	private void createEb() {
		capturePointsEB.put(9, new Capturepoint(9, "Stonemist Castle", 35,
				Capturepoint.Type.Castle, new Point(274, 264)));

		capturePointsEB.put(1, new Capturepoint(1, "Overlook", 25,
				Capturepoint.Type.Keep, new Point(130, 278)));
		capturePointsEB.put(17, new Capturepoint(17, "Mendon's Gap", 10,
				Capturepoint.Type.Tower, new Point(96, 200)));
		capturePointsEB.put(6, new Capturepoint(6, "Speldan Clearcut", 5,
				Capturepoint.Type.Camp, new Point(128, 108)));
		capturePointsEB.put(18, new Capturepoint(18, "Anzalias Pass", 10,
				Capturepoint.Type.Tower, new Point(190, 192)));
		capturePointsEB.put(20, new Capturepoint(20, "Veloka Slope", 10,
				Capturepoint.Type.Tower, new Point(92, 328)));
		capturePointsEB.put(5, new Capturepoint(5, "Pangloss Rise", 5,
				Capturepoint.Type.Camp, new Point(146, 368)));
		capturePointsEB.put(19, new Capturepoint(19, "Ogrewatch Cut", 10,
				Capturepoint.Type.Tower, new Point(166, 316)));

		capturePointsEB.put(2, new Capturepoint(2, "Valley", 25,
				Capturepoint.Type.Keep, new Point(360, 394)));
		capturePointsEB.put(22, new Capturepoint(22, "Bravost Escarpment", 10,
				Capturepoint.Type.Tower, new Point(310, 446)));
		capturePointsEB.put(8, new Capturepoint(8, "Umberglade Woods", 5,
				Capturepoint.Type.Camp, new Point(250, 412)));
		capturePointsEB.put(21, new Capturepoint(21, "Durios Gulch", 10,
				Capturepoint.Type.Tower, new Point(262, 350)));
		capturePointsEB.put(15, new Capturepoint(15, "Langor Gulch", 10,
				Capturepoint.Type.Tower, new Point(424, 394)));
		capturePointsEB.put(7, new Capturepoint(7, "Danelon Passage", 5,
				Capturepoint.Type.Camp, new Point(438, 320)));
		capturePointsEB.put(16, new Capturepoint(16, "Quentin Lake", 10,
				Capturepoint.Type.Tower, new Point(380, 286)));

		capturePointsEB.put(3, new Capturepoint(3, "Lowlands", 25,
				Capturepoint.Type.Keep, new Point(360, 98)));
		capturePointsEB.put(13, new Capturepoint(13, "Jerrifer's Slough", 10,
				Capturepoint.Type.Tower, new Point(410, 128)));
		capturePointsEB.put(4, new Capturepoint(4, "Golanta Clearing", 5,
				Capturepoint.Type.Camp, new Point(416, 186)));
		capturePointsEB.put(14, new Capturepoint(14, "Klovan Gully", 10,
				Capturepoint.Type.Tower, new Point(354, 188)));
		capturePointsEB.put(11, new Capturepoint(11, "Aldon's Ledge", 10,
				Capturepoint.Type.Tower, new Point(310, 56)));
		capturePointsEB.put(10, new Capturepoint(10, "Rogue's Quarry", 5,
				Capturepoint.Type.Camp, new Point(248, 88)));
		capturePointsEB.put(12, new Capturepoint(12, "Wildcreek Run", 10,
				Capturepoint.Type.Tower, new Point(276, 150)));
	}

	private void createBorderRed() {
		capturePointsRed.put(37, new Capturepoint(37, "Garrison", 25,
				Capturepoint.Type.Keep, new Point(282, 238)));
		capturePointsRed.put(33, new Capturepoint(33, "Dreaming Bay", 25,
				Capturepoint.Type.Keep, new Point(384, 40)));
		capturePointsRed.put(32, new Capturepoint(32, "Etheron Hills", 25,
				Capturepoint.Type.Keep, new Point(372, 468)));

		capturePointsRed.put(38, new Capturepoint(38, "Longview", 10,
				Capturepoint.Type.Tower, new Point(210, 125)));
		capturePointsRed.put(40, new Capturepoint(40, "Cliffside", 10,
				Capturepoint.Type.Tower, new Point(208, 356)));
		capturePointsRed.put(35, new Capturepoint(35, "Greenbriar", 10,
				Capturepoint.Type.Tower, new Point(460, 168)));
		capturePointsRed.put(36, new Capturepoint(36, "Bluelake", 10,
				Capturepoint.Type.Tower, new Point(470, 338)));

		capturePointsRed.put(39, new Capturepoint(39, "The Godsword", 5,
				Capturepoint.Type.Camp, new Point(50, 244)));
		capturePointsRed.put(52, new Capturepoint(52, "Arah's Hope", 5,
				Capturepoint.Type.Camp, new Point(240, 72)));
		capturePointsRed.put(51, new Capturepoint(51, "Astralholme", 5,
				Capturepoint.Type.Camp, new Point(224, 430)));
		capturePointsRed.put(53, new Capturepoint(53, "Greenvale Refuge", 5,
				Capturepoint.Type.Camp, new Point(484, 82)));
		capturePointsRed.put(50, new Capturepoint(50, "Bluewater Lowlands", 5,
				Capturepoint.Type.Camp, new Point(490, 426)));
		capturePointsRed.put(34, new Capturepoint(34, "Victors's Lodge", 5,
				Capturepoint.Type.Camp, new Point(600, 242)));
	}

	private void createBorderGreen() {
		capturePointsGreen.put(46, new Capturepoint(46, "Garrison", 25,
				Capturepoint.Type.Keep, new Point(282, 238)));
		capturePointsGreen.put(44, new Capturepoint(44, "Dreadfall Bay", 25,
				Capturepoint.Type.Keep, new Point(384, 40)));
		capturePointsGreen.put(41, new Capturepoint(41, "Shadaran Hills", 25,
				Capturepoint.Type.Keep, new Point(372, 468)));

		capturePointsGreen.put(47, new Capturepoint(47, "Sunnyhill", 10,
				Capturepoint.Type.Tower, new Point(210, 125)));
		capturePointsGreen.put(57, new Capturepoint(57, "Cragtop", 10,
				Capturepoint.Type.Tower, new Point(208, 356)));
		capturePointsGreen.put(45, new Capturepoint(45, "Bluebriar", 10,
				Capturepoint.Type.Tower, new Point(460, 168)));
		capturePointsGreen.put(42, new Capturepoint(42, "Redlake", 10,
				Capturepoint.Type.Tower, new Point(470, 338)));

		capturePointsGreen.put(56, new Capturepoint(56, "The Titanpaw", 5,
				Capturepoint.Type.Camp, new Point(50, 244)));
		capturePointsGreen.put(43, new Capturepoint(43, "Hero's Lodge", 5,
				Capturepoint.Type.Camp, new Point(600, 242)));
		capturePointsGreen.put(48, new Capturepoint(48, "Faithleap", 5,
				Capturepoint.Type.Camp, new Point(240, 72)));
		capturePointsGreen.put(54, new Capturepoint(54, "Foghaven", 5,
				Capturepoint.Type.Camp, new Point(224, 430)));
		capturePointsGreen.put(49, new Capturepoint(49, "Bluevale Refuge", 5,
				Capturepoint.Type.Camp, new Point(484, 82)));
		capturePointsGreen.put(55, new Capturepoint(55, "Redwater Lowlands", 5,
				Capturepoint.Type.Camp, new Point(490, 426)));
	}

	private void createBorderBlue() {
		capturePointsBlue.put(23, new Capturepoint(23, "Garrison", 25,
				Capturepoint.Type.Keep, new Point(282, 238)));
		capturePointsBlue.put(27, new Capturepoint(27, "Ascension Bay", 25,
				Capturepoint.Type.Keep, new Point(384, 40)));
		capturePointsBlue.put(31, new Capturepoint(31, "Askalion Hills", 25,
				Capturepoint.Type.Keep, new Point(372, 468)));

		capturePointsBlue.put(30, new Capturepoint(30, "Woodhaven", 10,
				Capturepoint.Type.Tower, new Point(210, 125)));
		capturePointsBlue.put(28, new Capturepoint(28, "Dawn's Eyrie", 10,
				Capturepoint.Type.Tower, new Point(208, 356)));
		capturePointsBlue.put(25, new Capturepoint(25, "Redbriar", 10,
				Capturepoint.Type.Tower, new Point(460, 168)));
		capturePointsBlue.put(26, new Capturepoint(26, "Greenlake", 10,
				Capturepoint.Type.Tower, new Point(470, 338)));

		capturePointsBlue.put(29, new Capturepoint(29, "The Spiritholme", 5,
				Capturepoint.Type.Camp, new Point(50, 244)));
		capturePointsBlue.put(58, new Capturepoint(58, "Godslore", 5,
				Capturepoint.Type.Camp, new Point(240, 72)));
		capturePointsBlue.put(60, new Capturepoint(60, "Stargrove", 5,
				Capturepoint.Type.Camp, new Point(224, 430)));
		capturePointsBlue.put(59, new Capturepoint(59, "Redvale Refuge", 5,
				Capturepoint.Type.Camp, new Point(484, 82)));
		capturePointsBlue.put(61, new Capturepoint(61, "Greenwater Lowlands",
				5, Capturepoint.Type.Camp, new Point(490, 426)));
		capturePointsBlue.put(24, new Capturepoint(24, "Champion's demense", 5,
				Capturepoint.Type.Camp, new Point(600, 242)));
	}
}
