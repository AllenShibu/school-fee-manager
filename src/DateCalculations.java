import java.io.File;
import java.util.Scanner;

public class DateCalculations {
    public static String year, month, date, dateStr;
    public static String todaysDate() {
        //Entering date as seperate elements to add more functonality later
        Scanner read = new Scanner( System.in );

        System.out.print( "Enter today's year in the foramt YYYY: " );
        year = read.next();
        System.out.println();
        System.out.print( "Enter today's month in the format MM: " );
        month = read.next();
        System.out.println();
        System.out.print( "Enter todays date in the format DD: " );
        date = read.next();
        System.out.println();

        dateStr = date + month + year;

        return dateStr;
    }

    public static int findCurrentTerm() {
        if( month.equals( "06" ) || month.equals( "07" ) || month.equals( "08" ) ) {
            return 1;
        } else if ( month.equals( "09" ) || month.equals( "10" ) || month.equals( "11" ) || month.equals( "12" ) ) {
            return 2;
        } else if ( month.equals( "01" ) || month.equals( "02" ) || month.equals( "03" ) ) {
            return 3;
        }
    }
}