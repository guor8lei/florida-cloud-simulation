package Clouds;

import javax.swing.JPanel;
import java.awt.*;

/**
 * @author Raymond Guo
 *
 * Frame for cell state array to form Florida map
 */
public class Board extends JPanel {
	
	private int[][] map; //array of cell states
	private int x; //dimensions
	private int y;
	private int cellSize = 10; //size of each cell

	/**
	 * @param x
	 * @param y
	 * @param cellArray
	 * 
	 * Board constructor
	 */
	public Board(int x, int y, int[][] cellArray) {
		this.map = cellArray;
		this.x = x/cellSize;
		this.y = y/cellSize;

		JPanel boardPanel = new JPanel();
		boardPanel.setLayout(null);
		boardPanel.setPreferredSize(new Dimension(x,y));
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 * 
	 * Details what color to put for each cell state
	 */
	public void paintComponent(Graphics g) {

		for (int i = 0; i < this.x; i++) {
			for (int j = 0; j < this.y; j++) {
				
				if (map[i][j] == 1) {
					g.setColor(Color.white); //cloud
				}

				if (map[i][j] == 2) {
					g.setColor(Color.white.darker()); //gray cloud
				}

				if (map[i][j] == 3) {
					g.setColor(Color.gray); //thundercloud
				}
				
				if (map[i][j] == 4) {
					g.setColor(Color.blue.darker()); //water
				}
				
				if (map[i][j] == 5) {
					g.setColor(Color.green.darker().darker()); //land
				}
				
				if (map[i][j] == 6) {
					g.setColor(Color.gray.darker()); //darker thundercloud
				}
				
				if (map[i][j] == 7) {
					g.setColor(Color.yellow); //lightening
				}
				
				if (map[i][j] == 8) {
					g.setColor(Color.red.brighter().brighter()); //fire
				}

				if (map[i][j] == 9) {
					g.setColor(Color.LIGHT_GRAY); //ash
				}
				
				if (map[i][j] == 10) {
					g.setColor(new Color(161,120,4).darker()); //ground
				}

				g.fill3DRect(j*cellSize, i*cellSize, cellSize, cellSize, true);	//fill in color rectangle
			}
		}
		repaint();
	}
}