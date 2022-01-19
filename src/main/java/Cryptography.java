import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

public class Cryptography {
    private final int PASSWORD_MAX_LEN = 64;
    Argon2 argon2 = Argon2Factory.create();

    // browserFingerPrintUUId extracted from request header
    // solution: https://github.com/khaleghsalehi/ironfox
    public boolean userAuth(String username, String password, String browserFingerPrintUUId) {
        // username and password validation
        if (!Security.checkUserName(username) || password.length() > PASSWORD_MAX_LEN) {
            System.out.println("maximum password length, return false");
            return false;
        }

        // rate limiter by guava cache
        if (Security.rateController.asMap().containsKey(browserFingerPrintUUId)) {
            System.out.println(username + "rate limiter: " + browserFingerPrintUUId + "  already tried for authentication, wait for second");
            return false;
        }

        try {
            String storedPwd = getStoredPasswordForUserFromDB(username);
            if (!Objects.isNull(storedPwd)) {
                if (verifyHash(username, password)) {
                    System.out.println("Password matches!");
                    return true;
                    // todo go to next step (2FactorAuthentication, e.g OTP by sms)
                } else {
                    System.out.println("Password incorrect!");
                    Security.rateController.asMap().put(browserFingerPrintUUId,
                            String.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)));
                    return false;
                }
            } else {
                System.out.println("No user found!");
            }
        } catch (Exception exc) {
            System.out.println("Error etc" + exc);
        }
        return false;
    }

    private String calculateHash(String password) {
        String hash = argon2.hash(10, 65536, 1, password);
        System.out.println(hash);
        return hash;
    }

    private boolean verifyHash(String username, String password) {
        return argon2.verify(getStoredPasswordForUserFromDB(username), password);
    }

    private String getStoredPasswordForUserFromDB(String username) {
        //sample mocked password for username
        return "$argon2i$v=19$m=65536,t=10,p=1$U/T4cR+Jz13S91Uj6r2GQg$4A6egM+90pjUs4Jo7fVPXYjJdqcpoDTvVoM0+W4e4/I"; // password
    }


}
