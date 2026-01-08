import { Injectable } from '@angular/core'; 
import { HttpClient } from '@angular/common/http';
@Injectable({ providedIn: 'root' })
export class SellerOrdersService {

  constructor(private http: HttpClient) {}

  getOrders() {
    return this.http.get<any[]>('/api/seller/orders');
  }

  updateStatus(orderId: number, status: string) {
    return this.http.patch(
      `/api/seller/orders/${orderId}/status?status=${status}`,
      {}
    );
  }
}
