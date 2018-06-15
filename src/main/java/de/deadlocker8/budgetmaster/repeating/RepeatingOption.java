package de.deadlocker8.budgetmaster.repeating;

import com.google.gson.annotations.Expose;
import de.deadlocker8.budgetmaster.entities.Payment;
import de.deadlocker8.budgetmaster.repeating.endoption.RepeatingEnd;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifier;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class RepeatingOption
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Expose
	private Integer ID;

	@DateTimeFormat(pattern = "dd.MM.yyyy")
	@Expose
	private DateTime startDate;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@Expose
	private RepeatingModifier modifier;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@Expose
	private RepeatingEnd endOption;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "repeatingOption", fetch = FetchType.LAZY)
	private List<Payment> referringPayments;

	public RepeatingOption(DateTime startDate, RepeatingModifier modifier, RepeatingEnd endOption)
	{
		this.startDate = startDate;
		this.modifier = modifier;
		this.endOption = endOption;
	}

	public RepeatingOption() {}

	public Integer getID()
	{
		return ID;
	}

	public void setID(Integer ID)
	{
		this.ID = ID;
	}

	public DateTime getStartDate()
	{
		return startDate;
	}

	public void setStartDate(DateTime startDate)
	{
		this.startDate = startDate;
	}

	public RepeatingModifier getModifier()
	{
		return modifier;
	}

	public void setModifier(RepeatingModifier modifier)
	{
		this.modifier = modifier;
	}

	public RepeatingEnd getEndOption()
	{
		return endOption;
	}

	public void setEndOption(RepeatingEnd endOption)
	{
		this.endOption = endOption;
	}

	public List<Payment> getReferringPayments()
	{
		return referringPayments;
	}

	public void setReferringPayments(List<Payment> referringPayments)
	{
		this.referringPayments = referringPayments;
	}

	public List<DateTime> getRepeatingDates(DateTime dateFetchLimit)
	{
		List<DateTime> dates = new ArrayList<>();
		dates.add(startDate);
		while(!endOption.isEndReached(dates))
		{
			DateTime lastDate = dates.get(dates.size() - 1);
			DateTime nextDate = modifier.getNextDate(lastDate);
			if(nextDate.isAfter(dateFetchLimit))
			{
				return dates;
			}

			List<DateTime> temporaryList = new ArrayList<>(dates);
			temporaryList.add(nextDate);
			if(endOption.isEndReached((temporaryList)))
			{
				return dates;
			}
			else
			{
				dates.add(nextDate);
			}
		}

		return dates;
	}

	@Override
	public String toString()
	{
		return "RepeatingOption{" +
				"startDate=" + startDate +
				", modifier=" + modifier +
				", endOption=" + endOption +
				'}';
	}
}
