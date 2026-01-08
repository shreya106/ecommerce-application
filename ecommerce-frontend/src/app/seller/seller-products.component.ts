import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SellerService } from './seller.service';
import { Router, RouterLink } from '@angular/router';

@Component({
  standalone: true,
  imports: [CommonModule, RouterLink],
  selector: 'app-seller-products',
  template: `
    <div class="page-container">
      <div class="card">

        <!-- TOP BAR -->
        <div class="top-bar">
          <h3 class="title">My Products</h3>

          <div class="nav-actions">
            <button class="nav-btn" (click)="goDashboard()">Dashboard</button>
            <button class="nav-btn" (click)="goOrders()">Orders</button>
            <button class="nav-btn" (click)="goAdd()">Add Product</button>
          </div>
        </div>

        <!-- PRODUCT LIST -->
        <div class="products-list">
          <div class="product-card" *ngFor="let p of products">

            <div class="info">
              <span class="name">{{ p.name }}</span>
              <span class="meta">₹{{ p.price }} · Stock: {{ p.stock }}</span>
            </div>

            <div class="actions">
              <button
                *ngIf="p.active"
                class="btn danger"
                (click)="remove(p.id)">
                Remove
              </button>

              <button
                *ngIf="!p.active"
                class="btn success"
                (click)="restore(p.id)">
                Add
              </button>

              <button
                class="btn secondary"
                [routerLink]="['/seller/products/edit', p.id]">
                Edit
              </button>
            </div>

          </div>
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
      flex-wrap: wrap;
      gap: 14px;
      margin-bottom: 26px;
    }

    .title {
      font-size: 24px;
      font-weight: 600;
      margin: 0;
    }

    .nav-actions {
      display: flex;
      gap: 10px;
      flex-wrap: wrap;
    }

    .nav-btn {
      padding: 8px 14px;
      border-radius: 10px;
      border: none;
      font-size: 14px;
      font-weight: 600;
      background: #ede9fe;
      color: #4c1d95;
      cursor: pointer;
    }

    .nav-btn:hover {
      background: #ddd6fe;
    }

    .nav-btn.primary {
      background: #4f46e5;
      color: #ffffff;
    }

    .nav-btn.primary:hover {
      background: #4338ca;
    }

    /* LIST */
    .products-list {
      display: flex;
      flex-direction: column;
      gap: 14px;
    }

    .product-card {
      display: flex;
      justify-content: space-between;
      align-items: center;
      background: #f8f9fc;
      padding: 16px;
      border-radius: 12px;
    }

    .info {
      display: flex;
      flex-direction: column;
    }

    .name {
      font-size: 16px;
      font-weight: 600;
      color: #111;
    }

    .meta {
      font-size: 13px;
      color: #6b7280;
      margin-top: 4px;
    }

    .actions {
      display: flex;
      gap: 10px;
    }

    /* ACTION BUTTONS */
    .btn {
      padding: 8px 14px;
      border: none;
      border-radius: 10px;
      font-size: 14px;
      font-weight: 600;
      cursor: pointer;
    }

    .btn.danger {
      background: #fee2e2;
      color: #991b1b;
    }

    .btn.danger:hover {
      background: #fecaca;
    }

    .btn.success {
      background: #dcfce7;
      color: #166534;
    }

    .btn.success:hover {
      background: #bbf7d0;
    }

    .btn.secondary {
      background: #ede9fe;
      color: #4c1d95;
    }

    .btn.secondary:hover {
      background: #ddd6fe;
    }

    @media (max-width: 700px) {
      .product-card {
        flex-direction: column;
        align-items: flex-start;
        gap: 12px;
      }

      .actions, .nav-actions {
        width: 100%;
      }

      .btn, .nav-btn {
        width: 100%;
      }
    }
  `]
})
export class SellerProductsComponent implements OnInit {

  products: any[] = [];

  constructor(
    private sellerService: SellerService,
    private router: Router
  ) {}

  ngOnInit() {
    this.load();
  }

  load() {
    this.sellerService.getProducts().subscribe(res => this.products = res);
  }

  remove(id: number) {
    this.sellerService.deleteProduct(id).subscribe(() => this.load());
  }

  restore(id: number) {
    this.sellerService.restoreProduct(id).subscribe(() => this.load());
  }

  /* NAVIGATION */
  goDashboard() {
    this.router.navigate(['/seller']);
  }

  goOrders() {
    this.router.navigate(['/seller/orders']);
  }

  goAdd() {
    this.router.navigate(['/seller/products/new']);
  }
}
