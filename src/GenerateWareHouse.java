import java.util.ArrayList;
import java.util.Random;

import javafx.scene.input.KeyCode;

/**
 * 
 * @author Antony Jeganathan
 *
 */
public class GenerateWareHouse {
	boolean debug = false;
	private GenerateMaze newMaze;
	private AStar astar;
	private ArrayList<CellOfMaze> pathCells;
	ArrayList<CellOfMaze> startCells;
	ArrayList<CellOfMaze> finishCells;
	ArrayList<CellOfMaze> goalCells;
	private int numBox;
	Random RG;
	ArrayList<CellOfMaze> undoMoves; //for undo implementation
	
	public GenerateWareHouse(int size,int numBox) {
		newMaze = new GenerateMaze();
		newMaze.createMaze(size);
		this.numBox = numBox;
		astar = new AStar(newMaze.getMaze(),newMaze.getMazeSize());
		pathCells = new ArrayList<CellOfMaze>();
		startCells = new ArrayList<CellOfMaze>();
		finishCells = new ArrayList<CellOfMaze>();
		RG = new Random();
		undoMoves = new ArrayList<CellOfMaze>(); 
		goalCells = new ArrayList<CellOfMaze>(); 
	}
	
	/**
	 * creating new maze and assigning already created one
	 * What happens to the already created one; How to remove that
	 * @return
	 */
	public CellOfMaze[][] buildWearHouse() {
		CellOfMaze[][] maze = new CellOfMaze[newMaze.getMazeSize()+2][newMaze.getMazeSize()+2];
		
		for (int r=0;r< newMaze.getMazeSize()+2;r++){
			for(int c=0;c< newMaze.getMazeSize()+2;c++){
				CellOfMaze mazeblock = new CellOfMaze(c,r);
				maze[c][r] = mazeblock;	
			}
		}

		for (int r = 1;r < newMaze.getMazeSize()+1;r++){
			for(int c = 1;c < newMaze.getMazeSize()+1;c++){
				CellOfMaze mazeblock = new CellOfMaze(c,r); // if dont create new cell; then it will still point to the old maze's locations
				maze[c][r] = mazeblock;						// then it will cause disaster
				maze[c][r].setStatus(newMaze.getMaze()[c-1][r-1].getStatus()); 
				if(maze[c][r].getStatus().equals(CellType.goal)){ // Getting all goal states
					goalCells.add(maze[c][r]);
				}
			}
		}
				
		newMaze.setMaze(maze);
		newMaze.setMazeSize(newMaze.getMazeSize()+2);
		return newMaze.getMaze();
	}
	
	
	/**
	 * it assumes that boxes move towards/away from goals;
	 * if want to make it hard; consider the moving direction towards goal 
	 * and remove corresponding walls  <-- or would it make the game more easier; since the path is obvious
	 * @return
	 */
	public CellOfMaze[][] addPathsWareHouse() {
		ArrayList<CellOfMaze> wallsToRemove = new ArrayList<CellOfMaze>();
		int x = 0;
		int y = 0;
		int pX = 0; 
		int pY = 0;
		
		for(CellOfMaze cell: finishCells){
			cell.setStatus(CellType.test);
		}

		// accounts for all possible box's path like  " B P" in the addPathsWareHouse() 
		for(CellOfMaze cell: pathCells) {
			if(cell.getStatus().equals(CellType.player)){
				pX = cell.getX();
				pY = cell.getY();
				cell.setStatus(CellType.test);
			}
		}
		
		for(CellOfMaze cell: pathCells) {
			x = cell.getX();
			y = cell.getY();
			//  * *
			//  *
			if(newMaze.getMaze()[x][y+1].getStatus().equals(CellType.test) && newMaze.getMaze()[x+1][y].getStatus().equals(CellType.test)) {
			  if(debug)System.out.println("made a path -> top Right ");
				wallsToRemove.add(newMaze.getMaze()[x-1][y]);
				wallsToRemove.add(newMaze.getMaze()[x-1][y+1]);
				wallsToRemove.add(newMaze.getMaze()[x][y-1]);
				wallsToRemove.add(newMaze.getMaze()[x+1][y-1]);
			}
			
			// * *
			//   *
			if(newMaze.getMaze()[x][y+1].getStatus().equals(CellType.test) && newMaze.getMaze()[x-1][y].getStatus().equals(CellType.test)) {
				if(debug)System.out.println("made a path -> top Left");
				wallsToRemove.add(newMaze.getMaze()[x+1][y]);
				wallsToRemove.add(newMaze.getMaze()[x+1][y+1]);
				wallsToRemove.add(newMaze.getMaze()[x][y-1]);
				wallsToRemove.add(newMaze.getMaze()[x-1][y-1]);
			}
			
			//  *
			//  * *
			if(newMaze.getMaze()[x][y-1].getStatus().equals(CellType.test) && newMaze.getMaze()[x+1][y].getStatus().equals(CellType.test)) {
				if(debug)System.out.println("made a path -> bottom right");
				wallsToRemove.add(newMaze.getMaze()[x-1][y]);
				wallsToRemove.add(newMaze.getMaze()[x-1][y-1]);
				wallsToRemove.add(newMaze.getMaze()[x][y+1]);
				wallsToRemove.add(newMaze.getMaze()[x+1][y+1]);
			}
			
			//   *
			// * * 
			if(newMaze.getMaze()[x][y-1].getStatus().equals(CellType.test) && newMaze.getMaze()[x-1][y].getStatus().equals(CellType.test)) {
				if(debug)System.out.println("made a path -> bottom left");
				wallsToRemove.add(newMaze.getMaze()[x+1][y]);
				wallsToRemove.add(newMaze.getMaze()[x+1][y-1]);
				wallsToRemove.add(newMaze.getMaze()[x][y+1]);
				wallsToRemove.add(newMaze.getMaze()[x-1][y+1]);
			}
			
			
		}
		
		// this is so simple; removed all blocks surrounds the Box; can make changes to remove certain blocks
		for(CellOfMaze cell: startCells){
			x = cell.getX();
			y = cell.getY();
			
			// B B
			// * B
			if(newMaze.getMaze()[x][y+1].getStatus().equals(CellType.test) || newMaze.getMaze()[x][y+1].getStatus().equals(CellType.box)){
				wallsToRemove.add(newMaze.getMaze()[x][y-1]);
				wallsToRemove.add(newMaze.getMaze()[x-1][y-1]);
				wallsToRemove.add(newMaze.getMaze()[x-1][y]);
				wallsToRemove.add(newMaze.getMaze()[x-1][y+1]);
				wallsToRemove.add(newMaze.getMaze()[x+1][y-1]);
				wallsToRemove.add(newMaze.getMaze()[x+1][y]);
				wallsToRemove.add(newMaze.getMaze()[x+1][y+1]);
			}
			// *
			// B
			if(newMaze.getMaze()[x][y-1].getStatus().equals(CellType.test) || newMaze.getMaze()[x][y-1].getStatus().equals(CellType.box)){
				wallsToRemove.add(newMaze.getMaze()[x][y+1]);
				wallsToRemove.add(newMaze.getMaze()[x-1][y+1]);
				wallsToRemove.add(newMaze.getMaze()[x-1][y]);
				wallsToRemove.add(newMaze.getMaze()[x-1][y-1]);
				wallsToRemove.add(newMaze.getMaze()[x+1][y+1]);
				wallsToRemove.add(newMaze.getMaze()[x+1][y]);
				wallsToRemove.add(newMaze.getMaze()[x+1][y-1]);
			}
			
			// B *
			if(newMaze.getMaze()[x+1][y].getStatus().equals(CellType.test) || newMaze.getMaze()[x+1][y].getStatus().equals(CellType.box)){
				wallsToRemove.add(newMaze.getMaze()[x-1][y]);
				wallsToRemove.add(newMaze.getMaze()[x-1][y-1]);
				wallsToRemove.add(newMaze.getMaze()[x][y-1]);
				wallsToRemove.add(newMaze.getMaze()[x+1][y-1]);
				wallsToRemove.add(newMaze.getMaze()[x-1][y+1]);
				wallsToRemove.add(newMaze.getMaze()[x][y+1]);
				wallsToRemove.add(newMaze.getMaze()[x+1][y+1]);
			}
			
			// * B
			if(newMaze.getMaze()[x-1][y].getStatus().equals(CellType.test) || newMaze.getMaze()[x-1][y].getStatus().equals(CellType.box)){
				wallsToRemove.add(newMaze.getMaze()[x+1][y]);
				wallsToRemove.add(newMaze.getMaze()[x+1][y-1]);
				wallsToRemove.add(newMaze.getMaze()[x][y-1]);
				wallsToRemove.add(newMaze.getMaze()[x-1][y-1]);
				wallsToRemove.add(newMaze.getMaze()[x+1][y+1]);
				wallsToRemove.add(newMaze.getMaze()[x][y+1]);
				wallsToRemove.add(newMaze.getMaze()[x-1][y+1]);
			}
		}
		
		for(CellOfMaze cell: wallsToRemove){
			cell.setStatus(CellType.floor);
		}
		for(CellOfMaze cell: pathCells){
			newMaze.getMaze()[cell.getX()][cell.getY()].setStatus(CellType.floor);
		}
		for(CellOfMaze cell: finishCells){
			newMaze.getMaze()[cell.getX()][cell.getY()].setStatus(CellType.goal);
		}
		for(CellOfMaze cell: startCells){
			newMaze.getMaze()[cell.getX()][cell.getY()].setStatus(CellType.box);
		}
		newMaze.getMaze()[pX][pY].setStatus(CellType.player);
		
		System.out.println("After creating path to move....");
		newMaze.printMaze();
	//	System.out.println("Players Location: "+ pX + "," + pY);
		return newMaze.getMaze();
	}
	
