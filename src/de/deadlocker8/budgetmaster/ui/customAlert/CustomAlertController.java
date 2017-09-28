package de.deadlocker8.budgetmaster.ui.customAlert;

import de.deadlocker8.budgetmaster.ui.controller.BaseController;
import de.deadlocker8.budgetmaster.ui.controller.SplashScreenController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tools.Localization;

public class CustomAlertController extends BaseController
{
	@FXML private AnchorPane anchorPaneMain;
	@FXML private ImageView imageView;
	@FXML private Label labelMessage;

	private Stage parentStage;
	private SplashScreenController controller;
	private AlertType alertType;
	private String title;
	private String message;

	public CustomAlertController(Stage parentStage, SplashScreenController controller, AlertType alertType, String title, String message)
	{
		this.parentStage = parentStage;
		this.controller = controller;
		this.alertType = alertType;
		this.title = title;
		this.message = message;
		load("/de/deadlocker8/budgetmaster/ui/customAlert/CustomAlert.fxml", Localization.getBundle());
		getStage().showAndWait();
	}

	@Override
	public void initStage(Stage stage)
	{
		stage.initOwner(parentStage);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle(title);
		stage.getIcons().add(controller.getIcon());
		stage.setResizable(false);
	}

	@Override
	public void init()
	{
		labelMessage.setText(message);		
		
		getStage().getScene().setOnKeyReleased((event)->{			
			event.consume();
			if(event.getCode().equals(KeyCode.ENTER))
			{
				confirm();
			}			
		});

		switch(alertType)
		{
			case CONFIRMATION:
				imageView.setImage(new Image("/com/sun/javafx/scene/control/skin/modena/dialog-confirmation.png"));
				break;
			case ERROR:
				imageView.setImage(new Image("/com/sun/javafx/scene/control/skin/modena/dialog-error.png"));
				break;
			case INFORMATION:
				imageView.setImage(new Image("/com/sun/javafx/scene/control/skin/modena/dialog-information.png"));
				break;
			case WARNING:
				imageView.setImage(new Image("/com/sun/javafx/scene/control/skin/modena/dialog-warning.png"));
				break;
			default:
				imageView.setImage(new Image("/com/sun/javafx/scene/control/skin/modena/dialog-information.png"));
				break;
		}
	}

	@FXML
	public void confirm()
	{
		getStage().close();
	}
}