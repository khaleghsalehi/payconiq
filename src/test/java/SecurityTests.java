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
    void rateLimiterVerification() {
        Cryptography cryptography = new Cryptography();
        String username = "user1@Payconiq.com";
        String browserFingerPrintUUID = "cad5fc067493ca7b3f85d60c65745b3bf5617ffdd38f7ad3eec163d55edddd06";
        String password = "password";
        cryptography.userAuth(username, password, browserFingerPrintUUID);
        assertFalse(Security.rateController.asMap().containsKey(browserFingerPrintUUID));

        password = "badPassword!"; // try to bruteforce
        cryptography.userAuth(username, password, browserFingerPrintUUID);
        cryptography.userAuth(username, password, browserFingerPrintUUID);
        cryptography.userAuth(username, password, browserFingerPrintUUID);
        assertTrue(Security.rateController.asMap().containsKey(browserFingerPrintUUID));


    }

}
