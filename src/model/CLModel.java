package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Observable;

@SuppressWarnings("deprecation")
public class CLModel extends Observable {
	private LinkedHashMap<String, Boolean> list;

	/**
	 * Constructor
	 */
	public CLModel() {
		this.list = new LinkedHashMap<>();
	}

	public void addTask(String task, boolean status) {
		boolean result;
		if (this.list.containsKey(task)) {
			result = true;
		} else {
			result = false;
			this.list.put(task, status);
		}
		setChanged();
		notifyObservers(new CLMessage("add", task, result, status));
	}

	public void completeTask(String task) {
		this.list.replace(task, true);
	}

	public void removeTask(String task) {
		boolean result;
		if (this.list.containsKey(task)) {
			result = false;
			this.list.remove(task);
		} else {
			result = true;
		}
		setChanged();
		notifyObservers(new CLMessage("remove", task, result, false));
	}

	public void uncompleteTask(String task) {
		this.list.replace(task, false);
	}

	public void save(String file_name) {
		try {
			FileWriter writer = new FileWriter(file_name);
			for (Map.Entry<String, Boolean> entry : list.entrySet()) {
				writer.write(entry.getKey() + " -> " + entry.getValue() + "\n");
			}
			writer.close();
		} catch (IOException e) {
			return;
		}
	}

	public void load(String file_name) {
		File file = new File(file_name);
		this.list.clear();
		Scanner scanner;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException fnfe) {
			return;
		}
		while (scanner.hasNext()) {
			String next_line = scanner.nextLine();
			String task_name = next_line.split("->")[0].trim();
			boolean status = Boolean.valueOf(next_line.split("->")[1].trim());
			addTask(task_name, status);
		}
		scanner.close();
	}
	
	public int numberOfEntries() {
		return this.list.size();
	}
}
