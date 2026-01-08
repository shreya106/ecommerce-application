			package com.app.Ecommerce.controller;
			
			import java.util.Optional;
			
			import org.springframework.beans.factory.annotation.Value;
			import org.springframework.http.HttpStatus;
			import org.springframework.http.ResponseEntity;
			import org.springframework.web.bind.annotation.PostMapping;
			import org.springframework.web.bind.annotation.RequestBody;
			import org.springframework.web.bind.annotation.RequestHeader;
			import org.springframework.web.bind.annotation.RequestMapping;
			import org.springframework.web.bind.annotation.RestController;
			
			import com.app.Ecommerce.enums.OrderStatus;
			import com.app.Ecommerce.model.Cart;
			import com.app.Ecommerce.model.OrderItem;
			import com.app.Ecommerce.model.Orders;
			import com.app.Ecommerce.model.Product;
			import com.app.Ecommerce.repository.CartRepository;
			import com.app.Ecommerce.repository.OrderRepository;
			import com.app.Ecommerce.repository.ProductRepository;
			import com.app.Ecommerce.service.EmailService;
			import com.app.Ecommerce.service.WhatsAppService;
			import com.stripe.exception.SignatureVerificationException;
			import com.stripe.model.Event;
			import com.stripe.model.checkout.Session;
			import com.stripe.net.Webhook;
			
			import jakarta.transaction.Transactional;
			
			@RestController
			@RequestMapping("/api/webhook")
			public class StripeWebhookController {
			
			    @Value("${stripe.webhook.secret}")
			    private String endpointSecret; 
			  
			    private final OrderRepository orderRepository;
			    private final EmailService emailService;
			    private final WhatsAppService whatsappService;
			    private final CartRepository cartRepository;
			    private final ProductRepository productRepository;
			    
			    
			    public StripeWebhookController(OrderRepository orderRepository, EmailService emailService,
						WhatsAppService whatsappService, CartRepository cartRepository, ProductRepository productRepository) {
					
					this.orderRepository = orderRepository;
					this.emailService = emailService;
					this.whatsappService = whatsappService;
					this.cartRepository = cartRepository;
					this.productRepository = productRepository;
				}
			
				
			
			    @PostMapping
			    public ResponseEntity<String> handleStripeWebhook(
			            @RequestBody String payload,
			            @RequestHeader("Stripe-Signature") String sigHeader) {
			
			        Event event = null;
			        
			
			        try {
			            // Verify Stripe signature to ensure webhook authenticity
			            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
			        } catch (SignatureVerificationException e) {
			            System.out.println("Webhook Signature verification failed: " + e.getMessage());
			            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
			        } catch (Exception e) {
			            e.printStackTrace();
			            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Webhook error");
			        }
			
			        if (event == null) {
			            System.out.println("️Event object is null — skipping processing.");
			            return ResponseEntity.ok("No event to process");
			        }
			
			        System.out.println("Received event type: " + event.getType());
			       
			
			        switch (event.getType()) {
			            case "checkout.session.completed":
			                handleCheckoutSessionCompleted(event);
			                break;
			
			            case "checkout.session.expired":
			                System.out.println("Checkout session expired.");
			                break;
			
			            default:
			                System.out.println("Unhandled event type: " + event.getType());
			                break;
			        }
			
			        return ResponseEntity.ok("Webhook processed");
			    }
			
			    @Transactional
			    private void handleCheckoutSessionCompleted(Event event) {
			        try {
			            Session session = (Session) event.getDataObjectDeserializer()
			                    .getObject()
			                    .orElse(null);
			
			            if (session == null) {
			                System.out.println("Session object is null in checkout.session.completed event.");
			                return;
			            }
			
			            String sessionId = session.getId();
			            String email = (session.getCustomerDetails() != null)
			                    ? session.getCustomerDetails().getEmail()
			                    : "Unknown";
			
			            System.out.println("Payment successful for session: " + sessionId + " | Email: " + email);
			
			            Optional<Orders> optionalOrder = orderRepository.findBySessionId(sessionId);
			
			            if (optionalOrder.isPresent()) {
			                Orders order = optionalOrder.get();
			                if (order.getStatus() != OrderStatus.COMPLETE) {
			                order.setStatus(OrderStatus.COMPLETE);
			                orderRepository.save(order);
			                for (OrderItem item : order.getItems()) {
			                    Product product = item.getProduct();
			
			                    int updatedStock = product.getStock() - item.getQuantity();
			
			                    if (updatedStock < 0) {
			                        throw new RuntimeException(
			                            "Stock inconsistency for product " + product.getName()
			                        );
			                    }
			
			                    product.setStock(updatedStock);
			                    productRepository.save(product);
			                }
							Cart cart = cartRepository.findByUser(order.getBuyer())
			                        .orElseThrow(() -> new RuntimeException("Cart not found"));
			
			                    cart.getItems().clear();
			                    cart.setTotal(0);
			                    cartRepository.save(cart);
			                System.out.println("Order " + order.getOrderId() + " marked as COMPLETE.");
			                emailService.sendMail(order.getBuyer().getEmailId(), "Payment Successful - Order Completed",
			                		"Hi " + order.getBuyer().getName() + ",\n\nYour payment was successful! " +
			                		        "Order #" + order.getOrderId() + " is now confirmed.");
			                
			                whatsappService.sendWhatsapp(order.getBuyer().getPhoneNumber(), 
			
			                		"Your Order #"+order.getOrderId()+ " has been placed successfully.\nThank you for shopping with us!");
			                }  
			            } else {
			                System.out.println("No order found for session " + sessionId);
			            }
			
			        } catch (Exception e) {
			            System.out.println("Error processing checkout.session.completed: " + e.getMessage());
			            e.printStackTrace();
			        }
			    }
			}
