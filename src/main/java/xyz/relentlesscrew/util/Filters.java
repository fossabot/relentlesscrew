package xyz.relentlesscrew.util;

import spark.Filter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.halt;

public class Filters {

    public static Filter addTrailingSlashes = (request, response) -> {
        if (!request.pathInfo().endsWith("/")) {
            response.redirect(request.pathInfo() + "/");
        }
    };

    public static Filter CORSFilter = (request, response) -> {
        Config config = Config.getConfig();
        if (request.ip().equals(System.getenv("PHASE_IP")) || request.host().equals(config.getWebUrl().substring(7))) {
            Map<String, String> CORSHeaders = Collections.unmodifiableMap(new HashMap<String, String>() {
                {
                    put("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE");
                    if (request.ip().equals(System.getenv("PHASE_IP"))) {
                        put("Access-Control-Allow-Origin", System.getenv("PHASE_IP"));
                    } else {
                        put("Access-Control-Allow-Origin", config.getWebUrl());
                    }
                    put("Access-Control-Allow-Headers", "Content-Type,apiKey");
                    put("Access-Control-Max-Age", "1800");
                }
            });
            CORSHeaders.forEach(response::header);
        } else {
            throw halt(403, JsonUtil.responseJson(response,"Sorry, but you can't access this part of the api.").toString());
        }
    };
}
