package de.deadlocker8.budgetmaster.reports.settings;

import de.deadlocker8.budgetmaster.reports.columns.ReportColumn;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
public class ReportSettings
{
	@Id
	private Integer ID = 1;

	@DateTimeFormat(pattern = "dd.MM.yyyy")
	private LocalDate date;

	private boolean includeBudget;
	private boolean splitTables;
	private boolean includeCategoryBudgets;

	@OneToMany(mappedBy = "referringSettings", cascade = CascadeType.ALL)
	private List<ReportColumn> columns;

	public static ReportSettings getDefault()
	{
		return new ReportSettings(LocalDate.now(), true, true, true);
	}

	private ReportSettings(LocalDate date, boolean includeBudget, boolean splitTables, boolean includeCategoryBudgets)
	{
		this.date = date;
		this.includeBudget = includeBudget;
		this.splitTables = splitTables;
		this.includeCategoryBudgets = includeCategoryBudgets;
		this.columns = new ArrayList<>();
	}

	public ReportSettings()
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

	public LocalDate getDate()
	{
		return date;
	}

	public void setDate(LocalDate date)
	{
		this.date = date;
	}

	public boolean isIncludeBudget()
	{
		return includeBudget;
	}

	public void setIncludeBudget(boolean includeBudget)
	{
		this.includeBudget = includeBudget;
	}

	public boolean isSplitTables()
	{
		return splitTables;
	}

	public void setSplitTables(boolean splitTables)
	{
		this.splitTables = splitTables;
	}

	public boolean isIncludeCategoryBudgets()
	{
		return includeCategoryBudgets;
	}

	public void setIncludeCategoryBudgets(boolean includeCategoryBudgets)
	{
		this.includeCategoryBudgets = includeCategoryBudgets;
	}

	public List<ReportColumn> getColumnsSorted()
	{
		return columns.stream().sorted(Comparator.comparing(ReportColumn::getPosition)).toList();
	}

	public List<ReportColumn> getColumnsSortedAndFiltered()
	{
		return columns.stream().filter(ReportColumn::isActivated).sorted(Comparator.comparing(ReportColumn::getPosition)).toList();
	}

	public List<ReportColumn> getColumns()
	{
		return columns;
	}

	public void setColumns(List<ReportColumn> columns)
	{
		this.columns = columns;
	}

	@Override
	public String toString()
	{
		return "ReportSettings{" +
				"date=" + date +
				", includeBudget=" + includeBudget +
				", splitTables=" + splitTables +
				", includeCategoryBudgets=" + includeCategoryBudgets +
				", columns=" + columns +
				'}';
	}
}
