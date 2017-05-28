package warehouse.robot.t4.Ev3warehouse;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

import lejos.ev3.tools.LCDDisplay;
import lejos.hardware.Bluetooth;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.lcd.TextLCD;
import lejos.remote.nxt.NXTCommConnector;
import lejos.remote.nxt.NXTConnection;
import lejos.utility.Delay;


public class Main {

	protected static final int SOCKET_PORT = 8051;
	protected static String IP = "130.238.15.109";
	protected static String rpiMAC = "00:0c:78:33:da:22";
	protected static String id;

	protected EV3 ev3 = null;
	protected TextLCD lcd = null;
	protected static Socket inetSocket = null;
	protected static PrintWriter writer = null;
	protected static DataInputStream reader = null;
	protected static byte[] messageBytes;
	protected static int readBytes;
	protected static int buffSize = 256;
	protected static String msg;
	protected static String site;
	protected static String mission;
	protected static NXTCommConnector connector;
	protected static NXTConnection connection;
	protected static boolean connected = false;

	public static void main(String[] args) {
		EV3 ev3 = (EV3) BrickFinder.getLocal();
		final TextLCD lcd = ev3.getTextLCD();
		messageBytes = new byte[256];

		mission = "UD";
		Wheel wheel = new Wheel();
		Fork fork = new Fork();
		lcd.clear();

		LCD.drawString("Select connection:", 0, 0);
		LCD.drawString("Left: UU Right: HUST", 0, 1);
		while(true){
			if (Button.LEFT.isDown()) {
				site = "uu";
				id = "0";
				break;
			}else if(Button.RIGHT.isDown()){
				site= "hust";
				id = "1";
				break;
			}
			
		}
		lcd.clear();
		System.out.println("Site is " + site);
		
		System.out.println(setupComm());
		//here
		try {
			readBytes = reader.read(messageBytes, 0, buffSize);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(readBytes);
		msg = new String(messageBytes, 0, readBytes);
		System.out.println(msg);
		if(msg.equals("Connected")){
			System.out.println("Connected to server!");
			connected = true;
			System.out.println(connected + ", " + msg);
			Sound.beepSequenceUp();
			writer.println(id);
			writer.flush();
			System.out.println("Id sent: " + id);
		}else{
			System.out.println("Something went wrong in connection!");
			connected = false;
		}

		//wait for mission
		while(connected){
			try {
				readBytes = reader.read(messageBytes, 0, buffSize);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (readBytes != -1){
				mission = new String(messageBytes, 0, readBytes);
				System.out.println(mission);
			}

			int squareNumber = 0;

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
				case 'I':
					wheel.greenOnRight = !(wheel.greenOnRight);
				default:
					break;
				}			
			}
			writer.println("END");
			if(Button.ESCAPE.isDown()){
				break;
			}
		}
		try {
			if (connected && site.equals("hust")) {
				inetSocket.close();
				writer.println("Robot leaving");
				writer.flush();
			}else if(connected && site.equals("uu")){
				connection.close();
				writer.println("Robot leaving");
				writer.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Sound.beepSequence();
	}

	/*
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
	 */

	static boolean setupComm(){
		boolean success = false;
		switch (site) {
		case "uu":
			connector = Bluetooth.getNXTCommConnector();
			connection = connector.connect(rpiMAC, NXTConnection.RAW);
			writer = new PrintWriter(connection.openOutputStream());
			reader = new DataInputStream(connection.openDataInputStream());
			success = true;
			break;
		case "hust":
			try {
				inetSocket = new Socket(IP, SOCKET_PORT);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (inetSocket.isConnected()){
				try{
					writer = new PrintWriter(inetSocket.getOutputStream());
					reader = new DataInputStream(inetSocket.getInputStream());					
				}catch (IOException e) {
					// TODO: handle exception
				}
			}
			success = true;
			break;
		default:
			break;
		}
		return success;
	}


}
