package de.deadlocker8.budgetmaster.transactions;

public enum TransactionSubmitAction
{
	SAVE("save"),
	TEMPLATE("template");

	private String actionName;

	TransactionSubmitAction(String actionName)
	{
		this.actionName = actionName;
	}

	public String getActionName()
	{
		return actionName;
	}

	@Override
	public String toString()
	{
		return "TransactionSubmitAction{" +
				"actionName='" + actionName + '\'' +
				'}';
	}
}
