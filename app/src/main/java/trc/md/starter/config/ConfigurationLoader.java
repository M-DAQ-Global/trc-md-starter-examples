package trc.md.starter.config;

import com.mdaq.trc.marketdata.client.common.config.RetryConfig;
import com.mdaq.trc.marketdata.client.common.config.SslConfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationLoader {

    private static final String CERTS_DIRECTORY = "app/src/main/resources/certs/";
    private static final Properties properties = new Properties();

    // Load configuration properties
    static {
        try (InputStream input = ConfigurationLoader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IOException("Configuration file 'config.properties' not found in classpath.");
            }
            properties.load(input);
        } catch (IOException ex) {
            throw new ExceptionInInitializerError("Failed to load configuration: " + ex.getMessage());
        }
    }

    // obtain SSL configurations
    public static SslConfig getSslConfig() {
        String keyStoreFileName = properties.getProperty("ssl.keyStoreFileName");
        String trustStoreFileName = properties.getProperty("ssl.trustStoreFileName");

        if (keyStoreFileName == null || trustStoreFileName == null) {
            throw new IllegalStateException("Keystore or Truststore file names are not specified in config.properties.");
        }

        String keyStorePath = new File(CERTS_DIRECTORY, keyStoreFileName).getAbsolutePath();
        String trustStorePath = new File(CERTS_DIRECTORY, trustStoreFileName).getAbsolutePath();

        String keyStorePassword = properties.getProperty("ssl.keyStorePassword");
        String trustStorePassword = properties.getProperty("ssl.trustStorePassword");

        if (keyStorePassword == null || trustStorePassword == null) {
            throw new IllegalStateException("SSL passwords are not set in environment variables.");
        }

        return new SslConfig(keyStorePath, keyStorePassword, trustStorePath, trustStorePassword);
    }

    public static String getUrl() {
        return properties.getProperty("subscriber.url");
    }

    public static int getPort() {
        return Integer.parseInt(properties.getProperty("subscriber.port"));
    }

    public static String getUsername() {
        return properties.getProperty("subscriber.username");
    }

    public static String getPassword() {
        return properties.getProperty("subscriber.password");
    }

    // Obtain retry configurations
    public static RetryConfig getRetryConfig() {
        int maxAttempts = Integer.parseInt(properties.getProperty("retry.maxAttempts"));
        int intervalMillis = Integer.parseInt(properties.getProperty("retry.intervalMillis"));
        return RetryConfig.builder().maxAttempts(maxAttempts).intervalMillis(intervalMillis).build();
    }
}