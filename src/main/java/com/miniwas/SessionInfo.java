package com.miniwas;

import java.time.Instant;

public class SessionInfo {
    private final String username;
    private final long createAt;
    private boolean disabled = false;

    public SessionInfo(String username) {
        this.username = username;
        this.createAt = Instant.now().toEpochMilli();
    }
    public String getUsername() {
        return this.username;
    }
    public long getCreateAt() {
        return this.createAt;
    }

    public boolean isExpired(long ttlMillis) {
        return Instant.now().toEpochMilli() - this.createAt > ttlMillis;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean b) {
        this.disabled = b;
    }
}
