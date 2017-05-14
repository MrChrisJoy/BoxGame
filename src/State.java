import java.util.ArrayList;

public class State {
	private int totalCost;
	private CellOfMaze curr;
	private ArrayList<CellOfMaze> adjCells;
	private State parent;
	
	public State(int totalCost, CellOfMaze curr, ArrayList<CellOfMaze> adjCells, State parent){
		this.totalCost = totalCost;
		this.curr = curr;
		this.adjCells = adjCells;
		this.parent = parent;
	}

	
	public ArrayList<CellOfMaze> getAdjCells() {
		return adjCells;
	}
	public int getTotalCost() {
		return totalCost;
	}

	public CellOfMaze getCell() {
		return curr;
	}

	public State getParent() {
		return parent;
	}
	
	
}
