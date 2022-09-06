package connexionmongodb.metiers;

import static com.mongodb.client.model.Filters.eq;


import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import connexionmongodb.entities.Commande;

public interface ICommande {
	public static List<Document> getAllCommande(MongoCollection<Document> c) {
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
	
	public void insertCommande(MongoCollection<Document> c,Document d);
	public Document toDocument();
	public void deletCommande (MongoCollection<Document> c,String codeC);
	public void updateCommande(MongoCollection<Document> c,String codeC,Commande commande);
	public static boolean commandeDispo(MongoCollection<Document> c,String code) {
		MongoCursor<Document> cursor = c.find(eq("codeC",code)).iterator();
		if(cursor.hasNext())
			return true;
		return false;
	}

	public static Document getDocument(MongoCollection<Document> c,String codeC) {
		Document doc=c.find(eq("codeC",codeC)).first();
		return doc;
	}
	public static String genererCodeC(MongoCollection<Document> c) {
		int codeI=1000;
		while (commandeDispo(c,"C"+codeI)) {
			codeI++;
		}
		return "C"+codeI;
	}


		

	
}
