package de.deadlocker8.budgetmaster.ui;

import java.util.Locale;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

public class ChartController
{	
	@FXML private AnchorPane anchorPaneMain;

	private Controller controller;
	private Image icon = new Image("de/deadlocker8/budgetmaster/resources/icon.png");
	private final ResourceBundle bundle = ResourceBundle.getBundle("de/deadlocker8/budgetmaster/main/", Locale.GERMANY);

	public void init(Controller controller)
	{
		this.controller = controller;
		anchorPaneMain.setStyle("-fx-background-color: #F4F4F4;");						
	}
}