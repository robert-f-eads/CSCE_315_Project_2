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
        try {
            dbConnection = DriverManager.getConnection(dbConnectionString, username, password);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.out.println("Could not connect to database... exiting program");
            System.exit(0);
        }
    }

    public boolean hasDbConnection() {
        if(dbConnection != null) {
            return true;
        }
        return false;
    }

    public void closeDbConnection() {
        try {
            dbConnection.close();
            System.out.println("Connection Closed.");
        } catch(Exception e) {
            System.out.println("Connection NOT Closed.");
        }
    }

    public void dbCreate(String filePath) {
        try {
            //Setup statement and scanner
            Statement stmt = dbConnection.createStatement();
            Scanner sc = new Scanner(new File(filePath));
            sc.useDelimiter("\n");

            //Read and execute each create command from the file
            System.out.println("Started adding tables...");
            String sqlStatement = "";
            while(sc.hasNext()) {
                sqlStatement = sc.next();
                String tableName = sqlStatement.substring(13, sqlStatement.indexOf(" ("));
                int result = stmt.executeUpdate(sqlStatement);
                if(result == 0) {
                    System.out.println(String.format("Added %s to database", tableName));
                }
            }
            sc.close();
            System.out.println("Finished adding tables...\n");
        } catch (Exception e) {
            System.out.println("Error Detected:");
            e.printStackTrace();
            System.exit(0);
        }
    }

    //TODO: Finish data writing function
    public void dbFill(String tableName, String filePath) {
        System.out.println(String.format("I will fill in %s with data from %s!", tableName, filePath));

    }

    //TODO: Finish writing drop function
    public void dbDrop(String filePath) {
        System.out.println(String.format("I will drop tables with data from %s!", filePath));
    }

}