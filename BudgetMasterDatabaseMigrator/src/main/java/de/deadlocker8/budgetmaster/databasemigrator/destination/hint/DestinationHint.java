package de.deadlocker8.budgetmaster.databasemigrator.destination.hint;


import de.deadlocker8.budgetmaster.databasemigrator.destination.ProvidesID;
import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = TableNames.HINT)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DestinationHint implements ProvidesID
{
	@Id
	private Integer ID;

	@Column(name = "localization_key")
	private String localizationKey;

	@Column(name = "is_dismissed")
	private boolean isDismissed;
}
