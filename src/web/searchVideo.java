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
 * Servlet implementation class searchVideo
 */
public class searchVideo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		FileBeanService service = new FileBeanService();

		List<FileBean> list = service.findByType("video%");
		
		//System.out.println(bean.getFile_path());
		request.setAttribute("list", list);
		request.getRequestDispatcher("/searchvideo.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
