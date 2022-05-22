package de.deadlocker8.budgetmaster.databasemigrator.destination.tag;


import de.deadlocker8.budgetmaster.databasemigrator.destination.ProvidesID;
import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import lombok.*;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = TableNames.TEMPLATE_TAGS)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@IdClass(DestinationTemplateTag.DestinationTemplateTagId.class)
public class DestinationTemplateTag implements ProvidesID
{
	public static final class DestinationTemplateTagId implements Serializable
	{
		@Serial
		private static final long serialVersionUID = 0L;
		private Integer templateID;
		private Integer tagsID;

		public DestinationTemplateTagId(Integer templateID, Integer tagsID)
		{
			this.templateID = templateID;
			this.tagsID = tagsID;
		}

		public DestinationTemplateTagId()
		{
		}

		public Integer templateID()
		{
			return templateID;
		}

		public Integer tagsID()
		{
			return tagsID;
		}

		@Override
		public boolean equals(Object obj)
		{
			if(obj == this) return true;
			if(obj == null || obj.getClass() != this.getClass()) return false;
			var that = (DestinationTemplateTagId) obj;
			return Objects.equals(this.templateID, that.templateID) &&
					Objects.equals(this.tagsID, that.tagsID);
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(templateID, tagsID);
		}

		@Override
		public String toString()
		{
			return "DestinationTemplateTagId[" +
					"templateID=" + templateID + ", " +
					"tagsID=" + tagsID + ']';
		}

	}

	@Id
	@Column(name = "template_id")
	private int templateID;

	@Id
	@Column(name = "tags_id")
	private int tagsID;

	@Override
	public Integer getID()
	{
		return null;
	}

	@Override
	public void setID(Integer ID)
	{
		// not allowed
	}
}

