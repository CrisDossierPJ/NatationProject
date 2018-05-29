/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetnatationsynchronisee;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Christian
 */
public class Personne {

    int id_personne;
    String nom;
    String prenom;
    String dateNaissance;
    String dateCreation;

    Connection connexion = null;
    ResultSet resultSet = null;
    ResourceBundle bundle = ResourceBundle.getBundle("natation.properties.config");
    String nomBdd = bundle.getString("bdd.name");
    String identifiant = bundle.getString("bdd.login");
    String pass = bundle.getString("bdd.password");

    public void Connection_Personne() {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            connexion = DriverManager.getConnection("jdbc:mysql://localhost/" + nomBdd, identifiant, pass);

        } catch (ClassNotFoundException e) {
            System.out.println(e);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public DefaultTableModel buildTableModelPersonne() throws SQLException {
        Connection_Personne();
        Statement statement = connexion.createStatement();
        ResultSet rs = statement.executeQuery("select * from personne");

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);

    }

    public Personne() {

    }

    public Personne(String nom, String prenom, String dateNaissance, String dateCreation) throws SQLException {
        Connection_Personne();
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.dateCreation = dateCreation;
        try (Statement statement = connexion.createStatement()) {
            String sql = "INSERT INTO `personne`(`nom`, `prenom`, `dateCreation`, `dateDeNaissance`) VALUES ('" + nom + "','" + prenom + "','" + dateNaissance + "','" + dateCreation + "')";
           
            statement.executeUpdate(sql);
        }
        connexion.close();
    }

    public int getId_personne() {
        return id_personne;

    }

    public void setId_personne(int id_personne) {
        this.id_personne = id_personne;
    }

    public String getNom(int id_personne) throws SQLException {
        Connection_Personne();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM personne WHERE id_personne = '" + id_personne + "'");
        while (result.next()) {
            return result.getString("nom");
        }

        return "Id_personne non existant";
    }

    public void setNom(String nom, int id_personne) throws SQLException {

        Connection_Personne();
        this.nom = nom;

        PreparedStatement stmt = connexion.prepareStatement("UPDATE personne SET nom = '" + nom + "' WHERE id_personne = '" + id_personne + "'");
        stmt.executeUpdate();
        connexion.close();
    }

    public String getPrenom(int id_personne) throws SQLException {
        Connection_Personne();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM personne WHERE id_personne = '" + id_personne + "'");
        while (result.next()) {
            return result.getString("prenom");
        }

        return "Id_personne non existant";
    }

    public void setPrenom(String prenom, int id_personne) throws SQLException {
        Connection_Personne();
        this.prenom = prenom;
        PreparedStatement stmt = connexion.prepareStatement("UPDATE personne SET prenom = '" + prenom + "' WHERE id_personne = '" + id_personne + "'");
        stmt.executeUpdate();
        connexion.close();
    }

    public String getDateNaissance(int id_personne) throws SQLException {
        Connection_Personne();

        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM personne WHERE id_personne = '" + id_personne + "'");
        while (result.next()) {
            return result.getString("datedenaissance");
        }

        return "";

    }

    public void setDateNaissance(String dateNaissance, int id_personne) throws SQLException {
        Connection_Personne();
        this.dateNaissance = dateNaissance;
        PreparedStatement stmt = connexion.prepareStatement("UPDATE personne SET dateDeNaissance = '" + dateNaissance + "' WHERE id_personne = '" + id_personne + "'");
        stmt.executeUpdate();
        connexion.close();
    }

    public String getDateCreation(int id_personne) throws SQLException {
        Connection_Personne();

        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM personne WHERE id_personne = '" + id_personne + "'");
        while (result.next()) {
            return result.getString("datecreation");
        }

        return "";

    }

    public void setDateCreation(String dateCreation, int id_personne) throws SQLException {
        Connection_Personne();
        this.dateCreation = dateCreation;

        PreparedStatement stmt = connexion.prepareStatement("UPDATE personne SET dateCreation = '" + dateCreation + "' WHERE id_personne = '" + id_personne + "'");
        stmt.executeUpdate();
        connexion.close();
    }

    public void deletePersonne(int id_personne) throws SQLException {
        Connection_Personne();
        PreparedStatement stmt = connexion.prepareStatement("DELETE FROM personne WHERE id_personne = '" + id_personne + "'");
        stmt.executeUpdate();
        connexion.close();
    }

    public String[] getAllPersonne() throws SQLException {
        Connection_Personne();
        Statement statement = connexion.createStatement();
        int taille = 0;
        ResultSet count = statement.executeQuery("SELECT  count(*) nb FROM personne ");
        while (count.next()) {
            taille = Integer.parseInt(count.getString("nb"));
        }

        count.close();
        int i = 0;
        ResultSet result = statement.executeQuery("SELECT * FROM personne ");

        String tabNomPrenom[] = new String[taille];


        while (result.next()) {

            tabNomPrenom[i] = result.getString("id_personne") + "-" + result.getString("nom") + " " + result.getString("prenom");
            i++;

        }
        return tabNomPrenom;

    }

    public String[] getIdPersonne() throws SQLException {
        Connection_Personne();
        Statement statement = connexion.createStatement();

        int taille = 0;
        ResultSet count = statement.executeQuery("SELECT  count(*) nb FROM personne ");
        while (count.next()) {
            taille = Integer.parseInt(count.getString("nb"));
        }

        count.close();
        ResultSet result = statement.executeQuery("SELECT * FROM personne ");
        int i = 0;

        String tabID[] = new String[taille];

        while (result.next()) {

            tabID[i] = result.getString("id_personne");
            i++;

        }

        return tabID;

    }

}
