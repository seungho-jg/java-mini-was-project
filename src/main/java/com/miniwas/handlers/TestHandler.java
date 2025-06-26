package com.miniwas.handlers;

import com.miniwas.SessionManager;
import com.miniwas.utils.HttpUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class TestHandler implements Handler {
    private final SessionManager sessionManager;

    public TestHandler(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void handle(BufferedReader in,
                       BufferedWriter out,
                       String method,
                       String path) throws IOException {

        if (!method.equals("GET")) {
            HttpUtils.sendMethodNotAllowed(out);
            return;
        }

        // 세션 목록 불러오기
        String[] sessionList = sessionManager.getAll();

        StringBuilder sb = new StringBuilder();
        sb.append("""
            <!DOCTYPE html>
            <html>
            <head>
              <meta charset="UTF-8">
              <title>세션 목록</title>
              <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/water.css@2/out/light.css" />
            </head>
            <body>
              <main style="max-width: 500px; margin: 100px auto;">
                <h1>현재 로그인된 사용자</h1>
                <ul>
        """);

        for (String username : sessionList) {
            sb.append("<li>").append(username);
        }

        sb.append("""
                </ul>
              </main>
            </body>
            </html>
        """);

        HttpUtils.sendHtmlResponse(out, sb.toString());
        out.flush();
    }
}
