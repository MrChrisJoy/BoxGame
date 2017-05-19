import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Scanner;

public class CachedLevels {
	private String player;
	private URLConnection lu;
	private OutputStreamWriter wr;
	
	public CachedLevels(String player) throws IOException {
		this.player = player;
	}
	
	private void connect() throws MalformedURLException, IOException {
		lu = new URL("http://sokoban.heliohost.org/levelEdit.php").openConnection();
		lu.setDoOutput(true);
		wr = new OutputStreamWriter(lu.getOutputStream());
		wr.flush();
	}
	
	public Record getRecord() {
		Record c = null;
		try {
			connect();
			wr.write(URLEncoder.encode("?player=" + player, "UTF-8"));
			wr.flush();
			Scanner s = new Scanner(new InputStreamReader(lu.getInputStream()));
			if(s.hasNext()) c = new Record(s.nextLong(), s.next(), s.nextLong());
			else System.out.println("none found");
			wr.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return c;
	}
	
	public boolean setRecord(Record r, long time) {
		boolean recordHolder = false;
		try {
			connect();
			wr.write(URLEncoder.encode("?player=" + player + "&seed=" + r.seed + "&time=" + time, "UTF-8"));
			wr.flush();
			
			Scanner s = new Scanner(new InputStreamReader(lu.getInputStream()));
			if(s.hasNextInt() && s.nextInt() == 1) recordHolder = true;
			wr.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return recordHolder;
	}
}
