package io.canduer.nexusflow.common.cache;

// final + private constructor : This is a utility class; nobody should EXTEND it.
public final class RedisCacheKeys {

    private RedisCacheKeys(){
    }

    public static String customer(String customerId) {
        return "customer:" + customerId;
    }

    public static String invoice(String invoiceId) {
        return "invoice:" + invoiceId;
    }

    public static String user(String userId) {
        return "user:" + userId;
    }
}