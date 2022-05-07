package de.deadlocker8.budgetmaster.databasemigrator.destination.report;


import de.deadlocker8.budgetmaster.databasemigrator.destination.ProvidesID;
import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = TableNames.REPORT_SETTINGS)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DestinationReportSettings implements ProvidesID
{
	@Id
	private Integer ID;

	private LocalDate date;

	@Column(name = "include_budget")
	private boolean includeBudget;

	@Column(name = "include_category_budgets")
	private boolean includeCategoryBudgets;

	@Column(name = "split_tables")
	private boolean splitTables;
}
