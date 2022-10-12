import java.sql.*;

public class dbFunctions {
    //TODO - Create connection username and password object here
    Connection dbConnection = null;

    //Member Functions
    void createDbConnection() {
        try {
            dbConnection = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315_901_2","", "");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    void closeDbConnection() {
        try {
            dbConnection.close();
            System.out.println("Connection Closed.");
        } catch(Exception e) {
            System.out.println("Connection NOT Closed.");
        }
    }

    private boolean checkDbConnection() {
        if(dbConnection != null) {
            return true;
        }
        return false;
    }

    int dbUpsert(String sqlStatement) {
        int result = -1;
        try {
            if(checkDbConnection()) {
                Statement stmt = dbConnection.createStatement();
                result = stmt.executeUpdate(sqlStatement);
            }    
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return result; 
    }

    ResultSet dbQuery(String sqlStatement) {
        ResultSet result = null;
        try {
            if(checkDbConnection()) {
                Statement stmt = dbConnection.createStatement();
                result = stmt.executeQuery(sqlStatement);    
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return result;
    }

}