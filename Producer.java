import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Callable;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Producer implements Callable<Integer>, Messenger {
	private Queue<String> queue;
	private int maxSize;
	private String website;
	private Elements links;
	private int linksSize;

	public Producer(Queue<String> queue, int maxSize, String website){
		this.queue = queue;
		this.maxSize = maxSize;
		this.website = website;

		// connect to website and retrieve the data
		Document doc = null;
		try {
			doc = Jsoup.connect(this.website).userAgent("mozilla/17.0").get();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		links = doc.select("a[href]");
		linksSize = links.size();
	}

	@Override
	public Integer call() throws Exception {
		try {
			while (true) {
				/* entering the critical section now; wrap in synchronized block; if queue is full, producer needs to wait
				 * else add entry to the queue */
				synchronized (queue) {
					while (queue.size() == maxSize) {
						System.out .println("Producer -- " + this.toString() + " Queue is full, will wait now");
						queue.wait();
					}
					/* producer gets some data from the web-page and put it into the queue */
					String str = get();
					if(!str.isEmpty()) {
						System.out.println("Producer -- " +  this.toString() + " Producing value : " + str);
						put(str);
					}
					queue.notifyAll();
				}
			}
		}

		catch(Exception e) {
			return -1;
		}
	}

	@Override
	public String get() {
		// randomly pick an entry and return it
		Random rand = new Random();
		int rand_int1 = rand.nextInt(this.linksSize);
		String str = this.links.get(rand_int1).text();
		return str;
	}

	@Override
	public void put(String str) {
		// process the string and add it to q -- here we convert all characters to lowercase
		queue.add(str.toLowerCase());
	}
}