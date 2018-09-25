package be.dela.logview.endpoints;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class Config {
    final Properties props = new Properties();

    private void load() {
        if (props.isEmpty()) {
            try {
                props.load(Files.newInputStream(Paths.get("config.properties")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String getCertPath(String env) {
        load();
        String certPath = props.getProperty("cert." + env);
        if (certPath == null) {
            throw new RuntimeException("Unknown environment: " + env);
        } else {
            return Paths.get(certPath).toAbsolutePath().toString();
        }
    }
}
