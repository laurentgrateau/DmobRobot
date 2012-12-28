package com.dmob.xom;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This version of the TwoWaySerialComm example makes use of the
 * SerialPortEventListener to avoid polling.
 * 
 */
public class TwoWaySerialComm {
	CommPort commPort;
	// For Linux
	public static final String DefaultPort = "/dev/ttyACM1";
	public static final int DefaultBaud = 19200;
	private OutputStream out;
	private InputStream in;
	private static DruinoEventListener listener;

	public TwoWaySerialComm(DruinoEventListener listener) {
		this.listener = listener;
	}

	void connect(String portName, int baud) throws Exception {
		System.out.println("Trying to connect to port : " + portName
				+ " with speed : " + baud + " baud");
		try {
			CommPortIdentifier portIdentifier = CommPortIdentifier
					.getPortIdentifier(portName);
			if (portIdentifier.isCurrentlyOwned()) {
				System.out.println("Error: Port is currently in use");
			} else {
				commPort = portIdentifier.open(this.getClass().getName(), 2000);

				if (commPort instanceof SerialPort) {
					SerialPort serialPort = (SerialPort) commPort;
					serialPort.setSerialPortParams(baud, SerialPort.DATABITS_8,
							SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

					in = serialPort.getInputStream();
					out = serialPort.getOutputStream();

					// (new Thread(new SerialWriter(out))).start();

					serialPort.addEventListener(new SerialReader(in));
					serialPort.notifyOnDataAvailable(true);
					System.out.print("System connected to the arduino");

				} else {
					System.out
							.println("Error: Only serial ports are handled by this example.");
				}
			}
		} catch (Exception ex) {
			System.out.println("Unable to connect to the arduino");
			throw ex;
		}

	}

	public void sendCommand(DruinoCommand cmd) throws IOException {
		out.write(cmd.getChar());
	}

	/**
	 * Handles the input coming from the serial port. A new line character is
	 * treated as the end of a block in this example.
	 */
	public static class SerialReader implements SerialPortEventListener {
		private InputStream in;
		private byte[] buffer = new byte[1024];

		public SerialReader(InputStream in) {
			this.in = in;
		}

		public void serialEvent(SerialPortEvent arg0) {
			int data;

			try {
				int len = 0;
				while ((data = in.read()) > -1) {
					if (data == '\n') {
						break;
					}
					buffer[len++] = (byte) data;
				}
				// System.out.print(new String(buffer, 0, len));
				listener.onMessage(new String(buffer, 0, len));
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}

	}

	/** */
	public static class SerialWriter implements Runnable {
		OutputStream out;

		public SerialWriter(OutputStream out) {
			this.out = out;
		}

		public void run() {
			try {
				int c = 0;
				while ((c = System.in.read()) > -1) {
					this.out.write(c);
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}

	// public static void main(String[] args) {
	// try {
	// (new TwoWaySerialComm()).connect(DefaultPort, DefaultBaud);
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	public void closeConnection() {
		// TODO Auto-generated method stub
		if (commPort != null)
			commPort.close();
	}

}
