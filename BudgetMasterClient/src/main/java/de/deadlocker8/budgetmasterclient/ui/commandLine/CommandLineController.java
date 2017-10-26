package de.deadlocker8.budgetmasterclient.ui.commandLine;

import java.util.ArrayList;
import java.util.Comparator;

import de.deadlocker8.budgetmasterclient.ui.commandLine.commands.Command;
import de.deadlocker8.budgetmasterclient.ui.commandLine.commands.PossibleCommands;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class CommandLineController
{
	@FXML private TextArea textareaHistory;
	@FXML private TextField textfieldInput;

	private CommandLine commandLine;

	public void init(CommandLine commandLine)
	{
		this.commandLine = commandLine;

		commandLine.getBundle().setController(this);
		commandLine.getBundle().setLanguageBundle(commandLine.getLanguageBundle());

		textareaHistory.setEditable(false);
		textareaHistory.setWrapText(true);

		textfieldInput.setOnKeyPressed(new EventHandler<KeyEvent>()
		{
			@Override
			public void handle(KeyEvent event)
			{
				if(event.getCode().equals(KeyCode.ENTER))
				{
					parse();
				}

				if(event.getCode().equals(KeyCode.UP))
				{
					showLastCommand();
				}

				if(event.getCode().equals(KeyCode.ESCAPE))
				{
					clearConsole();
				}
				
				if(event.getCode().equals(KeyCode.TAB))
				{
					autocomplete();
					event.consume();
				}
			}
		});

		printPrompt();
	}

	public void printPrompt()
	{
		setConsoleText();
		clearConsole();
	}

	public void print(String message)
	{
		commandLine.history.add(new HistoryEntry(HistoryType.MESSAGE, message));
		setConsoleText();
		printPrompt();
	}

	public void clearHistoryLog()
	{
		textareaHistory.setText("");
	}

	public void clearHistory()
	{
		commandLine.history = new ArrayList<>();
	}

	public void clearConsole()
	{
		textfieldInput.setText("");
		textfieldInput.requestFocus();
	}

	private void setConsoleText()
	{
		clearHistoryLog();

		StringBuilder sb = new StringBuilder();
		boolean printedLastEntry = false;
		for(int i = 0; i < commandLine.history.size(); i++)
		{
			HistoryEntry currentEntry = commandLine.history.get(i);
			if(currentEntry.getType().equals(HistoryType.COMMAND))
			{
				if(printedLastEntry)
				{
					sb.append("\n");
				}
				sb.append(commandLine.getPromptText());
				sb.append(" ");
				sb.append(currentEntry.getText());
				printedLastEntry = true;
			}
			else
			{
				if(i != 0)
				{
					sb.append("\n");
				}
				sb.append(currentEntry.getText());
				printedLastEntry = true;
			}
		}

		textareaHistory.setText(sb.toString());
		textareaHistory.positionCaret(sb.toString().length());
	}

	private boolean executeCommand(String[] command)
	{
		for(Command cmd : PossibleCommands.possibleCommands)
		{
			if(cmd.getKeyword().equals(command[0]))
			{
				cmd.execute(command, commandLine.getBundle());
				return true;
			}
		}
		return false;
	}

	private void parse()
	{
		String input = textfieldInput.getText().replace("\n", "");

		if(input.equals(""))
		{
			printPrompt();
			return;
		}

		commandLine.globalHistory.add(new HistoryEntry(HistoryType.COMMAND, input));
		commandLine.history.add(new HistoryEntry(HistoryType.COMMAND, input));
		commandLine.lastShownCommand = - 1;

		String[] command = input.split(" ");
		if( ! executeCommand(command))
		{
			print(commandLine.getLanguageBundle().getString("error.unknown.command"));
		}
		else
		{
			printPrompt();
		}
	}

	private void showLastCommand()
	{
		if(commandLine.globalHistory.size() > 0)
		{
			if(commandLine.lastShownCommand <= 0)
			{
				textfieldInput.setText(commandLine.globalHistory.get(commandLine.globalHistory.size() - 1).getText());
				commandLine.lastShownCommand = commandLine.globalHistory.size() - 1;
			}
			else
			{
				textfieldInput.setText(commandLine.globalHistory.get(commandLine.lastShownCommand - 1).getText());
				commandLine.lastShownCommand--;
			}
			
			Platform.runLater(()->
			{				
				textfieldInput.positionCaret(textfieldInput.getText().length());				
			});
		}
	}
	
	private void autocomplete()
	{
		String input = textfieldInput.getText().replace("\n", "");

		if(input.equals(""))
		{			
			return;
		}
		
		ArrayList<Command> commands = PossibleCommands.possibleCommands;
		
		//filter possible commands
		ArrayList<Command> filteredCommands = new ArrayList<>();
		for(Command currentCommand : commands)
		{
			if(currentCommand.getKeyword().startsWith(input))
			{
				filteredCommands.add(currentCommand);
			}
		}	
		
		//sort commands alphabetically		
		filteredCommands.sort(new Comparator<Command>()
		{
			@Override
			public int compare(Command o1, Command o2)
			{
				return o1.keyword.compareTo(o2.keyword);				
			}
		});		
		
		if(filteredCommands.size() == 1)
		{
			textfieldInput.setText(filteredCommands.get(0).getKeyword());
		}
		else
		{
			StringBuilder sb = new StringBuilder();		
			sb.append(">>> Possible commands for \"" + input + "\":\n");
			for(int i = 0; i < filteredCommands.size(); i++)
			{
				sb.append(filteredCommands.get(i).keyword);
				if(i != (filteredCommands.size()-1))
				{
					sb.append("\n");
				}
			}
			
			print(sb.toString());	
			
			textfieldInput.setText(input);
		}
		
		Platform.runLater(()->
		{				
			textfieldInput.positionCaret(textfieldInput.getText().length());				
		});
	}
}