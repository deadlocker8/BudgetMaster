package de.deadlocker8.budgetmaster.utils;

import de.deadlocker8.budgetmaster.utils.notification.Notification;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

public class WebRequestUtils
{
	private static final String ATTR_NOTIFICATION = "notification";

	private WebRequestUtils()
	{
	}

	public static void putNotification(WebRequest request, Notification notification)
	{
		put(request, notification, ATTR_NOTIFICATION);
	}

	public static Notification popNotification(WebRequest request)
	{
		return (Notification) pop(request, ATTR_NOTIFICATION);
	}

	private static void put(WebRequest request, Object any, String key)
	{
		request.setAttribute(key, any, RequestAttributes.SCOPE_SESSION);
	}

	public static Object pop(WebRequest request, String key)
	{
		final Object any = request.getAttribute(key, RequestAttributes.SCOPE_SESSION);
		request.removeAttribute(key, RequestAttributes.SCOPE_SESSION);

		return any;
	}
}
