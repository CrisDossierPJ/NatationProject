/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetnatationsynchronisee;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author Christian
 */
public class NatationSynchronisee extends javax.swing.JFrame {

    /**
     * Creates new form NatationSynchronisee
     */
    private DefaultTableModel dataModelUser;
    private DefaultTableModel dataModelPersonne;
    private DefaultTableModel dataModelClub;
    private DefaultTableModel dataModelCompet;
    private DefaultTableModel dataModelEquipe;
    private DefaultTableModel dataModelNageuse;
    private DefaultTableModel dataModelNote;

    User utilisateur = new User();
    Personne personne = new Personne();
    Juge juge = new Juge();
    Club club = new Club();
    Equipe equipe = new Equipe();
    Competition compet = new Competition();
    Nageuse nageuse = new Nageuse();
    note note = new note();
    JFrame fenetre = new JFrame();
    boolean popupBool = false;
    JLabel labelLogin = new JLabel();
    JLabel labelPswd = new JLabel();
    JTextField login = new JTextField(4);
    JPasswordField pass = new JPasswordField(4);

    public NatationSynchronisee() throws SQLException {
        dataModelUser = utilisateur.buildTableModelUser();
        dataModelPersonne = personne.buildTableModelPersonne();
        dataModelClub = club.buildTableModelClub();
        dataModelCompet = compet.buildTableModelCompetition();
        dataModelEquipe = equipe.buildTableModelEquipe();
        dataModelNageuse = nageuse.buildTableModelNageuse();
        dataModelNote = note.buildTableModelNote();
        popup();

    }

    public void refresh() throws SQLException {
        dataModelUser = utilisateur.buildTableModelUser();
        dataModelPersonne = personne.buildTableModelPersonne();
        dataModelClub = club.buildTableModelClub();
        dataModelCompet = compet.buildTableModelCompetition();
        dataModelEquipe = equipe.buildTableModelEquipe();
        dataModelNageuse = nageuse.buildTableModelNageuse();
        dataModelNote = note.buildTableModelNote();
        dataModelNageuse = nageuse.buildTableModelNageuse();

        this.tablePersonne.setModel(this.dataModelPersonne);
        this.jTableUser.setModel(this.dataModelUser);
        this.jtableClub.setModel(this.dataModelClub);
        this.jTableCompet.setModel(this.dataModelCompet);
        this.jTableEquipe.setModel(this.dataModelEquipe);
        this.jTableNote.setModel(this.dataModelNote);
        this.jTableNageuse.setModel(this.dataModelNageuse);

        DefaultComboBoxModel modelPersonne = new DefaultComboBoxModel(personneForUser());
        DefaultComboBoxModel modelCompet = new DefaultComboBoxModel(competForEquipe());
        DefaultComboBoxModel modelClub = new DefaultComboBoxModel(clubForEquipe());
        listPersonne.setModel(modelPersonne);
        listePersonneForNageuse.setModel(modelPersonne);
        listPersonneClub.setModel(modelPersonne);

        boxCompetForEquipe.setModel(modelCompet);
        boxClubForEquipe.setModel(modelClub);

        textJugeNote1.setText(note.getNotejuge(1) + "");
        textJugeNote2.setText(note.getNotejuge(2) + "");
        textJugeNote3.setText(note.getNotejuge(3) + "");
        textJugeNote4.setText(note.getNotejuge(4) + "");
        textJugeNote5.setText(note.getNotejuge(5) + "");

        if (radioButJuge.isSelected() == true) {
            labelRangJuge.setVisible(true);
            boxRang.setVisible(true);
            //radioButJugeArb.setSelected(false);
        } else {
            labelRangJuge.setVisible(false);
            boxRang.setVisible(false);
        }

    }

