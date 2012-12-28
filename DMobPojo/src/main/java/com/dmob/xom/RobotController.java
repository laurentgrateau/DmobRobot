package com.dmob.xom;
import java.io.IOException;

public class RobotController {
	TwoWaySerialComm druinoComm;
	boolean druinoAvailable = false;
	private int temperature;
	private int distance;
	/** Static instance */
	private static RobotController crtlInstance;

	/**
	 * Get Controller Instance
	 * @throws Exception 
	 */
	public synchronized static RobotController getInstance() throws Exception {
		if (null == crtlInstance) {
			crtlInstance = new RobotController();
			crtlInstance.connectDruino(TwoWaySerialComm.DefaultPort);
		}
		return crtlInstance;
	}

	/**
	 * Private constructor to avoid creation.
	 */
	private RobotController() {
		druinoComm = new TwoWaySerialComm(new DruinoEventListener(this));

	}

	private String getArduinoPort() {
		String port = null;
		port = System.getenv().get("ARDUINO_PORT");
		if(port == null) port = TwoWaySerialComm.DefaultPort;
		return port;
	}
	public void connectDruino(String port) throws Exception {
		try {
			druinoComm.connect(getArduinoPort(),
					TwoWaySerialComm.DefaultBaud);
		} catch (Exception ex) {
			druinoAvailable = false;
			throw ex;
		}
		druinoAvailable = true;
	}

	public boolean isDruinoAvailable() {
		return druinoAvailable;
	}

	public void turnLeft() {
		sendCommand(DruinoCommand.TURN_LEFT);
	}

	public void turnRight() {
		sendCommand(DruinoCommand.TURN_RIGHT);
	}

	public void moveForward() {
		sendCommand(DruinoCommand.MOVE_FORWARD);
	}

	public void moveBackward() {
		sendCommand(DruinoCommand.MOVE_BACKWARD);
	}

	public void stopRobot() {
		sendCommand(DruinoCommand.STOP);
	}

	public int getDistance() {
		sendCommand(DruinoCommand.DISTANCE);
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getTemperature() {
		sendCommand(DruinoCommand.TEMPERATURE);
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public void pingRobot() {
		sendCommand(DruinoCommand.PING);
	}

	private void sendCommand(DruinoCommand cmd) {
		try {
			if (druinoAvailable)
				druinoComm.sendCommand(cmd);
			else
				// TODO: Throw an exception
				System.err.println("Druino Not Connected");
		} catch (IOException ex) {
			// TODO: Localized
			System.err.println("An error Occured when Sending command");
		}
	}

	public void close() {
		// TODO Auto-generated method stub
		if (druinoComm != null) {
			druinoComm.closeConnection();
		}
	}

}
