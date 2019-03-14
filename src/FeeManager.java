/**
 * @author Allen Shibu
 * @author Albert Joseph Sheen
 * SchoolFeeManager helps small schools to store payment detaisl of fees of three terms.
 * All the files required for this program are stored in C:\data\schoolfeemanager
 * Passwords of users are stored in C:\\data\\schoolfeemanger\\users\\
 * Details of payment of each class is stored in C:\data\schoolfeemanager\database
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;

public class FeeManager extends DateCalculations {
    public static final String ADMIN = "admin";
    public static final String TODAYS_DATE = todaysDate();
    public static String userName, userPassword, studentName, studentClass, studentDiv;
    public static int  usrchMenu1, usrchMenu2, studentRollNo, daysLate = 0;
    public static String[][] classArray= new String[100][100];
    public static File classFile;
    public static boolean term1PaymentStatus,term2PaymentStatus,term3PaymentStatus;

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
                    do {
                        System.out.print( "Enter 1 to continue or 0 to exit: " );
                        usrchMenu1 = read.nextInt();
                        System.out.println();
                        System.out.print( "Enter the name of the student: " );
                        studentName = read.next();
                        System.out.println();
                        System.out.print( "Enter the class of the student: " );
                        studentClass = read.next();
                        System.out.println();
                        System.out.print( "Enter the division of the student: " );
                        studentDiv = read.next();
                        System.out.println();

                        cloneDatabase();
                        do {
                            do {
                                System.out.println( "1.Check details" );
                                System.out.println( "2.Pay fees" );
                                System.out.println( "0.Exit" );
                                System.out.print( "Enter your choice: " );
                                usrchMenu2 = read.nextInt();
                                System.out.println( usrchMenu2 );
                            } while( usrchMenu2 != 1 || usrchMenu2 != 2 || usrchMenu2 != 0 );

                            switch( usrchMenu2 ) {
                                case 1:
                                    displayDetails();
                                    break;
                                case 2:
                                    payFees();
                                    break;
                            }
                        } while( usrchMenu2 != 0 );
                    } while( usrchMenu1 != 0 );
                }
            else {
                    //object.displayTodaysTransactions();
                }
        }
    }

    public static void cloneDatabase() throws IOException {
        /**
         * Clones the details of payment of each class into a double dimensional array for better handling.
         * The details of payment of each class is stored in C:\data\schoolfeemanager\database\ inside files named
         * after the class in the following format.
         * (nameofthestudentinlowercasewithoutspaces) (dateofbirth) (paidornotpaidfirsttermfee) (paidornotpaidsecondtermfee) (paidornotpaidthirdtermfee)
         */
        int i = 0, j = 0;

        classFile = new File( "C:\\data\\schoolfeemanager\\database\\" + studentClass + studentDiv + ".txt" );

        if( classFile.exists() ) {
            FileReader fw = new FileReader( classFile );
            BufferedReader readFile = new BufferedReader( fw );

            String text = Integer.toString( readFile.read() );
            while( !( text.equals( "null" ) ) && i < 100 ) {
                for( j = 0; j < 5; j ++ ) {
                    classArray[i][j] = Integer.toString( readFile.read() );
                }
                text = Integer.toString( readFile.read() );
                i++;
            }
        } else {
            System.out.println("error:class not found");
        }
    }

    public static void displayDetails() {
        /**
         * Displays details of the required student
         */
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
        //Yet to be completed
        /**
         * Registers the payment of fees after checking whether payment is late and other things.
         */
        int i = studentRollNo  --, j, currentTerm;

        currentTerm = findCurrentTerm();
        j = currentTerm--;

        if( classArray[i][j].equals( "paid" ) ) {
            term1PaymentStatus = true;
        } else if( classArray[i][j].equals( "notpaid" ) ) {
            term1PaymentStatus = false;
        }
        if( classArray[i][j + 1].equals( "paid" ) ) {
            term2PaymentStatus = true;
        } else if( classArray[i][j + 1].equals( "notpaid" ) ) {
            term2PaymentStatus = false;
        }
        if( classArray[i][j + 2].equals( "paid" ) ) {
            term3PaymentStatus = true;
        } else if( classArray[i][j + 2].equals( "notpaid" ) ) {
            term3PaymentStatus = false;
        }


        if( term1PaymentStatus || term2PaymentStatus || term3PaymentStatus ) {
            System.out.println( "Fees have already been paid for the term " + currentTerm );
        } else if( ! ( term1PaymentStatus ) ) {
            if( isDueDateOver() ) {
                daysLate = findDaysLate();
            }
        }
    }

    public static void displayTodaysTransactions() {
        //Yet to be written
        /**
         * Displays the day's transactions for the administrator
         */
    }

    public static void saveDatabase() {
        //Yet to be written
        /**
         * Saves the details of payment to the database on the computer from the array after its use.
         */
    }

    private static boolean authenticate ( String userName, String password ) throws IOException {
        /**
         * Checks the username and password enterd by the user against a hashed copy of the password stored on th machine
         * Hashed passwords are stored in C:\\data\\schoolfeemanager\\users\\ inside files named after each user.
         * Hashing starts with an initial value of 1010101010101010 and the ASCII code of each charachter in the
         * password is added to it.
         * Function returns true if the username and password are correct; else false.
         */
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