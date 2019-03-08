package killergame;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Javi
 */
public class KillerPanel extends JPanel implements ActionListener {

    private KillerGame game;

    private JButton conectarLeft;
    private JButton conectarRight;
    private JButton conectarServer;
    private JButton start;
    private JLabel estadoLeft;
    private JLabel estadoRight;
    private JLabel estadoServer;
    private JLabel labelIpLeft;
    private JLabel labelIpRight;
    private JLabel labelPortLeft;
    private JLabel labelPortRight;
    private JLabel labelServer;
    private JTextField ipLeft;
    private JTextField ipRight;
    private JTextField portLeft;
    private JTextField portRigth;
    private JTextField portServer;

    public KillerPanel(KillerGame game) {

        super();

        this.game = game;

        generarUI();
    }

    /*##########################################################################
    ###### Interface Generation ################################################
    ##########################################################################*/
    private void generarUI() {
        
        this.setSize(800, 300);
        this.setBackground(Color.CYAN);
        this.setLayout(new GridBagLayout());
        GridBagConstraints ajustesGrid = new GridBagConstraints();
        ajustesGrid.fill = GridBagConstraints.BOTH;
        ajustesGrid.gridwidth = 1;
        ajustesGrid.gridheight = 1;
        ajustesGrid.weightx = 1;
        ajustesGrid.weighty = 1;
        ajustesGrid.insets = new Insets(10, 10, 10, 10);

        /* -------- Gestión Puerto Servidor -------- */
 /* Label Puerto Servidor */
        this.labelServer = new JLabel("Puerto Servidor");
        this.labelServer.setFont(new Font("Verdana", Font.PLAIN, 16));
        this.labelServer.setHorizontalAlignment(JTextField.CENTER);
        ajustesGrid.gridx = 0;
        ajustesGrid.gridy = 0;
        ajustesGrid.gridwidth = 1;
        ajustesGrid.gridheight = 1;
        this.add(this.labelServer, ajustesGrid);

        /* Input Puerto Servidor */
        this.portServer = new JTextField();
        this.portServer.setFont(new Font("Verdana", Font.PLAIN, 16));
        this.portServer.setHorizontalAlignment(JTextField.CENTER);
        this.portServer.setText("8000");
        ajustesGrid.gridx = 1;
        ajustesGrid.gridy = 0;
        ajustesGrid.gridwidth = 1;
        ajustesGrid.gridheight = 1;
        this.add(this.portServer, ajustesGrid);

        /* Botón Conectar Servidor */
        this.conectarServer = new JButton("Conectar Servidor");
        this.conectarServer.setFont(new Font("Verdana", Font.PLAIN, 16));
        this.conectarServer.setHorizontalAlignment(JTextField.CENTER);
        this.conectarServer.addActionListener(this);
        ajustesGrid.gridx = 2;
        ajustesGrid.gridy = 0;
        ajustesGrid.gridwidth = 1;
        ajustesGrid.gridheight = 1;
        this.add(this.conectarServer, ajustesGrid);

        /* Label Estado Servidor */
        this.estadoServer = new JLabel("Servidor Offline");
        this.estadoServer.setFont(new Font("Verdana", Font.PLAIN, 16));
        this.estadoServer.setHorizontalAlignment(JTextField.CENTER);
        ajustesGrid.gridx = 3;
        ajustesGrid.gridy = 0;
        ajustesGrid.gridwidth = 1;
        ajustesGrid.gridheight = 1;
        this.add(this.estadoServer, ajustesGrid);

        /* -------- Gestión Left Killer -------- */
 /* Label Estado Left */
        this.estadoLeft = new JLabel("No Conectado");
        this.estadoLeft.setFont(new Font("Verdana", Font.PLAIN, 16));
        this.estadoLeft.setHorizontalAlignment(JTextField.CENTER);
        ajustesGrid.gridx = 0;
        ajustesGrid.gridy = 1;
        ajustesGrid.gridwidth = 2;
        ajustesGrid.gridheight = 1;
        this.add(this.estadoLeft, ajustesGrid);

        /* Label Ip Left */
        this.labelIpLeft = new JLabel("IP LeftKiller");
        this.labelIpLeft.setFont(new Font("Verdana", Font.PLAIN, 16));
        this.labelIpLeft.setHorizontalAlignment(JTextField.CENTER);
        ajustesGrid.gridx = 0;
        ajustesGrid.gridy = 2;
        ajustesGrid.gridwidth = 1;
        ajustesGrid.gridheight = 1;
        this.add(this.labelIpLeft, ajustesGrid);

        /* Input Ip Left */
        this.ipLeft = new JTextField();
        this.ipLeft.setFont(new Font("Verdana", Font.PLAIN, 16));
        this.ipLeft.setHorizontalAlignment(JTextField.CENTER);
        this.ipLeft.setText("127.0.0.1");
        ajustesGrid.gridx = 1;
        ajustesGrid.gridy = 2;
        ajustesGrid.gridwidth = 1;
        ajustesGrid.gridheight = 1;
        this.add(this.ipLeft, ajustesGrid);

        /* Label Puerto Left */
        this.labelPortLeft = new JLabel("Puerto LeftKiller");
        this.labelPortLeft.setFont(new Font("Verdana", Font.PLAIN, 16));
        this.labelPortLeft.setHorizontalAlignment(JTextField.CENTER);
        ajustesGrid.gridx = 0;
        ajustesGrid.gridy = 3;
        ajustesGrid.gridwidth = 1;
        ajustesGrid.gridheight = 1;
        this.add(this.labelPortLeft, ajustesGrid);

        /* Input Puerto Left */
        this.portLeft = new JTextField();
        this.portLeft.setFont(new Font("Verdana", Font.PLAIN, 16));
        this.portLeft.setHorizontalAlignment(JTextField.CENTER);
        this.portLeft.setText("8000");
        ajustesGrid.gridx = 1;
        ajustesGrid.gridy = 3;
        ajustesGrid.gridwidth = 1;
        ajustesGrid.gridheight = 1;
        this.add(this.portLeft, ajustesGrid);

        /* Botón Conectar Left */
        this.conectarLeft = new JButton("Conectar Left Killer");
        this.conectarLeft.setFont(new Font("Verdana", Font.PLAIN, 16));
        this.conectarLeft.setHorizontalAlignment(JTextField.CENTER);
        this.conectarLeft.addActionListener(this);
        ajustesGrid.gridx = 0;
        ajustesGrid.gridy = 4;
        ajustesGrid.gridwidth = 2;
        ajustesGrid.gridheight = 1;
        this.add(this.conectarLeft, ajustesGrid);

        /* -------- Gestión Right Killer -------- */
 /* Label Estado Right */
        this.estadoRight = new JLabel("No Conectado");
        this.estadoRight.setFont(new Font("Verdana", Font.PLAIN, 16));
        this.estadoRight.setHorizontalAlignment(JTextField.CENTER);
        ajustesGrid.gridx = 2;
        ajustesGrid.gridy = 1;
        ajustesGrid.gridwidth = 2;
        ajustesGrid.gridheight = 1;
        this.add(this.estadoRight, ajustesGrid);

        /* Label Ip Right */
        this.labelIpRight = new JLabel("IP RightKiller");
        this.labelIpRight.setFont(new Font("Verdana", Font.PLAIN, 16));
        this.labelIpRight.setHorizontalAlignment(JTextField.CENTER);
        ajustesGrid.gridx = 2;
        ajustesGrid.gridy = 2;
        ajustesGrid.gridwidth = 1;
        ajustesGrid.gridheight = 1;
        this.add(this.labelIpRight, ajustesGrid);

        /* Input Ip Right */
        this.ipRight = new JTextField();
        this.ipRight.setFont(new Font("Verdana", Font.PLAIN, 16));
        this.ipRight.setHorizontalAlignment(JTextField.CENTER);
        this.ipRight.setText("127.0.0.1");
        ajustesGrid.gridx = 3;
        ajustesGrid.gridy = 2;
        ajustesGrid.gridwidth = 1;
        ajustesGrid.gridheight = 1;
        this.add(this.ipRight, ajustesGrid);

        /* Label Puerto Right */
        this.labelPortRight = new JLabel("Puerto RightKiller");
        this.labelPortRight.setFont(new Font("Verdana", Font.PLAIN, 16));
        this.labelPortRight.setHorizontalAlignment(JTextField.CENTER);
        ajustesGrid.gridx = 2;
        ajustesGrid.gridy = 3;
        ajustesGrid.gridwidth = 1;
        ajustesGrid.gridheight = 1;
        this.add(this.labelPortRight, ajustesGrid);

        /* Input Puerto Right */
        this.portRigth = new JTextField();
        this.portRigth.setFont(new Font("Verdana", Font.PLAIN, 16));
        this.portRigth.setHorizontalAlignment(JTextField.CENTER);
        this.portRigth.setText("8000");
        ajustesGrid.gridx = 3;
        ajustesGrid.gridy = 3;
        ajustesGrid.gridwidth = 1;
        ajustesGrid.gridheight = 1;
        this.add(this.portRigth, ajustesGrid);

        /* Botón Conectar Right */
        this.conectarRight = new JButton("Conectar Right Killer");
        this.conectarRight.setFont(new Font("Verdana", Font.PLAIN, 16));
        this.conectarRight.setHorizontalAlignment(JTextField.CENTER);
        this.conectarRight.addActionListener(this);
        ajustesGrid.gridx = 2;
        ajustesGrid.gridy = 4;
        ajustesGrid.gridwidth = 2;
        ajustesGrid.gridheight = 1;
        this.add(this.conectarRight, ajustesGrid);

        /* -------- Gestión Botón Start -------- */
        this.start = new JButton("Start");
        this.start.setFont(new Font("Verdana", Font.PLAIN, 16));
        this.start.setHorizontalAlignment(JTextField.CENTER);
        this.start.addActionListener(this);
        ajustesGrid.gridx = 0;
        ajustesGrid.gridy = 6;
        ajustesGrid.gridwidth = 4;
        ajustesGrid.gridheight = 1;
        this.add(this.start, ajustesGrid);
    }

