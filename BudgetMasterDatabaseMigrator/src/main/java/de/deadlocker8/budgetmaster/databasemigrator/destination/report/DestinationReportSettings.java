package de.deadlocker8.budgetmaster.databasemigrator.destination.report;


import de.deadlocker8.budgetmaster.databasemigrator.CustomIdGenerator;
import de.deadlocker8.budgetmaster.databasemigrator.destination.ProvidesID;
import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
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
	@GeneratedValue(generator = "custom_generator")
	@GenericGenerator(name = "custom_generator", strategy = CustomIdGenerator.GENERATOR)
	private Integer ID;

	private LocalDate date;

	@Column(name = "include_budget")
	private boolean includeBudget;

	@Column(name = "include_category_budgets")
	private boolean includeCategoryBudgets;

	@Column(name = "split_tables")
	private boolean splitTables;
}
