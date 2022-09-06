package connexionmongodb.presentationsInterface;

import java.awt.EventQueue;




import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;


import connexionmongodb.metiers.ICommande;
import connexionmongodb.metiers.IProduit;
import connexionmongodb.entities.Client;
import connexionmongodb.entities.Commande;
import connexionmongodb.entities.Produit;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

import java.awt.Window.Type;
import javax.swing.JTextPane;

import java.awt.Dialog.ModalExclusionType;
import java.awt.SystemColor;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EtchedBorder;




public class GestionCommandes implements IGestionCommandes {

	private JFrame frame;
	private JTextField nom;
	private JTextField label;
	private JTextField qauntite;
	private JTable table;
	private JTable table_1;
	private JTextField libelle;
	private JTextField prenom;
	private JTextField cinC;
	private JTextField adresse;
	private JTextField qte;
	private JTextField prixU;
	private JTextField codeCommande;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GestionCommandes window = new GestionCommandes();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GestionCommandes() {
		initialize();
	}
	   
	
	public MongoClient getConnection() {
		return MongoClients.create();
	};
	
	
	
	
	
	
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBackground(Color.GRAY);
		frame.getContentPane().setBackground(SystemColor.info);
		frame.getContentPane().setForeground(Color.DARK_GRAY);
		frame.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		frame.setType(Type.POPUP);
		frame.setBounds(100, 100, 1214, 958);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Gestion des Commandes");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblNewLabel.setForeground(new Color(0, 0, 0));
		lblNewLabel.setBounds(374, 2, 332, 43);
		frame.getContentPane().add(lblNewLabel);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 218, 185));
		panel.setForeground(Color.RED);
		panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Commander", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(264, 56, 495, 430);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Nom");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel_1.setForeground(new Color(0, 0, 0));
		lblNewLabel_1.setBounds(10, 31, 122, 29);
		panel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_2 = new JLabel("Quantite");
		lblNewLabel_1_2.setForeground(new Color(0, 0, 0));
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel_1_2.setBounds(10, 293, 122, 29);
		panel.add(lblNewLabel_1_2);
		
		nom = new JTextField();
		nom.setColumns(10);
		nom.setBounds(140, 31, 151, 35);
		panel.add(nom);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Produit");
		lblNewLabel_1_1_1.setForeground(new Color(0, 0, 0));
		lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel_1_1_1.setBounds(10, 237, 136, 29);
		panel.add(lblNewLabel_1_1_1);
		
		label = new JTextField();
		label.setColumns(10);
		label.setBounds(140, 237, 151, 35);
		panel.add(label);
		
		qauntite = new JTextField();
		qauntite.setColumns(10);
		qauntite.setBounds(140, 293, 151, 35);
		panel.add(qauntite);
		
		JButton btnCommander = new JButton("Commander");
		btnCommander.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	
	            List<Produit> products =new ArrayList<Produit>();
				Client client=new Client(nom.getText().toString(),prenom.getText().toString(),adresse.getText().toString(),cinC.getText().toString());
				Document docProduit=IProduit.getDocument(IGestionCommandes.ConnexioMongod("produit"), label.getText().toString());
				Produit produit=new Produit(docProduit.getString("libelle"),docProduit.getInteger("qte"),docProduit.getDouble("prixU"));
				products.add(produit);
				
				int [] qteC = {Integer.parseInt(qauntite.getText().toString())};
				double totale=0;
				totale+=qteC[0]*produit.getPrixU();
				Commande commande =new Commande(client,products,qteC,new Date(),totale,ICommande.genererCodeC(IGestionCommandes.ConnexioMongod("commande")));
				client.insertClient(IGestionCommandes.ConnexioMongod("client"), client.toDocument());
				produit.updateProduit(IGestionCommandes.ConnexioMongod("produit"),produit,qteC[0]);
				commande.insertCommande(IGestionCommandes.ConnexioMongod("commande"), commande.toDocument());
				
				//Afficher Commandes
				IGestionCommandes.afficherCommandes(table);
				IGestionCommandes.afficherProduits(table_1);



			}
		});
		btnCommander.setBounds(58, 362, 122, 57);
		panel.add(btnCommander);
		btnCommander.setForeground(new Color(0, 0, 0));
		btnCommander.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnCommander.setBackground(new Color(255, 222, 173));
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.setBounds(293, 362, 86, 57);
		panel.add(btnDelete);
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String codeC;
				codeC = codeCommande.getText().toString();
	
				Commande  commande =new Commande();
				commande.deletCommande(IGestionCommandes.ConnexioMongod("commande"),codeC);
				
				
				//Afficher Commandes
				IGestionCommandes.afficherCommandes(table);

				
	             

				
			}
		});
		btnDelete.setForeground(new Color(0, 0, 0));
		btnDelete.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnDelete.setBackground(new Color(255, 222, 173));
		
		JButton btnUpdiat = new JButton("Update");
		btnUpdiat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String codeC= codeCommande.getText().toString();
				
			
	            List<Produit> products =new ArrayList<Produit>();
				Client client=new Client(nom.getText().toString(),prenom.getText().toString(),adresse.getText().toString(),cinC.getText().toString());
				Document docProduit=IProduit.getDocument(IGestionCommandes.ConnexioMongod("produit"), label.getText().toString());
				Produit produit=new Produit(docProduit.getString("libelle"),docProduit.getInteger("qte"),docProduit.getDouble("prixU"));
				Document oldCommande=ICommande.getDocument(IGestionCommandes.ConnexioMongod("commande"), codeC);
				@SuppressWarnings("unchecked")
				List<Document> docccc =(List<Document>) oldCommande.get("produits");

				products.add(produit);
				
				int [] qteC = {Integer.parseInt(qauntite.getText().toString())};
				double totale=0;
				totale+=qteC[0]*produit.getPrixU();
				Commande commande =new Commande(client,products,qteC,new Date(),totale,"C0003");
				client.insertClient(IGestionCommandes.ConnexioMongod("client"), client.toDocument());
				produit.updateProduit(IGestionCommandes.ConnexioMongod("produit"), produit, commande.getQteC()[0]-docccc.get(0).getInteger("qtec"));

				commande.updateCommande(IGestionCommandes.ConnexioMongod("commande"), codeC, commande);
				
				//Afficher Commandes
				IGestionCommandes.afficherCommandes(table);
				IGestionCommandes.afficherProduits(table);


				
			
			}
		});
		btnUpdiat.setBounds(386, 362, 99, 57);
		panel.add(btnUpdiat);
		btnUpdiat.setForeground(new Color(0, 0, 0));
		btnUpdiat.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnUpdiat.setBackground(new Color(255, 222, 173));
		
		JLabel lblNewLabel_1_1_1_1 = new JLabel("Prenom");
		lblNewLabel_1_1_1_1.setForeground(new Color(0, 0, 0));
		lblNewLabel_1_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel_1_1_1_1.setBounds(10, 84, 86, 29);
		panel.add(lblNewLabel_1_1_1_1);
		
		prenom = new JTextField();
		prenom.setColumns(10);
		prenom.setBounds(140, 78, 151, 35);
		panel.add(prenom);
		
		JLabel lblNewLabel_1_2_1 = new JLabel("CIN");
		lblNewLabel_1_2_1.setForeground(new Color(0, 0, 0));
		lblNewLabel_1_2_1.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel_1_2_1.setBounds(10, 123, 71, 29);
		panel.add(lblNewLabel_1_2_1);
		
		cinC = new JTextField();
		cinC.setColumns(10);
		cinC.setBounds(140, 124, 151, 35);
		panel.add(cinC);
		
		JLabel lblNewLabel_1_2_1_1 = new JLabel("Adresse");
		lblNewLabel_1_2_1_1.setForeground(new Color(0, 0, 0));
		lblNewLabel_1_2_1_1.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel_1_2_1_1.setBounds(10, 175, 86, 29);
		panel.add(lblNewLabel_1_2_1_1);
		
		adresse = new JTextField();
		adresse.setColumns(10);
		adresse.setBounds(140, 175, 151, 35);
		panel.add(adresse);
		
		JLabel lblNewLabel_1_3 = new JLabel("Code");
		lblNewLabel_1_3.setForeground(new Color(0, 0, 0));
		lblNewLabel_1_3.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel_1_3.setBounds(299, 319, 56, 29);
		panel.add(lblNewLabel_1_3);
		
		codeCommande = new JTextField();
		codeCommande.setColumns(10);
		codeCommande.setBounds(386, 316, 99, 35);
		panel.add(codeCommande);
		
		JButton btnDelete_1 = new JButton("Afficher Produit");
		btnDelete_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				IGestionCommandes.afficherProduits(table_1);
				
			}
		});
		btnDelete_1.setForeground(new Color(0, 0, 0));
		btnDelete_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnDelete_1.setBackground(new Color(255, 222, 173));
		btnDelete_1.setBounds(51, 601, 158, 57);
		frame.getContentPane().add(btnDelete_1);
		
		JButton btnAfficherClient = new JButton("Afficher Client");
		btnAfficherClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				IGestionCommandes.afficherClients(table_1);

			}
		});
		btnAfficherClient.setForeground(new Color(0, 0, 0));
		btnAfficherClient.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnAfficherClient.setBackground(new Color(255, 222, 173));
		btnAfficherClient.setBounds(51, 511, 158, 57);
		frame.getContentPane().add(btnAfficherClient);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
			}
		));
		table.setBackground(new Color(255, 228, 181));
		table.setToolTipText("");
		table.setBounds(769, 56, 406, 370);
		frame.getContentPane().add(table);
		
		table_1 = new JTable();
		table_1.setBackground(new Color(255, 228, 181));
		table_1.setBounds(264, 503, 495, 183);
		frame.getContentPane().add(table_1);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setForeground(Color.RED);
		panel_1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Add Product", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_1.setBackground(new Color(255, 218, 185));
		panel_1.setBounds(769, 502, 406, 184);
		frame.getContentPane().add(panel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Libelle");
		lblNewLabel_1_1.setForeground(new Color(0, 0, 0));
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel_1_1.setBounds(10, 21, 86, 29);
		panel_1.add(lblNewLabel_1_1);
		
		libelle = new JTextField();
		libelle.setColumns(10);
		libelle.setBounds(106, 21, 151, 35);
		panel_1.add(libelle);
		
		JButton btnAddClient = new JButton("Add Client");
		btnAddClient.setForeground(Color.RED);
		btnAddClient.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnAddClient.setBackground(Color.BLUE);
		btnAddClient.setBounds(10, 233, 115, 57);
		panel_1.add(btnAddClient);
		
		JButton btnDelete_2 = new JButton("Delete");
		btnDelete_2.setForeground(Color.RED);
		btnDelete_2.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnDelete_2.setBackground(Color.BLUE);
		btnDelete_2.setBounds(135, 233, 122, 57);
		panel_1.add(btnDelete_2);
		
		JLabel lblNewLabel_1_1_2 = new JLabel("Quantite");
		lblNewLabel_1_1_2.setForeground(new Color(0, 0, 0));
		lblNewLabel_1_1_2.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel_1_1_2.setBounds(10, 72, 86, 29);
		panel_1.add(lblNewLabel_1_1_2);
		
		JLabel lblNewLabel_1_1_3 = new JLabel("PrixU");
		lblNewLabel_1_1_3.setForeground(new Color(0, 0, 0));
		lblNewLabel_1_1_3.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel_1_1_3.setBounds(10, 117, 86, 29);
		panel_1.add(lblNewLabel_1_1_3);
		
		qte = new JTextField();
		qte.setColumns(10);
		qte.setBounds(106, 67, 151, 35);
		panel_1.add(qte);
		
		prixU = new JTextField();
		prixU.setColumns(10);
		prixU.setBounds(106, 117, 151, 35);
		panel_1.add(prixU);
		
		JButton btnAddProduit = new JButton("Add Produit");
		btnAddProduit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Add Produit
				
	            
	            Produit produit=new Produit(libelle.getText().toString(),Integer.parseInt(qte.getText().toString()),Double.parseDouble(prixU.getText().toString()));
	            
	            produit.insertProduit(IGestionCommandes.ConnexioMongod("produit"), produit.toDocument());
	            
				IGestionCommandes.afficherProduits(table_1);

	            
				
				

			}
		});
		btnAddProduit.setForeground(new Color(0, 0, 0));
		btnAddProduit.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnAddProduit.setBackground(new Color(255, 222, 173));
		btnAddProduit.setBounds(267, 21, 122, 125);
		panel_1.add(btnAddProduit);
		
		JButton btnImprimer = new JButton("Imprimer");
		btnImprimer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Imprimer Client/produits
				MessageFormat entete = new MessageFormat("Liste");
				MessageFormat pied =new MessageFormat("Page )");
				try {
					table_1.print(JTable.PrintMode.FIT_WIDTH,entete,pied);
				}catch(Exception ee) {
					JOptionPane.showConfirmDialog(null, "erruer : \n "+ee,"Impresion",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnImprimer.setForeground(new Color(0, 0, 0));
		btnImprimer.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnImprimer.setBackground(new Color(255, 222, 173));
		btnImprimer.setBounds(601, 697, 158, 57);
		frame.getContentPane().add(btnImprimer);
		
		JButton btnImprimer_1 = new JButton("Imprimer");
		btnImprimer_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Imprimer Client/produits
				MessageFormat entete = new MessageFormat("Commandes");
				MessageFormat pied =new MessageFormat("Page 1");
				try {
					table.print(JTable.PrintMode.FIT_WIDTH,entete,pied);
				}catch(Exception ee) {
					JOptionPane.showConfirmDialog(null, "erruer : \n "+ee,"Impresion",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnImprimer_1.setForeground(new Color(0, 0, 0));
		btnImprimer_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnImprimer_1.setBackground(new Color(255, 222, 173));
		btnImprimer_1.setBounds(984, 437, 191, 57);
		frame.getContentPane().add(btnImprimer_1);
		
		JButton btnAfficherCommande = new JButton("Afficher Commande");
		btnAfficherCommande.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				IGestionCommandes.afficherCommandes(table);
				
				
			}
		});
		btnAfficherCommande.setForeground(new Color(0, 0, 0));
		btnAfficherCommande.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnAfficherCommande.setBackground(new Color(255, 222, 173));
		btnAfficherCommande.setBounds(769, 437, 191, 57);
		frame.getContentPane().add(btnAfficherCommande);
		
		JTextPane txtpnHjkhkj = new JTextPane();
		txtpnHjkhkj.setBackground(new Color(255, 228, 181));
		txtpnHjkhkj.setText("Fonctionnalite de l app:\r\n- Commander plusieurs Commandes \r\n- afficher les commandes / Clienrt / Produits\r\n- Ajouter des produirs\r\n- Imprimer la fiche des Commandes/ Clients/Produits\r\n\r\nConsignes:\r\n- Pour Supprimer une Commande seulment inserer le CodeC dans la case du \"Code\"\r\n- Pour changer la qte d'une commande  (Update) , Il faut inserer le CodeC, le Cin de client avec le nom du produit et la nouvelle Qte.\r\n \r\nBon Usage !");
		txtpnHjkhkj.setBounds(35, 56, 191, 430);
		frame.getContentPane().add(txtpnHjkhkj);
	}
	
}