	public CellOfMaze[][] generateBoxPath() {
		boolean pathCell = false;
		CellOfMaze finish;
		CellOfMaze start;
	//	finishCells.addAll(getOrderedFloorCell());
	//	System.out.println("[finishCells] " + finishCells.size());
		for(int i=0; i < numBox; i++) {
			finish = getRandomFloorCell(newMaze);
			start = getRandomFloorCell(newMaze);
			if(finishCells.contains(finish)) {
				finish = getRandomFloorCell(newMaze);
				i--;
				continue;
			}
			
			if(startCells.contains(start)) {
				start = getRandomFloorCell(newMaze);
				i--;
				continue;
			}
			
			startCells.add(start);
			finishCells.add(finish);
	//		finish = finishCells.get(i);
	//		System.out.println("G loc "  + finish.getX() + "," + finish.getY());
			pathCells.addAll(getPath(start,finish));
			pathCells.addAll(getPath(finish,getStartCellMaze(newMaze)));
		}	
		
		for(CellOfMaze cell: startCells){
			cell.setStatus(CellType.box);
		}
		for(CellOfMaze cell: finishCells){
			cell.setStatus(CellType.goal);
		}
		
		for(CellOfMaze cell: pathCells){
			if(newMaze.getMaze()[cell.getX()][cell.getY()].getStatus().equals(CellType.player)) {
				newMaze.getMaze()[cell.getX()][cell.getY()].setStatus(CellType.player);
				pathCell = true;
				//System.out.println("generateBoxPath player Loc " + cell.getX() + "," + cell.getY() );
			}else if (newMaze.getMaze()[cell.getX()][cell.getY()].getStatus().equals(CellType.box)) {
				newMaze.getMaze()[cell.getX()][cell.getY()].setStatus(CellType.box);
			}else if(newMaze.getMaze()[cell.getX()][cell.getY()].getStatus().equals(CellType.goal)) {
				newMaze.getMaze()[cell.getX()][cell.getY()].setStatus(CellType.goal);
			}else {
				newMaze.getMaze()[cell.getX()][cell.getY()].setStatus(CellType.test);
			}
		}	
		if(!pathCell){
			pathCells.add(getStartCellMaze(newMaze)); // Why does the original path doesnt have it; how does it get replaced
		}
		// PRINTINGGGG
		newMaze.printMaze();
		System.out.println("Num of pathCells: " + pathCells.size());
		return newMaze.getMaze();
		
	}
	
