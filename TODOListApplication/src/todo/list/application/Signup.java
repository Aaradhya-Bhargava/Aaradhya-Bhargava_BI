package todo.list.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Signup extends JFrame implements ActionListener {

    // Declare the components
    JTextField textUsername, textFullName, textEmail;
    JPasswordField passwordField, confirmPasswordField;
    JButton buttonSignup, buttonClear, buttonLogin;  // Added Login button
    JLabel logo;

    Signup() {
        super("Signup - TODO List Application");

        // Set Layout and Window Properties
        setLayout(null);
        setSize(500, 500);
        setLocation(450, 200);

        // Set background color
        getContentPane().setBackground(new Color(5, 245, 204, 247)); // White background for the page

        // Logo at the top-left corner
        logo = new JLabel();
        ImageIcon logoIcon = new ImageIcon(ClassLoader.getSystemResource("images/logo.png"));  // Update the path to your logo
        Image logoImage = logoIcon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
        logo.setIcon(new ImageIcon(logoImage));
        logo.setBounds(10, 10, 70, 70);
        add(logo);

        JLabel labelWelcome = new JLabel("Welcome to Sign-UP");
        labelWelcome.setBounds(180, 30, 500, 50);
        labelWelcome.setFont(new Font("Arial", Font.BOLD, 26));
        labelWelcome.setForeground(Color.BLACK);
        add(labelWelcome);

        // Full Name Label
        JLabel labelFullName = new JLabel("Full Name:");
        labelFullName.setBounds(50, 100, 200, 30);
        labelFullName.setFont(new Font("Arial", Font.BOLD, 16));
        add(labelFullName);

        // Full Name Text Field
        textFullName = new JTextField();
        textFullName.setBounds(200, 100, 250, 30);
        add(textFullName);

        // Username Label
        JLabel labelUsername = new JLabel("Username:");
        labelUsername.setBounds(50, 150, 200, 30);
        labelUsername.setFont(new Font("Arial", Font.BOLD, 16));
        add(labelUsername);

        // Username Text Field
        textUsername = new JTextField();
        textUsername.setBounds(200, 150, 250, 30);
        add(textUsername);

        // Email Label
        JLabel labelEmail = new JLabel("Email:");
        labelEmail.setBounds(50, 200, 200, 30);
        labelEmail.setFont(new Font("Arial", Font.BOLD, 16));
        add(labelEmail);

        // Email Text Field
        textEmail = new JTextField();
        textEmail.setBounds(200, 200, 250, 30);
        add(textEmail);

        // Password Label
        JLabel labelPassword = new JLabel("Password:");
        labelPassword.setBounds(50, 250, 200, 30);
        labelPassword.setFont(new Font("Arial", Font.BOLD, 16));
        add(labelPassword);

        // Password Field
        passwordField = new JPasswordField();
        passwordField.setBounds(200, 250, 250, 30);
        add(passwordField);

        // Confirm Password Label
        JLabel labelConfirmPassword = new JLabel("Confirm Password:");
        labelConfirmPassword.setBounds(50, 300, 200, 30);
        labelConfirmPassword.setFont(new Font("Arial", Font.BOLD, 16));
        add(labelConfirmPassword);

        // Confirm Password Field
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(200, 300, 250, 30);
        add(confirmPasswordField);

        // Signup Button
        buttonSignup = new JButton("Signup");
        buttonSignup.setBounds(200, 370, 100, 30);
        buttonSignup.addActionListener(this);
        add(buttonSignup);

        // Clear Button
        buttonClear = new JButton("Clear");
        buttonClear.setBounds(320, 370, 100, 30);
        buttonClear.addActionListener(this);
        add(buttonClear);

        // Login Button - New Button
        buttonLogin = new JButton("Login");
        buttonLogin.setBounds(200, 420, 220, 30);  // Set the position below the other buttons
        buttonLogin.addActionListener(this);  // ActionListener for Login button
        add(buttonLogin);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(false);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonSignup) {
            String username = textUsername.getText();
            String fullName = textFullName.getText();
            String email = textEmail.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (username.isEmpty() || fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.");
                return;
            }

            try {
                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection = connectionClass.connection;

                if (connection != null) {
                    String query = "INSERT INTO signup (username, full_name, email, password) VALUES (?, ?, ?, ?)";
                    PreparedStatement ps = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
                    ps.setString(1, username);
                    ps.setString(2, fullName);
                    ps.setString(3, email);
                    ps.setString(4, password);
                    ps.executeUpdate();

                    ResultSet generatedKeys = ps.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int userId = generatedKeys.getInt(1);
                        JOptionPane.showMessageDialog(this, "Signup Successful! Your User ID is: " + userId);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Database connection failed.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        } else if (e.getSource() == buttonClear) {
            textUsername.setText("");
            textFullName.setText("");
            textEmail.setText("");
            passwordField.setText("");
            confirmPasswordField.setText("");
        } else if (e.getSource() == buttonLogin) {
            // Redirect to Login Page
            setVisible(false);
            new Login();
        }
    }

    public static void main(String[] args) {
        new Signup();
    }
}
