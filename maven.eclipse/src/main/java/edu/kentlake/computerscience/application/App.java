package edu.kentlake.computerscience.application;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import edu.kentlake.computerscience.database.DataStructure;
import edu.kentlake.computerscience.database.Database;
import edu.kentlake.computerscience.utilities.Utils;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * @author Ruvim Slyusar
 */
public class App extends Application {
	
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
	
	HashMap<DataStructure, String> formats = new HashMap<>();
	
	EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
			String id = event.getSource().toString();
			id = id.substring(id.indexOf('\'') + 1, id.length() - 1);
			
			try {
				text.setText(Utils.fileToString(new File("files/" + id)));
			}catch(IOException e) {
				e.printStackTrace();
			}
		}};
	
	public static void main(String[] args) {
		new Database(new File("files"));
		
		launch(args);
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
		Button[] files = createButtonArray(new File("configuration/filesInSourceFolder.txt"));	
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
					sub_new_folder.setOnAction(e -> {
						//Creates a new a folder inside of the sourceFolder
					});
					_new.getItems().add(sub_new_folder);
				// }
				_import = new MenuItem("Import");
				_import.setOnAction(e -> {
					Utils.addFileToList(new FileChooser().showOpenDialog(primaryStage));
				});
				_export = new MenuItem("Export");
				_export.setOnAction(e -> {
					//Exports the selected folder to somewhere
				});
			file.getItems().addAll(_new, _import, _export);
			// }
			add = new Menu("Add"); // {
				
				dataFormat = new MenuItem("dataFormat");
				dataFormat.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						
					}});
				
			add.getItems().addAll(dataFormat);
			// }
		menuBar.getMenus().addAll(file, add);
		// }
		
		root.getChildren().addAll(menuBar, scrollPanes);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public Button[] createButtonArray(File file) throws IOException {
		String[] fileNames = Utils.fileToString(file).split("\n");
		Button[] buttons = new Button[fileNames.length + 1];
		for(int i = 0;i < fileNames.length;i++) {
			buttons[i] = new Button(fileNames[i]);
			buttons[i].setOnAction(event);
		}
		try {
			buttons[buttons.length - 1] = new Button("combinedFiles.txt");
			buttons[buttons.length - 1].setOnAction(event);
		}catch(Exception e) {
		}
		return buttons;
	}
}
