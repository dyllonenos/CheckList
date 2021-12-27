package controller;

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
}
