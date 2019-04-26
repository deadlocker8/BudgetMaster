package de.deadlocker8.budgetmaster.advices;

import de.deadlocker8.budgetmaster.MonthNames;
import de.deadlocker8.budgetmaster.WeekDay;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ControllerAdvice
public class CalendarAdvice
{
	@ModelAttribute("localizedWeekdays")
	public List<String> getWeekDays()
	{
		return WeekDay.getLocalizedStrings();
	}

	@ModelAttribute("localizesMonthNames")
	public List<String> getMonthNames()
	{
		return MonthNames.getLocalizedStrings();
	}
}