package Dispatch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

import entity.FileBean;
import service.FileBeanService;

/**
 * Servlet implementation class UploadVideo
 */
public class UploadVideoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UploadVideoServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = request.getSession().getServletContext().getRealPath("/upload");
		System.out.println(path);
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 2������һ���ļ��ϴ�������
		ServletFileUpload upload = new ServletFileUpload(factory);
		// ���õ����ļ�������ϴ�ֵ
//		upload.setFileSizeMax(15*1024*1024L);
//		// ��������request�����ֵ
//		upload.setSizeMax(15*1024*1024L);
		// ����ϴ��ļ�������������
		upload.setHeaderEncoding("UTF-8");
		// 3���ж��ύ�����������Ƿ����ϴ���������
		if (!ServletFileUpload.isMultipartContent(request)) {
			return;
		}
		// 4��ʹ��ServletFileUpload�����������ϴ����ݣ�����������ص���һ��List<FileItem>���ϣ�ÿһ��FileItem��Ӧһ��Form����������
		List<FileItem> list = null;
		try {
			list = upload.parseRequest(request);
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
		

		
		HashMap<String, String> map = new HashMap<String, String>();

		System.out.println("-------------------------------------------------------------");
		for (FileItem item : list) {
			if (item.isFormField()) {
				/**
				 * ������
				 */
				String name = item.getFieldName();
				// �����ͨ����������ݵ�������������
				String value = item.getString("UTF-8");
				// value = new String(value.getBytes("iso8859-1"),"UTF-8");
				System.out.println(name + "=" + value);
				map.put(name, value);// ����map����
				
			} else {
				/**
				 * �ļ��ϴ�
				 */
				//guid��jspҳ���������
				File fileParent = new File(path + "/" + map.get("guid"));//��guid������ʱ�ļ���
				System.out.println(fileParent.getPath());
				if (!fileParent.exists()) {
					fileParent.mkdir();
				}
				
				
				String filename = item.getName();
				if (filename == null || filename.trim().equals("")) {
					continue;
				}
				// ע�⣺��ͬ��������ύ���ļ����ǲ�һ���ģ���Щ������ύ�������ļ����Ǵ���·���ģ��磺
				// c:\a\b\1.txt������Щֻ�ǵ������ļ������磺1.txt
				// �����ȡ�����ϴ��ļ����ļ�����·�����֣�ֻ�����ļ�������
				filename = filename.substring(filename.lastIndexOf("\\") + 1);

				//�����ļ�
				File file;
				if (map.get("chunks") != null) {
					file = new File(fileParent, map.get("chunk"));
				} else {
					file = new File(fileParent, "0");
				}
				
				/*int len;
				byte[] byt = new byte[4096];
				
				FileOutputStream outputStream = new FileOutputStream(file);
				while((len = item.getInputStream().read(byt))!=-1){
					outputStream.write(byt, 0, len);
				}
				outputStream.close();*/
				
				//copy
				FileUtils.copyInputStreamToFile(item.getInputStream(), file);
				
				long size = item.getSize();
				
				String sizeStr = "";
				if(size>=1024 && size<1024*1024){
					sizeStr = (size/1024)+"KB";
				}else if(size>1024*1024 && size<=1024*1024*1024){
					sizeStr = (size/(1024*1024))+"MB";
				}else if(size >= 1024*1024*1024){
					sizeStr = (size/(1024*1024*1024))+"GB";
				}else{
					sizeStr = size+"B";
				}

				
			}
		}
	}
}
