/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NatationSynchronisee;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
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

import java.util.Timer;
import java.util.TimerTask;

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

    public void TimerStart() throws SQLException {
        TimerTask task = new RunMeTask(labelEquipe, buttonNoterJuge, jTableNote);
        Timer timer = new Timer();
        timer.schedule(task, 0, 1000);
        buttonNoterJuge.setEnabled(false);
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

        labelEquipe.setText(equipe.getEquipeEncours());

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
                        TimerStart();
                        fenetre.dispose();

                        login(login.getText());
                        personne.setInfos(login.getText());

                        labelNomJuge.setText("Bonjour " + personne.getNomAuth() + " " + personne.getPrenomAuth() + " n°" + juge.getRang(personne.getId_personne()));
                        jFrame1.setAlwaysOnTop(true);
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
                // System.out.println("caca");
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

        jFrame1 = new javax.swing.JFrame();
        jScrollPane1 = new javax.swing.JScrollPane();
        labMentionLegale = new javax.swing.JTextArea();
        panelGlobal = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        panelJuge = new javax.swing.JPanel();
        labelNomJuge = new javax.swing.JLabel();
        labelEquipe = new javax.swing.JLabel();
        buttonNoterJuge = new javax.swing.JButton();
        labelNote = new javax.swing.JLabel();
        SliderNote = new javax.swing.JSlider();
        refresh = new javax.swing.JToggleButton();
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
        AfficheResultat = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        butonLanceBallet = new javax.swing.JButton();
        stopBallet = new javax.swing.JButton();
        boxClubForLanc = new javax.swing.JComboBox<>(EquipeForNageuse());
        jTextField2 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        boxClubForAffichNote = new javax.swing.JComboBox<>(EquipeForNageuse());
        StopResult = new javax.swing.JButton();
        refeshJA = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel10 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jTextPrenomNageuse = new javax.swing.JTextField();
        jTextNomNageuse = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        CreerNageuse = new javax.swing.JButton();
        //DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try{
            jTextDateCreationNageuse = new javax.swing.JFormattedTextField(new MaskFormatter("####-##-##"));
            //DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            try{
                jTextDateNaissNageuse = new javax.swing.JFormattedTextField(new MaskFormatter("####-##-##"));
                jPanel16 = new javax.swing.JPanel();
                jLabel41 = new javax.swing.JLabel();
                jLabel42 = new javax.swing.JLabel();
                jLabel43 = new javax.swing.JLabel();
                jLabel44 = new javax.swing.JLabel();
                jTextLieu = new javax.swing.JTextField();
                jTextTitre = new javax.swing.JTextField();
                CreerCompet = new javax.swing.JButton();
                //DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                try{
                    jTextDate = new javax.swing.JFormattedTextField(new MaskFormatter("####-##-##"));
                    //DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    try{
                        jTextFinDate = new javax.swing.JFormattedTextField(new MaskFormatter("####-##-##"));
                        jLabel64 = new javax.swing.JLabel();
                        jPanel18 = new javax.swing.JPanel();
                        jLabel58 = new javax.swing.JLabel();
                        jLabel59 = new javax.swing.JLabel();
                        jTextClub = new javax.swing.JTextField();
                        jLabel60 = new javax.swing.JLabel();
                        CreerClub = new javax.swing.JButton();
                        //DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            textdateCreationClub1 = new javax.swing.JFormattedTextField(new MaskFormatter("####-##-##"));
                            jLabel79 = new javax.swing.JLabel();
                            jComboBoxDirigeant = new javax.swing.JComboBox<>(personneForUser());
                            jPanel17 = new javax.swing.JPanel();
                            jLabel50 = new javax.swing.JLabel();
                            jLabel51 = new javax.swing.JLabel();
                            jLabel52 = new javax.swing.JLabel();
                            jLabel53 = new javax.swing.JLabel();
                            jLabel55 = new javax.swing.JLabel();
                            jLabel56 = new javax.swing.JLabel();
                            jLabel57 = new javax.swing.JLabel();
                            jTextNumeroPassage = new javax.swing.JTextField();
                            jTextEquipe = new javax.swing.JTextField();
                            boxCompetForEquipe1 = new javax.swing.JComboBox<>(competForEquipe());
                            boxClubForEquipe1 = new javax.swing.JComboBox<>(clubForEquipe());
                            buttonCreateEquipe1 = new javax.swing.JButton();
                            boxPenaliteEquipe1 = new javax.swing.JComboBox<>();
                            //DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                            try{
                                jTextDateCreation = new javax.swing.JFormattedTextField(new MaskFormatter("####-##-##"));
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
                                updateUser1 = new javax.swing.JButton();
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
                                            //DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                                            try{
                                                textdatefinCompet = new javax.swing.JFormattedTextField(new MaskFormatter("####-##-##"));
                                                jLabel54 = new javax.swing.JLabel();
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
                                                    RemoveNageuseEquipe = new javax.swing.JButton();
                                                    addNageuseEquipe = new javax.swing.JButton();
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
                                                        buttonDeleteClub1 = new javax.swing.JButton();

                                                        jFrame1.setTitle("Mentions Légales");
                                                        jFrame1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                                                        jFrame1.setMinimumSize(new java.awt.Dimension(914, 542));

                                                        labMentionLegale.setColumns(20);
                                                        labMentionLegale.setRows(5);
                                                        jScrollPane1.setViewportView(labMentionLegale);

                                                        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
                                                        jFrame1.getContentPane().setLayout(jFrame1Layout);
                                                        jFrame1Layout.setHorizontalGroup(
                                                            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addGroup(jFrame1Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 894, Short.MAX_VALUE)
                                                                .addContainerGap())
                                                        );
                                                        jFrame1Layout.setVerticalGroup(
                                                            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addGroup(jFrame1Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)
                                                                .addContainerGap())
                                                        );

                                                        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
                                                        setTitle("Natation Synchronisée");
                                                        setAlwaysOnTop(true);

                                                        panelGlobal.setBackground(new java.awt.Color(51, 102, 255));
                                                        panelGlobal.setBorder(new javax.swing.border.MatteBorder(null));

                                                        jLabel1.setText("FFN");

                                                        jTabbedPane2.setBackground(new java.awt.Color(102, 102, 255));

                                                        labelNomJuge.setBackground(new java.awt.Color(255, 255, 255));
                                                        labelNomJuge.setText("Juge n °x Dupond Dupont");

                                                        labelEquipe.setText("Equipe  x ");

                                                        buttonNoterJuge.setText("Noter");
                                                        buttonNoterJuge.addActionListener(new java.awt.event.ActionListener() {
                                                            public void actionPerformed(java.awt.event.ActionEvent evt) {
                                                                buttonNoterJugeActionPerformed(evt);
                                                            }
                                                        });

                                                        labelNote.setText("Note : ");

                                                        SliderNote.addChangeListener(new javax.swing.event.ChangeListener() {
                                                            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                                                                SliderNoteStateChanged(evt);
                                                            }
                                                        });

                                                        refresh.setText("Rafraichir");
                                                        refresh.addActionListener(new java.awt.event.ActionListener() {
                                                            public void actionPerformed(java.awt.event.ActionEvent evt) {
                                                                refreshActionPerformed(evt);
                                                            }
                                                        });

                                                        javax.swing.GroupLayout panelJugeLayout = new javax.swing.GroupLayout(panelJuge);
                                                        panelJuge.setLayout(panelJugeLayout);
                                                        panelJugeLayout.setHorizontalGroup(
                                                            panelJugeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addGroup(panelJugeLayout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(panelJugeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelJugeLayout.createSequentialGroup()
                                                                        .addGap(0, 175, Short.MAX_VALUE)
                                                                        .addComponent(labelNomJuge)
                                                                        .addGap(579, 579, 579))
                                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelJugeLayout.createSequentialGroup()
                                                                        .addComponent(refresh)
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addComponent(buttonNoterJuge)
                                                                        .addContainerGap())))
                                                            .addGroup(panelJugeLayout.createSequentialGroup()
                                                                .addGroup(panelJugeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                    .addGroup(panelJugeLayout.createSequentialGroup()
                                                                        .addGap(232, 232, 232)
                                                                        .addComponent(labelEquipe)
                                                                        .addGap(18, 18, 18)
                                                                        .addComponent(SliderNote, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                    .addGroup(panelJugeLayout.createSequentialGroup()
                                                                        .addGap(392, 392, 392)
                                                                        .addComponent(labelNote)))
                                                                .addGap(0, 260, Short.MAX_VALUE))
                                                        );
                                                        panelJugeLayout.setVerticalGroup(
                                                            panelJugeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addGroup(panelJugeLayout.createSequentialGroup()
                                                                .addGap(25, 25, 25)
                                                                .addComponent(labelNomJuge)
                                                                .addGap(87, 87, 87)
                                                                .addGroup(panelJugeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                    .addComponent(labelEquipe)
                                                                    .addComponent(SliderNote, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(18, 18, 18)
                                                                .addComponent(labelNote)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 206, Short.MAX_VALUE)
                                                                .addGroup(panelJugeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                    .addComponent(buttonNoterJuge)
                                                                    .addComponent(refresh))
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

                                                        AfficheResultat.setText("Afficher Résultats");
                                                        AfficheResultat.addActionListener(new java.awt.event.ActionListener() {
                                                            public void actionPerformed(java.awt.event.ActionEvent evt) {
                                                                AfficheResultatActionPerformed(evt);
                                                            }
                                                        });

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

                                                        jLabel5.setText("Durée ballet : ");

                                                        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
                                                        jPanel7.setLayout(jPanel7Layout);
                                                        jPanel7Layout.setHorizontalGroup(
                                                            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addGroup(jPanel7Layout.createSequentialGroup()
                                                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                                                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                            .addGroup(jPanel7Layout.createSequentialGroup()
                                                                                .addGap(114, 114, 114)
                                                                                .addComponent(jLabel40))
                                                                            .addGroup(jPanel7Layout.createSequentialGroup()
                                                                                .addContainerGap()
                                                                                .addComponent(boxClubForLanc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                        .addGap(0, 0, Short.MAX_VALUE))
                                                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                                                        .addContainerGap()
                                                                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                                                                .addComponent(stopBallet)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 92, Short.MAX_VALUE)
                                                                                .addComponent(butonLanceBallet))
                                                                            .addGroup(jPanel7Layout.createSequentialGroup()
                                                                                .addGap(0, 0, Short.MAX_VALUE)
                                                                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                                                        .addGap(0, 0, Short.MAX_VALUE)
                                                                        .addComponent(jLabel5)))
                                                                .addContainerGap())
                                                        );
                                                        jPanel7Layout.setVerticalGroup(
                                                            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addGroup(jPanel7Layout.createSequentialGroup()
                                                                .addGap(6, 6, 6)
                                                                .addComponent(jLabel40)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(jLabel5)
                                                                .addGap(18, 18, 18)
                                                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                    .addComponent(boxClubForLanc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(33, 33, 33)
                                                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                    .addComponent(butonLanceBallet)
                                                                    .addComponent(stopBallet))
                                                                .addContainerGap())
                                                        );

                                                        boxClubForAffichNote.addActionListener(new java.awt.event.ActionListener() {
                                                            public void actionPerformed(java.awt.event.ActionEvent evt) {
                                                                boxClubForAffichNoteActionPerformed(evt);
                                                            }
                                                        });

                                                        StopResult.setText("Ne plus afficher les Résultats");
                                                        StopResult.addActionListener(new java.awt.event.ActionListener() {
                                                            public void actionPerformed(java.awt.event.ActionEvent evt) {
                                                                StopResultActionPerformed(evt);
                                                            }
                                                        });

                                                        refeshJA.setText("Rafraichir");
                                                        refeshJA.addActionListener(new java.awt.event.ActionListener() {
                                                            public void actionPerformed(java.awt.event.ActionEvent evt) {
                                                                refeshJAActionPerformed(evt);
                                                            }
                                                        });

                                                        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                                                        jPanel1.setLayout(jPanel1Layout);
                                                        jPanel1Layout.setHorizontalGroup(
                                                            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                                        .addComponent(jLabel3)
                                                                        .addGap(498, 498, 498))
                                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                                        .addComponent(boxClubForAffichNote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addGap(18, 18, 18)
                                                                        .addComponent(StopResult)
                                                                        .addGap(4, 4, 4)
                                                                        .addComponent(AfficheResultat)
                                                                        .addContainerGap())
                                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                                        .addComponent(refeshJA)
                                                                        .addGap(18, 18, 18)
                                                                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addGap(28, 28, 28))))
                                                        );
                                                        jPanel1Layout.setVerticalGroup(
                                                            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                                                        .addContainerGap()
                                                                        .addComponent(jLabel3)
                                                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                                                .addGap(72, 72, 72)
                                                                                .addComponent(refeshJA)
                                                                                .addGap(0, 0, Short.MAX_VALUE))))
                                                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                                                        .addGap(47, 47, 47)
                                                                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
                                                                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                    .addComponent(AfficheResultat)
                                                                    .addComponent(boxClubForAffichNote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(StopResult))
                                                                .addGap(6, 6, 6))
                                                        );

                                                        jTabbedPane2.addTab("Juge arbitre", jPanel1);

                                                        jLabel4.setText("Créateur de compétition ");

                                                        jPanel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

                                                        jLabel45.setText("Créer Nageuse");

                                                        jLabel46.setText("Nom");

                                                        jLabel47.setText("Prénom");

                                                        jLabel48.setText("Date de Naissance");

                                                        jLabel49.setText("Date Création");

                                                        CreerNageuse.setText("Créer une nouvelle nageuse");
                                                        CreerNageuse.addActionListener(new java.awt.event.ActionListener() {
                                                            public void actionPerformed(java.awt.event.ActionEvent evt) {
                                                                CreerNageuseActionPerformed(evt);
                                                            }
                                                        });

                                                        textdateCreationPersonne.setFocusLostBehavior(textdateCreationPersonne.PERSIST);
                                                    }
                                                    catch(Exception e){}
                                                    jTextDateCreationNageuse.addActionListener(new java.awt.event.ActionListener() {
                                                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                                            jTextDateCreationNageuseActionPerformed(evt);
                                                        }
                                                    });

                                                    textdateCreationPersonne.setFocusLostBehavior(textdateCreationPersonne.PERSIST);
                                                }
                                                catch(Exception e){}
                                                jTextDateNaissNageuse.addActionListener(new java.awt.event.ActionListener() {
                                                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                                                        jTextDateNaissNageuseActionPerformed(evt);
                                                    }
                                                });

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
                                                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel10Layout.createSequentialGroup()
                                                                            .addComponent(jLabel46)
                                                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                            .addComponent(jTextNomNageuse, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel10Layout.createSequentialGroup()
                                                                            .addComponent(jLabel47)
                                                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 88, Short.MAX_VALUE)
                                                                            .addComponent(jTextPrenomNageuse, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                    .addGroup(jPanel10Layout.createSequentialGroup()
                                                                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                            .addComponent(jLabel49)
                                                                            .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                            .addGroup(jPanel10Layout.createSequentialGroup()
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(jTextDateNaissNageuse, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                .addComponent(jTextDateCreationNageuse, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGap(2, 2, 2)))))))
                                                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                                                        .addGap(0, 0, Short.MAX_VALUE)
                                                        .addComponent(CreerNageuse)
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
                                                            .addComponent(jTextNomNageuse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGap(18, 18, 18)
                                                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                            .addComponent(jLabel47)
                                                            .addComponent(jTextPrenomNageuse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGap(18, 18, 18)
                                                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                            .addComponent(jLabel49)
                                                            .addComponent(jTextDateCreationNageuse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGap(18, 18, 18)
                                                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                            .addComponent(jLabel48)
                                                            .addComponent(jTextDateNaissNageuse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 83, Short.MAX_VALUE)
                                                        .addComponent(CreerNageuse)
                                                        .addGap(48, 48, 48))
                                                );

                                                jTabbedPane1.addTab("Ajouter Nageuse ", jPanel10);

                                                jPanel16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

                                                jLabel41.setText("Créer compétition");

                                                jLabel42.setText("Titre");

                                                jLabel43.setText("Date Compétition");

                                                jLabel44.setText("Lieu Compétition ");

                                                jTextLieu.addActionListener(new java.awt.event.ActionListener() {
                                                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                                                        jTextLieuActionPerformed(evt);
                                                    }
                                                });

                                                CreerCompet.setText("Créer compétition");
                                                CreerCompet.addActionListener(new java.awt.event.ActionListener() {
                                                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                                                        CreerCompetActionPerformed(evt);
                                                    }
                                                });

                                                textdateCreationPersonne.setFocusLostBehavior(textdateCreationPersonne.PERSIST);
                                            }
                                            catch(Exception e){}
                                            jTextDate.addActionListener(new java.awt.event.ActionListener() {
                                                public void actionPerformed(java.awt.event.ActionEvent evt) {
                                                    jTextDateActionPerformed(evt);
                                                }
                                            });

                                            textdateCreationCompet.setFocusLostBehavior(textdateCreationCompet.PERSIST);
                                        }
                                        catch(Exception e){}
                                        jTextFinDate.addActionListener(new java.awt.event.ActionListener() {
                                            public void actionPerformed(java.awt.event.ActionEvent evt) {
                                                jTextFinDateActionPerformed(evt);
                                            }
                                        });

                                        jLabel64.setText("Fin Compétition");

                                        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
                                        jPanel16.setLayout(jPanel16Layout);
                                        jPanel16Layout.setHorizontalGroup(
                                            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel16Layout.createSequentialGroup()
                                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(jPanel16Layout.createSequentialGroup()
                                                        .addGap(284, 284, 284)
                                                        .addComponent(jLabel41))
                                                    .addGroup(jPanel16Layout.createSequentialGroup()
                                                        .addContainerGap()
                                                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(jLabel43)
                                                            .addComponent(jLabel42))
                                                        .addGap(30, 30, 30)
                                                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(jTextFinDate, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                .addComponent(jTextTitre, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                                                                .addComponent(jTextDate)))))
                                                .addContainerGap(328, Short.MAX_VALUE))
                                            .addGroup(jPanel16Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                                                        .addComponent(jLabel44)
                                                        .addGap(30, 30, 30)
                                                        .addComponent(jTextLieu, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(CreerCompet)
                                                        .addGap(41, 41, 41))
                                                    .addGroup(jPanel16Layout.createSequentialGroup()
                                                        .addComponent(jLabel64)
                                                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                        );
                                        jPanel16Layout.setVerticalGroup(
                                            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel16Layout.createSequentialGroup()
                                                .addGap(28, 28, 28)
                                                .addComponent(jLabel41)
                                                .addGap(38, 38, 38)
                                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(jLabel42)
                                                    .addComponent(jTextTitre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(jLabel43)
                                                    .addComponent(jTextDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(jLabel64)
                                                    .addComponent(jTextFinDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(21, 21, 21)
                                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(CreerCompet)
                                                    .addComponent(jLabel44)
                                                    .addComponent(jTextLieu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addContainerGap(135, Short.MAX_VALUE))
                                        );

                                        jTabbedPane1.addTab("Créer compétition", jPanel16);

                                        jPanel18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

                                        jLabel58.setText("Créer club");

                                        jLabel59.setText("Nom Club");

                                        jLabel60.setText("Nom Dirigeant");

                                        CreerClub.setText("Créer Club");
                                        CreerClub.addActionListener(new java.awt.event.ActionListener() {
                                            public void actionPerformed(java.awt.event.ActionEvent evt) {
                                                CreerClubActionPerformed(evt);
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

                                    jComboBoxDirigeant.addActionListener(new java.awt.event.ActionListener() {
                                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                            jComboBoxDirigeantActionPerformed(evt);
                                        }
                                    });

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
                                                        .addComponent(jTextClub, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jComboBoxDirigeant, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGroup(jPanel18Layout.createSequentialGroup()
                                                    .addGap(283, 283, 283)
                                                    .addComponent(jLabel58)))
                                            .addGap(0, 365, Short.MAX_VALUE))
                                        .addGroup(jPanel18Layout.createSequentialGroup()
                                            .addContainerGap()
                                            .addComponent(jLabel79)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(textdateCreationClub1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(CreerClub)
                                            .addContainerGap())
                                    );
                                    jPanel18Layout.setVerticalGroup(
                                        jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel18Layout.createSequentialGroup()
                                            .addContainerGap()
                                            .addComponent(jLabel58)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel59)
                                                .addComponent(jTextClub, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(18, 18, 18)
                                            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel60)
                                                .addComponent(jComboBoxDirigeant, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(18, 18, 18)
                                            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel79)
                                                .addComponent(textdateCreationClub1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 181, Short.MAX_VALUE)
                                            .addComponent(CreerClub)
                                            .addGap(19, 19, 19))
                                    );

                                    jTabbedPane1.addTab("Créer club", jPanel18);

                                    jPanel17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

                                    jLabel50.setText("Créer équipe");

                                    jLabel51.setText("Nom équipe : ");

                                    jLabel52.setText("Numéro passage :");

                                    jLabel53.setText("Pénalité :");

                                    jLabel55.setText("Date Création :");

                                    jLabel56.setText("Compétition correspondante :");

                                    jLabel57.setText("Club correspondant :");

                                    boxClubForEquipe1.addActionListener(new java.awt.event.ActionListener() {
                                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                            boxClubForEquipe1ActionPerformed(evt);
                                        }
                                    });

                                    buttonCreateEquipe1.setText("Créer équipe");
                                    buttonCreateEquipe1.addActionListener(new java.awt.event.ActionListener() {
                                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                            buttonCreateEquipe1ActionPerformed(evt);
                                        }
                                    });

                                    boxPenaliteEquipe1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0", "0.5", "1", "2" }));
                                    boxPenaliteEquipe1.addActionListener(new java.awt.event.ActionListener() {
                                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                            boxPenaliteEquipe1ActionPerformed(evt);
                                        }
                                    });

                                    textdateCreationCompet.setFocusLostBehavior(textdateCreationCompet.PERSIST);
                                }
                                catch(Exception e){}
                                jTextDateCreation.addActionListener(new java.awt.event.ActionListener() {
                                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                                        jTextDateCreationActionPerformed(evt);
                                    }
                                });

                                javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
                                jPanel17.setLayout(jPanel17Layout);
                                jPanel17Layout.setHorizontalGroup(
                                    jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel17Layout.createSequentialGroup()
                                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel17Layout.createSequentialGroup()
                                                .addGap(78, 78, 78)
                                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(jLabel53)
                                                    .addComponent(jLabel52)
                                                    .addComponent(jLabel51)
                                                    .addComponent(jLabel55))
                                                .addGap(37, 37, 37)
                                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(boxPenaliteEquipe1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jTextNumeroPassage, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jTextEquipe, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jTextDateCreation, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(jPanel17Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel57))
                                                .addGap(38, 38, 38)
                                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(boxCompetForEquipe1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(boxClubForEquipe1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                                        .addGap(0, 322, Short.MAX_VALUE)
                                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                                                .addComponent(jLabel50)
                                                .addGap(301, 301, 301))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                                                .addComponent(buttonCreateEquipe1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(20, 20, 20))))
                                );
                                jPanel17Layout.setVerticalGroup(
                                    jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel17Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jLabel50)
                                        .addGap(32, 32, 32)
                                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel17Layout.createSequentialGroup()
                                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(jTextEquipe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel51))
                                                .addGap(18, 18, 18)
                                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(jTextNumeroPassage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel52))
                                                .addGap(18, 18, 18)
                                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(boxPenaliteEquipe1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel53))
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel55)
                                                .addGap(18, 18, 18)
                                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(jLabel56)
                                                    .addComponent(boxCompetForEquipe1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(jPanel17Layout.createSequentialGroup()
                                                .addComponent(jTextDateCreation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(35, 35, 35)))
                                        .addGap(17, 17, 17)
                                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(boxClubForEquipe1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel57))
                                        .addGap(18, 18, 18)
                                        .addComponent(buttonCreateEquipe1)
                                        .addContainerGap(34, Short.MAX_VALUE))
                                );

                                jTabbedPane1.addTab("Créer équipe", jPanel17);

                                javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
                                jPanel3.setLayout(jPanel3Layout);
                                jPanel3Layout.setHorizontalGroup(
                                    jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(374, 374, 374)
                                        .addComponent(jLabel4)
                                        .addContainerGap(395, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 704, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(71, 71, 71))
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
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addContainerGap(76, Short.MAX_VALUE)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                                .addComponent(jButton6)
                                                .addGap(34, 34, 34))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                                .addComponent(jLabel19)
                                                .addGap(411, 411, 411))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 756, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(56, 56, 56))))
                                );
                                jPanel4Layout.setVerticalGroup(
                                    jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addContainerGap(12, Short.MAX_VALUE)
                                        .addComponent(jLabel19)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
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

                                updateUser1.setText("Modifier Mot de passe");

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
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                                                        .addComponent(textFindUtilisateur, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(22, 22, 22))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                                                        .addComponent(jLabel72)
                                                        .addGap(12, 12, 12))))
                                            .addGroup(jPanel12Layout.createSequentialGroup()
                                                .addGap(132, 132, 132)
                                                .addComponent(boxRang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel12Layout.createSequentialGroup()
                                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(buttonAddUser, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                                                    .addComponent(updateUser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                                                        .addComponent(textPass)))))
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
                                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(updateUser)
                                                    .addComponent(updateUser1))))
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

                    textdateCreationCompet.setFocusLostBehavior(textdateCreationCompet.PERSIST);
                }
                catch(Exception e){}
                textdatefinCompet.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        textdatefinCompetActionPerformed(evt);
                    }
                });

                jLabel54.setText("Fin Compétition");

                javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
                jPanel13.setLayout(jPanel13Layout);
                jPanel13Layout.setHorizontalGroup(
                    jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel13Layout.createSequentialGroup()
                                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel36)
                                            .addComponent(jLabel35))
                                        .addGap(30, 30, 30)
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
                                                .addGap(12, 12, 12))))
                                    .addGroup(jPanel13Layout.createSequentialGroup()
                                        .addGap(191, 191, 191)
                                        .addComponent(jLabel34)
                                        .addGap(18, 18, 18))))
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel13Layout.createSequentialGroup()
                                        .addGap(38, 38, 38)
                                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(buttonUpdateCompet, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(buttonSupprimerCompet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(buttonCreateCompet, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(jPanel13Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel39)
                                            .addComponent(jLabel54))
                                        .addGap(29, 29, 29)
                                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(textdatefinCompet)
                                            .addComponent(textLieuCompet, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 263, Short.MAX_VALUE)))
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))
                );
                jPanel13Layout.setVerticalGroup(
                    jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                        .addContainerGap(17, Short.MAX_VALUE)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
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
                        .addGap(17, 17, 17)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel54)
                            .addComponent(textdatefinCompet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel39)
                            .addComponent(textLieuCompet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonCreateCompet)
                        .addGap(18, 18, 18)
                        .addComponent(buttonUpdateCompet)
                        .addGap(18, 18, 18)
                        .addComponent(buttonSupprimerCompet)
                        .addGap(20, 20, 20))
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

                listPersonneClub.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        listPersonneClubActionPerformed(evt);
                    }
                });

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
                                    .addGap(31, 31, 31)
                                    .addComponent(textdateCreationClub))
                                .addGroup(jPanel14Layout.createSequentialGroup()
                                    .addGap(0, 29, Short.MAX_VALUE)
                                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(listPersonneClub, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(fieldNomClub, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))))
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

            RemoveNageuseEquipe.setText("Enlever Nageuse");
            RemoveNageuseEquipe.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    RemoveNageuseEquipeActionPerformed(evt);
                }
            });

            addNageuseEquipe.setText("Ajouter nageuse");
            addNageuseEquipe.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    addNageuseEquipeActionPerformed(evt);
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
                                    .addComponent(addNageuseEquipe)
                                    .addGap(51, 51, 51)
                                    .addComponent(RemoveNageuseEquipe)))
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
                        .addComponent(RemoveNageuseEquipe)
                        .addComponent(addNageuseEquipe))
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
            boxPenaliteEquipe.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    boxPenaliteEquipeActionPerformed(evt);
                }
            });

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
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Admin", panelAdmin);

        buttonDeleteClub1.setText("Voir les mentions légales");
        buttonDeleteClub1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDeleteClub1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelGlobalLayout = new javax.swing.GroupLayout(panelGlobal);
        panelGlobal.setLayout(panelGlobalLayout);
        panelGlobalLayout.setHorizontalGroup(
            panelGlobalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGlobalLayout.createSequentialGroup()
                .addGroup(panelGlobalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelGlobalLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(panelGlobalLayout.createSequentialGroup()
                        .addGap(431, 431, 431)
                        .addComponent(jLabel1)
                        .addGap(304, 304, 304)
                        .addComponent(buttonDeleteClub1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelGlobalLayout.setVerticalGroup(
            panelGlobalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGlobalLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(panelGlobalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(buttonDeleteClub1))
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

    public String[] personneForDirigeant() {
        String[] valeurs = null;
        try {
            valeurs = club.getAllDirigeant();

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
            if (radioButJuge.isSelected() == false && radioButJugeArb.isSelected() == false) {
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

            if (radioButJuge.isSelected() == true || radioButJugeArb.isSelected() == true) {
                juge.deleteJuge(Integer.parseInt(allIdPersonne[index]));
            }
            refresh();
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_deleteUserActionPerformed

    private void buttonCreateCompetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCreateCompetActionPerformed
        try {
            // TODO add your handling code here:
            Competition newCompet = new Competition(textTitreCompet.getText(), textdateCreationCompet.getText(), textdatefinCompet.getText(), textLieuCompet.getText());
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

    private void CreerCompetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CreerCompetActionPerformed
        try {
            // TODO add your handling code here:
            Competition newCompetition = new Competition(jTextTitre.getText(), jTextDate.getText(), jTextFinDate.getText(), jTextLieu.getText());
            refresh();
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_CreerCompetActionPerformed

    private void CreerClubActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CreerClubActionPerformed
        try {
            // TODO add your handling code here:
            int index = 0;
            String allPersonne[] = club.getAllDirigeant();
            String allIdPersonne[] = club.getIdDirigeant();
            for (int i = 0; i < allPersonne.length; i++) {
                if (allPersonne[i].equals(jComboBoxDirigeant.getSelectedItem())) {
                    index = i;
                    break;
                }

            }
            Club newClub = new Club(jTextClub.getText(), Integer.parseInt(allIdPersonne[index]), textdateCreationClub1.getText());
            Object test = jComboBoxDirigeant.getSelectedItem();

            refresh();
            jComboBoxDirigeant.setSelectedItem(test);
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_CreerClubActionPerformed

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
            textdatefinCompet.setText(compet.getfin_Date_Compet(id_compet));
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
                //   System.out.println(tabCompetID[i]);
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
                //System.out.println("JE MAPELLE I " + index);

                listPersonne.setSelectedIndex(index);

                //System.out.println(listPersonne.getSelectedIndex());
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
            //        System.out.println("MDP !!" + textPass.getText());

            utilisateur.setEstAdmin(estadmin, Integer.parseInt(allIdPersonne[index]));
            utilisateur.setEstCreateurCompet(estCreateurCompet, Integer.parseInt(allIdPersonne[index]));
            //utilisateur.setPasswd(textLogin.getText(), Integer.parseInt(allIdPersonne[index]));
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

    private void RemoveNageuseEquipeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RemoveNageuseEquipeActionPerformed
        try {
            // TODO add your handling code here:
            Compose remove = new Compose();
            String CompetNaj = boxNaj.getSelectedItem().toString();
            int id_Naj = Integer.parseInt(CompetNaj.substring(0, 1));
            remove.deleteCompose(id_Naj);
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_RemoveNageuseEquipeActionPerformed

    private void addNageuseEquipeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNageuseEquipeActionPerformed
        try {
            // TODO add your handling code here:
            String CompetNaj = boxNaj.getSelectedItem().toString();
            int id_Naj = Integer.parseInt(CompetNaj.substring(0, 1));
            String CompetEquipe = boxEquipeNaj.getSelectedItem().toString();
            int id_Equipe = Integer.parseInt(CompetEquipe.substring(0, 1));
            Compose compose = new Compose(id_Naj, id_Equipe);
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_addNageuseEquipeActionPerformed

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
            boolean visible = false;
            if (boxVisible.getSelectedItem() == "Oui") {
                visible = true;
            }
            String CompetEquipe = boxCompetForEquipe.getSelectedItem().toString();
            int id_Compet = Integer.parseInt(CompetEquipe.substring(0, 1));

            String CompetClub = boxClubForEquipe.getSelectedItem().toString();
            int id_Club = Integer.parseInt(CompetClub.substring(0, 1));
            double penalite = Double.parseDouble(boxPenaliteEquipe.getSelectedItem().toString());
            Equipe newEquipe = new Equipe(textNomEquipe.getText(), Integer.parseInt(textNumPassage.getText()), penalite, visible, textdateCreationEquipe.getText(), id_Club, id_Compet);
            //  Object CompetEquipe = boxCompetForEquipe.getSelectedItem();
            //  Object CompetClub = boxClubForEquipe.getSelectedItem();
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
            boolean visible = false;

            String CompetEquipe = boxCompetForEquipe1.getSelectedItem().toString();
            int id_Compet = Integer.parseInt(CompetEquipe.substring(0, 1));

            String CompetClub = boxClubForEquipe1.getSelectedItem().toString();
            int id_Club = Integer.parseInt(CompetClub.substring(0, 1));
            double penalite = Double.parseDouble(boxPenaliteEquipe1.getSelectedItem().toString());
            Equipe newEquipe = new Equipe(jTextEquipe.getText(), Integer.parseInt(jTextNumeroPassage.getText()), penalite, visible, jTextDateCreation.getText(), id_Club, id_Compet);
            //  Object CompetEquipe = boxCompetForEquipe.getSelectedItem();
            //  Object CompetClub = boxClubForEquipe.getSelectedItem();
            refresh();
            boxCompetForEquipe1.setSelectedItem(CompetEquipe);
            boxClubForEquipe1.setSelectedItem(CompetClub);
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_buttonCreateEquipe1ActionPerformed

    private void textdateCreationClub1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textdateCreationClub1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textdateCreationClub1ActionPerformed

    private void jTableNageuseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableNageuseMouseClicked
        try {
            Compose compose = new Compose();
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
            int id_equipe = 0;
            for (int i = 0; i < tabIdNag.length; i++) {

                if (tabIdNag[i].equals((jTableNageuse.getModel().getValueAt(modelRow, jTableNageuse.getSelectedColumn())).toString())) {
                    indexnag = i;
                    id_equipe = compose.getId_equipe(Integer.parseInt(tabIdNag[i]));
                    // System.out.println(compose.getId_equipe(Integer.parseInt(tabIdNag[i])));
                    break;

                }
            }

            for (int i = 0; i < boxEquipeNaj.getItemCount(); i++) {

                if (id_equipe == Integer.parseInt(boxEquipeNaj.getItemAt(i).substring(0, 1))) {
                    boxEquipeNaj.setSelectedIndex(i);
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
            note.setNoteJuge(equipe_int);

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
            equipe.setAllNotVisible();
            equipe.setVisible(1, id_equipe);
            TimerStart();
            refresh();
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_butonLanceBalletActionPerformed

    private void valideNoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_valideNoteActionPerformed
        // TODO add your handling code here:
        //  int note, int id_personne, int id_equipe
        try {

            note.setNote(Double.parseDouble(textJugeNote1.getText()), equipe.getIdJuge(1), equipe.getIdEquipe(1));
            note.setNote(Double.parseDouble(textJugeNote2.getText()), equipe.getIdJuge(2), equipe.getIdEquipe(2));
            note.setNote(Double.parseDouble(textJugeNote3.getText()), equipe.getIdJuge(3), equipe.getIdEquipe(3));
            note.setNote(Double.parseDouble(textJugeNote4.getText()), equipe.getIdJuge(4), equipe.getIdEquipe(4));
            note.setNote(Double.parseDouble(textJugeNote5.getText()), equipe.getIdJuge(5), equipe.getIdEquipe(5));

            refresh();
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_valideNoteActionPerformed

    private void listPersonneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listPersonneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_listPersonneActionPerformed

    private void refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshActionPerformed
        try {
            // TODO add your handling code here:
            refresh();
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_refreshActionPerformed

    private void buttonNoterJugeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonNoterJugeActionPerformed
        try {
            // TODO add your handling code here:
            int curseur = SliderNote.getValue();
            int id_personne = personne.getId_personne();
            int id_equipe = equipe.getIdEquipeEncours();
            //System.out.print(id_personne);
            //System.out.print(id_equipe);
            // System.out.println("note : " + curseur + " Equipe ! " + id_equipe + " personne ! " + id_personne);

            note = new note(curseur, id_equipe, id_personne, 0);

            // refresh();
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_buttonNoterJugeActionPerformed

    private void AfficheResultatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AfficheResultatActionPerformed
        // TODO add your handling code here:
        //boxClubForAffichNote.getSelectedIndex();
        try {
            String id_String = boxClubForAffichNote.getSelectedItem().toString();
            id_String = id_String.substring(0, 1);
            int id_equipe = Integer.parseInt(id_String);

            note.setVisible(1, id_equipe);
            refresh();
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_AfficheResultatActionPerformed

    private void boxClubForAffichNoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxClubForAffichNoteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_boxClubForAffichNoteActionPerformed

    private void StopResultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StopResultActionPerformed
        // TODO add your handling code here:

        try {
            String id_String = boxClubForAffichNote.getSelectedItem().toString();
            id_String = id_String.substring(0, 1);
            int id_equipe = Integer.parseInt(id_String);

            note.setVisible(0, id_equipe);
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_StopResultActionPerformed

    private void jTextLieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextLieuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextLieuActionPerformed

    private void boxClubForEquipe1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxClubForEquipe1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_boxClubForEquipe1ActionPerformed

    private void boxPenaliteEquipeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxPenaliteEquipeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_boxPenaliteEquipeActionPerformed

    private void boxPenaliteEquipe1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxPenaliteEquipe1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_boxPenaliteEquipe1ActionPerformed

    private void CreerNageuseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CreerNageuseActionPerformed
        try {
            Personne newPersonne = new Personne(jTextNomNageuse.getText(), jTextPrenomNageuse.getText(), jTextDateCreationNageuse.getText(), jTextDateNaissNageuse.getText());

            int id_nageuse = personne.setInfosGetDernierId();
            // System.out.print(id_nageuse);
            Nageuse newNageuse = new Nageuse(id_nageuse);
            refresh();
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_CreerNageuseActionPerformed

    private void jTextDateCreationNageuseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextDateCreationNageuseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextDateCreationNageuseActionPerformed

    private void jTextDateNaissNageuseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextDateNaissNageuseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextDateNaissNageuseActionPerformed

    private void jTextDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextDateActionPerformed

    private void jTextDateCreationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextDateCreationActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextDateCreationActionPerformed

    private void listPersonneClubActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listPersonneClubActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_listPersonneClubActionPerformed

    private void refeshJAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refeshJAActionPerformed
        try {
            // TODO add your handling code here:

            refresh();
        } catch (SQLException ex) {
            Logger.getLogger(NatationSynchronisee.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_refeshJAActionPerformed

    private void textdatefinCompetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textdatefinCompetActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textdatefinCompetActionPerformed

    private void jTextFinDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFinDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFinDateActionPerformed

    private void jComboBoxDirigeantActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxDirigeantActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxDirigeantActionPerformed

    private void buttonDeleteClub1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDeleteClub1ActionPerformed
        jFrame1.setVisible(true);
        labMentionLegale.setText("Informations légales\n"
                + "1. Présentation de l'application.\n"
                + "En vertu de l'article 6 de la loi n° 2004-575 du 21 juin 2004 pour la confiance dans l'économie numérique, il est précisé aux utilisateurs de l'application Dolphin. L'identité des différents intervenants dans le cadre de sa réalisation et de son suivi :\n"
                + "\n"
                + "Propriétaire : OLITS – SARL – 133 Rue Marius Berliet, 69008 Lyon\n"
                + "Créateur : OLITS\n"
                + "Responsable publication : OLITS – http://www.ort-lyon.fr/\n"
                + "Le responsable publication est une personne physique ou une personne morale.\n"
                + "Webmaster : OLITS – http://www.ort-lyon.fr/\n"
                + "Hébergeur : OLITS – 69008\n"
                + "Crédits : Christian Attia, Célestine Chareyre, Florent Lalisse et Bruno Marques.\n"
                + "2. Conditions générales d’utilisation de l'application et des services proposés.\n"
                + "L’utilisation de l'application Dolphin implique l’acceptation pleine et entière des conditions générales d’utilisation ci-après décrites. Ces conditions d’utilisation sont susceptibles d’être modifiées ou complétées à tout moment, les utilisateurs de l'application Dolphin sont donc invités à les consulter de manière régulière.\n"
                + "\n"
                + "Cette application est normalement accessible à tout moment aux utilisateurs. Une interruption pour raison de maintenance technique peut être toutefois décidée par OLITS, qui s’efforcera alors de communiquer préalablement aux utilisateurs les dates et heures de l’intervention.\n"
                + "\n"
                + "L'application Dolphin est mise à jour régulièrement par OLITS. De la même façon, les mentions légales peuvent être modifiées à tout moment : elles s’imposent néanmoins à l’utilisateur qui est invité à s’y référer le plus souvent possible afin d’en prendre connaissance.\n"
                + "\n"
                + "3. Description des services fournis.\n"
                + "l'application Dolphin a pour objet d'informatiser la gestion des compétitions de natation synchronisée.\n"
                + "\n"
                + "OLITS s’efforce de fournir sur l'application Dolphin des informations aussi précises que possible. Toutefois, il ne pourra être tenue responsable des omissions, des inexactitudes et des carences dans la mise à jour, qu’elles soient de son fait ou du fait des tiers partenaires qui lui fournissent ces informations.\n"
                + "\n"
                + "Tous les informations indiquées sur l'application Dolphin sont données à titre indicatif, et sont susceptibles d’évoluer. Par ailleurs, les renseignements figurant sur l'application Dolphin ne sont pas exhaustifs. Ils sont donnés sous réserve de modifications ayant été apportées depuis leur mise en ligne.\n"
                + "\n"
                + "4. Limitations contractuelles sur les données techniques.\n"
                + "l'application utilise la technologie Java client lourd.\n"
                + "\n"
                + "l'application ne pourra être tenu responsable de dommages matériels liés à l’utilisation de l'application. De plus, l’utilisateur de l'application s’engage à accéder au application en utilisant un matériel récent et ne contenant pas de virus.\n"
                + "\n"
                + "5. Propriété intellectuelle et contrefaçons.\n"
                + "OLITS est propriétaire des droits de propriété intellectuelle ou détient les droits d’usage sur tous les éléments accessibles sur l'application, notamment les textes, images, graphismes, logo, icônes, sons, logiciels.\n"
                + "\n"
                + "Toute reproduction, représentation, modification, publication, adaptation de tout ou partie des éléments de l'application, quel que soit le moyen ou le procédé utilisé, est interdite, sauf autorisation écrite préalable de : OLITS.\n"
                + "\n"
                + "Toute exploitation non autorisée de l'application ou de l’un quelconque des éléments qu’il contient sera considérée comme constitutive d’une contrefaçon et poursuivie conformément aux dispositions des articles L.335-2 et suivants du Code de Propriété Intellectuelle.\n"
                + "\n"
                + "6. Limitations de responsabilité.\n"
                + "OLITS ne pourra être tenue responsable des dommages directs et indirects causés au matériel de l’utilisateur, lors de l’accès au application Dolphin, et résultant soit de l’utilisation d’un matériel ne répondant pas aux spécifications indiquées au point 4, soit de l’apparition d’un bug ou d’une incompatibilité.\n"
                + "\n"
                + "OLITS ne pourra également être tenue responsable des dommages indirects (tels par exemple qu’une perte de marché ou perte d’une chance) consécutifs à l’utilisation de l'application Dolphin.\n"
                + "\n"
                + "Des espaces interactifs (possibilité de poser des questions dans l’espace contact) sont à la disposition des utilisateurs. OLITS se réserve le droit de supprimer, sans mise en demeure préalable, tout contenu déposé dans cet espace qui contreviendrait à la législation applicable en France, en particulier aux dispositions relatives à la protection des données. Le cas échéant, OLITS se réserve également la possibilité de mettre en cause la responsabilité civile et/ou pénale de l’utilisateur, notamment en cas de message à caractère raciste, injurieux, diffamant, ou pornographique, quel que soit le support utilisé (texte, photographie…).\n"
                + "\n"
                + "7. Gestion des données personnelles.\n"
                + "En France, les données personnelles sont notamment protégées par la loi n° 78-87 du 6 janvier 1978, la loi n° 2004-801 du 6 août 2004, l'article L. 226-13 du Code pénal et la Directive Européenne du 24 octobre 1995.\n"
                + "\n"
                + "A l'occasion de l'utilisation de l'application Dolphin, peuvent êtres recueillies : l'URL des liens par l'intermédiaire desquels l'utilisateur a accédé au application Dolphin, le fournisseur d'accès de l'utilisateur, l'adresse de protocole Internet (IP) de l'utilisateur.\n"
                + "\n"
                + "En tout état de cause OLITS ne collecte des informations personnelles relatives à l'utilisateur que pour le besoin de certains services proposés par l'application Dolphin. L'utilisateur fournit ces informations en toute connaissance de cause, notamment lorsqu'il procède par lui-même à leur saisie. Il est alors précisé à l'utilisateur de l'application Dolphin l’obligation ou non de fournir ces informations.\n"
                + "\n"
                + "Conformément aux dispositions des articles 38 et suivants de la loi 78-17 du 6 janvier 1978 relative à l’informatique, aux fichiers et aux libertés, tout utilisateur dispose d’un droit d’accès, de rectification et d’opposition aux données personnelles le concernant, en effectuant sa demande écrite et signée, accompagnée d’une copie du titre d’identité avec signature du titulaire de la pièce, en précisant l’adresse à laquelle la réponse doit être envoyée.\n"
                + "\n"
                + "Aucune information personnelle de l'utilisateur de l'application Dolphin n'est publiée à l'insu de l'utilisateur, échangée, transférée, cédée ou vendue sur un support quelconque à des tiers. Seule l'hypothèse du rachat de OLITS et de ses droits permettrait la transmission des dites informations à l'éventuel acquéreur qui serait à son tour tenu de la même obligation de conservation et de modification des données vis à vis de l'utilisateur de l'application Dolphin.\n"
                + "\n"
                + "l'application n'est pas déclaré à la CNIL car il ne recueille pas d'informations personnelles. .\n"
                + "\n"
                + "Les bases de données sont protégées par les dispositions de la loi du 1er juillet 1998 transposant la directive 96/9 du 11 mars 1996 relative à la protection juridique des bases de données.\n"
                + "\n"
                + "9. Droit applicable et attribution de juridiction.\n"
                + "Tout litige en relation avec l’utilisation de l'application Dolphin est soumis au droit français. Il est fait attribution exclusive de juridiction aux tribunaux compétents de Paris.\n"
                + "\n"
                + "10. Les principales lois concernées.\n"
                + "Loi n° 78-17 du 6 janvier 1978, notamment modifiée par la loi n° 2004-801 du 6 août 2004 relative à l'informatique, aux fichiers et aux libertés.\n"
                + "\n"
                + "Loi n° 2004-575 du 21 juin 2004 pour la confiance dans l'économie numérique.\n"
                + "\n"
                + "11. Lexique.\n"
                + "Utilisateur : Internaute se connectant, utilisant l'application susnommé.\n"
                + "\n"
                + "Informations personnelles : « les informations qui permettent, sous quelque forme que ce soit, directement ou non, l'identification des personnes physiques auxquelles elles s'appliquent » (article 4 de la loi n° 78-17 du 6 janvier 1978).");

    }//GEN-LAST:event_buttonDeleteClub1ActionPerformed

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
    private javax.swing.JButton AfficheResultat;
    private javax.swing.JButton ButAddNag;
    private javax.swing.JButton CreerClub;
    private javax.swing.JButton CreerCompet;
    private javax.swing.JButton CreerNageuse;
    private javax.swing.JButton RemoveNageuseEquipe;
    private javax.swing.JSlider SliderNote;
    private javax.swing.JButton StopResult;
    private javax.swing.JButton addNageuseEquipe;
    private javax.swing.JComboBox<String> boxClubForAffichNote;
    private javax.swing.JComboBox<String> boxClubForEquipe;
    private javax.swing.JComboBox<String> boxClubForEquipe1;
    private javax.swing.JComboBox<String> boxClubForLanc;
    private javax.swing.JComboBox<String> boxClubForPen;
    private javax.swing.JComboBox<String> boxCompetForEquipe;
    private javax.swing.JComboBox<String> boxCompetForEquipe1;
    private javax.swing.JComboBox<String> boxEquipeNaj;
    private javax.swing.JComboBox<String> boxNaj;
    private javax.swing.JComboBox<String> boxPenaliteEquipe;
    private javax.swing.JComboBox<String> boxPenaliteEquipe1;
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
    private javax.swing.JButton buttonDeleteClub1;
    private javax.swing.JButton buttonDeleteEquipe;
    private javax.swing.JButton buttonNoterJuge;
    private javax.swing.JButton buttonSupprimerCompet;
    private javax.swing.JButton buttonUpdateClub;
    private javax.swing.JButton buttonUpdateCompet;
    private javax.swing.JButton buttonUpdateEquipe;
    private javax.swing.JButton deletePersonne;
    private javax.swing.JButton deleteUser;
    private javax.swing.JTextField fieldNomClub;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButtonPenalite;
    private javax.swing.JComboBox<String> jComboBoxDirigeant;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
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
    private javax.swing.JLabel jLabel5;
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
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
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
    private javax.swing.JTextField jTextClub;
    private javax.swing.JFormattedTextField jTextDate;
    private javax.swing.JFormattedTextField jTextDateCreation;
    private javax.swing.JFormattedTextField jTextDateCreationNageuse;
    private javax.swing.JFormattedTextField jTextDateNaissNageuse;
    private javax.swing.JTextField jTextEquipe;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JFormattedTextField jTextFinDate;
    private javax.swing.JTextField jTextLieu;
    private javax.swing.JTextField jTextNomNageuse;
    private javax.swing.JTextField jTextNumeroPassage;
    private javax.swing.JTextField jTextPrenomNageuse;
    private javax.swing.JTextField jTextTitre;
    private javax.swing.JTable jtableClub;
    private javax.swing.JTextArea labMentionLegale;
    private javax.swing.JLabel labelEquipe;
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
    private javax.swing.JButton refeshJA;
    private javax.swing.JToggleButton refresh;
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
    private javax.swing.JFormattedTextField textdatefinCompet;
    private javax.swing.JTextField textnomPersonne;
    private javax.swing.JTextField textprenomPersonne;
    private javax.swing.JButton updatePersonne;
    private javax.swing.JButton updateUser;
    private javax.swing.JButton updateUser1;
    private javax.swing.JComboBox<String> valeur_penalite;
    private javax.swing.JButton valideNote;
    // End of variables declaration//GEN-END:variables
}
