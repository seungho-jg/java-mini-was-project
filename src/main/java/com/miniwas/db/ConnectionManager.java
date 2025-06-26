package com.miniwas.db;

import com.miniwas.Main;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class ConnectionManager {
    // DB 파일 경로
    private static final String JDBC_URL = "jdbc:sqlite:miniwas.db";
    // volatile : 변수 값을 읽을 때 캐시에 저장된 값이 아닌 메모리에 저장된 최신값을 읽어온다.
    private static volatile boolean initialized = false;

    // static block : 클래스가 로드될 때 딱 한 번 실행
    static {
        try {
            // JDBC 드라이버 등록
            /* 최신버전은 import 만으로 자동 등록(호출) 가능*/
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("JDBC 드라이버 로드 실패", e);
        }
    }
    // 데이터베이스 커넥션을 반환, 사용후 close() 필요
    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(JDBC_URL);
        initSchema(conn);
        return conn;
    }

    private static synchronized void initSchema(Connection connection) {
        if (initialized) return;

        try {
            initialized = true;
            Statement stmt = connection.createStatement();
            InputStream is = Main.class.getClassLoader().getResourceAsStream("schema.sql");
            BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(is)));
            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sql.append(line).append('\n');
            }
            // ';' 기준으로 분리해 한 줄씩 실행
            for (String ddl : sql.toString().split(";")) {
                if (!ddl.isBlank()) {
                    stmt.execute(ddl.trim());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}
