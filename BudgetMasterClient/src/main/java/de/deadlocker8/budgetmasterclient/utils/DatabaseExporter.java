package de.deadlocker8.budgetmasterclient.utils;

import java.io.File;

import de.deadlocker8.budgetmaster.logic.serverconnection.ExceptionHandler;
import de.deadlocker8.budgetmaster.logic.serverconnection.ServerConnection;
import de.deadlocker8.budgetmaster.logic.utils.FileHelper;
import de.deadlocker8.budgetmaster.logic.utils.Strings;
import de.deadlocker8.budgetmasterclient.ui.controller.Controller;
import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import logger.Logger;
import tools.AlertGenerator;
import tools.Localization;
import tools.Worker;

public class DatabaseExporter
{
	private Controller controller;
	
	public DatabaseExporter(Controller controller)
	{
		this.controller = controller;
	}
	
	public void exportDatabase()
	{
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(Localization.getString(Strings.TITLE_DATABASE_EXPORT));
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON (*.json)", "*.json");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showSaveDialog(controller.getStage());
		if(file != null)
		{
			LoadingModal.showModal(controller, Localization.getString(Strings.TITLE_MODAL), Localization.getString(Strings.LOAD_DATABASE_EXPORT), controller.getStage(), controller.getIcon());

			Worker.runLater(() -> {
				try
				{
					ServerConnection connection = new ServerConnection(controller.getSettings());
					String databaseJSON = connection.exportDatabase();
					FileHelper.saveDatabaseJSON(file, databaseJSON);

					Platform.runLater(() -> {
						LoadingModal.closeModal();
						AlertGenerator.showAlert(AlertType.INFORMATION, 
												Localization.getString(Strings.INFO_TITLE_DATABASE_EXPORT), 
												"", 
												Localization.getString(Strings.INFO_TEXT_DATABASE_EXPORT), 
												controller.getIcon(), 
												controller.getStage(), 
												null, 
												false);
					});
				}
				catch(Exception e)
				{
					Logger.error(e);
					Platform.runLater(() -> {
						LoadingModal.closeModal();
						controller.showConnectionErrorAlert(ExceptionHandler.getMessageForException(e));						
					});
				}
			});
		}

	}
}