package Dispatch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;
//import org.springframework.util.FileCopyUtils;

import entity.FileBean;
import service.FileBeanService;

public class UploadSuccessServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String sizeStr;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadSuccessServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = request.getSession().getServletContext().getRealPath("/upload");

		String guid = request.getParameter("guid");
		int chunks = Integer.parseInt(request.getParameter("chunks"));
		String fileName = request.getParameter("fileName");
		
		System.out.println("start...!guid="+guid+";chunks="+chunks+";fileName="+fileName);
		//����bean��������ϴ��ļ�����Ϣ
		FileBean bean = new FileBean();
		FileBeanService service = new FileBeanService();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		

		
		/**
		 * �����ļ��ϲ�
		 */
		
		File file = new File(path+"/"+guid);
		/**
		 * �жϷ�Ƭ�����Ƿ���ȷ
		 */
		if(file.list().length != chunks){
			return;
		}
		//new File("H://upload"+"//"+guid).mkdirs();  ȡ��guid
		//new File("H://upload").mkdirs();
		/**
		 * �����ļ��ϲ�
		 */
		//File newFile = new File("H://upload"+"//"+fileName);
		//���ļ��洢����Ŀupload��
		File newFile = new File(path+"/"+fileName);
		FileOutputStream outputStream = new FileOutputStream(newFile, true);//�ļ�׷��д��
		
		byte[] byt = new byte[10*1024*1024];
		int len;
		FileInputStream temp = null;//��Ƭ�ļ�
		for(int i = 0 ; i<chunks ; i++){
			temp = new FileInputStream(new File(path+"/"+guid+"/"+i));
			while((len = temp.read(byt))!=-1){
				System.out.println(len);
				outputStream.write(byt, 0, len);
			}
		}
		/**
		 * ������׷��д�붼д��  �ſ��Թر���
		 */
		outputStream.close();
		temp.close();
		//׷��ɾ����ʱ��Ƭ�ļ�deleteFile(file);
		//���ļ���Ϣ�ӵ����ݿ���
		String str = fileName.substring(0, fileName.indexOf("."));
		bean.setName(str);
		//bean.setName(fileName);
		String sizefinal=getFilesize(newFile);
		bean.setSize(sizefinal);
		bean.setType(fileName.substring(fileName.lastIndexOf(".")));
		bean.setAddTime(sdf.format(new Date()));
		//bean.setFile_path(path+"//"+fileName);
		//bean.setFile_path("H://upload"+"//"+fileName);
		bean.setFile_path("/upload"+"/"+fileName);
		service.saveFile(bean);
		
		System.out.println("success!guid="+guid+";chunks="+chunks+";fileName="+fileName);
	
	}
	
	public String getFilesize(File file){
		if (file.exists() && file.isFile()) {
            //String fileName = file.getName();
            //System.out.println("�ļ�"+fileName+"�Ĵ�С�ǣ�"+file.length());
            long size = file.length();
            sizeStr = "";
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

		return sizeStr;
	}

}
