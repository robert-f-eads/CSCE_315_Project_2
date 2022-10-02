public class DbSetup {
    /*
        Main function used in conjunction with DbTools to setup and populate database for project 2
    */

    public static void main(String args[]) {
        
        if (args.length != 2) {
            System.out.println("Not enough arguments provided. Please provide username and password at runtime.");
            System.exit(0);
        }

    
        //Open connection to database and database functions
        DbTools tool = new DbTools();
        tool.openDbConnection(args[0], args[1]);
        if(tool.hasDbConnection()) {System.out.println("Connection to database successful");}


        //Create database tables from names in file
        tool.dbCreate("filepath");

        
        //Fill tables with data
        tool.dbFill("table1", "table1data.csv");


        //Drop database tables from names in file
        tool.dbDrop("filepath");


        //Close database connection
        tool.closeDbConnection();
    }
}