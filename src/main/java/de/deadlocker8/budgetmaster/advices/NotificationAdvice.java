package de.deadlocker8.budgetmaster.advices;

import de.deadlocker8.budgetmaster.utils.notification.Notification;
import de.deadlocker8.budgetmaster.utils.WebRequestUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@ControllerAdvice
public class NotificationAdvice
{
	@ModelAttribute("notifications")
	public List<Notification> getNotifications(WebRequest request)
	{
		return WebRequestUtils.getNotifications(request);
	}
}
