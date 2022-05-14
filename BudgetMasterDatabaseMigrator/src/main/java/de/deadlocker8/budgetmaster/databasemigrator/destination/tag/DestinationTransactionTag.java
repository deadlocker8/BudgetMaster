package de.deadlocker8.budgetmaster.databasemigrator.destination.tag;


import de.deadlocker8.budgetmaster.databasemigrator.destination.ProvidesID;
import de.deadlocker8.budgetmaster.databasemigrator.destination.TableNames;
import lombok.*;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = TableNames.TRANSACTION_TAGS)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@IdClass(DestinationTransactionTag.DestinationTransactionTagId.class)
public class DestinationTransactionTag implements ProvidesID
{
	public static final class DestinationTransactionTagId implements Serializable
	{
		@Serial
		private static final long serialVersionUID = 0L;
		private Integer transactionID;
		private Integer tagsID;

		public DestinationTransactionTagId(Integer transactionID, Integer tagsID)
		{
			this.transactionID = transactionID;
			this.tagsID = tagsID;
		}

		public DestinationTransactionTagId()
		{
		}

		public Integer transactionID()
		{
			return transactionID;
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
			var that = (DestinationTransactionTagId) obj;
			return Objects.equals(this.transactionID, that.transactionID) &&
					Objects.equals(this.tagsID, that.tagsID);
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(transactionID, tagsID);
		}

		@Override
		public String toString()
		{
			return "DestinationTransactionTagId[" +
					"transactionID=" + transactionID + ", " +
					"tagsID=" + tagsID + ']';
		}

	}

	@Id
	@Column(name = "transaction_id")
	private int transactionID;

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
	}
}
