package de.deadlocker8.budgetmaster.advices;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.TemplateHashModel;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class StaticClassesAdvice
{
	@ModelAttribute("static")
	public TemplateHashModel getLocalization()
	{
		BeansWrapperBuilder builder = new BeansWrapperBuilder(Configuration.VERSION_2_3_27);
		builder.setUseModelCache(true);
		builder.setExposeFields(true);
		BeansWrapper beansWrapper = builder.build();
		return beansWrapper.getStaticModels();
	}
}