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

		Document doc = null;
		try {
			doc = Jsoup.connect(this.website).userAgent("mozilla/17.0").get();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		String title = doc.title();
//		System.out.println(title);
		links = doc.select("a[href]");
		linksSize = links.size();
		//System.out.println(sz);
	}

	@Override
	public Integer call() throws Exception {
		try {
			while (true) {
				/* entering the critical section now; wrap in synchronized block
				 * if queue is full, producer needs to wait
				 * else add entry to the queue */
				synchronized (queue) {
					while (queue.size() == maxSize) {
						System.out .println("Producer -- " + this.toString() + " Queue is full, will wait now");
						queue.wait();
					}
					/* producer gets some data from the web-page and put it into the queue */
					String str = get();
					//System.out.println(str + " " + queue.size() + " " + maxSize);
					if(!str.isEmpty()) {
						System.out.println("Producer -- " +  this.toString() + " Producing value : " + str);
						queue.add(str);
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
		// System.out.print("In producer get() ");
		Random rand = new Random();
		int rand_int1 = rand.nextInt(this.linksSize);
		String str = this.links.get(rand_int1).text();
		// System.out.println(rand_int1 + " " + str);
		return str;
	}

	@Override
	public void put() {
		// TODO Auto-generated method stub

	}
}