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
public class Equipe {

    int id_equipe;
    String nom_equipe;
    int num_passage;
    double penalite;
    boolean Visible;
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

    public Equipe(String nom_equipe, int num_passage, double penalite, boolean Visible, String DateCreation, int id_club, int id_compet) throws SQLException {
        Connection_Equipe();

        this.nom_equipe = nom_equipe;
        this.num_passage = num_passage;
        this.penalite = penalite;
        this.Visible = Visible;
        this.DateCreation = DateCreation;
        this.id_club = id_club;
        this.id_compet = id_compet;

        try (Statement statement = connexion.createStatement()) {
            /* System.out.println("INSERT INTO equipe( nom_equipe, num_passage, penalite, visible, DateCreation, id_club, id_compet) VALUES ('"
                    + nom_equipe + "','" + num_passage + "','" + penalite + "','" + Visible + "','" + DateCreation + "','" + id_club + "','" + id_compet + "')");*/
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
        String nom_Equipe_Return = "";
        while (result.next()) {
            nom_Equipe_Return = result.getString("nom_equipe");
        }
        statement.close();
        connexion.close();
        return nom_Equipe_Return;
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
        int num_Passsage_Return = 0;
        while (result.next()) {
            num_Passsage_Return = result.getInt("num_passage");
        }
        statement.close();
        connexion.close();
        return num_Passsage_Return;
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
        int penalite_Return = 0;
        while (result.next()) {
            penalite_Return = result.getInt("penalite");
        }
        statement.close();
        connexion.close();
        return penalite_Return;
    }

    public void setPenalite(double penalite, int id_equipe) throws SQLException {

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
        boolean visible_return = false;
        while (result.next()) {
            visible_return = result.getBoolean("visible");
        }
        statement.close();
        connexion.close();
        return visible_return;
    }

    public void setVisible(int Visible, int id_equipe) throws SQLException {

        Connection_Equipe();
        // this.Visible = Visible;
        String visible = "";
        if (Visible == 0) {
            visible = "false";
        } else {
            visible = "true";
        }
        PreparedStatement stmt = connexion.prepareStatement("UPDATE equipe SET Visible = '" + visible + "' WHERE id_equipe = '" + id_equipe + "'");
        stmt.executeUpdate();
        connexion.close();

    }

    public String getDateCreation(int id_equipe) throws SQLException {
        Connection_Equipe();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM equipe WHERE id_equipe = '" + id_equipe + "'");
        String date_Creation_Return = "";
        while (result.next()) {
            date_Creation_Return = result.getString("DateCreation");
        }
        statement.close();
        connexion.close();
        return date_Creation_Return;
    }

    public void setDateCreation(String DateCreation, int id_equipe) throws SQLException {

        Connection_Equipe();
        this.DateCreation = DateCreation;
        PreparedStatement stmt = connexion.prepareStatement("UPDATE equipe SET DateCreation = '" + DateCreation + "' WHERE id_equipe = '" + id_equipe + "'");
        stmt.executeUpdate();
        connexion.close();

    }

    public int getId_club(int id_equipe) throws SQLException {
        Connection_Equipe();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM equipe WHERE id_equipe = '" + id_equipe + "'");
        int id_Club_Return = 0;
        while (result.next()) {
            id_Club_Return = result.getInt("id_club");
        }
        statement.close();
        connexion.close();
        return id_Club_Return;
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
        int id_Compet_Return = 0;
        while (result.next()) {
            id_Compet_Return = result.getInt("id_compet");
        }

        return id_Compet_Return;
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

    public int getIdEquipe(int rang) throws SQLException {
        Connection_Equipe();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("select * from note\n"
                + "join juge on juge.id_personne= note.id_personne\n"
                + "join equipe on equipe.id_equipe = note.id_equipe\n"
                + "WHERE rang = " + rang + " and equipe.visible = true;");
        int id_Equipe_return = 0;
        while (result.next()) {
            id_Equipe_return = result.getInt("id_equipe");
        }
        statement.close();
        connexion.close();
        return id_Equipe_return;

    }

    public int getIdJuge(int rang) throws SQLException {
        Connection_Equipe();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("select * from note\n"
                + "join juge on juge.id_personne= note.id_personne\n"
                + "join equipe on equipe.id_equipe = note.id_equipe\n"
                + "WHERE rang = " + rang + " and equipe.visible = true;");
        int id_Juge_return = 0;
        while (result.next()) {
            id_Juge_return = result.getInt("id_personne");
        }
        statement.close();
        connexion.close();
        return id_Juge_return;

    }

    public String getEquipeEncours() throws SQLException {
        Connection_Equipe();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("select * from equipe Where visible = true");
        String nom_equipe_Return = "";
        while (result.next()) {
            nom_equipe_Return = result.getString("nom_equipe");
        }
        statement.close();
        connexion.close();
        return nom_equipe_Return;
    }

    public void setAllNotVisible() throws SQLException {
        Connection_Equipe();
        PreparedStatement stmt = connexion.prepareStatement("UPDATE equipe SET Visible = 'false' ");
        stmt.executeUpdate();
        connexion.close();
    }

    public int getIdEquipeEncours() throws SQLException {
        Connection_Equipe();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("select * from equipe Where visible = true");
        int id_Equipe_Cours_Return = 0;
        while (result.next()) {
            id_Equipe_Cours_Return = result.getInt("id_equipe");
        }
        statement.close();
        connexion.close();
        return id_Equipe_Cours_Return;
    }
}
