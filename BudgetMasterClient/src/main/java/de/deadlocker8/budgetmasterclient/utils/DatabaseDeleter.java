package de.deadlocker8.budgetmasterclient.utils;

import java.util.Optional;

import de.deadlocker8.budgetmaster.logic.serverconnection.ExceptionHandler;
import de.deadlocker8.budgetmaster.logic.serverconnection.ServerConnection;
import de.deadlocker8.budgetmaster.logic.utils.Strings;
import de.deadlocker8.budgetmasterclient.ui.controller.Controller;
import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import logger.Logger;
import tools.AlertGenerator;
import tools.BASE58Type;
import tools.ConvertTo;
import tools.Localization;
import tools.RandomCreations;
import tools.Worker;

public class DatabaseDeleter
{
	private Controller controller;
	
	public DatabaseDeleter(Controller controller)
	{
		this.controller = controller;
	}
	
	public void deleteDatabase(boolean importPending)
	{
		String verificationCode = ConvertTo.toBase58(RandomCreations.generateRandomMixedCaseString(4, true), true, BASE58Type.UPPER);

		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle(Localization.getString(Strings.INFO_TITLE_DATABASE_DELETE));
		dialog.setHeaderText(Localization.getString(Strings.INFO_HEADER_TEXT_DATABASE_DELETE));
		dialog.setContentText(Localization.getString(Strings.INFO_TEXT_DATABASE_DELETE, verificationCode));
		Stage dialogStage = (Stage)dialog.getDialogPane().getScene().getWindow();
		dialogStage.getIcons().add(controller.getIcon());
		dialogStage.initOwner(controller.getStage());

		Optional<String> result = dialog.showAndWait();
		if(result.isPresent())
		{
			if(result.get().equals(verificationCode))
			{
				LoadingModal.showModal(controller, Localization.getString(Strings.TITLE_MODAL), Localization.getString(Strings.LOAD_DATABASE_DELETE), controller.getStage(), controller.getIcon());

				Worker.runLater(() -> {
					try
					{
						ServerConnection connection = new ServerConnection(controller.getSettings());
						connection.deleteDatabase();
						Platform.runLater(() -> {							
							LoadingModal.closeModal();
							if(importPending)
							{
								DatabaseImporter importer = new DatabaseImporter(controller);
								importer.importDB();
							}
							else
							{
								controller.refresh(controller.getFilterSettings());
							}
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
			else
			{
				AlertGenerator.showAlert(AlertType.WARNING, 
										Localization.getString(Strings.TITLE_WARNING), 
										"", 
										Localization.getString(Strings.WARNING_WRONG_VERIFICATION_CODE), 
										controller.getIcon(), 
										controller.getStage(), 
										null, 
										false);
				deleteDatabase(importPending);
			}
		}
	}
}