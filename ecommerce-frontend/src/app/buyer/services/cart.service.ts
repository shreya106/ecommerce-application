import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class CartService {

  private CART_API = `${environment.apiBaseUrl}/api/buyer/cart`;
  private ORDER_API = `${environment.apiBaseUrl}/api/buyer/orders`;


  private cartCountSubject = new BehaviorSubject<number>(0);
  cartCount$ = this.cartCountSubject.asObservable();

  constructor(private http: HttpClient) {

    const saved = localStorage.getItem('cartCount');
    if (saved) {
      this.cartCountSubject.next(+saved);
    }
  }

  getCart() {
    return this.http.get<any>(this.CART_API);
  }

  addToCart(productId: number, quantity: number) {
    return this.http.post<any>(
      this.CART_API,
      null,
      { params: { productId, quantity } }
    );
  }

  updateQuantity(itemId: number, quantity: number) {
    return this.http.patch(
      `${this.CART_API}/${itemId}`,
      null,
      { params: { quantity } }
    );
  }

  removeItem(itemId: number) {
    return this.http.delete(`${this.CART_API}/${itemId}`);
    }


  incrementCount(by: number = 1) {
    const newCount = this.cartCountSubject.value + by;
    this.cartCountSubject.next(newCount);
    localStorage.setItem('cartCount', newCount.toString());
  }

  setCount(count: number) {
    this.cartCountSubject.next(count);
    localStorage.setItem('cartCount', count.toString());
  }

  resetCount() {
    this.cartCountSubject.next(0);
    localStorage.removeItem('cartCount');
  }

  loadCartCountFromBackend() {
    this.getCart().subscribe({
      next: cart => {
        const totalCount = cart.items
          ? cart.items.reduce((sum: number, item: any) => sum + item.quantity, 0)
          : 0;
  
        this.setCount(totalCount);
      },
      error: () => this.setCount(0)
    });
  }
  
}
