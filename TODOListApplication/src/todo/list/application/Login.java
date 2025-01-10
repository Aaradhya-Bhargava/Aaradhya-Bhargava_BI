package todo.list.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login extends JFrame implements ActionListener {

    JLabel labelWelcome, labelUsername, labelPassword;
    JTextField textUsername;
    JPasswordField passwordField;
    JButton buttonLogin, buttonClear, buttonSignup;

    Login() {
        super("TODO List Application");

        // Set Layout and Window Properties
        setLayout(null);
        setSize(800, 500);
        setLocation(400, 200);

        // Background Image as Watermark
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("images/bg.png"));
        Image i2 = i1.getImage().getScaledInstance(900, 500, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel background = new JLabel(i3);
        background.setBounds(0, 0, 800, 500);
        add(background);

        // Welcome Label
        labelWelcome = new JLabel("Welcome to TODO List Application");
        labelWelcome.setBounds(180, 30, 500, 50);
        labelWelcome.setFont(new Font("Arial", Font.BOLD, 26));
        labelWelcome.setForeground(Color.BLACK);
        background.add(labelWelcome);

        // Username Label
        labelUsername = new JLabel("Username:");
        labelUsername.setBounds(150, 150, 200, 30);
        labelUsername.setFont(new Font("Arial", Font.BOLD, 20));
        labelUsername.setForeground(Color.BLACK);
        background.add(labelUsername);

        // Username Text Field
        textUsername = new JTextField();
        textUsername.setBounds(300, 150, 300, 30);
        textUsername.setFont(new Font("Arial", Font.PLAIN, 16));
        background.add(textUsername);

        // Password Label
        labelPassword = new JLabel("Password:");
        labelPassword.setBounds(150, 200, 200, 30);
        labelPassword.setFont(new Font("Arial", Font.BOLD, 20));
        labelPassword.setForeground(Color.BLACK);
        background.add(labelPassword);

        // Password Field
        passwordField = new JPasswordField();
        passwordField.setBounds(300, 200, 300, 30);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        background.add(passwordField);

        // Login Button
        buttonLogin = new JButton("Login");
        buttonLogin.setBounds(300, 270, 100, 30);
        buttonLogin.setBackground(Color.BLACK);
        buttonLogin.setForeground(Color.WHITE);
        buttonLogin.addActionListener(this);
        background.add(buttonLogin);

        // Clear Button
        buttonClear = new JButton("Clear");
        buttonClear.setBounds(420, 270, 100, 30);
        buttonClear.setBackground(Color.BLACK);
        buttonClear.setForeground(Color.WHITE);
        buttonClear.addActionListener(this);
        background.add(buttonClear);

        // Signup Button
        buttonSignup = new JButton("Signup");
        buttonSignup.setBounds(360, 320, 100, 30);
        buttonSignup.setBackground(Color.BLACK);
        buttonSignup.setForeground(Color.WHITE);
        buttonSignup.addActionListener(this);
        background.add(buttonSignup);

        // Set Window Properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setSize(800, 500);
        setLocation(450, 200);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonLogin) {
            try {
                ConnectionClass c = new ConnectionClass();
                String username = textUsername.getText();
                String password = new String(passwordField.getPassword());

                String query = "SELECT * FROM signup WHERE username = ? AND password = ?";
                PreparedStatement preparedStatement = c.connection.prepareStatement(query);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    JOptionPane.showMessageDialog(this, "Login Successful!");
                    setVisible(false);
                    new ToDoListApplication(); // Redirect to the main page
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Username or Password.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error connecting to database.");
            }
        } else if (e.getSource() == buttonClear) {
            textUsername.setText("");
            passwordField.setText("");
        } else if (e.getSource() == buttonSignup) {
            // Redirect to Signup Page
            setVisible(false);
            new Signup();
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}
