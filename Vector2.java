public class Vector2 {
	public final int x;
	public final int y;
	
	public static final Vector2 zero = new Vector2(0, 0);
	
	public Vector2(int x, int y) {
		this.x = x;
		this.y = y;
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
	
	public static Vector2 flipNorm(Vector2 v) {
		return (Math.abs(v.x) < Math.abs(v.y)) ? new Vector2(v.x > 0 ? 1 : -1, 0) : new Vector2(0, v.y > 0 ? 1 : -1);
	}
	
	public static int manMagnitude(Vector2 v) {
		return Math.abs(v.x + v.y);
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
