import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DiningCountServlet")
public class DiningCountServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public DiningCountServlet() {
        super();
    }

    // Handle GET requests properly
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<h3>Error: This servlet only supports POST requests.</h3>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            // Convert date string to java.sql.Date
            String dining_date = String.valueOf(request.getParameter("dining_date").trim());

            int boys = Integer.parseInt(request.getParameter("boys").trim());
            int girls = Integer.parseInt(request.getParameter("girls").trim());
            int medical_staff = Integer.parseInt(request.getParameter("medical_staff").trim());
            int mess_staff = Integer.parseInt(request.getParameter("mess_staff").trim());
            int sports = Integer.parseInt(request.getParameter("sports").trim());
            int events = Integer.parseInt(request.getParameter("events").trim());
            int parents = Integer.parseInt(request.getParameter("parents").trim());

            boolean success = storeData(  dining_date, boys, girls, medical_staff, mess_staff, sports, events, parents);

            if (success) {
                out.println("Record inserted successfully.");
            } else {
                out.println("Failed to insert record.");
            }

        } catch (IllegalArgumentException e) {
            out.println("Error: Invalid date format. Use YYYY-MM-DD.");
            e.printStackTrace();
        } catch (Exception e) {
            out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean storeData(String dining_date, int boys, int girls, int medical_staff, int mess_staff, int sports, int events, int parents) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hmms?useSSL=false&serverTimezone=UTC", "root", "");
            System.out.println("Database connected successfully.");

            String sql = "INSERT INTO dining_count_status (dining_date, boys, girls, medical_staff, mess_staff, sports, events, parents) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, dining_date); 
            pstmt.setInt(2, boys);
            pstmt.setInt(3, girls);
            pstmt.setInt(4, medical_staff);
            pstmt.setInt(5, mess_staff);
            pstmt.setInt(6, sports);
            pstmt.setInt(7, events);
            pstmt.setInt(8, parents);

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
            return rowsAffected == 1;

        } catch (ClassNotFoundException e) {
            System.err.println("Error: MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
