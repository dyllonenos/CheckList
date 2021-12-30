package controller;
import model.CLModel;

public class CLController {
	
	private CLModel model;
	
	public CLController(CLModel model) {
		this.model = model;
	}
	
	public void addTask(String task, boolean status) {
		this.model.addTask(task, status);
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
	
	public void save(String file_name) {
		this.model.save(file_name);
	}
	
	public void load(String file_name) {
		this.model.load(file_name);
	}
	
	public int numberOfEntries() {
		return this.model.numberOfEntries();
	}
}
