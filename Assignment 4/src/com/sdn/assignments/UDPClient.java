package com.sdn.assignments;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPClient {

	private int portNum;
	private RequestType type;
	private boolean filter;
	private Operator operator;
	private int referenceValue;

	// int portNum, String type, int referenceValue, boolean filter, String
	// operator

	public static void main(String[] args) {
		new UDPClient(Integer.parseInt(args[0]),
				RequestType.fromString(args[1]), Integer.parseInt(args[2]),
				(Integer.parseInt(args[3])==1), Operator.fromString(args[4]))
				.receive();
	}

	public UDPClient(int portNum, RequestType type, int referenceValue,
			boolean filter, Operator operator) {
		this.portNum = portNum;
		this.referenceValue = referenceValue;
		this.type = type;
		this.filter = filter;
		this.operator = operator;
	}

	public void receive() {
		DatagramSocket dsocket;
		try {
			dsocket = new DatagramSocket(portNum);
			byte[] buffer = new byte[2048];

			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

			while (true) {
				dsocket.receive(packet);
				DEBSPacket p = decode(packet);
				if (filter)
					filter(p);
				else {
					System.out.println(p);
				}
				packet.setLength(buffer.length);
			}
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	/**
	 * Filter packet based on user's reference type
	 * 
	 * @param p
	 *            packet to filter
	 */
	public void filter(DEBSPacket p) {
		if (type == p.getType()) {
			if (operator.check(p.getMeasurement(), referenceValue)) {
				System.out.println(p.toString());
			}
		}
	}

	/*
	 * # row format:
	 * [id],[time],[value],[type],[id_plug],[id_household],[id_house]
	 */
	public DEBSPacket decode(DatagramPacket packet) {
		String msg = new String(packet.getData(), 0, packet.getLength());
		String[] split = msg.split(",");
		return new DEBSPacket.Builder().measurement(Short.parseShort(split[2]))
				.type(RequestType.fromString(split[3])).build();
	}
}
