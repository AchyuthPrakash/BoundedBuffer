import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Mongo {
    private static ArrayList<String> moviesList;

    public static void getData(){
        org.jsoup.nodes.Document document = null;
        try {
            document = Jsoup.connect("http://www.imdb.com/chart/top").get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        moviesList = new ArrayList<String>();

        for (Element row : document.select("table.chart.full-width tr")) {
            final String title = row.select(".titleColumn a").text();
            final String rating = row.select(".imdbRating").text();
            //System.out.println(title + " -> Rating: " + rating);
            moviesList.add(title + "@" + rating);
        }

//        for(int i = 0; i< moviesList.size(); i++){
//            System.out.println(moviesList.get(i));
//        }
    }
    public static void main(String[] args) {
        getData();

        try {
            MongoClient db = new MongoClient("localhost", 27017);

            //MongoCredential credential;
            //credential = MongoCredential.createCredential("GFGUser", "mongoDb","password".toCharArray());
            //System.out.println("Successfully Connected" + " to the database");

            MongoDatabase database = db.getDatabase("mongoDb");
            //System.out.println("Credentials are: "+ credential);

//            // Create the collection -- Already Done!
//            String collectionName = "Hello";
//            database.createCollection(collectionName);
//            System.out.println("Collection created Successfully");

            MongoCollection<Document> collection = database.getCollection("Hello");     // Retrieve the collection
            System.out.println("Collection retrieved Successfully");

            // Adding the documents into a list
            List<Document> dbList  = new ArrayList<Document>();
            for(int i=1; i<moviesList.size(); i++){
                String str = moviesList.get(i);
                String[] arrOfStr = str.split("@");
                Document document  = new Document("title", arrOfStr[0]).append("about", arrOfStr[1]);
                dbList.add(document);
            }

            // collection.insertMany(dbList); // Insert the list of documents into DB -- Already Done!!

            //collection.drop();

            System.out.println("Displaying the list" + " of Documents");

            // Get the list of documents from the DB
            FindIterable<Document> iterobj = collection.find();

            // Print the documents using iterators
            Iterator itr = iterobj.iterator();
            while (itr.hasNext()) {
                System.out.println(itr.next());
            }
        }
        catch (Exception e) {
            System.out.println("Connection failed");
            System.out.println(e);
        }
    }

}
