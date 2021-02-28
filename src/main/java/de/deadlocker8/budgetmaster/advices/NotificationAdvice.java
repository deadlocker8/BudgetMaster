package de.deadlocker8.budgetmaster.advices;

import de.deadlocker8.budgetmaster.utils.Notification;
import de.deadlocker8.budgetmaster.utils.WebRequestUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class NotificationAdvice
{
	@ModelAttribute("notification")
	public Notification getToast(WebRequest request)
	{
		return WebRequestUtils.popNotification(request);
	}
}
