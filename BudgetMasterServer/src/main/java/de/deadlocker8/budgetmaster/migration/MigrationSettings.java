package de.deadlocker8.budgetmaster.migration;

public record MigrationSettings(String hostname, Integer port, String databaseName, String username, String password)
{
}
