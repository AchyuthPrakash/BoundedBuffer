import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MongoDocker {
    private static ArrayList<String> moviesList;

    public static void getData(){
        org.jsoup.nodes.Document document = null;
        try {
            document = Jsoup.connect(MongoDbParams.website).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        moviesList = new ArrayList<>();

        assert document != null;
        for (Element row : document.select("table.chart.full-width tr")) {
            final String title = row.select(".titleColumn a").text();
            final String rating = row.select(".imdbRating").text();
            moviesList.add(title + "@" + rating);
        }
    }

    public static void setUpDatabase(){
        try {
            MongoClient db = new MongoClient(HostPort.host, HostPort.port);
            MongoDatabase database = db.getDatabase(MongoDbParams.database);

            database.createCollection(MongoDbParams.collection);        // Create a collection
            System.out.println("Collection -- created -- Successfully");

            MongoCollection<Document> collection = database.getCollection(MongoDbParams.collection);     // Retrieve the collection
            System.out.println("Collection -- retrieved -- Successfully");

            // Adding the documents into a list
            List<Document> dbList  = new ArrayList<>();
            for(int i=1; i<moviesList.size(); i++){
                String str = moviesList.get(i);
                String[] arrOfStr = str.split("@");
                Document document  = new Document("title", arrOfStr[0]).append("about", arrOfStr[1]);
                dbList.add(document);
            }
            collection.insertMany(dbList); // Insert the list of documents into DB -- Already Done!!

            System.out.println("Displaying the list of Documents");
            FindIterable<Document> iterobj = collection.find();     // Get the list of documents from the DB
            // Print the documents using iterators
            for (Document document : iterobj) {
                System.out.println(document);
            }
        }
        catch (Exception e) {
            System.out.println("Connection failed");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        getData();
        setUpDatabase();
    }
}