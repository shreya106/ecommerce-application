package com.app.Ecommerce.dto;

import lombok.Data;

@Data
public class AdminUserResponse {
	private int userId;
    private String name;
    private String email;
    private String role;
    private boolean verified;
}
