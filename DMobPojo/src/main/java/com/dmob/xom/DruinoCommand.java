package com.dmob.xom;

public enum DruinoCommand {

	TURN_LEFT('l') , TURN_RIGHT('r'), MOVE_FORWARD('f'), MOVE_BACKWARD('b'), STOP('x'), PING('p'),TEMPERATURE('t'),DISTANCE('d');

	private char aChar;

	DruinoCommand(char aChar) {
		this.aChar = aChar;
	}

	public char getChar() {
		return aChar;
	}
}