    /*##########################################################################
    ###### Listener ############################################################
    ##########################################################################*/
    @Override
    public void actionPerformed(ActionEvent evento) {

        if (evento.getSource() == this.conectarServer) {

            this.game.iniciarServidor(Integer.parseInt(this.portServer.getText()));

        } else if (evento.getSource() == this.start) {

            this.game.startGame();

        } else if (evento.getSource() == this.conectarLeft) {

            this.game.conectarLeftKiller(this.ipLeft.getText(), this.portLeft.getText());

        } else if (evento.getSource() == this.conectarRight) {

            this.game.conectarRightKiller(this.ipRight.getText(), this.portRigth.getText());

        } else {

            System.out.println("Esto no debería estar ocurriendo! -> Revisa Listeners de KillerPanel");
        }
    }

    /*##########################################################################
    ###### State FeedBack ######################################################
    ##########################################################################*/
    public void servidorConectado() {

        this.labelServer.setText(this.game.getIp());

        this.estadoServer.setText("Servidor Online!");
    }

    public void leftKillerConectado(String ip, int puerto) {

        this.estadoLeft.setText("Conectado! ( " + ip + " - " + puerto + " )");
    }

    public void rightKillerConectado(String ip, int puerto) {

        this.estadoRight.setText("Conectado! ( " + ip + " - " + puerto + " )");
    }

    public void servidorPuertoOcupado() {

        this.estadoServer.setText("Puerto Ocupado");
    }

    public void leftKillerNoConectado() {

        this.estadoLeft.setText("No Conectado");
    }

    public void rightKillerNoConectado() {

        this.estadoRight.setText("No Conectado");
    }
}
