package connexionmongodb.entities;

import static com.mongodb.client.model.Filters.eq;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import connexionmongodb.metiers.IClient;
import connexionmongodb.metiers.ICommande;
import connexionmongodb.metiers.IProduit;

public class Commande implements ICommande{
	private Client client;
	private List<Produit> produits;
	private int[] qteC;
	private Date date;
	private double total;
	private String codeC;
	
	
	public Commande(Client client, List<Produit> produits, int[] qteC, Date date, double total,String codeC) {
		super();
		this.client = client;
		this.produits = produits;
		this.qteC = qteC;
		this.date = date;
		this.total = total;
		this.codeC = codeC;

	}
	
	
	
	




	@Override
	public String toString() {
		return "Commande [client=" + client + ", produits=" + produits + ", qteC=" + Arrays.toString(qteC) + ", date="
				+ date + ", total=" + total + ", codeC=" + codeC + "]";
	}

	public Commande() {
		super();
	}
	
	
	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public List<Produit> getProduits() {
		return produits;
	}

	public void setProduits(List<Produit> produits) {
		this.produits = produits;
	}

	public int[] getQteC() {
		return qteC;
	}

	public void setQteC(int[] qteC) {
		this.qteC = qteC;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getCodeC() {
		return codeC;
	}

	public void setCodeC(String codeC) {
		this.codeC = codeC;
	}

	@Override
	public void insertCommande(MongoCollection<Document> c, Document d) {
		// TODO Auto-generated method stub
			try {
				 c.insertOne(d);
				System.out.println("Success ! Inserted document ");
			}
			catch(MongoException me) {
				System.err.println("Unable to insert due to an error :"+me);
			}		
	}
	
	@Override
	public Document toDocument() {
		// TODO Auto-generated method stub
		MongoClient mongoClient =  MongoClients.create();
		MongoDatabase database = mongoClient.getDatabase("gestionCommandes");
        MongoCollection<Document> collection = database.getCollection("client");
        Document docClient = IClient.getDocument(collection, client.getCin());
		
        Document document=new Document("client",docClient.getObjectId("_id").toString());
        List<Document> l = new ArrayList<Document>();
        collection = database.getCollection("produit");
        Document docProduit ;
        int i=0;
        for(Produit p:produits) {
        	docProduit= IProduit.getDocument(collection, p.getLibelle());
        	l.add(new Document("produit",docProduit.getObjectId("_id").toString()).append("qtec", qteC[i++]));
        }
        document.append("produits", l);
        document.append("date",date);
        double totale=0;
        totale+=qteC[0]*produits.get(0).getPrixU();
        document.append("total", totale);
        document.append("codeC", codeC);
		return document;
	}

	@Override
	public void deletCommande (MongoCollection<Document> c,String codeC) {
		// TODO Auto-generated method stub
		try {
			
            DeleteResult result = c.deleteOne(eq("codeC",codeC));
            System.out.println("Deleted document count: " + result.getDeletedCount());
        } catch (MongoException me) {
            System.err.println("Unable to delete due to an error: " + me);
        }	
		
	}

	@Override
	public void updateCommande(MongoCollection<Document> c,String codeC,Commande commande)
	{
		// TODO Auto-generated method stub
			try {
				MongoClient mongoClient =  MongoClients.create();
				MongoDatabase database = mongoClient.getDatabase("gestionCommandes");
		        MongoCollection<Document> collection = database.getCollection("produit");
		        MongoCollection<Document> collectionC = database.getCollection("client");

		        Document docClient = IClient.getDocument(collectionC, client.getCin());
		        

				 Document docProduit ;
			     List<Document> l = new ArrayList<Document>();
			        int i=0;
			        for(Produit p:commande.getProduits()) {
			        	docProduit= IProduit.getDocument(collection, p.getLibelle());
			        	l.add(new Document("produit",docProduit.getObjectId("_id").toString()).append("qtec",commande.getQteC()[i++]));
			        	//totale+=p.getPrixU()*commande.getQteC()[i++];
			        }
				UpdateResult result = c.updateOne(eq("codeC",codeC),Updates.combine(Updates.set("client",docClient.getObjectId("_id").toString()),Updates.set("produits",l),Updates.set("date",commande.getDate()),Updates.set("total", commande.getTotal())));
				System.out.println("Success ! Modified document id : "+result.getUpsertedId());
			}
			catch(MongoException me) {
				System.err.println("Unable to update due to an error :"+me);
			}
		}
}
