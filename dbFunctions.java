import java.sql.*;
import src.dbLogin.dbLogin;

public class dbFunctions {
    
    Connection dbConnection = null;

    dbLogin user = new dbLogin();

    void createDbConnection() {
        try {
            dbConnection = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315_901_2", user.user, user.pswd);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    /**
     * close the database connection
     */
    void closeDbConnection() {
        try {
            dbConnection.close();
            System.out.println("Connection Closed.");
        } catch(Exception e) {
            System.out.println("Connection NOT Closed.");
        }
    }

    /**
     * 
     * @return if there exists a connection to the database that is valid
     */
    private boolean checkDbConnection() {
        if(dbConnection != null) {
            return true;
        }
        return false;
    }

    /**
     * 
     * @param sqlStatement a statement that will either update or insert into the database
     * @return an int representing success or failure
     */
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

    /**
     * 
     * @param sqlStatement a statement that we will use to query on the database
     * @return a ResultSet of all the items retrieved to dbQuery
     */
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