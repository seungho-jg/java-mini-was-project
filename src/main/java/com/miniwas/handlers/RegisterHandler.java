package com.miniwas.handlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.nio.charset.StandardCharsets;

import com.miniwas.db.UserDao;

import com.miniwas.utils.HttpUtils;

public class RegisterHandler implements Handler {
    private static final String REGISTER_HTML = """
        <!DOCTYPE html>
        <html>
        <head>
          <meta charset="UTF-8">
          <title>MiniWAS Register</title>
          <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/water.css@2/out/light.css" />
        </head>
        <body>
          <h1>회원가입</h1>
          <form method="post" action="/register">
            <input type="text" name="username" placeholder="이름" required/>
            <input type="password" name="password" placeholder="패스워드" required/>
            <button type="submit">회원가입</button>
          </form>
          <p><a href="/login">로그인</a></p>
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
                    break;
            }
        } finally {
            out.flush();
        }
    }

    public void handleGet(BufferedWriter out) throws IOException {
        HttpUtils.sendHtmlResponse(out, REGISTER_HTML);
    }

    public void handlePost(BufferedReader in, BufferedWriter out) throws IOException {
        // header 읽기
        String line;
        int contentLength = 0;
        while (!(line = in.readLine()).isEmpty()) {
            if (line.startsWith("Content-Length:")) {
                contentLength = Integer.parseInt(line.split(":")[1].trim());
                System.out.println(line);
            }
        }

        // Content-Length 만큼 바디 읽기
        String body = "";
        if (contentLength > 0) {
            int totalRead = 0;
            char[] buffer = new char[contentLength];
            while (totalRead < contentLength) {
                int read = in.read(buffer, totalRead, contentLength - totalRead);
                if (read == -1) break;
                totalRead += read;
            }

            body = new String(buffer, 0, totalRead);
        }
        System.out.println("POST Body: " + body);

        // body 파싱 Body: id=1313&username=1313&password=1313
        String[] data = body.split("&");
        String username = data[0].split("=")[1];
        String password = data[1].split("=")[1];
        String username_decoded= URLDecoder.decode(username, StandardCharsets.UTF_8);
        System.out.println(username_decoded + " " + password);

        //회원가입 로직
        UserDao dao = new UserDao();
        try {
            if (dao.createUser(username_decoded, password)) {
                // 리다이렉션
                HttpUtils.sendRedirect(out, "/login");
            } else {
                // 리다이렉션
                HttpUtils.sendCustomResponse(out,"회원가입에 실패");
            }
        } catch (SQLException e) {
            // 리다이렉션
            HttpUtils.sendCustomResponse(out,"회원가입에 실패");
        }



    }

}
