package de.deadlocker8.budgetmaster.ui.tagField;

import java.util.ArrayList;

import org.controlsfx.control.textfield.TextFields;

import de.deadlocker8.budgetmaster.logic.tag.Tag;
import de.deadlocker8.budgetmaster.logic.utils.Colors;
import de.deadlocker8.budgetmaster.logic.utils.Helpers;
import de.deadlocker8.budgetmaster.logic.utils.Strings;
import fontAwesome.FontIconType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tools.ConvertTo;
import tools.Localization;

public class TagField extends VBox
{
	private ArrayList<Tag> tags;
	private ArrayList<Tag> allTags;
	private FlowPane flowPane;
	private TextField textField;
	
	public TagField(ArrayList<Tag> tags, ArrayList<Tag> allAvailableTags)
	{
		this.tags = tags;
		this.allTags = allAvailableTags;
		
		this.flowPane = initFlowPane();	
		this.getChildren().add(flowPane);
		
		textField = new TextField();
		textField.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #000000; -fx-border-width: 1 0 0 0; -fx-background-radius: 5px;");		
		textField.setPromptText(Localization.getString(Strings.TAGFIELD_PLACEHOLDER));
		textField.setMaxWidth(Double.MAX_VALUE);
		textField.setOnKeyPressed((event)->{
            if(event.getCode().equals(KeyCode.ENTER))
            {
            	addTag(textField.getText().trim());
            }
	    });
		TextFields.bindAutoCompletion(textField, param -> getCompletions(allTags));
		this.getChildren().add(textField);		

		this.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #000000; -fx-background-radius: 5px; -fx-border-radius: 5px");
		this.setSpacing(5);
		
		refresh(false);
	}
	
	private FlowPane initFlowPane() 
	{
		FlowPane flowPane = new FlowPane();
		flowPane.setVgap(5);
		flowPane.setMinHeight(20);
		flowPane.setHgap(5);
		flowPane.setPadding(new Insets(5));
		return flowPane;
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
		flowPane.getChildren().clear();
		
		for(Tag currentTag : tags)
		{
			flowPane.getChildren().add(generateTag(currentTag));
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