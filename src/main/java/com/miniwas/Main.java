// Main.java
package com.miniwas;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class Main {
    static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        // TODO: ServerSocket을 원하는 포트(예: 8080)로 열기
        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(PORT); // 리스닝 소켓(수신 전용) 8080번 포트에 들어오는 연결 요청만 받음
            //serverSocket.bind(endpoint, backlog); // 생성될때 내부적으로 호출되어 생략 가능하다.

            // TODO: 서버 시작 메시지 출력 ("Server started at http://localhost:PORT")
            System.out.println("Server started at http://localhost:" + PORT);

            while (true) {
                // TODO: 클라이언트 연결 수락 (serverSocket.accept())
                // 클라이언트 요청을 기다림(blocking 상태)
                try(Socket clientSocket = serverSocket.accept()) { // 연결 수락: TCP 3-way handshake
                    // TODO: RequestHandler 생성 및 handle() 호출
                    // 이제부터 소켓을 통해 getInputStream()/getOutputStream()으로 데이터 송수신이 가능하다.
                    RequestHandler req = new RequestHandler(clientSocket);
                    req.handle();
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            if (serverSocket!=null) {
                serverSocket.close();
            }
        }
    }
}
