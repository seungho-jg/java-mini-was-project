package com.miniwas;

import java.nio.charset.StandardCharsets;

public class CustomResponse {
    String body = null;
    byte[] bodyBytes = null;
    public CustomResponse(String msg) {
        this.body = msg;
        this.bodyBytes = body.getBytes(StandardCharsets.UTF_8);
    }
    public String getResponse() {
        // HTTP 응답 문자열 구성 (상태 라인, 헤더, 빈 줄, 본문)
        String statusLine = "HTTP/1.1 200 OK\r\n";
        String header = "Content-Type: text/plain; charset=UTF-8\r\n";
        String header2 = "Content-Length: " + this.bodyBytes.length + "\r\n";
        String empty = "\r\n"; // RFC 7230 3.5 “빈 줄이 헤더 섹션의 끝을 표시한다”

        // 응답 전송
        return statusLine + header + header2 + empty + this.body;
    }
}
