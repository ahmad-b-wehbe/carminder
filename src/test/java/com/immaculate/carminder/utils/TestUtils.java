package com.immaculate.carminder.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestUtils {

    public static String loadResource(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }
}