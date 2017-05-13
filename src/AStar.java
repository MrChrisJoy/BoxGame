import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

import javafx.scene.control.Cell;

/**
 * 
 * @author Antony Jeganathan
 *
 */
public class AStar {
	private int mazeSize;
	private CellOfMaze[][] maze;
	private Comparator<State> comparator;
	private ArrayList<State> visitedStates;
	
	public AStar(CellOfMaze[][] maze, int size) {
		mazeSize = size;
		this.maze = maze;
		comparator = new StateComparator();
		visitedStates = new ArrayList<State>();
	}
	
	public ArrayList<CellOfMaze> findPath(CellOfMaze start, CellOfMaze finish) {
		// check if both inputs arnt null
		if(start == null){
			System.out.println("start location is null");
		}else if(finish == null) {
			System.out.println("finish location is null");
		}

		
		ArrayList<CellOfMaze> path = new ArrayList<CellOfMaze>();
		PriorityQueue<State> queue = new PriorityQueue<State>(comparator);
		int statesRemoved = 0;
		State startState = new State(0,start,getPossibleAdjCells(start),null);
		queue.add(startState);
		boolean found = false;
		
		while(!queue.isEmpty()) {
			State state = queue.poll();
			statesRemoved++;			
			
			if(!hasVisited(state,visitedStates)) {
				visitedStates.add(state);
				state.getCell().setVisited();
				if(getCostToDestination(state.getCell(),finish) == 1) {
					while(state != null) {
						path.add(state.getCell());
						state = state.getParent();
					}
					found = true;
				} else {
					ArrayList<CellOfMaze> possibleAdjCells = getPossibleAdjCells(state.getCell());
					CellOfMaze selected = selectedCell(possibleAdjCells,finish);
					if( !possibleAdjCells.isEmpty()) {
						for(CellOfMaze cell: possibleAdjCells){
							int newTotalCost = state.getTotalCost() + 1; 
							queue.add(new State(newTotalCost,cell,getPossibleAdjCells(cell), state));
						}
					}
				}
			}
			if(found) {
				Collections.reverse(path);
				/*
				for(CellOfMaze cell: path){
					if(cell.getStatus().equals(CellType.player)){
						System.out.println("playerFound in generating path: " + cell.getX()+","+cell.getY());
					}
				}*/
				break;
			}
		}	
		
		// need to account for it 
		if(queue.isEmpty() && !found){
			System.out.println("PAth is not found");
		}
		
		for (int r=0;r < mazeSize;r++){
			for(int c=0;c< mazeSize;c++){
				maze[c][r].setUnVisited();;					
			}
		}
		return path;
	}

	private boolean hasVisited(State currState, ArrayList<State> visitedStates) {
		boolean visited = false;
		for(State state: visitedStates) {
			if(state.getCell().getX() == currState.getCell().getX() && 
					state.getCell().getY() == currState.getCell().getY()) {
				visited = false;
				for(CellOfMaze cell: currState.getAdjCells()){
					if(cell.hasVisited()){
						visited = true;
					}
				}
			}
		}
		return visited;
	}

	private CellOfMaze selectedCell(ArrayList<CellOfMaze> possibleAdjCells, CellOfMaze finish) {
		int cost = 0;
		int prevCost = 0;
		CellOfMaze cell = null;
		for(CellOfMaze currCell: possibleAdjCells) {
			if(!currCell.hasVisited()){
				prevCost = getCostToDestination(currCell,finish);
				if(cost == 0){
					cell = currCell;
					cost = prevCost;
				}else if(prevCost <= cost) {
					cell = currCell;
					cost = prevCost;
				}
			}
		}
		return cell;
	}

	private int getCostToDestination(CellOfMaze start, CellOfMaze finish) {
		return Math.abs(start.getX() - finish.getX()) + Math.abs(start.getY() - finish.getY());
	}

	private ArrayList<CellOfMaze> getPossibleAdjCells(CellOfMaze cell) {
		// x<- col  y<- row
		ArrayList<CellOfMaze> possibleCells = new ArrayList<CellOfMaze>();
		int x = cell.getX();
		int y = cell.getY();
		if(y == mazeSize-1) {
			if(maze[x+1][y].getStatus().equals(CellType.floor) || maze[x+1][y].getStatus().equals(CellType.player)){
				if(!maze[x+1][y].hasVisited()){
					possibleCells.add(maze[x+1][y]);
				}
			}
			if(maze[x-1][y].getStatus().equals(CellType.floor)  || maze[x-1][y].getStatus().equals(CellType.player)){
				if(!maze[x-1][y].hasVisited()){
					possibleCells.add(maze[x-1][y]);
				}
			}
			if(maze[x][y-1].getStatus().equals(CellType.floor)  || maze[x][y-1].getStatus().equals(CellType.player)){
				if(!maze[x][y-1].hasVisited()){
					possibleCells.add(maze[x][y-1]);
				}
			}
		}else {
		
			if(maze[x][y+1].getStatus().equals(CellType.floor)  || maze[x][y+1].getStatus().equals(CellType.player)){
				if(!maze[x][y+1].hasVisited()){
					possibleCells.add(maze[x][y+1]);
				}
			}
			if(maze[x][y-1].getStatus().equals(CellType.floor)  || maze[x][y-1].getStatus().equals(CellType.player)){
				if(!maze[x][y-1].hasVisited()){
					possibleCells.add(maze[x][y-1]);
				}
			}		
			if(maze[x+1][y].getStatus().equals(CellType.floor)  || maze[x+1][y].getStatus().equals(CellType.player)){
				if(!maze[x+1][y].hasVisited()){
					possibleCells.add(maze[x+1][y]);
				}
			}
			if(maze[x-1][y].getStatus().equals(CellType.floor)  || maze[x-1][y].getStatus().equals(CellType.player)){
				if(!maze[x-1][y].hasVisited()){
					possibleCells.add(maze[x-1][y]);
				}
			}				
		}
		return possibleCells;
	}	
}
