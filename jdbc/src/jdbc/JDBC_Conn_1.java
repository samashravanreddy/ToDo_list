package jdbc;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBC_Conn_1 {
	public static void main(String[] args) {
		try {
			// loading JDBC driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			// Establishing Connection to DB 
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/sbb6_todo", "root", "");
			
			System.out.println("database sucessfully connected "+ con);
			
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}catch(SQLException e) {
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}	
	}
}