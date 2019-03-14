/**

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;

public class schoolfee {
    public static void main( String[] args ) throws IOException {
        Scanner sc = new Scanner( System.in );

        String userName, password;
        boolean access;

        System.out.print( "Enter username: " );
        userName = sc.nextLine();
        System.out.println();
        System.out.print( "Enter your password: " );
        password = sc.nextLine();
        System.out.println();

        access = authenticate( userName, password );

        System.out.println( access );
    }

    private static boolean authenticate ( String userName, String password ) throws IOException {
        File user = new File( "C:\\data\\schoolfeemanager\\users\\" + userName + ".txt" );

        if ( ! ( user.exists() ) ) {
            System.out.println( "User does not exist" );
            return false;
        }
        else {
            FileReader FW = new FileReader( user );
            BufferedReader passReader = new BufferedReader( FW );

            long passHashCode = 1010101010101010L, passHashCodeRef;
            int i, passLength = password.length();

            passHashCodeRef =  Long.parseLong( passReader.readLine() );

            for ( i = 0; i < passLength; i++ ) {
                char ch = password.charAt( i );
                passHashCode = passHashCode + ch;
            }

            System.out.println( passHashCode );

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
*/