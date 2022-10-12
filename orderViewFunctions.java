import java.util.Vector;
import java.sql.ResultSet;

public class orderViewFunctions {
    serverViewFunctions serverFunctions;
    dbFunctions dbConnection;

    public Vector<orderTicketInfo> getOrders() {
        try {
			dbConnection.createDbConnection();
			String sqlStatement = "SELECT * FROM orderTickets";
			ResultSet results = dbConnection.dbQuery(sqlStatement);
			while(results.next()) {
				orderTicketInfo oti = null; // serverFunctions.getIngredient(results.getInt("id")); 
                ingredients.add(i);
			}
		} catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        } 
        return new Vector<orderTicketInfo>();
    }

    public orderViewFunctions() {
        serverFunctions = new serverViewFunctions();
        dbConnection = new dbFunctions();
    }
}