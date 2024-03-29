package com.deskover.service;

import java.util.List;

import com.deskover.model.entity.database.Product;
import com.deskover.model.entity.dto.ecommerce.BrandDTO;
import com.deskover.model.entity.dto.ecommerce.CartLocal;
import com.deskover.model.entity.dto.ecommerce.Filter;
import com.deskover.model.entity.dto.ecommerce.Item;
import com.deskover.model.entity.dto.ecommerce.OrderDetailDTO;
import com.deskover.model.entity.dto.ecommerce.OrderPage;
import com.deskover.model.entity.dto.ecommerce.ProductDTO;
import com.deskover.model.entity.dto.ecommerce.ProductSaleDTO;
import com.deskover.model.entity.dto.ecommerce.Reviewer;
import com.deskover.model.entity.dto.ecommerce.Shop;
import com.deskover.service.impl.CartDTO;

public interface ShopService {
	public Shop search(Filter filter);
	public ProductDTO getProduct(String slug);
	public List<Item> getRecommendList(Long category);
	public List<ProductSaleDTO> getFlashSale();
	
	public List<Item> get4TopRate();
	public List<Item> get4TopSale();
	public List<Item> get4TopSold();
	public List<Item> getProductNew();
	
	public List<BrandDTO> getListBrand();
	public Reviewer getReviewer(String slug, Integer page);
	
	public List<CartDTO> getCart(List<CartLocal> list, String username);
	public List<CartDTO> deleteCart(String slug, String username);
	public void deleteAllCart(String username);
	List<CartDTO> updateCart(CartLocal item, String username);
	
	OrderPage getOrder(String username, Integer page, String filter);
	OrderDetailDTO getOrderDetail(String username, String id);
	OrderDetailDTO getOrderDetail(String id);
}
