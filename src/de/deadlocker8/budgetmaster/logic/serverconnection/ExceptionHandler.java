package de.deadlocker8.budgetmaster.logic.serverconnection;

import java.net.UnknownHostException;

public class ExceptionHandler
{
	public static String getMessageForException(Exception e)
	{
		if(e instanceof ServerConnectionException)
		{
			return handleServerConnectionException(e);
		}			
		
		if(e instanceof UnknownHostException)
		{
			return "Es konnte keine Verbindung mit dem Internet hergestellt werden.";
		}
		
		if(e.getMessage() == null)
		{
			return "Unbekannter Fehler (" + e.getClass() + ")";
		}
				
		if(e.getMessage().contains("Connection refused"))
		{
			return "Server nicht erreichbar.";
		}
		else if(e.getMessage().contains("HTTPS hostname wrong"))
		{
			return "Der Server verwendet ein selbst signiertes Zertifkat für die Verschlüsselung. "
					+ "Aus Sicherheitsgründen werden diese Zertifikate standardmäßig blockiert. "
					+ "Wenn du dem Zertifikat trotzdem vertrauen möchtest, dann füge den Hostnamen des Servers zur Liste der vertrauenswürdigen Hosts in den Einstellungen hinzu.";
		}
		return e.getMessage();
	}
	
	private static String handleServerConnectionException(Exception e)
	{
		switch(e.getMessage())
		{
			case "400": return "Der Server erhielt eine fehlerhafte Anfrage oder ungültige Parameter.";
			case "401": return "Ungültiges Passwort.";
			case "500": return "Beim Ausführen der Anfrage ist ein interner Serverfehler ist aufgetreten.";
			default: return e.getMessage();
		}
	}
}