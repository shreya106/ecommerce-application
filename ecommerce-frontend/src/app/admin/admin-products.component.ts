import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminService } from './services/admin.service';
import { RouterLink } from '@angular/router';

@Component({
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="page-container">
      <div class="card">

        <h3 class="title">All Products</h3>

        <div class="products-list">
          <div class="product-row" *ngFor="let p of products">
            <div class="product-info">
              <span class="name">{{ p.name }}</span>
              <span class="status">
                Status: {{ p.isActive ? 'Inactive' : 'Active' }}
              </span>
            </div>

            <!-- BUTTON STATE KEPT EXACTLY AS YOUR CODE -->
            <button class="action-btn" (click)="toggle(p)">
              {{ p.isActive ? 'Enable' : 'Disable' }}
            </button>
          </div>
        </div>

        <div class="footer">
          <a routerLink="/admin" class="back-btn">
            ← Back to Dashboard
          </a>
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

    .card {
      width: 100%;
      max-width: 750px;
      background: #ffffff;
      padding: 30px;
      border-radius: 12px;
      box-shadow: 0 10px 25px rgba(0,0,0,0.08);
    }

    .title {
      text-align: center;
      margin-bottom: 24px;
      font-weight: 600;
    }

    .products-list {
      display: flex;
      flex-direction: column;
      gap: 14px;
    }

    .product-row {
      display: flex;
      justify-content: space-between;
      align-items: center;
      background: #f8f9fc;
      padding: 14px 16px;
      border-radius: 10px;
    }

    .product-info {
      display: flex;
      flex-direction: column;
    }

    .name {
      font-weight: 600;
      color: #222;
    }

    .status {
      font-size: 13px;
      color: #666;
      margin-top: 4px;
    }

    .action-btn {
      padding: 8px 16px;
      border: none;
      border-radius: 8px;
      background: #4f46e5;
      color: #fff;
      font-weight: 500;
      cursor: pointer;
      transition: background 0.2s ease;
    }

    .action-btn:hover {
      background: #4338ca;
    }

    .footer {
      text-align: center;
      margin-top: 24px;
    }

    .back-btn {
      display: inline-block;
      padding: 10px 16px;
      background: #e5e7eb;
      color: #111;
      border-radius: 8px;
      text-decoration: none;
      font-weight: 500;
    }

    .back-btn:hover {
      background: #d1d5db;
    }

    @media (max-width: 600px) {
      .product-row {
        flex-direction: column;
        align-items: flex-start;
        gap: 10px;
      }

      .action-btn {
        width: 100%;
      }
    }
  `]
})
export class AdminProductsComponent implements OnInit {

  products: any[] = [];

  constructor(private adminService: AdminService) {}

  ngOnInit() {
    this.load();
  }

  load() {
    this.adminService.getProducts().subscribe(res => {
      this.products = res;
    });
  }

  toggle(product: any) {
    console.log('TOGGLING PRODUCT ID:', product.id);

    this.adminService.toggleProduct(product.id).subscribe({
      next: () => product.isActive = !product.isActive,
      error: err => {
        if (err.status === 200) {
          product.isActive = !product.isActive;
        } else {
          console.error('Product toggle failed', err);
        }
      }
    });
  }
}