	public boolean movePlayer(KeyCode button) {
		boolean moveMade = false;
		int x = 0;
		int y = 0;
		if(getPlayerLocation() != null) {
			x = getPlayerLocation().getX();
			y = getPlayerLocation().getY();
		}else {
			System.out.println("player Location is null");
			newMaze.printMaze();
		}
		
		if(button.equals(KeyCode.LEFT)){
			System.out.println("detect left");
			// " ""P"
			if(newMaze.getMaze()[x-1][y].getStatus().equals(CellType.floor) || newMaze.getMaze()[x-1][y].getStatus().equals(CellType.goal)){
				System.out.println("Players Location "+x+","+y);
				undoMoves.clear();
				CellOfMaze cell1 = new CellOfMaze(x-1,y);
				cell1.setStatus(newMaze.getMaze()[x-1][y].getStatus());
				CellOfMaze cell2 = new CellOfMaze(x,y);
				cell2.setStatus(newMaze.getMaze()[x][y].getStatus());
				undoMoves.add(cell1);
				undoMoves.add(cell2);
				
				newMaze.getMaze()[x-1][y].setStatus(CellType.player);
				newMaze.getMaze()[x][y].setStatus(CellType.floor);
				moveMade = true;
				
				
				
			// " ""B""P"	
			}else if(newMaze.getMaze()[x-1][y].getStatus().equals(CellType.box) && (newMaze.getMaze()[x-2][y].getStatus().equals(CellType.floor)
					|| newMaze.getMaze()[x-2][y].getStatus().equals(CellType.goal) ) ){
				
				undoMoves.clear();
				
				CellOfMaze cell1 = new CellOfMaze(x-1,y);
				cell1.setStatus(newMaze.getMaze()[x-1][y].getStatus());
				CellOfMaze cell2 = new CellOfMaze(x-2,y);
				cell2.setStatus(newMaze.getMaze()[x-2][y].getStatus());
				CellOfMaze cell3 = new CellOfMaze(x,y);
				cell3.setStatus(newMaze.getMaze()[x][y].getStatus());
				undoMoves.add(cell1);
				undoMoves.add(cell2);
				undoMoves.add(cell3);
				
				newMaze.getMaze()[x-2][y].setStatus(CellType.box);
				newMaze.getMaze()[x-1][y].setStatus(CellType.player);
				newMaze.getMaze()[x][y].setStatus(CellType.floor);
				
				moveMade = true;
			}			
		}else if(button.equals(KeyCode.RIGHT)){
			System.out.println("detect right");
			// "P"" "
			if(newMaze.getMaze()[x+1][y].getStatus().equals(CellType.floor) || newMaze.getMaze()[x+1][y].getStatus().equals(CellType.goal)){
				System.out.println("Players Location "+x+","+y);
				
				undoMoves.clear();
				
				CellOfMaze cell1 = new CellOfMaze(x+1,y);
				cell1.setStatus(newMaze.getMaze()[x+1][y].getStatus());
				CellOfMaze cell2 = new CellOfMaze(x,y);
				cell2.setStatus(newMaze.getMaze()[x][y].getStatus());
				undoMoves.add(cell1);
				undoMoves.add(cell2);
				
				newMaze.getMaze()[x+1][y].setStatus(CellType.player);
				newMaze.getMaze()[x][y].setStatus(CellType.floor);
				moveMade = true;
			// "P""B"" "	
			}else if(newMaze.getMaze()[x+1][y].getStatus().equals(CellType.box) && (newMaze.getMaze()[x+2][y].getStatus().equals(CellType.floor)
					|| newMaze.getMaze()[x+2][y].getStatus().equals(CellType.goal)) ){
				
				undoMoves.clear();
				
				CellOfMaze cell1 = new CellOfMaze(x+1,y);
				cell1.setStatus(newMaze.getMaze()[x+1][y].getStatus());
				CellOfMaze cell2 = new CellOfMaze(x+2,y);
				cell2.setStatus(newMaze.getMaze()[x+2][y].getStatus());
				CellOfMaze cell3 = new CellOfMaze(x,y);
				cell3.setStatus(newMaze.getMaze()[x][y].getStatus());
				undoMoves.add(cell1);
				undoMoves.add(cell2);
				undoMoves.add(cell3);
				
				newMaze.getMaze()[x+2][y].setStatus(CellType.box);
				newMaze.getMaze()[x+1][y].setStatus(CellType.player);
				newMaze.getMaze()[x][y].setStatus(CellType.floor);
				moveMade = true;
			}			
		}else if(button.equals(KeyCode.UP)){
			System.out.println("detect up");
			// " "
			// "P"
			if(newMaze.getMaze()[x][y-1].getStatus().equals(CellType.floor) || newMaze.getMaze()[x][y-1].getStatus().equals(CellType.goal)){
				System.out.println("Players Location "+x+","+y);
				
				undoMoves.clear();
				
				CellOfMaze cell1 = new CellOfMaze(x,y-1);
				cell1.setStatus(newMaze.getMaze()[x][y-1].getStatus());
				CellOfMaze cell2 = new CellOfMaze(x,y);
				cell2.setStatus(newMaze.getMaze()[x][y].getStatus());
				undoMoves.add(cell1);
				undoMoves.add(cell2);
				
				newMaze.getMaze()[x][y-1].setStatus(CellType.player);
				newMaze.getMaze()[x][y].setStatus(CellType.floor);
				moveMade = true;
			// " "
			// "B"
			// "P"	
			}else if(newMaze.getMaze()[x][y-1].getStatus().equals(CellType.box) && (newMaze.getMaze()[x][y-2].getStatus().equals(CellType.floor) 
					|| newMaze.getMaze()[x][y-2].getStatus().equals(CellType.goal))){
				
				undoMoves.clear();
				
				CellOfMaze cell1 = new CellOfMaze(x,y-1);
				cell1.setStatus(newMaze.getMaze()[x][y-1].getStatus());
				CellOfMaze cell2 = new CellOfMaze(x,y-2);
				cell2.setStatus(newMaze.getMaze()[x][y-2].getStatus());
				CellOfMaze cell3 = new CellOfMaze(x,y);
				cell3.setStatus(newMaze.getMaze()[x][y].getStatus());
				undoMoves.add(cell1);
				undoMoves.add(cell2);
				undoMoves.add(cell3);
				
				newMaze.getMaze()[x][y-2].setStatus(CellType.box);
				newMaze.getMaze()[x][y-1].setStatus(CellType.player);
				newMaze.getMaze()[x][y].setStatus(CellType.floor);
				moveMade = true;
			}			
		}else if(button.equals(KeyCode.DOWN)){
			System.out.println("detect down");
			// "P"
			// " "
			if(newMaze.getMaze()[x][y+1].getStatus().equals(CellType.floor)|| newMaze.getMaze()[x][y+1].getStatus().equals(CellType.goal)){
				System.out.println("Players Location "+x+","+y);
				
				undoMoves.clear();
				
				CellOfMaze cell1 = new CellOfMaze(x,y+1);
				cell1.setStatus(newMaze.getMaze()[x][y+1].getStatus());
				CellOfMaze cell2 = new CellOfMaze(x,y);
				cell2.setStatus(newMaze.getMaze()[x][y].getStatus());
				undoMoves.add(cell1);
				undoMoves.add(cell2);
				
				newMaze.getMaze()[x][y+1].setStatus(CellType.player);
				newMaze.getMaze()[x][y].setStatus(CellType.floor);
				moveMade = true;
			// "P"
			// "B"
			// " "	
			}else if(newMaze.getMaze()[x][y+1].getStatus().equals(CellType.box) && (newMaze.getMaze()[x][y+2].getStatus().equals(CellType.floor) 
					|| newMaze.getMaze()[x][y+2].getStatus().equals(CellType.goal)) ){
				
				undoMoves.clear();
				
				CellOfMaze cell1 = new CellOfMaze(x,y+1);
				cell1.setStatus(newMaze.getMaze()[x][y+1].getStatus());
				CellOfMaze cell2 = new CellOfMaze(x,y+2);
				cell2.setStatus(newMaze.getMaze()[x][y+2].getStatus());
				CellOfMaze cell3 = new CellOfMaze(x,y);
				cell3.setStatus(newMaze.getMaze()[x][y].getStatus());
				undoMoves.add(cell1);
				undoMoves.add(cell2);
				undoMoves.add(cell3);
				
				newMaze.getMaze()[x][y+2].setStatus(CellType.box);
				newMaze.getMaze()[x][y+1].setStatus(CellType.player);
				newMaze.getMaze()[x][y].setStatus(CellType.floor);
				moveMade = true;
			}			
		}else if(button.equals(KeyCode.U)){
			System.out.println("Undo.. MOves: " + undoMoves.size());
			for(CellOfMaze cell: undoMoves){
				newMaze.getMaze()[cell.getX()][cell.getY()].setStatus(cell.getStatus());
			}
			moveMade = true;
		}
				
		// resettin goal location to be goal after a player moved over it
		for(CellOfMaze cell: finishCells){
			x = cell.getX() + 1; // adjusted according to new map
			y = cell.getY() + 1;
			if(!newMaze.getMaze()[x][y].getStatus().equals(CellType.goal)){
				if(newMaze.getMaze()[x][y].getStatus().equals(CellType.box)){
					continue;
				}else if(newMaze.getMaze()[x][y].getStatus().equals(CellType.player)){
					continue;
				}else {
					newMaze.getMaze()[x][y].setStatus(CellType.goal);
				}
			}
		}
	/*	
		if(isWin()){
			System.out.println("Won");
		}
	*/	
		return moveMade;
	}

	
	public boolean isWin() {
		boolean win = false;
		int numOfWin = 0;
		for(CellOfMaze finish: goalCells){
			if(newMaze.getMaze()[finish.getX()][finish.getY()].getStatus().equals(CellType.box)){
				System.out.println("REached ");
				numOfWin++;
			}
			System.out.println("Goal Loc: " + finish.getX() + "," + finish.getY() + " " + finish.getStatus());
		}
		if(numOfWin == goalCells.size()){
			win = true;
		}
		return win;
	}
	
