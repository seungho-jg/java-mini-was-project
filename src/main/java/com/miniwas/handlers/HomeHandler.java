// HomeHandler.java
package com.miniwas.handlers;

import com.miniwas.handlers.Handler;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class HomeHandler implements Handler {
    @Override
    public void handle(BufferedReader in,
                       BufferedWriter out,
                       String method,
                       String path) throws IOException {
        // HTML 문자열
        String html = """
            <!DOCTYPE html>
            <html>
            <head>
              <meta charset="UTF-8">
              <title>MiniWAS Home</title>
            </head>
            <body>
              <h1>안녕하세요~~!</h1>
              <form method="post" action="/login">
                <input type="text"  name="id" placeholder="아이디" required/>
                <input type="password" name="password" placeholder="패스워드" required/>
                <button type="submit">로그인</button>
              </form>
              <p><a href="/register">회원가입</a></p>
            </body>
            </html>
            """;

        byte[] bodyBytes = html.getBytes(StandardCharsets.UTF_8);

        // 응답 헤더
        out.write("HTTP/1.1 200 OK\r\n");
        out.write("Content-Type: text/html; charset=UTF-8\r\n");
        out.write("Content-Length: " + bodyBytes.length + "\r\n");
        out.write("\r\n");  // 헤더 끝, 빈 줄

        // 본문 전송
        out.write(html);
        out.flush();
    }
}
