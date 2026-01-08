import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class AdminOrdersService {

  constructor(private http: HttpClient) {}

  getOrders() {
    return this.http.get<any[]>('/admin/orders');
  }

  getOrderById(id: number) {
    return this.http.get<any>(`/admin/orders/${id}`);
  }

  updateStatus(orderId: number, status: string) {
    return this.http.put(
      `/admin/orders/${orderId}/status/${status}`,
      {}
    );
  }
}
