import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CachedLevels {
	private String player;
	
	public CachedLevels(String player) {
		this.player = player;
	}
	
	public Record getRecord() {
		Record c = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL("http://sokoban.heliohost.org/levelEdit.php?player=" + player).openConnection();
			conn.setDoOutput(true);
			Scanner s = new Scanner(new InputStreamReader(conn.getInputStream()));
			if(s.hasNext()) c = new Record(s.nextLong(), s.next(), s.nextLong());
			s.close();
			conn.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return c;
	}
	
	public boolean setRecord(long seed, long time) {
		boolean recordHolder = false;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL("http://sokoban.heliohost.org/levelEdit.php?player=" + player + "&seed=" + seed + "&time=" + time).openConnection();
			conn.setDoOutput(true);
			Scanner s = new Scanner(new InputStreamReader(conn.getInputStream()));
			if(s.hasNextInt() && s.nextInt() == 1) recordHolder = true;
			s.close();
			conn.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return recordHolder;
	}
}
