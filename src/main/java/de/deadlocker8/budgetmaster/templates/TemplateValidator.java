package de.deadlocker8.budgetmaster.templates;

import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.utils.Strings;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;


public class TemplateValidator implements Validator
{
	private String previousTemplateName;
	private List<String> existingTemplateNames;

	public TemplateValidator(String previousTemplateName, List<String> existingTemplateNames)
	{
		this.previousTemplateName = previousTemplateName;
		this.existingTemplateNames = existingTemplateNames;
	}

	public boolean supports(Class clazz)
	{
		return Transaction.class.equals(clazz);
	}

	public void validate(Object obj, Errors errors)
	{
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "templateName", Strings.WARNING_EMPTY_TRANSACTION_NAME);


		final Template template = (Template) obj;
		if(previousTemplateName != null && previousTemplateName.equals(template.getTemplateName()))
		{
			return;
		}

		final boolean isNameAlreadyUsed = this.existingTemplateNames.contains(template.getTemplateName());
		if(isNameAlreadyUsed)
		{
			errors.rejectValue("templateName", Strings.WARNING_DUPLICATE_TEMPLATE_NAME);
		}
	}
}