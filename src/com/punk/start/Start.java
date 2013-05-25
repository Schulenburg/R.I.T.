package com.punk.start;

import jGW2API.jGW2API;

import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;

import com.punk.model.Border;
import com.punk.model.CapturepointsUtil;
import com.punk.view.GUI;

//TODO: Alpha channel 
//TODO: Alpha channel slider
//TODO: overlay slider horizontal
//TODO: overlay slider vertical
//TODO: retry after API failure

public class Start {

	private static final String SERVERNAME = "far shiverpeaks";
	private static int id = -1;
	private static String match_id = null;

	private static CapturepointsUtil capUtil;
	private static Timer timer = null;

	/**
	 * @author Sander Schulenburg aka "Much"(schulenburgsander@gmail.com)
	 * @param args
	 * @throws JSONException
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		capUtil = new CapturepointsUtil();

		findServer(SERVERNAME);
		findmatch(id);

		timer = new Timer();
		timer.schedule(new updateMatchDetails(), 0, 15000);
		new GUI(capUtil);
	}

	private static void findServer(String servername) {
		try {
			JSONArray jsonWorlds = jGW2API.getWorldNames();

			for (int i = 0; i < jsonWorlds.length(); i++) {
				if (jsonWorlds.getJSONObject(i).get("name").toString()
						.toLowerCase().startsWith(servername)) {

					id = jsonWorlds.getJSONObject(i).getInt("id");
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void findmatch(int serverId) {
		try {
			JSONArray jsonMatches = jGW2API.getWvWMatches();
			for (int i = 0; i < jsonMatches.length(); i++) {
				if (jsonMatches.getJSONObject(i).get("blue_world_id")
						.equals(id)
						|| jsonMatches.getJSONObject(i).get("green_world_id")
								.equals(id)
						|| jsonMatches.getJSONObject(i).get("red_world_id")
								.equals(id)) {
					match_id = jsonMatches.getJSONObject(i).getString(
							"wvw_match_id");
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static class updateMatchDetails extends TimerTask {
		public void run() {
			try {
				JSONArray jsonMatchDetails = jGW2API
						.getWvWMatchDetails(match_id);
				// System.err.println(jsonMatchDetails.get(0));
				JSONArray jsonMatchDetailsBorder1 = new JSONArray(
						jsonMatchDetails.get(0).toString()
								.replace("{\"objectives\":", ""));
				JSONArray jsonMatchDetailsBorder2 = new JSONArray(
						jsonMatchDetails.get(1).toString()
								.replace("{\"objectives\":", ""));
				JSONArray jsonMatchDetailsBorder3 = new JSONArray(
						jsonMatchDetails.get(2).toString()
								.replace("{\"objectives\":", ""));
				JSONArray jsonMatchDetailsEb = new JSONArray(jsonMatchDetails
						.get(3).toString().replace("{\"objectives\":", ""));

				// System.out.println("Border red:");
				for (int i = 0; i < jsonMatchDetailsBorder1.length(); i++) {
					// System.out.println("	" + jsonMatchDetailsBorder1.get(i));
					int id = jsonMatchDetailsBorder1.getJSONObject(i).getInt(
							"id");
					Color server = getColor(jsonMatchDetailsBorder1
							.getJSONObject(i).getString("owner"));
					capUtil.switchOwner(id, server, Border.RED);
				}

				// System.out.println("Border green:");
				for (int i = 0; i < jsonMatchDetailsBorder2.length(); i++) {
					// System.out.println("	" + jsonMatchDetailsBorder2.get(i));
					int id = jsonMatchDetailsBorder2.getJSONObject(i).getInt(
							"id");
					Color server = getColor(jsonMatchDetailsBorder2
							.getJSONObject(i).getString("owner"));
					capUtil.switchOwner(id, server, Border.GREEN);
				}

				// System.out.println("Border blue:");
				for (int i = 0; i < jsonMatchDetailsBorder3.length(); i++) {
					// System.out.println("	" + jsonMatchDetailsBorder3.get(i));
					int id = jsonMatchDetailsBorder3.getJSONObject(i).getInt(
							"id");
					Color server = getColor(jsonMatchDetailsBorder3
							.getJSONObject(i).getString("owner"));
					capUtil.switchOwner(id, server, Border.BLUE);
				}

				// System.out.println("EB:");
				for (int i = 0; i < jsonMatchDetailsEb.length(); i++) {
					// System.out.println("	" + jsonMatchDetailsEb.get(i));
					int id = jsonMatchDetailsEb.getJSONObject(i).getInt("id");
					Color server = getColor(jsonMatchDetailsEb.getJSONObject(i)
							.getString("owner"));
					capUtil.switchOwner(id, server, Border.EB);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static Color getColor(String name) {
		switch (name) {
		case "Red":
			return Color.RED;
		case "Green":
			return Color.GREEN;
		case "Blue":
			return Color.BLUE;
		}
		return Color.GRAY;
	}
}
