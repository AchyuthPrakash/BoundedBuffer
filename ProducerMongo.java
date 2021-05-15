import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Callable;

public class ProducerMongo implements Callable<Integer>, Messenger {
    private Queue<String> queue;
    private int maxSize;
    private String dataBase;
    private ArrayList<String> movies;
    private int moviesSize;

    public ProducerMongo(Queue<String> queue, int maxSize, String dataBase){
        this.queue = queue;
        this.maxSize = maxSize;
        this.dataBase = dataBase;
        this.movies = new ArrayList<String>();

        MongoClient db = new MongoClient("localhost", 27017);
        System.out.println("Successfully Connected" + " to the database");

        MongoDatabase database = db.getDatabase(this.dataBase);     // dbName = "mongoDb"
        MongoCollection<Document> collection = database.getCollection("Hello");     // Retrieve the collection
        System.out.println("Collection retrieved Successfully");

        FindIterable<Document> itrObj = collection.find();

        // Get the documents using iterators
        Iterator itr = itrObj.iterator();
        while (itr.hasNext()) {
            //System.out.println(itr.next());
            String str = itr.next().toString();
            String[] arrOfStr = str.split(",");
            int len = arrOfStr.length;
            movies.add("Movie: " + arrOfStr[1].substring(7) + ", Rating: " + arrOfStr[len-1].substring(7, 10));
        }

        moviesSize = movies.size();
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
        int rand_int1 = rand.nextInt(this.moviesSize);
        String str = this.movies.get(rand_int1);
        // System.out.println(rand_int1 + " " + str);
        return str;
    }

    @Override
    public void put() {
        // TODO Auto-generated method stub

    }
}
