package de.deadlocker8.budgetmasterserver.server.updater;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Months;

import de.deadlocker8.budgetmaster.logic.LatestRepeatingPayment;
import de.deadlocker8.budgetmaster.logic.RepeatingPayment;
import de.deadlocker8.budgetmasterserver.main.DatabaseHandler;
import de.deadlocker8.budgetmasterserver.main.Settings;
import logger.LogLevel;
import logger.Logger;

public class RepeatingPaymentUpdater
{
	private Settings settings;
	
	public RepeatingPaymentUpdater(Settings settings)
	{
		this.settings = settings;
	}

	public void updateRepeatingPayments()
	{
		try
		{
			DatabaseHandler handler = new DatabaseHandler(settings);			
			ArrayList<RepeatingPayment> repeatingPayments = handler.getAllRepeatingPayments();				
			ArrayList<LatestRepeatingPayment> latest = handler.getLatestRepeatingPaymentEntries();;
			
			for(RepeatingPayment currentPayment : repeatingPayments)
			{
				int index = latest.indexOf(currentPayment);	
				DateTime now = DateTime.now();			
				if(currentPayment.getRepeatEndDate() != null)
				{
					now = DateTime.parse(currentPayment.getRepeatEndDate());
				}
				ArrayList<DateTime> correctDates = getCorrectRepeatingDates(currentPayment, now);				
				if(index != -1)
				{
					LatestRepeatingPayment currentLatest = latest.get(index);					
					DateTime latestDate = DateTime.parse(currentLatest.getLastDate());	
					
					for(int i = correctDates.size()-1; i > 0; i--)
					{
						DateTime currentDate = correctDates.get(i);
						if(currentDate.isBefore(latestDate) || currentDate.isEqual(latestDate))
						{
							break;
						}
						
						handler.addRepeatingPaymentEntry(currentLatest.getRepeatingPaymentID(), currentDate.toString("yyyy-MM-dd"));
					}
				}
				else
				{
					for(DateTime currentDate : correctDates)
					{
						handler.addRepeatingPaymentEntry(currentPayment.getID(), currentDate.toString("yyyy-MM-dd"));
					}
				}
			}
		}
		catch(IllegalStateException ex)
		{
			Logger.log(LogLevel.ERROR, Logger.exceptionToString(ex));
		}
	}
	
	private ArrayList<DateTime> getCorrectRepeatingDates(RepeatingPayment payment, DateTime now)
	{
		ArrayList<DateTime> dates = new ArrayList<>();
		DateTime startDate = DateTime.parse(payment.getDate());
		
		//repeat every x days
		if(payment.getRepeatInterval() != 0)
		{			
			int numberOfDays = Days.daysBetween(startDate, now).getDays();
			int occurrences = numberOfDays % payment.getRepeatInterval();
			for(int i = 0; i <= occurrences + 1; i++)
			{
				dates.add(startDate.plusDays(i * payment.getRepeatInterval()));
			}
		}
		//repeat every month on day x
		else
		{
			int numberOfMonths = Months.monthsBetween(startDate.withDayOfMonth(payment.getRepeatMonthDay()), now).getMonths();
			for(int i = 0; i <= numberOfMonths; i++)
			{
				dates.add(startDate.plusMonths(i));
			}				
		}
		
		return dates;
	}
}