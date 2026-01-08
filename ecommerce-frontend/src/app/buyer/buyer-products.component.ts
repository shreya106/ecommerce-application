import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BuyerService } from './buyer.service';
import { CartService } from './services/cart.service';
import { Router } from '@angular/router';
import { Client } from '@stomp/stompjs';
import  SockJS  from 'sockjs-client';
import { Observable } from 'rxjs';

@Component({
  standalone: true,
  selector: 'app-buyer-products',
  imports: [CommonModule], 
  templateUrl: './buyer-products.component.html',
  styleUrls: ['./buyer-products.component.scss']
})
export class BuyerProductsComponent implements OnInit {

  products: any[] = [];
  private stompClient!: Client;
  cartCount$!: Observable<number>;
 


  constructor(
    private buyerService: BuyerService,
    private cartService: CartService,
    private router: Router
  ) {}

  
  ngOnInit(): void {
    this.cartCount$ = this.cartService.cartCount$;
    this.cartService.loadCartCountFromBackend();
    this.buyerService.getProducts().subscribe({
      next: res => {
        this.products = res;
        this.connectToStockUpdates();
      },
      error: err => console.error(err)
    });
  }

  connectToStockUpdates() {
    this.stompClient = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws-stock'),
      reconnectDelay: 5000
    });
  
    this.stompClient.onConnect = () => {
      console.log(' WebSocket connected');
  
      this.subscribeToAllProducts();
    };
  
    this.stompClient.activate();
  }

  subscribeToAllProducts() {
    this.products.forEach(product => {
      this.stompClient.subscribe(
        `/topic/products/${product.id}/stock`,
        message => {
          const data = JSON.parse(message.body);
  
          const p = this.products.find(x => x.id === data.productId);
          if (p) {
            p.stock = data.newStock;
          }
        }
      );
    });
  }
  
  
  
  addToCart(productId: number) {
    this.cartService.addToCart(productId, 1).subscribe({
      next: res => {
        this.cartService.incrementCount();
        console.log('Added to cart', res);
      },
      error: err => console.error(err)
    });
  }
  

  goToCart() {
    this.router.navigate(['/buyer/cart']);
  }

  goToOrders() {
    this.router.navigate(['/buyer/orders']);
  }

  logout() {
    localStorage.removeItem('token');  
    this.router.navigate(['/auth/login']);
  }
}
