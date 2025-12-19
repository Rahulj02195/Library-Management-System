package com.dao;

import com.util.DBConnection;
import java.sql.*;

public class BookDAO {

    public void addBook(String title, String author, int qty) throws Exception {
        Connection con = DBConnection.getConnection();
        String sql = "INSERT INTO books(title,author,quantity) VALUES(?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, author);
        ps.setInt(3, qty);
        ps.executeUpdate();
    }

    public void viewBooks() throws Exception {
        Connection con = DBConnection.getConnection();
        ResultSet rs = con.createStatement().executeQuery("SELECT * FROM books");

        System.out.println("\nID  Title  Author  Qty");
        while (rs.next()) {
            System.out.println(
                rs.getInt(1) + "  " +
                rs.getString(2) + "  " +
                rs.getString(3) + "  " +
                rs.getInt(4)
            );
        }
    }
}
