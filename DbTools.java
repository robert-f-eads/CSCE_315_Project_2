import java.sql.*;
import java.io.*;
import java.util.Scanner;

public class DbTools {

    //Variables    
    private String dbName;
    private String dbConnectionString;
    private Connection dbConnection = null;

    //Constructor
    public DbTools() {
        dbName = "csce315_901_2";
        dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;
    } 

    //Member functions
    public void openDbConnection(String username, String password) {
        System.out.println("I will open database connection!");
    }

    public boolean hasDbConnection() {
        System.out.println("I will check database connection!");
    }

    public void closeDbConnection() {
        System.out.println("I will close database connection!");
    }

    public void dbCreate(String filePath) {
        System.out.println(String.format("I will create tables with data from %s!", filePath));
    }

    public void dbFill(String tableName, String filePath) {
        System.out.println(String.format("I will fill in %s with data from %s!", tableName, filePath));

    }

    public void dbDrop(String filePath) {
        System.out.println(String.format("I will drop tables with data from %s!", filePath));
    }

}