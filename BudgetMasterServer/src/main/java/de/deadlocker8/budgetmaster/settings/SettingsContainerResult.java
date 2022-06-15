package de.deadlocker8.budgetmaster.settings;

public record SettingsContainerResult(boolean isSuccess, String templatePath, Settings previousSettings)
{
}
