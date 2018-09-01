package web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.FileBean;
import service.FileBeanService;

/**
 * Servlet implementation class searchimage
 */
public class searchTxt extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		FileBeanService service = new FileBeanService();
		//String strname = request.getParameter("sename");
		//int id = Integer.parseInt(strname);
		//strname = "\'"+strname+"\'";
		//FileBean bean = service.findByType("image/jpeg");
		//FileBean bean = service.findByName(strname);
		//FileBean bean = service.findById(id);
		List<FileBean> list = service.findByType("text%");
		
		//System.out.println(bean.getFile_path());
		request.setAttribute("list", list);
		request.getRequestDispatcher("/searchTxt.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
