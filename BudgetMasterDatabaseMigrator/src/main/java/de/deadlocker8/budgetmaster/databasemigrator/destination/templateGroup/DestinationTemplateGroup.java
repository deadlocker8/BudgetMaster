package de.deadlocker8.budgetmaster.databasemigrator.destination.templateGroup;


import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = TableNames.TEMPLATE_GROUP)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DestinationTemplateGroup
{
	@Id
	private int ID;

	private String name;

	private Integer type;
}
