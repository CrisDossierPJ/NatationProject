/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetnatationsynchronisee;

/**
 *
 * @author attiac
 */
public class Equipe {

    int id_equipe;
    String nom_equipe;
    int num_passage;
    int penalite;
    boolean Visible;
    String DateCreation;
    int id_club;
    int id_compet;

    public Equipe(int id_equipe, String nom_equipe, int num_passage, int penalite, boolean Visible, String DateCreation, int id_club, int id_compet) {
        this.id_equipe = id_equipe;
        this.nom_equipe = nom_equipe;
        this.num_passage = num_passage;
        this.penalite = penalite;
        this.Visible = Visible;
        this.DateCreation = DateCreation;
        this.id_club = id_club;
        this.id_compet = id_compet;
    }

    public int getId_equipe() {
        return id_equipe;
    }

    public void setId_equipe(int id_equipe) {
        this.id_equipe = id_equipe;
    }

    public String getNom_equipe() {
        return nom_equipe;
    }

    public void setNom_equipe(String nom_equipe) {
        this.nom_equipe = nom_equipe;
    }

    public int getNum_passage() {
        return num_passage;
    }

    public void setNum_passage(int num_passage) {
        this.num_passage = num_passage;
    }

    public int getPenalite() {
        return penalite;
    }

    public void setPenalite(int penalite) {
        this.penalite = penalite;
    }

    public boolean isVisible() {
        return Visible;
    }

    public void setVisible(boolean Visible) {
        this.Visible = Visible;
    }

    public String getDateCreation() {
        return DateCreation;
    }

    public void setDateCreation(String DateCreation) {
        this.DateCreation = DateCreation;
    }

    public int getId_club() {
        return id_club;
    }

    public void setId_club(int id_club) {
        this.id_club = id_club;
    }

    public int getId_compet() {
        return id_compet;
    }

    public void setId_compet(int id_compet) {
        this.id_compet = id_compet;
    }

}
