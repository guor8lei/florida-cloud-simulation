package Clouds;

import java.util.Random;

/**
 * @author Raymond Guo
 * 
 * Describes logic for changing cell color based on cloud and wind direction logic
 */
public class Cells {
	
	private int[] surroundingCells = new int[9]; //array of cells surrounding current cell
	private int cellState; //current cell state
	private int xPos; //x position of cell
	private int yPos; //y position of cell
	private int maxCloud = 7000; //maximum number of cloud cells on board at the same time

	/**
	 * @param surroundingCells
	 * @param xPos
	 * @param yPos
	 * @param cloudNum
	 * 
	 * Cell logic
	 */
	public Cells(int[] surroundingCells,int xPos, int yPos, int cloudNum) {
		this.surroundingCells = surroundingCells; //get surrounding cells
		cellState = this.surroundingCells[4]; //cell state of current cell
		Random rand = new Random(); //generate random number of probability calculations

		switch (cellState) { //go through each case of cell state
			//Nothing
			case 0: { 
				cellState = 0;
				break;
			}

			//Cloud
			case 1: {				
				int counter = 0;
				
				//counts number of surrounding cells that are clouds
				for (int i : this.surroundingCells) {
					if (i != 1) {
						counter++; 
					}
				}
				
				//turns into gray cloud by chance depending on temperature and humidity
				if (rand.nextInt((int)(GUI.humidityMap[xPos][yPos]*GUI.tempMap[xPos][yPos]/4)) == 0) {
					cellState = 1;
				} else {
					cellState = 2;
					cloudNum--;
				}
				
				//turns into gray cloud if not surrounded by white clouds
				if (counter>7) {
					cellState=2;
					cloudNum--;
				}
				
				//turns into gray cloud if too many clouds
				if (cloudNum>maxCloud){
					cellState = 2;
					cloudNum--;
				}
			break;
			}

			//Gray Cloud
			case 2: {
				if (rand.nextInt(100) == 0) {
					cellState = 2; //may stay as gray cloud
				} else {
					if (GUI.origGround[xPos][yPos]==false) { //turns into original starting cell state read from file
						cellState = GUI.startArray[xPos][yPos];
						GUI.origGround[xPos][yPos]=false;
					} 
					else {
						cellState=10; //turn to ground (fire only)
					}
				}
				break;
			}

			//Thundercloud
			case 3: {
				int counter = 0;
				
				//counts number of surrounding cells that are clouds
				for (int i : this.surroundingCells) {
					if (i != 1) {
						counter++;
					}
				}
				
				//stays thundercloud by chance depending on temperature and humidity
				if (rand.nextInt((int)(GUI.humidityMap[xPos][yPos]*GUI.tempMap[xPos][yPos]/8*3)) == 0) {
					cellState = 3;
				} else {
					if (rand.nextInt(50) == 0){ 
						cellState= 7; //may emit lightning
					} else {
						cellState = 6; //or turn into dark thundercloud
					}
					cloudNum--;
				}
				
				//turns into lightning/dark thundercloud if not surrounded by thunderclouds
				if (counter>6) {
					if (rand.nextInt(50) == 0){
						cellState= 7;
					} else {
						cellState = 6;	
					}
					cloudNum--;
				}
				
				//turns into lightning/dark thundercloud if too many clouds
				if (cloudNum>maxCloud){
					if (rand.nextInt(50) == 0){
						cellState= 7;
					} else {
						cellState = 6;	
					}
					cloudNum--;
				}
				break;
			}

			//Water
			case 4: {
				for (int i : this.surroundingCells) {
					
					//may turn into cloud based on wind direction and if surrounding cells are clouds
					if (GUI.windMap[xPos][yPos]==1){ //northwest wind direction
						if (surroundingCells[5]==3||surroundingCells[7]==3||surroundingCells[8]==3) {			
							if (rand.nextInt(2) == 0) {
								if (cloudNum<maxCloud){
									cellState = 3; //thundercloud
									cloudNum++;
								}
							} else {
								cellState = 4;
							}
						}
						
						if (surroundingCells[5]==1||surroundingCells[7]==1||surroundingCells[8]==1) {			
							if (rand.nextInt(3) == 0) {
								if (cloudNum<maxCloud){
									cellState = 1; //cloud
									cloudNum++;
								}
							} else {
								cellState = 4;
							}
						}
					}
					
					else if (GUI.windMap[xPos][yPos]==2){ //north wind direction
						if (surroundingCells[6]==3||surroundingCells[7]==3||surroundingCells[8]==3) {			
							if (rand.nextInt(2) == 0) {
								if (cloudNum<maxCloud){
									cellState = 3;
									cloudNum++;
								}
							} else {
								cellState = 4;
							}
						}
						
						if (surroundingCells[6]==1||surroundingCells[7]==1||surroundingCells[8]==1) {			
							if (rand.nextInt(3) == 0) {
								if (cloudNum<maxCloud){
									cellState = 1;
									cloudNum++;
								}
							} else {
								cellState = 4;
							}
						}
					}
					
					else if (GUI.windMap[xPos][yPos]==3){ //northeast wind direction
						if (surroundingCells[7]==3||surroundingCells[5]==3||surroundingCells[4]==3) {			
							if (rand.nextInt(2) == 0) {
								if (cloudNum<maxCloud){
									cellState = 3;
									cloudNum++;
								}
							} else {
								cellState = 4;
							}
						}
						
						if (surroundingCells[7]==1||surroundingCells[5]==1||surroundingCells[4]==1) {			
							if (rand.nextInt(3) == 0) {
								if (cloudNum<maxCloud){
									cellState = 1;
									cloudNum++;
								}
							} else {
								cellState = 4;
							}
						}
					}
					
					else if (GUI.windMap[xPos][yPos]==4){ //east wind direction
						if (surroundingCells[0]==3||surroundingCells[3]==3||surroundingCells[6]==3) {			
							if (rand.nextInt(2) == 0) {
								if (cloudNum<maxCloud){
									cellState = 3;
									cloudNum++;
								}
							} else {
								cellState = 4;
							}
						}
						
						if (surroundingCells[0]==1||surroundingCells[3]==1||surroundingCells[6]==1) {			
							if (rand.nextInt(3) == 0) {
								if (cloudNum<maxCloud){
									cellState = 1;
									cloudNum++;
								}
							} else {
								cellState = 4;
							}
						}
					}
					
					else if (GUI.windMap[xPos][yPos]==5){ //southeast wind direction
						if (surroundingCells[1]==3||surroundingCells[0]==3||surroundingCells[3]==3) {			
							if (rand.nextInt(2) == 0) {
								if (cloudNum<maxCloud){
									cellState = 3;
									cloudNum++;
								}
							} else {
								cellState = 4;
							}
						}
						
						if (surroundingCells[1]==1||surroundingCells[0]==1||surroundingCells[3]==1) {			
							if (rand.nextInt(3) == 0) {
								if (cloudNum<maxCloud){
									cellState = 1;
									cloudNum++;
								}
							} else {
								cellState = 4;
							}
						}
					}
					
					else if (GUI.windMap[xPos][yPos]==6){ //south wind direction
						if (surroundingCells[2]==3||surroundingCells[0]==3||surroundingCells[1]==3) {			
							if (rand.nextInt(2) == 0) {
								if (cloudNum<maxCloud){
									cellState = 3;
									cloudNum++;
								}
							} else {
								cellState = 4;
							}
						}
						
						if (surroundingCells[2]==1||surroundingCells[0]==1||surroundingCells[1]==1) {			
							if (rand.nextInt(3) == 0) {
								if (cloudNum<maxCloud){
									cellState = 1;
									cloudNum++;
								}
							} else {
								cellState = 4;
							}
						}
					}
					
					else if (GUI.windMap[xPos][yPos]==7){ //southwest wind direction
						if (surroundingCells[2]==3||surroundingCells[5]==3||surroundingCells[1]==3) {			
							if (rand.nextInt(2) == 0) {
								if (cloudNum<maxCloud){
									cellState = 3;
									cloudNum++;
								}
							} else {
								cellState = 4;
							}
						}
						
						if (surroundingCells[2]==1||surroundingCells[5]==1||surroundingCells[1]==1) {			
							if (rand.nextInt(3) == 0) {
								if (cloudNum<maxCloud){
									cellState = 1;
									cloudNum++;
								}
							} else {
								cellState = 4;
							}
						}
					}
					
					else if (GUI.windMap[xPos][yPos]==8){ //west wind direction
						if (surroundingCells[2]==3||surroundingCells[5]==3||surroundingCells[8]==3) {			
							if (rand.nextInt(2) == 0) {
								if (cloudNum<maxCloud){
									cellState = 3;
									cloudNum++;
								}
							} else {
								cellState = 4;
							}
						}
						
						if (surroundingCells[2]==1||surroundingCells[5]==1||surroundingCells[8]==1) {			
							if (rand.nextInt(3) == 0) {
								if (cloudNum<maxCloud){
									cellState = 1;
									cloudNum++;
								}
							} else {
								cellState = 4;
							}
						}
					}
				}

				
				if (rand.nextInt(500000) == 0) {
					if (cloudNum<maxCloud){
						cellState = 3;
						cloudNum++;
					}
				}
				
				
				if (rand.nextInt(10000) == 0) {
					if (cloudNum<maxCloud){
						cellState = 1;
						cloudNum++;
					}
				}

				break;
			}

			//Land
			//Same exact logic as Water, but instead of Land its Water
			case 5: {
				for (int i : this.surroundingCells){
					
					if (GUI.windMap[xPos][yPos]==1){
						if (surroundingCells[5]==3||surroundingCells[7]==3||surroundingCells[8]==3) {			
							if (rand.nextInt(2) == 0) {
								if (cloudNum<maxCloud){
									cellState = 3;
									cloudNum++;
								}
							} else {
								cellState = 5;
							}
						}
						
						if (surroundingCells[5]==1||surroundingCells[7]==1||surroundingCells[8]==1) {			
							if (rand.nextInt(3) == 0) {
								if (cloudNum<maxCloud){
									cellState = 1;
									cloudNum++;
								}
							} else {
								cellState = 5;
							}
						}
					}
					
					else if (GUI.windMap[xPos][yPos]==2){
						if (surroundingCells[6]==3||surroundingCells[7]==3||surroundingCells[8]==3) {			
							if (rand.nextInt(2) == 0) {
								if (cloudNum<maxCloud){
									cellState = 3;
									cloudNum++;
								}
							} else {
								cellState = 5;
							}
						}
						
						if (surroundingCells[6]==1||surroundingCells[7]==1||surroundingCells[8]==1) {			
							if (rand.nextInt(3) == 0) {
								if (cloudNum<maxCloud){
									cellState = 1;
									cloudNum++;
								}
							} else {
								cellState = 5;
							}
						}
					}
					
					else if (GUI.windMap[xPos][yPos]==3){
						if (surroundingCells[7]==3||surroundingCells[5]==3||surroundingCells[4]==3) {			
							if (rand.nextInt(2) == 0) {
								if (cloudNum<maxCloud){
									cellState = 3;
									cloudNum++;
								}
							} else {
								cellState = 5;
							}
						}
						
						if (surroundingCells[7]==1||surroundingCells[5]==1||surroundingCells[4]==1) {			
							if (rand.nextInt(3) == 0) {
								if (cloudNum<maxCloud){
									cellState = 1;
									cloudNum++;
								}
							} else {
								cellState = 5;
							}
						}
					}
					
					else if (GUI.windMap[xPos][yPos]==4){
						if (surroundingCells[0]==3||surroundingCells[3]==3||surroundingCells[6]==3) {			
							if (rand.nextInt(2) == 0) {
								if (cloudNum<maxCloud){
									cellState = 3;
									cloudNum++;
								}
							} else {
								cellState = 5;
							}
						}
						
						if (surroundingCells[0]==1||surroundingCells[3]==1||surroundingCells[6]==1) {			
							if (rand.nextInt(3) == 0) {
								if (cloudNum<maxCloud){
									cellState = 1;
									cloudNum++;
								}
							} else {
								cellState = 5;
							}
						}
					}
					
					else if (GUI.windMap[xPos][yPos]==5){
						if (surroundingCells[1]==3||surroundingCells[0]==3||surroundingCells[3]==3) {			
							if (rand.nextInt(2) == 0) {
								if (cloudNum<maxCloud){
									cellState = 3;
									cloudNum++;
								}
							} else {
								cellState = 5;
							}
						}
						
						if (surroundingCells[1]==1||surroundingCells[0]==1||surroundingCells[3]==1) {			
							if (rand.nextInt(3) == 0) {
								if (cloudNum<maxCloud){
									cellState = 1;
									cloudNum++;
								}
							} else {
								cellState = 5;
							}
						}
					}
					
					else if (GUI.windMap[xPos][yPos]==6){
						if (surroundingCells[2]==3||surroundingCells[0]==3||surroundingCells[1]==3) {			
							if (rand.nextInt(2) == 0) {
								if (cloudNum<maxCloud){
									cellState = 3;
									cloudNum++;
								}
							} else {
								cellState = 5;
							}
						}
						
						if (surroundingCells[2]==1||surroundingCells[0]==1||surroundingCells[1]==1) {			
							if (rand.nextInt(3) == 0) {
								if (cloudNum<maxCloud){
									cellState = 1;
									cloudNum++;
								}
							} else {
								cellState = 5;
							}
						}
					}
					
					else if (GUI.windMap[xPos][yPos]==7){
						if (surroundingCells[2]==3||surroundingCells[5]==3||surroundingCells[1]==3) {			
							if (rand.nextInt(2) == 0) {
								if (cloudNum<maxCloud){
									cellState = 3;
									cloudNum++;
								}
							} else {
								cellState = 5;
							}
						}
						
						if (surroundingCells[2]==1||surroundingCells[5]==1||surroundingCells[1]==1) {			
							if (rand.nextInt(3) == 0) {
								if (cloudNum<maxCloud){
									cellState = 1;
									cloudNum++;
								}
							} else {
								cellState = 5;
							}
						}
					}
					
					else if (GUI.windMap[xPos][yPos]==8){
						if (surroundingCells[2]==3||surroundingCells[5]==3||surroundingCells[8]==3) {			
							if (rand.nextInt(2) == 0) {
								if (cloudNum<maxCloud){
									cellState = 3;
									cloudNum++;
								}
							} else {
								cellState = 5;
							}
						}
						
						if (surroundingCells[2]==1||surroundingCells[5]==1||surroundingCells[8]==1) {			
							if (rand.nextInt(3) == 0) {
								if (cloudNum<maxCloud){
									cellState = 1;
									cloudNum++;
								}
							} else {
								cellState = 5;
							}
						}
					}
					
					if (i == 8) {
						if (rand.nextInt(10) == 0) {
							cellState = 8;
						} else {
							cellState = 5;
						}
					}
				}

				
				if (rand.nextInt(500000) == 0) {
					if (cloudNum<maxCloud){
						cellState = 3;
						cloudNum++;
					}
				}
				
				
				if (rand.nextInt(10000) == 0) {
					if (cloudNum<maxCloud){
						cellState = 1;
						cloudNum++;
					}
				}

				break;
			}
			
			//Darker Thundercloud
			case 6: {
				if (rand.nextInt(100) == 0) {
					cellState = 6; //may stay as dark thundercloud
				} else {
					if (GUI.origGround[xPos][yPos]==false) {
						cellState = GUI.startArray[xPos][yPos]; //turns into original map cell state
						GUI.origGround[xPos][yPos]=false;
					} else {
						cellState=10;
					}
				}
				break;
			}
			
			//Lightening
			case 7: {
				if (rand.nextInt(10000) == 0) {
					cellState = 7; //lightning may stay longer than usual but this is very rare
				} else {
					cellState = 6; //turns into dark thundercloud
				}
				break;
			}
			
		} 
		
		this.surroundingCells[4] = cellState; //current cell state
	}

	/**
	 * @return surrounding cells
	 */
	public int[] returnCells() {
		return this.surroundingCells; //return new cells states
	}
	
	/**
	 * @return current cell state
	 */
	public int getCellState() {
		return this.cellState;
	}

}