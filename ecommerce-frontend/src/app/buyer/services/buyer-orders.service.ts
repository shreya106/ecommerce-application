import { Injectable } from '@angular/core';   
import { environment } from '../../../environments/environment';  
import { HttpClient } from '@angular/common/http';
@Injectable({ providedIn: 'root' })
export class BuyerOrdersService {

  private API = `${environment.apiBaseUrl}/api/buyer/orders`;

  constructor(private http: HttpClient) {}

  getOrders() {
    return this.http.get<any[]>(this.API);
  }

  getOrderById(id: number) {
    return this.http.get<any>(`${this.API}/${id}`);
    console.log(this.API);
  }

  checkout() {
    return this.http.post<any>(`${this.API}/checkout`, {});
  }
}
