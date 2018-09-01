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
			
			//1)�ļ�������������
			upload.setHeaderEncoding("utf-8");
			//2)�����ļ���С----��Ҫɾ��
//			upload.setFileSizeMax(100*1024);//ÿ���ļ�
//			upload.setSizeMax(500*1024);//���ļ�
			
			//����
			List<FileItem> list = upload.parseRequest(request);
			
			FileBeanService service = new FileBeanService();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			List<FileBean> fileList = new ArrayList<FileBean>();
			
			if(list!=null){
				FileBean bean = null;
				//����ÿ���ļ�
				for (FileItem item : list) {
					if(item.isFormField()){
						//��ͨ���
						//��������
						bean.setInfo(item.getString("utf-8"));
						fileList.add(bean);
						//���浽���ݿ�
						service.saveFile(bean);
					}else{
						bean = new FileBean();
						//1)���ļ����浽������Ӳ����
						//String uuid = UUID.randomUUID().toString();
						String fileName = item.getName();
						//1.1 ����ļ����ƣ���ֹ�ظ�
						//fileName = uuid+fileName.substring(fileName.lastIndexOf("."));
						
						//1.2 ���Ŀ¼�ṹ
						//�õ�webӦ���е�ĳ��Ŀ¼�ľ���·��: e:/tomcat6/webapps/day22/upload
						String baseDir = this.getServletContext().getRealPath("/upload");
						
						String subDir = makeDirectory(fileName);
						
						String finalDir = baseDir +"/"+ subDir; // e:/tomcat6/webapps/day22/upload/10/8
						
						long size = item.getSize();
						String contentType = item.getContentType();
						FileUtils.copyInputStreamToFile(item.getInputStream(), new File(finalDir+fileName));
						//ɾ����ʱ�ļ�
						item.delete();
						//e:/tomcat6/webapps/day22/upload/10/8/23232323232.jpg
						
						//2)���ļ���Ϣ���浽���ݿ���
						//2.1 ��װFileBean����
						
						bean.setName(fileName);
						//�����ļ���С
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
				//����ɵ��ļ���Ϣת����jspҳ����ʾ
				request.setAttribute("fileList", fileList);
				request.getRequestDispatcher("/success.jsp").forward(request, response);
			}
	//����catch��Ҫɾ��
		} catch(FileSizeLimitExceededException e){//ÿ���ļ�������
			request.setAttribute("msg", "ÿ���ļ����ܳ���100KB");
			request.getRequestDispatcher("/upload.jsp").forward(request, response);
		} catch(SizeLimitExceededException e){ //���ļ�����
			request.setAttribute("msg", "���ļ���С���ܳ���500KB");
			request.getRequestDispatcher("/upload.jsp").forward(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	private String makeDirectory(String fileName){
			//1.�õ��ļ�����hashCodeֵ
			int code = fileName.hashCode();
			
			//2.�����һ��Ŀ¼������
			int first = code & 0xF;
			
			//3.����ڶ���Ŀ¼������
			int second = code & (0xF>>1);
			return first+"/"+second+"/";
	}
		
		
		


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
