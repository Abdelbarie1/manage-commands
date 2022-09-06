package connexionmongodb.metiers;

import static com.mongodb.client.model.Filters.eq;


import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import connexionmongodb.entities.Produit;

public interface IProduit {
	public static List<Document> getAllProduit(MongoCollection<Document> c) {
		List<Document> docs = new  ArrayList<Document>();
		MongoCursor<Document> cursor = c.find().iterator();
		try {
            while(cursor.hasNext()) {
            	docs.add((Document) cursor.next());
            	//++++++++++++++++++++++++++++++++++++++++
            	//System.out.println(cursor.next().toJson());
            }
        	return docs;

     
		}catch(MongoException err) {
			System.out.println("Unable to download all products due to : "+err);
		}
		finally {
			cursor.close();
        }
		return docs;
	}
	public void insertProduit(MongoCollection<Document> c,Document d);
	public void updateProduit(MongoCollection<Document> c, Produit d,int qte);
	public boolean produitDispo(MongoCollection<Document> c,String libelle);
	public static Document getDocument(MongoCollection<Document> c,String libelle) {
		Document doc=c.find(eq("libelle",libelle)).first();
		return doc;
	}
	public static Document getDocumentById(MongoCollection<Document> c,String id) {
		Document doc=c.find(eq("_id",id)).first();
		doc=c.find(eq("_id", new ObjectId(id))).first();
		return doc;
	}
	
	public Document toDocument();
	public void deleteproduit (MongoCollection<Document> c,String libelle);
}
