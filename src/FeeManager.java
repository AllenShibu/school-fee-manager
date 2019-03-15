/**
 * @author Allen Shibu
 * @author Albert Joseph Sheen
 * SchoolFeeManager helps small schools to store payment detaisl of fees of three terms.
 * All the files required for this program are stored in C:\data\schoolfeemanager
 * Passwords of users are stored in C:\\data\\schoolfeemanger\\users\\
 * Details of payment of each class is stored in C:\data\schoolfeemanager\database
 */

import java.io.*;
import java.util.Scanner;

public class FeeManager extends DateCalculations {
    public static final String ADMIN = "admin";
    public static final String TODAYS_DATE = todaysDate();
    public static String userName, userPassword, studentName, studentClass, studentDiv;
    public static int  usrchMenu1, usrchMenu2, studentRollNo, daysLate = 0, classTotalStudents = 0, currentTerm, amountToPay = 0, studentClassInt, fine;
    public static String[][] classArray= new String[100][5];
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
                        System.out.print( "Enter the name of the student: " );
                        studentName = read.next();
                        System.out.println();
                        System.out.println( "Enter the class of the student" );
                        System.out.print( "Note! -1 for L.K.G and -2 for U.K.G: " );
                        studentClass = read.next();
                        studentClassInt = Integer.parseInt( studentClass );     //For later use
                        System.out.println();
                        System.out.print( "Enter the division of the student: " );
                        studentDiv = read.next();
                        System.out.println();

                        cloneDatabase();
                        if( checkStudentExists() ) {
                            do {
                                do {
                                    System.out.println( "1.Check details" );
                                    System.out.println( "2.Pay fees" );
                                    System.out.println( "0.Exit" );
                                    System.out.print( "Enter your choice: " );
                                    usrchMenu2 = read.nextInt();
                                    System.out.println( usrchMenu2 );
                                } while( ! ( usrchMenu2 == 1 || usrchMenu2 == 2 || usrchMenu2 == 0 ) );

                                switch( usrchMenu2 ) {
                                    case 1:
                                        displayDetails();
                                        break;
                                    case 2:
                                        payFees();
                                        break;
                                }
                            } while( ! ( usrchMenu2 == 0 ) );
                        }
                        System.out.print( "Enter 1 to continue or 0 to exit the app: " );
                        usrchMenu1 = read.nextInt();
                        System.out.println();
                    } while( ! ( usrchMenu1 == 0 ) );
                }
            else {
                    //displayTodaysTransactions();
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
            Scanner readFile = new Scanner( classFile );
            readFile.useDelimiter( "&&" );

            while( readFile.hasNext() ) {
                for( j = 0; j < 5; j ++ ) {
                    classArray[i][j] = readFile.next().trim();
                }
                if( classArray[i][0].equals( "null" ) ) {
                    break;
                }
                i++;
            }
            classTotalStudents = i;
        } else {
            System.out.println("error:class not found");
        }
    }

    public static boolean checkStudentExists() {
        int i;
        for( i = 0; i < classTotalStudents; i ++ ) {
            if( classArray[i][0].equals( studentName ) ) {
                studentRollNo = i + 1;
                return true;
            }
        }
        System.out.println( "error:student does not exist" );
        return false;
    }

    public static void displayDetails() {
        /**
         * Displays details of the required student
         */
        int i, j;
        for( i = 0; i < classTotalStudents ; i ++ ) {
            if( classArray[i][0].equals( studentName ) ) {
                System.out.println("Student name: " + studentName);
                for (j = 2; j < 5; j++) {
                    if (classArray[i][j].equals("notpaid")) {
                        System.out.println( classArray[i][j]);
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
            }
        }
    }

    public static void payFees() {
        //Yet to be completed
        /**
         * Registers the payment of fees after checking whether payment is late and other things.
         */
        Scanner read = new Scanner( System.in );
        int i = studentRollNo - 1, j;
        char usrch = 'N';

        currentTerm = findCurrentTerm();
        System.out.println( currentTerm );
        j = currentTerm - 1;

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
        if( classArray[i][j + 1].equals( "paid" ) ) {
            term3PaymentStatus = true;
        } else if( classArray[i][j + 2].equals( "notpaid" ) ) {
            term3PaymentStatus = false;
        }


        if( term1PaymentStatus || term2PaymentStatus || term3PaymentStatus ) {
            System.out.println( "Fees have already been paid" );
        } else {
            if( isDueDateOver() ) {
                daysLate = findDaysLate();
                fine = daysLate * 10;
            }
            System.out.println( "Term " + currentTerm + " fee payment" );
            amountToPay += calculateFeeAmount();
            amountToPay += fine;
            System.out.println( "Base payment rate: " + calculateFeeAmount() );
            System.out.println( "Fine for " + findDaysLate() + " days late: " + fine );
            System.out.println( "Total amount to pay: " + amountToPay );
            System.out.println( "Enter Y to proceed or N to cancel: " );
            usrch = read.next().charAt( 0 );
            System.out.println();
            if( usrch == 'N' ) {
                System.out.println( "Canceling payment..." );
            } else {
                classArray[i][j] = "paid";
                System.out.println( "Payment successful..." );
                updateDataBase();
            }
        }
    }

    public static int calculateFeeAmount() {
        Scanner read = new Scanner( System.in );
        switch( studentClassInt ) {
            case -1:
                return 13000;
            case -2:
                return 12880;
            case 1:
                return 13350;
            case 2:
                return 13350;
            case 3:
                return 13710;
            case 4:
                return 13710;
            case 5:
                return 13710;
            case 6:
                return 13463;
            case 7:
                return 13463;
            case 8:
                return 14003;
            case 9:
                return 16270;
            case 10:
                return 14245;
            case 11:
                return 71303;
            case 12:
                return 51192;
            default:
                System.out.println( "error:can not determine class automatically" );
                System.out.print( "Please enter class manually: " );
                int temp = read.nextInt();
                System.out.println();
                return temp;
        }
    }

    public static void updateDataBase() {
        int i, j;

        FileWriter fw = new FileWriter( classFile );
        BufferedWriter bw = new BufferedWriter( fw );
        PrintWriter write =new PrintWriter( bw );

        for( i = 0; i < classTotalStudents; i ++ ) {
            for( j = 0l j < 5; j++ ) {
                
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
        return true;
        /**
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
         */
    }
}