package com.deskover.controller.rest.api.dashboard;

import com.deskover.model.entity.database.AdminRole;
import com.deskover.model.entity.database.Administrator;
import com.deskover.model.entity.dto.AdminCreateDto;
import com.deskover.model.entity.dto.AdministratorDto;
import com.deskover.model.entity.dto.ChangePasswordDto;
import com.deskover.model.entity.dto.security.payload.MessageResponse;
import com.deskover.other.util.ValidationUtil;
import com.deskover.service.AdminAuthorityService;
import com.deskover.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("v1/api/admin/users")
public class AdministratorApi {
	@Autowired
	AdminService adminService;

	@Autowired
	AdminAuthorityService adminAuthorityService;
	@GetMapping()
	public ResponseEntity<?> doGetIsActived(@RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size, @RequestParam("isActive") Optional<Boolean> isActive) {
		Page<Administrator> Admins = adminService.getByActived(isActive.orElse(Boolean.TRUE), page.orElse(0),
				size.orElse(1));
		if (Admins.isEmpty()) {
			return ResponseEntity.ok(new MessageResponse("Not Found Category Activated"));
		}
		return ResponseEntity.ok(Admins);
	}

	@GetMapping("/actived")
    public ResponseEntity<?> doGetAllActive() {
        List<Administrator> admins = adminService.getByActived(Boolean.TRUE);
        if (admins.isEmpty()) {
            return ResponseEntity.ok(new MessageResponse("Not Found Category Activated"));
        }
        return ResponseEntity.ok(admins);
    }

	@GetMapping("/{id}")
	public ResponseEntity<?> doGetProfile(@PathVariable("id") Long id) {
		try {
			Administrator admin = adminService.getById(id);
			if (admin == null) {
				return ResponseEntity.badRequest().body(new MessageResponse("Administrator not found"));
			}
			return ResponseEntity.ok(admin);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage(),e);
		}
		
	
	}

	@PostMapping()
	public ResponseEntity<?> doCreate(@Valid @RequestBody AdminCreateDto admin, BindingResult result) {
		if (result.hasErrors()) {
			MessageResponse errors = ValidationUtil.ConvertValidationErrors(result);
			return ResponseEntity.badRequest().body(errors);
		}
		try {
			AdministratorDto adminCreated = adminService.create(admin);
			return ResponseEntity.ok().body(adminCreated);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping()
	public ResponseEntity<?> doUpdate(@Valid @RequestBody Administrator admin, BindingResult result) {
		if (result.hasErrors()) {
			MessageResponse errors = ValidationUtil.ConvertValidationErrors(result);
			return ResponseEntity.badRequest().body(errors);
		}
		try {
			Administrator adminUpdated = adminService.save(admin);
			return ResponseEntity.ok().body(adminUpdated);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
		}
	}

	@PutMapping("/password")
	public ResponseEntity<?> doUpdatePassword(@Valid @RequestBody ChangePasswordDto admin, BindingResult result) {
		if (result.hasErrors()) {
			MessageResponse errors = ValidationUtil.ConvertValidationErrors(result);
			return ResponseEntity.badRequest().body(errors);
		}
		try {
			AdministratorDto adminUpdated = adminService.updatePassword(admin);
			return ResponseEntity.ok().body(adminUpdated);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage(),e);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> doChangeActive(@PathVariable("id") Long id) {
		try {
			adminService.changeActived(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	}

	@PostMapping("/authority")
	public ResponseEntity<?> doChangeRole(
			@RequestParam(value = "adminId", required = true) Long adminId,
			@RequestParam(value = "roleId", required = true) Long roleId
	) {
		try {
			adminAuthorityService.changeRole(adminId, roleId);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	}

	@GetMapping("/roles")
	public List<AdminRole> getAllRole() {
		return adminService.getAllRole();
	}


	@PostMapping("/datatables")
	public ResponseEntity<?> doGetForDatatablesByActive(
			@Valid @RequestBody DataTablesInput input,
			@RequestParam Optional<Boolean> isActive,
			@RequestParam Optional<Long> roleId
	) {
		return ResponseEntity.ok(adminService.getByActiveForDatatables(
				input,
				isActive.orElse(Boolean.TRUE),
				roleId.orElse(null)
		));
	}


}
