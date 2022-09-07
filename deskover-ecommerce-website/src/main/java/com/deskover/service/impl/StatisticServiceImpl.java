package com.deskover.service.impl;

import com.deskover.dto.TotalPrice;
import com.deskover.dto.dashboard.OrderReport;
import com.deskover.dto.dashboard.ProductReport;
import com.deskover.reponsitory.OrderItemRepository;
import com.deskover.reponsitory.UserRepository;
import com.deskover.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticServiceImpl implements StatisticService {
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private ProductService productService;
	@Autowired
	private UserService userService;
	@Autowired
	private RatingService ratingService;
	@Autowired
	private OrderItemRepository orderItemRepo;
	@Autowired
	private UserRepository userRepo;

	@Override
	public TotalPrice getTotalByCategory(String month, String year) {
		
		List<Object[]> totalByCategories = orderService.getTotalByCategory(month, year);
		
		List<String> array1 = new ArrayList<>();
		List<Double> array2 = new ArrayList<>();
		
		totalByCategories.forEach((total)->{
			array1.add(total[0].toString());
			array2.add((Double) total[1]);

		});
		TotalPrice price = new TotalPrice(); 
		price.setName(array1);
		price.setTotalPrice(array2);
		return price;
	}

	@Override
	public String[][] getTotalPricePerMonthAndYear(Integer months,Integer years) {
		
		
		YearMonth current = YearMonth.now();
		if(years.equals(current.getYear())) {
			String[][] result = new String[2][months];
			for (int i = 0; i < months; i++) {
				String month = current.minusMonths((long)i).getMonthValue() + "";
				String year = current.minusMonths((long)i).getYear() + "";
				System.out.println(year);
				result[0][(months-1)-i] = month + "-" + year;
				result[1][(months-1)-i] = orderService.getTotalPricePerYear(month, year);
			}
			return result;
		}
		String[][] result = new String[2][12];
		for (int i = 0; i < 12; i++) {
			result[0][(12-1)-i] = 12-i + "-" + years;
			result[1][(12-1)-i] = orderService.getTotalPricePerYear(12-i+"", years+"");
		}
 		return result;
	}


	@Override
	public Map<String, Object> getTotalGeneral() {
		Map<String, Object> totalGeneral = new HashMap<>();
		totalGeneral.put("totalOrders", orderService.totalOrders("C-XN"));
		totalGeneral.put("totalProducts", productService.totalProducts());
		totalGeneral.put("totalCustomers", userService.totalCustomers());
		totalGeneral.put("totalPosts", ratingService.totalRatings());
		totalGeneral.put("totalRevenue", orderService.totalRevenue());
		return totalGeneral;
	}

	@Override
	public List<OrderReport> getQuantityProductSoldBySubcategory() {
		return orderItemRepo.getQuantityProductSoldBySubcategory();
	}

	@Override
	public List<ProductReport> getTopProductSold(Integer size) {
		return orderItemRepo.getTopProductSold().stream()
				.limit(size)
				.sorted((o1, o2) -> (int) (o2.getQuantity() - o1.getQuantity()))
				.collect(Collectors.toList());
	}

	@Override
	public List<Object> getTotalAccountByRole() {
		return userRepo.getTotalAccountByRole();
	}
}
