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
public class note {

    double note;
    int id_equipe;
    int id_personne;
    int visible;
    Equipe equipe;

    Connection connexion = null;
    ResultSet resultSet = null;
    ResourceBundle bundle = ResourceBundle.getBundle("natation.properties.config");
    String nomBdd = bundle.getString("bdd.name");
    String identifiant = bundle.getString("bdd.login");
    String pass = bundle.getString("bdd.password");
    String host = bundle.getString("bdd.hostname");
    String port = bundle.getString("bdd.port");

    public void Connection_Note() {
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

    public note() {

    }

    public DefaultTableModel buildTableModelNote() throws SQLException {
        Connection_Note();
        Statement statement = connexion.createStatement();
        this.equipe = new Equipe();
        ResultSet rs = statement.executeQuery("select n.note, e.nom_equipe, co.titre, n.visible from note n\n"
                + "join equipe e on n.id_equipe = e.id_equipe\n"
                + "join competition co on e.id_compet = co.id_compet\n"
                + "WHERE "
                + " co.date_compet <= current_date "
                + "AND current_date <= co.fin_date_compet "
                + "AND n.visible = true");
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
            if (rs.getBoolean("visible") == true) {
                Vector<Object> vector = new Vector<Object>();
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    vector.add(rs.getObject(columnIndex));
                }
                data.add(vector);
            }

        }
        statement.close();
        return new DefaultTableModel(data, columnNames);

    }

    public note(int note, int id_equipe, int id_personne, int visible) throws SQLException {
        this.equipe = new Equipe();
        Connection_Note();
        this.note = note;
        this.id_equipe = id_equipe;
        this.id_personne = id_personne;
        this.visible = visible;
        try (Statement statement = connexion.createStatement()) {
            String sql = "INSERT INTO note(note, id_equipe, id_personne, visible) VALUES ('" + note + "','" + id_equipe + "','" + id_personne + "', 'false')";

            statement.executeUpdate(sql);
            statement.close();
        }

        connexion.close();

    }

    public boolean isVisible(int id_equipe) throws SQLException {
        Connection_Note();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM note WHERE id_equipe = '" + id_equipe + "'");
        while (result.next()) {
            return result.getBoolean("visible");
        }

        return false;
    }

    public void setVisible(int visible, int id_equipe) throws SQLException {

        Connection_Note();
        this.visible = visible;
        String String_visible = "";
        if (visible == 0) {
            String_visible = "false";
        } else {
            String_visible = "true";
        }
        System.out.println("UPDATE note SET Visible = '" + String_visible + "' WHERE id_equipe = '" + id_equipe + "'");
        PreparedStatement stmt = connexion.prepareStatement("UPDATE note SET Visible = '" + String_visible + "' WHERE id_equipe = '" + id_equipe + "'");
        stmt.executeUpdate();
        connexion.close();

    }

    public int getNote(int id_equipe) throws SQLException {
        Connection_Note();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM note WHERE id_equipe = '" + id_equipe + "'");
        int note_Return = 0;
        while (result.next()) {
            note_Return = result.getInt("note");
        }
        statement.close();
        connexion.close();
        return note_Return;
    }

    public void setNote(double note, int id_personne, int id_equipe) throws SQLException {
        Connection_Note();
        this.note = note;
        PreparedStatement stmt = connexion.prepareStatement("UPDATE note SET note = '" + note + "' WHERE id_personne = '" + id_personne + "' AND id_equipe = '" + id_equipe + "'");
        stmt.executeUpdate();
        connexion.close();
        stmt.close();
    }

    public int getId_equipe() {
        return id_equipe;
    }

    public void setId_equipe(int id_equipe) {
        this.id_equipe = id_equipe;
    }

    public int getId_personne(int id_equipe) throws SQLException {
        Connection_Note();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM note WHERE id_equipe = '" + id_equipe + "'");
        int id_personne_Return = 0;
        while (result.next()) {
            id_personne_Return = result.getInt("id_equipe");
        }
        statement.close();
        connexion.close();
        return id_personne_Return;
    }

    public void setId_personne(int id_personne, int id_equipe) throws SQLException {
        Connection_Note();
        this.id_personne = id_personne;
        PreparedStatement stmt = connexion.prepareStatement("UPDATE id_personne SET id_personne = '" + id_personne + "' WHERE id_equipe = '" + id_equipe + "'");
        stmt.executeUpdate();
        connexion.close();
        stmt.close();
    }

    public void setNoteJuge(int id_equipe) throws SQLException {
        Connection_Note();
        this.id_personne = id_personne;
        PreparedStatement stmt = connexion.prepareStatement("update note set note = note - (select penalite from equipe where note.id_equipe = equipe.id_equipe "
                + "and equipe.id_equipe = " + id_equipe
                + ")");
        stmt.executeUpdate();
        connexion.close();
        stmt.close();
    }

    public double getNotejuge(int rang) throws SQLException {

        Connection_Note();
        Statement statement = connexion.createStatement();
        ResultSet result = statement.executeQuery("select * from note\n"
                + "join juge on juge.id_personne= note.id_personne\n"
                + "join equipe on equipe.id_equipe = note.id_equipe\n"
                + "WHERE rang = " + rang + " and equipe.visible = true;");

        double note_Return = 0;
        while (result.next()) {
            note_Return = result.getDouble("note");
        }
        statement.close();
        connexion.close();
        return note_Return;

    }

}
