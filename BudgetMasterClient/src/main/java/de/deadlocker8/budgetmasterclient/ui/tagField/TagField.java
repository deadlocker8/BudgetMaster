package de.deadlocker8.budgetmasterclient.ui.tagField;

import java.util.ArrayList;
import java.util.Collection;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import de.deadlocker8.budgetmaster.logic.tag.Tag;
import de.deadlocker8.budgetmaster.logic.utils.Colors;
import de.deadlocker8.budgetmaster.logic.utils.Helpers;
import de.deadlocker8.budgetmaster.logic.utils.Strings;
import de.deadlocker8.budgetmasterclient.ui.controller.NewPaymentController;
import fontAwesome.FontIconType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import tools.AlertGenerator;
import tools.ConvertTo;
import tools.Localization;

public class TagField extends VBox
{
	private ArrayList<Tag> tags;
	private ArrayList<Tag> allTags;
	private HBox hboxTags;
	private TextField textField;
	private NewPaymentController parentController; 
	
	public TagField(ArrayList<Tag> tags, ArrayList<Tag> allAvailableTags, NewPaymentController parentController)
	{
		this.tags = tags;
		this.allTags = allAvailableTags;
		this.parentController = parentController;
		
		this.hboxTags = initHboxTags();	
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setContent(hboxTags);
		scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.setMinHeight(50);
		scrollPane.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 5px; -fx-border-color: #000000; -fx-border-width: 1 1 0 1; -fx-border-radius: 5px 5px 0 0");

		this.getChildren().add(scrollPane);
		VBox.setVgrow(scrollPane, Priority.ALWAYS);
		
		textField = new TextField();
		textField.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #000000; -fx-border-width: 1; -fx-background-radius: 5px; -fx-border-radius: 0 0 5px 5px");		
		textField.setPromptText(Localization.getString(Strings.TAGFIELD_PLACEHOLDER));
		textField.setMaxWidth(Double.MAX_VALUE);
		textField.setOnKeyPressed((event)->{
            if(event.getCode().equals(KeyCode.ENTER))
            {
            	addTag(textField.getText().trim());
            }
            else if(event.getCode().equals(KeyCode.DOWN))
            {
            	textField.setText(" ");
            	textField.setText("");
            }
	    });
		
		TextFields.bindAutoCompletion(textField, new Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>()
		{
			@Override
			public Collection<String> call(org.controlsfx.control.textfield.AutoCompletionBinding.ISuggestionRequest param)
			{
				ArrayList<String> completions = getCompletions(allTags);
				ArrayList<String> remainingCompletions = new ArrayList<>();
				for(String currentCompletion : completions)
				{
					if(currentCompletion.toLowerCase().contains(param.getUserText().toLowerCase()))
					{
						remainingCompletions.add(currentCompletion);
					}
				}
				
				return remainingCompletions;
			}
		});
		this.getChildren().add(textField);		

		this.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 5px;");
		
		refresh(false);
	}
	
	private HBox initHboxTags() 
	{
		HBox newHboxTags = new HBox();
		newHboxTags.setSpacing(5);
		newHboxTags.setPadding(new Insets(5));
		newHboxTags.setStyle("-fx-background-color: transparent");		
		return newHboxTags;
	}
	
	private ArrayList<String> getCompletions(ArrayList<Tag> allTags)
	{		
		ArrayList<String> newCompletions = new ArrayList<>();
		for(Tag currentTag : allTags)
		{
			boolean isAlreadyInList = false;
			for(Tag paymentTag : tags)
			{
				if(currentTag.getName().equals(paymentTag.getName()))
				{
					isAlreadyInList = true;
				}
			}
			
			if(!isAlreadyInList)
			{
				newCompletions.add(currentTag.getName());
			}
		}
		
		return newCompletions;
	}
	
	public ArrayList<Tag> getTags()
	{
		return tags;
	}
	
	public void setTags(ArrayList<Tag> tags)
	{
		this.tags = tags;
		refresh(false);
	}

	public void setAllTags(ArrayList<Tag> allTags)
	{
		this.allTags = allTags;
		refresh(false);
	}

	public void addTag(String tagName)
	{		
		if(tagName.equals(""))
		{
			return;
		}
		
		for(Tag currentTag : tags)
		{
			if(currentTag.getName().equals(tagName))
			{
				return;
			}
		}
		
		if(tagName.length() > 45) 
		{
			AlertGenerator.showAlert(AlertType.WARNING, 
									Localization.getString(Strings.TITLE_WARNING), 
									"", 
									Localization.getString(Strings.WARNING_TAG_CHARACTER_LIMIT_REACHED_45), 
									parentController.getController().getIcon(), 
									parentController.getStage(), 
									null, 
									false);
			return;
		}
		
		tags.add(new Tag(-1, tagName));
		refresh(true);
	}
	
	public void removeTag(Tag tag)
	{
		tags.remove(tag);
		refresh(true);
	}
	
	private void refresh(boolean requstFocus)
	{
		hboxTags.getChildren().clear();
		
		for(Tag currentTag : tags)
		{
			hboxTags.getChildren().add(generateTag(currentTag));
		}
		
		if(requstFocus)
		{
			textField.setText("");
			textField.requestFocus();
		}
	}
	
	private HBox generateTag(Tag tag)
	{
		HBox hboxTag = new HBox();
		hboxTag.setSpacing(5);
		hboxTag.setAlignment(Pos.CENTER_LEFT);
		hboxTag.setPadding(new Insets(0, 3, 0, 7));
		hboxTag.setStyle("-fx-background-color: #cccccc; -fx-background-radius: 5px;");
		
		Label labelTagName = new Label(tag.getName());
		labelTagName.setStyle("-fx-font-size: 13; -fx-text-fill: " + ConvertTo.toRGBHexWithoutOpacity(Colors.TEXT));		
		hboxTag.getChildren().add(labelTagName);
		
		Button buttonDelete = new Button();
		buttonDelete.setGraphic(Helpers.getFontIcon(FontIconType.TIMES, 13, Colors.TEXT));
		buttonDelete.setStyle("-fx-background-color: transparent;");
		buttonDelete.getStyleClass().add("button-hoverable");
		buttonDelete.setOnAction((event)->{
			removeTag(tag);
		});
		hboxTag.getChildren().add(buttonDelete);
			
		return hboxTag;
	}
}