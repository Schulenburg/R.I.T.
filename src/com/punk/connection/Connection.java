package com.punk.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;

import com.punk.model.Capturepoint;
import com.punk.start.Start;
import com.punk.start.Start.Border;

public class Connection {
	private static Connection instance = null;
	private String ip = Start.ip;

	protected Connection() {
	}

	public static Connection getInstance() {
		if (instance == null) {
			instance = new Connection();
		}
		return instance;
	}

	public double getVersion() {
		double serverVersion = -1;
		Socket socket = null;

		try {
			socket = new Socket(ip, 11111);

		} catch (UnknownHostException uhe) {
			socket = null;
		} catch (IOException ioe) {
			socket = null;
		}

		if (socket != null) {
			ObjectInputStream in = null;
			PrintWriter out = null;

			try {
				in = new ObjectInputStream(socket.getInputStream());
				out = new PrintWriter(new OutputStreamWriter(
						socket.getOutputStream()));
				out.println("version");
				out.flush();

				serverVersion = in.readDouble();
				out.println("Quit");
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					out.close();
					in.close();
					socket.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return serverVersion;
	}

	public Map<Border, Map<Integer, Capturepoint>> getTimers(
			Map<Border, Map<Integer, Capturepoint>> capturePoints) {
		Socket socket = null;

		try {
			socket = new Socket(Start.ip, 11111);

		} catch (UnknownHostException uhe) {
			socket = null;
		} catch (IOException ioe) {
			socket = null;
		}

		if (socket != null) {
			ObjectInputStream in = null;
			PrintWriter out = null;

			try {
				in = new ObjectInputStream(socket.getInputStream());
				out = new PrintWriter(new OutputStreamWriter(
						socket.getOutputStream()));
				out.println("data");
				out.flush();

				try {
					@SuppressWarnings("unchecked")
					Map<Integer, Map<Integer, Integer>> capturepoints = (Map<Integer, Map<Integer, Integer>>) in
							.readObject();

					for (int i : capturepoints.get(1).keySet()) {
						capturePoints.get(Border.EB).get(i)
								.setRiTime(capturepoints.get(1).get(i));
					}
					for (int i : capturepoints.get(2).keySet()) {
						capturePoints.get(Border.RED).get(i)
								.setRiTime(capturepoints.get(2).get(i));
					}
					for (int i : capturepoints.get(3).keySet()) {
						capturePoints.get(Border.BLUE).get(i)
								.setRiTime(capturepoints.get(3).get(i));
					}
					for (int i : capturepoints.get(4).keySet()) {
						capturePoints.get(Border.GREEN).get(i)
								.setRiTime(capturepoints.get(4).get(i));
					}

				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				out.println("Quit");
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					out.close();
					in.close();
					socket.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return capturePoints;
	}
}
