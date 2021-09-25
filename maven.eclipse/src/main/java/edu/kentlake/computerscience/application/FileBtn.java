package edu.kentlake.computerscience.application;

import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class FileBtn extends BtnObject {
	public static final byte FOLDER = 0;
	public static final byte FILE = 1;

	private byte fileType;
	
	private ContextMenu contextMenu;
	
	public String text = "";
	
	EventHandler<MouseEvent> openFileBtn = new EventHandler<>() {
		@Override
		public void handle(MouseEvent event) {
			switch(fileType) {
			case FileBtn.FOLDER:
				if(event.getButton() == MouseButton.PRIMARY) {
					getCurrentEvent().handle(event);
				}else if(event.getButton() == MouseButton.SECONDARY) {
					
				}
			case FileBtn.FILE:
			}
		}};
		
	private EventHandler<MouseEvent> curEvent = openFileBtn;
		
	public FileBtn() {
//		this(new Pane(new Label("Empty")));
	}
	
	public FileBtn(Pane btn) {
		this(btn, FileBtn.FILE);
	}
	
	public FileBtn(Pane btn, byte fileType) {
		this.btn = btn;
		this.btn.setOnMouseClicked(curEvent);
		openedBtn = new VBox(2);
		openedBtn.getChildren().add(btn);
		subBtns = new ArrayList<>();
		this.fileType = fileType;
		
		if(fileType == FileBtn.FOLDER) {
			contextMenu = createContextMenu();
		}
		((Label) btn.getChildren().get(0)).setContextMenu(contextMenu);;
	}
	
	@Override
	public void setEvent(EventHandler<MouseEvent> event) {
		this.curEvent = event;
		btn.setOnMouseClicked(curEvent);
	}
	
	public void setType(byte fileType) {
		this.fileType = fileType;
	}
	
	public ContextMenu createContextMenu() {
		ContextMenu cm = new ContextMenu();
		
		MenuItem _import = new MenuItem("Import");
		_import.setId(btn.getId());
		_import.setOnAction(e -> App.importEvent(e));
		MenuItem _add = new MenuItem("Add");
		_add.setId(btn.getId());
		_add.setOnAction(e -> App.addFileEvent(e));
		
		MenuItem _remove = new MenuItem("Remove");
		_remove.setId(btn.getId());
		_remove.setOnAction(e -> App.removeFileEvent(e));
			
		cm.getItems().addAll(_import, _add, _remove);
		return cm;
	}
}
