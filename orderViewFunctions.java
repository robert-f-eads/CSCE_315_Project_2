import java.util.Vector;
import java.sql.ResultSet;

/**
 * @author Shreyes, Robert, Alexia, Emma
 */
public class orderViewFunctions {
   /* serverViewFunctions serverFunctions;
    dbFunctions dbConnection;

    /**
     * 
     * @return order tickets as received from dbFunctions
     */
    public Vector<orderTicketInfo> getOrders() {
        Vector<orderTicketInfo> orderTickets = new Vector<>();
        try {
			dbConnection.createDbConnection();
			String sqlStatement = "SELECT * FROM orderTickets";
			ResultSet results = dbConnection.dbQuery(sqlStatement);
			while(results.next()) {
				orderTicketInfo oti = serverFunctions.getOrderTicket(results.getInt("id")); // serverFunctions.getIngredient(results.getInt("id")); 
                orderTickets.add(oti);
			}
		} catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        } 
        return orderTickets;
    }

    public orderViewFunctions() {
        serverFunctions = new serverViewFunctions();
        dbConnection = new dbFunctions();
    }*/
}