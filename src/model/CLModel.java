package model;
import java.util.*;
import java.util.Observable;

@SuppressWarnings("deprecation")
public class CLModel extends Observable {
	private HashMap<String, Boolean> list;
	
	/**
	 * Constructor
	 */
	public CLModel() {
		this.list = new HashMap<>();
	}
	
	public void addTask(String task) {
		boolean result;
		if (this.list.containsKey(task)) {
			result = true;
		}
		else {
			result = false;
			this.list.put(task, false);
		}
		setChanged();
		notifyObservers(new CLMessage("add", task, result));
	}
	
	public void completeTask(String task) {
		this.list.replace(task, true);
	}
	
	public void removeTask(String task) {
		boolean result;
		if (this.list.containsKey(task)) {
			result = false;
			this.list.remove(task);
		}
		else {
			result = true;
		}
		setChanged();
		notifyObservers(new CLMessage("remove", task, result));
	}
	
	public void uncompleteTask(String task) {
		this.list.replace(task, false);
	}
}
