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
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Christian
 */
public class User {

    String login;
    String passwd;
    int estAdmin;
    int estCreateurCompet;
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

            connexion = DriverManager.getConnection("jdbc:mysql://localhost/" + nomBdd, identifiant, pass);

        } catch (ClassNotFoundException e) {
            System.out.println(e);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public DefaultTableModel buildTableModelUser() throws SQLException {
        Statement statement = connexion.createStatement();
        ResultSet rs = statement.executeQuery("select * from user");

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

    /**
     *
     */
    public User() {
        Connection_User();
    }

    public User(String login, String passwd, int estAdmin, int estCreateurCompet, int id_personne) throws SQLException {
        Connection_User();
        this.login = login;
        this.passwd = passwd;
        this.estAdmin = estAdmin;
        this.estCreateurCompet = estCreateurCompet;
        this.id_personne = id_personne;

        try (Statement statement = connexion.createStatement()) {
            System.out.println("INSERT INTO `user`(`login`, `passwd`, `estAdmin`, `estCreateurCompet`, `id_personne`) VALUES ('" + login + "','" + passwd + "','" + estAdmin + "','" + estCreateurCompet + "','" + id_personne + "' ) ");
            statement.executeUpdate("INSERT INTO `user`(`login`, `passwd`, `estAdmin`, `estCreateurCompet`, `id_personne`) VALUES ('" + login + "','" + passwd + "','" + estAdmin + "','" + estCreateurCompet + "','" + id_personne + "' ) ");
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
        ResultSet result = statement.executeQuery("SELECT * FROM user WHERE id_personne = '" + id_personne + "'");
        while (result.next()) {
            return result.getString("login");
        }

        return "Id_personne non existant";
    }

    public void setLogin(String login, int id_personne) throws SQLException {
        Connection_User();
        Connection_User();
        this.login = login;
        PreparedStatement stmt = connexion.prepareStatement("UPDATE user SET login = '" + login + "' WHERE id_personne = '"+id_personne + "'");
        resultSet = stmt.executeQuery();
        connexion.close();

    }

    public String getPasswd() {
        return passwd;

    }

    public void setPasswd(String passwd, int id_personne) throws SQLException {
        Connection_User();
        this.passwd = passwd;
        PreparedStatement stmt = connexion.prepareStatement("UPDATE user SET passwd = '" + passwd + "' WHERE id_personne = '" + id_personne + "'");
        resultSet = stmt.executeQuery();
        connexion.close();
    }

    public boolean isEstAdmin(int id_personne) throws SQLException {
        Connection_User();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM user WHERE id_personne = '" + id_personne + "'");
        while (result.next()) {
            return result.getBoolean("estAdmin");
        }

        return false;

    }
    public boolean isEstAdminAuth(String user_login) throws SQLException {
        Connection_User();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM user WHERE login = '" + user_login + "'");
        while (result.next()) {
            return result.getBoolean("estAdmin");
        }

        return false;

    }

    public void setEstAdmin(int estAdmin,  int id_personne) throws SQLException {
        Connection_User();
        this.estAdmin = estAdmin;
        PreparedStatement stmt = connexion.prepareStatement("UPDATE user SET estAdmin = '" + estAdmin + "' WHERE id_personne = '" + id_personne + "'");
        resultSet = stmt.executeQuery();
        connexion.close();
    }

    public boolean isEstCreateurCompet(int id_personne) throws SQLException {
        Connection_User();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM user WHERE id_personne = '" + id_personne + "'");
        while (result.next()) {
            return result.getBoolean("estCreateurCompet");
        }

        return false;
    }

    public void setEstCreateurCompet(int estCreateurCompet, int id_personne) throws SQLException {
        Connection_User();
        this.estCreateurCompet = estCreateurCompet;
        PreparedStatement stmt = connexion.prepareStatement("UPDATE user SET estCreateurCompet = '" + estCreateurCompet + "' WHERE id_personne = '" + id_personne + "'");
        resultSet = stmt.executeQuery();
        connexion.close();
    }

    public void setId_personne(int id_personne) {
        this.id_personne = id_personne;
    }

}
