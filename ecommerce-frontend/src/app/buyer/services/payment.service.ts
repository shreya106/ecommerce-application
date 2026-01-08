import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
@Injectable({ providedIn: 'root' })
export class PaymentService {
  private API = `${environment.apiBaseUrl}/api/payment`;

  constructor(private http: HttpClient) {}

  createCheckoutSession() {
    return this.http.post<any>(
      `${this.API}/create-checkout-session`,
      {}
    );
  }
}
