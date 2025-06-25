
package com.miniwas;

import java.io.*;
import java.net.Socket;



public class RequestHandler {
    private final Socket client;

    public RequestHandler(Socket client) {
        this.client = client;
    }

    public void handle() throws IOException {
        /* 여기서 BufferedReader를 사용하는 이유
            InputStreamReader로 바이트 단위로 읽는 것 보다 8kb 정도의 버퍼를 가지고 한번에 읽어오는게
            시스템 콜 회수를 줄여 부담이 적다.
        */
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

        // TODO: 요청 첫 줄 읽기 및 null 체크
        String requestLine = in.readLine(); // GET / HTTP/1.1
        // 1) null 체크: 아무것도 없다면 처리할 게 없으니 바로 리턴
        if (requestLine == null) {
            return;
        }

        // TODO: method와 path 파싱 (split by space)
        String[] parts = requestLine.split(" ");
        String method = parts[0];
        String path = parts[1];
        String version = parts[2];
        System.out.println(method + path + version);

        // TODO: 응답 바디 생성 ("Hello from MiniWAS: " + method + " " + path)

        // TODO: HTTP 응답 문자열 구성 (상태 라인, 헤더, 빈 줄, 본문)

        // TODO: 응답 전송 (out.write(...) 및 out.flush())

        // TODO: 클라이언트 소켓 닫기 (client.close())
    }
}