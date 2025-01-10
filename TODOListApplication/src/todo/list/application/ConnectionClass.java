package todo.list.application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ConnectionClass {
    public Connection connection;
    public Statement statement;

    public ConnectionClass() {
        try {
            // Update the MySQL Workbench URL, username, and password
            String dbUrl = "jdbc:mysql://localhost:3306/ToDo_list_Application"; // Database name: ToDo_list_Application
            String dbUsername = "root"; // Replace with your MySQL username
            String dbPassword = "Password"; // Replace with your MySQL password

            // Establish the connection
            connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while establishing database connection.");
        }
    }
}
