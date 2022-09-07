package com.deskover.reponsitory;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.deskover.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	//Shipper
	@Query(value = "{CALL getTotalPrice_Shipping_PerDay(:day,:month, :year,:modified_by,:code)}", nativeQuery = true)
	String getTotalPrice_Shipping_PerDay(
			@Param("day") String day,
			@Param("month") String month,
			@Param("year") String year,
			@Param("modified_by") String modified_by,
			@Param("code") String code);
	
	@Query(value = "{CALL getTotalPrice_Shiping_PerMonth(:month, :year,:modified_by)}", nativeQuery = true)
	String getToTalPricePerMonth(@Param("month") String month,
			@Param("year") String year,
			@Param("modified_by") String modified_by);
	
	@Query(value = "{CALL countOrder(:month, :year,:modified_by)}", nativeQuery = true)
	String getCountOrder(@Param("month") String month,
			@Param("year") String year,
			@Param("modified_by") String modified_by);
	
	//Tổng tiền đơn hàng
	
	@Query(value = "{CALL totalOrder(:id)}", nativeQuery = true)
	Double getTotalOrder(@Param("id") Long id);
	
	
	//DashBoard-Admin
	@Query(value="{CALL getTotalByCategory(:month, :year)}", nativeQuery = true)
	List<Object[]> getTotalByCategory(@Param("month") String month, @Param("year") String year);
	
	@Query(value = "{CALL getTotalPricePerYear(:month, :year)}", nativeQuery = true)
	String getTotalPricePerYear(@Param("month") String month,
			@Param("year") String year);

	//Query nornal
	List<Order> findByOrderStatusCode(String statusCode);
	
	List<Order> findByOrderStatusCodeAndUserUsernameOrderByCreatedAtDesc(String statusCode, String username);
	
	List<Order> findByUserUsernameOrderByCreatedAtDesc(String username);
	
	List<Order> findByShippingShippingId(String shippingId);
	
	List<Order> findByModifiedByAndOrderStatusCode(String modifiedBy, String code);
	
	List<Order> findByModifiedBy(String modifiedBy);
	
	Order findByOrderCodeAndOrderStatusCode( String orderCode,  String status);
	
	Order findByOrderCodeAndUserUsername(String orderCode,  String username);
	
	Order findByOrderCode( String orderCode);

	@Query("select count(o) from Order o where o.orderStatus.code = ?1")
	Long countByOrderStatusCode(String statusCode);
	
	@Query(value = "SELECT * FROM orders ORDER BY ID DESC LIMIT 1", nativeQuery = true)
	Order getLastOrder();
	
	@Query(value = "SELECT o FROM Order o WHERE o.user.username = ?1", nativeQuery = false)
	Page<Order> getListOrderByUsername(String username, Pageable Page);
	
	@Query(value = "SELECT o FROM Order o WHERE o.user.username = ?1 AND o.orderStatus.code = 'GH-TC'", nativeQuery = false)
	Page<Order> getListOrderByUsernameDelivered(String username, Pageable Page);
	
	@Query(value = "SELECT o FROM Order o WHERE o.user.username = ?1 AND o.orderStatus.code != 'GH-TC'", nativeQuery = false)
	Page<Order> getListOrderByUsernameUnDelivered(String username, Pageable Page);
	
	@Query(value = "SELECT o FROM Order o WHERE o.user.username = ?1 AND o.statusPayment.code = 'D-TT'", nativeQuery = false)
	Page<Order> getListOrderByUsernamePaid(String username, Pageable Page);
	
	@Query(value = "SELECT o FROM Order o WHERE o.user.username = ?1 AND o.statusPayment.code != 'D-TT'", nativeQuery = false)
	Page<Order> getListOrderByUsernameUnPaid(String username, Pageable Page);
	
	@Query(value = "SELECT o FROM Order o WHERE o.user.username = ?1 AND o.orderCode = ?2", nativeQuery = false)
	Order findOrderByUsernameAndID(String username, String id);
	
	@Query(value = "SELECT o FROM Order o WHERE o.orderCode = ?1", nativeQuery = false)
	Order findOrderByID(String id);
}