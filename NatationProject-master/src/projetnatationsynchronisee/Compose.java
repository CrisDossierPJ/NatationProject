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
public class Compose {

    int id_equipe;
    int id_personne;

    public Compose() {
    }

    public Compose(int id_equipe, int id_personne) {
        this.id_equipe = id_equipe;
        this.id_personne = id_personne;
    }

    public int getId_equipe() {
        return id_equipe;
    }

    public void setId_equipe(int id_equipe) {
        this.id_equipe = id_equipe;
    }

    public int getId_personne() {
        return id_personne;
    }

    public void setId_personne(int id_personne) {
        this.id_personne = id_personne;
    }

}
