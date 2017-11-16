package de.deadlocker8.budgetmasterclient.ui.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javax.imageio.ImageIO;

import de.deadlocker8.budgetmaster.logic.charts.ChartExportable;
import de.deadlocker8.budgetmaster.logic.utils.Colors;
import de.deadlocker8.budgetmaster.logic.utils.Strings;
import de.deadlocker8.budgetmasterclient.ui.Styleable;
import fontAwesome.FontIcon;
import fontAwesome.FontIconType;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logger.Logger;
import tools.AlertGenerator;
import tools.ConvertTo;
import tools.Localization;

public class ExportChartController extends BaseController implements Styleable
{
	@FXML private AnchorPane anchorPaneMain;
	@FXML private TextField textFieldWidth;
	@FXML private TextField textFieldHeight;
	@FXML private Label labelSavePath;
	@FXML private Button buttonChooseFile;
	@FXML private Button buttonExport;
	@FXML private Button buttonCancel;
	
	private ChartController controller;
	private Stage parentStage;
	private ChartExportable chart;
	private File savePath;
	
	public ExportChartController(Stage parentStage, ChartController controller, ChartExportable chart)
	{
		this.parentStage = parentStage;
		this.controller = controller;
		this.chart = chart;
		load("/de/deadlocker8/budgetmaster/ui/fxml/ExportChartGUI.fxml", Localization.getBundle());
		getStage().showAndWait();
	}
	
	@Override
	public void initStage(Stage stage)
	{
		stage.initOwner(parentStage);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle(Localization.getString(Strings.TITLE_CHART_EXPORT));		
		stage.getIcons().add(controller.getController().getIcon());
		stage.setResizable(false);
	}
	
	@Override
	public void init()
	{	
		applyStyle();
		
		this.savePath = controller.getLastExportPath();
		if(savePath != null)
		{
			labelSavePath.setText(savePath.getAbsolutePath());
		}
		
		textFieldWidth.setText(String.valueOf((int)chart.getSuggestedWidth()));
		textFieldHeight.setText(String.valueOf((int)chart.getSuggestedHeight()));	
		
		textFieldWidth.setTextFormatter(new TextFormatter<>(c -> {
			if(c.getControlNewText().isEmpty())
			{
				return c;
			}

			if(c.getControlNewText().matches("[0-9]*"))
			{
				return c;
			}
			else
			{
				return null;
			}
		}));
		
		textFieldHeight.setTextFormatter(new TextFormatter<>(c -> {
			if(c.getControlNewText().isEmpty())
			{
				return c;
			}

			if(c.getControlNewText().matches("[0-9]*"))
			{
				return c;
			}
			else
			{
				return null;
			}
		}));
	}	

	public void chooseFile()
	{
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(Localization.getString(Strings.TITLE_CHART_EXPORT));
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG (*.png)", "*.png");
		if(savePath != null)
		{
			fileChooser.setInitialDirectory(savePath.getParentFile());
			fileChooser.setInitialFileName(savePath.getName());
		}
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showSaveDialog(getStage());		
		if(file != null)
		{		
			savePath = file;
			labelSavePath.setText(file.getAbsolutePath());
		}
	}

