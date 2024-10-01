package de.deadlocker8.budgetmaster.services;

import java.util.List;

public record AccountEndDateReminderData(boolean show, List<String> accountsWithEndDateSoon)
{
}
