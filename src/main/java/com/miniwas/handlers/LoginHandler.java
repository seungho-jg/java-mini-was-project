// HomeHandler.java
package com.miniwas.handlers;

import com.miniwas.utils.HttpUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class LoginHandler implements Handler {
    private static final String LOGIN_HTML = """
            <!DOCTYPE html>
            <html>
            <head>
              <meta charset="UTF-8">
              <title>MiniWAS Login</title>
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

    }
}
