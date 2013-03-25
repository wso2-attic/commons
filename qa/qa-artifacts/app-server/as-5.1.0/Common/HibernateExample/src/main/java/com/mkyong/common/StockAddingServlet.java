package com.mkyong.common;



import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.mkyong.persistence.HibernateUtil;

/**
 * Servlet implementation class StockServlet
 */
@WebServlet("/StockServlet")
public class StockAddingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public StockAddingServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		try 
		{
			String code = request.getParameter("code");
			String name = request.getParameter("name");
			
	        System.out.println("Maven + Hibernate + MySQL");
	        Session session = HibernateUtil.getSessionFactory().openSession();
	        
	        session.beginTransaction();
	        Stock stock = new Stock();
	        
	        stock.setStockCode(code);
	        stock.setStockName(name);
	        
	        session.save(stock);
	        session.getTransaction().commit();
	        
	        // request.getRequestDispatcher("response/thankYou.jsp").forward(request, response);
	        
	        // request.getRequestDispatcher("/listStocks").forward(request, response);
	        
	        response.sendRedirect("listStocks");
		}catch(Exception e)
		{
			// request.getRequestDispatcher("response/error.jsp").forward(request, response);		
			throw new ServletException(e);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
