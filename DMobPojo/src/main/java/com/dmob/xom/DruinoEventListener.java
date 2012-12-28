package com.dmob.xom;

public class DruinoEventListener {
	
		RobotController ctrl;
		public DruinoEventListener(RobotController robotController) {
		this.ctrl = robotController;
	}

		public void onMessage(String message) {
			// TODO Auto-generated method stub
			System.out.print("On Message : "+message);
			ctrl.setDistance(0);
			
		}
	
}
