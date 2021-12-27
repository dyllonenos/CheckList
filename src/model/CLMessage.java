package model;

public class CLMessage {
	private String action;
	private String task;
	private boolean isError;
	
	public CLMessage(String action, String task, boolean error) {
		this.action = action;
		this.isError = error;
		this.task = task;
	}
	
	public String getAction() {
		return this.action;
	}
	
	public boolean getErrorStatus() {
		return this.isError;
	}
	
	public String getTask() {
		return this.task;
	}
}
