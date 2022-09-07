package com.deskover.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.deskover.entity.Subcategory;

import java.io.Serializable;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OrderReport implements Serializable {
    private static final long serialVersionUID = -7904327218808929032L;

    @Id
    private Subcategory subcategory;
    private Long quantity;

}
