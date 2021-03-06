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
public class Compose {

    int id_equipe;
    int id_personne;

    Connection connexion = null;
    ResultSet resultSet = null;
    ResourceBundle bundle = ResourceBundle.getBundle("natation.properties.config");
    String nomBdd = bundle.getString("bdd.name");
    String identifiant = bundle.getString("bdd.login");
    String pass = bundle.getString("bdd.password");
    String host = bundle.getString("bdd.hostname");
    String port = bundle.getString("bdd.port");

    public void Connection_Compose() throws SQLException {
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

    public Compose() {
    }

    public DefaultTableModel buildTableModelCompose() throws SQLException {
        Connection_Compose();
        Statement statement = connexion.createStatement();
        ResultSet rs = statement.executeQuery("select * from compose");

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

    public Compose(int id_equipe, int id_personne) throws SQLException {
        Connection_Compose();
        this.id_equipe = id_equipe;
        this.id_personne = id_personne;
        this.id_personne = id_personne;
        try (Statement statement = connexion.createStatement()) {
            String sql = "INSERT INTO compose(id_equipe, id_personne) VALUES ('" + id_equipe + "','" + id_personne + "')";

            statement.executeUpdate(sql);
            statement.close();
        }
        connexion.close();
    }

    public int getId_equipe(int id_personne) throws SQLException {
        Connection_Compose();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM compose WHERE id_personne = '" + id_personne + "'");
        int id_equipe_Return = 0;
        while (result.next()) {
            id_equipe_Return = result.getInt("id_equipe");
        }
        statement.close();
        connexion.close();
        return id_equipe_Return;
    }

    public void setId_equipe(int id_equipe, int id_personne) throws SQLException {
        Connection_Compose();
        //  this.prenom = prenom;
        PreparedStatement stmt = connexion.prepareStatement("UPDATE compose SET id_equipe = '" + id_equipe + "' WHERE id_personne = '" + id_personne + "'");
        stmt.executeUpdate();
        stmt.close();
        connexion.close();
    }

    public int getId_personne(int id_personne) throws SQLException {
        Connection_Compose();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM compose WHERE id_personne = '" + id_personne + "'");
        int id_Personne_Return = 0;
        while (result.next()) {
            id_Personne_Return = result.getInt("id_equipe");
        }
        statement.close();
        connexion.close();
        return id_Personne_Return;
    }

    public void setId_personne(int id_personne) {
        this.id_personne = id_personne;
    }

    public void deleteCompose(int id_personne) throws SQLException {
        Connection_Compose();
        PreparedStatement stmt = connexion.prepareStatement("DELETE FROM compose WHERE id_personne = '" + id_personne + "'");
        stmt.executeUpdate();
        stmt.close();
        connexion.close();
    }

}
