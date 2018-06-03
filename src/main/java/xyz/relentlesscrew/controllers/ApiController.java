package xyz.relentlesscrew.controllers;

import spark.Filter;
import xyz.relentlesscrew.util.Config;

import static spark.Spark.halt;

public class ApiController {

    public static Filter beforeApiCall = (request, response) -> {
        try {
            String apiKey = request.headers("apiKey");
            if (!apiKey.equals(Config.getConfig().getApiKey()) ) {
                throw halt(401, "Invalid apiKey...");
            }
        } catch (NullPointerException e) {
            throw halt(401, "apiKey missing...");
        }
    };
}
