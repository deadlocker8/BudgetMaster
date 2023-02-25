package de.deadlocker8.budgetmaster.hints;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Hint
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ID;

	private String localizationKey;
	private boolean isDismissed;

	public Hint(String localizationKey, boolean isDismissed)
	{
		this.localizationKey = localizationKey;
		this.isDismissed = isDismissed;
	}

	public Hint()
	{
	}

	public int getID()
	{
		return ID;
	}

	public void setID(int ID)
	{
		this.ID = ID;
	}

	public String getLocalizationKey()
	{
		return localizationKey;
	}

	public void setLocalizationKey(String localizationKey)
	{
		this.localizationKey = localizationKey;
	}

	public boolean isDismissed()
	{
		return isDismissed;
	}

	public void setDismissed(boolean dismissed)
	{
		isDismissed = dismissed;
	}

	@Override
	public String toString()
	{
		return "Hint{" +
				"ID=" + ID +
				", localizationKey='" + localizationKey + '\'' +
				", isDismissed=" + isDismissed +
				'}';
	}
}