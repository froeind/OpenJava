package data;

import java.time.Duration;
import java.time.Instant;

public class Timer implements AutoCloseable {
    private Instant start;
    private String name;
    private int precision;

    public Timer(String name, int precision) {
        this.start = Instant.now();
        this.name = name;
        this.precision = precision;
    }

    public void stop() {
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        double ms = duration.toMillis();
        String formatString = "%s ausgeführt in %." + precision + "f ms\n";
        System.out.printf(formatString, name, ms);
    }

    @Override
    public void close() {
        stop();
    }
}