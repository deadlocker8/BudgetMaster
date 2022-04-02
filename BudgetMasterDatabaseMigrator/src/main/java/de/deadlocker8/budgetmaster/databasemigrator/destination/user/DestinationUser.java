package de.deadlocker8.budgetmaster.databasemigrator.destination.user;


import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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

	private int ID;

	private String name;

	private String password;

	@Column(name = "selected_account_id")
	private Integer selectedAccountID;
}
