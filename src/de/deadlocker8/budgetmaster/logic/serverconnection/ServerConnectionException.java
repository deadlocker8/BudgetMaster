package de.deadlocker8.budgetmaster.logic.serverconnection;

public class ServerConnectionException extends Exception
{
	private static final long serialVersionUID = 2784475774757068549L;

	public ServerConnectionException()
	{
		super();	
	}

	public ServerConnectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);		
	}

	public ServerConnectionException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ServerConnectionException(String message)
	{
		super(message);
	}

	public ServerConnectionException(Throwable cause)
	{
		super(cause);
	}
}