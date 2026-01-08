import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminService } from './services/admin.service';
import { RouterLink } from '@angular/router';

@Component({
  standalone: true,
  imports: [CommonModule, RouterLink],
  selector: 'app-admin-orders',
  template: `
    <div class="page">
      <div class="card">

        <h3 class="title">All Orders</h3>

        <div class="list">
          <div class="row" *ngFor="let o of orders">
            <div class="info">
              <span class="name">Order #{{ o.orderId }}</span>
              <span class="meta">Buyer: {{ o.buyerEmail }}</span>
              <span class="meta">Status: {{ o.status }}</span>
            </div>

           <span class="amount">{{ o.total | currency:'USD':'symbol' }}</span>
          </div>
        </div>

        <p *ngIf="orders.length === 0" class="empty">
          No orders found.
        </p>

        <div class="footer">
          <a routerLink="/admin" class="back">← Back to Dashboard</a>
        </div>

      </div>
    </div>
  `,
  styles: [`
    .page { min-height:100vh; display:flex; justify-content:center; align-items:center; background:#f5f6fa; padding:20px; }
    .card { width:100%; max-width:800px; background:#fff; padding:30px; border-radius:12px; box-shadow:0 10px 25px rgba(0,0,0,0.08); }
    .title { text-align:center; margin-bottom:24px; }
    .list { display:flex; flex-direction:column; gap:14px; }
    .row { display:flex; justify-content:space-between; background:#f8f9fc; padding:14px; border-radius:10px; }
    .info { display:flex; flex-direction:column; }
    .name { font-weight:600; }
    .meta { font-size:13px; color:#666; }
    .amount { font-weight:600; }
    .empty { text-align:center; color:#666; margin-top:20px; }
    .footer { text-align:center; margin-top:24px; }
    .back { padding:10px 16px; background:#e5e7eb; border-radius:8px; text-decoration:none; color:#111; }
  `]
})
export class AdminOrdersComponent implements OnInit {

  orders: any[] = [];

  constructor(private adminService: AdminService) {}

  ngOnInit() {
    this.adminService.getOrders().subscribe(res => this.orders = res);
  }
}
