package connexionmongodb.entities;

import org.bson.Document;


import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;


import static com.mongodb.client.model.Filters.eq;


import connexionmongodb.metiers.IProduit;

public class Produit implements IProduit {
	private String libelle;
	private int qte;
	private double prixU;
	
	
	public Produit(String libelle, int qte, double prixU) {
		super();
		this.libelle = libelle;
		this.qte = qte;
		this.prixU = prixU;
	}
	
	public Produit(Document d) {
		super();
		this.libelle = d.getString("libelle");
		this.qte = d.getInteger("qte");
		this.prixU = d.getDouble("prixU");
	}
	
	public Produit() {
		super();
	}

	
	
	@Override
	public String toString() {
		return "Produit [libelle=" + libelle + ", qte=" + qte + ", prixU=" + prixU + "]";
	}
	
	

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public int getQte() {
		return qte;
	}

	public void setQte(int qte) {
		this.qte = qte;
	}

	public double getPrixU() {
		return prixU;
	}

	public void setPrixU(double prixU) {
		this.prixU = prixU;
	}

	@Override
	public void insertProduit(MongoCollection<Document> c, Document d) {
		// TODO Auto-generated method stub
		if (!produitDispo(c,d.getString("libelle"))) {
			try {
				c.insertOne(d);
				System.out.println("Success ! Inserted document ");
			}
			catch(MongoException me) {
				System.err.println("Unable to insert due to an error :"+me);
			}
		}
		else {
			System.out.println("Produit dispo!!");
		}	
	}
	
	@Override
	public void updateProduit(MongoCollection<Document> c, Produit  produit,int qte) {
		// TODO Auto-generated method stub
		if(produit.getQte()==0) {
			deleteproduit(c,produit.getLibelle());
		}
		else {
			try {
				UpdateResult result = c.updateOne(eq("libelle",produit.getLibelle()),Updates.inc("qte",-qte));
				System.out.println("Success ! Modified document id : "+result.getUpsertedId());
			}
			catch(MongoException me) {
				System.err.println("Unable to update due to an error :"+me);
			}
		}
		
		
	}
	@Override
	public boolean produitDispo(MongoCollection<Document> c, String libelle) {
		// TODO Auto-generated method stub
		MongoCursor<Document> cursor = c.find(eq("libelle",libelle)).iterator();
		if(cursor.hasNext())
			return true;
		return false;
	}
	
	@Override
	public Document toDocument() {
		// TODO Auto-generated method stub
		return new Document("libelle",libelle).append("qte", qte).append("prixU", prixU);
	}

	

	@Override
	public void deleteproduit(MongoCollection<Document> c, String libelle) {
		// TODO Auto-generated method stub
		try {
            DeleteResult result = c.deleteOne(eq("libelle",libelle));
            System.out.println("Deleted document count: " + result.getDeletedCount());
        } catch (MongoException me) {
            System.err.println("Unable to delete due to an error: " + me);
        }	
	}

	
	
}
