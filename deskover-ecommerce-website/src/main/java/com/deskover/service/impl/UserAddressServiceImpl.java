package com.deskover.service.impl;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deskover.entity.UserAddress;
import com.deskover.entity.Users;
import com.deskover.reponsitory.UserAddressRepository;
import com.deskover.service.UserAddressService;
import com.deskover.service.UserService;

@Service
public class UserAddressServiceImpl implements UserAddressService {
	@Autowired
	private UserAddressRepository userAddressRepository;
	
	@Autowired
	private UserService userService;

	@Override
	public List<UserAddress> findByUsername() {
		List<UserAddress> listAddress = userAddressRepository.findByUserUsernameOrderByActivedDesc(SecurityContextHolder.getContext().getAuthentication().getName());
		if(listAddress == null) {
			throw new IllegalArgumentException("Không có địa chỉ");
		}
//		listAddress.forEach((address)->{
//			if(address.getActived()) {
//				address.setChoose(Boolean.TRUE);
//				userAddressRepository.save(address);
//			}else if (!address.getActived()) {
//				address.setChoose(Boolean.FALSE);
//				userAddressRepository.save(address);
//			}
//		});
		return listAddress;
	}

	@Override
	@Transactional
	public void changeActive(Long id) {
		
		List<UserAddress> userAddresses = userAddressRepository.findByUserUsernameOrderByActivedDesc(SecurityContextHolder.getContext().getAuthentication().getName());
		UserAddress userAddress = userAddressRepository.getById(id);
		if(userAddress == null) {
			throw new IllegalArgumentException("Không tìm thấy địa chỉ");
		}
		userAddresses.forEach((address)->{
			if(userAddress.getId() == address.getId()) {
				userAddress.setActived(Boolean.TRUE);
				userAddressRepository.saveAndFlush(userAddress);
			}else if (userAddress.getId() != address.getId()) {
				address.setActived(Boolean.FALSE);
				userAddressRepository.saveAndFlush(address);
			}
		}
		);	
	}

	@Override
	@Transactional
	public void changeChoose(Long id ) {
		List<UserAddress> userAddresses = userAddressRepository.findByUserUsernameOrderByActivedDesc(SecurityContextHolder.getContext().getAuthentication().getName());
		UserAddress userAddress = userAddressRepository.getById(id);
		if(userAddress == null) {
			throw new IllegalArgumentException("Không tìm thấy địa chỉ");
		}
		userAddresses.forEach((address)->{
			if(userAddress.getId() == address.getId()) {
				userAddress.setChoose(Boolean.TRUE);
				userAddressRepository.saveAndFlush(userAddress);
			}else if (userAddress.getId() != address.getId()) {
				address.setChoose(Boolean.FALSE);
				userAddressRepository.saveAndFlush(address);
			}
		}
		);
		
	}

	@Override
	public UserAddress findByUsernameAndChoose(Boolean choose) {
		return userAddressRepository.findByUserUsernameAndChoose(SecurityContextHolder.getContext().getAuthentication().getName(), choose);
	}

	@Override
	@Transactional
	public UserAddress doPostAddAddress(UserAddress userAddress) {
		Users user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		if(userAddressRepository.existsByTel(userAddress.getTel())) {
			throw new IllegalArgumentException("Số điện thoại đã tồn tại");
		}
		userAddress.setUser(user);
		userAddress.setActived(Boolean.FALSE);
		userAddress.setChoose(Boolean.FALSE);
		return userAddressRepository.saveAndFlush(userAddress);
	}

	@Override
	public UserAddress doPutAddAddress(@Valid UserAddress userAddress) {
		Users user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		userAddress.setUser(user);
		return userAddressRepository.saveAndFlush(userAddress);
	}


}
