	package com.app.Ecommerce.model;
	
	import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
	
	@Getter
	@Setter
	@NoArgsConstructor
	@Entity
	@Table(name = "order_items")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	public class OrderItem {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "order_item_id")
	    private int orderItemId;
	
	    @ManyToOne
	    @JoinColumn(name = "order_id", nullable = false)
	    @JsonIgnore
	    private Orders order;
	
	    @ManyToOne
	    @JoinColumn(name = "product_id", nullable = false)
	    @JsonIgnoreProperties({"seller", "stock"})
	    private Product product;
	
	    @Column(nullable = false)
	    private int quantity;
	    
	}