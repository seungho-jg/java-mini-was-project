// HomeHandler.java
package com.miniwas.handlers;

import com.miniwas.SessionManager;
import com.miniwas.model.User;
import com.miniwas.utils.HttpUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import com.miniwas.db.UserDao;

public class LoginHandler implements Handler {
    private final UserDao dao;
    private final SessionManager sessionManager;

    public LoginHandler(UserDao dao, SessionManager sessionManager) {
        this.dao  = dao;
        this.sessionManager = sessionManager;
    }
    private static final String LOGIN_HTML = """
            <!DOCTYPE html>
            <html>
            <head>
              <meta charset="UTF-8">
              <title>MiniWAS Login</title>
              <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/water.css@2/out/light.css" />
            </head>
            <body>
            <main style="max-width: 300px; margin: 100px auto; text-align: center;">
              <h1>로그인</h1>
              <form method="post" action="/login">
                <input type="text"  name="id" placeholder="아이디" required style="width: 100%; max-width: 300px;"/>
                <input type="password" name="password" placeholder="패스워드" required style="width: 100%; max-width: 300px;"/>
                <button type="submit">로그인</button>
              </form>
              <p><a href="/register">회원가입</a></p>
            </main>
            </body>
            </html>
            """;

    @Override
    public void handle(BufferedReader in,
                       BufferedWriter out,
                       String method,
                       String path) throws IOException {

        try {
            switch (method) {
                case "GET":
                    handleGet(out);
                    break;
                case "POST":
                    handlePost(in, out);
                    break;
                default:
                    HttpUtils.sendMethodNotAllowed(out);
            }
        } finally {
            out.flush();
        }
    }
    public void handleGet(BufferedWriter out) throws IOException {
        HttpUtils.sendHtmlResponse(out, LOGIN_HTML);
    }

    public void handlePost(BufferedReader in, BufferedWriter out) throws IOException {
        // header 읽기
        String line;
        int contentLenght = 0;
        while (!(line = in.readLine()).isEmpty()) {
            if (line.startsWith("Content-Length")) {
                contentLenght = Integer.parseInt(line.split(":")[1].trim());
            }
        }
        // 바디 읽기
        String body = "";
        if (contentLenght > 0) {
            int totalRead = 0;
            char[] buffer = new char[contentLenght];
            while (totalRead < contentLenght) {
                int read = in.read(buffer, totalRead, contentLenght - totalRead);
                if (read == -1) break;
                totalRead += read;
            }
            body = new String(buffer, 0, totalRead);
        }

        // body 파싱
        String[] data = body.split("&");
        String username = data[0].split("=")[1];
        String password = data[1].split("=")[1];

        String username_decoded = URLDecoder.decode(username, StandardCharsets.UTF_8);

        ;
        try {
            Optional<User> find = this.dao.findByCredentials(username_decoded, password);
            //로그인 성공
            if (find.isPresent()) {
                User user = find.get();
                // 세션 생성
                String sessionId = this.sessionManager.create(user.getUsername());

                // Set-Cookie + 리다이렉트 응답
                out.write("HTTP/1.1 302 Found\r\n");
                out.write("Set-Cookie: SESSIONID=" + sessionId + "; HttpOnly; Path=/\r\n");
                out.write("Location: /\r\n");
                out.write("Content-Length: 0\r\n");
                out.write("\r\n");
            }
        } catch (Exception e) {
            System.out.println("로그인 실패");
        }
    }
}
