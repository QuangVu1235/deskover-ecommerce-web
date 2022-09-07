package com.deskover.configuration.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.deskover.configuration.userdetail.UsersDetailsService;
import com.deskover.dto.MessageResponse;
import com.deskover.dto.login.JwtRequest;
import com.deskover.service.UserService;
import com.deskover.utils.JwtTokenUtil;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/v1/api/customer/auth")
public class ApplicationAuthenticationController {
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private UsersDetailsService usersDetailsService;

	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
		try {
			authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		} catch (DisabledException e) {
			return  ResponseEntity.badRequest().body(new MessageResponse("Tài khoản đã bị khoá")) ;
		} catch (BadCredentialsException e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Tài khoản hoặc mật khẩu không đúng")) ;
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Lỗi hệ thống")) ;
		}

		final UserDetails userDetails = usersDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);
		return ResponseEntity.ok( new MessageResponse(token));
	}
		
	private void authenticate(String username, String password) throws Exception {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
	}

	@GetMapping("/get-principal")
	public ResponseEntity<?> getProfile() {
		System.out.println(SecurityContextHolder.getContext().getAuthentication());
		return ResponseEntity.ok(userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
	}
}
