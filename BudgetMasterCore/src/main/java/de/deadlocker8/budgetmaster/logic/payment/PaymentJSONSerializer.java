package de.deadlocker8.budgetmaster.logic.payment;

import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class PaymentJSONSerializer
{
	public static JsonObject serializePayment(Payment payment)
	{
		JsonObject paymentObject = new JsonObject();
		paymentObject.addProperty("ID", payment.getID());
		paymentObject.addProperty("amount", payment.getAmount());
		paymentObject.addProperty("date", payment.getDate());
		paymentObject.addProperty("categoryID", payment.getCategoryID());
		paymentObject.addProperty("name", payment.getName());		
		paymentObject.addProperty("description", payment.getDescription());
			
		if(payment instanceof NormalPayment)
		{
			paymentObject.addProperty("classType", "NormalPayment");
		}
		
		if(payment instanceof RepeatingPayment)
		{
			RepeatingPayment repeatingPayment = (RepeatingPayment)payment;
			
			paymentObject.addProperty("repeatInterval", repeatingPayment.getRepeatInterval());
			paymentObject.addProperty("repeatMonthDay", repeatingPayment.getRepeatMonthDay());
			paymentObject.addProperty("repeatEndDate", repeatingPayment.getRepeatEndDate());		
			
			paymentObject.addProperty("classType", "RepeatingPayment");
		}

		return paymentObject;
	}	
	
	public static JsonObject serializePaymentList(List<Payment> payments)
	{
		JsonArray paymentArray = new JsonArray();		
		
		for(Payment currentPayment : payments)
		{
			paymentArray.add(serializePayment(currentPayment));
		}
		
		JsonObject result = new JsonObject();
		result.add("payments", paymentArray);
		
		return result;
	}
}
