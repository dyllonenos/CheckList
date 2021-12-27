package view;
import javafx.application.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.CLMessage;
import model.CLModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import controller.CLController;

@SuppressWarnings("deprecation")
public class CLGui extends Application implements Observer {
	
	private BorderPane main_pane = new BorderPane();
	private GridPane list_pane = new GridPane();
	private CLModel model = new CLModel();
	private CLController controller = new CLController(model);

	@Override
	public void start(Stage stage) throws Exception {
		model.addObserver(this);
		makeGui();
		Scene scene = new Scene(main_pane, 300, 300);
		stage.setTitle("Check List");
		stage.setScene(scene);
		stage.show();
		
	}

	/**
	 * 
	 */
	private void makeGui() {
		main_pane.setCenter(list_pane);
		Button newTaskButton = new Button("New Task");
		newTaskButton.setOnAction((event)->{
			Stage anotherStage = new Stage();
			BorderPane pane = new BorderPane();
			TextField txt_fld = new TextField();
			txt_fld.setPromptText("Enter task");
			txt_fld.setFocusTraversable(false);
			txt_fld.setOnKeyPressed((event2)->{
				if (event2.getCode() == KeyCode.ENTER) {
					String input_task = txt_fld.getText();
					controller.addTask(input_task);
					anotherStage.close();
				}
			});
			pane.setCenter(txt_fld);
			anotherStage.setTitle("Add Task");
			anotherStage.setScene(new Scene(pane, 300, 100));
			anotherStage.show();
		});
		Button saveButton = new Button("Save");
		saveButton.setOnAction((event)->{
			Stage anotherStage = new Stage();
			BorderPane pane = new BorderPane();
			TextField txt_fld = new TextField();
			txt_fld.setPromptText("Enter file name to save");
			txt_fld.setFocusTraversable(false);
			txt_fld.setOnKeyPressed((event2)->{
				if (event2.getCode() == KeyCode.ENTER) {
					String input_task = txt_fld.getText();
					try {
						FileWriter writer = new FileWriter(input_task + ".txt");
						for (Object curr_obj : this.list_pane.getChildren()) {
							HBox curr_hbox = (HBox) curr_obj;
							CheckBox curr_checkBox = (CheckBox) curr_hbox.getChildren().get(0);
							Label curr_label = (Label) curr_hbox.getChildren().get(1);
							if (curr_checkBox.isSelected()) {
								writer.write(curr_label.getText() + " -> true\n");
							}
							else {
								writer.write(curr_label.getText() + " -> false\n");
							}
						}
						writer.close();
					} catch (IOException e) {
					}
					anotherStage.close();
				}
			});
			pane.setCenter(txt_fld);
			anotherStage.setTitle("Enter File Name To Save");
			anotherStage.setScene(new Scene(pane, 300, 100));
			anotherStage.show();
		});
		
		Button loadButton = new Button("Load");
		loadButton.setOnAction((event)->{
			Stage anotherStage = new Stage();
			BorderPane pane = new BorderPane();
			TextField txt_fld = new TextField();
			txt_fld.setPromptText("Enter file name to load");
			txt_fld.setFocusTraversable(false);
			txt_fld.setOnKeyPressed((event2)->{
				if (event2.getCode() == KeyCode.ENTER) {
					String input_task = txt_fld.getText();
					File file = new File(input_task + ".txt");
					try {
						Scanner scanner = new Scanner(file);
						this.list_pane.getChildren().clear();
						while (scanner.hasNext()) {
							String curr_line = scanner.nextLine();
							String task = curr_line.split("->")[0].trim();
							boolean status = Boolean.valueOf(curr_line.split("->")[1].trim());
							int row_count = this.list_pane.getRowCount();
							
							HBox hbox = new HBox();
							CheckBox checkBox = new CheckBox();
							checkBox.setSelected(status);
							checkBox.setPadding(new Insets(2));
							checkBox.setOnAction((event3)->{
								if (checkBox.isSelected()) {
									controller.completeTask(task);
								}
								else {
									controller.uncompleteTask(task);
								}
							});
							Label label = new Label(task);
							hbox.getChildren().add(checkBox);
							hbox.getChildren().add(label);
							hbox.setPadding(new Insets(5));
							this.list_pane.add(hbox, 0, row_count+1);
						}
						scanner.close();
					} catch (FileNotFoundException e) {
					}
					anotherStage.close();
				}
			});
			pane.setCenter(txt_fld);
			anotherStage.setTitle("Enter File Name To Load");
			anotherStage.setScene(new Scene(pane, 300, 100));
			anotherStage.show();
		});
		HBox menue_bar = new HBox();
		menue_bar.getChildren().add(newTaskButton);
		menue_bar.getChildren().add(saveButton);
		menue_bar.getChildren().add(loadButton);
		main_pane.setTop(menue_bar);
	}

	@Override
	public void update(Observable o, Object arg) {
		CLMessage message = (CLMessage) arg;
		String action = message.getAction();
		String task_name = message.getTask();
		boolean isError = message.getErrorStatus();
		int row_count = this.list_pane.getRowCount();
		
		// ERROR Check
		if (isError == true) {
			errorHandle(action);
		}
		
		// NO ERROR
		else {
			if (action.equals("add")) {
				HBox hbox = new HBox();
				CheckBox checkBox = new CheckBox();
				checkBox.setPadding(new Insets(2));
				checkBox.setOnAction((event)->{
					if (checkBox.isSelected()) {
						controller.completeTask(task_name);
					}
					else {
						controller.uncompleteTask(task_name);
					}
				});
				Label label = new Label(task_name);
				hbox.getChildren().add(checkBox);
				hbox.getChildren().add(label);
				hbox.setPadding(new Insets(5));
				this.list_pane.add(hbox, 0, row_count+1);
			}
			
			else if (action.equals("remove")) {
				for (Object curr_obj : this.list_pane.getChildren()) {
					HBox curr_hbox = (HBox) curr_obj;
					Label curr_label = (Label) curr_hbox.getChildren().get(1);
					if (curr_label.getText().equals(task_name)) {
						this.list_pane.getChildren().remove(curr_obj);
						break;
					}
				}
			}
		}
		
		
	}

	/**
	 * @param action
	 */
	private void errorHandle(String action) {
		if (action.equals("add")) {
			// ALERT
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("ADD ERROR");
			alert.setHeaderText("You have made an error");
			alert.setContentText("You can not add that task because it already exists!");
			alert.show();
		}
		
		else if (action.equals("remove")) {
			// ALERT
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("You have made an error");
			alert.setContentText("That task does not exist!");
			alert.setTitle("REMOVE ERROR");
			alert.show();
		}
	}

}
