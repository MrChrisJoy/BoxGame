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
	
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof Record)) return false;
		Record r = (Record)o;
		return r.seed == seed && r.opponent.equals(opponent) && r.time == time;
	}
}
