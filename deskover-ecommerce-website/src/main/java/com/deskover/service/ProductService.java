package com.deskover.service;

import com.deskover.dto.dashboard.OrderReport;
import com.deskover.entity.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface ProductService {

	Page<Product> getByActive(Boolean isActive, Optional<Integer> page, Optional<Integer> size);

	Page<Product> getByName(String name, Optional<Integer> page, Optional<Integer> size);
	
	List<Product> getBySubcategoryId(Long id);

	Product changeActive(Long id);

	Product save(Product product);

	Product save(Product product, Boolean isCopy);

	Product findById(Long id);

	Boolean existsBySlug(String slug);

	Product findBySlug(String slug);

	Boolean existsByOtherSlug(Product product);

    Boolean existsBySlug(Product product);

    DataTablesOutput<Product> getByActiveForDatatables(
			@Valid DataTablesInput input,
			Boolean isActive,
			Long categoryId,
			Long brandId,
			Boolean isDiscount,
			Boolean isFlashSale
	);

	void changeDelete(List<Product> products, Boolean isActive);

	void changeActiveSubcategoty(Long id);

	Page<Product>  getProductByCreateAtDesc(Boolean active,Optional<Integer> page, Optional<Integer> size);

	Page<Product> getProductByCategoryId(Boolean active, Long categoryId, Optional<Integer> page,
			Optional<Integer> size, String keySort);

	Page<Product> getProductBySubId(Boolean active, Long subId, Optional<Integer> page, Optional<Integer> size, String keySort);

	Page<Product> doGetProductSale(Optional<Integer> page, Optional<Integer> size);

	Page<Product> doGetProductDiscount(Optional<Integer> page, Optional<Integer> size, String keySort);

	Long totalProducts();

	List<OrderReport> totalQuantityProductByCategory();
}