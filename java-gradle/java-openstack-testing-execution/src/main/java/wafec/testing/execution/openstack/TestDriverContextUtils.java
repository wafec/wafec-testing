package wafec.testing.execution.openstack;

import wafec.testing.execution.TestDriverInputFunctionHandler;

import java.util.Base64;

public class TestDriverContextUtils {
    private TestDriverContextUtils() {

    }

    public static String getKey(TestDriverInputFunctionHandler handler) {
        return handler.getTestDriverContext().<String>get("key").orElseThrow(IllegalStateException::new);
    }

    public static void setKey(TestDriverInputFunctionHandler handler, long keyId) {
        handler.getTestDriverContext().set("keyId", keyId);
        handler.getTestDriverContext().set("key", Base64.getEncoder().encodeToString(String.format("%d", keyId).getBytes()));
    }
}
