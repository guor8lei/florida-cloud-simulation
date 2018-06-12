package Clouds;

import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.*;

import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;


/**
 * @author Raymond Guo
 * 
 * Graphical User Interface - creates cell array which and constantly loops to run simulation correctly.
 *
 */
public class GUI extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	//constants regarding water/land and wind direction
	private static final char WATER = '.'; //water
	private static final char LANDNW = '1'; //land OR northwest wind direction
	private static final char N = '2'; //north wind direction
	private static final char NE = '3'; //northeast wind direction
	private static final char E = '4'; //east wind direction
	private static final char SE = '5'; //southeast wind direction
	private static final char S = '6'; //south wind direction
	private static final char SW = '7'; //southwest wind direction
	private static final char W = '8'; //west wind direction
	private static final char COLD = 'K'; //cold temperature: 0-40 degrees F
	private static final char COOL = 'C'; //cool temperature: 41-60 degrees F
	private static final char WARM = 'W'; //warm temperature: 61-80 degrees F
	private static final char HOT = 'H'; //hot temperature: more than 90 degrees F
	

	public static int cloudNum = 0; //number of cells that are clouds...start at 0
	public static ArrayList<String> land = new ArrayList<String>(); //map data, read from file
	public static ArrayList<String> wind = new ArrayList<String>(); //wind direction vector field, read from file
	public static ArrayList<String> humidity = new ArrayList<String>(); //humidity map data, read from file
	public static ArrayList<String> temperature = new ArrayList<String>(); //temperature map data, read from file
	public static boolean[][] origGround; //tests if the cell was originally ground before cloud passed over it
	public static int[][] startArray; //starting map, read from ArrayList
	public static int[][] windMap; //starting wind direction vector field, read from ArrayList
	public static double[][] humidityMap; //humidity map, read from ArrayList
	public static int[][] tempMap; //temperature map, read from ArrayList
	public static int cloudCover = 0; //number of cells that are clouds
	public static int thundercloudCover = 0; //number of cells that are thunderclouds
	public static double totalCells; //total number of cells on grid
	
	private int[][] mapCells; //map + clouds
	private int xNew; //dimensions
	private int yNew;
	private int[] tempCells = new int[9]; //initial cell generation
	private int[] newCells = new int[9]; //final cell generation
	private int cellSize = 10; //pixels per cell side
	
	/**
	 * @param x
	 * @param y
	 * @throws FileNotFoundException
	 * @throws IOException
	 * 
	 * Constructor
	 */
	public GUI(int x, int y) throws FileNotFoundException, IOException {
		super("Florida Cloud Simulation"); //name of program
		super.frameInit(); //frame
		makeCells(x,y); //creates cells
		//printCells(cellArray); //print array - error checking
		setLayout(null);
		setContentPane(new Board(x, y,mapCells)); //create new board to array
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //exit condition
		pack();
		setVisible(true); //make visible
		setResizable(false); //not resizable
		setSize(y,x+22); //set window size

		xNew = x/cellSize; //dimensions in terms of cells
		yNew = y/cellSize;
	}

	/**
	 * Draws the cells and updates each cell based on new color
	 */
	public void drawCells() {
		
		for (int i = 0; i < xNew; i++) {
			for (int j = 0; j < yNew; j++) {
				
				int tempNumPlus = 0;

				//iterates through each surrounding cell
				for (int iX = -1; iX <= 1; iX++) {
					for (int jX = -1; jX <= 1; jX++) {
						
						//surrounding cell position on board
						int xValue = i+iX;
						int yValue = j+jX;

						//x boundary checking
						if (xValue == -1) {
							xValue = xNew-1; //wraps around
						} else if (xValue == xNew) {
							xValue = 0;
						}

						//y boundary checking
						if (yValue == -1) {
							yValue = yNew-1;
						} else if (yValue == yNew) {
							yValue = 0;
						}

						//put cells in temp array
						tempCells[tempNumPlus] = mapCells[xValue][yValue];
						tempNumPlus++;
					}
				}

				//Cells myCells = new Cells(tempCells,i,j,cloudNum); //create cells
				Cells2 myCells = new Cells2(tempCells,i,j,cloudNum); //FIRE EXTENSION: create cells
				newCells = myCells.returnCells(); //get new cell color based on logic

				for (int iY = -1; iY <= 1; iY++) {
					for (int jY = -1; jY <= 1; jY++) {
						
						int xValue = i+iY;
						int yValue = j+jY;

						//x boundary checking
						if (xValue == -1) {
							xValue = xNew-1;
						} else if (xValue == xNew) {
							xValue = 0;
						}

						//y boundary checking
						if (yValue == -1) {
							yValue = yNew-1;
						} else if (yValue == yNew) {
							yValue = 0;
						}

						mapCells[xValue][yValue] = newCells[(iY+1)*3+(jY+1)]; //updates cell color
					}
				}
			}
		}
		
		//iterate through once more
		for (int i = 0; i < xNew; i++) {
			for (int j = 0; j < yNew; j++) {
				if (mapCells[i][j]==1||mapCells[i][j]==2) {
					cloudCover++; //count total cloud cells
				}
				else if (mapCells[i][j]==3||mapCells[i][j]==6) {
					thundercloudCover++; //count total thundercloud cells
				} 
				
				if (startArray[i][j]==5) {
					totalCells++; //count total land cells
				}
			}
		}	
		
		int totalCloudCover = cloudCover + thundercloudCover;
		
		//calculate cloud cover percentages
		double cloudPercentage = cloudCover/totalCells;
		double thundercloudPercentage = thundercloudCover/totalCells;
		double totalCloudPercentage = totalCloudCover/totalCells;
		
		//output data
		System.out.println("Total Cloud Cells:\t"+totalCloudCover+"\tTotal Cloud Percentage:\t"+totalCloudPercentage+"\tCloud Cells:\t"+cloudCover+"\tCloud Percentage:\t"+cloudPercentage+"\tThundercloud Cells:\t"+thundercloudCover+"\tThundercloud Percentage:\t"+thundercloudPercentage);
		
		//reset variables for next run
		cloudCover = 0;
		thundercloudCover = 0;
		totalCells=0;
	}

	/**
	 * @param timeTime
	 * 
	 * GUI Simulation Timer
	 */
	public void GameTimer(int timeTime) {
		TimerTask task = new TimerTask() {
			public void run() {
				drawCells();
			}
		};
		Timer timer = new Timer();
		timer.schedule(task,0,timeTime);
	}

	/**
	 * @param arrayX
	 * @param arrayY
	 * @throws FileNotFoundException
	 * @throws IOException
	 * 
	 * Initiates simulation by creating cells, board, and starting values
	 */
	public void makeCells(int arrayX, int arrayY) throws FileNotFoundException, IOException {	
		
		//board dimensions in number of cells
		int tempX = arrayX/cellSize;
		int tempY = arrayY/cellSize;
		
		//read Florida map from file
		try (BufferedReader br = new BufferedReader(new FileReader(new File("FloridaMap")))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	land.add(line);
		    }
		}

		//create public arrays
		startArray = new int[tempX][tempY];
		windMap = new int[tempX][tempY];
		humidityMap = new double[tempX][tempY];
		origGround = new boolean[tempX][tempY];
		mapCells = new int[tempX][tempY];
		tempMap = new int[tempX][tempY];
		
		//creates initial map based on read files
		for(int y = 0; y < land.size();y++){
			String row = land.get(y);
			for(int x = 0; x < row.length();x=x+2){
				switch(row.charAt(x)){
					case WATER:
						startArray[y][x/2]=4;
						break;
					case LANDNW:
						startArray[y][x/2]=5;
						break;
					default: //will catch EMPTY
						startArray[y][x/2]=4;
				}
			}
		}

		for (int i = 0; i < tempX; i++) { //make initial cell color
			for (int j = 0; j < tempY; j++) {
				mapCells[i][j] = startArray[i][j];
				origGround[i][j] = false;
			}
		}
		
		//creating starting wind direction vector field based on read file
		try (BufferedReader br = new BufferedReader(new FileReader(new File("FloridaWind")))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	wind.add(line);
		    }
		}
		
		//wind direction possibilities
		for(int y = 0; y < wind.size();y++){
			String row = wind.get(y);
			for(int x = 0; x < row.length();x=x+2){
				switch(row.charAt(x)){
					case LANDNW:
						windMap[y][x/2]=1;
						break;
					case N:
						windMap[y][x/2]=2;
						break;
					case NE:
						windMap[y][x/2]=3;
						break;
					case E:
						windMap[y][x/2]=4;
						break;
					case SE:
						windMap[y][x/2]=5;
						break;
					case S:
						windMap[y][x/2]=6;
						break;
					case SW:
						windMap[y][x/2]=7;
						break;
					case W:
						windMap[y][x/2]=8;
						break;
					default: //
						windMap[y][x/2]=1;
				}
			}
		}
		
		//creating starting humidity map based on read file
		try (BufferedReader br = new BufferedReader(new FileReader(new File("FloridaHumidity")))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	humidity.add(line);
		    }
		}
		
		//humidity possibilities
		for(int y = 0; y < humidity.size();y++){
			String row = humidity.get(y);
			for(int x = 0; x < row.length();x++){
				switch(row.charAt(x)){
					case '0':
						humidityMap[y][x]=0.0;
						break;
					case '1':
						humidityMap[y][x]=.1;
						break;
					case '2':
						humidityMap[y][x]=.2;
						break;
					case '3':
						humidityMap[y][x]=.3;
						break;
					case '4':
						humidityMap[y][x]=.4;
						break;
					case '5':
						humidityMap[y][x]=.5;
						break;
					case '6':
						humidityMap[y][x]=.6;
						break;
					case '7':
						humidityMap[y][x]=.7;
						break;
					case '8':
						humidityMap[y][x]=.8;
						break;
					case '9':
						humidityMap[y][x]=.9;
						break;
					case '.':
						humidityMap[y][x]=1;
						break;
					default: //
						humidityMap[y][x]=1;
				}
			}
		}
		
		//creating starting humidity map based on read file
		try (BufferedReader br = new BufferedReader(new FileReader(new File("FloridaTemperature")))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	temperature.add(line);
		    }
		}
		
		//humidity possibilities
		for(int y = 0; y < temperature.size();y++){
			String row = temperature.get(y);
			for(int x = 0; x < row.length();x++){
				switch(row.charAt(x)){
					case COLD:
						tempMap[y][x]=0;
						break;
					case COOL:
						tempMap[y][x]=5;
						break;
					case WARM:
						tempMap[y][x]=10;
						break;
					case HOT:
						tempMap[y][x]=15;
						break;
					default:
						tempMap[y][x]=0;
				}
			}
		}
	}

	/**
	 * @param myArray
	 * 
	 * Prints map for error checking
	 */
	public void printCells(int[][] myArray) {
		for (int[] array1D : myArray) {
			System.out.println();
			for (int i : array1D) {
				System.out.print(i + " ");
			}
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.JFrame#processWindowEvent(java.awt.event.WindowEvent)
	 * 
	 * When user closes window
	 */
	public void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			this.dispose();
			System.exit(0);
		}
	}

}