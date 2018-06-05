/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetnatationsynchronisee;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

/**
 *
 * @author attiac
 */
public class Nageuse {

    int id_personne;
    Connection connexion = null;
    ResultSet resultSet = null;
    ResourceBundle bundle = ResourceBundle.getBundle("natation.properties.config");
    String nomBdd = bundle.getString("bdd.name");
    String identifiant = bundle.getString("bdd.login");
    String pass = bundle.getString("bdd.password");

    String host = bundle.getString("bdd.hostname");
    String port = bundle.getString("bdd.port");

    public void Connection_Nageuse() {
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

    public Nageuse(int id_personne) throws SQLException {
        Connection_Nageuse();
        try (Statement statement = connexion.createStatement()) {

            statement.executeUpdate("INSERT INTO nageuse( id_personne) VALUES ('" + id_personne + "' ) ");
            statement.close();
        }
        connexion.close();
        this.id_personne = id_personne;
    }

    public int getId_personne() {
        return id_personne;
    }

    public void setId_personne(int id_personne) {
        this.id_personne = id_personne;
    }

}
