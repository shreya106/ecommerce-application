import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="page-container">
      <div class="card">

        <div class="icon">🎉</div>

        <h2 class="title">Payment Successful</h2>
        <p class="subtitle">
          Your order has been placed successfully.
        </p>

        <button (click)="goToOrders()">
          View My Orders
        </button>

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
      max-width: 420px;
      background: #ffffff;
      padding: 36px 30px;
      border-radius: 16px;
      box-shadow: 0 12px 28px rgba(0,0,0,0.08);
      text-align: center;
    }

    .icon {
      font-size: 48px;
      margin-bottom: 14px;
    }

    .title {
      font-size: 24px;
      font-weight: 600;
      margin-bottom: 6px;
    }

    .subtitle {
      font-size: 14px;
      color: #6b7280;
      margin-bottom: 28px;
    }

    button {
      padding: 12px 20px;
      border-radius: 10px;
      border: none;
      font-size: 15px;
      font-weight: 600;
      background: #16a34a;
      color: #ffffff;
      cursor: pointer;
      transition: background 0.2s ease;
    }

    button:hover {
      background: #15803d;
    }
  `]
})
export class PaymentSuccessComponent {
  constructor(private router: Router) {}

  goToOrders() {
    this.router.navigate(['/buyer/orders']);
  }
}
