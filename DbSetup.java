public class DbSetup {
    /*
        Main function used in conjunction with DbTools to manage database for project 2
        Compile: javac *.java

        NOTE: Before running program, please ensue your pgpass.conf file is setup in your enviornment 
                using formatting "csce-315-db.engr.tamu.edu:*:csce315_901_2:your_username:your_password"
        Windows: %APPDATA%\postgresql\pgpass.conf
        Mac/Linux: ~/pgpass.conf

        Run Program:
        Windows: java -cp ".;postgresql-42.2.8.jar" -Dusername=your_username_here -Dpassword=your_password_here DbSetup filepath_to_tableMetaData directorypath_to_tableData filepath_to_query_file
        Mac/Linux: java -cp ".:postgresql-42.2.8.jar" -Dusername=your_username_here -Dpassword=your_password_here DbSetup filepath_to_tableMetaData directorypath_to_tableData filepath_to_query_file
    */

    public static void main(String args[]) {
        
        //Validate input
        String username = System.getProperty("username");
        if(username == null) {
            System.out.println("Please use argument \"-Dusername=your_username_here\" before executable name to enable database connection");
            System.exit(1);
        }
        String password = System.getProperty("password");
        if(password == null) {
            System.out.println("Please use argument \"-Dpassword=your_password_here\" before executable name to enable database connection");
            System.exit(1);
        }
        if(args.length < 3) {
            System.out.println("To use the program please enter the file path of table metadata file and table data directory path as a command line argument after the excutable");
            System.exit(1);
        }
        else if(args.length == 3) {
            System.out.println("To use the program please enter one or more of the following as a command line argument after the file and directory path [create|fill|drop|query]");
            System.exit(1);
        }


        DbTools dbtool = new DbTools();

        //Open and store connection to database
        dbtool.openDbConnection(username, password);
        if(dbtool.hasDbConnection()) {System.out.println("\nConnection to database successful");}


        //Import table metadata into runtime storage
        dbtool.importMetaData(args[0]);
        

        //Perform orperations based on runtime input
        for(int i = 3; i < args.length; i++){
            switch(args[i]) {
                case "create":
                    dbtool.dbCreate();
                    break;
                case "fill":
                    dbtool.dbFill(args[1], username);
                    break;
                case "drop":
                    dbtool.dbDrop();
                    break;
                case "query":
                    dbtool.dbRunQueries(args[2]);
                    break;
                default:
                    System.out.println("\nUnrecognized option, please select one or more of the following options [create|fill|drop|query]");
            }

        }
        
        //Close database connection
        dbtool.closeDbConnection();
    }
}