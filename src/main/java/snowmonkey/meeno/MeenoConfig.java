package snowmonkey.meeno;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class MeenoConfig {
    private final Properties properties;

    public MeenoConfig(Properties properties) {
        this.properties = properties;
    }

    public static MeenoConfig loadMeenoConfig() {
        return loadMeenoConfig(Paths.get("credentials.properties"));
    }

    public static MeenoConfig loadMeenoConfig(Path file) {
        Properties properties = new Properties();
        try {
            try (Reader reader = Files.newBufferedReader(file)) {
                properties.load(reader);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read config file " + file, e);
        }
        return new MeenoConfig(properties);
    }

    public AppKey delayedAppKey() {
        return new AppKey(properties.getProperty("app-key.delayed"));
    }

    public AppKey appKey() {
        return new AppKey(properties.getProperty("app-key"));
    }

    public String username() {
        return properties.getProperty("username");
    }

    public String password() {
        return properties.getProperty("password");
    }

    public File certificateFile() {
        return new File(properties.getProperty("certificate.file"));
    }

    public String certificatePassword() {
        return properties.getProperty("certificate.password");
    }
}
