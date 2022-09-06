package connexionmongodb.metiers;

import static com.mongodb.client.model.Filters.eq;


import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;


public interface IClient {
	public static List<Document> getAllClient(MongoCollection<Document> c) {
		List<Document> docs = new  ArrayList<Document>();
		MongoCursor<Document> cursor = c.find().iterator();
		try {
            while(cursor.hasNext()) {
            	docs.add((Document) cursor.next());
            }
        	return docs;
		}catch(MongoException err) {
			System.out.println("Unable to download all Clients due to : "+err);
		}
		finally {
			cursor.close();
        }
		return docs;
	}	
	
	public static  Document getDocument(MongoCollection<Document> c,String cin) {
		Document doc=c.find(eq("cin",cin)).first();
		return doc;
	}
	
	public static Document getDocumentById(MongoCollection<Document> c,String id) {
		Document doc=c.find(eq("_id",id)).first();
		doc=c.find(eq("_id", new ObjectId(id))).first();
		return doc;
	}
	
	
	public void insertClient(MongoCollection<Document> c,Document client);
	public Document toDocument();
	public void deleteClient (MongoCollection<Document> c,String cin);
	public boolean clientDispo(MongoCollection<Document> c,String cin);

}
