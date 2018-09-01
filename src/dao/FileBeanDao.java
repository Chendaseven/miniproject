package dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import entity.FileBean;
import util.JdbcUtil;

public class FileBeanDao {

	/**
	 * �����ļ���Ϣ�ķ���
	 */
	public void saveFile(FileBean bean){
		try {
			QueryRunner qr = new QueryRunner(JdbcUtil.getDataSource());
			qr.update("insert into file_list(name,size,type,addtime,file_path,info) values(?,?,?,?,?,?)",
					new Object[]{
					bean.getName(),
					bean.getSize(),
					bean.getType(),
					bean.getAddTime(),
					bean.getFile_path(),
					bean.getInfo()
			});
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * ����id��ѯ�ļ�
	 */
	public FileBean findById(int id){
		try {
			QueryRunner qr = new QueryRunner(JdbcUtil.getDataSource());
			return (FileBean)qr.query("select * from file_list where id=?", 
									new BeanHandler(FileBean.class), 
									new Object[]{id});
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * ����name��ѯ�ļ�
	 */
	public FileBean findByName(String name){
		try {
			QueryRunner qr = new QueryRunner(JdbcUtil.getDataSource());
			return (FileBean)qr.query("select * from file_list where name like ?", 
									new BeanHandler(FileBean.class), 
									new Object[]{name});
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	
	//�����ļ����Ͳ�ѯ�ļ�
	public List<FileBean> findByType(String type){
		try {
			QueryRunner qr = new QueryRunner(JdbcUtil.getDataSource());
			return (List<FileBean>)qr.query("select * from file_list where type like ?", 
									new BeanListHandler(FileBean.class), 
									new Object[]{type});
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	
	
	/**
	 * ��ѯ�����ļ�
	 * @param args
	 */
	public List<FileBean> findAll(){
		try {
			QueryRunner qr = new QueryRunner(JdbcUtil.getDataSource());
			return (List<FileBean>)qr.query("select * from file_list", 
									new BeanListHandler(FileBean.class), 
									new Object[]{});
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	
	
	public static void main(String[] args) {
		FileBeanDao dao = new FileBeanDao();
		/*FileBean bean = new FileBean();
		bean.setInfo("����");
		bean.setName("aaaaa.jpg");
		bean.setSize("23KB");
		bean.setType("image/jpg");
		bean.setAddTime("2015-06-30 17:23:32");
		bean.setFile_path("/upload/10/3/aaaaa.jpg");
		dao.saveFile(bean);*/
		FileBean bean = dao.findById(1);
		System.out.println(bean);
	}
}
