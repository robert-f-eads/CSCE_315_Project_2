public class DbSetup {
    /*
        Main function used in conjunction with DbTools to setup and populate database for project 2
<<<<<<< HEAD
        Compile: javac *.java

        Run:
        Windows: java -cp ".;postgresql-42.2.8.jar" DbSetup "csce315_901_yourlastname" "password"
        Mac/Linux: java -cp ".:postgresql-42.2.8.jar" DbSetup "csce315_901_yourlastname" "password"
=======
>>>>>>> e9fed09e71174c5ee0a7564b5c43c31b978ed943
    */

    public static void main(String args[]) {
        
        if (args.length != 2) {
            System.out.println("Not enough arguments provided. Please provide username and password at runtime.");
            System.exit(0);
        }

    
        //Open connection to database and database functions
        DbTools tool = new DbTools();
        tool.openDbConnection(args[0], args[1]);
<<<<<<< HEAD
        if(tool.hasDbConnection()) {System.out.println("\nConnection to database successful\n");}
=======
        if(tool.hasDbConnection()) {System.out.println("Connection to database successful");}
>>>>>>> e9fed09e71174c5ee0a7564b5c43c31b978ed943


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