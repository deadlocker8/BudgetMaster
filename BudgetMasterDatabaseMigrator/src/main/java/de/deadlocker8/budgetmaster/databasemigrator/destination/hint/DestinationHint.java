package de.deadlocker8.budgetmaster.databasemigrator.destination.hint;


import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = TableNames.HINT)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DestinationHint
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ID;

	@Column(name = "localization_key")
	private String localizationKey;

	@Column(name = "is_dismissed")
	private boolean isDismissed;
}
