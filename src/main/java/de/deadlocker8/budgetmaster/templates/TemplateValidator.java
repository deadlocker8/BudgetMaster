package de.deadlocker8.budgetmaster.templates;

import de.deadlocker8.budgetmaster.transactions.Transaction;
import de.deadlocker8.budgetmaster.utils.Strings;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


public class TemplateValidator implements Validator
{
	public boolean supports(Class clazz)
	{
		return Transaction.class.equals(clazz);
	}

	public void validate(Object obj, Errors errors)
	{
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "templateName", Strings.WARNING_EMPTY_TRANSACTION_NAME);
	}
}