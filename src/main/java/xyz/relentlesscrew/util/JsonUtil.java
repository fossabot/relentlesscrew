package xyz.relentlesscrew.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import spark.Response;

public class JsonUtil {

    public static JsonObject responseJson(Response response, Object o) {
        JsonObject json = new JsonObject();
        json.add("response", new Gson().toJsonTree(o));

        response.type("application/json");
        return json;
    }
}
