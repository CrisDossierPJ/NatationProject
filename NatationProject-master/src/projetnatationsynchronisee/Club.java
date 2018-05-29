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
public class Club {

    int id_club;
    String nom_club;
    int id_personne;

    Connection connexion = null;
    ResultSet resultSet = null;
    ResourceBundle bundle = ResourceBundle.getBundle("natation.properties.config");
    String nomBdd = bundle.getString("bdd.name");
    String identifiant = bundle.getString("bdd.login");
    String pass = bundle.getString("bdd.password");

    public void Connection_Club() {
        try {
            Class.forName("com.mysql.jdbc.Driver");

    
            
            connexion = DriverManager.getConnection("jdbc:mysql://localhost/" + nomBdd, identifiant, pass);

        } catch (ClassNotFoundException e) {
            System.out.println(e);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public Club() {
        Connection_Club();
    }

    public Club(int id_club, String nom_club, int id_personne) {
        Connection_Club();
        this.id_club = id_club;
        this.nom_club = nom_club;
        this.id_personne = id_personne;
    }

    public int getId_club(int id_personne) throws SQLException {
        Connection_Club();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM club WHERE id_personne = " + id_personne + "'");
        while (result.next()) {
            return result.getInt("id_club");
        }

        return 0;

    }

    public void setId_club(int id_club) {
        this.id_club = id_club;
    }

    public String getNom_club(int id_personne) throws SQLException {
        Connection_Club();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM club WHERE id_personne = " + id_personne + "'");
        while (result.next()) {
            return result.getString("nom_club");
        }

        return "";
    }

    public void setNom_club(String nom_club) {

        this.nom_club = nom_club;
    }

    public int getId_personne(String nom_club) throws SQLException {
        Connection_Club();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM club WHERE id_personne = " + nom_club + "'");
        while (result.next()) {
            return result.getInt("nom_club");
        }
        return id_personne;
    }

    public void setId_personne(int id_personne) {
        this.id_personne = id_personne;
    }

}
