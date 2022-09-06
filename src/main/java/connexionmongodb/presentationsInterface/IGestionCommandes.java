package connexionmongodb.presentationsInterface;

import java.util.List;


import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import connexionmongodb.metiers.IClient;
import connexionmongodb.metiers.ICommande;
import connexionmongodb.metiers.IProduit;

public interface IGestionCommandes {
	
	public static  MongoCollection<Document> ConnexioMongod (String collection) {
	
		MongoClient mongoClient =  MongoClients.create();
		MongoDatabase database = mongoClient.getDatabase("gestionCommandes");
        MongoCollection<Document> Collection = database.getCollection(collection);
		return Collection;
	}
	
	public static void afficherCommandes (JTable table) {
		List<Document> commandes =ICommande.getAllCommande(ConnexioMongod("commande"));
        String[] coulunmnames = {"CodeC","Cin","clientN","ProduitN","QteC","Total"};
        DefaultTableModel model = new DefaultTableModel(coulunmnames,0);
    	model.addRow(new Object[] {coulunmnames[0],coulunmnames[1],coulunmnames[2],coulunmnames[3],coulunmnames[4],coulunmnames[5]});
        for(Document ppp:commandes) {	
        	String clientN= (IClient.getDocumentById(ConnexioMongod("client"), ppp.getString("client"))).getString("nom");
        	String cinN=(IClient.getDocumentById(ConnexioMongod("client"), ppp.getString("client"))).getString("cin");
           	@SuppressWarnings("unchecked")
			List<Document> docccc =(List<Document>) ppp.get("produits");
        	String produitN=(IProduit.getDocumentById(ConnexioMongod("produit"), (docccc.get(0).getString("produit")))).getString("libelle");
        	int qtecc=docccc.get(0).getInteger("qtec");
        	double total=ppp.getDouble("total");
        	String codeC=ppp.getString("codeC");
        	model.addRow(new Object[] {codeC,cinN,clientN,produitN,qtecc,total+" Dh"});
        }
        table.setModel(model);
	}
	
	
	
	public static void afficherProduits (JTable table) {
    List<Document> product =IProduit.getAllProduit(ConnexioMongod("produit"));
    String[] coulunmnames = {"libelle","Qte","PrixU"};
    DefaultTableModel model = new DefaultTableModel(coulunmnames,0);
	model.addRow(new Object[] {coulunmnames[0],coulunmnames[1],coulunmnames[2]});
    for(Document ppp:product) {
    	String liblle= ppp.getString("libelle");
    	int qte=ppp.getInteger("qte");
    	double prixU=ppp.getDouble("prixU");
    	model.addRow(new Object[] {liblle,qte,prixU+" Dh"});
    }
    table.setModel(model);
	}
	
	
	public static void afficherClients (JTable table) {
	List<Document> product =IClient.getAllClient(ConnexioMongod("client"));
    String[] coulunmnames = {"Cin","nom","prenom","adresse"};
    DefaultTableModel model = new DefaultTableModel(coulunmnames,0);
	model.addRow(new Object[] {coulunmnames[0],coulunmnames[1],coulunmnames[2],coulunmnames[3]});
    for(Document ppp:product) {
    	String cin=ppp.getString("cin");
    	String liblle= ppp.getString("nom");
    	String qte=ppp.getString("prenom");
    	String prixU=ppp.getString("adresse");
    	model.addRow(new Object[] {cin,liblle,qte,prixU});
    }
    table.setModel(model);
	}
	

}