    public final void popup() throws SQLException {

        JPanel pan = new JPanel();
        JButton bouton = new JButton("Connection");

        // login.setVisible(true);
        bouton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {

                try {
                    if (utilisateur.Authentification(login.getText(), pass.getText()) == true) {
                        initComponents();
                        refresh();
                        fenetre.dispose();

                        login(login.getText());
                        personne.setInfos(login.getText());
                        labelNomJuge.setText("Bonjour " + personne.getNomAuth() + " " + personne.getPrenomAuth() + " n°" + juge.getRang(personne.getId_personne()));
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        fenetre.setSize(150, 150);
        fenetre.toFront();
        fenetre.setAlwaysOnTop(true);
        labelLogin.setText("Nom d'utilisateur ");
        pan.add(labelLogin);
        pan.add(login);
        labelLogin.setText("Mot de passe :");
        pan.add(labelPswd);
        pan.add(pass);
        pan.add(bouton);
        fenetre.setContentPane(pan);
        fenetre.setVisible(true);
        fenetre.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    }

    /* private void boutonActionPerformed(java.awt.event.ActionEvent evt) {

    }*/
    public void efface(String Type, JTabbedPane Panneau) {

        while (Panneau.getTabCount() != 2) {

            for (int i = 0; i < Panneau.getTabCount(); i++) {
                if (Panneau.getTitleAt(i).equals(Type) == false) {
                    if (Panneau.getTitleAt(i).equals("Resultat") == false) {
                        Panneau.removeTabAt(i);
                    }
                }

            }
        }

    }

    public void login(String user_login) throws SQLException {

        if (null != login.getText()) {

            if (utilisateur.isEstAdminAuth(user_login)) {
                efface("Admin", jTabbedPane2);

            } else if (utilisateur.isCreateurCompetAuth(user_login)) {
                efface("Créateur de compétition", jTabbedPane2);

            } else if (utilisateur.isJugeArbAuth(user_login)) {
                efface("Juge-Arbitre", jTabbedPane2);
            } else if (utilisateur.isJugeArbAuth(user_login) == false) {
                efface("Juge", jTabbedPane2);
            } else {
                System.out.println("caca");
            }
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jLabel69 = new javax.swing.JLabel();
        panelGlobal = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        panelJuge = new javax.swing.JPanel();
        labelNomJuge = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        buttonNoterJuge = new javax.swing.JButton();
        labelNote = new javax.swing.JLabel();
        SliderNote = new javax.swing.JSlider();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        valeur_penalite = new javax.swing.JComboBox<>();
        jButtonPenalite = new javax.swing.JButton();
        boxClubForPen = new javax.swing.JComboBox<>(EquipeForNageuse());
        jPanel6 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        valideNote = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        textJugeNote1 = new javax.swing.JTextField();
        textJugeNote2 = new javax.swing.JTextField();
        textJugeNote3 = new javax.swing.JTextField();
        textJugeNote5 = new javax.swing.JTextField();
        textJugeNote4 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        butonLanceBallet = new javax.swing.JButton();
        stopBallet = new javax.swing.JButton();
        boxClubForLanc = new javax.swing.JComboBox<>(EquipeForNageuse());
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel10 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jTextField27 = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        jTextField28 = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        jTextField29 = new javax.swing.JTextField();
        jButton15 = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jTextField24 = new javax.swing.JTextField();
        jTextField25 = new javax.swing.JTextField();
        jTextField26 = new javax.swing.JTextField();
        jButton14 = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jTextField34 = new javax.swing.JTextField();
        jLabel60 = new javax.swing.JLabel();
        jButton17 = new javax.swing.JButton();
        jComboBox9 = new javax.swing.JComboBox<>();
        //DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try{
            textdateCreationClub1 = new javax.swing.JFormattedTextField(new MaskFormatter("####-##-##"));
            jLabel79 = new javax.swing.JLabel();
            jPanel17 = new javax.swing.JPanel();
            jLabel50 = new javax.swing.JLabel();
            jLabel51 = new javax.swing.JLabel();
            jLabel52 = new javax.swing.JLabel();
            jLabel53 = new javax.swing.JLabel();
            jLabel54 = new javax.swing.JLabel();
            jLabel55 = new javax.swing.JLabel();
            jLabel56 = new javax.swing.JLabel();
            jLabel57 = new javax.swing.JLabel();
            jTextField12 = new javax.swing.JTextField();
            jTextField30 = new javax.swing.JTextField();
            jTextField31 = new javax.swing.JTextField();
            jTextField32 = new javax.swing.JTextField();
            jTextField33 = new javax.swing.JTextField();
            boxCompetForEquipe1 = new javax.swing.JComboBox<>(competForEquipe());
            boxClubForEquipe1 = new javax.swing.JComboBox<>(clubForEquipe());
            buttonCreateEquipe1 = new javax.swing.JButton();
            jPanel4 = new javax.swing.JPanel();
            jScrollPane2 = new javax.swing.JScrollPane();
            jTableNote = new javax.swing.JTable();
            jButton6 = new javax.swing.JButton();
            jLabel19 = new javax.swing.JLabel();
            panelAdmin = new javax.swing.JPanel();
            jLabel2 = new javax.swing.JLabel();
            jTabbedPane3 = new javax.swing.JTabbedPane();
            jPanel12 = new javax.swing.JPanel();
            jLabel12 = new javax.swing.JLabel();
            buttonAddUser = new javax.swing.JButton();
            labelRangJuge = new javax.swing.JLabel();
            boxRang = new javax.swing.JComboBox<>();
            jLabel37 = new javax.swing.JLabel();
            jLabel38 = new javax.swing.JLabel();
            textLogin = new javax.swing.JTextField();
            radioButJuge = new javax.swing.JRadioButton();
            radioButJugeArb = new javax.swing.JRadioButton();
            radioButAdm = new javax.swing.JRadioButton();
            radioButCrea = new javax.swing.JRadioButton();
            deleteUser = new javax.swing.JButton();
            updateUser = new javax.swing.JButton();
            jScrollPane7 = new javax.swing.JScrollPane();
            jTableUser = new javax.swing.JTable();
            jLabel30 = new javax.swing.JLabel();
            textPass = new javax.swing.JPasswordField();
            jLabel31 = new javax.swing.JLabel();
            textFindUtilisateur = new javax.swing.JTextField();
            jLabel72 = new javax.swing.JLabel();
            listPersonne = new javax.swing.JComboBox<>(personneForUser());
            jPanel19 = new javax.swing.JPanel();
            jLabel28 = new javax.swing.JLabel();
            AddUser = new javax.swing.JButton();
            jLabel61 = new javax.swing.JLabel();
            textprenomPersonne = new javax.swing.JTextField();
            jLabel62 = new javax.swing.JLabel();
            textnomPersonne = new javax.swing.JTextField();
            deletePersonne = new javax.swing.JButton();
            updatePersonne = new javax.swing.JButton();
            jLabel63 = new javax.swing.JLabel();
            jLabel66 = new javax.swing.JLabel();
            jScrollPane6 = new javax.swing.JScrollPane();
            tablePersonne = new javax.swing.JTable();
            //DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            try{
                textdateCreationPersonne = new javax.swing.JFormattedTextField(new MaskFormatter("####-##-##"));
                DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                try{
                    textdateDeNaissancePersonne = new javax.swing.JFormattedTextField(new MaskFormatter("####-##-##"));
                    jLabel29 = new javax.swing.JLabel();
                    labelID = new javax.swing.JLabel();
                    jLabel7 = new javax.swing.JLabel();
                    textFindPersonne = new javax.swing.JTextField();
                    jLabel73 = new javax.swing.JLabel();
                    jPanel13 = new javax.swing.JPanel();
                    jLabel34 = new javax.swing.JLabel();
                    jLabel35 = new javax.swing.JLabel();
                    jLabel36 = new javax.swing.JLabel();
                    jLabel39 = new javax.swing.JLabel();
                    textLieuCompet = new javax.swing.JTextField();
                    textTitreCompet = new javax.swing.JTextField();
                    buttonCreateCompet = new javax.swing.JButton();
                    buttonUpdateCompet = new javax.swing.JButton();
                    buttonSupprimerCompet = new javax.swing.JButton();
                    jScrollPane3 = new javax.swing.JScrollPane();
                    jTableCompet = new javax.swing.JTable();
                    textFindCompet = new javax.swing.JTextField();
                    jLabel75 = new javax.swing.JLabel();
                    //DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    try{
                        textdateCreationCompet = new javax.swing.JFormattedTextField(new MaskFormatter("####-##-##"));
                        jPanel14 = new javax.swing.JPanel();
                        jLabel8 = new javax.swing.JLabel();
                        jLabel9 = new javax.swing.JLabel();
                        fieldNomClub = new javax.swing.JTextField();
                        jLabel13 = new javax.swing.JLabel();
                        buttonCreateClub = new javax.swing.JButton();
                        buttonUpdateClub = new javax.swing.JButton();
                        buttonDeleteClub = new javax.swing.JButton();
                        jScrollPane5 = new javax.swing.JScrollPane();
                        jtableClub = new javax.swing.JTable();
                        listPersonneClub = new javax.swing.JComboBox<>(personneForUser());
                        jLabel74 = new javax.swing.JLabel();
                        textFindClub = new javax.swing.JTextField();
                        jLabel77 = new javax.swing.JLabel();
                        //DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            textdateCreationClub = new javax.swing.JFormattedTextField(new MaskFormatter("####-##-##"));
                            jLabel78 = new javax.swing.JLabel();
                            jPanel8 = new javax.swing.JPanel();
                            listePersonneForNageuse = new javax.swing.JComboBox<>(personneForUser());
                            jLabel65 = new javax.swing.JLabel();
                            ButAddNag = new javax.swing.JButton();
                            jLabel67 = new javax.swing.JLabel();
                            jLabel68 = new javax.swing.JLabel();
                            jLabel70 = new javax.swing.JLabel();
                            jButton5 = new javax.swing.JButton();
                            jButton7 = new javax.swing.JButton();
                            jLabel71 = new javax.swing.JLabel();
                            jScrollPane8 = new javax.swing.JScrollPane();
                            jTableNageuse = new javax.swing.JTable();
                            butDeleteNaj = new javax.swing.JToggleButton();
                            boxEquipeNaj = new javax.swing.JComboBox<>(EquipeForNageuse());
                            boxNaj = new javax.swing.JComboBox<>(AllNageuse());
                            jPanel15 = new javax.swing.JPanel();
                            jLabel10 = new javax.swing.JLabel();
                            jLabel11 = new javax.swing.JLabel();
                            jLabel14 = new javax.swing.JLabel();
                            jLabel15 = new javax.swing.JLabel();
                            jLabel16 = new javax.swing.JLabel();
                            jLabel21 = new javax.swing.JLabel();
                            jLabel32 = new javax.swing.JLabel();
                            jLabel33 = new javax.swing.JLabel();
                            textNomEquipe = new javax.swing.JTextField();
                            buttonCreateEquipe = new javax.swing.JButton();
                            boxCompetForEquipe = new javax.swing.JComboBox<>(competForEquipe());
                            boxClubForEquipe = new javax.swing.JComboBox<>(clubForEquipe());
                            buttonDeleteEquipe = new javax.swing.JButton();
                            textNumPassage = new javax.swing.JTextField();
                            boxVisible = new javax.swing.JComboBox<>();
                            boxPenaliteEquipe = new javax.swing.JComboBox<>();
                            buttonUpdateEquipe = new javax.swing.JButton();
                            jScrollPane4 = new javax.swing.JScrollPane();
                            jTableEquipe = new javax.swing.JTable();
                            textFindEquipe = new javax.swing.JTextField();
                            jLabel76 = new javax.swing.JLabel();
                            //DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                            try{
                                textdateCreationEquipe = new javax.swing.JFormattedTextField(new MaskFormatter("####-##-##"));

                                javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
                                jPanel2.setLayout(jPanel2Layout);
                                jPanel2Layout.setHorizontalGroup(
                                    jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGap(0, 100, Short.MAX_VALUE)
                                );
                                jPanel2Layout.setVerticalGroup(
                                    jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGap(0, 100, Short.MAX_VALUE)
                                );

                                jTextField1.setText("jTextField1");

                                jLabel69.setText("jLabel69");

                                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

                                panelGlobal.setBackground(new java.awt.Color(51, 102, 255));
                                panelGlobal.setBorder(new javax.swing.border.MatteBorder(null));

                                jLabel1.setText("FFN");

                                jTabbedPane2.setBackground(new java.awt.Color(102, 102, 255));

                                labelNomJuge.setBackground(new java.awt.Color(255, 255, 255));
                                labelNomJuge.setText("Juge n °x Dupond Dupont");

                                jLabel6.setText("Equipe  x ");

                                jLabel17.setText("Temps restant : ");

                                buttonNoterJuge.setText("Noter");

                                labelNote.setText("Note : ");

                                SliderNote.addChangeListener(new javax.swing.event.ChangeListener() {
                                    public void stateChanged(javax.swing.event.ChangeEvent evt) {
                                        SliderNoteStateChanged(evt);
                                    }
                                });

                                javax.swing.GroupLayout panelJugeLayout = new javax.swing.GroupLayout(panelJuge);
                                panelJuge.setLayout(panelJugeLayout);
                                panelJugeLayout.setHorizontalGroup(
                                    panelJugeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelJugeLayout.createSequentialGroup()
                                        .addContainerGap(144, Short.MAX_VALUE)
                                        .addGroup(panelJugeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelJugeLayout.createSequentialGroup()
                                                .addComponent(labelNomJuge)
                                                .addGap(420, 420, 420)
                                                .addComponent(jLabel17)
                                                .addGap(80, 80, 80))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelJugeLayout.createSequentialGroup()
                                                .addComponent(buttonNoterJuge)
                                                .addContainerGap())))
                                    .addGroup(panelJugeLayout.createSequentialGroup()
                                        .addGroup(panelJugeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(panelJugeLayout.createSequentialGroup()
                                                .addGap(232, 232, 232)
                                                .addComponent(jLabel6)
                                                .addGap(18, 18, 18)
                                                .addComponent(SliderNote, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(panelJugeLayout.createSequentialGroup()
                                                .addGap(392, 392, 392)
                                                .addComponent(labelNote)))
                                        .addGap(0, 247, Short.MAX_VALUE))
                                );
                                panelJugeLayout.setVerticalGroup(
                                    panelJugeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelJugeLayout.createSequentialGroup()
                                        .addGap(25, 25, 25)
                                        .addGroup(panelJugeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(labelNomJuge)
                                            .addComponent(jLabel17))
                                        .addGap(87, 87, 87)
                                        .addGroup(panelJugeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel6)
                                            .addComponent(SliderNote, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addComponent(labelNote)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 196, Short.MAX_VALUE)
                                        .addComponent(buttonNoterJuge)
                                        .addContainerGap())
                                );

                                jTabbedPane2.addTab("Juge", panelJuge);

                                jLabel3.setText("Juge Arbitre");

                                jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

                                jLabel18.setText("Ajouter Pénalité");

                                valeur_penalite.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0.5", "1", "1.5", "2" }));

                                jButtonPenalite.setText("Mettre pénalité");
                                jButtonPenalite.addActionListener(new java.awt.event.ActionListener() {
                                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                                        jButtonPenaliteActionPerformed(evt);
                                    }
                                });

                                boxClubForPen.addActionListener(new java.awt.event.ActionListener() {
                                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                                        boxClubForPenActionPerformed(evt);
                                    }
                                });

                                javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
                                jPanel5.setLayout(jPanel5Layout);
                                jPanel5Layout.setHorizontalGroup(
                                    jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel5Layout.createSequentialGroup()
                                                .addComponent(boxClubForPen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(43, 43, 43)
                                                .addComponent(valeur_penalite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                                .addGap(0, 86, Short.MAX_VALUE)
                                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(78, 78, 78))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                                        .addComponent(jButtonPenalite)
                                                        .addContainerGap())))))
                                );
                                jPanel5Layout.setVerticalGroup(
                                    jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jLabel18)
                                        .addGap(31, 31, 31)
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(valeur_penalite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(boxClubForPen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButtonPenalite)
                                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                );

                                jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

                                jLabel20.setText("Corriger notes");

                                valideNote.setText("Valider notes");
                                valideNote.addActionListener(new java.awt.event.ActionListener() {
                                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                                        valideNoteActionPerformed(evt);
                                    }
                                });

                                jLabel22.setText("Juge 1 : ");

                                jLabel23.setText("Juge 2 : ");

                                jLabel24.setText("Juge 4 : ");

                                jLabel25.setText("Juge 5 : ");

                                jLabel26.setText("Juge 3 : ");

                                javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
                                jPanel6.setLayout(jPanel6Layout);
                                jPanel6Layout.setHorizontalGroup(
                                    jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addGap(0, 89, Short.MAX_VALUE)
                                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(59, 59, 59))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                                        .addComponent(valideNote)
                                                        .addContainerGap())))
                                            .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                                        .addComponent(jLabel22)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(textJugeNote1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                                        .addComponent(jLabel23)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(textJugeNote2, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(jLabel26)
                                                            .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.TRAILING))
                                                        .addGap(18, 18, 18)
                                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(textJugeNote3, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(textJugeNote4, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                                        .addComponent(jLabel25)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(textJugeNote5, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(0, 0, Short.MAX_VALUE))))
                                );
                                jPanel6Layout.setVerticalGroup(
                                    jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jLabel20)
                                        .addGap(19, 19, 19)
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel22)
                                            .addComponent(textJugeNote1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel23)
                                            .addComponent(textJugeNote2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(16, 16, 16)
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(textJugeNote3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel26))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(textJugeNote4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel24))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel25)
                                            .addComponent(textJugeNote5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 108, Short.MAX_VALUE)
                                        .addComponent(valideNote)
                                        .addContainerGap())
                                );

                                jButton3.setText("Afficher Résultats");

                                jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

                                jLabel40.setText("Lancer équipe : ");

                                butonLanceBallet.setText("Lancer Ballet");
                                butonLanceBallet.addActionListener(new java.awt.event.ActionListener() {
                                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                                        butonLanceBalletActionPerformed(evt);
                                    }
                                });

                                stopBallet.setText("Arreter ballet");
                                stopBallet.addActionListener(new java.awt.event.ActionListener() {
                                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                                        stopBalletActionPerformed(evt);
                                    }
                                });

                                boxClubForLanc.addActionListener(new java.awt.event.ActionListener() {
                                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                                        boxClubForLancActionPerformed(evt);
                                    }
                                });

                                javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
                                jPanel7.setLayout(jPanel7Layout);
                                jPanel7Layout.setHorizontalGroup(
                                    jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(stopBallet)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 92, Short.MAX_VALUE)
                                                .addComponent(butonLanceBallet))
                                            .addGroup(jPanel7Layout.createSequentialGroup()
                                                .addGap(114, 114, 114)
                                                .addComponent(jLabel40)
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                        .addContainerGap())
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(boxClubForLanc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                );
                                jPanel7Layout.setVerticalGroup(
                                    jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(jLabel40)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                                        .addComponent(boxClubForLanc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(33, 33, 33)
                                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(butonLanceBallet)
                                            .addComponent(stopBallet))
                                        .addContainerGap())
                                );

                                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                                jPanel1.setLayout(jPanel1Layout);
                                jPanel1Layout.setHorizontalGroup(
                                    jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel3)
                                                .addGap(498, 498, 498))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap())))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(jButton3))
                                );
                                jPanel1Layout.setVerticalGroup(
                                    jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jLabel3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(47, 47, 47)
                                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(6, 6, 6)
                                        .addComponent(jButton3)
                                        .addContainerGap())
                                );

                                jTabbedPane2.addTab("Juge arbitre", jPanel1);

                                jLabel4.setText("Créateur de compétition ");

                                jPanel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

                                jLabel45.setText("Créer Nageuse");

                                jLabel46.setText("Nom");

                                jLabel47.setText("Prénom");

                                jLabel48.setText("Date de Naissance");

                                jLabel49.setText("Date Création");

                                jButton15.setText("Créer une nouvelle nageuse");

                                javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
                                jPanel10.setLayout(jPanel10Layout);
                                jPanel10Layout.setHorizontalGroup(
                                    jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel10Layout.createSequentialGroup()
                                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel10Layout.createSequentialGroup()
                                                .addGap(279, 279, 279)
                                                .addComponent(jLabel45))
                                            .addGroup(jPanel10Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel10Layout.createSequentialGroup()
                                                        .addComponent(jLabel46)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jTextField27, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel10Layout.createSequentialGroup()
                                                        .addComponent(jLabel47)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel10Layout.createSequentialGroup()
                                                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(jLabel49)
                                                            .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(jTextField28, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(jTextField29, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                                        .addContainerGap(346, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(jButton15)
                                        .addGap(22, 22, 22))
                                );
                                jPanel10Layout.setVerticalGroup(
                                    jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel10Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jLabel45)
                                        .addGap(26, 26, 26)
                                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel46)
                                            .addComponent(jTextField27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel47)
                                            .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel49)
                                            .addComponent(jTextField29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel48)
                                            .addComponent(jTextField28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 99, Short.MAX_VALUE)
                                        .addComponent(jButton15)
                                        .addGap(48, 48, 48))
                                );

                                jTabbedPane1.addTab("Ajouter Nageuse ", jPanel10);

                                jPanel16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

                                jLabel41.setText("Créer compétition");

                                jLabel42.setText("Titre");

                                jLabel43.setText("Date Compétition");

                                jLabel44.setText("Lieu Compétition ");

                                jButton14.setText("Créer compétition");
                                jButton14.addActionListener(new java.awt.event.ActionListener() {
                                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                                        jButton14ActionPerformed(evt);
                                    }
                                });

                                javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
                                jPanel16.setLayout(jPanel16Layout);
                                jPanel16Layout.setHorizontalGroup(
                                    jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel16Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel43)
                                            .addComponent(jLabel44)
                                            .addComponent(jLabel42))
                                        .addGap(30, 30, 30)
                                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel16Layout.createSequentialGroup()
                                                .addComponent(jTextField25, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                            .addGroup(jPanel16Layout.createSequentialGroup()
                                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jTextField24, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jTextField26, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addContainerGap(486, Short.MAX_VALUE))))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton14)
                                        .addGap(41, 41, 41))
                                    .addGroup(jPanel16Layout.createSequentialGroup()
                                        .addGap(284, 284, 284)
                                        .addComponent(jLabel41)
                                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                );
                                jPanel16Layout.setVerticalGroup(
                                    jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel16Layout.createSequentialGroup()
                                        .addGap(28, 28, 28)
                                        .addComponent(jLabel41)
                                        .addGap(38, 38, 38)
                                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel42)
                                            .addComponent(jTextField26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel43)
                                            .addComponent(jTextField24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel44)
                                            .addComponent(jTextField25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addComponent(jButton14)
                                        .addContainerGap(126, Short.MAX_VALUE))
                                );

                                jTabbedPane1.addTab("Créer compétition", jPanel16);

                                jPanel18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

                                jLabel58.setText("Créer club");

                                jLabel59.setText("Nom Club");

                                jLabel60.setText("Nom Dirigeant");

                                jButton17.setText("Créer Club");
                                jButton17.addActionListener(new java.awt.event.ActionListener() {
                                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                                        jButton17ActionPerformed(evt);
                                    }
                                });

                                jComboBox9.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
                                jComboBox9.addActionListener(new java.awt.event.ActionListener() {
                                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                                        jComboBox9ActionPerformed(evt);
                                    }
                                });

                                textdateCreationCompet.setFocusLostBehavior(textdateCreationCompet.PERSIST);
                            }
                            catch(Exception e){}
                            textdateCreationClub1.addActionListener(new java.awt.event.ActionListener() {
                                public void actionPerformed(java.awt.event.ActionEvent evt) {
                                    textdateCreationClub1ActionPerformed(evt);
                                }
                            });

                            jLabel79.setText("Date création club");

                            javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
                            jPanel18.setLayout(jPanel18Layout);
                            jPanel18Layout.setHorizontalGroup(
                                jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel18Layout.createSequentialGroup()
                                    .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel18Layout.createSequentialGroup()
                                            .addContainerGap()
                                            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel60)
                                                .addComponent(jLabel59))
                                            .addGap(24, 24, 24)
                                            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jTextField34, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(jPanel18Layout.createSequentialGroup()
                                            .addGap(283, 283, 283)
                                            .addComponent(jLabel58)))
                                    .addGap(0, 365, Short.MAX_VALUE))
                                .addGroup(jPanel18Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                                            .addGap(0, 0, Short.MAX_VALUE)
                                            .addComponent(jButton17))
                                        .addGroup(jPanel18Layout.createSequentialGroup()
                                            .addComponent(jLabel79)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(textdateCreationClub1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            );
                            jPanel18Layout.setVerticalGroup(
                                jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel18Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(jLabel58)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel59)
                                        .addComponent(jTextField34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel60)
                                        .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel79)
                                        .addComponent(textdateCreationClub1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 189, Short.MAX_VALUE)
                                    .addComponent(jButton17)
                                    .addContainerGap())
                            );

                            jTabbedPane1.addTab("Créer club", jPanel18);

                            jPanel17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

                            jLabel50.setText("Créer équipe");

                            jLabel51.setText("Nom équipe : ");

                            jLabel52.setText("Numéro passage");

                            jLabel53.setText("Pénalité");

                            jLabel54.setText("Visible");

                            jLabel55.setText("Date Création");

                            jLabel56.setText("Compétition correspondante");

                            jLabel57.setText("Club correspondant");

                            buttonCreateEquipe1.setText("Créer équipe");
                            buttonCreateEquipe1.addActionListener(new java.awt.event.ActionListener() {
                                public void actionPerformed(java.awt.event.ActionEvent evt) {
                                    buttonCreateEquipe1ActionPerformed(evt);
                                }
                            });

                            javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
                            jPanel17.setLayout(jPanel17Layout);
                            jPanel17Layout.setHorizontalGroup(
                                jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel17Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel53)
                                        .addComponent(jLabel52)
                                        .addComponent(jLabel51)
                                        .addComponent(jLabel54)
                                        .addComponent(jLabel55)
                                        .addComponent(jLabel57))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTextField33, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextField32, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextField31, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextField12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextField30, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(boxClubForEquipe1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(boxCompetForEquipe1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                                    .addGap(0, 334, Short.MAX_VALUE)
                                    .addComponent(jLabel50)
                                    .addGap(301, 301, 301))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(buttonCreateEquipe1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addContainerGap())
                            );
                            jPanel17Layout.setVerticalGroup(
                                jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel17Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(jLabel50)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel17Layout.createSequentialGroup()
                                            .addComponent(jLabel51)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabel52)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jLabel53)
                                            .addGap(12, 12, 12)
                                            .addComponent(jLabel54)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jLabel55))
                                        .addGroup(jPanel17Layout.createSequentialGroup()
                                            .addComponent(jTextField30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jTextField31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(6, 6, 6)
                                            .addComponent(jTextField33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jTextField32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel17Layout.createSequentialGroup()
                                            .addComponent(jLabel56)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jLabel57))
                                        .addGroup(jPanel17Layout.createSequentialGroup()
                                            .addComponent(boxCompetForEquipe1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(boxClubForEquipe1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGap(42, 42, 42)
                                    .addComponent(buttonCreateEquipe1)
                                    .addContainerGap(68, Short.MAX_VALUE))
                            );

                            jTabbedPane1.addTab("Créer équipe", jPanel17);

                            javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
                            jPanel3.setLayout(jPanel3Layout);
                            jPanel3Layout.setHorizontalGroup(
                                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addGap(374, 374, 374)
                                    .addComponent(jLabel4)
                                    .addContainerGap(392, Short.MAX_VALUE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 704, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(84, 84, 84))
                            );
                            jPanel3Layout.setVerticalGroup(
                                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel4)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(16, 16, 16))
                            );

                            jTabbedPane2.addTab("Créateur de compétition", jPanel3);

                            jTableNote.setModel(new javax.swing.table.DefaultTableModel(
                                new Object [][] {
                                    {null, null, null, null},
                                    {null, null, null, null},
                                    {null, null, null, null},
                                    {null, null, null, null}
                                },
                                new String [] {
                                    "Title 1", "Title 2", "Title 3", "Title 4"
                                }
                            ));
                            this.jTableNote.setModel(this.dataModelNote);
                            jTableNote.addMouseListener(new java.awt.event.MouseAdapter() {
                                public void mouseClicked(java.awt.event.MouseEvent evt) {
                                    jTableNoteMouseClicked(evt);
                                }
                            });
                            jScrollPane2.setViewportView(jTableNote);

                            jButton6.setText("Enregistrer Résultat");
                            jButton6.addActionListener(new java.awt.event.ActionListener() {
                                public void actionPerformed(java.awt.event.ActionEvent evt) {
                                    jButton6ActionPerformed(evt);
                                }
                            });

                            jLabel19.setText("Résultat");

                            javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
                            jPanel4.setLayout(jPanel4Layout);
                            jPanel4Layout.setHorizontalGroup(
                                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                    .addGap(0, 0, Short.MAX_VALUE)
                                    .addComponent(jButton6)
                                    .addGap(34, 34, 34))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                    .addContainerGap(70, Short.MAX_VALUE)
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 758, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(57, 57, 57))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                            .addComponent(jLabel19)
                                            .addGap(411, 411, 411))))
                            );
                            jPanel4Layout.setVerticalGroup(
                                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel19)
                                    .addGap(18, 18, 18)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton6)
                                    .addGap(22, 22, 22))
                            );

                            jTabbedPane2.addTab("Resultat", jPanel4);

                            panelAdmin.setForeground(new java.awt.Color(0, 51, 153));

                            jLabel2.setText("Administration");

                            jTabbedPane3.addMouseListener(new java.awt.event.MouseAdapter() {
                                public void mouseClicked(java.awt.event.MouseEvent evt) {
                                    jTabbedPane3MouseClicked(evt);
                                }
                            });

                            jPanel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

                            jLabel12.setText("Création utilisateur");

                            buttonAddUser.setText("Ajouter utilisateur");
                            buttonAddUser.addActionListener(new java.awt.event.ActionListener() {
                                public void actionPerformed(java.awt.event.ActionEvent evt) {
                                    buttonAddUserActionPerformed(evt);
                                }
                            });

                            labelRangJuge.setText("Rang");

                            boxRang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5" }));

                            jLabel37.setText("Mot de passe");

                            jLabel38.setText("Login");

                            radioButJuge.setText("Juge");
                            radioButJuge.addActionListener(new java.awt.event.ActionListener() {
                                public void actionPerformed(java.awt.event.ActionEvent evt) {
                                    radioButJugeActionPerformed(evt);
                                }
                            });

                            radioButJugeArb.setText("Juge-Arbitre");
                            radioButJugeArb.addActionListener(new java.awt.event.ActionListener() {
                                public void actionPerformed(java.awt.event.ActionEvent evt) {
                                    radioButJugeArbActionPerformed(evt);
                                }
                            });

                            radioButAdm.setText("Administrateur");
                            radioButAdm.addActionListener(new java.awt.event.ActionListener() {
                                public void actionPerformed(java.awt.event.ActionEvent evt) {
                                    radioButAdmActionPerformed(evt);
                                }
                            });

                            radioButCrea.setText("Créateur de compétition");
                            radioButCrea.addActionListener(new java.awt.event.ActionListener() {
                                public void actionPerformed(java.awt.event.ActionEvent evt) {
                                    radioButCreaActionPerformed(evt);
                                }
                            });

                            deleteUser.setText("Supprimer utilisateur");
                            deleteUser.addActionListener(new java.awt.event.ActionListener() {
                                public void actionPerformed(java.awt.event.ActionEvent evt) {
                                    deleteUserActionPerformed(evt);
                                }
                            });

                            updateUser.setText("Modifier utilisateur");
                            updateUser.addActionListener(new java.awt.event.ActionListener() {
                                public void actionPerformed(java.awt.event.ActionEvent evt) {
                                    updateUserActionPerformed(evt);
                                }
                            });

                            jTableUser.setModel(new javax.swing.table.DefaultTableModel(
                                new Object [][] {
                                    {},
                                    {},
                                    {},
                                    {}
                                },
                                new String [] {

                                }
                            ));
                            this.jTableUser.setModel(this.dataModelUser);
                            jTableUser.addMouseListener(new java.awt.event.MouseAdapter() {
                                public void mouseClicked(java.awt.event.MouseEvent evt) {
                                    jTableUserMouseClicked(evt);
                                }
                                public void mouseEntered(java.awt.event.MouseEvent evt) {
                                    jTableUserMouseEntered(evt);
                                }
                            });
                            jScrollPane7.setViewportView(jTableUser);

                            jLabel30.setText("Personne");

                            jLabel31.setText("Cliquez sur un ID pour afficher les informations de l'utilisateur");

                            textFindUtilisateur.addKeyListener(new java.awt.event.KeyAdapter() {
                                public void keyPressed(java.awt.event.KeyEvent evt) {
                                    textFindUtilisateurKeyPressed(evt);
                                }
                                public void keyReleased(java.awt.event.KeyEvent evt) {
                                    textFindUtilisateurKeyReleased(evt);
                                }
                            });

                            jLabel72.setText("Rechercher utilisateur");

                            listPersonne.addActionListener(new java.awt.event.ActionListener() {
                                public void actionPerformed(java.awt.event.ActionEvent evt) {
                                    listPersonneActionPerformed(evt);
                                }
                            });

                            javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
                            jPanel12.setLayout(jPanel12Layout);
                            jPanel12Layout.setHorizontalGroup(
                                jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel12Layout.createSequentialGroup()
                                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(jPanel12Layout.createSequentialGroup()
                                                    .addComponent(jLabel12)
                                                    .addGap(16, 16, 16))
                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel12Layout.createSequentialGroup()
                                                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel12Layout.createSequentialGroup()
                                                            .addComponent(radioButJugeArb)
                                                            .addGap(18, 18, 18))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                                                            .addComponent(radioButJuge)
                                                            .addGap(54, 54, 54)))
                                                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(radioButAdm)
                                                        .addComponent(radioButCrea))))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 87, Short.MAX_VALUE)
                                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                                                    .addComponent(textFindUtilisateur, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(22, 22, 22))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                                                    .addComponent(jLabel72)
                                                    .addGap(12, 12, 12))))
                                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel12Layout.createSequentialGroup()
                                                .addGap(132, 132, 132)
                                                .addComponent(boxRang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel12Layout.createSequentialGroup()
                                                .addComponent(buttonAddUser, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(updateUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(deleteUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                            .addComponent(labelRangJuge)
                                            .addGroup(jPanel12Layout.createSequentialGroup()
                                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel38)
                                                    .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel30))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(listPersonne, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(textLogin, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                                                        .addComponent(textPass))))))
                                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel12Layout.createSequentialGroup()
                                            .addGap(70, 70, 70)
                                            .addComponent(jLabel31)
                                            .addGap(34, 34, 34))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                                            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(55, 55, 55))))
                            );
                            jPanel12Layout.setVerticalGroup(
                                jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel12Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel12Layout.createSequentialGroup()
                                            .addComponent(jLabel31)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel12Layout.createSequentialGroup()
                                            .addComponent(jLabel12)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel12Layout.createSequentialGroup()
                                                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(radioButAdm)
                                                        .addComponent(radioButJuge))
                                                    .addGap(18, 18, 18)
                                                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(radioButCrea)
                                                        .addComponent(radioButJugeArb)))
                                                .addGroup(jPanel12Layout.createSequentialGroup()
                                                    .addComponent(jLabel72)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(textFindUtilisateur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGap(9, 9, 9)
                                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel30)
                                                .addComponent(listPersonne, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(14, 14, 14)
                                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel38)
                                                .addComponent(textLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(18, 18, 18)
                                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel37)
                                                .addComponent(textPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(labelRangJuge)
                                                .addComponent(boxRang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(23, 23, 23)
                                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(buttonAddUser)
                                                .addComponent(deleteUser, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(updateUser)))
                                    .addContainerGap(32, Short.MAX_VALUE))
                            );

                            jTabbedPane3.addTab("Créer utilisateur", jPanel12);

                            jPanel19.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

                            jLabel28.setText("Création Personne");

                            AddUser.setText("Ajouter utilisateur");
                            AddUser.addActionListener(new java.awt.event.ActionListener() {
                                public void actionPerformed(java.awt.event.ActionEvent evt) {
                                    AddUserActionPerformed(evt);
                                }
                            });

                            jLabel61.setText("Prénom");

                            jLabel62.setText("Nom");

                            deletePersonne.setText("Supprimer utilisateur");
                            deletePersonne.addActionListener(new java.awt.event.ActionListener() {
                                public void actionPerformed(java.awt.event.ActionEvent evt) {
                                    deletePersonneActionPerformed(evt);
                                }
                            });

                            updatePersonne.setText("Modifier utilisateur");
                            updatePersonne.addActionListener(new java.awt.event.ActionListener() {
                                public void actionPerformed(java.awt.event.ActionEvent evt) {
                                    updatePersonneActionPerformed(evt);
                                }
                            });

                            jLabel63.setText("Date de naissance");

                            jLabel66.setText("Date création");

                            tablePersonne.setModel(new javax.swing.table.DefaultTableModel(
                                new Object [][] {
                                    {},
                                    {},
                                    {},
                                    {}
                                },
                                new String [] {

                                }
                            ));
                            this.tablePersonne.setModel(this.dataModelPersonne);
                            tablePersonne.addMouseListener(new java.awt.event.MouseAdapter() {
                                public void mouseClicked(java.awt.event.MouseEvent evt) {
                                    tablePersonneMouseClicked(evt);
                                }
                            });
                            jScrollPane6.setViewportView(tablePersonne);

                            textdateCreationPersonne.setFocusLostBehavior(textdateCreationPersonne.PERSIST);
                        }
                        catch(Exception e){}
                        textdateCreationPersonne.addActionListener(new java.awt.event.ActionListener() {
                            public void actionPerformed(java.awt.event.ActionEvent evt) {
                                textdateCreationPersonneActionPerformed(evt);
                            }
                        });

                        textdateCreationPersonne.setFocusLostBehavior(textdateCreationPersonne.PERSIST);
                    }
                    catch(Exception e){}
                    textdateDeNaissancePersonne.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            textdateDeNaissancePersonneActionPerformed(evt);
                        }
                    });

                    jLabel29.setText("Id : ");

                    jLabel7.setText("Cliquez sur un ID pour afficher les informations de la personne");

                    textFindPersonne.addKeyListener(new java.awt.event.KeyAdapter() {
                        public void keyPressed(java.awt.event.KeyEvent evt) {
                            textFindPersonneKeyPressed(evt);
                        }
                        public void keyReleased(java.awt.event.KeyEvent evt) {
                            textFindPersonneKeyReleased(evt);
                        }
                    });

                    jLabel73.setText("Rechercher personne ");

                    javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
                    jPanel19.setLayout(jPanel19Layout);
                    jPanel19Layout.setHorizontalGroup(
                        jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel19Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel19Layout.createSequentialGroup()
                                    .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel19Layout.createSequentialGroup()
                                            .addComponent(jLabel61, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(39, 39, 39)
                                            .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(textnomPersonne, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(textprenomPersonne, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(jLabel62))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel66, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel63))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(textdateCreationPersonne, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(textdateDeNaissancePersonne, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE))
                                .addGroup(jPanel19Layout.createSequentialGroup()
                                    .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel28)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                                                .addGap(26, 26, 26)
                                                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(AddUser, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(deletePersonne))))
                                        .addGroup(jPanel19Layout.createSequentialGroup()
                                            .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(32, 32, 32)
                                            .addComponent(labelID, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel19Layout.createSequentialGroup()
                                            .addGap(40, 40, 40)
                                            .addComponent(updatePersonne, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                                                    .addComponent(textFindPersonne, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(26, 26, 26))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                                                    .addComponent(jLabel73)
                                                    .addGap(12, 12, 12)))))))
                            .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(28, 28, 28))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                                    .addComponent(jLabel7)
                                    .addGap(35, 35, 35))))
                    );
                    jPanel19Layout.setVerticalGroup(
                        jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                            .addComponent(jLabel7)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel19Layout.createSequentialGroup()
                                    .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel19Layout.createSequentialGroup()
                                            .addComponent(jLabel28)
                                            .addGap(26, 26, 26)
                                            .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(labelID, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel29)))
                                        .addGroup(jPanel19Layout.createSequentialGroup()
                                            .addComponent(jLabel73)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(textFindPersonne, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGap(24, 24, 24)
                                    .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel62)
                                        .addComponent(textnomPersonne, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel66)
                                        .addComponent(textdateCreationPersonne, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel61)
                                        .addComponent(textprenomPersonne, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel63)
                                        .addComponent(textdateDeNaissancePersonne, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(85, 85, 85)
                                    .addComponent(AddUser)
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(deletePersonne)
                                        .addComponent(updatePersonne))
                                    .addContainerGap(37, Short.MAX_VALUE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addContainerGap())))
                    );

                    jTabbedPane3.addTab("Créer personne", jPanel19);

                    jPanel13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

                    jLabel34.setText("Créer compétition");

                    jLabel35.setText("Titre");

                    jLabel36.setText("Date Compétition");

                    jLabel39.setText("Lieu Compétition ");

                    buttonCreateCompet.setText("Créer compétition");
                    buttonCreateCompet.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            buttonCreateCompetActionPerformed(evt);
                        }
                    });

                    buttonUpdateCompet.setText("Modifier compétition");
                    buttonUpdateCompet.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            buttonUpdateCompetActionPerformed(evt);
                        }
                    });

                    buttonSupprimerCompet.setText("Supprimer compétition");
                    buttonSupprimerCompet.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            buttonSupprimerCompetActionPerformed(evt);
                        }
                    });

                    jTableCompet.setModel(new javax.swing.table.DefaultTableModel(
                        new Object [][] {
                            {},
                            {},
                            {},
                            {}
                        },
                        new String [] {

                        }
                    ));
                    this.jTableCompet.setModel(this.dataModelCompet);
                    jTableCompet.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            jTableCompetMouseClicked(evt);
                        }
                    });
                    jScrollPane3.setViewportView(jTableCompet);

                    textFindCompet.addKeyListener(new java.awt.event.KeyAdapter() {
                        public void keyPressed(java.awt.event.KeyEvent evt) {
                            textFindCompetKeyPressed(evt);
                        }
                        public void keyReleased(java.awt.event.KeyEvent evt) {
                            textFindCompetKeyReleased(evt);
                        }
                    });

                    jLabel75.setText("Rechercher compétition");

                    textdateCreationCompet.setFocusLostBehavior(textdateCreationCompet.PERSIST);
                }
                catch(Exception e){}
                textdateCreationCompet.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        textdateCreationCompetActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
                jPanel13.setLayout(jPanel13Layout);
                jPanel13Layout.setHorizontalGroup(
                    jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel36)
                                    .addComponent(jLabel39)
                                    .addComponent(jLabel35))
                                .addGap(30, 30, 30)
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(textLieuCompet, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel13Layout.createSequentialGroup()
                                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(textTitreCompet, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                                            .addComponent(textdateCreationCompet))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 136, Short.MAX_VALUE)
                                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                                                .addComponent(textFindCompet, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(32, 32, 32))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                                                .addComponent(jLabel75)
                                                .addGap(12, 12, 12))))))
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel13Layout.createSequentialGroup()
                                        .addGap(191, 191, 191)
                                        .addComponent(jLabel34))
                                    .addGroup(jPanel13Layout.createSequentialGroup()
                                        .addGap(28, 28, 28)
                                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(buttonUpdateCompet, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(buttonSupprimerCompet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(buttonCreateCompet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                                .addGap(18, 18, 18)))
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))
                );
                jPanel13Layout.setVerticalGroup(
                    jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(jLabel34)
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel13Layout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel35)
                                            .addComponent(textTitreCompet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel36)
                                            .addComponent(textdateCreationCompet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel13Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel75)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(textFindCompet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(21, 21, 21)
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel39)
                                    .addComponent(textLieuCompet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(51, 51, 51)
                                .addComponent(buttonCreateCompet)
                                .addGap(18, 18, 18)
                                .addComponent(buttonUpdateCompet)
                                .addGap(18, 18, 18)
                                .addComponent(buttonSupprimerCompet)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                );

                jTabbedPane3.addTab("Créer compétition", jPanel13);

                jPanel14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

                jLabel8.setText("Créer club");

                jLabel9.setText("Nom Club");

                jLabel13.setText("Nom Dirigeant");

                buttonCreateClub.setText("Créer Club");
                buttonCreateClub.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        buttonCreateClubActionPerformed(evt);
                    }
                });

                buttonUpdateClub.setText("Modifier Club");
                buttonUpdateClub.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        buttonUpdateClubActionPerformed(evt);
                    }
                });

                buttonDeleteClub.setText("Supprimer Club");
                buttonDeleteClub.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        buttonDeleteClubActionPerformed(evt);
                    }
                });

                jtableClub.setModel(new javax.swing.table.DefaultTableModel(
                    new Object [][] {
                        {},
                        {},
                        {},
                        {}
                    },
                    new String [] {

                    }
                ));
                this.jtableClub.setModel(this.dataModelClub);
                jtableClub.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        jtableClubMouseClicked(evt);
                    }
                });
                jScrollPane5.setViewportView(jtableClub);

                jLabel74.setText("Rechercher personne ");

                textFindClub.addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyPressed(java.awt.event.KeyEvent evt) {
                        textFindClubKeyPressed(evt);
                    }
                    public void keyReleased(java.awt.event.KeyEvent evt) {
                        textFindClubKeyReleased(evt);
                    }
                });

                jLabel77.setText("Cliquez sur un ID pour afficher les informations du club");

                textdateCreationCompet.setFocusLostBehavior(textdateCreationCompet.PERSIST);
            }
            catch(Exception e){}
            textdateCreationClub.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    textdateCreationClubActionPerformed(evt);
                }
            });

            jLabel78.setText("Date création club");

            javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
            jPanel14.setLayout(jPanel14Layout);
            jPanel14Layout.setHorizontalGroup(
                jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel14Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(buttonDeleteClub, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                            .addComponent(buttonUpdateClub, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonCreateClub, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel14Layout.createSequentialGroup()
                            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel13)
                                .addComponent(jLabel9)
                                .addComponent(jLabel78))
                            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel14Layout.createSequentialGroup()
                                    .addGap(0, 34, Short.MAX_VALUE)
                                    .addComponent(fieldNomClub, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel14Layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(listPersonneClub, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, Short.MAX_VALUE))
                                .addGroup(jPanel14Layout.createSequentialGroup()
                                    .addGap(31, 31, 31)
                                    .addComponent(textdateCreationClub)))
                            .addGap(75, 75, 75)
                            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                                    .addComponent(textFindClub, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(14, 14, 14))
                                .addComponent(jLabel74, javax.swing.GroupLayout.Alignment.TRAILING))))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(25, 25, 25))
                .addGroup(jPanel14Layout.createSequentialGroup()
                    .addGap(318, 318, 318)
                    .addComponent(jLabel8)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel77)
                    .addGap(27, 27, 27))
            );
            jPanel14Layout.setVerticalGroup(
                jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel14Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(jLabel77))
                    .addGap(8, 8, 8)
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel14Layout.createSequentialGroup()
                            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel14Layout.createSequentialGroup()
                                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel9)
                                        .addComponent(fieldNomClub, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(22, 22, 22)
                                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel13)
                                        .addComponent(listPersonneClub, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel14Layout.createSequentialGroup()
                                    .addComponent(jLabel74)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(textFindClub, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(16, 16, 16)
                            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel78)
                                .addComponent(textdateCreationClub, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(39, 39, 39)
                            .addComponent(buttonDeleteClub)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(buttonUpdateClub)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(buttonCreateClub))
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jTabbedPane3.addTab("Créer Club", jPanel14);

            listePersonneForNageuse.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    listePersonneForNageuseActionPerformed(evt);
                }
            });

            jLabel65.setText("Personne");

            ButAddNag.setText("Ajouter Nageuse");
            ButAddNag.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    ButAddNagActionPerformed(evt);
                }
            });

            jLabel67.setText("Nageuse");

            jLabel68.setText("Créer nageuse");

            jLabel70.setText("Ajouter nageuse à équipe");

            jButton5.setText("Enlever Nageuse");
            jButton5.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton5ActionPerformed(evt);
                }
            });

            jButton7.setText("Ajouter nageuse");
            jButton7.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton7ActionPerformed(evt);
                }
            });

            jLabel71.setText("Equipe");

            jTableNageuse.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {},
                    {},
                    {},
                    {}
                },
                new String [] {

                }
            ));
            this.jTableNageuse.setModel(this.dataModelNageuse);
            jTableNageuse.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    jTableNageuseMouseClicked(evt);
                }
            });
            jScrollPane8.setViewportView(jTableNageuse);

            butDeleteNaj.setText("Supprimer Nageuse");
            butDeleteNaj.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    butDeleteNajActionPerformed(evt);
                }
            });

            boxEquipeNaj.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    boxEquipeNajActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
            jPanel8.setLayout(jPanel8Layout);
            jPanel8Layout.setHorizontalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel8Layout.createSequentialGroup()
                                    .addGap(187, 187, 187)
                                    .addComponent(jLabel68))
                                .addGroup(jPanel8Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel65)
                                        .addComponent(jLabel67)
                                        .addComponent(jLabel71))
                                    .addGap(31, 31, 31)
                                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(listePersonneForNageuse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(boxEquipeNaj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(boxNaj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel8Layout.createSequentialGroup()
                                    .addGap(67, 67, 67)
                                    .addComponent(jButton7)
                                    .addGap(51, 51, 51)
                                    .addComponent(jButton5)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 126, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                                    .addComponent(ButAddNag)
                                    .addGap(18, 18, 18)
                                    .addComponent(butDeleteNaj)
                                    .addGap(18, 18, 18))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                                    .addComponent(jLabel70)
                                    .addGap(176, 176, 176)))))
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );
            jPanel8Layout.setVerticalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel68)
                    .addGap(43, 43, 43)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ButAddNag)
                        .addComponent(butDeleteNaj))
                    .addGap(18, 18, 18)
                    .addComponent(jLabel70)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton5)
                        .addComponent(jButton7))
                    .addGap(46, 46, 46))
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addGap(42, 42, 42)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel65)
                        .addComponent(listePersonneForNageuse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(92, 92, 92)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                            .addComponent(jLabel67)
                            .addGap(24, 24, 24)
                            .addComponent(jLabel71))
                        .addGroup(jPanel8Layout.createSequentialGroup()
                            .addComponent(boxNaj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(boxEquipeNaj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                    .addContainerGap(19, Short.MAX_VALUE)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );

            jTabbedPane3.addTab("Créer nageuse", jPanel8);

            jPanel15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

            jLabel10.setText("Créer équipe");

            jLabel11.setText("Nom équipe : ");

            jLabel14.setText("Numéro passage");

            jLabel15.setText("Pénalité");

            jLabel16.setText("Visible");

            jLabel21.setText("Date Création");

            jLabel32.setText("Compétition correspondante");

            jLabel33.setText("Club correspondant");

            buttonCreateEquipe.setText("Créer équipe");
            buttonCreateEquipe.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    buttonCreateEquipeActionPerformed(evt);
                }
            });

            boxClubForEquipe.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    boxClubForEquipeActionPerformed(evt);
                }
            });

            buttonDeleteEquipe.setText("Supprimer équipe");
            buttonDeleteEquipe.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    buttonDeleteEquipeActionPerformed(evt);
                }
            });

            boxVisible.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Oui", "Non" }));

            boxPenaliteEquipe.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0", "0.5", "1", "2" }));

            buttonUpdateEquipe.setText("Modifier équipe");
            buttonUpdateEquipe.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    buttonUpdateEquipeActionPerformed(evt);
                }
            });

            jTableEquipe.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {},
                    {},
                    {},
                    {}
                },
                new String [] {

                }
            ));
            this.jTableEquipe.setModel(this.dataModelEquipe);
            jTableEquipe.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    jTableEquipeMouseClicked(evt);
                }
            });
            jScrollPane4.setViewportView(jTableEquipe);

            textFindEquipe.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    textFindEquipeKeyPressed(evt);
                }
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    textFindEquipeKeyReleased(evt);
                }
            });

            jLabel76.setText("Rechercher personne ");

            textdateCreationCompet.setFocusLostBehavior(textdateCreationCompet.PERSIST);
        }
        catch(Exception e){}
        textdateCreationEquipe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textdateCreationEquipeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(123, 123, 123)
                        .addComponent(jLabel10))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel16)
                                    .addComponent(jLabel21)
                                    .addComponent(jLabel33)
                                    .addGroup(jPanel15Layout.createSequentialGroup()
                                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel15Layout.createSequentialGroup()
                                                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(boxClubForEquipe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(boxCompetForEquipe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel15Layout.createSequentialGroup()
                                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel14)
                                                    .addComponent(jLabel11)
                                                    .addComponent(jLabel15))
                                                .addGap(34, 34, 34)
                                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(boxPenaliteEquipe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(textNomEquipe, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                                                    .addComponent(textNumPassage, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                                                    .addComponent(boxVisible, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(textdateCreationEquipe))))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                                                .addComponent(textFindEquipe, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(14, 14, 14))
                                            .addComponent(jLabel76, javax.swing.GroupLayout.Alignment.TRAILING)))))
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addComponent(buttonCreateEquipe, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(buttonDeleteEquipe)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonUpdateEquipe, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)))))
                .addGap(12, 12, 12)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel15Layout.createSequentialGroup()
                                        .addGap(24, 24, 24)
                                        .addComponent(jLabel11))
                                    .addGroup(jPanel15Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(textNomEquipe, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel14)
                                    .addComponent(textNumPassage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel15)
                                    .addComponent(boxPenaliteEquipe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel16)
                                    .addComponent(boxVisible, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel21)
                                    .addComponent(textdateCreationEquipe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel15Layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(jLabel76)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textFindEquipe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel32)
                            .addComponent(boxCompetForEquipe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel33)
                            .addComponent(boxClubForEquipe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(buttonCreateEquipe)
                            .addComponent(buttonDeleteEquipe)
                            .addComponent(buttonUpdateEquipe))
                        .addGap(51, 51, 51))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jTabbedPane3.addTab("Créer équipe", jPanel15);

        javax.swing.GroupLayout panelAdminLayout = new javax.swing.GroupLayout(panelAdmin);
        panelAdmin.setLayout(panelAdminLayout);
        panelAdminLayout.setHorizontalGroup(
            panelAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAdminLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 862, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(184, 184, 184))
            .addGroup(panelAdminLayout.createSequentialGroup()
                .addGap(395, 395, 395)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelAdminLayout.setVerticalGroup(
            panelAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAdminLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Admin", panelAdmin);

        javax.swing.GroupLayout panelGlobalLayout = new javax.swing.GroupLayout(panelGlobal);
        panelGlobal.setLayout(panelGlobalLayout);
        panelGlobalLayout.setHorizontalGroup(
            panelGlobalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGlobalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(panelGlobalLayout.createSequentialGroup()
                .addGap(431, 431, 431)
                .addComponent(jLabel1)
                .addContainerGap(460, Short.MAX_VALUE))
        );
        panelGlobalLayout.setVerticalGroup(
            panelGlobalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGlobalLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelGlobal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelGlobal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public String[] personneForUser() {
        String[] valeurs = null;
        try {
            valeurs = personne.getAllPersonne();

            return valeurs;
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valeurs;
    }

    public String[] AllNageuse() {
        String[] valeurs = null;
        try {
            valeurs = nageuse.getAllNageuse();

            return valeurs;
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valeurs;
    }

    public String[] clubForEquipe() {
        String[] valeurs = null;
        try {
            valeurs = club.getAllClub();

            return valeurs;
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valeurs;
    }

    public String[] competForEquipe() {
        String[] valeurs = null;
        try {
            valeurs = compet.getAllCompet();

            return valeurs;
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valeurs;
    }

    public String[] EquipeForNageuse() {
        String[] valeurs = null;
        try {
            valeurs = equipe.getAllEquipe();

            return valeurs;
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valeurs;
    }


    private void buttonAddUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAddUserActionPerformed

        try {
            // TODO add your handling code here:
            int estAdmin = 0;
            int estCreateur = 0;
            if (radioButAdm.isSelected()) {
                estAdmin = 1;

            }
            if (radioButCrea.isSelected()) {
                estCreateur = 1;
            }

            int index = 0;
            String allPersonne[] = personne.getAllPersonne();
            String allIdPersonne[] = personne.getIdPersonne();
            for (int i = 0; i < allPersonne.length; i++) {
                if (allPersonne[i].equals(listPersonne.getSelectedItem())) {
                    index = i;
                    break;
                }

            }
            User newUser = new User();
            Juge juge = new Juge();
            if (radioButJuge.isSelected() == false || radioButJugeArb.isSelected() == false) {
                newUser = new User(textLogin.getText(), textPass.getText(), estAdmin, estCreateur, Integer.parseInt(allIdPersonne[index]));
                Object test = listPersonne.getSelectedItem();

                refresh();
                listPersonne.setSelectedItem(test);
            } else {
                newUser = new User(textLogin.getText(), textPass.getText(), estAdmin, estCreateur, Integer.parseInt(allIdPersonne[index]));
                int estArbitre = 0;
                if (radioButJugeArb.isSelected() == true) {
                    estArbitre = 1;
                    juge = new Juge(0, estArbitre, Integer.parseInt(allIdPersonne[index]));
                } else {
                    juge = new Juge(Integer.parseInt(boxRang.getSelectedItem().toString()), estArbitre, Integer.parseInt(allIdPersonne[index]));
                }

                Object test = listPersonne.getSelectedItem();

                refresh();
                listPersonne.setSelectedItem(test);
            }

        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_buttonAddUserActionPerformed

    private void jTableNoteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableNoteMouseClicked

    }//GEN-LAST:event_jTableNoteMouseClicked

    private void SliderNoteStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_SliderNoteStateChanged
        // TODO add your handling code here:
        labelNote.setText("Note :  " + SliderNote.getValue());
    }//GEN-LAST:event_SliderNoteStateChanged

    private void deleteUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteUserActionPerformed

        try {
            // TODO add your handling code here:
            int index = 0;
            String allPersonne[] = personne.getAllPersonne();
            String allIdPersonne[] = personne.getIdPersonne();
            for (int i = 0; i < allPersonne.length; i++) {
                if (allPersonne[i].equals(listPersonne.getSelectedItem())) {
                    index = i;
                    break;
                }

            }

            utilisateur.deleteUser(Integer.parseInt(allIdPersonne[index]));
            refresh();
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_deleteUserActionPerformed

    private void buttonCreateCompetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCreateCompetActionPerformed
        try {
            // TODO add your handling code here:
            Competition newCompet = new Competition(textTitreCompet.getText(), textdateCreationCompet.getText(), textLieuCompet.getText());
            refresh();
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_buttonCreateCompetActionPerformed

    private void buttonCreateClubActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCreateClubActionPerformed
        try {
            // TODO add your handling code here:
            int index = 0;
            String allPersonne[] = personne.getAllPersonne();
            String allIdPersonne[] = personne.getIdPersonne();
            for (int i = 0; i < allPersonne.length; i++) {
                if (allPersonne[i].equals(listePersonneForNageuse.getSelectedItem())) {
                    index = i;
                    break;
                }

            }
            Club newClub = new Club(fieldNomClub.getText(), Integer.parseInt(allIdPersonne[index]), textdateCreationClub.getText());
            Object test = listPersonneClub.getSelectedItem();

            refresh();
            listPersonneClub.setSelectedItem(test);
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_buttonCreateClubActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jComboBox9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox9ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    private void stopBalletActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopBalletActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            //System.out.println(boxClubForLanc.getSelectedItem().toString());
            String id_String = boxClubForLanc.getSelectedItem().toString();
            id_String = id_String.substring(0, 1);
            int id_equipe = Integer.parseInt(id_String);
            equipe.setVisible(0, id_equipe);
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_stopBalletActionPerformed

    private void buttonUpdateCompetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonUpdateCompetActionPerformed
        // TODO add your handling code here:
        try {

            int viewRow = jTableCompet.getSelectedRow();
            int modelRow = jTableCompet.convertRowIndexToModel(viewRow);

            int id_compet = Integer.parseInt(jTableCompet.getValueAt(modelRow, jTableCompet.getSelectedColumn()).toString());

            compet.setTitre(textTitreCompet.getText(), id_compet);
            compet.setDate_Compet(textdateCreationCompet.getText(), id_compet);
            compet.setLieu_Compet(textLieuCompet.getText(), id_compet);

            refresh();
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_buttonUpdateCompetActionPerformed

    private void buttonUpdateClubActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonUpdateClubActionPerformed
        try {
            int viewRow = jtableClub.getSelectedRow();
            int modelRow = jtableClub.convertRowIndexToModel(viewRow);

            int id_club = Integer.parseInt(jtableClub.getValueAt(modelRow, jtableClub.getSelectedColumn()).toString());

            int index = 0;
            String allPersonne[] = personne.getAllPersonne();
            String allIdPersonne[] = personne.getIdPersonne();
            for (int i = 0; i < allPersonne.length; i++) {
                if (allPersonne[i].equals(listPersonneClub.getSelectedItem())) {
                    index = i;
                    break;
                }

            }

            club.setNom_club(fieldNomClub.getText(), id_club);
            club.setId_personne(Integer.parseInt(allIdPersonne[index]), id_club);
            club.setDate_Creation_Club(textdateCreationClub.getText(), id_club);

            Object test = listPersonneClub.getSelectedItem();

            refresh();
            listPersonneClub.setSelectedItem(test);
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_buttonUpdateClubActionPerformed

    private void buttonSupprimerCompetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSupprimerCompetActionPerformed
        try {
            // TODO add your handling code here:
            int id_compet = Integer.parseInt(jTableCompet.getValueAt(jTableCompet.getSelectedRow(), jTableCompet.getSelectedColumn()).toString());
            compet.deleteCompet(id_compet);
            refresh();
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_buttonSupprimerCompetActionPerformed

    private void buttonDeleteClubActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDeleteClubActionPerformed
        try {
            // TODO add your handling code here:

            int id_club = Integer.parseInt(jtableClub.getValueAt(jtableClub.getSelectedRow(), jtableClub.getSelectedColumn()).toString());

            club.deleteClub(id_club);

            refresh();
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_buttonDeleteClubActionPerformed

    private void AddUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddUserActionPerformed

        try {
            // TODO add your handling code here:

            Personne newPersonne = new Personne(textnomPersonne.getText(), textprenomPersonne.getText(), textdateCreationPersonne.getText(), textdateDeNaissancePersonne.getText());

            Object test = listPersonneClub.getSelectedItem();
            refresh();
            listPersonneClub.setSelectedItem(test);
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_AddUserActionPerformed

    private void deletePersonneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletePersonneActionPerformed
        try {
            // TODO add your handling code here:

            personne.deletePersonne(Integer.parseInt(labelID.getText()));
            refresh();
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_deletePersonneActionPerformed

    private void updatePersonneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updatePersonneActionPerformed
        try {
            // TODO add your handling code here:
            personne.setNom(textnomPersonne.getText(), Integer.parseInt(labelID.getText()));
            personne.setPrenom(textprenomPersonne.getText(), Integer.parseInt(labelID.getText()));
            personne.setDateCreation(textdateCreationPersonne.getText(), Integer.parseInt(labelID.getText()));
            personne.setDateNaissance(textdateDeNaissancePersonne.getText(), Integer.parseInt(labelID.getText()));
            refresh();

        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_updatePersonneActionPerformed

    private void jTableCompetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableCompetMouseClicked
        // TODO add your handling code here:
        try {
            int viewRow = jTableCompet.getSelectedRow();
            int modelRow = jTableCompet.convertRowIndexToModel(viewRow);

            // TODO add your handling code here:
            int id_compet = Integer.parseInt(jTableCompet.getModel().getValueAt(modelRow, jTableCompet.getSelectedColumn()).toString());
            textTitreCompet.setText(compet.getTitre(id_compet));
            textdateCreationCompet.setText(compet.getDate_Compet(id_compet));
            textLieuCompet.setText(compet.getLieu_Compet(id_compet));

        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jTableCompetMouseClicked

    private void jTableEquipeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableEquipeMouseClicked
        // TODO add your handling code here:
        try {
            int viewRow = jTableEquipe.getSelectedRow();
            int modelRow = jTableEquipe.convertRowIndexToModel(viewRow);

            // TODO add your handling code here:
            textNomEquipe.setText(equipe.getNom_equipe((int) jTableEquipe.getModel().getValueAt(modelRow, jTableEquipe.getSelectedColumn())));
            textNumPassage.setText("" + equipe.getNum_passage((int) jTableEquipe.getModel().getValueAt(modelRow, jTableEquipe.getSelectedColumn())));
            textdateCreationEquipe.setText(equipe.getDateCreation((int) jTableEquipe.getModel().getValueAt(modelRow, jTableEquipe.getSelectedColumn())));

            int id_equipe = Integer.parseInt((jTableEquipe.getModel().getValueAt(modelRow, jTableEquipe.getSelectedColumn())).toString());
            //  textLogin.setText(utilisateur.getLogin(id_personne));
            String tabClubID[] = club.getIdClub();
            String tabCompetID[] = compet.getIdCompet();
            int indexClub = 0;
            int indexCompet = 0;

            for (int i = 0; i < tabClubID.length; i++) {

                if (Integer.parseInt(tabClubID[i]) == equipe.getId_club(id_equipe)) {
                    indexClub = i;

                    break;
                }
            }
            for (int i = 0; i < tabCompetID.length; i++) {
                System.out.println(tabCompetID[i]);
                if (Integer.parseInt(tabCompetID[i]) == equipe.getId_club(id_equipe)) {
                    indexCompet = i;

                    break;
                }
            }
            boxCompetForEquipe.setSelectedIndex(indexCompet);
            boxClubForEquipe.setSelectedIndex(indexClub);

        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jTableEquipeMouseClicked

    private void jtableClubMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtableClubMouseClicked
        try {
            int viewRow = jtableClub.getSelectedRow();
            int modelRow = jtableClub.convertRowIndexToModel(viewRow);

            // TODO add your handling code here:
            fieldNomClub.setText(club.getNom_club((int) jtableClub.getModel().getValueAt(modelRow, jtableClub.getSelectedColumn())));
            textdateCreationClub.setText(club.getDate_Creation_Club((int) jtableClub.getModel().getValueAt(modelRow, jtableClub.getSelectedColumn())));
            int index = 0;
            String tabID[] = personne.getIdPersonne();
            for (int i = 0; i < tabID.length; i++) {
                //   System.out.println((jtableClub.getModel().getValueAt(jtableClub.getSelectedRow(), jtableClub.getSelectedColumn())).toString());

                if (Integer.parseInt(tabID[i]) == club.getId_personne(Integer.parseInt(jtableClub.getModel().getValueAt(modelRow, jtableClub.getSelectedColumn()).toString()))) {

                    index = i;

                    break;

                }
            }

            listPersonneClub.setSelectedIndex(index);

        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jtableClubMouseClicked

    private void tablePersonneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablePersonneMouseClicked
        try {
            int viewRow = tablePersonne.getSelectedRow();
            int modelRow = tablePersonne.convertRowIndexToModel(viewRow);
            // TODO add your handling code here:
            labelID.setText(tablePersonne.getModel().getValueAt(modelRow, tablePersonne.getSelectedColumn()).toString());
            textdateCreationPersonne.setText(personne.getDateCreation(Integer.parseInt(tablePersonne.getModel().getValueAt(modelRow, tablePersonne.getSelectedColumn()).toString())));
            textdateDeNaissancePersonne.setText(personne.getDateNaissance(Integer.parseInt(tablePersonne.getModel().getValueAt(modelRow, tablePersonne.getSelectedColumn()).toString())));
            textnomPersonne.setText(personne.getNom(Integer.parseInt(tablePersonne.getModel().getValueAt(modelRow, tablePersonne.getSelectedColumn()).toString())));
            textprenomPersonne.setText(personne.getPrenom(Integer.parseInt(tablePersonne.getModel().getValueAt(modelRow, tablePersonne.getSelectedColumn()).toString())));

        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_tablePersonneMouseClicked

    private void jTableUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableUserMouseClicked
        int viewRow = jTableUser.getSelectedRow();
        int modelRow = jTableUser.convertRowIndexToModel(viewRow);

        // TODO add your handling code here:
        //System.out.println(jTableUser.getModel().getValueAt(jTableUser.getSelectedRow(), jTableUser.getSelectedColumn()));
        radioButAdm.setSelected(false);
        radioButCrea.setSelected(false);
        radioButJuge.setSelected(false);
        radioButJugeArb.setSelected(false);

        jTableUser.getColumnName(jTableUser.getSelectedRow());
        //   System.out.println((jTableUser.getModel().getValueAt(jTableUser.getSelectedRow(), jTableUser.getSelectedColumn())).toString());
        int id_personne = 0;

        if (jTableUser.getColumnModel().getColumn(jTableUser.getSelectedColumn()).getHeaderValue().equals("id_personne")) {
            try {
                id_personne = Integer.parseInt((jTableUser.getModel().getValueAt(modelRow, jTableUser.getSelectedColumn())).toString());
                textLogin.setText(utilisateur.getLogin(id_personne));
                String tabID[] = personne.getIdPersonne();
                int index = 0;

                for (int i = 0; i < tabID.length; i++) {

                    if (tabID[i].equals((jTableUser.getModel().getValueAt(modelRow, jTableUser.getSelectedColumn())).toString())) {
                        index = i;

                        break;

                    }
                }
                System.out.println("JE MAPELLE I " + index);

                listPersonne.setSelectedIndex(index);

                System.out.println(listPersonne.getSelectedIndex());
                if (utilisateur.isEstAdmin(id_personne)) {
                    radioButAdm.setSelected(true);
                } else if (utilisateur.isEstCreateurCompet(id_personne)) {
                    radioButCrea.setSelected(true);
                } else if (utilisateur.isJugeArb(id_personne)) {
                    radioButJugeArb.setSelected(true);
                } else {
                    radioButJuge.setSelected(true);
                }

                //  refresh();
            } catch (SQLException ex) {
                Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //textLogin.setText(utilisateur.getLogin());

    }//GEN-LAST:event_jTableUserMouseClicked

    private void jTabbedPane3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane3MouseClicked
        // TODO add your handling code here:
        Date actuelle = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dat = dateFormat.format(actuelle);
        textdateCreationPersonne.setText(dat);
    }//GEN-LAST:event_jTabbedPane3MouseClicked

    private void textdateCreationPersonneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textdateCreationPersonneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textdateCreationPersonneActionPerformed

    private void textdateDeNaissancePersonneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textdateDeNaissancePersonneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textdateDeNaissancePersonneActionPerformed

    private void updateUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateUserActionPerformed
        try {
            // TODO add your handling code here:
            int index = 0;

            int estadmin = 0;
            int estCreateurCompet = 0;
            String allPersonne[] = personne.getAllPersonne();
            String allIdPersonne[] = personne.getIdPersonne();
            for (int i = 0; i < allPersonne.length; i++) {
                if (allPersonne[i].equals(listPersonne.getSelectedItem())) {
                    index = i;
                    break;
                }

            }
            if (radioButAdm.isSelected()) {
                estadmin = 1;
            }
            if (radioButCrea.isSelected()) {
                estCreateurCompet = 1;
            }
            if (radioButJuge.isSelected()) {
                juge.setRang(Integer.parseInt(boxRang.getSelectedItem().toString()), Integer.parseInt(allIdPersonne[index]));
            }
            if (radioButJugeArb.isSelected()) {
                juge.setEstArbitre(1, Integer.parseInt(allIdPersonne[index]));
            }

            utilisateur.setLogin(textLogin.getText(), Integer.parseInt(allIdPersonne[index]));
            utilisateur.setPasswd(textPass.getText(), Integer.parseInt(allIdPersonne[index]));
            utilisateur.setEstAdmin(estadmin, Integer.parseInt(allIdPersonne[index]));
            utilisateur.setEstCreateurCompet(estCreateurCompet, Integer.parseInt(allIdPersonne[index]));
            Object test = listPersonne.getSelectedItem();

            refresh();
            listPersonne.setSelectedItem(test);
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_updateUserActionPerformed

    private void radioButJugeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButJugeActionPerformed
        try {
            // TODO add your handling code here:

            if (radioButJuge.isSelected() == true) {
                radioButJugeArb.setSelected(false);
            }
            Object test = listPersonne.getSelectedItem();

            refresh();
            listPersonne.setSelectedItem(test);
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_radioButJugeActionPerformed

    private void radioButAdmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButAdmActionPerformed
        try {
            // TODO add your handling code here:
            Object test = listPersonne.getSelectedItem();

            refresh();
            listPersonne.setSelectedItem(test);
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_radioButAdmActionPerformed

    private void radioButJugeArbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButJugeArbActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            if (radioButJugeArb.isSelected() == true) {
                radioButJuge.setSelected(false);
            }
            Object test = listPersonne.getSelectedItem();

            refresh();
            listPersonne.setSelectedItem(test);

        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_radioButJugeArbActionPerformed

    private void radioButCreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButCreaActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            Object test = listPersonne.getSelectedItem();

            refresh();
            listPersonne.setSelectedItem(test);
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_radioButCreaActionPerformed

    private void listePersonneForNageuseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listePersonneForNageuseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_listePersonneForNageuseActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void ButAddNagActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButAddNagActionPerformed
        try {
            // TODO add your handling code here:
            int index = 0;
            String allPersonne[] = personne.getAllPersonne();
            String allIdPersonne[] = personne.getIdPersonne();
            for (int i = 0; i < allPersonne.length; i++) {
                if (allPersonne[i].equals(listePersonneForNageuse.getSelectedItem())) {
                    index = i;
                    break;
                }

            }
            System.out.println(allIdPersonne[index]);
            Nageuse newNageuse = new Nageuse(Integer.parseInt(allIdPersonne[index]));
            refresh();
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_ButAddNagActionPerformed
    private void filter(String query, DefaultTableModel dm, JTable jtable) {

        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(dm);

        jtable.setRowSorter(tr);

        tr.setRowFilter(RowFilter.regexFilter(query));

    }
    private void textFindUtilisateurKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFindUtilisateurKeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_textFindUtilisateurKeyPressed

    private void textFindUtilisateurKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFindUtilisateurKeyReleased
        // TODO add your handling code here:
        String query = textFindUtilisateur.getText();
        filter(query, dataModelUser, jTableUser);
    }//GEN-LAST:event_textFindUtilisateurKeyReleased

    private void textFindPersonneKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFindPersonneKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_textFindPersonneKeyPressed

    private void textFindPersonneKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFindPersonneKeyReleased
        // TODO add your handling code here:
        String query = textFindPersonne.getText();
        filter(query, dataModelPersonne, tablePersonne);

    }//GEN-LAST:event_textFindPersonneKeyReleased

    private void jTableUserMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableUserMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableUserMouseEntered

    private void textFindClubKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFindClubKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_textFindClubKeyPressed

    private void textFindClubKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFindClubKeyReleased
        // TODO add your handling code here:
        String query = textFindClub.getText();
        filter(query, dataModelClub, jtableClub);
    }//GEN-LAST:event_textFindClubKeyReleased

    private void textFindCompetKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFindCompetKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_textFindCompetKeyPressed

    private void textFindCompetKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFindCompetKeyReleased
        // TODO add your handling code here:
        String query = textFindCompet.getText();
        filter(query, dataModelCompet, jTableCompet);
    }//GEN-LAST:event_textFindCompetKeyReleased

    private void textFindEquipeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFindEquipeKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_textFindEquipeKeyPressed

    private void textFindEquipeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFindEquipeKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_textFindEquipeKeyReleased

    private void textdateCreationCompetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textdateCreationCompetActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textdateCreationCompetActionPerformed

    private void textdateCreationClubActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textdateCreationClubActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textdateCreationClubActionPerformed

    private void textdateCreationEquipeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textdateCreationEquipeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textdateCreationEquipeActionPerformed

    private void buttonCreateEquipeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCreateEquipeActionPerformed
        try {
            // TODO add your handling code here:

            int indexClub = 0;
            int indexCompet = 0;
            String allClub[] = club.getAllClub();
            String allIdClub[] = club.getIdClub();

            String allCompet[] = compet.getAllCompet();
            String allIdCompet[] = compet.getIdCompet();
            for (int i = 0; i < allClub.length; i++) {
                if (allClub[i].equals(listPersonne.getSelectedItem())) {
                    indexClub = i;
                    break;
                }

            }

            for (int i = 0; i < allCompet.length; i++) {
                if (allCompet[i].equals(listPersonne.getSelectedItem())) {
                    indexCompet = i;
                    break;
                }

            }
            int penalite = 0;
            if (boxVisible.getSelectedItem() == "oui") {
                penalite = 1;
            }

            Equipe newEquipe = new Equipe(textNomEquipe.getText(), Integer.parseInt(textNumPassage.getText()), Integer.parseInt((boxPenaliteEquipe.getSelectedItem()).toString()), penalite, textdateCreationEquipe.getText(), Integer.parseInt(allIdClub[indexClub]), Integer.parseInt(allIdCompet[indexCompet]));
            Object CompetEquipe = boxCompetForEquipe.getSelectedItem();
            Object CompetClub = boxClubForEquipe.getSelectedItem();
            refresh();
            boxCompetForEquipe.setSelectedItem(CompetEquipe);
            boxClubForEquipe.setSelectedItem(CompetClub);
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_buttonCreateEquipeActionPerformed

    private void buttonDeleteEquipeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDeleteEquipeActionPerformed
        try {
            // TODO add your handling code here:
            int viewRow = jTableEquipe.getSelectedRow();
            int modelRow = jTableEquipe.convertRowIndexToModel(viewRow);

            int id_equipe = Integer.parseInt(jTableEquipe.getValueAt(modelRow, jTableEquipe.getSelectedColumn()).toString());
            equipe.deleteEquipe(id_equipe);
            refresh();
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_buttonDeleteEquipeActionPerformed

    private void buttonUpdateEquipeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonUpdateEquipeActionPerformed
        try {
            // TODO add your handling code here:

            int viewRow = jTableEquipe.getSelectedRow();
            int modelRow = jTableEquipe.convertRowIndexToModel(viewRow);

            int id_equipe = Integer.parseInt(jTableEquipe.getValueAt(modelRow, jTableEquipe.getSelectedColumn()).toString());
            equipe.setDateCreation(textdateCreationEquipe.getText(), id_equipe);
            equipe.setNom_equipe(textNomEquipe.getText(), id_equipe);
            equipe.setNum_passage(Integer.parseInt(textNumPassage.getText()), id_equipe);
            equipe.setPenalite(Integer.parseInt(boxPenaliteEquipe.getSelectedItem().toString()), id_equipe);

            String[] allClub = club.getAllClub();
            String[] allIdClub = club.getIdClub();
            int index_club = 0;

            String[] allCompet = club.getAllClub();
            String[] allIdCompet = club.getIdClub();
            int index_equipe = 0;

            for (int i = 0; i < allClub.length; i++) {
                if (allClub[i].equals(boxClubForEquipe.getSelectedItem())) {
                    index_club = i;
                    break;
                }

            }
            for (int i = 0; i < allCompet.length; i++) {
                if (allCompet[i].equals(boxCompetForEquipe.getSelectedItem())) {
                    index_equipe = i;
                    break;
                }

            }

            equipe.setId_club(Integer.parseInt(allIdClub[index_club]), id_equipe);
            equipe.setId_club(Integer.parseInt(allIdClub[index_equipe]), id_equipe);

            if ("oui".equals(boxVisible.getSelectedItem().toString())) {
                equipe.setVisible(0, id_equipe);
            } else {
                equipe.setVisible(1, id_equipe);
            }
            Object compet = boxCompetForEquipe.getSelectedItem();
            Object club = boxClubForEquipe.getSelectedItem();
            refresh();
            boxCompetForEquipe.setSelectedItem(compet);
            boxCompetForEquipe.setSelectedItem(club);
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_buttonUpdateEquipeActionPerformed

    private void buttonCreateEquipe1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCreateEquipe1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buttonCreateEquipe1ActionPerformed

    private void textdateCreationClub1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textdateCreationClub1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textdateCreationClub1ActionPerformed

    private void jTableNageuseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableNageuseMouseClicked
        try {
            int viewRow = jTableNageuse.getSelectedRow();
            int modelRow = jTableNageuse.convertRowIndexToModel(viewRow);
            int indexPers = 0;
            String tabIdPers[] = personne.getIdPersonne();
            for (int i = 0; i < tabIdPers.length; i++) {

                if (tabIdPers[i].equals((jTableNageuse.getModel().getValueAt(modelRow, jTableNageuse.getSelectedColumn())).toString())) {
                    indexPers = i;

                    break;

                }
            }
            String tabIdNag[] = nageuse.getIdNageuse();
            int indexnag = 0;
            for (int i = 0; i < tabIdNag.length; i++) {

                if (tabIdNag[i].equals((jTableNageuse.getModel().getValueAt(modelRow, jTableNageuse.getSelectedColumn())).toString())) {
                    indexnag = i;
                    break;

                }
            }

            listePersonneForNageuse.setSelectedIndex(indexPers);
            boxNaj.setSelectedIndex(indexnag);

        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jTableNageuseMouseClicked

    private void jButtonPenaliteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPenaliteActionPerformed
        // TODO add your handling code here:
        try {
            String var_string = boxClubForPen.getSelectedItem().toString();
            String var_modif = var_string.substring(0, 1);
            int equipe_int = Integer.parseInt(var_modif);

            double penalite = Double.parseDouble(valeur_penalite.getSelectedItem().toString());
            equipe.setPenalite(penalite, equipe_int);
            refresh();
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }
//        equipe.setPenalite(HEIGHT, SOMEBITS);
    }//GEN-LAST:event_jButtonPenaliteActionPerformed

    private void boxClubForEquipeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxClubForEquipeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_boxClubForEquipeActionPerformed

    private void boxClubForPenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxClubForPenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_boxClubForPenActionPerformed

    private void butDeleteNajActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butDeleteNajActionPerformed
        try {
            // TODO add your handling code here:
            int index = 0;
            String allPersonne[] = personne.getAllPersonne();
            String allIdPersonne[] = personne.getIdPersonne();
            for (int i = 0; i < allPersonne.length; i++) {
                if (allPersonne[i].equals(listePersonneForNageuse.getSelectedItem())) {
                    index = i;
                    break;
                }

            }
            System.out.println(allIdPersonne[index]);
            nageuse.deleteNageuse(Integer.parseInt(allIdPersonne[index]));
            //Nageuse newNageuse = new Nageuse(Integer.parseInt(allIdPersonne[index]));
            refresh();
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_butDeleteNajActionPerformed

    private void boxEquipeNajActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxEquipeNajActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_boxEquipeNajActionPerformed

    private void boxClubForLancActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxClubForLancActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_boxClubForLancActionPerformed

    private void butonLanceBalletActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butonLanceBalletActionPerformed
        try {
            // TODO add your handling code here:
            //System.out.println(boxClubForLanc.getSelectedItem().toString());
            String id_String = boxClubForLanc.getSelectedItem().toString();
            id_String = id_String.substring(0, 1);
            int id_equipe = Integer.parseInt(id_String);
            equipe.setVisible(1, id_equipe);
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_butonLanceBalletActionPerformed

    private void valideNoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_valideNoteActionPerformed
        // TODO add your handling code here:
        //  int note, int id_personne, int id_equipe
        try {

            for (int i = 1; i <= 5; i++) {
                note.setNote(Integer.parseInt(textJugeNote1.getText()), equipe.getNoteEquipe(i), equipe.getNoteEquipe(i));
            }
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_valideNoteActionPerformed

    private void listPersonneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listPersonneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_listPersonneActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NatationSynchronisee.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NatationSynchronisee.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NatationSynchronisee.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NatationSynchronisee.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new NatationSynchronisee().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddUser;
    private javax.swing.JButton ButAddNag;
    private javax.swing.JSlider SliderNote;
    private javax.swing.JComboBox<String> boxClubForEquipe;
    private javax.swing.JComboBox<String> boxClubForEquipe1;
    private javax.swing.JComboBox<String> boxClubForLanc;
    private javax.swing.JComboBox<String> boxClubForPen;
    private javax.swing.JComboBox<String> boxCompetForEquipe;
    private javax.swing.JComboBox<String> boxCompetForEquipe1;
    private javax.swing.JComboBox<String> boxEquipeNaj;
    private javax.swing.JComboBox<String> boxNaj;
    private javax.swing.JComboBox<String> boxPenaliteEquipe;
    private javax.swing.JComboBox<String> boxRang;
    private javax.swing.JComboBox<String> boxVisible;
    private javax.swing.JToggleButton butDeleteNaj;
    private javax.swing.JButton butonLanceBallet;
    private javax.swing.JButton buttonAddUser;
    private javax.swing.JButton buttonCreateClub;
    private javax.swing.JButton buttonCreateCompet;
    private javax.swing.JButton buttonCreateEquipe;
    private javax.swing.JButton buttonCreateEquipe1;
    private javax.swing.JButton buttonDeleteClub;
    private javax.swing.JButton buttonDeleteEquipe;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton buttonNoterJuge;
    private javax.swing.JButton buttonSupprimerCompet;
    private javax.swing.JButton buttonUpdateClub;
    private javax.swing.JButton buttonUpdateCompet;
    private javax.swing.JButton buttonUpdateEquipe;
    private javax.swing.JButton deletePersonne;
    private javax.swing.JButton deleteUser;
    private javax.swing.JTextField fieldNomClub;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButtonPenalite;
    private javax.swing.JComboBox<String> jComboBox9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTable jTableCompet;
    private javax.swing.JTable jTableEquipe;
    private javax.swing.JTable jTableNageuse;
    private javax.swing.JTable jTableNote;
    private javax.swing.JTable jTableUser;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JTextField jTextField25;
    private javax.swing.JTextField jTextField26;
    private javax.swing.JTextField jTextField27;
    private javax.swing.JTextField jTextField28;
    private javax.swing.JTextField jTextField29;
    private javax.swing.JTextField jTextField30;
    private javax.swing.JTextField jTextField31;
    private javax.swing.JTextField jTextField32;
    private javax.swing.JTextField jTextField33;
    private javax.swing.JTextField jTextField34;
    private javax.swing.JTable jtableClub;
    private javax.swing.JLabel labelID;
    private javax.swing.JLabel labelNomJuge;
    private javax.swing.JLabel labelNote;
    private javax.swing.JLabel labelRangJuge;
    private javax.swing.JComboBox<String> listPersonne;
    private javax.swing.JComboBox<String> listPersonneClub;
    private javax.swing.JComboBox<String> listePersonneForNageuse;
    private javax.swing.JPanel panelAdmin;
    private javax.swing.JPanel panelGlobal;
    private javax.swing.JPanel panelJuge;
    private javax.swing.JRadioButton radioButAdm;
    private javax.swing.JRadioButton radioButCrea;
    private javax.swing.JRadioButton radioButJuge;
    private javax.swing.JRadioButton radioButJugeArb;
    private javax.swing.JButton stopBallet;
    private javax.swing.JTable tablePersonne;
    private javax.swing.JTextField textFindClub;
    private javax.swing.JTextField textFindCompet;
    private javax.swing.JTextField textFindEquipe;
    private javax.swing.JTextField textFindPersonne;
    private javax.swing.JTextField textFindUtilisateur;
    private javax.swing.JTextField textJugeNote1;
    private javax.swing.JTextField textJugeNote2;
    private javax.swing.JTextField textJugeNote3;
    private javax.swing.JTextField textJugeNote4;
    private javax.swing.JTextField textJugeNote5;
    private javax.swing.JTextField textLieuCompet;
    private javax.swing.JTextField textLogin;
    private javax.swing.JTextField textNomEquipe;
    private javax.swing.JTextField textNumPassage;
    private javax.swing.JPasswordField textPass;
    private javax.swing.JTextField textTitreCompet;
    private javax.swing.JFormattedTextField textdateCreationClub;
    private javax.swing.JFormattedTextField textdateCreationClub1;
    private javax.swing.JFormattedTextField textdateCreationCompet;
    private javax.swing.JFormattedTextField textdateCreationEquipe;
    private javax.swing.JFormattedTextField textdateCreationPersonne;
    private javax.swing.JFormattedTextField textdateDeNaissancePersonne;
    private javax.swing.JTextField textnomPersonne;
    private javax.swing.JTextField textprenomPersonne;
    private javax.swing.JButton updatePersonne;
    private javax.swing.JButton updateUser;
    private javax.swing.JComboBox<String> valeur_penalite;
    private javax.swing.JButton valideNote;
    // End of variables declaration//GEN-END:variables
}
