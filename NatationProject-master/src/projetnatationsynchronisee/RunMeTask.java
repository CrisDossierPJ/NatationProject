/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetnatationsynchronisee;

/**
 *
 * @author Christian
 */
import java.sql.SQLException;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;

public class RunMeTask extends TimerTask {

    JLabel myLabel = new JLabel();
    JButton button = new JButton();
    int minute = 0;
    int seconde = 60;
    Equipe equipe;

    public RunMeTask(JLabel myLabel, int minute, JButton button) throws SQLException {
        this.equipe = new Equipe();
        this.myLabel = myLabel;
        this.minute = minute;
        this.button = button;

    }

    @Override
    public void run() {
        try {

            if (equipe.getEquipeEncours().equals("Aucune equipe en cours") == false) {
                seconde--;
                System.out.println(minute + "m" + seconde + "s");
                myLabel.setText(minute + "m" + seconde + "s");
                if (seconde == 0 && minute == 0) {
                    System.out.println("AAAAAAAAAAA");
                    button.setEnabled(false);
                    cancel();

                }
                if (seconde == 0) {
                    seconde = 60;
                    minute--;
                }

            } else {
                System.out.println("Aucune equipe en cours");
            }
        } catch (SQLException ex) {
            Logger.getLogger(RunMeTask.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
