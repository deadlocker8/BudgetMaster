package de.deadlocker8.budgetmaster.reports;

public class ReportColumn
{
	private boolean activated;
	private int position;

	public boolean isActivated()
	{
		return activated;
	}

	public void setActivated(boolean activated)
	{
		this.activated = activated;
	}

	public int getPosition()
	{
		return position;
	}

	public void setPosition(int position)
	{
		this.position = position;
	}

	@Override
	public String toString()
	{
		return "ReportColumn{" +
				"activated=" + activated +
				", position=" + position +
				'}';
	}
}
