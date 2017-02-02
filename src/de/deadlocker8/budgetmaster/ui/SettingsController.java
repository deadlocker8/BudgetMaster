package de.deadlocker8.budgetmaster.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class SettingsController
{
	@FXML private AnchorPane anchorPaneMain;
	@FXML private TextField textFieldSecret;
	@FXML private Label labelSecret;
	@FXML private Button buttonSave;

	private Controller controller;

	public void init(Controller controller)
	{
		this.controller = controller;
		anchorPaneMain.setStyle("-fx-background-color: #F4F4F4;");
		labelSecret.setStyle("-fx-text-fill: " + controller.getBundle().getString("color.text"));
		buttonSave.setStyle("-fx-background-color: #2E79B9; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16;");
		//TODO prefill with secret if savefile found
	}
	
	public void save()
	{
	
	}
}