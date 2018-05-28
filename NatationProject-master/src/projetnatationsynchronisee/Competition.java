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
public class Competition {

    int id_Compet;
    String Titre;
    String Date_Compet;
    String Lieu_Compet;

    public Competition(int id_Compet, String Titre, String Date_Compet, String Lieu_Compet) {
        this.id_Compet = id_Compet;
        this.Titre = Titre;
        this.Date_Compet = Date_Compet;
        this.Lieu_Compet = Lieu_Compet;
    }
   
    public String getTitre() {
        return Titre;
    }

    public void setTitre(String Titre) {
        this.Titre = Titre;
    }

    public String getDate_Compet() {
        return Date_Compet;
    }

    public void setDate_Compet(String Date_Compet) {
        this.Date_Compet = Date_Compet;
    }

    public String getLieu_Compet() {
        return Lieu_Compet;
    }

    public void setLieu_Compet(String Lieu_Compet) {
        this.Lieu_Compet = Lieu_Compet;
    }

}
