package xyz.relentlesscrew.controllers;

import spark.Route;
import xyz.relentlesscrew.util.Config;
import xyz.relentlesscrew.util.Path;
import xyz.relentlesscrew.util.ViewUtil;

import java.util.HashMap;
import java.util.Map;

public class IndexController {
    /**
     * Serve the index page
     */
    public static Route serveIndexPage = (request, response) -> {
        Map<String, Object> model = new HashMap<>();

        Config config = Config.getConfig();

        model.put("about", config.getAbout());
        model.put("invite", config.getDiscordInvite());
        model.put("recaptchaKey", config.getRecaptchaSiteKey());

        String latestAnnouncement = config.getLatestAnnouncement();

        if (!latestAnnouncement.trim().equals("")) {
            model.put("annoucement", latestAnnouncement);
        }

        return ViewUtil.render(model, Path.Template.INDEX);

    };
}