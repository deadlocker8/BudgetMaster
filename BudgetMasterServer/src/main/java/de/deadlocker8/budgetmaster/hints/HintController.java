package de.deadlocker8.budgetmaster.hints;

import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.utils.Mappings;
import de.deadlocker8.budgetmaster.utils.WebRequestUtils;
import de.deadlocker8.budgetmaster.utils.notification.Notification;
import de.deadlocker8.budgetmaster.utils.notification.NotificationType;
import de.thecodelabs.utils.util.Localization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;


@Controller
@RequestMapping(Mappings.HINTS)
public class HintController extends BaseController
{
	private final HintService hintService;

	@Autowired
	public HintController(HintService hintService)
	{
		this.hintService = hintService;
	}

	@GetMapping("/dismiss/{ID}")
	@ResponseStatus(value = HttpStatus.OK)
	public void dismissHint(@PathVariable("ID") Integer ID)
	{
		hintService.dismiss(ID);
	}

	@GetMapping("/resetAll")
	public String resetAll(WebRequest request)
	{
		hintService.resetAll();

		WebRequestUtils.putNotification(request, new Notification(Localization.getString("notification.hints.reset"), NotificationType.SUCCESS));
		return "redirect:/settings";
	}
}