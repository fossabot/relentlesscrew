package xyz.relentlesscrew.util;

import org.eclipse.jetty.http.HttpStatus;
import spark.ModelAndView;
import spark.Route;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

public class ViewUtil {

    public static String render(Map<String, Object> model, String templatePath) {
        model.put("WebPath", Path.Web.class);
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }

    public static Route notFound = (request, response) -> {
        response.status(HttpStatus.NOT_FOUND_404);
        return render(new HashMap<>(), Path.Template.NOT_FOUND);
    };

    public static Route notFoundApi = (request, response) -> {
        response.status(HttpStatus.NOT_FOUND_404);
        return JsonUtil.responseJson(response, "Requested Route " + request.pathInfo() + " doesn't exist.");
    };
}
