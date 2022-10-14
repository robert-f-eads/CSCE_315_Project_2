import java.util.Vector;
import java.sql.ResultSet;

public class inventoryViewFunctions {
    serverViewFunctions serverFunctions;
    dbFunctions dbConnection;

    public Vector<ingredient> getIngredients() {
        return getIngredients("");
    }


    public Vector<material> getMaterials() {
        return getMaterials("");
    }

    // filter by a search term
    public Vector<ingredient> getIngredients(String filter) {
        Vector<ingredient> ingredients = new Vector<>();
        try {
			dbConnection.createDbConnection();
			String sqlStatement = String.format("SELECT id FROM ingredients WHERE name ILIKE '%s%s%s'", "%", filter, "%");
			ResultSet results = dbConnection.dbQuery(sqlStatement);
			while(results.next()) {
				ingredient i = serverFunctions.getIngredient(results.getInt("id")); 
                ingredients.add(i);
			}
		} catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        } 
        return ingredients;
    }

    // filter by a search term
    public Vector<material> getMaterials(String filter) {
        Vector<material> materials = new Vector<>();
        try {
			dbConnection.createDbConnection();
			String sqlStatement = String.format("SELECT id FROM materials WHERE name ILIKE '%s%s%s'", "%", filter, "%");
			ResultSet results = dbConnection.dbQuery(sqlStatement);
			while(results.next()) {
				material m = serverFunctions.getMaterial(results.getInt("id")); 
                materials.add(m);
			}
		} catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        } 
        return materials;
    }

    public inventoryViewFunctions() {
        serverFunctions = new serverViewFunctions();
        dbConnection = new dbFunctions();
    }
}