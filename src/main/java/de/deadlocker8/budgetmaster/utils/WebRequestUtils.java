package de.deadlocker8.budgetmaster.utils;

import de.deadlocker8.budgetmaster.utils.notification.Notification;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

public class WebRequestUtils
{
	private static final String ATTR_NOTIFICATIONS = "notifications";

	private WebRequestUtils()
	{
	}

	public static void putNotification(WebRequest request, Notification notification)
	{
		List<Notification> notifications = getNotifications(request, ATTR_NOTIFICATIONS);
		notifications.add(notification);
		put(request, notifications, ATTR_NOTIFICATIONS);
	}

	public static List<Notification> getNotifications(WebRequest request)
	{
		List<Notification> notifications = getNotifications(request, ATTR_NOTIFICATIONS);
		request.removeAttribute(ATTR_NOTIFICATIONS, RequestAttributes.SCOPE_SESSION);
		return notifications;
	}

	@SuppressWarnings("unchecked")
	private static List<Notification> getNotifications(WebRequest request, String key)
	{
		Object notifications = request.getAttribute(key, RequestAttributes.SCOPE_SESSION);
		if(notifications == null)
		{
			return new ArrayList<>();
		}

		return (List<Notification>) notifications;
	}

	private static void put(WebRequest request, Object any, String key)
	{
		request.setAttribute(key, any, RequestAttributes.SCOPE_SESSION);
	}
}
