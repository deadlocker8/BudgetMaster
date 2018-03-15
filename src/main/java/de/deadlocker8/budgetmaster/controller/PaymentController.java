package de.deadlocker8.budgetmaster.controller;

import de.deadlocker8.budgetmaster.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class PaymentController extends BaseController
{
	@Autowired
	private PaymentRepository paymentRepository;

	@RequestMapping("/payments")
	public String payments(Model model)
	{
		model.addAttribute("payments", paymentRepository.findAll());
		return "payments/payments";
	}
}