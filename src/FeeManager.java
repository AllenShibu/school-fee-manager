/**
 * @author Allen Shibu
 * @author Albert Joseph Sheen
 * SchoolFeeManager helps small schools to store payment details of fees of three terms.
 * All the files required for this program are stored in C:\data\schoolfeemanager
 * Passwords of users are stored in C:\\data\\schoolfeemanger\\users\\
 * Details of payment of each class is stored in C:\data\schoolfeemanager\database
 */

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.File;
import java.util.Scanner;

public class FeeManager extends DateCalculations {
    public static final String TODAYS_DATE = todaysDate();
    public static String userName, userPassword, studentName, studentClass, studentDiv;
    public static int studentRollNo, daysLate = 0, classTotalStudents = 0, currentTerm, amountToPay = 0, studentClassInt, fine;
    //Variables for user's choice in menus
    public static int usrchMenu1, usrchMenu2;
    //Array to store details of class for better data handling
    public static String[][] classArray= new String[100][5];
    public static File classFile, classFeeFile;
    //Variables indicating payment status of each term
    public static boolean term1PaymentStatus,term2PaymentStatus,term3PaymentStatus;

    public static void main( String[] args ) throws IOException {
        /**
         * Main method of the program
         * This method contains only menus
         */
        Scanner read = new Scanner( System.in );

        System.out.print( "Enter your username: " );
        userName = read.next();
        System.out.println();
        System.out.print( "Enter your password: " );
        userPassword = read.next();
        System.out.println();

        if( authenticate( userName, userPassword) ) {
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
                            System.out.println( "0.Exit " + studentName );
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
                            case 0:
                                updateDataBase();
                                term1PaymentStatus = false; term2PaymentStatus = false; term3PaymentStatus = false;
                                break;
                        }
                    } while( ! ( usrchMenu2 == 0 ) );
                }
                System.out.print( "Enter 1 to check another student's details or 0 to exit the app: " );
                usrchMenu1 = read.nextInt();
                System.out.println();
            } while( ! ( usrchMenu1 == 0 ) );
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

        for( i = 0; i < 100; i ++ ) {
            for( j = 0; j < 5; j ++ ) {
                classArray[i][j] = "";
            }
        }
        i = 0; j = 0;

        classFile = new File( "C:\\data\\schoolfeemanager\\database\\" + studentClass + studentDiv + ".txt" );

        if( classFile.exists() ) {
            Scanner readFile = new Scanner( classFile );
            readFile.useDelimiter( "&&" );

            while( readFile.hasNext() ) {
                for( j = 0; j < 5; j ++ ) {
                    if( readFile.hasNext() ) {
                        classArray[i][j] = readFile.next().trim();
                    }
                }
                if( classArray[i][0].equals( "null" ) ) {
                    break;
                }
                i++;
            }
            classTotalStudents = i;
            readFile.close();
        } else {
            System.out.println("error:class not found");
        }
    }

    public static boolean checkStudentExists() {
        /**
         * Functions to check whether the required student exists or not
         */
        int i;
        for( i = 0; i < classTotalStudents; i ++ ) {
            if( classArray[i][0].equalsIgnoreCase( studentName ) ) {
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
            if( classArray[i][0].equalsIgnoreCase( studentName ) ) {
                System.out.println("Student name: " + classArray[i][0] );
                System.out.println( "Roll No: " + studentRollNo );
                System.out.println( "Admission No: " + classArray[i][1] );
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
            }
        }
    }

    public static void payFees() throws IOException {
        /**
         * Registers the payment of fees after checking whether payment is late
         * A token 'paid' shows that fees has been paid
         * A token 'notpaid' shows that fees has not been paid
         */

        Scanner read = new Scanner( System.in );
        int i = studentRollNo - 1, j;
        char usrch = 'N';

        currentTerm = findCurrentTerm();
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
        if( classArray[i][j + 2].equals( "paid" ) ) {
            term3PaymentStatus = true;
        } else if( classArray[i][j + 2].equals( "notpaid" ) ) {
            term3PaymentStatus = false;
        }

        if( ( ! term1PaymentStatus ) && ( currentTerm >= 1 ) ) {
            if( isDueDateOver( 1 ) ) {
                daysLate = findDaysLate( 1 );
                fine = daysLate * 10;
            }
            System.out.println( "Term 1 fee payment" );
            amountToPay += calculateFeeAmount();
            amountToPay += fine;
            System.out.println( "Base payment rate: " + calculateFeeAmount() );
            System.out.println( "Fine for " + daysLate + " days late: " + fine );
            System.out.println( "Total amount to pay: " + amountToPay );
            System.out.println( "Enter Y to proceed or N to cancel: " );
            usrch = read.next().charAt( 0 );
            System.out.println();
            amountToPay = 0;
            daysLate = 0;
            fine = 0;
            if( usrch == 'N' || usrch == 'n' ) {
                System.out.println( "Canceling payment..." );
                term1PaymentStatus = false;
            } else if( usrch == 'Y' || usrch == 'y' ){
                classArray[i][1 + 1] = "paid";
                System.out.println( "Payment successful..." );
                term1PaymentStatus = true;
                updateDataBase();
                cloneDatabase();
            }
        } if( ( ! term2PaymentStatus ) && ( currentTerm >= 2 ) ){
            if( isDueDateOver( 2 ) ) {
                daysLate = findDaysLate( 2 );
                fine = daysLate * 10;
            }
            System.out.println( "Term 2 fee payment" );
            amountToPay += calculateFeeAmount();
            amountToPay += fine;
            System.out.println( "Base payment rate: " + calculateFeeAmount() );
            System.out.println( "Fine for " + daysLate + " days late: " + fine );
            System.out.println( "Total amount to pay: " + amountToPay );
            System.out.println( "Enter Y to proceed or N to cancel: " );
            usrch = read.next().charAt( 0 );
            System.out.println();
            amountToPay = 0;
            daysLate = 0;
            fine = 0;
            if( usrch == 'N' || usrch == 'n'  ) {
                System.out.println( "Canceling payment..." );
                term2PaymentStatus = false;
            } else if( usrch == 'Y' || usrch == 'y' ){
                classArray[i][2 + 1] = "paid";
                System.out.println( "Payment successful..." );
                term2PaymentStatus = true;
                updateDataBase();
                cloneDatabase();
            }
        } if( ( ! term3PaymentStatus ) && ( currentTerm >= 3 ) ) {
            if( isDueDateOver( 3 ) ) {
                daysLate = findDaysLate( 3 );
                fine = daysLate * 10;
            }
            System.out.println( "Term 3 fee payment" );
            amountToPay += calculateFeeAmount();
            amountToPay += fine;
            System.out.println( "Base payment rate: " + calculateFeeAmount() );
            System.out.println( "Fine for " + daysLate + " days late: " + fine );
            System.out.println( "Total amount to pay: " + amountToPay );
            System.out.println( "Enter Y to proceed or N to cancel: " );
            usrch = read.next().charAt( 0 );
            System.out.println();
            amountToPay = 0;
            daysLate = 0;
            fine = 0;
            if( usrch == 'N' || usrch == 'n'  ) {
                System.out.println( "Canceling payment..." );
                term3PaymentStatus = false;
            } else if( usrch == 'Y' || usrch == 'y' ){
                classArray[i][3 + 1] = "paid";
                term3PaymentStatus = true;
                System.out.println( "Payment successful..." );
                updateDataBase();
                cloneDatabase();
            }
        } if(  term1PaymentStatus || term2PaymentStatus || term3PaymentStatus ) {
            System.out.println( "Fees for term " + currentTerm + " has been paid" );
        }
    }

    public static int calculateFeeAmount() throws IOException {
        /**
         * Calculates the base fee amount for each class
         * Base fee amount for each term is stored in C:\data\schoolfeemanager\fee\ under each class's name.
         * Base fee can be changed as per needed by changing the value in the files.
         */
        int fee = 0;
        classFeeFile = new File( "C:\\data\\schoolfeemanager\\fee\\" + studentClass + ".txt" );
        Scanner read = new Scanner( System.in );
        Scanner readFeeFile = new Scanner( classFeeFile );

        switch( studentClassInt ) {
            case - 1:           //L.K.G
                fee = readFeeFile.nextInt();
                return fee;
            case - 2:           //U.K.G
                fee = readFeeFile.nextInt();
                return fee;
            case 1:             //STD 1
                fee = readFeeFile.nextInt();
                return fee;
            case 2:             //STD 2
                fee = readFeeFile.nextInt();
                return fee;
            case 3:             //STD 3
                fee = readFeeFile.nextInt();
                return fee;
            case 4:             //STD 4
                fee = readFeeFile.nextInt();
                return fee;
            case 5:             //STD 5
                fee = readFeeFile.nextInt();
                return fee;
            case 6:             //STD 6
                fee = readFeeFile.nextInt();
                return fee;
            case 7:             //STD 7
                fee = readFeeFile.nextInt();
                return fee;
            case 8:             //STD 8
                fee = readFeeFile.nextInt();
                return fee;
            case 9:             //STD 9
                fee = readFeeFile.nextInt();
                return fee;
            case 10:             //STD 10
                fee = readFeeFile.nextInt();
                return fee;
            case 11:             //STD 11
                fee = readFeeFile.nextInt();
                return fee;
            case 12:             //STD 12
                fee = readFeeFile.nextInt();
                return fee;
        }
        return fee;
    }

    public static void updateDataBase() throws IOException {
        /**
         * Updates the class database from the classArray[][]
         * Class databases are stored in C:\data\schoolfeemanager\databases
         */
        int i, j;

        FileWriter fw = new FileWriter( classFile );
        BufferedWriter bw = new BufferedWriter( fw );
        PrintWriter write =new PrintWriter( bw );

        for( i = 0; i < classTotalStudents; i ++ ) {
            for( j = 0; j < 5; j++ ) {
                write.print( classArray[i][j] + "&&" );
            }
            write.println();
        }

        write.close();
        bw.close();
        fw.close();

        System.out.println( "Databases have been updated successfully..." );
    }

    private static boolean authenticate ( String userName, String password ) throws IOException {
        /**
         * Checks the username and password entered by the user against a hashed copy of the password stored on th machine
         * Hashed passwords are stored in C:\\data\\schoolfeemanager\\users\\ inside files named after each user.
         * Hashing starts with an initial value of 10101010 and the ASCII code of each charachter in the
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

            long passHashCode = 10101010L, passHashCodeRef;
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