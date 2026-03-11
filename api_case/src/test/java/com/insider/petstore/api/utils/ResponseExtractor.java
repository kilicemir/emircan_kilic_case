package com.insider.petstore.api.utils;

import io.restassured.response.Response;

import java.util.function.Supplier;
import java.util.regex.Pattern;

public final class ResponseExtractor {

    private static final Pattern STATUS_PATTERN = Pattern.compile("status code: (\\d+)");

    private ResponseExtractor() {
    }

    public static int getStatusCode(Supplier<Response> call) {
        try {
            return call.get().statusCode();
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg != null) {
                var m = STATUS_PATTERN.matcher(msg);
                if (m.find()) {
                    return Integer.parseInt(m.group(1));
                }
            }
            throw new AssertionError("Could not parse status from: " + msg, e);
        }
    }
}
