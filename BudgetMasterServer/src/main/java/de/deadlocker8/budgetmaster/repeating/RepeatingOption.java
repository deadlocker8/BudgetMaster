package de.deadlocker8.budgetmaster.repeating;

import com.google.gson.annotations.Expose;
import de.deadlocker8.budgetmaster.repeating.endoption.RepeatingEnd;
import de.deadlocker8.budgetmaster.repeating.modifier.RepeatingModifier;
import de.deadlocker8.budgetmaster.transactions.Transaction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class RepeatingOption
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Expose
	private Integer ID;

	@DateTimeFormat(pattern = "dd.MM.yyyy")
	@Expose
	private LocalDate startDate;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@Expose
	private RepeatingModifier modifier;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@Expose
	private RepeatingEnd endOption;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "repeatingOption", fetch = FetchType.LAZY)
	private List<Transaction> referringTransactions;

	public RepeatingOption(LocalDate startDate, RepeatingModifier modifier, RepeatingEnd endOption)
	{
		this.startDate = startDate;
		this.modifier = modifier;
		this.endOption = endOption;
	}

	public RepeatingOption()
	{
	}

	public Integer getID()
	{
		return ID;
	}

	public void setID(Integer ID)
	{
		this.ID = ID;
	}

	public LocalDate getStartDate()
	{
		return startDate;
	}

	public void setStartDate(LocalDate startDate)
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

	public List<Transaction> getReferringTransactions()
	{
		return referringTransactions;
	}

	public void setReferringTransactions(List<Transaction> referringTransactions)
	{
		this.referringTransactions = referringTransactions;
	}

	public List<LocalDate> getRepeatingDates(LocalDate dateFetchLimit)
	{
		List<LocalDate> dates = new ArrayList<>();
		dates.add(startDate);
		while(!endOption.isEndReached(dates))
		{
			LocalDate lastDate = dates.get(dates.size() - 1);
			LocalDate nextDate = modifier.getNextDate(lastDate);
			if(nextDate.isAfter(dateFetchLimit))
			{
				return dates;
			}

			List<LocalDate> temporaryList = new ArrayList<>(dates);
			temporaryList.add(nextDate);
			if(endOption.isEndReached(temporaryList))
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

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		RepeatingOption that = (RepeatingOption) o;
		return Objects.equals(ID, that.ID) &&
				Objects.equals(startDate, that.startDate) &&
				Objects.equals(modifier, that.modifier) &&
				Objects.equals(endOption, that.endOption);
	}

	@Override
	public int hashCode()
	{

		return Objects.hash(ID, startDate, modifier, endOption);
	}
}
