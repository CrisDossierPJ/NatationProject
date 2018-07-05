/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NatationSynchronisee;

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
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class RunMeTask extends TimerTask {

    JLabel myLabel = new JLabel();
    JLabel labelEquipe = new JLabel();
    JButton button = new JButton();
    int minute = 0;
    int minuteSauv = 0;
    int seconde = 60;
    Equipe equipe;
    private DefaultTableModel dataModelNote;
    note note = new note();
    JTable jTableNote;

    public RunMeTask(JLabel labelEquipe, JLabel myLabel, int minute, JButton button, JTable jTableNote) throws SQLException {
        this.equipe = new Equipe();
        this.myLabel = myLabel;
        this.minute = minute - 1;
        this.button = button;
        this.labelEquipe = labelEquipe;
        this.minuteSauv = minute;
        this.jTableNote = jTableNote;

    }

    @Override
    public void run() {
        //minuteSauv = minute;
        //  System.out.println("MINute "+minuteSauv);
        try {
            dataModelNote = note.buildTableModelNote();
            this.jTableNote.setModel(this.dataModelNote);
            labelEquipe.setText(equipe.getEquipeEncours());
            if (equipe.getEquipeEncours().equals("Aucune equipe en cours") == false) {

                button.setEnabled(true);
                seconde--;

                // System.out.println(minute + "m" + seconde + "s");
                myLabel.setText(minute + "m" + seconde + "s");
                if (seconde == 0 && minute == 0) {
                    button.setEnabled(false);
                    cancel();
                    this.minute = minute;

                }
                if (seconde == 0) {
                    seconde = 60;
                    minute--;
                }

            } else {
                //  System.out.println("MINUTE SAVER "+minuteSauv);
                minute = minuteSauv - 1;
                seconde = 60;
                button.setEnabled(false);
                //   System.out.println("Aucune equipe en cours");
            }
        } catch (SQLException ex) {
            Logger.getLogger(RunMeTask.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
