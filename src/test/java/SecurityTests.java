import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class SecurityTests {

    @Test
    void testUserNameValidation() {
        //safe username, return True
        assertTrue(Security.checkUserName("user1@Payconiq.com"));

        //sql injection, return False
        assertFalse(Security.checkUserName("demo' or '100'='100"));

        // overflow, return False
        assertFalse(Security.checkUserName("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));

    }

    @Test
    void rateLimiterVerification(){
        Cryptography cryptography=new Cryptography();
        String username = "user1@Payconiq.com";

        String password = "password";
        cryptography.userAuth(username, password);
        assertFalse(Security.rateController.asMap().containsKey(username));

        password="badPassword!";
        cryptography.userAuth(username, password);
        cryptography.userAuth(username, password);
        cryptography.userAuth(username, password);
        assertTrue(Security.rateController.asMap().containsKey(username));



    }

}
