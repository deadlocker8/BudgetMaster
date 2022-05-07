package de.deadlocker8.budgetmaster.databasemigrator.destination.category;


import de.deadlocker8.budgetmaster.databasemigrator.destination.ProvidesID;
import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = TableNames.CATEGORY)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DestinationCategory implements ProvidesID
{
	@Id
	private Integer ID;

	private String name;

	private String color;

	private Integer type;

	@Column(name = "icon_reference_id")
	private Integer iconReferenceID;
}
