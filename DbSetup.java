public class DbSetup {
    /*
        Main function used in conjunction with DbTools to manage database for project 2
        Compile: javac *.java

        Fill tables (Default):
        Windows: java -cp ".;postgresql-42.2.8.jar" DbSetup "csce315_901_yourlastname" "password"
        Mac/Linux: java -cp ".:postgresql-42.2.8.jar" DbSetup "csce315_901_yourlastname" "password"

        Additional arguments placed before "DbSetup":
        "-DrunCreate=true" - Create new tables from info in file
        "-DrunDrop=true" - Drop table from info in file
    */

    public static void main(String args[]) {
        
        if (args.length != 2) {
            System.out.println("Not enough arguments provided. Please provide username and password at runtime.");
            System.exit(0);
        }

        //Checking additional flags
        String runCreate = System.getProperty("runCreate");
        if(runCreate == null) {runCreate= "false";}
        String runDrop = System.getProperty("runDrop");
        if(runDrop == null) {runDrop = "false";}

    
        //Open connection to database and database functions
        DbTools dbtool = new DbTools();
        dbtool.openDbConnection(args[0], args[1]);
        if(dbtool.hasDbConnection()) {System.out.println("\nConnection to database successful");}

        dbtool.importData("dbInfo\\dbTableInfo.txt");


        /*//Create database tables from names in file
        if(runCreate.equals("true")) {
            //System.out.println("Will create tables");
            dbtool.dbCreate("dbInfo\\dbTableCreateInfo.txt");
        }
        
        //TODO - Write wrapping for write tables 
        //Fill tables with data
        dbtool.dbFill("table1", "table1data.csv");


        //Drop database tables from names in file
        if(runDrop.equals("true")) {
            //System.out.println("Will drop tables");
            dbtool.dbDrop("dbInfo\\dbTableDropInfo.txt");
        }


        //Close database connection
        dbtool.closeDbConnection();*/
    }
}