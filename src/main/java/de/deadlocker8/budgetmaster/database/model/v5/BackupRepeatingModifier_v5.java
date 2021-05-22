package de.deadlocker8.budgetmaster.database.model.v5;

import java.util.Objects;

public class BackupRepeatingModifier_v5
{
	private Integer quantity;
	private String localizationKey;

	public BackupRepeatingModifier_v5()
	{
	}

	public BackupRepeatingModifier_v5(Integer quantity, String localizationKey)
	{
		this.quantity = quantity;
		this.localizationKey = localizationKey;
	}

	public Integer getQuantity()
	{
		return quantity;
	}

	public void setQuantity(Integer quantity)
	{
		this.quantity = quantity;
	}

	public String getLocalizationKey()
	{
		return localizationKey;
	}

	public void setLocalizationKey(String localizationKey)
	{
		this.localizationKey = localizationKey;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		BackupRepeatingModifier_v5 that = (BackupRepeatingModifier_v5) o;
		return Objects.equals(quantity, that.quantity) && Objects.equals(localizationKey, that.localizationKey);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(quantity, localizationKey);
	}

	@Override
	public String toString()
	{
		return "BackupRepeatingModifier_v5{" +
				"quantity=" + quantity +
				", localizationKey='" + localizationKey + '\'' +
				'}';
	}
}
