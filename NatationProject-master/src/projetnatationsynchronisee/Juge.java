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
public class Juge {

    int Rang;
    boolean estArbitre;
    int id_personne;

    public Juge(int Rang, boolean estArbitre, int id_personne) {
        this.Rang = Rang;
        this.estArbitre = estArbitre;
        this.id_personne = id_personne;
    }

    public int getRang() {
        return Rang;
    }

    public void setRang(int Rang) {
        this.Rang = Rang;
    }

    public boolean isEstArbitre() {
        return estArbitre;
    }

    public void setEstArbitre(boolean estArbitre) {
        this.estArbitre = estArbitre;
    }

    public int getId_personne() {
        return id_personne;
    }

    public void setId_personne(int id_personne) {
        this.id_personne = id_personne;
    }

}
