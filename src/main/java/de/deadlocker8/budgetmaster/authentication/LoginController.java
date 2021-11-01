package de.deadlocker8.budgetmaster.authentication;

import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.utils.DateHelper;
import de.deadlocker8.budgetmaster.utils.Mappings;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping(Mappings.LOGIN)
public class LoginController extends BaseController
{
	@GetMapping
	public String login(HttpServletRequest request, Model model)
	{
		Map<String, String[]> paramMap = request.getParameterMap();

		if(paramMap.containsKey("error"))
		{
			model.addAttribute("isError", true);
		}

		if(paramMap.containsKey("logout"))
		{
			model.addAttribute("isLogout", true);
		}

		DefaultSavedRequest savedRequest = (DefaultSavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");
		if(savedRequest != null)
		{
			request.getSession().setAttribute("preLoginURL", savedRequest.getRequestURL());
		}

		model.addAttribute("currentDate", DateHelper.getCurrentDate());
		return "login";
	}
}