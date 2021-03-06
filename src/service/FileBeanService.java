package service;

import java.util.List;

import dao.FileBeanDao;
import entity.FileBean;

public class FileBeanService {
	
	FileBeanDao dao = new FileBeanDao();
	
	public void saveFile(FileBean bean){
		dao.saveFile(bean);
	}
	
	public FileBean findById(int id){
		return dao.findById(id);
	}
	
	public List<FileBean> findAll(){
		return dao.findAll();
	}
	
	public FileBean findByName(String name){
		return dao.findByName(name);
	}
	public List<FileBean> findByType(String type){
		return dao.findByType(type);
	}
}
