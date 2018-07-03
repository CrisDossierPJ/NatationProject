/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NatationSynchronisee;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author attiac
 */
public class Club {

    int id_club;
    String nom_club;
    int id_personne;
    String dateCreation;

    Connection connexion = null;
    ResultSet resultSet = null;
    ResourceBundle bundle = ResourceBundle.getBundle("natation.properties.config");
    String nomBdd = bundle.getString("bdd.name");
    String identifiant = bundle.getString("bdd.login");
    String pass = bundle.getString("bdd.password");
    String host = bundle.getString("bdd.hostname");
    String port = bundle.getString("bdd.port");

    public void Connection_Club() {
        try {
           // Class.forName("com.mysql.jdbc.Driver");
            Class.forName("org.postgresql.Driver");

            connexion = DriverManager.getConnection("jdbc:postgresql://" + host + ":" + port + "/" + nomBdd, identifiant, pass);

        } catch (ClassNotFoundException e) {
            System.out.println(e);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public DefaultTableModel buildTableModelClub() throws SQLException {
        Connection_Club();
        Statement statement = connexion.createStatement();
        ResultSet rs = statement.executeQuery("select * from club");

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
        statement.close();
        return new DefaultTableModel(data, columnNames);

    }

    public Club() throws SQLException {
        Connection_Club();

    }

    public Club(String nom_club, int id_personne, String dateCreation) throws SQLException {
        Connection_Club();
        this.nom_club = nom_club;
        this.id_personne = id_personne;
        this.dateCreation = dateCreation;
        try (Statement statement = connexion.createStatement()) {

            statement.executeUpdate("INSERT INTO club(nom_club, id_personne, dateCreation) VALUES ('" + nom_club + "','" + id_personne + "','" + dateCreation + "')");
        }
        connexion.close();
    }

    public String getDate_Creation_Club(int id_club) throws SQLException {
        Connection_Club();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM club WHERE id_club = '" + id_club + "'");
        while (result.next()) {
            return result.getString("dateCreation");
        }
        connexion.close();
        return "";
    }

    public void setDate_Creation_Club(String dateCreation, int id_club) throws SQLException {
        Connection_Club();

        this.dateCreation = dateCreation;
        PreparedStatement stmt = connexion.prepareStatement("UPDATE club SET dateCreation = '" + dateCreation + "' WHERE id_club = '" + id_club + "'");
        stmt.executeUpdate();
        connexion.close();

    }

    public String getNom_club(int id_club) throws SQLException {
        Connection_Club();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM club WHERE id_club = '" + id_club + "'");
        while (result.next()) {
            return result.getString("nom_club");
        }
        connexion.close();
        return "";
    }

    public void setNom_club(String nom_club, int id_club) throws SQLException {
        Connection_Club();

        this.nom_club = nom_club;
        PreparedStatement stmt = connexion.prepareStatement("UPDATE club SET nom_club = '" + nom_club + "' WHERE id_club = '" + id_club + "'");
        stmt.executeUpdate();
        connexion.close();

    }

    public int getId_personne(int id_club) throws SQLException {
        Connection_Club();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM club WHERE id_club = '" + id_club + "'");
        while (result.next()) {
            return result.getInt("id_personne");
        }
        return 0;
    }

    public void setId_personne(int id_personne, int id_club) throws SQLException {
        Connection_Club();
        this.id_personne = id_personne;
        PreparedStatement stmt = connexion.prepareStatement("UPDATE club SET id_personne = '" + id_personne + "' WHERE id_club = '" + id_club + "'");
        stmt.executeUpdate();
        connexion.close();

    }

    public void deleteClub(int id_club) throws SQLException {
        Connection_Club();
        PreparedStatement stmt = connexion.prepareStatement("DELETE FROM club WHERE id_club = '" + id_club + "'");
        stmt.executeUpdate();
        stmt.close();
        connexion.close();
    }

    public String[] getAllClub() throws SQLException {
        Connection_Club();
        Statement statement = connexion.createStatement();
        int taille = 0;
        ResultSet count = statement.executeQuery("SELECT  count(*) nb FROM club ");
        while (count.next()) {
            taille = Integer.parseInt(count.getString("nb"));
        }

        count.close();
        int i = 0;
        ResultSet result = statement.executeQuery("SELECT * FROM club ");

        String tabNomPrenom[] = new String[taille];

        while (result.next()) {

            tabNomPrenom[i] = result.getString("id_club") + "-" + result.getString("nom_club");
            i++;

        }
        statement.close();
        connexion.close();
        return tabNomPrenom;

    }

    public String[] getIdClub() throws SQLException {
        Connection_Club();
        Statement statement = connexion.createStatement();

        int taille = 0;
        ResultSet count = statement.executeQuery("SELECT  count(*) nb FROM club ");
        while (count.next()) {
            taille = Integer.parseInt(count.getString("nb"));
        }

        count.close();
        ResultSet result = statement.executeQuery("SELECT * FROM club ");
        int i = 0;

        String tabID[] = new String[taille];

        while (result.next()) {

            tabID[i] = result.getString("id_club");
            i++;

        }
        statement.close();
        connexion.close();
        return tabID;

    }
        public String[] getAllDirigeant() throws SQLException {
        Connection_Club();
        Statement statement = connexion.createStatement();
        int taille = 0;
        ResultSet count = statement.executeQuery("SELECT count(distinct id_personne) nb FROM club  ");
        while (count.next()) {
            taille = Integer.parseInt(count.getString("nb"));
        }

        count.close();
        int i = 0;
        ResultSet result = statement.executeQuery("SELECT distinct club.id_personne, nom, prenom FROM club JOIN personne on personne.id_personne = club.id_personne ");

        String tabNomPrenom[] = new String[taille];

        while (result.next()) {

            tabNomPrenom[i] = result.getString("id_personne") + "-" + result.getString("nom") + " " + result.getString("prenom");
            i++;

        }
        statement.close();
        connexion.close();
        return tabNomPrenom;

    }

    public String[] getIdDirigeant() throws SQLException {
        Connection_Club();
        Statement statement = connexion.createStatement();

        int taille = 0;
        ResultSet count = statement.executeQuery("SELECT count(distinct id_personne) nb FROM club  ");
        while (count.next()) {
            taille = Integer.parseInt(count.getString("nb"));
        }

        count.close();
        ResultSet result = statement.executeQuery("SELECT distinct club.id_personne, nom, prenom FROM club JOIN personne on personne.id_personne = club.id_personne ");
        int i = 0;

        String tabID[] = new String[taille];

        while (result.next()) {

            tabID[i] = result.getString("id_personne");
            i++;

        }
        statement.close();
        connexion.close();
        return tabID;

    }

}
