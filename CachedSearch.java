import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
//TODO search for nearest globalTraversed then to goal

/**
 * Stores past search information to optimize successive searches from the same point
 * @author KW
 */
public class CachedSearch {
	/**
	 * Stores a series of ordered positions
	 * @author KW
	 */
	private class GridState {
		private GridState parent;
		private Vector2 position;
		private int cost;
		
		public GridState(GridState parent, Vector2 position, int cost) {
			this.cost = cost;
			if((this.parent = parent) != null) this.cost += parent.getCost();
			this.position = position;
		}
		
		public GridState(Vector2 position) {
			this(null, position, 0);
		}
		
		public GridState getParent() {
			return parent;
		}
		
		public Vector2 getPosition() {
			return position;
		}
		
		public int getCost() {
			return cost;
		}
		
		@Override
		public boolean equals(Object o) {
			if(o == null || !(o instanceof GridState)) return false;
			GridState s = (GridState)o;
			return s.position.equals(position);
		}
		
		@Override
		public int hashCode() {
			return 21 + position.hashCode();
		}
	}
	
	
	private Vector2 start;
	private Collection<Vector2> obsticals;
	private Set<Vector2> successPaths;
	
	/**
	 * Instantiates a new CachedSearch
	 * @param start constant start of search
	 * @param obsticals constant locations search must avoid
	 */
	public CachedSearch(Vector2 start, Collection<Vector2> obsticals) {
		this.start = start;
		this.obsticals = obsticals;
		
		successPaths = new HashSet<Vector2>();
		successPaths.add(start);
	}
	
	/**
	 * Attempts to find a path to the given end point
	 * @param end the location to search for
	 * @return true if end is accessible from start
	 */
	public boolean hasPath(Vector2 end) {
		return search(end, null, 1);
		
	}
	
	public boolean hasObstical(Vector2 obstical) {
		return obsticals.contains(obstical);
	}
	
	public boolean addMinimumPath(Vector2 end, Collection<Vector2> floor, int newFloorCost) {
		return search(end, floor, newFloorCost);
	}
	
	/**
	 * Attempts to find a path to the given end point
	 * @param end the location to search for
	 * @param floor a subset of locations to search within
	 * @param subOptimality number of unnecessary twists in path
	 * @return path an ordered continuous collection from start to end or null if no path exists
	 */
	private boolean search(Vector2 end, Collection<Vector2> floor, int newFloorCost) {
		if(obsticals.contains(end)) return false;
		
		Queue<GridState> pathQueue = new PriorityQueue<GridState>((a, b)-> a.getCost() + Vector2.manDistance(a.getPosition(), start) - b.getCost() - Vector2.manDistance(b.getPosition(), start));
		pathQueue.add(new GridState(end));
		List<GridState> closed = new ArrayList<GridState>();
		
		while(!pathQueue.isEmpty()) {
			GridState parent = pathQueue.poll();
			closed.add(parent);
			
			if(floor != null ? parent.getPosition().equals(start) : successPaths.contains(parent.getPosition())) {
				if(floor != null) for(GridState g = parent; g != null; g = g.getParent()) floor.add(g.getPosition());
				else for(GridState g : closed) successPaths.add(g.getPosition());
				return true;
			}
			
			for(Vector2 childDir : Generator.directions) {
				Vector2 childPos = Vector2.add(parent.getPosition(), childDir);
				GridState child = new GridState(parent, childPos, (floor != null && floor.contains(childPos)) ? 1 :  newFloorCost);
				if(!obsticals.contains(childPos)) {
					int prevIndex = 0;
					if((prevIndex = closed.indexOf(child)) != -1) {
						if(child.getCost() < closed.get(prevIndex).getCost()) {
							closed.remove(prevIndex);
							pathQueue.add(child);
						}
					} else if(pathQueue.removeIf(a -> child.equals(a) && child.getCost() < a.getCost())) {
						pathQueue.add(child);
					} else {
						pathQueue.add(child);
					}
				}
			}
		}
		return false;
	}
}



