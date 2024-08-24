package com.klu.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode @ToString
@Entity

public class LineItem {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long lineItemId;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id")
	private Product product =new Product();
	private double unitPrice;
	private int quantity;
	private double itemTotal;
	@ManyToOne(fetch = FetchType.EAGER)
	@JsonIgnore
	@JoinColumn(name = "cart_id")
	private Cart cart= new Cart();
	

}
