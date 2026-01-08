package com.app.Ecommerce.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class StockNotificationService {
	 private final SimpMessagingTemplate messagingTemplate;

	    public StockNotificationService(SimpMessagingTemplate messagingTemplate) {
	        this.messagingTemplate = messagingTemplate;
	    }

	    public void notifyStockChange(int productId, int newStock) {
	        String destination = "/topic/products/" + productId + "/stock";
//	        messagingTemplate.convertAndSend(destination, newStock);
	        Map<String, Object> payload = new HashMap<>();
	        payload.put("productId", productId);
	        payload.put("newStock", newStock);
	        System.out.println("📡 Sending WS event to " + destination + " -> " + payload);
	        messagingTemplate.convertAndSend(destination, payload);
	    }
	    public void trackLiveOrder(int orderId, String status) {
	    	String destination ="/topic/orders/"+orderId+ "/status";
	    	Map<String, Object> payload = new HashMap<>();
	        payload.put("orderId", orderId);
	        payload.put("status", status);
	    	messagingTemplate.convertAndSend(destination, payload);
	    	
	    }
}
