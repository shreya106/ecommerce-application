import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class BuyerService {
  private API = `${environment.apiBaseUrl}/api/buyer/product`;

  constructor(private http: HttpClient) {}

  getProducts() {
    return this.http.get<any[]>(`${this.API}/products`);
  }
}
