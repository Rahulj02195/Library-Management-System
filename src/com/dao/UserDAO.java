package com.dao;

import com.util.DBConnection;
import com.model.User;
import java.sql.*;

public class UserDAO {

    public User login(String username, String password) throws Exception {
        Connection con = DBConnection.getConnection();
        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, password);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            User u = new User();
            u.id = rs.getInt("user_id");
            u.username = rs.getString("username");
            u.role = rs.getString("role");
            return u;
        }
        return null;
    }
    public void createStudent(String username, String password) throws Exception {
        Connection con = DBConnection.getConnection();
        String sql = "INSERT INTO users(username,password,role) VALUES(?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, password);
        ps.setString(3, "STUDENT");
        ps.executeUpdate();
    }

}
