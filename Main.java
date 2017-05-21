package main;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Arrays;

import lejos.hardware.BrickFinder;
import lejos.hardware.Sound;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;
import lejos.utility.Delay;

public class Main {

	protected static final int SEND_PORT = 1337;
	protected static final int RECIEVE_PORT = 1337;
	protected EV3 ev3 = null;
	protected TextLCD lcd = null;
	protected static Socket send_Socket = null;
	protected static Socket recieveSocket = null;
	protected static String IP = "130.243.201.239";
	protected static byte[] messageByte;

	public static void main(String[] args) {
		EV3 ev3 = (EV3) BrickFinder.getLocal();
		final TextLCD lcd = ev3.getTextLCD();
		String mission = "DLPDRUT";
		Wheel wheel = new Wheel();
		Fork fork = new Fork();
		lcd.clear();
//		setupSendSocket();

//		while (true) {
//			Delay.msDelay(100);
//
////			if (Button.DOWN.isDown()) {
////				sendMessage("Lejos Message");
////				Sound.systemSound(true, Sound.BEEP);
////			}
////
////			if (Button.ESCAPE.isDown()) {
////				try {
////					send_Socket.close();
////					recieveSocket.close();
////				} catch (IOException e) {
////					// TODO Auto-generated catch block
////					e.printStackTrace();
////				}
////				return;
////			}
//
//			retrieveMessage();
//		}

		 for (int i = 0; i < mission.length(); i++) {
		 switch (mission.charAt(i)) {
		 case 'U':
		 wheel.setMode(true);
		 wheel.forwardUntiHitSpot();
		 break;
		 case 'D':
		 wheel.setMode(false);
		 wheel.forwardUntiHitSpot();
		 break;
		 case 'L':
		 wheel.moveToSpotCenter();
		 wheel.left90();
		 wheel.forwardUntiHitSpot();
		 break;
		 case 'R':
		 wheel.moveToSpotCenter();
		 wheel.right90();
		 wheel.forwardUntiHitSpot();
		 break;
		 case 'P':
		 fork.up(300, 3000);
		 break;
		 case 'T':
		 fork.down(300, 3000);
		 break;
		 default:
		 break;
		 }
		 }
	}

	static void setupSendSocket() {
		try {
			send_Socket = new Socket(IP, SEND_PORT);
			send_Socket.setTcpNoDelay(true);

			recieveSocket = new Socket(IP, RECIEVE_PORT);
			recieveSocket.setTcpNoDelay(true);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static void retrieveMessage() {
		try {

			String messageString = "";

			DataInputStream in = new DataInputStream(recieveSocket.getInputStream());
			int bytesRead = 0;

			bytesRead = in.read(messageByte);

			if (bytesRead != -1) {
				messageString += new String(messageByte, 0, bytesRead);
				System.out.println("MESSAGE: " + messageString);

				Sound.systemSound(true, Sound.ASCENDING);
			} else {

				System.out.println("No Message");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			System.out.println(e.getLocalizedMessage());

			try {
				recieveSocket = new Socket(IP, RECIEVE_PORT);
				recieveSocket.setTcpNoDelay(true);
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		}
	}

	static void sendMessage(String string) {
		// byte[] b = string.getBytes();
		byte[] b = string.getBytes(Charset.forName("UTF-8"));

		try {
			send_Socket.getOutputStream().write(b);
			send_Socket.getOutputStream().flush();
			send_Socket.getOutputStream().write(0);
			send_Socket.getOutputStream().flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Delay.msDelay(100);
	}
}
