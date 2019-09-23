package Misc;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionClass {
    //Connecting to SQL database
    public Connection connection;

    public Connection getconnection() {
        String dbName = "chatline"; //Database Name
        String user = "root"; //Username of Database
        String password = "root";//Password
        //Tries conecting to database if doesnt work gives a error
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName + "?useSSL=false", user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
}
