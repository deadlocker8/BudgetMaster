package de.deadlocker8.budgetmaster.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class LoginController extends BaseController
{
	@RequestMapping("/login")
	public String login(HttpServletRequest request, Model model)
	{
		Map<String, String[]> paramMap = request.getParameterMap();

		if(paramMap.containsKey("error"))
			model.addAttribute("isError", true);

		if(paramMap.containsKey("logout"))
			model.addAttribute("isLogout", true);

		request.getSession().setAttribute("preLoginURL", request.getHeader("Referer"));
		return "login";
	}
}