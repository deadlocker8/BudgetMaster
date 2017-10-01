package de.deadlocker8.budgetmasterclient.utils;

import java.io.IOException;

import de.deadlocker8.budgetmaster.logic.utils.Helpers;
import de.deadlocker8.budgetmasterclient.ui.controller.ModalController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logger.Logger;
import tools.Localization;

public class UIHelpers
{
	public static Stage showModal(String title, String message, Stage owner, Image icon)
	{
		try
		{
			FXMLLoader fxmlLoader = new FXMLLoader(Helpers.class.getResource("/de/deadlocker8/budgetmaster/ui/fxml/Modal.fxml"));
			Parent root = (Parent)fxmlLoader.load();
			fxmlLoader.setResources(Localization.getBundle());
			Stage newStage = new Stage();
			newStage.initOwner(owner);
			newStage.initModality(Modality.APPLICATION_MODAL);
			newStage.setTitle(title);
			newStage.setScene(new Scene(root));
			newStage.getIcons().add(icon);
			newStage.setResizable(false);
			ModalController newController = fxmlLoader.getController();
			newController.init(newStage, message);
			newStage.show();

			return newStage;
		}
		catch(IOException e)
		{
			Logger.error(e);
			return null;
		}
	}
}