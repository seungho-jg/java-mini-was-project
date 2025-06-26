package com.miniwas.db;

import com.miniwas.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserDao {
    /*
    * 회원가입: 새 사용자 추가
    * */
    public boolean createUser(String username, String password, String nickname) throws SQLException {
        String sql = "INSERT INTO users(username, password, nickname) VALUES(?, ?, ?)";
        Connection conn = null;
        try {
            conn = ConnectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, nickname);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            return false;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public Optional<User> findByCredentials(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username == ? AND password = ?";
        Connection conn = null;
        try {
            conn = ConnectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("nickname")
                );
                return Optional.of(user);
            } else {
                return Optional.empty();
            }

        } catch (SQLException e) {
            return Optional.empty();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }
}
