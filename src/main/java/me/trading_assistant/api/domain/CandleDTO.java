package me.trading_assistant.api.domain;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class CandleDTO {
    private long timestamp;
    private String date;
    private double open;
    private double high;
    private double low;
    private double close;
    private List<String> patterns;

    public CandleDTO(Object timestamp, double open, double high, double low, double close) {
        this.timestamp = convertToLong(timestamp);
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.date = convertTimestampToDate(this.timestamp);
    }

    private long convertToLong(Object timestamp) {
        if (timestamp instanceof Long) {
            return (Long) timestamp;
        } else if (timestamp instanceof Integer) {
            return ((Integer) timestamp).longValue();
        } else {
            throw new IllegalArgumentException("Le timestamp doit être de type Long ou Integer");
        }
    }

    private String convertTimestampToDate(long timestamp) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(
            Instant.ofEpochSecond(timestamp),
            ZoneId.systemDefault()
        );
        return dateTime.toString();
    }

    // Getters et Setters
    public long getTimestamp() {
        return timestamp;
    }

    public String getDate() {
        return date;
    }

    public double getOpen() {
        return open;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getClose() {
        return close;
    }

    public List<String> getPatterns() {
        return patterns;
    }

    public void setPatterns(List<String> patterns) {
        this.patterns = patterns;
    }
} 