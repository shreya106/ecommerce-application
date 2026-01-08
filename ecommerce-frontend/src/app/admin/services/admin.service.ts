import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AdminService {

  private API = `${environment.apiBaseUrl}/api/admin`;

  constructor(private http: HttpClient) {}

  // dashboard
  getDashboard() {
    return this.http.get<any>(`${this.API}/dashboard`);
  }

  getAnalytics() {
    return this.http.get<any>(`${this.API}/analytics`);
  }
  // users
  getUsers() {
    return this.http.get<any[]>(`${this.API}/users`);
  }

  deleteUser(id: number) {
    return this.http.delete(`${this.API}/users/${id}`);
  }

  // products
  getProducts() {
    return this.http.get<any[]>(`${this.API}/products`);
  }

  toggleProduct(id: number) {
    return this.http.put(`${this.API}/products/${id}/toggle`, {});
  }

  // orders
  getOrders() {
    return this.http.get<any[]>(`${this.API}/orders`);
  }

  updateOrderStatus(id: number, status: string) {
    return this.http.put(`${this.API}/orders/${id}/status/${status}`, {});
  }
  toggleUser(id: number) {
    return this.http.put(`${this.API}/users/${id}/toggle`, {});
  }
}
