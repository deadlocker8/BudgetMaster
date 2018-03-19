package de.deadlocker8.budgetmaster.validators;

import de.deadlocker8.budgetmaster.entities.Payment;
import de.deadlocker8.budgetmaster.utils.Strings;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


public class PaymentValidator implements Validator
{
	public boolean supports(Class clazz)
	{
		return Payment.class.equals(clazz);
	}

	public void validate(Object obj, Errors errors)
	{
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", Strings.WARNING_EMPTY_PAYMENT_NAME);
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "amount", Strings.WARNING_PAYMENT_AMOUNT);
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "date", Strings.WARNING_EMPTY_PAYMENT_DATE);
	}
}