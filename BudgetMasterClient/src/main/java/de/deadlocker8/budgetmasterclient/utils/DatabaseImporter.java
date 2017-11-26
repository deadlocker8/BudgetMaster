package de.deadlocker8.budgetmasterclient.utils;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import de.deadlocker8.budgetmaster.logic.database.Database;
import de.deadlocker8.budgetmaster.logic.serverconnection.ExceptionHandler;
import de.deadlocker8.budgetmaster.logic.serverconnection.ServerConnection;
import de.deadlocker8.budgetmaster.logic.utils.FileHelper;
import de.deadlocker8.budgetmaster.logic.utils.Strings;
import de.deadlocker8.budgetmasterclient.ui.controller.Controller;
import de.deadlocker8.budgetmasterclient.utils.UIHelpers;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logger.Logger;
import tools.AlertGenerator;
import tools.Localization;
import tools.Worker;

public class DatabaseImporter
{
	private Controller controller;
	
	public DatabaseImporter(Controller controller)
	{
		this.controller = controller;
	}
	
	public void importDatabase() 
	{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(Localization.getString(Strings.INFO_TITLE_DATABASE_IMPORT_DIALOG));
		alert.setHeaderText("");		
		alert.setContentText(Localization.getString(Strings.INFO_TEXT_DATABASE_IMPORT_DIALOG));
		Stage dialogStage = (Stage)alert.getDialogPane().getScene().getWindow();
		dialogStage.getIcons().add(controller.getIcon());
		dialogStage.initOwner(controller.getStage());

		ButtonType buttonTypeDelete = new ButtonType(Localization.getString(Strings.INFO_TEXT_DATABASE_IMPORT_DIALOG_DELETE));
		ButtonType buttonTypeAppend = new ButtonType(Localization.getString(Strings.INFO_TEXT_DATABASE_IMPORT_DIALOG_APPEND));
		ButtonType buttonTypeCancel = new ButtonType(Localization.getString(Strings.CANCEL), ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(buttonTypeDelete, buttonTypeAppend, buttonTypeCancel);
		
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getButtonTypes().stream().map(dialogPane::lookupButton).forEach(button -> button.addEventHandler(KeyEvent.KEY_PRESSED, (event) -> {
			if(KeyCode.ENTER.equals(event.getCode()) && event.getTarget() instanceof Button)
			{
				((Button)event.getTarget()).fire();
			}
		}));
		
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == buttonTypeDelete)
		{
			DatabaseDeleter deleter = new DatabaseDeleter(controller);
			deleter.deleteDatabase(true);
		}	
		else if(result.get() == buttonTypeAppend)
		{	
			importDB();
		}
	}
	
	private void importDB()
	{
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(Localization.getString(Strings.TITLE_DATABASE_IMPORT));
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON (*.json)", "*.json");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showOpenDialog(controller.getStage());
		if(file != null)
		{
			Database database;
			try
			{
				database = FileHelper.loadDatabaseJSON(file);
				if(database.getCategories() == null 
					|| database.getNormalPayments() == null 
					|| database.getRepeatingPayments() == null
					|| database.getTags() == null
					|| database.getTagMatches() == null)
				{
					AlertGenerator.showAlert(AlertType.ERROR, 
											Localization.getString(Strings.TITLE_ERROR), 
											"", 
											Localization.getString(Strings.ERROR_DATABASE_IMPORT_WRONG_FILE), 
											controller.getIcon(), 
											controller.getStage(), 
											null, 
											false);
					return;
				}
			}
			catch(IOException e1)
			{
				Logger.error(e1);
				AlertGenerator.showAlert(AlertType.ERROR, 
										Localization.getString(Strings.TITLE_ERROR), 
										"", 
										Localization.getString(Strings.ERROR_DATABASE_IMPORT), 
										controller.getIcon(), 
										controller.getStage(), 
										null, 
										false);
				return;
			}

			Stage modalStage = UIHelpers.showModal(Localization.getString(Strings.TITLE_MODAL), Localization.getString(Strings.LOAD_DATABASE_IMPORT), controller.getStage(), controller.getIcon());

			Worker.runLater(() -> {
				try
				{
					ServerConnection connection = new ServerConnection(controller.getSettings());
					connection.importDatabase(database);

					Platform.runLater(() -> {
						if(modalStage != null)
						{
							modalStage.close();
						}						
						
						AlertGenerator.showAlert(AlertType.INFORMATION, 
												Localization.getString(Strings.INFO_TITLE_DATABASE_IMPORT), 
												"", 
												Localization.getString(Strings.INFO_TEXT_DATABASE_IMPORT), 
												controller.getIcon(), 
												controller.getStage(), 
												null, 
												false);
						
						controller.refresh(controller.getFilterSettings());
					});
				}
				catch(Exception e)
				{
					Logger.error(e);
					Platform.runLater(() -> {
						if(modalStage != null)
						{
							modalStage.close();
						}
						controller.showConnectionErrorAlert(ExceptionHandler.getMessageForException(e));
					});
				}
			});
		}
		else
		{
			controller.refresh(controller.getFilterSettings());
		}
	}
}