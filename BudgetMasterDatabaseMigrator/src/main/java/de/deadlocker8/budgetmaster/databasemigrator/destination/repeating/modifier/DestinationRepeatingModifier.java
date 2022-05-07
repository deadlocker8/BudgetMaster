package de.deadlocker8.budgetmaster.databasemigrator.destination.repeating.modifier;


import de.deadlocker8.budgetmaster.databasemigrator.destination.ProvidesID;
import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = TableNames.REPEATING_MODIFIER)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DestinationRepeatingModifier implements ProvidesID
{
	@Id
	private Integer ID;

	@Column(name = "localization_key")
	private String localizationKey;

	private String type;

	private Integer quantity;
}
