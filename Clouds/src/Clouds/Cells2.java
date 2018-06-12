package Clouds;

import java.util.Random;

/**
 * @author Raymond Guo
 * 
 * This is just for fun FOREST FIRES IN FLORIDA
 */
public class Cells2 {
	
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
	 * Cell logic + forest fires
	 */
	public Cells2(int[] surroundingCells,int xPos, int yPos, int cloudNum) {
		this.surroundingCells = surroundingCells;
		cellState = this.surroundingCells[4];

		Random rand = new Random();

		switch (cellState) {
			//Nothing
			case 0: { 
				cellState = 0;
				break;
			}

			//Cloud
			case 1: {				
				int counter = 0;
				
				for (int i : this.surroundingCells) {
					if (i != 1) {
						counter++;
					}
				}
				
				if (rand.nextInt(2) == 0) {
					cellState = 1;
				} else {
					cellState = 2;
					cloudNum--;
				}
				
				if (counter>7) {
					cellState=2;
					cloudNum--;
				}
				
				if (cloudNum>maxCloud){
					cellState = 2;
					cloudNum--;
				}
			break;
			}

			//Gray Cloud
			case 2: {
				if (rand.nextInt(100) == 0) {
					cellState = 2;
				} else {
					if (GUI.origGround[xPos][yPos]==false) {
						cellState = GUI.startArray[xPos][yPos];
						GUI.origGround[xPos][yPos]=false;
					} 
					else {
						cellState=10;
					}
				}
				break;
			}

			//Thundercloud
			case 3: {
				int counter = 0;
				
				for (int i : this.surroundingCells) {
					if (i != 1) {
						counter++;
					}
				}
				
				if (rand.nextInt(3) == 0) {
					cellState = 3;
				} else {
					if (rand.nextInt(50) == 0){
						cellState= 7;
					} else {
						cellState = 6;	
					}
					cloudNum--;
				}
				
				if (counter>6) {
					if (rand.nextInt(50) == 0){
						cellState= 7;
					} else {
						cellState = 6;	
					}
					cloudNum--;
				}
				
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
					if (surroundingCells[5]==3||surroundingCells[7]==3||surroundingCells[8]==3) {			
						if (rand.nextInt(2) == 0) {
							if (cloudNum<maxCloud){
								cellState = 3;
								cloudNum++;
							}
						} else {
							cellState = 4;
						}
					}
					
					if (surroundingCells[5]==1||surroundingCells[7]==1||surroundingCells[8]==1) {			
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
			case 5: {
				for (int i : this.surroundingCells) {
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
					cellState = 6;
				} else {
					if (GUI.origGround[xPos][yPos]==false) {
						cellState = GUI.startArray[xPos][yPos];
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
					cellState = 7;
				} else {
					cellState = 6;
				}
				
				if (GUI.startArray[xPos][yPos]==5 && GUI.origGround[xPos][yPos]==false) {
					cellState=8;
				}
				break;
			}
			
			//Fire
			case 8: {
				for (int i : this.surroundingCells) {
					if (i == 4) {
						i = 10;
						cellState = 10;
						GUI.origGround[xPos][yPos] = true;
					}

					if (i != 4) {
						if (rand.nextInt(4) == 0) {
							cellState = 9;
						} else {
							cellState = 8;
						}
					}
				}
				
				break;
			}
			
			//Ash
			case 9: {
				if (rand.nextInt(100) == 0) {
					cellState = 9;
				} else {
					GUI.origGround[xPos][yPos] = true;
					cellState = 10;
				}
				break;
			}

			//Ground
			case 10: {
				if (rand.nextInt(200) == 0) {
					GUI.origGround[xPos][yPos] = false;
					cellState=5;
				} else {
					cellState = 10;
				}
				
				for (int i : this.surroundingCells) {
					if (surroundingCells[5]==3||surroundingCells[7]==3||surroundingCells[8]==3) {			
						if (rand.nextInt(2) == 0) {
							if (cloudNum<maxCloud){
								cellState = 3;
								cloudNum++;
							}
						} else {
							cellState = 10;
						}
					}
					
					if (surroundingCells[5]==1||surroundingCells[7]==1||surroundingCells[8]==1) {			
						if (rand.nextInt(3) == 0) {
							if (cloudNum<maxCloud){
								cellState = 1;
								cloudNum++;
							}
						} else {
							cellState = 10;
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
		} 
		
		this.surroundingCells[4] = cellState;
	}

	public int[] returnCells() {
		return this.surroundingCells;
	}

}