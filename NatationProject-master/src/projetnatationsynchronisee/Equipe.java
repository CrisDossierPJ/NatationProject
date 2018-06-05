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
public class Equipe {

    int id_equipe;
    String nom_equipe;
    int num_passage;
    int penalite;
    int Visible;
    String DateCreation;
    int id_club;
    int id_compet;

    Connection connexion = null;
    ResultSet resultSet = null;
    ResourceBundle bundle = ResourceBundle.getBundle("natation.properties.config");
    String nomBdd = bundle.getString("bdd.name");
    String identifiant = bundle.getString("bdd.login");
    String pass = bundle.getString("bdd.password");
    String host = bundle.getString("bdd.hostname");
    String port = bundle.getString("bdd.port");

    public void Connection_Equipe() {
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

    public DefaultTableModel buildTableModelEquipe() throws SQLException {
        Connection_Equipe();
        Statement statement = connexion.createStatement();
        ResultSet rs = statement.executeQuery("select * from equipe");

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

    public Equipe() throws SQLException {
        Connection_Equipe();
        connexion.close();
    }

    public Equipe(String nom_equipe, int num_passage, int penalite, int Visible, String DateCreation, int id_club, int id_compet) throws SQLException {
        Connection_Equipe();

        this.nom_equipe = nom_equipe;
        this.num_passage = num_passage;
        this.penalite = penalite;
        this.Visible = Visible;
        this.DateCreation = DateCreation;
        this.id_club = id_club;
        this.id_compet = id_compet;

        try (Statement statement = connexion.createStatement()) {
            System.out.println("INSERT INTO equipe( nom_equipe, num_passage, penalite, visible, DateCreation, id_club, id_compet) VALUES ('"
                    + nom_equipe + "','" + num_passage + "','" + penalite + "','" + Visible + "','" + DateCreation + "','" + id_club + "','" + id_compet + "')");
            statement.executeUpdate("INSERT INTO equipe( nom_equipe, num_passage, penalite, visible, DateCreation, id_club, id_compet) VALUES ('"
                    + nom_equipe + "','" + num_passage + "','" + penalite + "','" + Visible + "','" + DateCreation + "','" + id_club + "','" + id_compet + "')");

        }
        connexion.close();
    }

    public int getId_equipe() {
        return id_equipe;
    }

    public void setId_equipe(int id_equipe) {

        this.id_equipe = id_equipe;
    }

    public String getNom_equipe(int id_equipe) throws SQLException {
        Connection_Equipe();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM equipe WHERE id_equipe = '" + id_equipe + "'");
        while (result.next()) {
            return result.getString("nom_equipe");
        }

        return "";
    }

    public void setNom_equipe(String nom_equipe, int id_equipe) throws SQLException {

        Connection_Equipe();
        this.nom_equipe = nom_equipe;
        PreparedStatement stmt = connexion.prepareStatement("UPDATE equipe SET nom_equipe = '" + nom_equipe + "' WHERE id_equipe = '" + id_equipe + "'");
        stmt.executeUpdate();
        connexion.close();

    }

    public int getNum_passage(int id_equipe) throws SQLException {
        Connection_Equipe();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM equipe WHERE id_equipe = '" + id_equipe + "'");
        while (result.next()) {
            return result.getInt("num_passage");
        }

        return 0;
    }

    public void setNum_passage(int num_passage, int id_equipe) throws SQLException {

        Connection_Equipe();
        this.num_passage = num_passage;
        PreparedStatement stmt = connexion.prepareStatement("UPDATE equipe SET num_passage = '" + num_passage + "' WHERE id_equipe = '" + id_equipe + "'");
        stmt.executeUpdate();
        connexion.close();

    }

    public int getPenalite(int id_equipe) throws SQLException {
        Connection_Equipe();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM equipe WHERE id_equipe = '" + id_equipe + "'");
        while (result.next()) {
            return result.getInt("penalite");
        }

        return 0;
    }

    public void setPenalite(int penalite, int id_equipe) throws SQLException {

        Connection_Equipe();
        this.penalite = penalite;
        PreparedStatement stmt = connexion.prepareStatement("UPDATE equipe SET penalite = '" + penalite + "' WHERE id_equipe = '" + id_equipe + "'");
        stmt.executeUpdate();
        connexion.close();

    }

    public boolean isVisible(int id_equipe) throws SQLException {
        Connection_Equipe();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM equipe WHERE id_equipe = '" + id_equipe + "'");
        while (result.next()) {
            return result.getBoolean("visible");
        }

        return false;
    }

    public void setVisible(int Visible, int id_equipe) throws SQLException {

        Connection_Equipe();
        this.Visible = Visible;
        PreparedStatement stmt = connexion.prepareStatement("UPDATE equipe SET Visible = '" + Visible + "' WHERE id_equipe = '" + id_equipe + "'");
        stmt.executeUpdate();
        connexion.close();

    }

    public String getDateCreation(int id_equipe) throws SQLException {
        Connection_Equipe();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM equipe WHERE id_equipe = '" + id_equipe + "'");
        while (result.next()) {
            return result.getString("DateCreation");
        }

        return "";
    }

    public void setDateCreation(String DateCreation, int id_equipe) throws SQLException {

        Connection_Equipe();
        this.DateCreation = DateCreation;
        PreparedStatement stmt = connexion.prepareStatement("UPDATE equipe SET Visible = '" + Visible + "' WHERE id_equipe = '" + id_equipe + "'");
        stmt.executeUpdate();
        connexion.close();

    }

    public int getId_club(int id_equipe) throws SQLException {
        Connection_Equipe();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM equipe WHERE id_equipe = '" + id_equipe + "'");
        while (result.next()) {
            return result.getInt("id_club");
        }

        return 0;
    }

    public void setId_club(int id_club, int id_equipe) throws SQLException {

        Connection_Equipe();
        this.id_club = id_club;
        PreparedStatement stmt = connexion.prepareStatement("UPDATE equipe SET id_club = '" + id_club + "' WHERE id_equipe = '" + id_equipe + "'");
        stmt.executeUpdate();
        connexion.close();

    }

    public int getId_compet(int id_equipe) throws SQLException {
        Connection_Equipe();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM equipe WHERE id_equipe = '" + id_equipe + "'");
        while (result.next()) {
            return result.getInt("id_compet");
        }

        return 0;
    }

    public void setId_compet(int id_compet, int id_equipe) throws SQLException {

        Connection_Equipe();
        this.id_compet = id_compet;
        PreparedStatement stmt = connexion.prepareStatement("UPDATE equipe SET id_compet = '" + id_compet + "' WHERE id_equipe = '" + id_equipe + "'");
        stmt.executeUpdate();
        connexion.close();

    }

    public void deleteEquipe(int id_equipe) throws SQLException {
        Connection_Equipe();
        PreparedStatement stmt = connexion.prepareStatement("DELETE FROM equipe WHERE id_equipe = '" + id_equipe + "'");
        stmt.executeUpdate();
        stmt.close();
        connexion.close();
    }

    public String[] getAllEquipe() throws SQLException {
        Connection_Equipe();
        Statement statement = connexion.createStatement();
        int taille = 0;
        ResultSet count = statement.executeQuery("SELECT  count(*) nb FROM equipe ");
        while (count.next()) {
            taille = Integer.parseInt(count.getString("nb"));
        }

        count.close();
        int i = 0;
        ResultSet result = statement.executeQuery("SELECT * FROM equipe ");

        String tabNomPrenom[] = new String[taille];

        while (result.next()) {

            tabNomPrenom[i] = result.getString("id_equipe") + "-" + result.getString("nom_equipe");
            i++;

        }
        statement.close();
        connexion.close();
        return tabNomPrenom;

    }

    public String[] getIdEquipe() throws SQLException {
        Connection_Equipe();
        Statement statement = connexion.createStatement();

        int taille = 0;
        ResultSet count = statement.executeQuery("SELECT  count(*) nb FROM equipe ");
        while (count.next()) {
            taille = Integer.parseInt(count.getString("nb"));
        }

        count.close();
        ResultSet result = statement.executeQuery("SELECT * FROM equipe ");
        int i = 0;

        String tabID[] = new String[taille];

        while (result.next()) {

            tabID[i] = result.getString("id_equipe");
            i++;

        }
        statement.close();
        connexion.close();
        return tabID;

    }
}
