package warehouse.robot.t4.Ev3warehouse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;

public class Main {

	protected static final int SOCKET_PORT = 8051;
	protected static String IP = "130.238.15.109";

	protected EV3 ev3 = null;
	protected TextLCD lcd = null;
	protected static Socket inetSocket = null;
	protected static PrintWriter writer = null;
	protected static InputStreamReader reader = null;
	protected static byte[] messageBytes;

	public static void main(String[] args) {
		messageBytes = new byte[256];
		 String mission = "FFRFUSFLFFD";
//		 String mission = "F";
		Wheel wheel = new Wheel();
		Fork fork = new Fork();
		// setupComm();
		

		for (int i = 0; i < mission.length(); i++) {
			switch (mission.charAt(i)) {
			case 'F':
				Actions.Forward(wheel);
				break;
			case 'L':
				Actions.Turn90(wheel, true);
				break;
			case 'R':
				Actions.Turn90(wheel, false);
				break;
			case 'U':
				Actions.ForkUp(fork);
				break;
			case 'D':
				Actions.ForkDown(fork);
				break;
			case 'P':
				Actions.Pickup(wheel, fork);
				break;
			case 'd':
				Actions.Drop(wheel, fork);
				break;
			case 'S':
				Actions.Spin(wheel);
				wheel.mode = !(wheel.mode);
			default:
				break;
			}
		}
	}

	static void setupSendSocket() {
		try {
			inetSocket = new Socket(IP, SOCKET_PORT);
			// send_Socket.setTcpNoDelay(true);

			// recieveSocket = new Socket(IP, RECIEVE_PORT);
			// recieveSocket.setTcpNoDelay(true);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * static void retrieveMessage() { try {
	 * 
	 * String messageString = "";
	 * 
	 * DataInputStream in = new DataInputStream(recieveSocket.getInputStream());
	 * int bytesRead = 0;
	 * 
	 * bytesRead = in.read(messageByte);
	 * 
	 * if (bytesRead != -1) { messageString += new String(messageByte, 0,
	 * bytesRead); System.out.println("MESSAGE: " + messageString);
	 * 
	 * Sound.systemSound(true, Sound.ASCENDING); } else {
	 * 
	 * System.out.println("No Message"); }
	 * 
	 * } catch (IOException e) { // TODO Auto-generated catch block
	 * e.printStackTrace();
	 * 
	 * System.out.println(e.getLocalizedMessage());
	 * 
	 * try { recieveSocket = new Socket(IP, RECIEVE_PORT);
	 * recieveSocket.setTcpNoDelay(true); } catch (IOException e2) { // TODO
	 * Auto-generated catch block e2.printStackTrace(); } } }
	 */

	static void setupComm() {

	}

}
