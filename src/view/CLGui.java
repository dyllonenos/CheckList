package view;

import javafx.application.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.CLMessage;
import model.CLModel;

import java.util.Observable;
import java.util.Observer;

import controller.CLController;

@SuppressWarnings("deprecation")
public class CLGui extends Application implements Observer {

	private BorderPane main_pane = new BorderPane();
	private GridPane list_pane = new GridPane();
	private CLModel model = new CLModel();
	private CLController controller = new CLController(model);
	private Stage main_stage;
	private ScrollPane scroll_pane = new ScrollPane();

	@Override
	public void start(Stage stage) throws Exception {
		model.addObserver(this);
		makeGui();
		main_stage = stage;
		Scene scene = new Scene(main_pane, 300,  300);
		main_stage.setTitle("Check List");
		main_stage.setScene(scene);
		main_stage.show();

	}

	/**
	 * 
	 */
	private void makeGui() {
		scroll_pane.setContent(list_pane);
		main_pane.setCenter(scroll_pane);
		
		Button newTaskButton = setAddButton();
		Button saveButton = setSaveButton();
		Button loadButton = setLoadButton();
		Button removeTaskButton = setRemoveButton();
		
		HBox menue_bar = new HBox();
		menue_bar.getChildren().add(newTaskButton);
		menue_bar.getChildren().add(removeTaskButton);
		menue_bar.getChildren().add(saveButton);
		menue_bar.getChildren().add(loadButton);
		main_pane.setTop(menue_bar);
	}

	/**
	 * @return
	 */
	private Button setAddButton() {
		Button newTaskButton = new Button("New Task");
		newTaskButton.setOnAction((event) -> {
			Stage anotherStage = new Stage();
			BorderPane pane = new BorderPane();
			TextField txt_fld = new TextField();
			txt_fld.setPromptText("Enter task");
			txt_fld.setFocusTraversable(false);
			txt_fld.setOnKeyPressed((event2) -> {
				if (event2.getCode() == KeyCode.ENTER) {
					String input_task = txt_fld.getText();
					controller.addTask(input_task, false);
					anotherStage.close();
				}
			});
			pane.setCenter(txt_fld);
			anotherStage.setTitle("Add Task");
			anotherStage.setScene(new Scene(pane, 300, 100));
			anotherStage.show();
		});
		return newTaskButton;
	}

	/**
	 * @return
	 */
	private Button setRemoveButton() {
		Button removeTaskButton = new Button("Remove");
		removeTaskButton.setOnAction((event)->{
			Stage anotherStage = new Stage();
			BorderPane pane = new BorderPane();
			TextField txt_fld = new TextField();
			txt_fld.setPromptText("Enter task to remove");
			txt_fld.setFocusTraversable(false);
			txt_fld.setOnKeyPressed((event2) -> {
				if (event2.getCode() == KeyCode.ENTER) {
					String input = txt_fld.getText();
					controller.removeTask(input);
					anotherStage.close();
				}
			});
			pane.setTop(new Label("Enter the name of task to remove"));
			pane.setCenter(txt_fld);
			anotherStage.setTitle("Remove Task");
			anotherStage.setScene(new Scene(pane, 300, 100));
			anotherStage.show();
		});
		return removeTaskButton;
	}

	/**
	 * @return
	 */
	private Button setSaveButton() {
		Button saveButton = new Button("Save");
		saveButton.setOnAction((event) -> {
			Stage anotherStage = new Stage();
			BorderPane pane = new BorderPane();
			TextField txt_fld = new TextField();
			txt_fld.setPromptText("Enter file name to save");
			txt_fld.setFocusTraversable(false);
			txt_fld.setOnKeyPressed((event2) -> {
				if (event2.getCode() == KeyCode.ENTER) {
					String input = txt_fld.getText() + ".txt";
					controller.save(input);
					anotherStage.close();
				}
			});
			pane.setTop(new Label("Enter the name of your save"));
			pane.setCenter(txt_fld);
			anotherStage.setTitle("Enter File Name To Save");
			anotherStage.setScene(new Scene(pane, 300, 100));
			anotherStage.show();
		});
		return saveButton;
	}

	/**
	 * @return
	 */
	private Button setLoadButton() {
		Button loadButton = new Button("Load");
		loadButton.setOnAction((event) -> {
			Stage anotherStage = new Stage();
			BorderPane pane = new BorderPane();
			TextField txt_fld = new TextField();
			txt_fld.setPromptText("Enter file name to load");
			txt_fld.setFocusTraversable(false);
			txt_fld.setOnKeyPressed((event2) -> {
				if (event2.getCode() == KeyCode.ENTER) {
					String input = txt_fld.getText() + ".txt";
					this.list_pane.getChildren().clear();
					this.controller.load(input);
					anotherStage.close();
				}
			});
			pane.setTop(new Label("Enter the name of your load"));
			pane.setCenter(txt_fld);
			anotherStage.setTitle("Enter File Name To Load");
			anotherStage.setScene(new Scene(pane, 300, 100));
			anotherStage.show();
		});
		return loadButton;
	}

	@Override
	public void update(Observable o, Object arg) {
		CLMessage message = (CLMessage) arg;
		String action = message.getAction();
		String task_name = message.getTask();
		boolean isError = message.getErrorStatus();
		boolean status = message.getStatus();
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
				checkBox.setSelected(status);
				checkBox.setPadding(new Insets(2));
				checkBox.setOnAction((event) -> {
					if (checkBox.isSelected()) {
						controller.completeTask(task_name);
					} else {
						controller.uncompleteTask(task_name);
					}
				});
				Label label = new Label(task_name);
				hbox.getChildren().add(checkBox);
				hbox.getChildren().add(label);
				hbox.setPadding(new Insets(5));
				this.list_pane.add(hbox, 0, row_count + 1);
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
