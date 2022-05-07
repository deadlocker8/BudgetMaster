package de.deadlocker8.budgetmaster.databasemigrator.destination.image;

import de.deadlocker8.budgetmaster.databasemigrator.destination.ProvidesID;
import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = TableNames.IMAGE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DestinationImage implements ProvidesID
{
	@Id
	private Integer ID;

	@Lob
	@ToString.Exclude
	private byte[] image;

	@Column(name = "file_name")
	private String fileName;

	@Column(name = "file_extension")
	private Integer fileExtension;
}