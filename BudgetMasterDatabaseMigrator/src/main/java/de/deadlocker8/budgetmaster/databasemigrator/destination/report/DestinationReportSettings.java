package de.deadlocker8.budgetmaster.databasemigrator.destination.report;


import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = TableNames.REPORT_SETTINGS)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DestinationReportSettings
{
	@Id

	private int ID;

	private String date;

	@Column(name = "include_budget")
	private boolean includeBudget;

	@Column(name = "include_category_budgets")
	private boolean includeCategoryBudgets;

	@Column(name = "split_tables")
	private boolean splitTables;
}
