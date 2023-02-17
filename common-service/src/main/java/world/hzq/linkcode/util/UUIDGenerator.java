package world.hzq.linkcode.util;

import java.util.UUID;

public class UUIDGenerator {

    private UUIDGenerator() {
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
