package server;

import builders.Auntification;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Base64;

public class SqlUserManager {
    private Connection conn;

    public SqlUserManager(Connection connection) {
        this.conn = connection;
    }

    public void initUserTable() throws SQLException {
        try (Statement s = conn.createStatement()) {
            String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS users(" +
                    "id BIGSERIAL PRIMARY KEY," +
                    "user_name varchar(127) NOT NULL UNIQUE," +
                    "password varchar (127) NOT NULL)";
            s.execute(CREATE_TABLE_USER);
        }
    }


    public void registration(Auntification auntification) throws NoSuchAlgorithmException {
        String newPassword = hashPassword(auntification.getPassword());
        try (PreparedStatement s = conn.prepareStatement("INSERT INTO users (user_name, password) VALUES (?, ?)")) {
            s.setString(1, auntification.getNickname());
            s.setString(2, newPassword);
            s.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());
        String encodedHash = Base64.getEncoder().encodeToString(hash);
        return encodedHash;
    }

    public Boolean searchUser(Auntification auntification) {
        try (PreparedStatement s = conn.prepareStatement("SELECT id, password FROM users WHERE user_name = ? LIMIT 1")) {
            s.setString(1, auntification.getNickname());
            try (ResultSet res = s.executeQuery()) {
                if (res.next()) {
                    String realPasswordHashed = res.getString("password");
                    String passwordHashed = hashPassword(auntification.getPassword());
                    if (passwordHashed.equals(realPasswordHashed)) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public Long login(Auntification auntification) {
        try (PreparedStatement s = conn.prepareStatement("SELECT id, password FROM users WHERE user_name = ? LIMIT 1")) {
            s.setString(1, auntification.getNickname());
            try (ResultSet res = s.executeQuery()) {
                if (res.next()) {
                    String realPasswordHashed = res.getString("password");
                    String passwordHashed = hashPassword(auntification.getPassword());
                    if (passwordHashed.equals(realPasswordHashed)) {
                        return res.getLong("id");
                    }
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
