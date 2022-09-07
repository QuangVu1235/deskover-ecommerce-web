package com.deskover.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.deskover.dto.ProductDto;
import com.deskover.entity.UserAddress;
import com.deskover.service.SessionService;
import com.deskover.service.ShopService;
import com.deskover.service.impl.CheckoutServiceImpl;

@Controller
public class CheckoutController {
	@Autowired ShopService shopService;
	@Autowired SessionService sessionService;
	@Autowired CheckoutServiceImpl checkoutService;
	
	@PostMapping("/cartAmounts")
	public String amounts(@RequestBody List<Integer> amounts, Model model) {
		sessionService.set("amount", amounts);	
		return "ok";
	}
	
	@PostMapping("/cartItem")
	public String checkout(@RequestBody List<ProductDto> items, Model model) {
		try {
			List<Integer> amounts =  sessionService.get("amount");
			for (int i = 0; i < amounts.size(); i++) {
				items.get(i).setQuantity((long)amounts.get(i));
				sessionService.set("items", items);
			}
		} catch (Exception e) { }
		return "ok";
	}
	
	@PostMapping("/ok")
	public String checkoutOk(Model model ,@ModelAttribute("addressForm") @Valid UserAddress entity, Errors errors, @ModelAttribute("Total") String total ) {
		ArrayList<ProductDto> items =  sessionService.get("items");
		checkoutService.saveOrder(entity,total, items);
		return "redirect:/invoice";
	}
	
	@PostMapping("/invoice")
	public String checkoutvnPayOk( ) {
		return "redirect:/invoice";
	}
}
