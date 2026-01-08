import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { BuyerOrdersService } from './services/buyer-orders.service';

@Component({
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="page-container">
      <div class="card">

        <h2 class="title">Order Details</h2>

        <div *ngIf="order" class="content">

          <!-- SUMMARY -->
          <div class="summary">
            <p><b>Order #{{ order.orderId }}</b></p>
            <p>Status: <span class="status">{{ order.status }}</span></p>
            <p>Total: <b>₹{{ order.total }}</b></p>
          </div>

          <!-- ITEMS -->
          <h4 class="items-title">Items</h4>

          <div class="items">
            <div class="item" *ngFor="let i of order.items">

              <!-- IMAGE -->
              <div class="image-wrapper">
                <img
                  [src]="i.imageUrl"
                  alt="{{ i.productName }}"
                  loading="lazy"
                />
              </div>

              <!-- INFO -->
              <div class="item-info">
                <span class="name">{{ i.productName }}</span>
                <span class="qty">Quantity: × {{ i.quantity }}</span>
              </div>

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
      background:
        radial-gradient(circle at top left, #c7d2fe, transparent 40%),
        radial-gradient(circle at bottom right, #ddd6fe, transparent 40%),
        linear-gradient(135deg, #eef2ff, #f8fafc);
      padding: 24px;
    }

    .card {
      width: 100%;
      max-width: 700px;
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
      margin-bottom: 24px;
    }

    .content {
      display: flex;
      flex-direction: column;
      gap: 24px;
    }

    .summary p {
      margin: 4px 0;
      font-size: 14px;
      color: #1f2937;
    }

    .status {
      font-weight: 600;
      color: #4338ca;
    }

    .items-title {
      font-size: 18px;
      font-weight: 600;
      color: #1e1b4b;
    }

    .items {
      display: flex;
      flex-direction: column;
      gap: 14px;
    }

    .item {
      display: flex;
      align-items: center;
      gap: 16px;
      background: #f8f9fc;
      padding: 14px;
      border-radius: 14px;
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

    .item-info {
      display: flex;
      flex-direction: column;
      gap: 6px;
    }

    .name {
      font-size: 15px;
      font-weight: 600;
      color: #1e1b4b;
    }

    .qty {
      font-size: 13px;
      color: #475569;
    }

    @media (max-width: 600px) {
      .item {
        flex-direction: column;
        align-items: flex-start;
      }
    }
  `]
})
export class BuyerOrderDetailsComponent implements OnInit {
  order: any;

  constructor(
    private route: ActivatedRoute,
    private ordersService: BuyerOrdersService
  ) {}

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.ordersService.getOrderById(id).subscribe(res => this.order = res);
  }
}
