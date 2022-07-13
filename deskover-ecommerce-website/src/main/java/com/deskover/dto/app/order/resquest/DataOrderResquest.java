package com.deskover.dto.app.order.resquest;

import java.io.Serializable;
import java.util.List;

import com.deskover.dto.app.order.OrderDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataOrderResquest implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 8702941383046256239L;
	
	private List<OrderDto> data ;
	

}
