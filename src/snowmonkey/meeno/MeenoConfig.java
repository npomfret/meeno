package snowmonkey.meeno;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class MeenoConfig {
    private Properties properties;

    public MeenoConfig(Properties properties) {
        this.properties = properties;
    }

    public static MeenoConfig load() {
        File file = new File("credentials.properties");

        Properties properties = new Properties();
        try {
            try (FileReader reader = new FileReader(file)) {
                properties.load(reader);
            }
        } catch (IOException e) {
            throw new Defect("Cannot read config file " + file, e);
        }
        return new MeenoConfig(properties);
    }

    public AppKey delayedAppKey() {
        return new AppKey(properties.getProperty("app-key.delayed"));
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
