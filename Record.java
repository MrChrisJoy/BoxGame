public class Record {
	public final long seed;
	public final String opponent;
	public final long time;
	
	public Record(long seed, String opponent, long time) {
		this.seed = seed;
		this.opponent = opponent;
		this.time = time;
	}
	
	@Override
	public String toString() {
		return seed + " " + opponent + " " + time;
	}
}
