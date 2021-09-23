package edu.kentlake.computerscience.application;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import edu.kentlake.computerscience.database.DataStructure;
import edu.kentlake.computerscience.database.Database;
import edu.kentlake.computerscience.database.filestorage.FileDataType;
import edu.kentlake.computerscience.utilities.Utils;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * @author Ruvim Slyusar
 */
public class App extends Application {
	
	Database database;
	
	//Application
	VBox root;
	SplitPane scrollPanes;
	Scene scene;
	ScrollPane fileViewScroll;
	ScrollPane fileDataViewScroll;
	Text text;
	
	//Menu Bar (this is hierarchy)
	MenuBar menuBar;
	Menu file;
	Menu _new;
	MenuItem sub_new_folder;
	MenuItem _import;
	MenuItem _export;
	
	Menu add;
	MenuItem dataFormat;
	
	EventHandlerStorage eventHandler;
	
	HashMap<String, DataStructure> formats = new HashMap<>();
		
	public App() {
		init();
	}
	
	public void init() {
		database = new Database();
		initEvents();
	}
	
	public void initEvents() {
		eventHandler = new EventHandlerStorage();
		
		eventHandler.put(EventHandlerStorage.OPEN_FILE, new EventHandler<ActionEvent>() {			
			@Override
			public void handle(ActionEvent event) {
				String id = event.getSource().toString();
				id = id.substring(id.indexOf('\'') + 1, id.length() - 1);				
				text.setText(database.getFileData(id, FileDataType.PRETTY_FILE));		
			}});
		eventHandler.put(EventHandlerStorage.MAKE_FOLDER, new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Stage dialog = new Stage();
				VBox dialogVBox = new VBox(20);
				Text text = new Text("Testing");
				TextField userTextField = new TextField();
				userTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
					@Override
					public void handle(KeyEvent keyEvent) { 
						if(keyEvent.getCode() == KeyCode.ENTER) {
							Utils.makeFolder(new File(Database.USER_FILES_DIR + "/" + userTextField.getText()));
							dialog.close();
						}}});
				
				dialogVBox.setAlignment(Pos.TOP_CENTER);
				VBox.setMargin(text, new Insets(40, 0, 0, 0));
				VBox.setMargin(userTextField, new Insets(20, 40, 100, 40));
				dialogVBox.getChildren().addAll(text, userTextField);
				
				dialog.setScene(new Scene(dialogVBox, 400, 200));
				dialog.show();
			}});
		eventHandler.put("Import", new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Utils.addFileToList(new FileChooser().showOpenDialog(new Stage()));
			}});
	}
	
	public Button[] createButtonArray(File file) throws IOException {
		String[] fileNames = Utils.fileToString(file).split("\n");
		Button[] buttons = new Button[fileNames.length + 1];
		for(int i = 0;i < fileNames.length;i++) {
			buttons[i] = new Button(fileNames[i]);
			buttons[i].setOnAction(eventHandler.get(EventHandlerStorage.OPEN_FILE));
		}
		try {
			buttons[buttons.length - 1] = new Button("combinedFiles.txt");
			buttons[buttons.length - 1].setOnAction(eventHandler.get(EventHandlerStorage.OPEN_FILE));
		}catch(Exception e) {
		}
		return buttons;
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		//Root pane and scene
		root = new VBox();
		scene = new Scene(root, 1000, 500);
		
		//The scrollPane where you can see inside of your file
		fileDataViewScroll = new ScrollPane();
		text = new Text();	
		fileDataViewScroll.setContent(text);
		
		//ScrollPane to where you'll see all your files
		fileViewScroll = new ScrollPane();
		VBox box = new VBox();
		Button[] files = createButtonArray(new File(Database.CONFIG_DIR + "/filesInUserFiles.txt"));	
		box.getChildren().addAll(files);
		fileViewScroll.setContent(box);
		
		//The splitPane that holds the two scrollPanes
		scrollPanes = new SplitPane();
		scrollPanes.setDividerPosition(0, 0.2);
		scrollPanes.prefHeightProperty().bind(scene.heightProperty());
		scrollPanes.getItems().addAll(fileViewScroll, fileDataViewScroll);
		
		//Creates menuBar with all the MenuItems and subMenus
		menuBar = new MenuBar(); // {
			file = new Menu("File"); // {
				
				_new = new Menu("New"); // {
					sub_new_folder = new MenuItem("Folder");
					sub_new_folder.setOnAction(eventHandler.get(EventHandlerStorage.MAKE_FOLDER));
					
					_new.getItems().add(sub_new_folder);
				// }
				_import = new MenuItem("Import");
				_import.setOnAction(eventHandler.get(EventHandlerStorage.IMPORT));
				
				_export = new MenuItem("Export");
//				_export.setOnAction();
			file.getItems().addAll(_new, _import, _export);
			// }
			add = new Menu("Add"); // {
				
				dataFormat = new MenuItem("dataFormat");
//				dataFormat.setOnAction();
				
			add.getItems().addAll(dataFormat);
			// }
		menuBar.getMenus().addAll(file, add);
		// }
		
		root.getChildren().addAll(menuBar, scrollPanes);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
