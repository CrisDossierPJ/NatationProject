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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;

/**
 *
 * @author Christian
 */
public class User {

    String login;
    String passwd;
    boolean estAdmin;
    boolean estCreateurCompet;
    int id_personne;

    Connection connexion = null;
    ResultSet resultSet = null;
    ResourceBundle bundle = ResourceBundle.getBundle("natation.properties.config");
    String nomBdd = bundle.getString("bdd.name");
    String identifiant = bundle.getString("bdd.login");
    String pass = bundle.getString("bdd.password");

    public void Connection_User() {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            System.out.println("Le pilote JDBC MySQL a été chargé");
            connexion = DriverManager.getConnection("jdbc:mysql://localhost/" + nomBdd, identifiant, pass);

        } catch (ClassNotFoundException e) {
            System.out.println(e);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     *
     */
    public User() {
        Connection_User();
    }

    public User(String login, String passwd, boolean estAdmin, boolean estCreateurCompet, int id_personne) throws SQLException {
        Connection_User();
        this.login = login;
        this.passwd = passwd;
        this.estAdmin = estAdmin;
        this.estCreateurCompet = estCreateurCompet;
        this.id_personne = id_personne;

        try (Statement statement = connexion.createStatement()) {
            statement.executeUpdate("INSERT INTO `user`(`login`, `passwd`, `estAdmin`, `estCreateurCompet`, `id_personne`) VALUES (" + login + "," + passwd + "," + estAdmin + "," + estCreateurCompet + "," + id_personne + " ) ");
        }
        connexion.close();
    }

    public boolean Authentification(String User, String pass) throws SQLException {
        Connection_User();
        Statement statement = connexion.createStatement();

        ResultSet result = statement.executeQuery("SELECT * FROM user WHERE login = '" + User + "' AND passwd = '" + pass + "'");
        if (result.next() == false) {
            JOptionPane.showMessageDialog(null, "Oups ! Login/Mdp incorrect ! Recommencez");
            statement.close();
            connexion.close();
            return false;

        } else {

            statement.close();
            connexion.close();
            return true;
        }
    }

    public String getLogin(int id_personne) throws SQLException {
        Connection_User();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM user WHERE id_personne = " + id_personne + "'");
        while (result.next()) {
            return result.getString("login");
        }

        return "Id_personne non existant";
    }

    public void setLogin(String login) throws SQLException {
        Connection_User();
        Connection_User();
        this.login = login;
        PreparedStatement stmt = connexion.prepareStatement("UPDATE user SET login = '" + login + "'");
        resultSet = stmt.executeQuery();
        connexion.close();

    }

    public String getPasswd() {
        return passwd;

    }

    public void setPasswd(String passwd) throws SQLException {
        Connection_User();
        this.passwd = passwd;
        PreparedStatement stmt = connexion.prepareStatement("UPDATE user SET login = '" + passwd + "'");
        resultSet = stmt.executeQuery();
        connexion.close();
    }

    public boolean isEstAdmin(String user_login) throws SQLException {
        Connection_User();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM user WHERE login = '" + user_login + "'");
        while (result.next()) {
            return result.getBoolean("estAdmin");
        }

        return false;

    }

    public void setEstAdmin(boolean estAdmin) throws SQLException {
        Connection_User();
        this.estAdmin = estAdmin;
        PreparedStatement stmt = connexion.prepareStatement("UPDATE user SET login = '" + estAdmin + "'");
        resultSet = stmt.executeQuery();
        connexion.close();
    }

    public boolean isEstCreateurCompet(String user_login) throws SQLException {
        Connection_User();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM user WHERE id_personne = '" + user_login + "'");
        while (result.next()) {
            return result.getBoolean("estCreateurCompet");
        }

        return false;
    }

    public void setEstCreateurCompet(boolean estCreateurCompet) throws SQLException {
        Connection_User();
        this.estCreateurCompet = estCreateurCompet;
        PreparedStatement stmt = connexion.prepareStatement("UPDATE user SET login = '" + estCreateurCompet + "'");
        resultSet = stmt.executeQuery();
        connexion.close();
    }

    public int getId_personne(String user_login) throws SQLException {
        Connection_User();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM user WHERE id_personne = '" + user_login + "'");
        while (result.next()) {
            return result.getInt("estCreateurCompet");
        }
        return 0;
    }

    public void setId_personne(int id_personne) {
        this.id_personne = id_personne;
    }

}
