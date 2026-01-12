package lggur.shorturl.infra.config;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class AppConfig {

    private final Properties properties = new Properties();

    public AppConfig() {
        loadProperties();
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Не удалось найти файл config.properties");
                return;
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Duration getDefaultLifetime() {
        String hours = properties.getProperty("link.default-lifetime-hours", "24");
        return Duration.ofHours(Long.parseLong(hours));
    }

    public int getMinClicks() {
        return Integer.parseInt(properties.getProperty("link.min-clicks", "1"));
    }

    public int getMaxClicksLimit() {
        return Integer.parseInt(properties.getProperty("link.max-clicks-limit", "10"));
    }

    public long getSchedulerInitialDelay() {
        return Long.parseLong(properties.getProperty("scheduler.initial-delay", "1"));
    }

    public long getSchedulerPeriod() {
        return Long.parseLong(properties.getProperty("scheduler.period", "1"));
    }

    public TimeUnit getSchedulerTimeUnit() {
        String unit = properties.getProperty("scheduler.time-unit", "HOURS");
        return TimeUnit.valueOf(unit);
    }
}
