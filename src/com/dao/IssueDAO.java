package com.dao;

import com.util.DBConnection;
import java.sql.*;

public class IssueDAO {

    
    public void issueBook(int bookId, int userId) throws Exception {
        Connection con = DBConnection.getConnection();
        con.setAutoCommit(false);

       
        String checkQty = "SELECT quantity FROM books WHERE book_id=?";
        PreparedStatement ps1 = con.prepareStatement(checkQty);
        ps1.setInt(1, bookId);
        ResultSet rs = ps1.executeQuery();

        if (rs.next() && rs.getInt(1) > 0) {

            
            String issueSql =
                "INSERT INTO issued_books(book_id,user_id,issue_date) VALUES(?,?,CURDATE())";
            PreparedStatement ps2 = con.prepareStatement(issueSql);
            ps2.setInt(1, bookId);
            ps2.setInt(2, userId);
            ps2.executeUpdate();

           
            String updateSql =
                "UPDATE books SET quantity = quantity - 1 WHERE book_id=?";
            PreparedStatement ps3 = con.prepareStatement(updateSql);
            ps3.setInt(1, bookId);
            ps3.executeUpdate();

            con.commit();
            System.out.println("Book issued successfully!");

        } else {
            System.out.println("Book not available!");
            con.rollback();
        }

        con.setAutoCommit(true);
    }

   
    public void viewIssuedBooks() throws Exception {
        Connection con = DBConnection.getConnection();

        String sql =
            "SELECT i.issue_id, b.title, u.username, i.issue_date " +
            "FROM issued_books i " +
            "JOIN books b ON i.book_id = b.book_id " +
            "JOIN users u ON i.user_id = u.user_id";

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);

        System.out.println("\nID  Book  Issued To  Date");
        while (rs.next()) {
            System.out.println(
                rs.getInt(1) + "  " +
                rs.getString(2) + "  " +
                rs.getString(3) + "  " +
                rs.getDate(4)
            );
        }
    }
}
