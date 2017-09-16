package de.deadlocker8.budgetmaster.ui.tagField;

import java.util.ArrayList;

import org.controlsfx.control.textfield.TextFields;

import de.deadlocker8.budgetmaster.logic.tag.Tag;
import de.deadlocker8.budgetmaster.logic.tag.TagHandler;
import de.deadlocker8.budgetmaster.logic.utils.Colors;
import de.deadlocker8.budgetmaster.logic.utils.Helpers;
import fontAwesome.FontIconType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import tools.ConvertTo;

public class TagField extends FlowPane
{
	private ArrayList<Tag> tags;
	private TextField textField;
	
	public TagField(ArrayList<Tag> tags, TagHandler tagHandler)
	{
		this.tags = tags;
		this.setVgap(5);
		this.setHgap(5);
		this.setPadding(new Insets(5));
		this.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #000000; -fx-background-radius: 5px; -fx-border-radius: 5px");
		
		textField = new TextField();
		textField.setMaxWidth(Double.MAX_VALUE);
		textField.setOnKeyPressed((event)->{
            if(event.getCode().equals(KeyCode.ENTER))
            {
            	addTag(textField.getText().trim());
            }
	    });
		
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
				textField.setText("");
				return;
			}
		}
		
		tags.add(new Tag(-1, tagName));
		textField.setText("");
		refresh(true);
	}
	
	public void removeTag(Tag tag)
	{
		tags.remove(tag);
		refresh(true);
	}
	
	private void refresh(boolean requstFocus)
	{
		this.getChildren().clear();
		
		for(Tag currentTag : tags)
		{
			this.getChildren().add(generateTag(currentTag));
		}
	
		ArrayList<String> test = new ArrayList<>();		
		test.add("apfel");
		test.add("baum");
		TextFields.bindAutoCompletion(textField, test);
		this.getChildren().add(textField);	
		
		if(requstFocus)
		{
			textField.requestFocus();
		}
	}
	
	private HBox generateTag(Tag tag)
	{
		HBox hboxTag = new HBox();
		hboxTag.setSpacing(5);
		hboxTag.setAlignment(Pos.CENTER_LEFT);
		hboxTag.setPadding(new Insets(0, 5, 0, 5));
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