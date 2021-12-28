package controller;

import java.util.HashMap;

import model.CLModel;

public class CLController {
	
	private CLModel model;
	
	public CLController(CLModel model) {
		this.model = model;
	}
	
	public void addTask(String task) {
		this.model.addTask(task);
	}
	
	public void completeTask(String task) {
		this.model.completeTask(task);
	}
	
	public void removeTask(String task) {
		this.model.removeTask(task);
	}
	
	public void uncompleteTask(String task) {
		this.model.uncompleteTask(task);
	}
	
	public void save(String file_name, HashMap<String, Boolean> progress) {
		this.model.save(file_name, progress);
	}
	
	public HashMap<String, Boolean> load(String file_name) {
		return this.model.load(file_name);
	}
}
