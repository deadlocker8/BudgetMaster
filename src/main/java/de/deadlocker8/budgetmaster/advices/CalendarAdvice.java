package de.deadlocker8.budgetmaster.advices;

import de.deadlocker8.budgetmaster.utils.types.MonthNames;
import de.deadlocker8.budgetmaster.utils.types.WeekDay;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class CalendarAdvice
{
	@ModelAttribute("localizedWeekdays")
	public List<String> getWeekDays()
	{
		return WeekDay.getLocalizedStrings();
	}

	@ModelAttribute("localizedMonthNames")
	public List<String> getMonthNames()
	{
		return MonthNames.getLocalizedStrings();
	}
}