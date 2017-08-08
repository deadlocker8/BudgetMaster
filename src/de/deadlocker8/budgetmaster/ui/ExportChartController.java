package de.deadlocker8.budgetmaster.ui;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javax.imageio.ImageIO;

import de.deadlocker8.budgetmaster.logic.chartGenerators.ChartExportable;
import fontAwesome.FontIcon;
import fontAwesome.FontIconType;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logger.Logger;
import tools.AlertGenerator;

public class ExportChartController
{
	@FXML private AnchorPane anchorPaneMain;
	@FXML private TextField textFieldWidth;
	@FXML private TextField textFieldHeight;
	@FXML private Label labelSavePath;
	@FXML private Button buttonChooseFile;
	@FXML private Button buttonExport;
	@FXML private Button buttonCancel;
	
	private ChartController controller;
	private Stage stage;
	private ChartExportable chart;
	private File savePath;

	public void init(Stage stage, ChartController controller, ChartExportable chart)
	{
		this.controller = controller;
		this.stage = stage;
		this.chart = chart;
		
		this.savePath = controller.getLastExportPath();
		if(savePath != null)
		{
			labelSavePath.setText(savePath.getAbsolutePath());
		}
				
		textFieldWidth.setText("600");
		textFieldHeight.setText("400");

		anchorPaneMain.setStyle("-fx-background-color: #F4F4F4;");
		
		FontIcon iconShow = new FontIcon(FontIconType.FOLDER_OPEN);
		iconShow.setSize(14);
		iconShow.setColor(Color.WHITE);
		buttonChooseFile.setStyle("-fx-background-color: #2E79B9; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;");
		buttonChooseFile.setGraphic(iconShow);

		FontIcon iconShow2 = new FontIcon(FontIconType.SAVE);
		iconShow2.setSize(14);
		iconShow2.setColor(Color.WHITE);
		buttonExport.setStyle("-fx-background-color: #2E79B9; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;");
		buttonExport.setGraphic(iconShow2);

		FontIcon iconShow3 = new FontIcon(FontIconType.TIMES);
		iconShow3.setSize(14);
		iconShow3.setColor(Color.WHITE);
		buttonCancel.setStyle("-fx-background-color: #2E79B9; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;");
		buttonCancel.setGraphic(iconShow3);		
		
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
		fileChooser.setTitle("Diagramm exportieren");
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG (*.png)", "*.png");
		if(savePath != null)
		{
			fileChooser.setInitialDirectory(savePath.getParentFile());
			fileChooser.setInitialFileName(savePath.getName());
		}
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showSaveDialog(stage);		
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
			AlertGenerator.showAlert(AlertType.WARNING, "Warnung", "", "Bitte gib eine Breite in Pixeln an.", controller.getControlle().getIcon(), stage, null, false);
			return;
		}
		
		int width = 0;
		try 
		{
			width = Integer.parseInt(widthText);
		}
		catch(Exception e)
		{
			AlertGenerator.showAlert(AlertType.WARNING, "Warnung", "", "Nur ganzahlige Werte sind für das Feld Breite erlaubt.", controller.getControlle().getIcon(), stage, null, false);
			return;
		}
		
		String heightText = textFieldHeight.getText();
		if(heightText == null || heightText.equals(""))
		{
			AlertGenerator.showAlert(AlertType.WARNING, "Warnung", "", "Bitte gib eine Höhe in Pixeln an.", controller.getControlle().getIcon(), stage, null, false);
			return;
		}
		
		int height = 0;
		try 
		{
			height = Integer.parseInt(heightText);
		}
		catch(Exception e)
		{
			AlertGenerator.showAlert(AlertType.WARNING, "Warnung", "", "Nur ganzahlige Werte sind für das Feld Höhe erlaubt.", controller.getControlle().getIcon(), stage, null, false);
			return;
		}

		if(savePath == null)
		{
			AlertGenerator.showAlert(AlertType.WARNING, "Warnung", "", "Wähle einen Speicherort für das Diagramm aus.", controller.getControlle().getIcon(), stage, null, false);
			return;
		}
		
		WritableImage image = chart.export(width, height);		
		
		try
		{
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", savePath);
			controller.getControlle().showNotification("Diagramm erfolgreich exportiert");	
			
			stage.close();			
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Erfolgreich erstellt");
			alert.setHeaderText("");
			alert.setContentText("Das Diagramm wurde erfolgreich exportiert");			
			Stage dialogStage = (Stage)alert.getDialogPane().getScene().getWindow();
			dialogStage.getIcons().add(controller.getControlle().getIcon());						
			
			ButtonType buttonTypeOne = new ButtonType("Ordner öffnen");
			ButtonType buttonTypeTwo = new ButtonType("Diagramm öffnen");
			ButtonType buttonTypeThree = new ButtonType("OK");						
			alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree);
			
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
					AlertGenerator.showAlert(AlertType.ERROR, "Fehler", "", "Der Ordner konnte nicht geöffnet werden\n\n" + e1.getMessage(), controller.getControlle().getIcon(), stage, null, false);
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
					AlertGenerator.showAlert(AlertType.ERROR, "Fehler", "", "Das Diagramm konnte nicht geöffnet werden\n\n" + e1.getMessage(), controller.getControlle().getIcon(), stage, null, false);
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
			AlertGenerator.showAlert(AlertType.ERROR, "Fehler", "", "Beim Exportieren des Diagramms ist ein Fehler aufgetreten:\n\n" + e.getMessage(), controller.getControlle().getIcon(), stage, null, false);
		}
		
		stage.close();	
		controller.setLastExportPath(savePath);
	}
	
	public void cancel()
	{
		stage.close();
	}
}