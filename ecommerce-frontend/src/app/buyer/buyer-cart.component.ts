import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CartService } from './services/cart.service';
import { Router } from '@angular/router';

@Component({
  standalone: true,
  imports: [CommonModule],
  selector: 'app-buyer-cart',
  template: `
    <div class="page-container">
      <div class="card">

        <h2 class="title">Your Cart</h2>

        <div *ngIf="cart?.items?.length; else empty">

          <div class="item-card" *ngFor="let item of cart.items">

            <!-- PRODUCT IMAGE -->
            <div class="image-wrapper">
              <img
                [src]="item.imageUrl"
                alt="{{ item.productName }}"
                loading="lazy"
              />
            </div>

            <!-- PRODUCT INFO -->
            <div class="item-info">
              <strong class="name">{{ item.productName }}</strong>
              <span class="meta">Qty: {{ item.quantity }}</span>
             <span class="meta">Price: {{ item.price | currency:'USD':'symbol':'1.0-0' }}</span>
            </div>

            <!-- ACTIONS -->
            <div class="actions">
              <button
                class="qty-btn"
                (click)="update(item.cartItemId, item.quantity + 1)">+</button>

              <button
                class="qty-btn"
                (click)="update(item.cartItemId, item.quantity - 1)">−</button>

              <button
                class="remove-btn"
                (click)="remove(item.cartItemId)">Remove</button>
            </div>

          </div>

          <div class="total">
  Total: {{ cart.total | currency:'USD':'symbol':'1.0-0' }}
</div>

<button
  class="home-btn"
  type="button"
  (click)="goToHome()">
  ← Back to Shop
</button>
          <button
            class="checkout-btn"
            type="button"
            (click)="checkout()">
            Checkout
          </button>

        </div>

        <ng-template #empty>
          <p class="empty">Your cart is empty.</p>
        </ng-template>

      </div>
    </div>
  `,
  styles: [`
    .page-container {
      min-height: 100vh;
      display: flex;
      justify-content: center;
      align-items: center;
      background:
        radial-gradient(circle at top left, #c7d2fe, transparent 40%),
        radial-gradient(circle at bottom right, #ddd6fe, transparent 40%),
        linear-gradient(135deg, #eef2ff, #f8fafc);
      padding: 24px;
    }

    .card {
      width: 100%;
      max-width: 760px;
      background: rgba(255, 255, 255, 0.9);
      border-radius: 20px;
      padding: 32px;
      box-shadow: 0 40px 80px rgba(0, 0, 0, 0.12);
    }

    .title {
      text-align: center;
      font-size: 26px;
      font-weight: 700;
      color: #3730a3;
      margin-bottom: 28px;
    }

    .item-card {
      background: #f8f9fc;
      border-radius: 16px;
      padding: 16px;
      margin-bottom: 16px;
      display: flex;
      align-items: center;
      gap: 16px;
    }

    /* IMAGE */
    .image-wrapper {
      width: 90px;
      height: 90px;
      background: #eef2ff;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      flex-shrink: 0;
    }

    .image-wrapper img {
      max-width: 100%;
      max-height: 100%;
      object-fit: contain;
      border-radius: 8px;
    }

    .item-info {
      display: flex;
      flex-direction: column;
      gap: 4px;
      flex: 1;
    }

    .name {
      font-size: 15px;
      font-weight: 600;
      color: #1e1b4b;
    }

    .meta {
      font-size: 13px;
      color: #475569;
    }

    .actions {
      display: flex;
      gap: 8px;
      align-items: center;
    }

    .qty-btn {
      width: 34px;
      height: 34px;
      border-radius: 10px;
      border: none;
      background: #e0e7ff;
      color: #3730a3;
      font-size: 16px;
      font-weight: 700;
      cursor: pointer;
    }

    .qty-btn:hover {
      background: #c7d2fe;
    }

    .remove-btn {
      padding: 6px 12px;
      border-radius: 10px;
      border: none;
      background: #fee2e2;
      color: #991b1b;
      font-weight: 600;
      cursor: pointer;
    }

    .remove-btn:hover {
      background: #fecaca;
    }

    .total {
      text-align: right;
      font-size: 18px;
      font-weight: 600;
      margin-top: 18px;
      margin-bottom: 18px;
      color: #1f2937;
    }

    .checkout-btn {
      width: 100%;
      padding: 14px;
      border-radius: 14px;
      border: none;
      font-size: 16px;
      font-weight: 600;
      background: #e0e7ff;
      color: #3730a3;
      cursor: pointer;
    }

    .checkout-btn:hover {
      background: #c7d2fe;
    }

    .empty {
      text-align: center;
      font-size: 15px;
      color: #6b7280;
    }

    @media (max-width: 640px) {
      .item-card {
        flex-direction: column;
        align-items: flex-start;
      }

      .actions {
        width: 100%;
        justify-content: flex-start;
      }
    }
      .home-btn {
  width: 100%;
  padding: 12px;
  margin-bottom: 12px;
  border-radius: 14px;
  border: none;
  font-size: 15px;
  font-weight: 600;
  background: #f1f5f9;
  color: #1e293b;
  cursor: pointer;
}

.home-btn:hover {
  background: #e2e8f0;
}

  `]
})
export class BuyerCartComponent implements OnInit {
  cart: any;

  constructor(
    private cartService: CartService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loadCart();
  }

  loadCart() {
    this.cartService.getCart().subscribe(res => this.cart = res);
  }

  update(itemId: number, qty: number) {
    if (qty <= 0) return;
    this.cartService.updateQuantity(itemId, qty).subscribe(() => this.loadCart());
  }

  remove(itemId: number) {
    this.cartService.removeItem(itemId).subscribe(() => this.loadCart());
  }

  checkout() {
    
    this.router.navigate(['/buyer/checkout/address']);
  }
  goToHome() {
    this.router.navigate(['/buyer/products']);
  }
}
