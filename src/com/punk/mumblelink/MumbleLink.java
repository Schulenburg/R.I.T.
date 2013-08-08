package com.punk.mumblelink;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinNT.HANDLE;

public class MumbleLink {

	private final HANDLE sharedFile;

	private int uiVersion;
	private int uiTick;
	private float[] fAvatarPosition;
	private float[] fAvatarFront;
	private float[] fAvatarTop;
	private char[] name;
	private float[] fCameraPosition;
	private float[] fCameraFront;
	private float[] fCameraTop;
	private char[] identity;
	private int context_len;
	private byte[] context;
	private char[] description;

	private String charName;
	private int profession;
	private int mapId;
	private String worldId;
	private int teamColor;
	private boolean isCommander;

	public MumbleLink() {
		final String name = "MumbleLink";

		sharedFile = Kernel32.INSTANCE.CreateFileMapping(
				WinBase.INVALID_HANDLE_VALUE, null, WinNT.PAGE_READWRITE, 0,
				5460, name);

		Timer timer = new Timer();
		timer.schedule(new updateMumbleLink(), 0, 1000);
	}

	private class updateMumbleLink extends TimerTask {
		@Override
		public void run() {

			Pointer sharedMemory = Kernel32.INSTANCE.MapViewOfFile(sharedFile,
					WinNT.SECTION_MAP_READ, 0, 0, 5460);

			if (sharedMemory != null) {
				uiVersion = sharedMemory.getInt(0);
				uiTick = sharedMemory.getInt(4);
				fAvatarPosition = sharedMemory.getFloatArray(8, 3);
				fAvatarFront = sharedMemory.getFloatArray(20, 3);
				fAvatarTop = sharedMemory.getFloatArray(32, 3);
				name = sharedMemory.getCharArray(44, 256);
				fCameraPosition = sharedMemory.getFloatArray(556, 3);
				fCameraFront = sharedMemory.getFloatArray(568, 3);
				fCameraTop = sharedMemory.getFloatArray(580, 3);

				identity = sharedMemory.getCharArray(592, 256);
				context_len = sharedMemory.getInt(1104);
				context = sharedMemory.getByteArray(1108, 256);

				String identityString = "";
				for (char c : identity) {
					identityString += c;
				}

				identityString = identityString.replace("{", "");
				if (identityString.contains("}")) {
					identityString = identityString.substring(0,
							identityString.indexOf("}"));
					String[] identityInfo = identityString.split(",");

					charName = identityInfo[0].split(": ")[1].replace("\"", "")
							.trim();
					profession = Integer
							.parseInt(identityInfo[1].split(": ")[1].replace(
									"\"", "").trim());
					mapId = Integer.parseInt(identityInfo[2].split(": ")[1]
							.replace("\"", "").trim());
					worldId = identityInfo[3].split(": ")[1].replace("\"", "")
							.trim();
					teamColor = Integer.parseInt(identityInfo[4].split(":")[1]
							.replace("\"", "").trim());

					isCommander = Boolean.parseBoolean(identityInfo[5]
							.split(": ")[1].replace("\"", "").trim());

				}
			}
			// System.out.println("fAvatarPosition "
			// + Arrays.toString(fAvatarPosition));
			// description = sharedMemory.getCharArray(1620, 2048);
			// if (!WinBase.INVALID_HANDLE_VALUE.equals(sharedFile)) {
			// Kernel32.INSTANCE.CloseHandle(sharedFile);
			// }
			// print();
		}
	}

	public void print() {
		// System.out.println("uiVersion " + uiVersion);
		// System.out.println("uiTick " + uiTick);
		// System.out.println("fAvatarPosition "
		// + Arrays.toString(fAvatarPosition));
		// System.out.println("fAvatarFront " + Arrays.toString(fAvatarFront));
		// System.out.println("fAvatarTop " + Arrays.toString(fAvatarTop));
		// System.out.println("name " + Arrays.toString(name));
		// System.out.println("fCameraPosition "
		// + Arrays.toString(fCameraPosition));
		// System.out.println("fCameraFront " + Arrays.toString(fCameraFront));
		// System.out.println("fCameraTop " + Arrays.toString(fCameraTop));
		// System.out.println("identity " + Arrays.toString(identity));
		// System.out.println("context_len " + context_len);
		// System.out.println("context " + Arrays.toString(context));

		System.out
				.println("description " + Arrays.toString(description) + "\n");
	}

	public char[] getName() {
		return name;
	}

	public int getUiVersion() {
		return uiVersion;
	}

	public int getUiTick() {
		return uiTick;
	}

	public float[] getfAvatarPosition() {
		return fAvatarPosition;
	}

	public float[] getfAvatarFront() {
		return fAvatarFront;
	}

	public float[] getfAvatarTop() {
		return fAvatarTop;
	}

	public float[] getfCameraPosition() {
		return fCameraPosition;
	}

	public float[] getfCameraFront() {
		return fCameraFront;
	}

	public float[] getfCameraTop() {
		return fCameraTop;
	}

	public char[] getIdentity() {
		return identity;
	}

	public int getContext_len() {
		return context_len;
	}

	public byte[] getContext() {
		return context;
	}

	public char[] getDescription() {
		return description;
	}

	public String getCharName() {
		return charName;
	}

	public int getProfession() {
		if (isCommander) {
			return 0;
		}
		return profession;
	}

	public int getMapId() {
		return mapId;
	}

	public String getWorldId() {
		return worldId;
	}

	public int getTeamColor() {
		return teamColor;
	}

	public boolean isCommander() {
		return isCommander;
	}

	public static void main(String[] args) {
		new MumbleLink();
	}
}
