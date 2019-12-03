package penso.stackhat.server.util;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class SimpleKeyGenerator implements KeyGenerator {

    // ======================================
    // =          Business methods          =
    // ======================================

    public Key generateKey() {
        String keyString = "FKLKFJFIEJFQPOKFDLJGJ809586098gjgJDKFJAKSJGaadfwqwljfqwlkfMFJWKFQFIUFUAKFWJngskjjgQHGQigajskgsjQHGWKgjaksn3984lkjfasFJSDKFAJsfaadfwqwljfqwlkfMFJWKFQFIUFUAKFWJngskjjgQHGQigajskgsjQHGWKgjaksn3984lkjfasFJSDKFAJsfaadfwqwljfqwlkfMFJWKFQFIUFUAKFWJngskjjgQHGQigajskgsjQHGWKgjaksn3984lkjfasFJSDKFAJsfaFDLKOFEEJFJKDJFAJALFJDFDSKFJWIFJWFFKLKFJFIEJFQPOKFDLJGJ809586098gjgJDKFJAKSJGaadfwqwljfqwlkfMFJWKFQFIUFUAKFWJngskjjgQHGQigajskgsjQHGWKgjaksn3984lkjfasFJSDKFAJsfaadfwqwljfqwlkfMFJWKFQFIUFUAKFWJngskjjgQHGQigajskgsjQHGWKgjaksn3984lkjfasFJSDKFAJsfaadfwqwljfqwlkfMFJWKFQFIUFUAKFWJngskjjgQHGQigajskgsjQHGWKgjaksn3984lkjfasFJSDKFAJsfaFDLKOFEEJFJKDJFAJALFJDFDSKFJWIFJWF";
        Key key = new SecretKeySpec(keyString.getBytes(), 0, keyString.getBytes().length, "HMACSHA256");
        return key;
    }
}