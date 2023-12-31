package src.main.shooter.gui.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import src.main.shooter.ToComeIn.LoginForm;
import src.main.shooter.ToComeIn.RegistrationForm;
import src.main.shooter.net.Server;

public class ClientMenuPanel extends JPanel {
    private final JTextField ipAddress, portNumber;
    private boolean userLoggedIn = false;

    public ClientMenuPanel() {
        ipAddress = new JTextField("localhost");
        add(ipAddress);

        portNumber = new JTextField("" + Server.DEFAULT_PORT_NUMBER);
        add(portNumber);

        JButton joinGameButton = new JButton("Join Game");
        joinGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userLoggedIn) {
                    ((ClientMainFrame) ClientMenuPanel.this.getTopLevelAncestor()).startGame(ipAddress.getText(),
                            Integer.parseInt(portNumber.getText()));
                } else {
                    JOptionPane.showMessageDialog(null, "Please log in first.");
                }
            }
        });
        add(joinGameButton);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegistrationForm();
                registerButton.setVisible(false);
            }
        });
        add(registerButton);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginForm();
                userLoggedIn = true;
                registerButton.setVisible(false);
                loginButton.setVisible(false);
            }
        });
        add(loginButton);
    }

}
