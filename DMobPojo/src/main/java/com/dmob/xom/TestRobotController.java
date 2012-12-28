package com.dmob.xom;
import java.io.IOException;

public class TestRobotController {

	/** */
	public static class KeyBoardInputCommand implements Runnable {
		RobotController ctrl;

		public KeyBoardInputCommand(RobotController ctrl) {
			this.ctrl = ctrl;
		}

		public void run() {
			try {
				char c = 0;
				while ((c = (char) System.in.read()) != 'q') {
					System.out.print(c);
					switch (c) {
					case 'l':
						ctrl.turnLeft();
						break;
					case 'r':
						ctrl.turnRight();
						break;
					case 'f':
						ctrl.moveForward();
						break;
					case 'b':
						ctrl.moveBackward();
						break;
					case 's':
						ctrl.stopRobot();
						break;
					case 'p':
						ctrl.pingRobot();
						break;
					case 'd':
						ctrl.getDistance();
						break;
					case 't':
						ctrl.getTemperature();
						break;
					}

				}
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		RobotController ctrl = RobotController.getInstance();
	
		System.out.println("DDD : "+DruinoCommand.valueOf("TURN_LEFT1"));
		Thread th ;
		try {

			ctrl.connectDruino(TwoWaySerialComm.DefaultPort);
			System.out.println("Arduino Connected");
			System.out.println("-----------------");
			System.out.println("Please Enter command: ()");
			th = new Thread(new KeyBoardInputCommand(ctrl));
			th.start();
			th.join();
			if (ctrl != null)
				ctrl.close();
		} finally {
			
			
		}
	}
}
