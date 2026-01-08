import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { SellerOrdersService } from './services/seller-orders.service';

@Component({
  standalone: true,
  imports: [CommonModule],
  selector: 'app-seller-orders',
  template: `
    <div class="page-container">
      <div class="card">

        <!-- TOP BAR -->
        <div class="top-bar">
          <h2 class="title">Seller Orders</h2>

          <div class="actions">
            <button class="nav-btn" (click)="goDashboard()"> Dashboard</button>
            <button class="nav-btn" (click)="goAdd()">Add Product</button>
            <button class="nav-btn" (click)="goProducts()">Edit Products</button>
          </div>
        </div>

        <!-- ORDERS -->
        <div class="orders-list">
          <div class="order-card" *ngFor="let o of orders">

            <h3 class="order-id">Order #{{ o.orderId }}</h3>
            <p class="status">Status: {{ o.status }}</p>

            <!-- BUTTONS KEPT EXACTLY AS IS -->
            <button *ngIf="o.status === 'PENDING'" (click)="ship(o.orderId)">
              Mark as Shipped
            </button>

            <button *ngIf="o.status === 'SHIPPED'" (click)="deliver(o.orderId)">
              Mark as Delivered
            </button>

          </div>
        </div>

        <p *ngIf="orders.length === 0" class="empty">
          No orders yet.
        </p>

      </div>
    </div>
  `,
  styles: [`
    .page-container {
      min-height: 100vh;
      display: flex;
      justify-content: center;
      align-items: center;
      background: #f5f6fa;
      padding: 20px;
    }

    .card {
      width: 100%;
      max-width: 900px;
      background: #ffffff;
      padding: 30px;
      border-radius: 14px;
      box-shadow: 0 10px 25px rgba(0,0,0,0.08);
    }

    /* TOP BAR */
    .top-bar {
      display: flex;
      justify-content: space-between;
      align-items: center;
      gap: 16px;
      margin-bottom: 24px;
      flex-wrap: wrap;
    }

    .title {
      font-size: 26px;
      font-weight: 600;
      margin: 0;
    }

    .actions {
      display: flex;
      gap: 10px;
      flex-wrap: wrap;
    }

    .nav-btn {
  padding: 8px 14px;
  border-radius: 10px;
  border: none;
  background: #ede9fe;      
  color: #4c1d95;           
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
}

.nav-btn:hover {
  background: #ddd6fe;
}

    /* ORDERS */
    .orders-list {
      display: flex;
      flex-direction: column;
      gap: 16px;
    }

    .order-card {
      background: #f8f9fc;
      padding: 18px;
      border-radius: 12px;
    }

    .order-id {
      margin: 0 0 6px;
      font-size: 18px;
      font-weight: 600;
    }

    .status {
      margin-bottom: 14px;
      font-size: 14px;
      color: #555;
    }

    button {
      padding: 8px 16px;
      border: none;
      border-radius: 8px;
      font-size: 14px;
      font-weight: 500;
      cursor: pointer;
      margin-right: 10px;
      background: #2563eb;
      color: #fff;
    }

    button:last-child {
      margin-right: 0;
    }

    button:hover {
      opacity: 0.9;
    }

    .empty {
      text-align: center;
      margin-top: 20px;
      color: #6b7280;
    }

    @media (max-width: 600px) {
      .actions {
        width: 100%;
      }

      .nav-btn {
        flex: 1;
      }

      button {
        width: 100%;
        margin-bottom: 8px;
      }
    }
  `]
})
export class SellerOrdersComponent implements OnInit {

  orders: any[] = [];

  constructor(
    private ordersService: SellerOrdersService,
    private router: Router
  ) {}

  ngOnInit() {
    this.load();
  }

  load() {
    this.ordersService.getOrders()
      .subscribe({
        next: res => this.orders = res,
        error: err => console.error('Failed to load orders', err)
      });
  }

  ship(orderId: number) {
    this.ordersService.updateStatus(orderId, 'SHIPPED')
      .subscribe(() => this.load());
  }

  deliver(orderId: number) {
    this.ordersService.updateStatus(orderId, 'DELIVERED')
      .subscribe(() => this.load());
  }

  /* NAVIGATION */
  goDashboard() {
    this.router.navigate(['/seller']);
  }

  goAdd() {
    this.router.navigate(['/seller/products/new']);
  }

  goProducts() {
    this.router.navigate(['/seller/products']);
  }
}
