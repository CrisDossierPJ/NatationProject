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
    String host = bundle.getString("bdd.hostname");
    String port = bundle.getString("bdd.port");

    public void Connection_User() {
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

    public DefaultTableModel buildTableModelUser() throws SQLException {
        Connection_User();
        Statement statement = connexion.createStatement();
        ResultSet rs = statement.executeQuery("SELECT n.*, j.rang, j.estarbitre FROM user_natation n \n"
                + "left JOIN juge j on j.id_personne = n.id_personne;\n");

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
        connexion.close();
        return new DefaultTableModel(data, columnNames);

    }

    /**
     *
     */
    public User() {

    }

    public User(String login, String passwd, int estAdmin, int estCreateurCompet, int id_personne) throws SQLException {
        Connection_User();
        this.login = login;
        this.passwd = passwd;
        this.estAdmin = estAdmin;
        this.estCreateurCompet = estCreateurCompet;
        this.id_personne = id_personne;

        try (Statement statement = connexion.createStatement()) {
            System.out.println("INSERT INTO user_natation(login, passwd, estAdmin, estCreateurCompet, id_personne) VALUES ('" + login + "','" + passwd + "','" + estAdmin + "','" + estCreateurCompet + "','" + id_personne + "' ) ");
            statement.executeUpdate("INSERT INTO user_natation(login, passwd, estAdmin, estCreateurCompet, id_personne) VALUES ('" + login + "','" + passwd + "','" + estAdmin + "','" + estCreateurCompet + "','" + id_personne + "' ) ");
            statement.close();
        }
        connexion.close();

    }

    public boolean Authentification(String User, String pass) throws SQLException {
        Connection_User();
        Statement statement = connexion.createStatement();

        ResultSet result = statement.executeQuery("SELECT * FROM user_natation WHERE login = '" + User + "' AND passwd = crypt('" + pass + "',passwd)");
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
        ResultSet result = statement.executeQuery("SELECT * FROM user_natation WHERE id_personne = '" + id_personne + "'");
        String login_Return = "";
        while (result.next()) {
            login_Return = result.getString("login");
        }
        statement.close();
        connexion.close();
        return login_Return;
    }

    public void setLogin(String login, int id_personne) throws SQLException {

        Connection_User();
        this.login = login;
        PreparedStatement stmt = connexion.prepareStatement("UPDATE user_natation SET login = '" + login + "' WHERE id_personne = '" + id_personne + "'");
        stmt.executeUpdate();
        connexion.close();
        stmt.close();

    }

    public String getPasswd() {
        return passwd;

    }

    public void setPasswd(String passwd, int id_personne) throws SQLException {
        Connection_User();
        //this.passwd = passwd;
        PreparedStatement stmt = connexion.prepareStatement("UPDATE user_natation SET passwd = '" + passwd + "' WHERE id_personne = '" + id_personne + "'");
        System.out.println(stmt);
        stmt.executeUpdate();
        connexion.close();
        stmt.close();
    }

    public boolean isEstAdmin(int id_personne) throws SQLException {
        Connection_User();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM user_natation WHERE id_personne = '" + id_personne + "'");
        boolean est_Admin_Return = false;
        while (result.next()) {
            est_Admin_Return = result.getBoolean("estAdmin");
        }
        statement.close();
        connexion.close();
        return est_Admin_Return;

    }

    public boolean isEstAdminAuth(String user_natation_login) throws SQLException {
        Connection_User();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM user_natation WHERE login = '" + user_natation_login + "'");
        boolean est_Admin_Return = false;
        while (result.next()) {
            est_Admin_Return = result.getBoolean("estAdmin");
        }
        statement.close();
        connexion.close();
        return est_Admin_Return;

    }

    public boolean isCreateurCompetAuth(String user_natation_login) throws SQLException {
        Connection_User();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM user_natation WHERE login = '" + user_natation_login + "'");
        boolean createur_compet_Return = false;
        while (result.next()) {
            id_personne = result.getInt("id_personne");
            createur_compet_Return = result.getBoolean("estcreateurCompet");
        }
        statement.close();
        connexion.close();
        return createur_compet_Return;

    }

    public boolean isJugeArbAuth(String user_natation_login) throws SQLException {
        Connection_User();
        boolean vraie = false;
        int id_personne = 0;
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM user_natation WHERE login = '" + user_natation_login + "'");
        boolean juge_Arb_Return = false;
        while (result.next()) {
            if (result.getBoolean("estcreateurCompet") == false && result.getBoolean("estadmin") == false) {
                id_personne = result.getInt("id_personne");
                vraie = true;
                break;
            }

        }
        if (vraie == true) {
            ResultSet resultJuge = statement.executeQuery("SELECT * FROM juge WHERE id_personne = '" + id_personne + "'");
            while (resultJuge.next()) {
                juge_Arb_Return = resultJuge.getBoolean("estArbitre");
            }
        }

        statement.close();
        connexion.close();
        return juge_Arb_Return;

    }

    public boolean isJugeArb(int id_personne) throws SQLException {
        Connection_User();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM juge WHERE id_personne = '" + id_personne + "'");

        boolean juge_Arb_Return = false;
        while (result.next()) {

            juge_Arb_Return = result.getBoolean("estarbitre");

        }
        statement.close();
        connexion.close();
        return juge_Arb_Return;

    }

    public void setEstAdmin(int estAdmin, int id_personne) throws SQLException {
        Connection_User();
        this.estAdmin = estAdmin;
        PreparedStatement stmt = connexion.prepareStatement("UPDATE user_natation SET estAdmin = '" + estAdmin + "' WHERE id_personne = '" + id_personne + "'");
        stmt.executeUpdate();
        stmt.close();
        connexion.close();
    }

    public boolean isEstCreateurCompet(int id_personne) throws SQLException {
        Connection_User();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM user_natation WHERE id_personne = '" + id_personne + "'");
        boolean createur_Compet_Return = false;
        while (result.next()) {
            createur_Compet_Return = result.getBoolean("estCreateurCompet");
        }
        statement.close();
        connexion.close();
        return createur_Compet_Return;
    }

    public void setEstCreateurCompet(int estCreateurCompet, int id_personne) throws SQLException {
        Connection_User();
        this.estCreateurCompet = estCreateurCompet;
        PreparedStatement stmt = connexion.prepareStatement("UPDATE user_natation SET estCreateurCompet = '" + estCreateurCompet + "' WHERE id_personne = '" + id_personne + "'");
        stmt.executeUpdate();
        connexion.close();
        stmt.close();
    }

    public void setId_personne(int id_personne) {
        this.id_personne = id_personne;
    }

    public void deleteUser(int id_personne) throws SQLException {
        Connection_User();
        PreparedStatement stmt = connexion.prepareStatement("DELETE FROM user_natation WHERE id_personne = '" + id_personne + "'");
        stmt.executeUpdate();
        stmt.close();
        connexion.close();
    }

    public int getIdEquipeEncours() throws SQLException {
        Connection_User();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("select * from equipe Where visible = true");
        int id_Equipe_Return = 0;
        while (result.next()) {
            id_Equipe_Return = result.getInt("id_equipe");
        }
        statement.close();
        connexion.close();
        return id_Equipe_Return;
    }
}
