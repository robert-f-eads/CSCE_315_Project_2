import java.sql.*;
import java.io.*;
import java.util.*;

public class DbTools {

    //Variables    
    private String dbName;
    private String dbConnectionString;
    private Connection dbConnection = null;
    private Vector<TableInfo> infoForTables = new Vector<TableInfo>();
    private Vector<String> users = new Vector<String>();

    //Constructor
    public DbTools() {
        dbName = "csce315_901_2";
        dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;
        users.add("csce315_901_eads");
        users.add("csce315_901_ong");
        users.add("csce315_901_kaliyur");
        users.add("csce315_901_hassan");
    } 

    //Member functions
    public void openDbConnection(String username, String password) {
        try {
            dbConnection = DriverManager.getConnection(dbConnectionString, username, password);
            users.remove(users.indexOf(username));
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

                for(String user : users) {
                    stmt.executeUpdate(String.format("GRANT ALL ON %s TO %s", currentTable.tableName, user));
                }
                
            }

            System.out.println("Finished adding tables...");
        } catch (Exception e) {
            System.out.println("Error Detected:");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void dbFill(String directoryPath, String username) {
        try{
            String executable = "powershell.exe";
            String login = String.format("psql -h csce-315-db.engr.tamu.edu -U %s -d %s", username, dbName);

            for(TableInfo currentTable : infoForTables) {
                System.out.print(String.format("\nAdding entries to %1$s from file %1$s.csv: ", currentTable.tableName));

                //Format and execute psql command
                String command = String.format("%s %s -c '\\COPY %3$s FROM \"%4$s%3$s.csv\" csv'", executable, login, currentTable.tableName, directoryPath);
                Process powerShellProcess = Runtime.getRuntime().exec(command);                
                powerShellProcess.getOutputStream().close();
                String line;

                //Check for output
                BufferedReader stdout = new BufferedReader(new InputStreamReader(powerShellProcess.getInputStream()));
                if((line = stdout.readLine()) != null) {
                    System.out.println(line);  
                    while((line = stdout.readLine()) != null) {System.out.println(line);}
                    stdout.close();
                }

                //Check for errors
                BufferedReader stderr = new BufferedReader(new InputStreamReader(powerShellProcess.getErrorStream()));
                if((line = stderr.readLine()) != null) {
                    System.out.println(line);  
                    while((line = stderr.readLine()) != null) {System.out.println(line);}
                    stderr.close();
                }

                if (currentTable.tableName != "productsToIngredients") {
                    Statement stmt = dbConnection.createStatement();
                    String sqlStatement = String.format("SELECT id FROM %s ORDER BY id DESC LIMIT 1", currentTable.tableName); 
                    ResultSet result = stmt.executeQuery(sqlStatement);
                    result.next();
                    ////ALTER SEQUENCE <table>_id_seq RESTART WITH <number>
                    int temp =  result.getInt("id");
                    sqlStatement = String.format("ALTER SEQUENCE %s_id_seq RESTART WITH %d", currentTable.tableName, temp + 1);
                    int results = stmt.executeUpdate(sqlStatement); 
                    
                }
            


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

            for(int i = infoForTables.size()-1; i >= 0; i--){
                String sqlStatement = String.format("DROP TABLE IF EXISTS %s", infoForTables.get(i).tableName);
                int result = stmt.executeUpdate(sqlStatement);
                System.out.println(String.format("Dropped %s from the database", infoForTables.get(i).tableName));
            }

            System.out.println("Finished dropping tables...");
        } catch (Exception e) {
            System.out.println("Error Detected:");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void dbRunQueries(String filePath) {
        try {
            Statement stmt = dbConnection.createStatement();
            Scanner sc = new Scanner(new File(filePath));
            String sqlStatement = "";
            System.out.println("\nStarting file reading...");
            
            while(sc.hasNext()) {
                sqlStatement = sc.nextLine();
                
                //Run queries from file
                System.out.println(String.format("Running query: %s", sqlStatement));
                ResultSet result = stmt.executeQuery(sqlStatement);

                //Get metadata and print results
                ResultSetMetaData metaData = result.getMetaData();
                int numColumns = metaData.getColumnCount();

                //Print table result labels
                for (int i = 1; i <= numColumns; i++) {
                    if (i > 1) {
                        System.out.print(",  ");
                    }
                    System.out.print(metaData.getColumnName(i));
                }
                System.out.println("");

                //Print results
                while (result.next()) {
                    for (int i = 1; i <= numColumns; i++) {
                        if (i > 1) {
                            System.out.print(",  ");
                        }
                        System.out.print(result.getString(i));
                    }
                    System.out.println("");
                }
                System.out.println("");
            }
            sc.close();
            System.out.println("Finished file reading...");

        } catch (Exception e) {
            System.out.println("Error Detected:");
            e.printStackTrace();
            System.exit(1);
        }
    }
}