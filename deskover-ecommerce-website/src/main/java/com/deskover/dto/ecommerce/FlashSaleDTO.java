package com.deskover.dto.ecommerce;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.deskover.entity.FlashSale;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlashSaleDTO {
	
	public FlashSaleDTO(FlashSale fs) {
		this.end = new SimpleDateFormat("yyyy/MM/dd").format(fs.getEndDate());
	}
	
	private String end;
	private List<Item> items;
}
