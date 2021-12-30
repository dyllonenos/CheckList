package model;

public class CLMessage {
	private String action;
	private String task;
	private boolean isError;
	private boolean status;
	
	public CLMessage(String action, String task, boolean error, boolean status) {
		this.action = action;
		this.isError = error;
		this.task = task;
		this.status = status;
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
	
	public boolean getStatus() {
		return this.status;
	}
}
