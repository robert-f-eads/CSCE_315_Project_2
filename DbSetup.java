public class DbSetup {
    /*
        Main function used in conjunction with DbTools to setup and populate database for project 2
        Compile: javac *.java

        Run:
        Windows: java -cp ".;postgresql-42.2.8.jar" DbSetup "csce315_901_yourlastname" "password"
        Mac/Linux: java -cp ".:postgresql-42.2.8.jar" DbSetup "csce315_901_yourlastname" "password"
    */

    public static void main(String args[]) {
        
        if (args.length != 2) {
            System.out.println("Not enough arguments provided. Please provide username and password at runtime.");
            System.exit(0);
        }

    
        //Open connection to database and database functions
        DbTools tool = new DbTools();
        tool.openDbConnection(args[0], args[1]);
        if(tool.hasDbConnection()) {System.out.println("\nConnection to database successful\n");}


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