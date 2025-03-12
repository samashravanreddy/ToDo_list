package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.sql.Statement;

import java.util.ArrayList;

import beans.Register;
import beans.Task;
import factory.DBConn;

public class ToDaDAOImpl implements ToDaDAO {
	Connection con;
	Statement stmt;
	PreparedStatement pstmt1,pstmt2,pstmt3,pstmt4;
	ResultSet rs;
	static ToDaDAO toDaDAO;
	
	private ToDaDAOImpl() {
		try {
			con=DBConn.getConn();
			stmt= con.createStatement();
			pstmt1=con.prepareStatement("INSERT INTO register VALUES(?,?,?,?,?,?,?)");
			pstmt2=con.prepareStatement("INSERT INTO tasks VALUES(?,?,?,?,?)");
			pstmt3=con.prepareStatement("INSERT INTO taskid_pks VALUES(?,?)");
			pstmt4=con.prepareStatement("Update taskid_pks SET taskid=? WHERE regid=?");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static ToDaDAO getInstance() {
		if(toDaDAO==null)
			toDaDAO=new ToDaDAOImpl();
		return toDaDAO;
	
	}

	@Override
	public int register(Register reg) {
		// TODO Auto-generated method stub
		int regid=0;
		try {
			rs= ((java.sql.Statement) stmt).executeQuery("select max(regid)from register");
			if(rs.next()) {
				regid=rs.getInt(1);
			}
			regid++;
			
			//record insertation
			pstmt1.setInt(1, regid);
			pstmt1.setString(2, reg.getFname());
			pstmt1.setString(3, reg.getLname());
			pstmt1.setString(4, reg.getEmail());
			pstmt1.setString(5, reg.getPass());
			pstmt1.setLong(6, reg.getMobile());
			pstmt1.setString(7,reg.getAddress());
			int i=pstmt1.executeUpdate();
			if(i==1) 
				System.out.println(
						"Record inserted into register table");
			
		}catch (Exception e) {
			e.printStackTrace();	
			}
		return regid;
	}

	@Override
	public int login(String email, String pass) {
		int regId=0;
		try {
			 rs=stmt.executeQuery("select regid from register where email='"+email+"' and pass='"+pass+"'");
			   if(rs.next()) {
			    regId=rs.getInt(1);
			   }
			  } catch(Exception e) {
			   e.printStackTrace();
			  }
			  return regId;
			 }

	@Override
	public int addTask(Task task, int regid) {
		int taskId = 0;
		boolean isNew = true;
		int i, j = 0;
		try {
			rs = stmt.executeQuery("select taskid from taskid_pks where regid=" + regid);
			if (rs.next()) {
				isNew = false;
				taskId = rs.getInt(1);
			}
			taskId++;

			con.setAutoCommit(false); // Transaction Start
			pstmt2.setInt(1, taskId);
			pstmt2.setString(2, task.getTaskname());
			pstmt2.setString(3, task.getTaskdate());
			pstmt2.setInt(4, task.getTaskstatus());
			pstmt2.setInt(5, task.getRegid());
			i = pstmt2.executeUpdate();

			if (isNew) {
				pstmt3.setInt(1, task.getRegid());
				pstmt3.setInt(2, taskId);
				j = pstmt3.executeUpdate();
			} else {
				pstmt4.setInt(1, taskId);
				pstmt4.setInt(2, task.getRegid());
				j = pstmt4.executeUpdate();
			}

			if (i == 1 && j == 1) {
				con.commit(); // ✅ Commit TX
				System.out.println("TX Success, task added");
				return 1; // ✅ Task added successfully
			} else {
				con.rollback(); // ❌ Rollback TX
				System.out.println("TX Failed");
				return -1; // Task Not Added
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // Task Not Added
	}


	@Override
	public List<Task> findAllTasksByRegid(int regid) {
		List<Task> taskList=new ArrayList<Task>();
		  try {
		   rs=stmt.executeQuery("select * from tasks where regid="+regid);
		   while(rs.next()) {
		    Task task=new Task(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getInt(4),rs.getInt(5));
		    taskList.add(task);
		   }
		  } catch(Exception e) {
		   e.printStackTrace();
		  }
		  return taskList;
		 }

	@Override
	public boolean markTaskCompleted(int taskid, int regid) {
		  boolean flag=false;
		  try {
		   int i=stmt.executeUpdate("update tasks set taskstatus=3 where regid="+regid+" and taskid="+taskid);
		   if(i==1) {
		    flag=true;
		    System.out.println("Task "+taskid+" of "+regid+" completed");
		   }
		  } catch(Exception e) {
		   e.printStackTrace();
		  }
		  return flag;
		 }
@Override
public String getFLNameByRegid(int regid) {
	String flname = null;
	try {
		rs = stmt.executeQuery("SELECT fname, lname FROM register WHERE regid=" + regid);
		if (rs.next()) {
			flname = rs.getString(1) + " " + rs.getString(2);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return flname;
}
}

		