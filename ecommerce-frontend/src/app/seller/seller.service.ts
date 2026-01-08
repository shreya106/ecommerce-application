import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class SellerService {
  private API = `${environment.apiBaseUrl}/api/seller/products`;

  constructor(private http: HttpClient) {}

  // -------------------------
  // GET PRODUCTS
  // -------------------------
  getProducts() {
    return this.http.get<any[]>(this.API);
  }

  getProductById(id: number) {
    return this.http.get<any>(`${this.API}/${id}`);
  }

  // -------------------------
  // CREATE (WITH IMAGE)
  // -------------------------
  createProduct(formData: FormData) {
    return this.http.post(this.API, formData);
  }

  // -------------------------
  // UPDATE (JSON ONLY - legacy)
  // -------------------------
  updateProduct(id: number, data: any) {
    return this.http.put(`${this.API}/${id}`, data);
  }

  // -------------------------
  // UPDATE (WITH IMAGE)
  // -------------------------
  updateProductWithImage(id: number, formData: FormData) {
    return this.http.put(`${this.API}/${id}`, formData);
  }

  // -------------------------
  // DELETE / RESTORE
  // -------------------------
  deleteProduct(id: number) {
    return this.http.delete(`${this.API}/${id}`);
  }

  restoreProduct(id: number) {
    return this.http.patch(`${this.API}/${id}/restore`, {});
  }

  // -------------------------
  // STOCK
  // -------------------------
  updateStock(id: number, stock: number) {
    return this.http.patch(
      `${this.API}/${id}/stock?stock=${stock}`,
      {}
    );
  }
}
