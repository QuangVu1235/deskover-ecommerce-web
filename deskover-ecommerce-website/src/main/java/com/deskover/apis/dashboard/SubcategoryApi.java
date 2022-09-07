package com.deskover.apis.dashboard;

import com.deskover.dto.MessageResponse;
import com.deskover.entity.Subcategory;
import com.deskover.service.SubcategoryService;
import com.deskover.utils.ValidationUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController("SubcategoryApiForAdmin")
@CrossOrigin("*")
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SELLER')")
@RequestMapping("v1/api/admin")
public class SubcategoryApi {
    @Autowired
    SubcategoryService subcategoryService;

    @GetMapping("/subcategories")
    public ResponseEntity<?> doGetByActiveAndCategory(
            @RequestParam("categoryId") Optional<Long> categoryId,
            @RequestParam("isActive") Optional<Boolean> isActive
    ) {
        List<Subcategory> subcategory = subcategoryService.getAll(
                isActive.orElse(null),
                categoryId.orElse(null)
        );
        return ResponseEntity.ok(Objects.requireNonNullElseGet(subcategory, () -> new MessageResponse("Subcategory not found")));
    }

    @GetMapping("/subcategories/{id}")
    public ResponseEntity<?> doGetById(@PathVariable("id") Long id) {
        Subcategory subcategory = subcategoryService.getById(id);
        return ResponseEntity.ok(Objects.requireNonNullElseGet(subcategory, () -> new MessageResponse("Subcategory not found")));
    }

    @PostMapping("/subcategories/datatables")
    public ResponseEntity<?> doGetByActiveForDatatablesTest(@Valid @RequestBody DataTablesInput input,
                                                            @RequestParam("isActive") Optional<Boolean> isActive,
                                                            @RequestParam("categoryId") Optional<Long> categoryId) {
        return ResponseEntity.ok(subcategoryService.getByActiveForDatatables(input, isActive.orElse(Boolean.TRUE),
                categoryId.orElse(null)));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/subcategories")
    public ResponseEntity<?> doPostCreate(@Valid @RequestBody Subcategory subcategory, BindingResult result) {
        if (result.hasErrors()) {
            MessageResponse errors = ValidationUtil.ConvertValidationErrors(result);
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            subcategoryService.create(subcategory);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/subcategories")
    public ResponseEntity<?> updateSubcategory(@RequestBody Subcategory subcategory) {
        try {
            subcategoryService.update(subcategory);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/subcategories/{id}")
    public ResponseEntity<?> doChangeActive(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(subcategoryService.changeActive(id));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

}
