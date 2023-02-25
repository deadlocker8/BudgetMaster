package de.deadlocker8.budgetmaster.authentication;

import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.utils.DateHelper;
import de.deadlocker8.budgetmaster.utils.Mappings;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping(Mappings.LOGIN)
public class LoginController extends BaseController
{
	private static class ModelAttributes
	{
		public static final String IS_ERROR = "isError";
		public static final String IS_LOGOUT = "isLogout";
		public static final String CURRENT_DATE = "currentDate";
	}

	private static class ReturnValues
	{
		public static final String LOGIN = "login";
	}

	@GetMapping
	public String login(HttpServletRequest request, Model model)
	{
		Map<String, String[]> paramMap = request.getParameterMap();

		if(paramMap.containsKey("error"))
		{
			model.addAttribute(ModelAttributes.IS_ERROR, true);
		}

		if(paramMap.containsKey("logout"))
		{
			model.addAttribute(ModelAttributes.IS_LOGOUT, true);
		}

		DefaultSavedRequest savedRequest = (DefaultSavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");
		if(savedRequest != null)
		{
			request.getSession().setAttribute("preLoginURL", savedRequest.getRequestURL());
		}

		model.addAttribute(ModelAttributes.CURRENT_DATE, DateHelper.getCurrentDate());
		return ReturnValues.LOGIN;
	}
}