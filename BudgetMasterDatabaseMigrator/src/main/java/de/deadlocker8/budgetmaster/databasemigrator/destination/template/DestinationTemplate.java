package de.deadlocker8.budgetmaster.databasemigrator.destination.template;


import de.deadlocker8.budgetmaster.databasemigrator.CustomIdGenerator;
import de.deadlocker8.budgetmaster.databasemigrator.destination.ProvidesID;
import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = TableNames.TEMPLATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DestinationTemplate implements ProvidesID
{
	@Id
	@GeneratedValue(generator = "custom_generator")
	@GenericGenerator(name = "custom_generator", strategy = CustomIdGenerator.GENERATOR)
	private Integer ID;

	@Column(name = "template_name")
	private String templateName;

	private Integer amount;

	@Column(name = "is_expenditure")
	private Boolean isExpenditure;

	@Column(name = "account_id")
	private Integer accountID;

	@Column(name = "category_id")
	private Integer categoryID;

	private String name;

	private String description;

	@Column(name = "icon_reference_id")
	private Integer iconReferenceID;

	@Column(name = "transfer_account_id")
	private Integer transferAccountID;

	@Column(name = "template_group_id")
	private Integer templateGroupID;
}
