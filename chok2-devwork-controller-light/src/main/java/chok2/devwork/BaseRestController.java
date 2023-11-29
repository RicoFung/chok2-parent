package chok2.devwork;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ModelAttribute;

public class BaseRestController
{
	protected HttpServletRequest request;
	protected HttpServletResponse response;

	@ModelAttribute
	public void BaseInitialization(HttpServletRequest request, HttpServletResponse response)
	{
		this.request = request;
		this.response = response;
	}

}
