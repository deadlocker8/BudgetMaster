package de.deadlocker8.budgetmaster.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController extends BaseController
{
	@RequestMapping("/login")
	public String login(HttpServletRequest request)
	{
		request.getSession().setAttribute("preLoginURL", request.getHeader("Referer"));
		return "login";
	}
}