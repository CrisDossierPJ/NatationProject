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
public class Competition {

    int id_compet;
    String Titre;
    String Date_Compet;
    String Lieu_Compet;

    Connection connexion = null;
    ResultSet resultSet = null;
    ResourceBundle bundle = ResourceBundle.getBundle("natation.properties.config");
    String nomBdd = bundle.getString("bdd.name");
    String identifiant = bundle.getString("bdd.login");
    String pass = bundle.getString("bdd.password");
     String host = bundle.getString("bdd.hostname");
    String port = bundle.getString("bdd.port");
    
    public DefaultTableModel buildTableModelCompetition() throws SQLException {
        Connection_Competition();
        Statement statement = connexion.createStatement();
        ResultSet rs = statement.executeQuery("select * from competition");

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

    public void Connection_Competition() {
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

    public Competition() throws SQLException {
        Connection_Competition();
        connexion.close();
    }

    public Competition(String Titre, String Date_Compet, String Lieu_Compet) throws SQLException {
Connection_Competition();
        this.Titre = Titre;
        this.Date_Compet = Date_Compet;
        this.Lieu_Compet = Lieu_Compet;

        try (Statement statement = connexion.createStatement()) {

            statement.executeUpdate("INSERT INTO competition(Titre, Date_Compet, Lieu_Compet) VALUES ('" + Titre + "','"
                    + "" + Date_Compet + "','" + Lieu_Compet + "')");
        }
        connexion.close();
    }

    public String getTitre(int id_compet) throws SQLException {
        Connection_Competition();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM competition WHERE id_compet = '" + id_compet + "'");
        while (result.next()) {
            return result.getString("Titre");
        }

        return "Id_personne non existant";
    }

    public void setTitre(String Titre, int id_compet) throws SQLException {

        Connection_Competition();
        this.Titre = Titre;
        PreparedStatement stmt = connexion.prepareStatement("UPDATE competition SET Titre = '" + Titre + "' WHERE id_compet = '" + id_compet + "'");
         stmt.executeUpdate();
        connexion.close();

    }

    public String getDate_Compet(int id_compet) throws SQLException {
        Connection_Competition();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM competition WHERE id_compet = '" + id_compet + "'");
        while (result.next()) {
            return result.getString("Date_Compet");
        }
        return "";
    }

    public void setDate_Compet(String Date_Compet, int id_compet) throws SQLException {

        Connection_Competition();
        this.Date_Compet = Date_Compet;
        PreparedStatement stmt = connexion.prepareStatement("UPDATE competition SET Date_Compet = '" + Date_Compet + "' WHERE id_compet = '" + id_compet + "'");
         stmt.executeUpdate();
        connexion.close();

    }

    public String getLieu_Compet(int id_compet) throws SQLException {
        Connection_Competition();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM competition WHERE id_compet = '" + id_compet + "'");
        while (result.next()) {
            return result.getString("Lieu_Compet");
        }
        return "";
    }

    public void setLieu_Compet(String Lieu_Compet, int id_compet) throws SQLException {

        Connection_Competition();
        this.Lieu_Compet = Lieu_Compet;
        PreparedStatement stmt = connexion.prepareStatement("UPDATE competition SET Lieu_Compet = '" + Lieu_Compet + "' WHERE id_compet = '" + id_compet + "'");
         stmt.executeUpdate();
        connexion.close();

    }
    public void deleteCompet(int id_compet)throws SQLException {
        Connection_Competition();

        PreparedStatement stmt = connexion.prepareStatement("DELETE FROM competition WHERE id_compet = '" + id_compet + "'");
         stmt.executeUpdate();
        connexion.close();
    }
    
     public String[] getAllCompet() throws SQLException {
        Connection_Competition();
        Statement statement = connexion.createStatement();
        int taille = 0;
        ResultSet count = statement.executeQuery("SELECT  count(*) nb FROM competition ");
        while (count.next()) {
            taille = Integer.parseInt(count.getString("nb"));
        }

        count.close();
        int i = 0;
        ResultSet result = statement.executeQuery("SELECT * FROM competition ");

        String tabNomPrenom[] = new String[taille];

        while (result.next()) {

            tabNomPrenom[i] = result.getString("id_compet") + "-" + result.getString("Titre") ;
            i++;

        }
        statement.close();
        connexion.close();
        return tabNomPrenom;

    }

    public String[] getIdCompet() throws SQLException {
        Connection_Competition();
        Statement statement = connexion.createStatement();

        int taille = 0;
        ResultSet count = statement.executeQuery("SELECT  count(*) nb FROM competition ");
        while (count.next()) {
            taille = Integer.parseInt(count.getString("nb"));
        }

        count.close();
        ResultSet result = statement.executeQuery("SELECT * FROM competition ");
        int i = 0;

        String tabID[] = new String[taille];

        while (result.next()) {

            tabID[i] = result.getString("id_compet");
            i++;

        }
        statement.close();
        connexion.close();
        return tabID;

    }


}
