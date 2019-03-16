/**
 * @author Allen Shibu
 * Class containing functions required to handle date and related stuff for the SchoolFeeManager
 */

import java.util.Scanner;

public class DateCalculations {
    public static String yearStr, monthStr, dateStr, fullDateStr;
    public static int year, month, date, currentTerm;
    public static String todaysDate() {
        Scanner read = new Scanner( System.in );

        //Date is entered seperately to add more flexibility when handling it.
        System.out.print( "Enter today's year in the foramt YYYY: " );
        yearStr = read.next();
        year = Integer.parseInt( yearStr );
        System.out.println();
        System.out.print( "Enter today's month in the format MM: " );
        monthStr = read.next();
        month = Integer.parseInt( monthStr );
        System.out.println();
        System.out.print( "Enter today's date in the format DD: " );
        dateStr = read.next();
        date = Integer.parseInt( dateStr );
        System.out.println();

        fullDateStr = dateStr + monthStr + yearStr;

        return fullDateStr;
    }

    public static int findCurrentTerm() {
        /**
         * Academic year is divided into three terms as follows
         * Term 1 -- June to August
         * Term 2 -- September to December
         * Term 3 -- January to March and lasts up to the start of the next academic year
         */
        if( month == 6 || month == 7 || month == 8 ) {
            currentTerm = 1;
            return 1;
        } else if ( month == 9 || month == 10 || month == 11 || month == 12 ) {
            currentTerm = 2;
            return 2;
        } else if( month == 1 || month == 2 || month == 3 ) {
            currentTerm = 3;
            return 3;
        } else {
            currentTerm = 3;
            return 3;
        }
    }

    public static boolean isDueDateOver( int refTerm ) {
        /**
         * Function to check whether the due date is over
         * Number of days late is calculated separately in another function
         */

        if( refTerm == 1 && ( month == 6 || month == 7 || month == 8 ) ) {
            return false;
        } else if( refTerm == 2 && ( month == 9 || month == 10 || month == 11 || month == 12 ) ) {
            return false;
        } else if( refTerm == 3 && ( month == 1 || month == 2 || month == 3 ) ) {
            return false;
        } else {
            return true;
        }
    }

    public static int findDaysLate( int refTerm ) {
        /**
         * Calculates the number of days late after the last day of the term
         * Checking whether the due date is over is done separately in another function
         */

        int daysLate = 0, tempMonth = month - 1;
        if( tempMonth == 0 ) {
            tempMonth = 12;
        }
        switch( refTerm ) {
            case 1:
                daysLate = date;
                while ( tempMonth != 8 ) {
                    daysLate += findDaysInMonth( tempMonth );
                    tempMonth--;
                    if( tempMonth == 0 ) {
                        tempMonth = 12;
                    }
                }
                break;
            case 2:
                daysLate = date;
                while ( tempMonth != 12 ) {
                    daysLate += findDaysInMonth( tempMonth );
                    tempMonth--;
                    if( tempMonth == 0 ) {
                        tempMonth = 12;
                    }
                }
                break;
            case 3:
                daysLate = date;
                while ( tempMonth != 3 ) {
                    daysLate += findDaysInMonth( tempMonth );
                    tempMonth--;
                    if( tempMonth == 0 ) {
                        tempMonth = 12;
                    }
                }
                break;
        }
        return daysLate;
    }

    public static int findDaysInMonth( int refMonth ) {
        /**
         * Finds and returns the number of days in a month and returns it
         * This function is used occasionally in the program
         */
        if( refMonth == 1 || refMonth == 3 || refMonth == 7 || refMonth == 8 || refMonth == 10 || refMonth == 12 ) {
            return 31;
        } else if( refMonth == 4 || refMonth == 6 || refMonth == 9 || refMonth == 11 ) {
            return 30;
        } else if ( refMonth == 2 && year % 4 == 0 ) {
            return 29;
        } else { //if ( refMonth == 2 &&  year % 4 != 0 ) {
            return 28;
        }
    }
}