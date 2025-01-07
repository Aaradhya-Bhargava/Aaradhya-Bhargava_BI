package bank.application;

import java.sql.*;
public class Conn {
    Connection connection;
    Statement statement;
    public Conn(){
        try{
            //Add the MySQLWorkbeanch url with the username and your password
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Bank_Application","username","Password");
            statement = connection.createStatement();
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}