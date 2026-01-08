import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { AdminOrdersService } from './services/admin-orders.service';

@Component({
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="page">
      <div class="card">

        <h3 class="title">Order Details</h3>

        <div *ngIf="order">
          <p><b>Order #{{ order.orderId }}</b></p>
          <p>Buyer: {{ order.buyer.emailId }}</p>
          <p>Status: {{ order.status }}</p>
          <p>Total: ₹{{ order.total }}</p>

          <h4 class="subtitle">Items</h4>

          <div class="item" *ngFor="let i of order.items">
            {{ i.product.name }} × {{ i.quantity }}
          </div>
        </div>

        <div class="footer">
          <a routerLink="/admin/orders" class="back">
            ← Back to Orders
          </a>
        </div>

      </div>
    </div>
  `,
  styles: [`
    .page { min-height:100vh; display:flex; justify-content:center; align-items:center; background:#f5f6fa; padding:20px; }
    .card { width:100%; max-width:600px; background:#fff; padding:30px; border-radius:12px; box-shadow:0 10px 25px rgba(0,0,0,0.08); }
    .title { text-align:center; margin-bottom:20px; }
    .subtitle { margin-top:20px; }
    .item { background:#f8f9fc; padding:10px; border-radius:8px; margin-top:8px; }
    .footer { text-align:center; margin-top:24px; }
    .back { padding:10px 16px; background:#e5e7eb; border-radius:8px; text-decoration:none; color:#111; }
  `]
})
export class AdminOrderDetailsComponent implements OnInit {

  order: any;

  constructor(
    private route: ActivatedRoute,
    private ordersService: AdminOrdersService
  ) {}

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.ordersService.getOrderById(id).subscribe(res => this.order = res);
  }
}
