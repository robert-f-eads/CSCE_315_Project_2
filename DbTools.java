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
            System.out.println("\nConnection Closed.");
        } catch(Exception e) {
            System.out.println("\nConnection NOT Closed.");
        }
    }

    public void importData(String filePath) {
        try {
            Scanner sc = new Scanner(new File(filePath));
            String tableMetadata = "";
            System.out.println("\nStarting file reading...");
            
            while(sc.hasNext()) {
                TableInfo tempInfo = new TableInfo();
                tableMetadata = sc.nextLine();

                //Spits table metadata into proper formatting
                String[] sections = tableMetadata.split(" : ");
                tempInfo.tableName = sections[0];
                tempInfo.attributes = sections[1].split(", ");
                tempInfo.dataTypes = sections[2].split(", ");
                tempInfo.modifiers = sections[3].split(", ");

                infoForTables.add(tempInfo);
                System.out.println(String.format("Imported %s metadata", tempInfo.tableName));
            }
            sc.close();
            System.out.println("Finished file reading...");

        } catch (Exception e) {
            System.out.println("Error Detected:");
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void dbCreate() {
        try {
            Statement stmt = dbConnection.createStatement();

            //Create each table from metadata
            System.out.println("\nStarted adding tables...");

             for(TableInfo currentTable : infoForTables) {
                String sqlStatement = String.format("CREATE TABLE %s (", currentTable.tableName); 

                for(int i = 0; i < currentTable.attributes.length; i++) {
                    sqlStatement += String.format("%s %s %s", currentTable.attributes[i], currentTable.dataTypes[i], currentTable.modifiers[i]);
                    if(i < currentTable.attributes.length - 1) {sqlStatement += ", ";}
                }
                sqlStatement += ")";

                int result = stmt.executeUpdate(sqlStatement);
                if(result == 0) {
                    System.out.println(String.format("Added %s to database", currentTable.tableName));
                }

            }
            System.out.println("Finished adding tables...");
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

    public void dbDrop() {
        /*
            --------------------------------- WARNING ---------------------------------
            This is will drop all tables in table metadata vector
            Double check there is not another solution before performing this action
        */
        try {
            Statement stmt = dbConnection.createStatement();

            //Drop each table based on metadata
            System.out.println("\nStarted dropping tables...");

            for(TableInfo currentTable : infoForTables) {
                String sqlStatement = String.format("DROP TABLE IF EXISTS %s", currentTable.tableName);
                int result = stmt.executeUpdate(sqlStatement);
                if(result == 0) {
                    System.out.println(String.format("Dropped %s from the database", currentTable.tableName));
                }
            }
            System.out.println("Finished dropping tables...");
        } catch (Exception e) {
            System.out.println("Error Detected:");
            e.printStackTrace();
            System.exit(0);
        }
    }

}