	public ArrayList<CellOfMaze> getFloorCells(GenerateMaze maze) {
		ArrayList<CellOfMaze> floorCells = new ArrayList<CellOfMaze>();	
		for (int r = 0; r < maze.getMazeSize(); r++){
			for(int c = 0; c < maze.getMazeSize(); c++){
				if(maze.getMaze()[c][r].getStatus().equals(CellType.floor)){
					floorCells.add(maze.getMaze()[c][r]);
				}
			}
		}
		return floorCells;
	}
	
	public CellOfMaze getStartCellMaze(GenerateMaze maze) {	
		for (int r = 0; r < maze.getMazeSize(); r++){
			for(int c = 0; c < maze.getMazeSize(); c++){
				if(maze.getMaze()[c][r].getStatus().equals(CellType.player)){	
					return maze.getMaze()[c][r];
				}				
			}
		}
		return null;
	}
	
	public CellOfMaze getRandomFloorCell(GenerateMaze maze) {
		ArrayList<CellOfMaze> floorCells = getFloorCells(maze);
		int item  = RG.nextInt(floorCells.size());
		if(floorCells.get(item) != null) {
			return floorCells.get(item);
		}
		return null;
	}
	
	public ArrayList<CellOfMaze> getOrderedFloorCell(){
		ArrayList<CellOfMaze> floorCells = getFloorCells(newMaze);
		ArrayList<CellOfMaze> orderedFloor = new ArrayList<CellOfMaze>();

		for(int i=0; i < numBox; i++){			
			orderedFloor.add(floorCells.get(floorCells.size()-1-i));
		}
		System.out.println("size of finish locs: " + floorCells.size());
		return orderedFloor;
	}
	
	
	private CellOfMaze getPlayerLocation() {
		for (int r = 0; r < newMaze.getMazeSize(); r++){
			for(int c = 0; c < newMaze.getMazeSize(); c++){
				if(newMaze.getMaze()[c][r].getStatus().equals(CellType.player)){
					return newMaze.getMaze()[c][r];
				}		
			}
		}
		return null;
	}

	public boolean checkAdjacent(CellOfMaze cell, CellType type){
		int x = cell.getX();
		int y = cell.getY();
		if(newMaze.getMaze()[x+1][y].getStatus().equals(type)){
			return true;
		}
		if(newMaze.getMaze()[x-1][y].getStatus().equals(type)){
			return true;
		}
		if(newMaze.getMaze()[x][y+1].getStatus().equals(type)){
			return true;
		}
		if(newMaze.getMaze()[x][y-1].getStatus().equals(type)){
			return true;
		}
		return false;	
	}
	public ArrayList<CellOfMaze> getPath(CellOfMaze start, CellOfMaze finish) {
		return astar.findPath(start, finish);
	}
	
	public GenerateMaze getMaze() {
		return newMaze;
	}	
	

}
