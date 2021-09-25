package edu.kentlake.computerscience.application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import edu.kentlake.computerscience.database.DataStructure;
import edu.kentlake.computerscience.database.Database;
import edu.kentlake.computerscience.database.filestorage.FileDataType;
import edu.kentlake.computerscience.database.filestorage.FileObject;
import edu.kentlake.computerscience.utilities.Utils;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * @author Ruvim Slyusar
 * 
 * Ugly code
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
	MenuItem _remove;
	MenuItem _import;
	MenuItem _export;
	
	Menu add;
	MenuItem dataFormat;
	
	VBox userFilesHierarchy;
	
	static EventHandlerStorage eventHandler;
	EventHandler<MouseEvent> openFile;
	
	HashMap<String, DataStructure> formats = new HashMap<>();
	
	// Lol JavaFX calls this method... man it was doing everything twice for a while
	public void init() {
		database = new Database();
		initEvents();
	}
	
	public void initEvents() {
		eventHandler = new EventHandlerStorage();
		
		openFile = new EventHandler<>() {
			@Override
			public void handle(MouseEvent event) {
				final Node source = (Node) event.getSource();
				String id = source.getId();			
				text.setText(database.getFileData(id, FileDataType.PRETTY_FILE));	
			}};
		EventHandlerStorage.put(EventHandlerStorage.REMOVE, new EventHandler<ActionEvent>() {
			String id;		
			@Override
			public void handle(ActionEvent event) {
				try { id = ((Node) event.getSource()).getId(); }catch(Exception e) { id = ((MenuItem) event.getSource()).getId(); }
				
				Stage dialog = new Stage();
				VBox dialogVBox = new VBox(20);
				Text text = new Text("Enter file dir (EX: userMadeFolder/userMadeFile.txt)");
				TextField userTextField = new TextField();
				userTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
					@Override
					public void handle(KeyEvent keyEvent) { 
						if(keyEvent.getCode() == KeyCode.ENTER) {
							try {
								String dir = id + "/" + userTextField.getText();
								File file = new File(dir);
								file.delete();
								database.removeFileData(dir);
								refreshFileView();
							}catch(NullPointerException e) {
								System.err.println("File doesn't exist");
							}
							dialog.close();
						}}});
				dialogVBox.setAlignment(Pos.TOP_CENTER);
				VBox.setMargin(text, new Insets(40, 0, 0, 0));
				VBox.setMargin(userTextField, new Insets(20, 40, 100, 40));
				dialogVBox.getChildren().addAll(text, userTextField);
				
				dialog.setScene(new Scene(dialogVBox, 400, 200));
				dialog.show();
			}});		
