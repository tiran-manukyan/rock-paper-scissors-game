package org.rock.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public record Configs(int serverPort, int playerActionTimeoutSeconds) {

    public static Configs loadFromFile() throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = Configs.class.getResourceAsStream("/config.properties");
        properties.load(inputStream);

        int serverPort = Integer.parseInt(properties.getProperty("server.port"));
        int playerActionTimeoutSeconds = Integer.parseInt(properties.getProperty("player.action.timeout.seconds"));

        return new Configs(serverPort, playerActionTimeoutSeconds);
    }
}
