import java.util.Scanner;
import java.io.File;
import java.io.IOException;

public class FeeManager extends DateCalculations {
    public static final String ADMIN = "admin";
    public static final String TODAYS_DATE = todaysDate();
    public static String userName, userPassword, studentName, studentClass, studentDiv;
    public static int arrayNo, usrch, studentRollNo;
    public static String[][] classArray= new String[100][100];
    public static File classFile;

    public static void main( String[] args ) throws IOException {
        Scanner read = new Scanner( System.in );
        FeeManager object = new FeeManager();

        System.out.print( "Enter your username: " );
        userName = read.next();
        System.out.println();
        System.out.print( "Enter your password: " );
        userPassword = read.next();
        System.out.println();

        if( authenticate( userName, userPassword) ) {
            if( ! ( userName.equals( "admin" ) ) ) {
                System.out.print( "Enter the nam of the student: ");
                studentName = read.next();
                System.out.println();
                System.out.print("Enter the class of the student: ");
                studentClass = read.next();
                System.out.println();
                System.out.print("Enter the division of the student: ");
                studentDiv = read.next();
                System.out.println();

                object.cloneDatabase();
                do {
                    do {
                        System.out.println("1.Check details");
                        System.out.println("2.Pay fees");
                        System.out.println("0.Exit");
                        System.out.print("Enter your choice: ");
                        usrch = read.nextInt();
                    } while (usrch != 1 || usrch != 2 || usrch != 0);

                    switch (usrch) {
                        case 1:
                            object.displayDetails();
                            break;
                        case 2:
                            object.payFees();
                            break;
                    }
                } while( usrch != 0 );
            } else {
                object.findTodaysTransactions();
            }
        }
    }

    public static void cloneDatabase() throws IOException {
        int i, j;

        classFile = new File( "C:\\data\\schoolfeemanager\\database\\" + studentClass + studentDiv + ".txt" );

        if( classFile.exists() ) {
            Scanner readFile = new Scanner( classFile );

            for( i = 0; ( ! ( readFile.next().equals( "null") ); i++ ) {
                for( j = 0; j < 5; j ++ ) {
                    classArray[i][j] = readFile.next();
                }
            }
        } else {
            System.out.println("error:class not found");
        }
    }

    public static void displayDetails() {
        int i, j;
        for( i = 0; ; i ++ ) {
            if( classArray[i][0].equals( studentName ) ) {
                studentRollNo = i + 1;
                System.out.println("Student name:\t" + studentName);
                System.out.println("Roll no:\t\t" + studentRollNo);
                for (j = 2; j < 5; j++) {
                    if (classArray[i][j].equals("notpaid")) {
                        switch (j) {
                            case 2:
                                System.out.println("First term fees not paid");
                                break;
                            case 3:
                                System.out.println("Second term fees not paid");
                                break;
                            case 4:
                                System.out.println("Third term fees not paid");
                                break;
                        }
                    }
                }
                break;
            } else if( classArray[i][0].equals( "null" ) ) {
                break;
            }
        }
    }

    public static void payFees() {
        int i = studentRollNo  --, j, currentTerm;

        currentTerm = findCurrentTerm();
    }

    private static boolean authenticate ( String userName, String password ) throws IOException {
        File user = new File( "C:\\data\\schoolfeemanager\\users\\" + userName + ".txt" );

        if ( ! ( user.exists() ) ) {
            System.out.println( "User does not exist" );
            return false;
        }
        else {
            Scanner passwordRead = new Scanner( user );

            long passHashCode = 1010101010101010L, passHashCodeRef;
            int i, passLength = password.length();

            passHashCodeRef =  Long.parseLong( passwordRead.next() );

            for ( i = 0; i < passLength; i++ ) {
                char ch = password.charAt( i );
                passHashCode = passHashCode + ch;
            }

            if ( passHashCode == passHashCodeRef ) {
                System.out.println( "Authenticated" );
                return true;
            }
            else {
                System.out.println( "Incorrect password entered" );
                return false;
            }
        }
    }
}

/**
 * Term 1 -> June 1 -- August 31
 * Term 2 -> September 1 -- December 31
 * Term 3 -> January 1 -- March 31
 */