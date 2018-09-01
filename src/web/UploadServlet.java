package web;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

import entity.FileBean;
import service.FileBeanService;

/**
 * Servlet implementation class UploadServlet
 */
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			DiskFileItemFactory factory = new DiskFileItemFactory(10*1024,new File("H:/temp"));
			
			ServletFileUpload upload = new ServletFileUpload(factory);
			
			//1)文件名称中文问题
			upload.setHeaderEncoding("utf-8");
			//2)限制文件大小----需要删除
//			upload.setFileSizeMax(100*1024);//每个文件
//			upload.setSizeMax(500*1024);//总文件
			
			//解析
			List<FileItem> list = upload.parseRequest(request);
			
			FileBeanService service = new FileBeanService();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			List<FileBean> fileList = new ArrayList<FileBean>();
			
			if(list!=null){
				FileBean bean = null;
				//遍历每个文件
				for (FileItem item : list) {
					if(item.isFormField()){
						//普通组件
						//设置描述
						bean.setInfo(item.getString("utf-8"));
						fileList.add(bean);
						//保存到数据库
						service.saveFile(bean);
					}else{
						bean = new FileBean();
						//1)把文件保存到服务器硬盘中
						//String uuid = UUID.randomUUID().toString();
						String fileName = item.getName();
						//1.1 随机文件名称，防止重复
						//fileName = uuid+fileName.substring(fileName.lastIndexOf("."));
						
						//1.2 组件目录结构
						//得到web应用中的某个目录的绝对路径: e:/tomcat6/webapps/day22/upload
						String baseDir = this.getServletContext().getRealPath("/upload");
						
						String subDir = makeDirectory(fileName);
						
						String finalDir = baseDir +"/"+ subDir; // e:/tomcat6/webapps/day22/upload/10/8
						
						long size = item.getSize();
						String contentType = item.getContentType();
						FileUtils.copyInputStreamToFile(item.getInputStream(), new File(finalDir+fileName));
						//删除临时文件
						item.delete();
						//e:/tomcat6/webapps/day22/upload/10/8/23232323232.jpg
						
						//2)把文件信息保存到数据库中
						//2.1 封装FileBean对象
						
						bean.setName(fileName);
						//计算文件大小
						/**
						 *  e.g.   1024b=1KB  1024*1024b=1MB
						 */
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
						bean.setSize(sizeStr);
						bean.setType(contentType);
						bean.setAddTime(sdf.format(new Date()));
						bean.setFile_path("/upload"+"/"+subDir+fileName);	
					}										
				}
				//把完成的文件信息转发到jsp页面显示
				request.setAttribute("fileList", fileList);
				request.getRequestDispatcher("/success.jsp").forward(request, response);
			}
	//两个catch需要删除
		} catch(FileSizeLimitExceededException e){//每个文件的限制
			request.setAttribute("msg", "每个文件不能超过100KB");
			request.getRequestDispatcher("/upload.jsp").forward(request, response);
		} catch(SizeLimitExceededException e){ //总文件限制
			request.setAttribute("msg", "总文件大小不能超过500KB");
			request.getRequestDispatcher("/upload.jsp").forward(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	private String makeDirectory(String fileName){
			//1.得到文件名的hashCode值
			int code = fileName.hashCode();
			
			//2.算出第一层目录的名称
			int first = code & 0xF;
			
			//3.算出第二层目录的名称
			int second = code & (0xF>>1);
			return first+"/"+second+"/";
	}
		
		
		


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
