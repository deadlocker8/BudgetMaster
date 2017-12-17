package de.deadlocker8.budgetmasterclient.ui.controller.settings;

import de.deadlocker8.budgetmasterclient.ui.Styleable;
import de.deadlocker8.budgetmasterclient.ui.controller.Controller;
import de.deadlocker8.budgetmasterclient.utils.DatabaseDeleter;
import de.deadlocker8.budgetmasterclient.utils.DatabaseExporter;
import de.deadlocker8.budgetmasterclient.utils.DatabaseImporter;

public abstract class SettingsController implements Styleable
{
	Controller controller;

	public abstract void init(Controller controller);
	
	public abstract void prefill();
	
	abstract void refreshLabelsUpdate();

	public abstract void save();

	public void exportDB()
	{
		DatabaseExporter exporter = new DatabaseExporter(controller);
		exporter.exportDatabase();
	}

	public void importDB()
	{
		DatabaseImporter importer = new DatabaseImporter(controller);
		importer.importDatabase();	
	}
	
	public void deleteDB()
	{
		DatabaseDeleter deleter = new DatabaseDeleter(controller);
		deleter.deleteDatabase(false);
	}

	public void checkForUpdates()
	{
		controller.checkForUpdates(true);		
		refreshLabelsUpdate();
	}
}