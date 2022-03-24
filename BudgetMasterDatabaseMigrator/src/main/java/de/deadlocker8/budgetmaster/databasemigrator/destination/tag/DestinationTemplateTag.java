package de.deadlocker8.budgetmaster.databasemigrator.destination.tag;


import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = TableNames.TEMPLATE_TAGS)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DestinationTemplateTag
{
	@Id
	private int ID;

	@Column(name = "template_id")
	private int templateID;

	@Column(name = "tags_id")
	private int tagsID;
}
