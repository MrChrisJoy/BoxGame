/**
 * Updated
 * @author AM
 *
 */
public class Vector2 {
	public final int x;
	public final int y;
	private BlockType blockType;
	
	public static final Vector2 zero = new Vector2(0, 0);
	
	public static final int LEFT = 0;
	public static final int UP = 1;
	public static final int RIGHT = 2;
	public static final int DOWN = 3;

	public static final Vector2[] directions = {new Vector2(-1, 0), new Vector2(0, 1), new Vector2(1, 0), new Vector2(0, -1)};
	
	public Vector2(int x, int y) {
		this.x = x;
		this.y = y;
		blockType = BlockType.wall;
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	public BlockType getBlockType() {
		return this.blockType;
	}
	
	public void setBlockType(BlockType type) {
		this.blockType = type;
	}
	
	public static Vector2 add(Vector2 v1, Vector2 v2) {
		return new Vector2(v1.x + v2.x, v1.y + v2.y);
	}
	
	public static Vector2 subtract(Vector2 v1, Vector2 v2) {
		return new Vector2(v1.x - v2.x, v1.y - v2.y);
	}
	
	public static Vector2 scale(Vector2 v, int scale) {
		return new Vector2(v.x * scale, v.y * scale);
	}
	
	public static int manDistance(Vector2 v1, Vector2 v2) {
		return Math.abs(v1.x - v2.x) + Math.abs(v1.y - v2.y);
	}
	
	
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof Vector2)) return false;
		Vector2 v = (Vector2)o;
		return x == v.x && y == v.y;
	}
	
	@Override
	public int hashCode() {
		return 7 * x + 13 * y;
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
