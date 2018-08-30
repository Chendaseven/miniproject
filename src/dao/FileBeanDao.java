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
	 * 保存文件信息的方法
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
	 * 根据id查询文件
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
	 * 根据name查询文件
	 */
	public FileBean findByName(String name){
		try {
			QueryRunner qr = new QueryRunner(JdbcUtil.getDataSource());
			return (FileBean)qr.query("select * from file_list where name=?", 
									new BeanHandler(FileBean.class), 
									new Object[]{name});
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * 查询所有文件
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
		bean.setInfo("描述");
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
