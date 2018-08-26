package de.deadlocker8.budgetmaster.validators;

import de.deadlocker8.budgetmaster.entities.Transaction;
import de.deadlocker8.budgetmaster.utils.Strings;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


public class TransactionValidator implements Validator
{
	public boolean supports(Class clazz)
	{
		return Transaction.class.equals(clazz);
	}

	public void validate(Object obj, Errors errors)
	{
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", Strings.WARNING_EMPTY_TRANSACTION_NAME);
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "amount", Strings.WARNING_TRANSACTION_AMOUNT);
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "date", Strings.WARNING_EMPTY_TRANSACTION_DATE);
	}
}