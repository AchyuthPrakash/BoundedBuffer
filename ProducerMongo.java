import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.*;
import java.util.concurrent.Callable;

public class ProducerMongo implements Callable<Integer>, Messenger {
    private Queue<String> queue;
    private int maxSize;
    private String dataBase;
    private String collectionName;
    private ArrayList<String> movies;
    private int moviesSize;

    public ProducerMongo(Queue<String> queue, int maxSize, String dataBase, String collectionName){
        this.queue = queue;
        this.maxSize = maxSize;
        this.dataBase = dataBase;
        this.movies = new ArrayList<String>();
        this.collectionName = collectionName;

        // establish connection to mongoDB
        try {
            MongoClient db = new MongoClient("localhost", 27017);
            System.out.println("Successfully Connected" + " to the database");

            // connect to the database and retrieve the required collection
            MongoDatabase database = db.getDatabase(this.dataBase);     // dbName = "mongoDbDocker"
            MongoCollection<Document> collection = database.getCollection(this.collectionName);     // Retrieve the collection, collName = TopMoviesList
            System.out.println("Collection " + this.collectionName + " retrieved Successfully");

            // Get the documents using iterator
            FindIterable<Document> itrObj = collection.find();
            Iterator itr = itrObj.iterator();
            while (itr.hasNext()) {
                String str = itr.next().toString();
                String[] arrOfStr = str.split(",");
                int len = arrOfStr.length;
                movies.add("Movie: " + arrOfStr[1].substring(7) + ", Rating: " + arrOfStr[len - 1].substring(7, 10));
            }
            moviesSize = movies.size();
        }
        catch (Exception e) {
            System.out.println("Connection failed");
            System.out.println(e);
        }
    }

    @Override
    public Integer call() throws Exception {
        try {
            while (true) {
                /* entering the critical section now; wrap in synchronized block ; if queue is full, producer needs to wait
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
        int rand_int1 = rand.nextInt(this.moviesSize);
        String str = this.movies.get(rand_int1);
        return str;
    }

    @Override
    public void put(String str) {
        // process the string and add it to q -- here we convert all characters to upper-case
        queue.add(str.toUpperCase());
    }
}
