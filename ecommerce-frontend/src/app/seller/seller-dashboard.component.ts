import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, Router } from '@angular/router';

@Component({
  standalone: true,
  imports: [CommonModule, RouterLink],
  selector: 'app-seller-dashboard',
  template: `
    <div class="page-container">
      <div class="dashboard-card">

        <h2 class="title">Seller Dashboard</h2>
        <p class="subtitle">Manage your products and orders</p>

        <div class="actions-grid">
          <a routerLink="/seller/products" class="action-card">
            📦
            <span>My Products</span>
          </a>

          <a routerLink="/seller/products/new" class="action-card">
            ➕
            <span>Add Product</span>
          </a>

          <button class="action-card" (click)="goToOrders()">
            🧾
            <span>View Orders</span>
          </button>
        </div>

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

    .dashboard-card {
      width: 100%;
      max-width: 700px;
      background: #ffffff;
      padding: 32px;
      border-radius: 14px;
      box-shadow: 0 12px 28px rgba(0,0,0,0.08);
      text-align: center;
    }

    .title {
      margin-bottom: 6px;
      font-weight: 600;
    }

    .subtitle {
      color: #666;
      margin-bottom: 30px;
      font-size: 14px;
    }

    .actions-grid {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 18px;
    }

    .action-card {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      gap: 8px;
      padding: 22px;
      border-radius: 12px;
      background: #f8f9fc;
      border: none;
      cursor: pointer;
      text-decoration: none;
      color: #111;
      font-size: 18px;
      font-weight: 500;
      transition: all 0.2s ease;
    }

    .action-card span {
      font-size: 14px;
      font-weight: 500;
    }

    .action-card:hover {
      background: #eef0ff;
      transform: translateY(-2px);
    }

    @media (max-width: 600px) {
      .actions-grid {
        grid-template-columns: 1fr;
      }
    }
  `]
})
export class SellerDashboardComponent {

  constructor(private router: Router) {}

  goToOrders() {
    this.router.navigate(['/seller/orders']);
  }
}
