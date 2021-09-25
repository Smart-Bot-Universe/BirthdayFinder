package program.application;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import program.data.FindBirthdays.TodaysBirthday;
import program.data.JsonToClassroom;
import program.utils.Utils;

public class App extends Application {
	public static final String FILE_FOR_USE = "src/main/resources/json/dataForBirthdays";
	
	BorderPane root;
	Scene scene;	
	List<TodaysBirthday> themBirthdays;
	
	MenuBar menuBar;
	Menu file;
	MenuItem _import;
	
	String jsonString;
	JsonToClassroom jtc;
	EventHandler<ActionEvent> imp;
	
	public void init() {
		jtc = new JsonToClassroom();
		try {
			jsonString = Utils.fileToString(new File(App.FILE_FOR_USE));
			jtc.start(jsonString);
			themBirthdays = jtc.getTodaysBirthdays();
			createBirthdayFrameAndFill();
		}catch(Exception e) {
		}
		
		imp = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				File json = fileChooser.showOpenDialog(new Stage());
				if(json != null) {
					Utils.delete(new File(App.FILE_FOR_USE));
					Utils.addFile(json, App.FILE_FOR_USE);
					try {
						jsonString = Utils.fileToString(json);
						jtc.start(jsonString);
						themBirthdays = jtc.getTodaysBirthdays();
						createBirthdayFrameAndFill();
					} catch (IOException e) { e.printStackTrace(); }
				}
			}};			
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		root = new BorderPane();
		scene = new Scene(root, 600, 400);
		root.setPrefSize(600, 400);
		
		menuBar = new MenuBar();
		file = new Menu("File");
		_import = new MenuItem("Import");
		_import.setOnAction(imp);
		file.getItems().add(_import);
		menuBar.getMenus().add(file);
		
		createBirthdayFrameAndFill();
		
		root.setTop(menuBar);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void createBirthdayFrameAndFill() {
		GridPane pane = new GridPane();
		pane.setMinSize(600, 300);
		
		for(int period = 1;period <= 6;period++) {
			VBox classroom = new VBox(5);
//			classroom.setId(String.valueOf(period));
			Label periodNum = new Label("Period: " + period);
			periodNum.setFont(new Font(25));
			periodNum.setPadding(new Insets(0, 50, 50, 50));
			VBox.setVgrow(periodNum, Priority.ALWAYS);
			classroom.getChildren().add(periodNum);
			if(period > 3) pane.add(classroom, period - 4, 1);
			else pane.add(classroom, period - 1, 0);
		}
		if(themBirthdays == null) return;
		for(int i = 0;i < themBirthdays.size();i++) {
			TodaysBirthday student = themBirthdays.get(i);
			Label studentName = new Label(student.getBirthdayStudent().getLastName() + ", " + student.getBirthdayStudent().getFirst_MI());
			studentName.setPadding(new Insets(5, 0, 5, 25));
			((VBox)pane.getChildren().get(student.getStudentsPeriodNum() - 1)).getChildren().add(studentName);
		}
		BorderPane.setAlignment(pane, Pos.CENTER);
		pane.setAlignment(Pos.CENTER);
		root.setCenter(pane);
	}
}
