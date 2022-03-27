package de.deadlocker8.budgetmaster.databasemigrator.destination.transaction;


import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = TableNames.TRANSACTION)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DestinationTransaction
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ID;

	private Integer amount;

	@Column(name = "is_expenditure")
	private Boolean isExpenditure;

	private String date;

	@Column(name = "account_id")
	private Integer accountID;

	@Column(name = "category_id")
	private Integer categoryID;

	private String name;

	private String description;

	@Column(name = "repeating_option_id")
	private Integer repeatingOptionID;

	@Column(name = "transfer_account_id")
	private Integer transferAccountID;
}
