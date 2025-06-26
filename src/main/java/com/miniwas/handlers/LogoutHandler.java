package com.miniwas.handlers;

import com.miniwas.SessionInfo;
import com.miniwas.SessionManager;
import com.miniwas.utils.CookieUtils;
import com.miniwas.utils.HttpUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

public class LogoutHandler implements Handler {
    SessionManager sessionManager;

    @Override
    public void handle(BufferedReader in,
                BufferedWriter out,
                String method,
                String path) throws IOException {
        try {
            if (!method.equals("GET")) {
                HttpUtils.sendMethodNotAllowed(out);
                return;
            }
            // 로그아웃
            handleLogout(in, out);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            out.flush();
        }


    }

    public LogoutHandler(SessionManager sessionManager){
        this.sessionManager = sessionManager;
    }

    public void handleLogout(BufferedReader in, BufferedWriter out) throws IOException{
        // 헤더 파싱
        Map<String, String> headers = HttpUtils.parseHeaders(in);
        String cookieHeader = headers.getOrDefault("cookie", "");
        // 세션ID 추출
        String sessionId = CookieUtils.parse(cookieHeader, "SESSIONID");
        sessionManager.delete(sessionId);
        HttpUtils.sendRedirect(out, "/");
    }
}
