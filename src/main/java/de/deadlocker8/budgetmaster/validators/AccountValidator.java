package de.deadlocker8.budgetmaster.validators;

import de.deadlocker8.budgetmaster.entities.category.Category;
import de.deadlocker8.budgetmaster.utils.Strings;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


public class AccountValidator implements Validator
{
	public boolean supports(Class clazz)
	{
		return Category.class.equals(clazz);
	}

	public void validate(Object obj, Errors errors)
	{
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", Strings.WARNING_EMPTY_ACCOUNT_NAME);
	}
}