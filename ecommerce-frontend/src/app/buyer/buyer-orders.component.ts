import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BuyerOrdersService } from './services/buyer-orders.service';
import { Router } from '@angular/router';

@Component({
  standalone: true,
  imports: [CommonModule],
  selector: 'app-buyer-orders',
  template: `
    <div class="page-container">
      <div class="card">

        <div class="header">
  <button class="home-btn" (click)="goToHome()">← Back to Shop</button>
  <h2 class="title">My Orders</h2>
</div>

        <div class="orders-list">
          <div class="order-card" *ngFor="let o of orders">

           

            <!-- INFO -->
            <div class="order-info">
              <div class="order-header">
                <span class="order-id">Order #{{ o.orderId }}</span>
                <span class="status">{{ o.status }}</span>
              </div>

              <div class="order-meta">
  <span>Total: {{ o.total | currency:'USD' }}</span>
  <span>{{ o.timestamp | date:'short' }}</span>
</div>
            </div>

            

            <!-- ACTION -->
            <button (click)="view(o.orderId)" class="view-btn">
              View Details
            </button>

          </div>
        </div>

        <p *ngIf="orders.length === 0" class="empty">
          No orders found.
        </p>

      </div>
    </div>
  `,
  styles: [`
    .page-container {
      min-height: 100vh;
      background:
        radial-gradient(circle at top left, #c7d2fe, transparent 40%),
        radial-gradient(circle at bottom right, #ddd6fe, transparent 40%),
        linear-gradient(135deg, #eef2ff, #f8fafc);
      display: flex;
      justify-content: center;
      align-items: center;
      padding: 24px;
    }

    .card {
      width: 100%;
      max-width: 900px;
      background: rgba(255, 255, 255, 0.9);
      padding: 32px;
      border-radius: 20px;
      box-shadow: 0 40px 80px rgba(0,0,0,0.12);
    }

    .title {
      text-align: center;
      font-size: 26px;
      font-weight: 700;
      color: #3730a3;
      margin-bottom: 28px;
    }

    .orders-list {
      display: flex;
      flex-direction: column;
      gap: 18px;
    }

    .order-card {
      background: #f8f9fc;
      padding: 16px;
      border-radius: 16px;
      display: flex;
      align-items: center;
      gap: 16px;
    }

    /* IMAGE */
    .image-wrapper {
      width: 80px;
      height: 80px;
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

    .order-info {
      flex: 1;
      display: flex;
      flex-direction: column;
      gap: 8px;
    }

    .order-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .order-id {
      font-weight: 600;
      font-size: 16px;
      color: #1e1b4b;
    }

    .status {
      font-size: 13px;
      font-weight: 600;
      color: #4338ca;
    }

    .order-meta {
      display: flex;
      justify-content: space-between;
      font-size: 14px;
      color: #475569;
    }

    .view-btn {
      padding: 8px 16px;
      border-radius: 10px;
      border: none;
      background: #e0e7ff;
      color: #3730a3;
      font-weight: 600;
      cursor: pointer;
    }

    .view-btn:hover {
      background: #c7d2fe;
    }

    .empty {
      text-align: center;
      margin-top: 20px;
      color: #6b7280;
      font-size: 15px;
    }

    @media (max-width: 600px) {
      .order-card {
        flex-direction: column;
        align-items: flex-start;
      }

      .order-meta {
        flex-direction: column;
        gap: 4px;
      }

      .view-btn {
        width: 100%;
      }
    }
      .home-btn {
  padding: 8px 14px;
  border-radius: 10px;
  border: none;
  background: #f1f5f9;
  color: #1e293b;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
}
  .home-btn:hover {
  background: #e2e8f0;
}
  `]
})
export class BuyerOrdersComponent implements OnInit {

  orders: any[] = [];
  selected: any;

  constructor(
    private ordersService: BuyerOrdersService,
    private router: Router
  ) {}

  ngOnInit() {
    this.ordersService.getOrders().subscribe(res => this.orders = res);
  }

  view(id: number) {
    this.router.navigate(['/buyer/orders', id]);
  }
  goToHome() {
    this.router.navigate(['/buyer/products']);
  }
}
