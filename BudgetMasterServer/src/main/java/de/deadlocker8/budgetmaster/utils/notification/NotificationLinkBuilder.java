package de.deadlocker8.budgetmaster.utils.notification;

import jakarta.servlet.http.HttpServletRequest;
import java.text.MessageFormat;

public class NotificationLinkBuilder
{
	private NotificationLinkBuilder()
	{
		// empty
	}

	public static String buildEditLink(HttpServletRequest request, String linkName, String relativeLinkPath, Integer id)
	{
		final String contextPath = request.getContextPath();

		return MessageFormat.format("<a href=\"{0}{1}/{2}/edit\" class=\"text-default\">{3}</a>", contextPath, relativeLinkPath, String.valueOf(id), linkName);
	}

	public static String build(HttpServletRequest request, String linkName, String relativeLinkPath)
	{
		final String contextPath = request.getContextPath();

		return MessageFormat.format("<a href=\"{0}{1}\">{2}</a>", contextPath, relativeLinkPath, linkName);
	}
}
