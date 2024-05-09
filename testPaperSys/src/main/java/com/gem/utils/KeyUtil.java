package com.gem.utils;

import java.util.UUID;

public class KeyUtil {
    public static String generateKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
