package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.ToDaDAO;
import dao.ToDaDAOImpl;


@WebServlet("/MarkTaskCompletedServlet")
public class MarkTaskCompletedServlet extends HttpServlet {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html");
    PrintWriter out=response.getWriter();
    HttpSession session=request.getSession();
    ServletContext context=getServletContext();
    
    int regId=Integer.parseInt(request.getParameter("regId"));
    int taskId=Integer.parseInt(request.getParameter("taskId"));
    
    ToDaDAO dao=ToDaDAOImpl.getInstance();
    boolean flag = dao.markTaskCompleted(taskId, regId);;
    if(flag)
      context.getRequestDispatcher("/ViewTasks.jsp").forward(request, response);
    else
      out.println("TX Failed, Task not marked");
  }

}