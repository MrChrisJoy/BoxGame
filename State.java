import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class State {
	private State parent;
	private Vector2[] boxLocations;
	private Vector2 playerStart;
	private int boxMoved;
	private int dirMoved;
	
	private State(State parent, Vector2[] boxLocations, Vector2 playerStart, int boxMoved, int dirMoved) {
		this.parent = parent;
		this.boxLocations = boxLocations;
		this.playerStart = playerStart;
		this.boxMoved = boxMoved;
		this.dirMoved = dirMoved;
	}
	
	public State(Vector2[] boxLocations, Vector2 playerStart) {
		this(null, boxLocations, playerStart, -1, -1);
	}
	
	public State(Vector2[] boxLocations) {
		this(null, boxLocations, Vector2.zero, -1, -1);
	}
	
	public State getParent() {
		return parent;
	}
	
	public Vector2 getPlayerLocation() {
		return playerStart;
	}
	
	public Vector2[] getBoxLocations() {
		return boxLocations;
	}
	
	public Vector2 getBoxLocation(int index) {
		return boxLocations[index];
	}
	
	public Vector2 getMovedBoxLocation() {
		return boxMoved == -1 ? null : boxLocations[boxMoved];
	}
	
	public int getMovedBox() {
		return boxMoved;
	}
	
	public int getMovedDirection() {
		return dirMoved;
	}
	
	public List<State> getStates(boolean reverse) {
		List<State> states = new ArrayList<State>(boxLocations.length << 2);
		CachedSearch search = new CachedSearch(getPlayerLocation(), Arrays.asList(boxLocations));
		
		for(int box = 0; box < boxLocations.length; box++) {
			for(int dir = 0; dir < Generator.directions.length; dir++) {
				State found = (reverse ? getPreviousBoxState(search, box, dir, null, 1) : getNextBoxState(search, box, dir, null, 1));
				if(found != null) states.add(found);
			}
		}
		return states;
	}
	
	public State getRandomState(Set<Vector2> floor, boolean reverse, int newFloorCost) {
		CachedSearch search = new CachedSearch(getPlayerLocation(), Arrays.asList(boxLocations));
		for(int box : Generator.randomSequence(0, boxLocations.length)) {
			for(int dir : Generator.randomSequence(0, Generator.directions.length)) {
				State s = reverse ? getPreviousBoxState(search, box, dir, floor, newFloorCost) : getNextBoxState(search, box,  dir, floor, newFloorCost);
				if(s != null) return s;
			}
		}
		return null;
	}
	
	public void print() {
		for(Vector2 v : boxLocations) System.out.print("box(" + v.x + " " + v.y + ") ");
		System.out.println();
	}
	
	private State getNextBoxState(CachedSearch search, int box, int dir, Set<Vector2> floor, int newFloorCost) {
		Vector2 newBoxLocation =  Vector2.add(boxLocations[box], Generator.directions[dir]);
		Vector2 playerAccess = Vector2.subtract(boxLocations[box], Generator.directions[dir]);
		
		if(!search.hasObstical(newBoxLocation) && search.addMinimumPath(playerAccess, floor, newFloorCost)) {
			Vector2[] newBoxLocations = boxLocations.clone();
			newBoxLocations[box] = newBoxLocation;
			return new State(this, newBoxLocations, boxLocations[box], box, dir);
		}
		return null;
	}
	
	private State getPreviousBoxState(CachedSearch search, int box, int dir, Set<Vector2> floor, int newFloorCost) {
		Vector2 newBoxLocation =  Vector2.add(boxLocations[box], Generator.directions[dir]);
		Vector2 playerAccess = Vector2.add(boxLocations[box], Vector2.scale(Generator.directions[dir], 2));
		
		if(!search.hasObstical(newBoxLocation) && search.addMinimumPath(playerAccess, floor, newFloorCost)) {
			floor.add(boxLocations[box]);
			floor.add(newBoxLocation);
			Vector2[] newBoxLocations = boxLocations.clone();
			newBoxLocations[box] = newBoxLocation;
			return new State(this, newBoxLocations, playerAccess, box, dir);
		}
		return null;
	}
	
	public boolean boxHasNextState(boolean reverse, Set<Vector2> floor, int box) {
		CachedSearch search = new CachedSearch(getPlayerLocation(), Arrays.asList(boxLocations));
		for(int dir=0; dir<Generator.directions.length; dir++) {
			State s = reverse ? getPreviousBoxState(search, box, dir, null, 1) : getNextBoxState(search, box,  dir, null, 1);
			if(s != null && floor.contains(Vector2.subtract(s.getPlayerLocation(),  Generator.directions[dir])) && floor.contains(s.getMovedBoxLocation())) {
				return true;
			}
		}
		return false;
	}
}
