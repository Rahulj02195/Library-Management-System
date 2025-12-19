package com.dao;

import com.util.DBConnection;
import java.sql.*;

public class RequestDAO {

   
    public void requestBook(int bookId, int userId) throws Exception {
        Connection con = DBConnection.getConnection();
        String sql =
            "INSERT INTO issue_requests(book_id,user_id,status,request_date) " +
            "VALUES(?,?, 'PENDING', CURDATE())";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, bookId);
        ps.setInt(2, userId);
        ps.executeUpdate();

        System.out.println("Book issue request submitted!");
    }

    
    public void viewPendingRequests() throws Exception {
        Connection con = DBConnection.getConnection();
        String sql =
            "SELECT r.request_id, b.title, u.username, r.request_date " +
            "FROM issue_requests r " +
            "JOIN books b ON r.book_id = b.book_id " +
            "JOIN users u ON r.user_id = u.user_id " +
            "WHERE r.status='PENDING'";

        ResultSet rs = con.createStatement().executeQuery(sql);

        System.out.println("\nReqID  Book  Student  Date");
        while (rs.next()) {
            System.out.println(
                rs.getInt(1) + "  " +
                rs.getString(2) + "  " +
                rs.getString(3) + "  " +
                rs.getDate(4)
            );
        }
    }

   
    public void approveRequest(int requestId, IssueDAO issueDAO) throws Exception {
        Connection con = DBConnection.getConnection();

        String getReq =
            "SELECT book_id, user_id FROM issue_requests WHERE request_id=? AND status='PENDING'";
        PreparedStatement ps = con.prepareStatement(getReq);
        ps.setInt(1, requestId);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int bookId = rs.getInt(1);
            int userId = rs.getInt(2);

            issueDAO.issueBook(bookId, userId);

            String update =
                "UPDATE issue_requests SET status='APPROVED' WHERE request_id=?";
            PreparedStatement ps2 = con.prepareStatement(update);
            ps2.setInt(1, requestId);
            ps2.executeUpdate();

            System.out.println("Request approved!");
        } else {
            System.out.println("Invalid request!");
        }
    }

    
    public void viewMyBooks(int userId) throws Exception {
        Connection con = DBConnection.getConnection();
        String sql =
            "SELECT b.title, i.issue_date " +
            "FROM issued_books i " +
            "JOIN books b ON i.book_id=b.book_id " +
            "WHERE i.user_id=?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, userId);

        ResultSet rs = ps.executeQuery();

        System.out.println("\nBook  Issued Date");
        while (rs.next()) {
            System.out.println(
                rs.getString(1) + "  " +
                rs.getDate(2)
            );
        }
    }
}
