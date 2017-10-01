package de.deadlocker8.budgetmaster.logic.payment;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

public class PaymentJSONDeserializer
{
	public static Payment deserializePayment(JsonObject paymentObject)
	{		
		if(paymentObject.get("classType").getAsString().equals("NormalPayment"))
		{
			Payment payment = new NormalPayment();
			payment.setID(paymentObject.get("ID").getAsInt());
			payment.setAmount(paymentObject.get("amount").getAsInt());
			payment.setDate(paymentObject.get("date").getAsString());
			payment.setCategoryID(paymentObject.get("categoryID").getAsInt());
			payment.setName(paymentObject.get("name").getAsString());
			payment.setDescription(paymentObject.get("description").getAsString());
			return payment;
		}
		
		if(paymentObject.get("classType").getAsString().equals("RepeatingPayment"))
		{
			RepeatingPayment payment = new RepeatingPayment();
			payment.setID(paymentObject.get("ID").getAsInt());
			payment.setAmount(paymentObject.get("amount").getAsInt());
			payment.setDate(paymentObject.get("date").getAsString());
			payment.setCategoryID(paymentObject.get("categoryID").getAsInt());
			payment.setName(paymentObject.get("name").getAsString());
			payment.setDescription(paymentObject.get("description").getAsString());
			
			payment.setRepeatInterval(paymentObject.get("repeatInterval").getAsInt());
			payment.setRepeatMonthDay(paymentObject.get("repeatMonthDay").getAsInt());
			
			JsonElement repeatEndDate = paymentObject.get("repeatEndDate");
			payment.setRepeatEndDate(repeatEndDate != JsonNull.INSTANCE ? repeatEndDate.getAsString() : null);
			
			return payment;
		}	
		
		return null;
	}	
	
	public static ArrayList<Payment> deserializePaymentList(JsonArray paymentArray)
	{
		ArrayList<Payment> payments = new ArrayList<>();
		
		for(JsonElement currentElement : paymentArray)
		{
			payments.add(deserializePayment(currentElement.getAsJsonObject()));
		}		
		
		return payments;
	}
}
