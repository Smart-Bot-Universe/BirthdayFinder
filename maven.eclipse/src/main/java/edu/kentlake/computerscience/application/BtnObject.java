package edu.kentlake.computerscience.application;

import java.util.ArrayList;
import java.util.List;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BtnObject {
	protected Pane btn;
	protected VBox openedBtn;
	protected boolean btnOpen = false;
	protected List<FileBtn> subBtns;
	
	public String text = "";
	
	EventHandler<MouseEvent> openBtn = new EventHandler<>() {
		@Override
		public void handle(MouseEvent event) {
			if(!btnOpen) {
				for(FileBtn btn : subBtns) {
					openedBtn.getChildren().add(btn.getBtn());
				}
			}else {
				openedBtn.getChildren().removeAll(openedBtn.getChildren());
				openedBtn.getChildren().add(btn);
			}
			btnOpen = !btnOpen;
		}};
	
	private EventHandler<MouseEvent> currentEvent = openBtn;
		
	public BtnObject() {
//		this(new Pane(new Label("Empty")));
	}
	
	public BtnObject(Pane btn) {
		this.btn = btn;
		this.btn.setOnMouseClicked(currentEvent);
		openedBtn = new VBox(2);
		openedBtn.getChildren().add(btn);
		subBtns = new ArrayList<>();
	}
	
	public Pane getPaneBtn() {
		return btn;
	}
	
	public void setPaneBtn(Pane btn) {
		openedBtn.getChildren().remove(this.btn);
		this.btn = btn;
		openedBtn.getChildren().add(this.btn);
	}

	public VBox getBtn() {
		return openedBtn;
	}
	
	public void addSubBtn(FileBtn subBtn) {
		subBtns.add(subBtn);
	}
	
	public FileBtn getSubBtn(int index) {
		return subBtns.get(index);
	}
	
	public EventHandler<MouseEvent> getCurrentEvent(){
		return currentEvent;
	}
	
	public void setEvent(EventHandler<MouseEvent> event) {
		this.currentEvent = event;
		btn.setOnMouseClicked(event);
	}
}