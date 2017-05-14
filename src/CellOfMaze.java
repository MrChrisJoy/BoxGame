/**
 * 
 * @author Antony Jeganathan
 *
 */
public class CellOfMaze {
	//private String status; //Wall or not?
	private boolean visited;
	private int x; //Col index in 2D array
	private int y; //Row index in 2D array
	private CellType cellType;
	
	/**
	 * Creates a MazeBlock object
	 * All MazeBlocks default to wall and are changed during maze generation
	 * 
	 * @param x the col index position
	 * @param y the row index position
	 */
	public CellOfMaze(int x,int y){
		cellType = CellType.wall;
		this.x = x;
		this.y = y;
		visited = false;
	}
		
	public boolean hasVisited(){
		return visited;
	}
	
	public void setVisited() {
		visited = true;
	}
	public void setUnVisited() {
		visited = false;
	}
	
	public CellType getStatus() {
		return cellType;
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}
	
	public void setStatus(CellType cell){
		cellType = cell; 
	}
}
