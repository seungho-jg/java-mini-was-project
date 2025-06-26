package com.miniwas;

import java.util.Map;

public interface SessionManagerInterface extends Create, Read, Update, Delete {
}

interface Create {
    String create(String username);
}

interface Read {
    Map<String, SessionInfo> getAll();
    SessionInfo getOne(String sessionId);
}

interface Update {
    void grant(String sessionId, boolean b);
}

interface Delete {
    boolean delete(String sessionId);
    void cleanupExpired();
}
