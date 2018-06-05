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
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author attiac
 */
public class Juge {

    int Rang;
    int estArbitre;
    int id_personne;

    Connection connexion = null;
    ResultSet resultSet = null;
    ResourceBundle bundle = ResourceBundle.getBundle("natation.properties.config");
    String nomBdd = bundle.getString("bdd.name");
    String identifiant = bundle.getString("bdd.login");
    String pass = bundle.getString("bdd.password");
    String host = bundle.getString("bdd.hostname");
    String port = bundle.getString("bdd.port");

    public void Connection_Juge() {
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

    public Juge() {
        Connection_Juge();
    }

    public DefaultTableModel buildTableModelJuge() throws SQLException {
        Connection_Juge();
        Statement statement = connexion.createStatement();
        ResultSet rs = statement.executeQuery("SELECT login, passwd, estarbitre, j.id_personne FROM user_natation n , juge j\n"
                + "WHERE n.id_personne = j.id_personne\n"
                + "\n"
                + "");

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

    public Juge(int Rang, int estArbitre, int id_personne) throws SQLException {
        Connection_Juge();
        this.Rang = Rang;
        this.estArbitre = estArbitre;
        this.id_personne = id_personne;
        try (Statement statement = connexion.createStatement()) {

            statement.executeUpdate("INSERT INTO juge(Rang, estArbitre, id_personne) VALUES ('" + Rang + "','" + estArbitre + "','" + id_personne + "')");
            statement.close();
        }
        connexion.close();
    }

    public int getRang(int id_personne) throws SQLException {
        Connection_Juge();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM juge WHERE id_personne = '" + id_personne + "'");
        while (result.next()) {
            return result.getInt("Rang");
        }
        statement.close();
        connexion.close();
        return 0;

    }

    public void setRang(int Rang, int id_personne) throws SQLException {
        Connection_Juge();
        this.Rang = Rang;
        PreparedStatement stmt = connexion.prepareStatement("UPDATE juge SET rang = '" + Rang + "' WHERE id_personne = '" + id_personne + "'");
        stmt.executeUpdate();
        connexion.close();
        stmt.close();

    }

    public boolean isEstArbitre(int id_personne) throws SQLException {
        Connection_Juge();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM juge WHERE id_personne = '" + id_personne + "'");
        while (result.next()) {
            return result.getBoolean("estArbitre");
        }
        statement.close();
        connexion.close();
        return false;

    }

    public void setEstArbitre(int estArbitre, int id_personne) throws SQLException {
        Connection_Juge();
        this.estArbitre = estArbitre;
        PreparedStatement stmt = connexion.prepareStatement("UPDATE juge SET estArbitre = '" + estArbitre + "' WHERE id_personne = '" + id_personne + "'");
        stmt.executeUpdate();
        connexion.close();
        stmt.close();

    }

    public int getId_personne() throws SQLException {

        return 0;

    }

    public void setId_personne(int id_personne) {
        this.id_personne = id_personne;
    }

}
