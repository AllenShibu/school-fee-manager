/**import java.io.IOException;
import java.util.Scanner;
import java.io.File;

public class Password {
    protected static boolean authenticate ( String userName, String password ) throws IOException {
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
 */