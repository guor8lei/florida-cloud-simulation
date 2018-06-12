package Clouds;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Raymond Guo
 * 
 * Runs simulation by creating GUI
 *
 */
public class CloudSimulation {
	private static int height; //dimensions of board
	private static int width;
	private static int time;

	public static void main(String[] args) throws FileNotFoundException, IOException {
		height = 1000; //board dimensions
		width = 1000;
		time = 100; //update time

		GUI gui = new GUI(height,width); //initiates simulation
		gui.GameTimer(time);
	}
}