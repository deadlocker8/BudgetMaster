package de.deadlocker8.budgetmaster.services;

import de.deadlocker8.budgetmaster.utils.Mappings;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorCodeController implements ErrorController
{
	@Override
	public String getErrorPath()
	{
		return Mappings.ERROR;
	}

	@RequestMapping(Mappings.ERROR)
	public String handleError(HttpServletRequest request)
	{
		final Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

		if(status == null)
		{
			return "error/500";
		}

		int statusCode = Integer.parseInt(status.toString());

		if(statusCode == HttpStatus.BAD_REQUEST.value())
		{
			return "error/400";
		}
		else if(statusCode == HttpStatus.FORBIDDEN.value())
		{
			return "error/403";
		}
		else if(statusCode == HttpStatus.NOT_FOUND.value())
		{
			return "error/404";
		}
		else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value())
		{
			return "error/500";
		}

		return "error/500";
	}
}
