package xyz.relentlesscrew.controllers;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mrpowergamerbr.temmiewebhook.DiscordEmbed;
import com.mrpowergamerbr.temmiewebhook.DiscordMessage;
import com.mrpowergamerbr.temmiewebhook.TemmieWebhook;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Route;
import xyz.relentlesscrew.persistence.DAO.ApplicationDAO;
import xyz.relentlesscrew.persistence.DAO.MemberDAO;
import xyz.relentlesscrew.persistence.model.Application;
import xyz.relentlesscrew.util.Config;
import xyz.relentlesscrew.util.JsonUtil;

import java.util.HashMap;
import java.util.Map;

public class ApplicationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationController.class);
    private static ApplicationDAO applicationDAO = new ApplicationDAO();
    private static Config config = Config.getConfig();

    /**
     * Handle an application requested from the web page
     * @param discordUsername username on discord (has to be something#1234)
     * @param dauntlessUsername username on dauntless (between 3 and 32 chars)
     * @param recaptchaResponse response from google's re-captcha (to verify on the server side)
     * @return json response with message ("response")
     */
    public static Route handleApplication = (request, response) -> {
        response.type("application/json");

         // fetch the params
        String discordUsername = request.queryParams("discordusername");
        String dauntlessUsername = request.queryParams("dauntlessusername");
        String recaptchaResponse = request.queryParams("g-recaptcha-response");

        // check/validate the discord and dauntless usernames
        String validation = validateInput(discordUsername, dauntlessUsername);
        if (validation != null) {
            return JsonUtil.responseJson(response, validation);
        }

        // server-sided re-captcha verification
        Map<String, String> recaptchaData = new HashMap<>();
        recaptchaData.put("secret", System.getenv("RECAPTCHA_SECRET"));
        recaptchaData.put("response", recaptchaResponse);
        recaptchaData.put("remoteip", request.ip());

        boolean captchaSuccess = new Gson()
                .fromJson(HttpRequest
                                  .post("https://www.google.com/recaptcha/api/siteverify")
                                  .form(recaptchaData)
                                  .body(), JsonObject.class)
                .get("success")
                .getAsBoolean();

        if (!captchaSuccess) {
            return JsonUtil.responseJson(response, "Recaptcha verification failed.");
        }

        // base64 the user input (who knows what kind of characters they used)
        discordUsername = Base64.encodeBase64String(discordUsername.getBytes());
        dauntlessUsername = Base64.encodeBase64String(dauntlessUsername.getBytes());

        // check if that username is already a member?
        MemberDAO memberDAO = new MemberDAO();
        if (memberDAO.findByDauntlessUsername(dauntlessUsername) != null) {
            return JsonUtil.responseJson(response,"You are already a part of our guild, dummy.");
        }

        // create a new application and add it to the database (if it can)
        Application application = new Application(discordUsername, dauntlessUsername);

        // if it returns false, 9/10 he/she already applied
        if (!applicationDAO.add(application)) {
            return JsonUtil.responseJson(response, "Looks like you already applied, " +
                    "if you think this is a mistake, please contact one of the leaders");
        }

        // send a message to discord that a user applied
        TemmieWebhook webhook = new TemmieWebhook(config.getDiscordWebhook());

        String dauntlessUsernameDecoded = new String(Base64.decodeBase64(dauntlessUsername), "UTF-8");
        String discordUsernameDecoded = new String(Base64.decodeBase64(discordUsername), "UTF-8");
        String title = dauntlessUsernameDecoded+ " just appied.";
        StringBuilder description = new StringBuilder();
        description.append("Dauntless Username: ")
                .append(dauntlessUsernameDecoded)
                .append("\n");
        description.append("Discord Username: ")
                .append(discordUsernameDecoded)
                .append("\n");

        DiscordEmbed embed = new DiscordEmbed(title, String.valueOf(description));
        embed.setColor(Integer.parseInt("002cb6", 16));

        DiscordMessage discordMessage = new DiscordMessage();
        discordMessage.getEmbeds().add(embed);
        webhook.sendMessage(discordMessage);

        return JsonUtil.responseJson(response, "Thanks for applying, if you haven't " +
                "already please join our discord to complete the application.");
    };

    /**
     * @param Application as json (discord username, dauntless username)
     * @return json response with message or json object
     */
    public static Route addApplication = (request, response) -> {
        Application application = new Gson().fromJson(request.body(), Application.class);

        String validation =
                validateInput(application.getDiscordUsername(), application.getDauntlessUsername());

        if (validation != null) {
            return JsonUtil.responseJson(response, validation);
        }

        String responseString;
        if (applicationDAO.add(application)) {
            responseString = "Application for " +
                    application.getDauntlessUsername() +
                    " was successfully added";
        } else {
            responseString = "Could not add the application. It already exists?";
        }
        return JsonUtil.responseJson(response, responseString);
    };

    public static Route getApplication = (request, response) -> {
        String requestParams = request.params(":id");
        Application application;
        try {
            Long id = Long.parseLong(requestParams);
            application = applicationDAO.findById(id);
        } catch (NumberFormatException e) {
            application = applicationDAO.findByDiscordUsername(requestParams);
        }

        return JsonUtil.responseJson(response, application);
    };

    /**
     * @param Application as json
     * @return json response with message or json object
     */
    public static Route updateApplication = (request, response) -> {
        Application application;
        try {
            application = new Gson().fromJson(request.body(), Application.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage() + " Caused by: " + e.getCause());
            return JsonUtil.responseJson(response, "There was an error. Check the logs");
        }

        applicationDAO.update(application);

        return JsonUtil.responseJson(response, application);
    };

    public static Route removeApplication = (request, response) -> {
        Long id = Long.parseLong(request.params(":id"));
        Application application = applicationDAO.findById(id);

        String responseString;
        if (applicationDAO.remove(application)) {
            responseString = "Application for " +
                    application.getDauntlessUsername() +
                    "(" + application.getId() +
                    ") was successfully removed.";
        } else {
            responseString = "Could not remove the application. Please check the logs.";
        }
        return JsonUtil.responseJson(response, responseString);
    };

    public static Route getAllApplications = (request, response) ->
            JsonUtil.responseJson(response, applicationDAO.findAll());

    private static String validateInput(String discordUsername, String dauntlessUsername) {
        if (!discordUsername.matches(".*#\\d{4}")) {
            return "Invalid Discord Username";
        }

        if (!dauntlessUsername.matches("^[a-zA-Z0-9]{3,32}")) {
            return "Invalid Dauntless username";
        }

        return null;
    }
}
