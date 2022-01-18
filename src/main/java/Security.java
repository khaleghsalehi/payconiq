import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Security {

    private static final long CACHE_TTL_DEFAULT = 60;
    private static final String USERNAME_WHITE_LIST = "[^A-Za-z0-9@.+]"; // username, alphabet, email, pin, phone. etc.
    private static final int USERNAME_MAX_LENGTH = 32;


    public static LoadingCache<String, String> rateController = CacheBuilder
            .newBuilder()
            .expireAfterWrite(CACHE_TTL_DEFAULT, TimeUnit.SECONDS)
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String s) throws Exception {
                    return "";
                }
            });

    public static boolean checkUserName(String userName) {
        if (userName.length() > USERNAME_MAX_LENGTH)
            return false;
        Pattern pattern = Pattern.compile(USERNAME_WHITE_LIST);
        Matcher matcher = pattern.matcher(userName);
        boolean matchFound = matcher.find();
        if (matchFound) {
            return false;
        } else {
            return true;
        }
    }

}
