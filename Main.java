package main;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import lejos.hardware.BrickFinder;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;
import lejos.utility.Delay;

public class Main {

	protected static final int SEND_PORT = 8051;
	protected static final int RECIEVE_PORT = 8051;
	protected EV3 ev3 = null;
	protected TextLCD lcd = null;
	protected static PrintWriter writer = null;
	protected static InputStreamReader reader = null;
	protected static Socket sconnection;
	// protected static String IP = "130.243.201.239";
	// protected static String IP = "ctrl.gspd4.student.it.uu.se";
	protected static String IP = "130.238.15.109";
	protected static byte[] messageByte;

	public static void main(String[] args) {
		EV3 ev3 = (EV3) BrickFinder.getLocal();
		final TextLCD lcd = ev3.getTextLCD();
		// String mission = "PBRFTBRB";
		String mission = "BRFBRB";
		Wheel wheel = new Wheel();
		Fork fork = new Fork();
		lcd.clear();
		setupSendSocket();

		while (true) {
			Delay.msDelay(100);
			String a = retrieveMessage("Hello");
		}

		// for (int i = 0; i < mission.length(); i++) {
		// switch (mission.charAt(i)) {
		// case 'F':
		// wheel.setMode(true);
		// wheel.forwardUntiHitSpot();
		// break;
		// case 'B':
		// wheel.backwardThroughBlack();
		// wheel.setMode(false);
		// wheel.forwardUntiHitSpot();
		// break;
		// case 'L':
		// wheel.escapeBlack();
		// wheel.moveToSpotCenter();
		// wheel.left90();
		// wheel.escapeBlack();
		// wheel.forwardUntiHitSpot();
		// break;
		// case 'R':
		// wheel.escapeBlack();
		// wheel.moveToSpotCenter();
		// wheel.right90();
		// wheel.escapeBlack();
		// wheel.forwardUntiHitSpot();
		// break;
		// case 'P':
		// fork.up(300, 3000);
		// break;
		// case 'T':
		// fork.down(300, 3000);
		// break;
		// default:
		// break;
		// }
		// }
	}

	static void setupSendSocket() {
		try {
			sconnection = new Socket(IP, SEND_PORT);
			writer = new PrintWriter(sconnection.getOutputStream());
			reader = new InputStreamReader(sconnection.getInputStream());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static String retrieveMessage(String command) {
		System.out.println("vao day roi ");
		writer.println(command);
		char[] msg = new char[100];
		try {
			int bytesRead = reader.read(msg);
			String messageString = "";
			messageString += new String(msg, 0, bytesRead);
			System.out.println("MESSAGE: " + messageString);
			return messageString;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("MESSAGE: FAILASDASDASDASD");
			e.printStackTrace();
		}
		return null;
	}
}
