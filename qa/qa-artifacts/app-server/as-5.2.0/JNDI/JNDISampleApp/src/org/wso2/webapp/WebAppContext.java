package org.wso2.webapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class WebAppContext
 */
@WebServlet("/WebAppContext")
public class WebAppContext extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WebAppContext() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 Context initCtx;

		 try {

	            initCtx = new InitialContext();

	            PrintWriter out = response.getWriter();
	           
	            try{
	                DataSource tomcatDataSource = (DataSource) initCtx.lookup("java:comp/env/jdbc/WebAppTestDB");
	                
	               
	                
	                Connection con=tomcatDataSource.getConnection();
	                Statement stmt = con.createStatement();
	                String query = "select * from employee";
	                ResultSet rs = stmt.executeQuery(query);
	     
	                
	                
	                
	               
	                response.setContentType("text/html");
	                out.print("<center><h1>Accessing JNDI resources-Registered at WebApp context level</h1></center>");
	                out.print("<html><body><center><table border=\"1\" cellspacing=10 cellpadding=5>");
	                
	                out.print("<th>Employee Name</th>");
	               
	                out.print("<th>Employee Age</th>");
	               
	                
	                while(rs.next())
	                {
	                    out.print("<tr>");
	       
	                    out.print("<td>" + rs.getString("name") + "</td>");
	                   
	                    out.print("<td>" + rs.getString("age") + "</td>");
	                   
	                    out.print("</tr>");
	                   
	                }
	                out.print("</center></table></body></html>");
	                
	                out.print("###############################################");
	                
	                

	            }catch(Exception e){
	                response.getWriter().println("Error in accessing JNDI resource-Configured at Web App Level");
	                response.getWriter().println(e.getMessage());
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	            
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
