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
            System.exit(1);
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

    public void importMetaData(String filePath) {
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
                if(sections.length == 4) {tempInfo.modifiers = sections[3].split(", ");}
                
                infoForTables.add(tempInfo);
                System.out.println(String.format("Imported %s metadata", tempInfo.tableName));
            }
            sc.close();
            System.out.println("Finished file reading...");

        } catch (Exception e) {
            System.out.println("Error Detected:");
            e.printStackTrace();
            System.exit(1);
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

                    //When no more modifiers left
                    if(i >= currentTable.modifiers.length) {
                        sqlStatement += String.format("%s %s", currentTable.attributes[i], currentTable.dataTypes[i]);
                    }
                    //When modifiers using place holder modifiers to skip a space
                    else if(i < currentTable.modifiers.length && currentTable.modifiers[i] == "NULL"){
                        sqlStatement += String.format("%s %s", currentTable.attributes[i], currentTable.dataTypes[i]); 
                    }
                    //Regular use
                    else {
                        sqlStatement += String.format("%s %s %s", currentTable.attributes[i], currentTable.dataTypes[i], currentTable.modifiers[i]);
                    }
                    if(i < currentTable.attributes.length - 1) {sqlStatement += ", ";}
                }
                sqlStatement += ")";

                int result = stmt.executeUpdate(sqlStatement);
                System.out.println(String.format("Added %s to database", currentTable.tableName));
            }

            System.out.println("Finished adding tables...");
        } catch (Exception e) {
            System.out.println("Error Detected:");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void dbFill(String directoryPath) {
        try{
            Statement stmt = dbConnection.createStatement();
            String sqlStatement = "";

            for(TableInfo currentTable : infoForTables) {
                System.out.println(String.format("\nAdding entries to %1$s from file %1$s.csv", currentTable.tableName));

                //Open each file in scanner
                Scanner sc = new Scanner(new File(String.format("%1$s%2$s.csv", directoryPath, currentTable.tableName)));
                int entrySum = 0;
                int rowCount = 0;

                //Reading and formatting each line in the opened file
                while(sc.hasNext()) 
                {
                    rowCount++;
                    String currentline = sc.nextLine();
                    String[] lineData = currentline.split(",");
                    String dataFormatting = "";

                    //Reading and formatting each data point properly
                    for(int i = 0; i < currentTable.dataTypes.length; i++) {
                        switch(currentTable.dataTypes[i]) {
                            case "INT":
                                dataFormatting += String.format("%d", Integer.parseInt(lineData[i]));
                                break;
                            case "DOUBLE":
                                dataFormatting += String.format("%.2f", Double.parseDouble(lineData[i]));
                                break;
                            default:
                                dataFormatting += String.format("'%s'", lineData[i]);
                        }
                        if(i < currentTable.dataTypes.length-1) {
                            dataFormatting += ", ";
                        }
                    }

                    //Reading a formatting each column name properly
                    String columnNameFormatting = "";
                    for(int i = 0; i < currentTable.attributes.length; i++) {
                        if(currentTable.dataTypes[i] == "SERIAL") {continue;}
                        else {
                            columnNameFormatting += String.format("%s", currentTable.attributes[i]);
                        }
                        
                        if(i < currentTable.attributes.length-1) {
                            columnNameFormatting += ", ";
                        }
                    }

                    //Creating sql statement
                    sqlStatement = String.format("INSERT INTO %s (%s) VALUES (%s)", currentTable.tableName, columnNameFormatting, dataFormatting);
                    int result = stmt.executeUpdate(sqlStatement);
                    entrySum += result;
                }
                sc.close();
                System.out.println(String.format("Successfully added %d entries to %s from file with %d rows", entrySum, currentTable.tableName, rowCount));
            } 
        } catch (Exception e) {
            System.out.println("Error Detected:");
            e.printStackTrace();
            System.exit(1);
        }
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
                System.out.println(String.format("Dropped %s from the database", currentTable.tableName));
            }

            System.out.println("Finished dropping tables...");
        } catch (Exception e) {
            System.out.println("Error Detected:");
            e.printStackTrace();
            System.exit(1);
        }
    }

}