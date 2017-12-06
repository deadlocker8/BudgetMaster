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

public class LoadingModal
{
	private static Stage modalStage;
	private static ModalController modalController;
	
	public static void showModal(String title, String message, Stage owner, Image icon)
	{
		closeModal();
		modalStage = createModal(title, message, owner, icon);
	}
	
	public static void setMessage(String message)
	{
		if(modalController != null)
		{
			modalController.setMessage(message);
		}
	}
	
	public static void closeModal()
	{
		if(modalStage != null)
		{
			modalStage.close();
			modalStage = null;
			modalController = null;
		}
	}
	
	public static boolean isShowing()
	{
		return modalStage != null && modalStage.isShowing();
	}
	
	private static Stage createModal(String title, String message, Stage owner, Image icon)
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
			newStage.setAlwaysOnTop(true);
			modalController = fxmlLoader.getController();
			modalController.init(newStage, message);
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