	public void export()
	{
		String widthText = textFieldWidth.getText();
		if(widthText == null || widthText.equals(""))
		{
			AlertGenerator.showAlert(AlertType.WARNING, 
			                         Localization.getString(Strings.TITLE_WARNING), 
			                         "", 
			                         Localization.getString(Strings.WARNING_EMPTY_WIDTH_IN_PIXELS), 
			                         controller.getController().getIcon(), 
			                         getStage(), 
			                         null, 
			                         false);
			return;
		}
		
		int width = 0;
		try 
		{
			width = Integer.parseInt(widthText);
		}
		catch(Exception e)
		{
			AlertGenerator.showAlert(AlertType.WARNING, 
			                        Localization.getString(Strings.TITLE_WARNING), 
			                        "", 
			                        Localization.getString(Strings.WARNING_INTEGER_WIDTH_IN_PIXELS), 
			                        controller.getController().getIcon(), 
			                        getStage(), 
			                        null, 
			                        false);
			return;
		}
		
		String heightText = textFieldHeight.getText();
		if(heightText == null || heightText.equals(""))
		{
			AlertGenerator.showAlert(AlertType.WARNING, 
			                        Localization.getString(Strings.TITLE_WARNING), 
			                        "", 
			                        Localization.getString(Strings.WARNING_EMPTY_HEIGHT_IN_PIXELS), 
			                        controller.getController().getIcon(), 
			                        getStage(), 
			                        null, 
			                        false);
			return;
		}
		
		int height = 0;
		try 
		{
			height = Integer.parseInt(heightText);
		}
		catch(Exception e)
		{
			AlertGenerator.showAlert(AlertType.WARNING, 
			                        Localization.getString(Strings.TITLE_WARNING), 
			                        "", 
			                        Localization.getString(Strings.WARNING_INTEGER_HEIGHT_IN_PIXELS),
			                        controller.getController().getIcon(), 
			                        getStage(), 
			                        null, 
			                        false);
			return;
		}

		if(savePath == null)
		{
			AlertGenerator.showAlert(AlertType.WARNING, 
			                        Localization.getString(Strings.TITLE_WARNING), 
			                        "", 
			                        Localization.getString(Strings.WARNING_EMPTY_SAVEPATH_CHART), 
			                        controller.getController().getIcon(), 
			                        getStage(),
			                        null, 
			                        false);
			return;
		}
		
		WritableImage image = chart.export(width, height);		
		
		try
		{
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", savePath);
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle(Localization.getString(Strings.INFO_TITLE_CHART_EXPORT));
			alert.initOwner(controller.getController().getStage());
			alert.setHeaderText("");
			alert.setContentText(Localization.getString(Strings.INFO_TEXT_CHART_EXPORT));			
			Stage dialogStage = (Stage)alert.getDialogPane().getScene().getWindow();
			dialogStage.getIcons().add(controller.getController().getIcon());						
			
			ButtonType buttonTypeOne = new ButtonType(Localization.getString(Strings.INFO_TEXT_CHART_EXPORT_OPEN_FOLDER));
			ButtonType buttonTypeTwo = new ButtonType(Localization.getString(Strings.INFO_TEXT_CHART_EXPORT_OPEN_CHART));
			ButtonType buttonTypeThree = new ButtonType(Localization.getString(Strings.OK));						
			alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree);
			
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getButtonTypes().stream().map(dialogPane::lookupButton).forEach(button -> button.addEventHandler(KeyEvent.KEY_PRESSED, (event) -> {
				if(KeyCode.ENTER.equals(event.getCode()) && event.getTarget() instanceof Button)
				{
					((Button)event.getTarget()).fire();
				}
			}));
			
			Optional<ButtonType> result = alert.showAndWait();						
			if (result.get() == buttonTypeOne)
			{
				try
				{
					Desktop.getDesktop().open(new File(savePath.getParent().replace("\\", "/")));
				}
				catch(IOException e1)
				{
					Logger.error(e1);
					AlertGenerator.showAlert(AlertType.ERROR, 
					                        Localization.getString(Strings.TITLE_ERROR), 
                                            "",
                                            Localization.getString(Strings.ERROR_OPEN_FOLDER, e1.getMessage()),
                                            controller.getController().getIcon(), 
                                            getStage(), 
                                            null, 
                                            false);
				}
			}
			else if (result.get() == buttonTypeTwo)
			{
				try
				{
					Desktop.getDesktop().open(new File(savePath.getAbsolutePath().replace("\\", "/")));
				}
				catch(IOException e1)
				{
					Logger.error(e1);
					AlertGenerator.showAlert(AlertType.ERROR, 
                                            Localization.getString(Strings.TITLE_ERROR), 
                                            "", 
                                            Localization.getString(Strings.ERROR_OPEN_CHART, e1.getMessage()), 
                                            controller.getController().getIcon(), 
                                            getStage(), 
                                            null, 
                                            false);
				}
			}
			else
			{
				alert.close();
			}
		}
		catch(IOException e)
		{
			Logger.error(e);
			AlertGenerator.showAlert(AlertType.ERROR, 
			                         Localization.getString(Strings.TITLE_ERROR), 
			                         "",
			                         Localization.getString(Strings.ERROR_CHART_EXPORT, e.getMessage()),
			                         controller.getController().getIcon(), 
			                         getStage(), 
			                         null, 
			                         false);
		}
		
		getStage().close();	
		controller.getController().showNotification(Localization.getString(Strings.NOTIFICATION_CHART_EXPORT));	
		controller.setLastExportPath(savePath);
	}
	
	public void cancel()
	{
		getStage().close();
	}

	@Override
	public void applyStyle()
	{
		anchorPaneMain.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND));		
		
		buttonChooseFile.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;");
		buttonChooseFile.setGraphic(new FontIcon(FontIconType.FOLDER_OPEN, 14, Color.WHITE));
		
		buttonExport.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;");
		buttonExport.setGraphic(new FontIcon(FontIconType.SAVE, 14, Color.WHITE));

		buttonCancel.setStyle("-fx-background-color: " + ConvertTo.toRGBHexWithoutOpacity(Colors.BACKGROUND_BUTTON_BLUE) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;");
		buttonCancel.setGraphic(new FontIcon(FontIconType.TIMES, 14, Color.WHITE));		
	}
}