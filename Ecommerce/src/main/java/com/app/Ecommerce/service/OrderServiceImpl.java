package com.app.Ecommerce.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.app.Ecommerce.dto.OrderItemResponse;
import com.app.Ecommerce.dto.OrderResponse;
import com.app.Ecommerce.enums.OrderStatus;
import com.app.Ecommerce.exception.ResourceNotFoundException;
import com.app.Ecommerce.model.Cart;
import com.app.Ecommerce.model.CartItem;
import com.app.Ecommerce.model.OrderItem;
import com.app.Ecommerce.model.Orders;
import com.app.Ecommerce.model.Product;
import com.app.Ecommerce.model.User;
import com.app.Ecommerce.repository.CartRepository;
import com.app.Ecommerce.repository.OrderItemRepository;
import com.app.Ecommerce.repository.OrderRepository;
import com.app.Ecommerce.repository.ProductRepository;
import com.app.Ecommerce.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final CartRepository cartRepository;
	private final ProductRepository productRepository;
	private final UserRepository userRepository;
	private final StockNotificationService stockNotificationService;
	@Autowired
	private EmailService emailService;

	@Autowired
	private WhatsAppService whatsappService;
	private final SimpMessagingTemplate messagingTemplate;
	private final AdminAnalyticsService analyticsService;
	
	
	
	
	public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
			CartRepository cartRepository, ProductRepository productRepository, UserRepository userRepository,
			StockNotificationService stockNotificationService,SimpMessagingTemplate messagingTemplate,
	        AdminAnalyticsService analyticsService) {
		
		this.orderRepository = orderRepository;
		this.orderItemRepository = orderItemRepository;
		this.cartRepository = cartRepository;
		this.productRepository = productRepository;
		this.userRepository = userRepository;
		this.stockNotificationService = stockNotificationService;
		this.messagingTemplate = messagingTemplate;
	    this.analyticsService = analyticsService;
	}
	
	public void updateOrderStatus(int orderId, String status) {

    Orders order = orderRepository.findById(orderId)
        .orElseThrow(() -> new RuntimeException("Order not found"));

    OrderStatus newStatus = OrderStatus.valueOf(status);
    OrderStatus currentStatus = order.getStatus();

    //Prevent invalid transitions
    if (currentStatus == OrderStatus.DELIVERED) {
        throw new RuntimeException("Order already delivered");
    }

    if (currentStatus == OrderStatus.PENDING && newStatus != OrderStatus.SHIPPED) {
        throw new RuntimeException("Order must be shipped first");
    }

    if (currentStatus == OrderStatus.SHIPPED && newStatus != OrderStatus.DELIVERED) {
        throw new RuntimeException("Invalid status transition");
    }

    // Prevent duplicate updates
    if (currentStatus == newStatus) return;

    // ️Update status
    order.setStatus(newStatus);
    orderRepository.save(order);

    User buyer = order.getBuyer();

    // Notifications
    if (newStatus == OrderStatus.SHIPPED) {

        emailService.sendMail(
            buyer.getEmailId(),
            "Your order has been shipped ",
            "Hi " + buyer.getName() +
            ",\n\nYour order #" + order.getOrderId() + " has been shipped."
        );

        if (buyer.getPhoneNumber() != null) {
            whatsappService.sendWhatsapp(
                buyer.getPhoneNumber(),
                " Your order #" + order.getOrderId() + " has been SHIPPED!"
            );
        }
    }

    if (newStatus == OrderStatus.DELIVERED) {

        emailService.sendMail(
            buyer.getEmailId(),
            "Your order has been delivered ",
            "Hi " + buyer.getName() +
            ",\n\nYour order #" + order.getOrderId() + " has been delivered."
        );

        if (buyer.getPhoneNumber() != null) {
            whatsappService.sendWhatsapp(
                buyer.getPhoneNumber(),
                " Your order #" + order.getOrderId() + " has been DELIVERED!"
            );
        }
    }
    
    messagingTemplate.convertAndSend(
            "/topic/admin/analytics",
            analyticsService.getAnalytics()
    );
}


	@Override
	@Transactional
	public Orders checkout(String email) {
		User buyer = userRepository.findByEmailId(email)
	            .orElseThrow(() -> new ResourceNotFoundException("Buyer not found"));
	        Cart cart = cartRepository.findByUser(buyer)
	            .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
	        
	        if(cart.getItems().isEmpty())throw new RuntimeException("Cart empty");
	        
	        Orders order = new Orders();
	        order.setBuyer(buyer);
	        order.setTotal(cart.getTotal());
	        order.setTimestamp(LocalDateTime.now());
	        order.setStatus(OrderStatus.PENDING);
	        Orders saved = orderRepository.save(order);
	        stockNotificationService.trackLiveOrder(order.getOrderId(),order.getStatus().name());
	        
	        for(CartItem item: cart.getItems()) {
	        	Product p = item.getProduct();
	        	if (p.getStock() < item.getQuantity()) throw new RuntimeException("Out of stock");
	        	p.setStock(p.getStock() - item.getQuantity());
	        	
	        	productRepository.save(p);
	        	stockNotificationService.notifyStockChange(p.getProdId(),p.getStock());
	        	
	        	OrderItem oi= new OrderItem();	        
		        oi.setOrder(saved);
		        oi.setProduct(p);
		        oi.setQuantity(item.getQuantity());
		        orderItemRepository.save(oi);
		        
	        }
	        cart.getItems().clear();
	        cart.setTotal(0);
	        cartRepository.save(cart);
	        
	        messagingTemplate.convertAndSend(
	                "/topic/admin/analytics",
	                analyticsService.getAnalytics()
	            );
	        return saved;
	        
	}

	@Override
	@Transactional
	public List<Orders> getOrders(String email) {
	    return orderRepository.findByBuyerEmailIdOrderByTimestampDesc(email);
	}

	@Override
	public OrderResponse getOrderById(int id, String email) {
		Orders order = orderRepository
		        .findOrderWithItems(id, email)
		        .orElseThrow(() -> new RuntimeException("Order not found"));

		    List<OrderItemResponse> items = order.getItems()
		        .stream()
		        .map(i -> new OrderItemResponse(
		            i.getProduct().getName(),
		            i.getQuantity(),
		            i.getProduct().getImageUrl()
		            
		            
		        ))
		        .toList();

		    return new OrderResponse(
		        order.getOrderId(),
		        order.getStatus().name(),
		        order.getTotal(),
		        items
		    );
	    
	}

	

	
}
