package GUI;

import Data.User;
import Data.UserDatabase;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private UserDatabase database;
    private NewUserable newUserable;

    private JLabel nameLabel;
    private JLabel passwordLabel;

    private JTextField nameField;
    private JPasswordField passwordField;

    private JButton okButton;

    LoginFrame(UserDatabase database, NewUserable newUserable) {
        super("Login");
        this.database = database;
        this.newUserable = newUserable;

        setLayout(null);
        setVisible(true);
        setSize(400,150);
        setResizable(false);

        nameLabel = new JLabel("Name:");
        passwordLabel = new JLabel("Password:");

        nameField = new JTextField();
        passwordField = new JPasswordField();

        okButton = new JButton("OK");

        nameLabel.setBounds(20,10,60, 20);
        nameLabel.setOpaque(false);
        add(nameLabel);

        passwordLabel.setBounds(20,40,120,20);
        passwordLabel.setOpaque(false);
        add(passwordLabel);

        nameField.setBounds(140,10,200,20);
        nameField.setOpaque(false);
        add(nameField);

        passwordField.setBounds(140,40,200,20);
        passwordField.setOpaque(false);
        add(passwordField);

        okButton.setBounds(240,65,80,40);
        okButton.setOpaque(false);
        add(okButton);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (nameField.getText().length() != 0 && passwordField.getText().length() != 0) {
                    String user  = database.logIn(nameField.getText(), passwordField.getText());

                    if (user != null) {
                        newUserable.newUser(user);
                        setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(null,"Incorrect Password");
                    }

                } else {
                    JOptionPane.showMessageDialog(null,"Please fill the lines");
                }
            }
        });


    }
}
