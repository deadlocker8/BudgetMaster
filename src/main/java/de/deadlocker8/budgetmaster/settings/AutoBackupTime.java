package de.deadlocker8.budgetmaster.settings;

import de.thecodelabs.utils.util.Localization;

public enum AutoBackupTime
{
	TIME_00("00:00"),
	TIME_01("01:00"),
	TIME_02("02:00"),
	TIME_03("03:00"),
	TIME_04("04:00"),
	TIME_05("05:00"),
	TIME_06("06:00"),
	TIME_07("07:00"),
	TIME_08("08:00"),
	TIME_09("09:00"),
	TIME_10("10:00"),
	TIME_11("11:00"),
	TIME_12("12:00"),
	TIME_13("13:00"),
	TIME_14("14:00"),
	TIME_15("15:00"),
	TIME_16("16:00"),
	TIME_17("17:00"),
	TIME_18("18:00"),
	TIME_19("19:00"),
	TIME_20("20:00"),
	TIME_21("21:00"),
	TIME_22("22:00"),
	TIME_23("23:00");

	private String time;

	AutoBackupTime(String time)
	{
		this.time = time;
	}

	public String getTime()
	{
		return time;
	}

	public String getLocalized()
	{
		return Localization.getString("settings.backup.auto.time.short", getTime());
	}

	public int getCronTime()
	{
		return Integer.parseInt(name().substring(name().lastIndexOf('_') + 1));
	}
}
