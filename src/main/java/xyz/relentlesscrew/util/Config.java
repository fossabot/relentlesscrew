package xyz.relentlesscrew.util;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Config {

    private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);

    @Getter
    private static Config config;
    @Getter @Setter
    private String about;
    @Getter @Setter
    private String latestAnnouncement;
    @Getter @Setter
    private String discordWebhook;
    @Getter @Setter
    private String apiKey;
    @Getter @Setter
    private String discordInvite;
    @Getter @Setter
    private String recaptchaSiteKey;
    @Getter @Setter
    private String recaptchaSecretKey;

    static {
        try {
            config = new Yaml().loadAs(new FileReader("config.yml"), Config.class);
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage() + " Caused by: " + e.getCause());
        }
    }

    public static void save(Config config) {
        try (BufferedWriter writer =
                     Files.newBufferedWriter(Paths.get("config.yml"))) {
            new Yaml().dump(config, writer);
        } catch (IOException e) {
            LOGGER.error(e.getMessage() + " Caused by: " + e.getCause());
        }
    }

}
