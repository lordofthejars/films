package demo;

import java.security.SecureRandom;

public class SharedSecret {

    private static byte[] secret = new byte[32];

    static {
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(secret);
    }

    public static byte[] getSecret() {
        return SharedSecret.secret;
    }
}
