import java.util.Vector;
import java.sql.ResultSet;

/**
 * @author Shreyes Kaliyur
 */
public class inventoryViewFunctions {
    serverViewFunctions serverFunctions;
    dbFunctions dbConnection;

    /**
     * Get the ingredients in the database
     * @return all the ingredients in the database
     */
    public Vector<ingredient> getIngredients() {
        return getIngredients("");
    }

    /**
     * Get the materials in the database
     * @return all the materials in the database
     */
    public Vector<material> getMaterials() {
        return getMaterials("");
    }

    // filter by a search term
    /**
     * Filter the ingredients in the database by a search term
     * @param filter what to limit the database search for ingredients by
     * @return all ingredients which have filter as part of their name
     */
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

    /**
     * Filter the materials in the database by the search term
     * @param filter what to limit the database search for materials by
     * @return all materials which have the filter as part of their name
     */
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

    /**
     * initialize the server functions and db connection
     */
    public inventoryViewFunctions() {
        serverFunctions = new serverViewFunctions();
        dbConnection = new dbFunctions();
    }
}