//		eventHandler.put(EventHandlerStorage.OPEN_FILE, new EventHandler<ActionEvent>() {	// Not used but I would love it to go to this	
//			@Override																		// Don't got time tho... right now at least.
//			public void handle(ActionEvent event) {
//				final Node source = (Node) event.getSource();
//				String id = source.getId();			
//				text.setText(database.getFileData(id, FileDataType.PRETTY_FILE));		
//			}});
		EventHandlerStorage.put(EventHandlerStorage.ADD_FILE, new EventHandler<ActionEvent>() {
			String id;
			@Override
			public void handle(ActionEvent event) {
				try { id = ((Node) event.getSource()).getId(); }catch(Exception e) { id = ((MenuItem) event.getSource()).getId(); }
				
				Stage dialog = new Stage();
				VBox dialogVBox = new VBox(20);
				Text text = new Text("Create new File (EX: randomFile.txt)");
				TextField userTextField = new TextField();
				userTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
					@Override
					public void handle(KeyEvent keyEvent) { 
						if(keyEvent.getCode() == KeyCode.ENTER) {
							Utils.makeFile(new File(id + "/" + userTextField.getText()));
							refreshFileView();
							dialog.close();
						}}});
				dialogVBox.setAlignment(Pos.TOP_CENTER);
				VBox.setMargin(text, new Insets(40, 0, 0, 0));
				VBox.setMargin(userTextField, new Insets(20, 40, 100, 40));
				dialogVBox.getChildren().addAll(text, userTextField);
				
				dialog.setScene(new Scene(dialogVBox, 400, 200));
				dialog.show();
			}});
		EventHandlerStorage.put(EventHandlerStorage.MAKE_FOLDER, new EventHandler<ActionEvent>() {
			String id;
			@Override
			public void handle(ActionEvent event) {
				try { id = ((Node) event.getSource()).getId(); }catch(Exception e) { id = ((MenuItem) event.getSource()).getId(); }
				
				Stage dialog = new Stage();
				VBox dialogVBox = new VBox(20);
				Text text = new Text("Create new folder (EX: randomFolder)");
				TextField userTextField = new TextField();
				userTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
					@Override
					public void handle(KeyEvent keyEvent) { 
						if(keyEvent.getCode() == KeyCode.ENTER) {
							Utils.makeFolder(new File(id + "/" + userTextField.getText()));
							refreshFileView();
							dialog.close();
						}}});
				dialogVBox.setAlignment(Pos.TOP_CENTER);
				VBox.setMargin(text, new Insets(40, 0, 0, 0));
				VBox.setMargin(userTextField, new Insets(20, 40, 100, 40));
				dialogVBox.getChildren().addAll(text, userTextField);
				
				dialog.setScene(new Scene(dialogVBox, 400, 200));
				dialog.show();
			}});
		EventHandlerStorage.put(EventHandlerStorage.IMPORT, new EventHandler<ActionEvent>() {
			String id;
			@Override
			public void handle(ActionEvent event) {
				try { id = ((Node) event.getSource()).getId(); }catch(Exception e) { id = ((MenuItem) event.getSource()).getId(); }
				
				FileChooser fileChooser = new FileChooser();
				File file = fileChooser.showOpenDialog(new Stage());
				if(file != null) {
					Utils.addFile(file, id);
					refreshFileView();
			}}});
	}
	
	public VBox createButtonsArray(String userFilesDir) throws IOException {
		FileObject userFiles = new FileObject(userFilesDir);
		VBox rootPane = new VBox(2);
		for(FileObject child : userFiles.listFiles()) {
			rootPane.getChildren().add(createButtonHierarchy(child).getBtn());
		}
		return rootPane;
	}

	// MenuBar and Menu and MenuItems but with custom buttons because menu objects are only for menus
	public FileBtn createButtonHierarchy(FileObject currentParent) {
		Pane button = new Pane();
		button.setStyle("-fx-background-color: gray;");
		button.setPrefSize(197, 20);
		
		FileBtn mainBtn = new FileBtn();
		
		FileObject[] files = currentParent.listFiles();
		
		if(files.length == 0 && currentParent.isFile()) {
			Label fileName = createLabel(currentParent.getFileName(), "Assets/File.png");
			
			button.setId(currentParent.getDir());
			fileName.setId(button.getId());
			button.getChildren().add(fileName);
			
			mainBtn = new FileBtn(button, FileBtn.FILE);
			mainBtn.setEvent(openFile);
		}else if(files.length == 0 && currentParent.isDirectory()){
			Label folderName = createLabel(currentParent.getFileName(), "Assets/Folder.png");
			
			button.setId(currentParent.getDir());
			folderName.setId(button.getId());
			button.getChildren().add(folderName);
			
			mainBtn = new FileBtn(button, FileBtn.FOLDER);
		}else {
			if(currentParent.isDirectory()) {
				Label folderName = createLabel(currentParent.getFileName(), "Assets/Folder.png");
				
				button.setId(currentParent.getDir());
				folderName.setId(button.getId());
				button.getChildren().add(folderName);
				
				mainBtn = new FileBtn(button, FileBtn.FOLDER);
				for(FileObject child : files) {
					mainBtn.addSubBtn(createButtonHierarchy(child));
				}
			}
		}
		return mainBtn;
	}
	
	public Label createLabel(String labelName, String imgDir) {
//		System.out.println(imgDir); check if order if correct
		try {
			ImageView img = new ImageView(new Image(new FileInputStream(imgDir)));
			Label label = new Label(labelName, img);
			label.setPadding(new Insets(0, 0, 0, 10));
			return label;
		}catch(Exception e) {
			e.printStackTrace();
			return new Label("Error in Method \"createLabel\"");
		}
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
		userFilesHierarchy = createButtonsArray(Database.USER_FILES_DIR);	
		fileViewScroll.setContent(userFilesHierarchy);
		
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
					sub_new_folder.setId(Database.USER_FILES_DIR);
					sub_new_folder.setOnAction((EventHandler<ActionEvent>) eventHandler.get(EventHandlerStorage.MAKE_FOLDER));
					
					_new.getItems().add(sub_new_folder);
				// }
				_remove = new MenuItem("Remove");
				_remove.setId(Database.USER_FILES_DIR);
				_remove.setOnAction((EventHandler<ActionEvent>) eventHandler.get(EventHandlerStorage.REMOVE));
						
				_import = new MenuItem("Import");
				_import.setId(Database.USER_FILES_DIR);
				_import.setOnAction((EventHandler<ActionEvent>) eventHandler.get(EventHandlerStorage.IMPORT));
				
				_export = new MenuItem("Export");
//				_export.setOnAction();
			file.getItems().addAll(_new, _remove, _import, _export);
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
	
	public void refreshFileView() {
		database.check();
		
		try {
			userFilesHierarchy = createButtonsArray(Database.USER_FILES_DIR);
			fileViewScroll.setContent(userFilesHierarchy);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// EWWWWWWWWWWWWWWW but I need a prototype out
	public static void importEvent(ActionEvent e) {
		eventHandler.get(EventHandlerStorage.IMPORT).handle(e);
	}
	
	public static void addFileEvent(ActionEvent e) {
		eventHandler.get(EventHandlerStorage.ADD_FILE).handle(e);
	}
	
	public static void removeFileEvent(ActionEvent e) {
		eventHandler.get(EventHandlerStorage.REMOVE).handle(e);
	}
}
