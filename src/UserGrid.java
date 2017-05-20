import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * 
 * @author AM
 *
 */
public class UserGrid {
	private int GRID_ROW_SIZE = 80;  //  setting a xed size
	private int GRID_COL_SIZE = 80;  // 
	private char[][] grid;
	private int numGoals;
	private int numBoxes;
	private Vector2[][] userDefinedGrid;

	public UserGrid() {
		grid = new char[GRID_ROW_SIZE][GRID_COL_SIZE];
	}
	
	public void readInputGrid() {
		Scanner sc = null;
		try{
			sc = new Scanner(new FileReader("sample.txt"));    // args[0] is the first command line argument
			sc.useDelimiter("\\n");
			int i = 0;
			String str = "";
			while(sc.hasNext()){
				str = sc.next();
				if(i == 0) {
					GRID_COL_SIZE = str.toCharArray().length;
					//System.out.println("length of COL: " + GRID_COL_SIZE );
				}				
				grid[i] = str.toCharArray();
				i++;
			}
			GRID_ROW_SIZE = i;
			//System.out.println("length of ROW: " + GRID_ROW_SIZE );
	    }catch (FileNotFoundException e) {
	    	System.out.println("File Not Found: " + e.getMessage());
	    }
		finally {
	    	if (sc != null){
	    		sc.close();
	    	}
	    }
	}
	
	
	public void createUserDefinedGrid() {
		numGoals = 0;
		numBoxes = 0;
		readInputGrid();
		userDefinedGrid = new Vector2[GRID_ROW_SIZE][GRID_COL_SIZE];
		for(int r =0; r < GRID_ROW_SIZE; r++) {
			for(int c =0; c < GRID_COL_SIZE; c++) {
				Vector2 mazeblock = new Vector2(r,c);
				userDefinedGrid[r][c] = mazeblock;
				switch (grid[r][c]) {
					case '#':
						userDefinedGrid[r][c].setBlockType(BlockType.wall);
						break;
					case ' ':
						userDefinedGrid[r][c].setBlockType(BlockType.floor);
						break;
					case 'G':
						userDefinedGrid[r][c].setBlockType(BlockType.goal);
						numGoals++;
						break;
					case 'B':
						userDefinedGrid[r][c].setBlockType(BlockType.box);
						numBoxes++;
						break;
					case 'P':
						userDefinedGrid[r][c].setBlockType(BlockType.player);
						break;
				}	
			}
		}
	}
	
	public void printGrid() {
		for(int r =0; r < GRID_ROW_SIZE; r++) {
			for(int c =0; c < GRID_COL_SIZE; c++) {
				if(userDefinedGrid[r][c].getBlockType().equals(BlockType.wall)){
					System.out.print("#");
				}else if(userDefinedGrid[r][c].getBlockType().equals(BlockType.floor)) {
					System.out.print(" ");
				}else if(userDefinedGrid[r][c].getBlockType().equals(BlockType.goal)) {
					System.out.print("G");
				}else if(userDefinedGrid[r][c].getBlockType().equals(BlockType.box)) {
					System.out.print("B");
				}else if(userDefinedGrid[r][c].getBlockType().equals(BlockType.player)) {
					System.out.print("P");
				}
			}
			System.out.println();
		}
	}

	public int getGridRawSize() {
		return GRID_ROW_SIZE;
	}

	public int getGridColSize() {
		return GRID_COL_SIZE;
	}

	public Vector2[][] getUserDefinedGrid() {
		return userDefinedGrid;
	}

	public Vector2 getPlayerLocation() {
		for(int r =0; r < GRID_ROW_SIZE; r++) {
			for(int c =0; c < GRID_COL_SIZE; c++) {
				if(userDefinedGrid[r][c].getBlockType().equals(BlockType.player))
					return userDefinedGrid[r][c];
			}
		}
		return null;
	}

	public Set<Vector2> getFloorSet() {
		Set<Vector2> floorVector = new HashSet<Vector2>();
		for(int r =0; r < GRID_ROW_SIZE; r++) {
			for(int c =0; c < GRID_COL_SIZE; c++) {
				if(!userDefinedGrid[r][c].getBlockType().equals(BlockType.wall))
					floorVector.add(userDefinedGrid[r][c]);
			}
		}
		return floorVector;
	}

	public Set<Vector2> getEdgesSet() {
		Set<Vector2> wallVector = new HashSet<Vector2>();
		for(int r =0; r < GRID_ROW_SIZE; r++) {
			for(int c =0; c < GRID_COL_SIZE; c++) {
				if(userDefinedGrid[r][c].getBlockType().equals(BlockType.wall))
					wallVector.add(userDefinedGrid[r][c]);
			}
		}
		return wallVector;
	}


	public Vector2[] getGoals() {
		Vector2[] goals = new Vector2[numGoals];
		int i = 0;
		for(int r =0; r < GRID_ROW_SIZE; r++) {
			for(int c =0; c < GRID_COL_SIZE; c++) {
				if(userDefinedGrid[r][c].getBlockType().equals(BlockType.goal)){
					goals[i] = userDefinedGrid[r][c];
					i++;
				}
			}
		}
		return goals;
	}

	public Vector2[] getBoxLoctions() {
		Vector2[] boxes = new Vector2[numBoxes]; // numGoals must be equal to num ofBoxes
		int i = 0;
		for(int r =0; r < GRID_ROW_SIZE; r++) {
			for(int c =0; c < GRID_COL_SIZE; c++) {
				if(userDefinedGrid[r][c].getBlockType().equals(BlockType.box)){
					boxes[i] = userDefinedGrid[r][c];
					i++;
				}
			}
		}
		return boxes;
	}
	
	public int getNumGoals() {
		return numGoals;
	}
	public int getNumBoxes() {
		return numBoxes;
	}
}
