package de.deadlocker8.budgetmaster.databasemigrator.destination.user;


import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = TableNames.USER_DESTINATION)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DestinationUser
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ID;

	private String name;

	private String password;

	@Column(name = "selected_account_id")
	private Integer selectedAccountID;
}
