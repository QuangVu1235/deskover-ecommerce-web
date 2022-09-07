package com.deskover.apis.dashboard;

import com.deskover.dto.MessageResponse;
import com.deskover.dto.application.DataOrderResquest;
import com.deskover.dto.application.DataTotaPrice7DaysAgo;
import com.deskover.dto.application.OrderDto;
import com.deskover.entity.Order;
import com.deskover.entity.OrderStatus;
import com.deskover.entity.PaymentMethods;
import com.deskover.entity.ShippingMethods;
import com.deskover.reponsitory.OrderRepository;
import com.deskover.service.OrderService;
import com.deskover.utils.DecimalFormatUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController("OrderApiForAdmin")
@CrossOrigin("*")
@PreAuthorize("hasAnyRole('ADMIN', 'SHIPPER')")
@RequestMapping("v1/api/admin")
public class OrderApi {
	@Autowired
	private OrderService orderService;
	
	@Autowired OrderRepository orderRepository;
	
	
	/*
	 * 1 Chờ xác nhận
	 * 2 Xác nhận đơn hàng
	 * 3 Chờ lấy hàng
	 * 4 Lấy hàng thành công
	 * 5 Lấy hàng không thành công
	 * 6 Đang giao
	 * 7 Đã giao
	 * 8 Giao hàng không thành công
	 * 9.Huỷ đơn
	 */


	@GetMapping("/orders/statuses")
	public List<OrderStatus> doGetAllOrderStatus() {
		return orderService.getAllOrderStatus();
	}

	@GetMapping("/orders/payments")
	public List<PaymentMethods> doGetAllPayment() {
		return orderService.getAllPayment();
	}

	@GetMapping("/orders/shipping-units")
	public List<ShippingMethods> doGetAllShippingUnit() {
		return orderService.getAllShippingUnit();
	}

	@GetMapping("/orders")
	public ResponseEntity<?> doGetOrders(@RequestParam("status") String status) {
		List<Order> orders = orderService.getAllOrderByStatus(status.toUpperCase());
		if (orders.isEmpty()) {
			ResponseEntity.notFound().build();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Không tìm thấy đơn hàng"));
		}
		return ResponseEntity.ok(orders);
	}

	@PostMapping("/orders/datatables")
	public ResponseEntity<?> doPostOrdersForDatatables(
			@Valid @RequestBody DataTablesInput input,
			@RequestParam Optional<String> statusCode) {
		try {
			DataTablesOutput<Order> orders = orderService.getAllForDatatables(
					input,
					statusCode.orElse(null)
			);
			return ResponseEntity.ok(orders);
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/orders/{orderCode}")
	public ResponseEntity<?> doGetOrderByOrderCode(@PathVariable("orderCode") String orderCode,
					@RequestParam("status") String status){
		try {
			if(status.isBlank()) {
				OrderDto orderDto = orderService.findByCode(orderCode);
				return ResponseEntity.ok(orderDto);
			}
			OrderDto orderDto = orderService.getByOrderCode(orderCode, status);
			return ResponseEntity.ok(orderDto);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Không tìm thấy đơn hàng"));
		}
	}
	
	@PreAuthorize("hasRole('SHIPPER')")
	@GetMapping("/orders/delivery")
	public ResponseEntity<?> doGetDelivery(@RequestParam("status") String status){
		try {
			DataOrderResquest dtos = orderService.getListOrder(status);
			return ResponseEntity.ok(dtos);

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Không tìm thấy đơn hàng"));
		}
	}
	
	@PreAuthorize("hasRole('SHIPPER')")
	@GetMapping("/orders/statistical")
	public ResponseEntity<?> doGetAllByUser(){
		try {
			DataOrderResquest dtos = orderService.getListOrderByUser();
			return ResponseEntity.ok(dtos);

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Không tìm thấy đơn hàng"));
		}
	}
	
	@PreAuthorize("hasRole('SHIPPER')")
	@GetMapping("/orders-7days")
	public ResponseEntity<?> doGetTotalPrice7DaysAgo(){
			try {
				DataTotaPrice7DaysAgo totals = orderService.doGetTotalPrice7DaysAgo();
				return ResponseEntity.ok(totals);
			} catch (Exception e) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage(), e);
			}
    } 
	
	@PreAuthorize("hasRole('SHIPPER')")
	@GetMapping("/orders-total-per-month")
	public ResponseEntity<?> doGetPrice() {
		try {
			
			String order = orderService.getToTalPricePerMonth();
			return ResponseEntity.ok(DecimalFormatUtil.FormatDecical(order));

		} catch (Exception e) {
			return ResponseEntity.ok("0");
		}
	}
	
	@PreAuthorize("hasRole('SHIPPER')")
	@GetMapping("/orders-count-order-per-month")
	public ResponseEntity<?> doGetCountOrder(){
		try {
			String countOrder = orderService.getCountOrderPerMonth();
			return ResponseEntity.ok(DecimalFormatUtil.FormatDecical(countOrder));
			
		} catch (Exception e) {
			return ResponseEntity.ok("0");
		}
	}

	@PreAuthorize("hasAnyRole('ADMIN','SHIPPER')")
	@PostMapping("/orders/{orderCode}")
	public ResponseEntity<?> doPostPickup(@PathVariable("orderCode") String orderCode,@RequestParam("status") String status,
			@RequestParam("note") String note){
		try {
			 orderService.pickupOrder(orderCode,status,note);
			 return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Cập nhập đơn hàng thất bại"));
		}
	}

	@PreAuthorize("hasAnyRole('ADMIN','SHIPPER')")
	@PostMapping("/orders/change-status-code/{orderCode}")
	public ResponseEntity<?> changeOrderStatusCode(@PathVariable("orderCode") String orderCode){
		try {
			 return ResponseEntity.ok(orderService.changeOrderStatusCode(orderCode));
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage(),e);
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/orders/cancel/{orderCode}")
	public ResponseEntity<?> cancelOrder(@PathVariable String orderCode){
		try {
			orderService.cancelOrder(orderCode);
			return ResponseEntity.ok(new MessageResponse("Hủy đơn hàng thành công"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/orders/refund/{orderCode}")
	public ResponseEntity<?> refundMoney(@PathVariable String orderCode){
		try {
			orderService.refundMoney(orderCode);
			return ResponseEntity.ok(new MessageResponse("Hoàn tiền thành công"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/orders/count-by-status/{orderStatusCode}")
	public ResponseEntity<?> countByStatus(@PathVariable String orderStatusCode){
		try {
			return ResponseEntity.ok(orderService.countByStatus(orderStatusCode));
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage(),e);
		}
	}
}
