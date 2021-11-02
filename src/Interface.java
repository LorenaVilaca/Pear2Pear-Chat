import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;


public class Interface {
    private JPanel panel;
    private JTextField textField;
    private JButton sendbutton;
    private JButton cancelbutton;
    private JTextArea textArea;
    private JButton clearbutton;
    private JLabel title;
    UDPCliente cliente;
    Timer sendTimer;
    String contentAtual;

    public Interface(){
        try {
            this.cliente = new UDPCliente(textArea);
            JFrame janela = new JFrame();
            janela.setContentPane(panel);
            janela.setVisible(true);
            janela.pack();
            janela.setSize(600,600);
            janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            sendTimer = new Timer(10000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    send(contentAtual);
                }
            });
            sendTimer.setRepeats(false);
            sendbutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e){
                    if(sendTimer.isRunning()){
                        sendTimer.stop();
                        send(contentAtual);
                    }
                    contentAtual = textField.getText().trim() + "\n";
                    textField.setText("");
                    sendTimer.start();

                }
            });

            clearbutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    textArea.setText("");
                }
            });

            textField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed (KeyEvent e){
                    if(e.getKeyCode() == KeyEvent.VK_ENTER){
                        if(sendTimer.isRunning()){
                            sendTimer.stop();
                            send(contentAtual);
                        }
                        contentAtual = textField.getText().trim() + "\n";
                        textField.setText("");
                        sendTimer.start();
                    }
                }
            });

            cancelbutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    sendTimer.stop();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String content){
        try {
            if(!content.equals("\n")) {
                textArea.append(cliente.enviarDados(content));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}