package com.deskover.service;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import com.deskover.entity.FlashSale;

import javax.validation.Valid;
public interface FlashSaleService {
    DataTablesOutput<FlashSale> getByActiveForDatatables(@Valid DataTablesInput input);

	void isCheckActived();
	
	FlashSale create(FlashSale flashSale);
	
	void delete(Long id);
	
	FlashSale updateProductToFlashSale(FlashSale discount, Long productIdToAdd, Long productIdToRemove);
	
	FlashSale getById(Long id);
	
	FlashSale getFlashSale();

	FlashSale activeToggle(Long id);
}
