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
public class Club {

    int id_club;
    String nom_club;
    int id_personne;

    

    public Club(int id_club, String nom_club, int id_personne) {
        this.id_club = id_club;
        this.nom_club = nom_club;
        this.id_personne = id_personne;
    }

    public int getId_club() {
        
        return id_club;
    }

    public void setId_club(int id_club) {
        this.id_club = id_club;
    }

    public String getNom_club() {
        return nom_club;
    }

    public void setNom_club(String nom_club) {
        this.nom_club = nom_club;
    }

    public int getId_personne() {
        return id_personne;
    }

    public void setId_personne(int id_personne) {
        this.id_personne = id_personne;
    }

}
