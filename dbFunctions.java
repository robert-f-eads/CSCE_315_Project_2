import java.sql.*;

public class dbFunctions {
    
    Connection dbConnection = null;
    dbLogin user = new dbLogin();

    //Member Functions
    void createDbConnection() {
        try {
            dbConnection = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315_901_2", user.user, user.pswd);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }//End createDbConnection

    void closeDbConnection() {
        try {
            dbConnection.close();
            System.out.println("Connection Closed.");
        } catch(Exception e) {
            System.out.println("Connection NOT Closed.");
        }
    }//End closeDbConnection

    private boolean checkDbConnection() {
        if(dbConnection != null) {
            return true;
        }
        return false;
    }//End checkDbConnection

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
    }//End dpUpsert

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
    }//dbQuery

}