package com.miniwas.handlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RegisterHandler implements Handler {
    @Override
    public void handle(BufferedReader in,
                       BufferedWriter out,
                       String method,
                       String path) throws IOException {

        if (method.equals("GET")) {
            get(in, out);
        } else if (method.equals("POST")) {
            post(in, out);
        }
        out.flush();
    }

    public void get(BufferedReader in, BufferedWriter out) throws IOException {
        // HTML 문자열
        String html = """
            <!DOCTYPE html>
            <html>
            <head>
              <meta charset="UTF-8">
              <title>MiniWAS Register</title>
            </head>
            <body>
              <h1>회원가입</h1>
              <form method="post" action="/register">
              <input type="text"  name="id" placeholder="아이디" required/>
                <input type="text"  name="username" placeholder="이름" required/>
                <input type="password" name="password" placeholder="패스워드" required/>
                <button type="submit">회원가입</button>
              </form>
              <p><a href="/">로그인</a></p>
            </body>
            </html>
            """;

        byte[] bodyBytes = html.getBytes(StandardCharsets.UTF_8);

        // 응답 헤더
        out.write("HTTP/1.1 200 OK\r\n");
        out.write("Content-Type: text/html; charset=UTF-8\r\n");
        out.write("Content-Length: " + bodyBytes.length + "\r\n");
        out.write("\r\n");  // 헤더 끝, 빈 줄

        out.write(html);
    }

    public void post(BufferedReader in, BufferedWriter out) throws IOException {
        String line;
        int contentLength = 0;
        while (!(line = in.readLine()).isEmpty()) {
            if (line.startsWith("Content-Length:")) {
                contentLength = Integer.parseInt(line.split(":")[1].trim());
                System.out.println(line);
            }
        }

        // Content-Length 만큼 바디 읽기
        String body;
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

        // 상태 라인: 302 Found
        out.write("HTTP/1.1 302 Found\r\n");

        // Location 헤더: 리다이렉션할 경로
        out.write("Location: /\r\n");

        // Content-Length: 0 으로 바디 없음 명시
        out.write("Content-Length: 0\r\n");

        // 빈 줄: 헤더 종료
        out.write("\r\n");
    }
}
