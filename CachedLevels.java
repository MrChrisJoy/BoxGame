import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class CachedLevels {
	private String player;
	
	public CachedLevels(String player) {
		this.player = player;
	}
	
	public Record getRecord() {
		Record c = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL("http://sokoban.heliohost.org/levelEdit.php").openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(URLEncoder.encode("?player=" + player, "UTF-8"));
			wr.flush();
			Scanner s = new Scanner(new InputStreamReader(conn.getInputStream()));
			if(s.hasNext()) c = new Record(s.nextLong(), s.next(), s.nextLong());
			else System.out.println("none found");
			wr.close();
			s.close();
			conn.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return c;
	}
	
	public boolean setRecord(Record r, long time) {
		boolean recordHolder = false;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL("http://sokoban.heliohost.org/levelEdit.php").openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(URLEncoder.encode("?player=" + player + "&seed=" + r.seed + "&time=" + time, "UTF-8"));
			wr.flush();
			Scanner s = new Scanner(new InputStreamReader(conn.getInputStream()));
			if(s.hasNextInt() && s.nextInt() == 1) recordHolder = true;
			wr.close();
			s.close();
			conn.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return recordHolder;
	}
}
