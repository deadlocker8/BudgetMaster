package de.deadlocker8.budgetmaster.databasemigrator.destination.icon;

import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = TableNames.ICON)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DestinationIcon
{
	@Id

	private Integer ID;

	@Column(name = "image_id")
	private Integer imageID;

	@Column(name = "builtin_identifier")
	private String builtinIdentifier;

	@Column(name = "font_color")
	private String fontColor;
}
