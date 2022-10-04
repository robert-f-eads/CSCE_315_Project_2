import java.sql.*;
import java.io.*;
import java.util.*;


public class DbTools {

    //Variables    
    private String dbName;
    private String dbConnectionString;
    private Connection dbConnection = null;
    private Vector<TableInfo> infoForTables = new Vector<TableInfo>();

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

    public void importData(String filePath) {
        try {
            Scanner sc = new Scanner(new File(filePath));
            String tableMetadata = "";
            
            while(sc.hasNext()) {
                TableInfo tempInfo = new TableInfo();
                tableMetadata = sc.nextLine();

                //Spits table metadata into proper formatting
                String[] sections = tableMetadata.split(" : ");
                tempInfo.tableName = sections[0];
                tempInfo.attributes = sections[1].split(",");
                tempInfo.dataTypes = sections[2].split(",");
                tempInfo.modifiers = sections[3].split(",");

                infoForTables.add(tempInfo);
            }
            sc.close();

        } catch (Exception e) {
            System.out.println("Error Detected:");
            e.printStackTrace();
            System.exit(0);
        }
    }



    public void dbCreate(String filePath) {
        try {
            //Setup statement and scanner
            Statement stmt = dbConnection.createStatement();
            Scanner sc = new Scanner(new File(filePath));
            sc.useDelimiter("\n");

            //Read and execute each create command from the file
            System.out.println("\nStarted adding tables...");
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

    public void dbDrop(String filePath) {
        /*
            --------------------------------- WARNING ---------------------------------
            This is will drop all tables in function parameter file
            Double check there is not another solution before performing this action
        */
        try {
            //Setup statement and scanner
            Statement stmt = dbConnection.createStatement();
            Scanner sc = new Scanner(new File(filePath));
            sc.useDelimiter("\n");

            //Read and execute each drop command from the file
            System.out.println("\nStarted dropping tables...");
            String sqlStatement = "";
            while(sc.hasNext()) {
                sqlStatement = sc.next();
                String tableName = sqlStatement.substring(11);
                int result = stmt.executeUpdate(sqlStatement);
                if(result == 0) {
                    System.out.println(String.format("Dropped %s from the database", tableName));
                }
            }
            sc.close();
            System.out.println("Finished dropping tables...\n");
        } catch (Exception e) {
            System.out.println("Error Detected:");
            e.printStackTrace();
            System.exit(0);
        }
    }

}