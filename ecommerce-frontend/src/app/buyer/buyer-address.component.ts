import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PaymentService } from './services/payment.service';
import { FormsModule } from '@angular/forms';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="page-container">
      <div class="card">

        <h2 class="title">Shipping Address</h2>
        <p class="subtitle">Enter your delivery details</p>

        <div class="form">

          <div class="field">
            <label>Address Line 1</label>
            <input
              placeholder="Street address"
              [(ngModel)]="address.line1" />
          </div>

          <div class="grid">
            <div class="field">
              <label>City</label>
              <input
                placeholder="City"
                [(ngModel)]="address.city" />
            </div>

            <div class="field">
              <label>Zip Code</label>
              <input
                placeholder="ZIP"
                [(ngModel)]="address.zip" />
            </div>
          </div>

          <button
            (click)="pay()"
            [disabled]="!address.line1 || !address.city || !address.zip">
            💳 Pay & Continue
          </button>

        </div>

      </div>
    </div>
  `,
  styles: [`
    /* 🌈 Login-theme background */
    .page-container {
      min-height: 100vh;
      display: flex;
      justify-content: center;
      align-items: center;
      background:
        radial-gradient(circle at top left, #c7d2fe, transparent 40%),
        radial-gradient(circle at bottom right, #fde68a, transparent 40%),
        linear-gradient(135deg, #eef2ff, #f8fafc);
      padding: 24px;
    }

    /* 🧊 Card */
    .card {
      width: 100%;
      max-width: 500px;
      background: rgba(255, 255, 255, 0.85);
      border-radius: 20px;
      padding: 32px;
      box-shadow: 0 40px 80px rgba(0, 0, 0, 0.12);
    }

    /* 🪩 Title */
    .title {
      text-align: center;
      font-size: 24px;
      font-weight: 700;
      color: #3730a3;
      margin-bottom: 6px;
    }

    /* 🎈 Subtitle */
    .subtitle {
      text-align: center;
      font-size: 14px;
      color: #475569;
      margin-bottom: 28px;
    }

    .form {
      display: flex;
      flex-direction: column;
      gap: 18px;
    }

    .field {
      display: flex;
      flex-direction: column;
      gap: 6px;
    }

    label {
      font-size: 13px;
      font-weight: 600;
      color: #4338ca;
    }

    /* ✍️ Inputs (login theme) */
    input {
      padding: 12px 14px;
      border-radius: 10px;
      border: 1px solid #e5e7eb;
      font-size: 14px;
      background: #f8fafc;
    }

    input:focus {
      outline: none;
      border-color: #c7d2fe;
      background: #ffffff;
    }

    .grid {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 14px;
    }

    /* 🌸 Primary button (login theme) */
    button {
      margin-top: 10px;
      padding: 13px;
      border-radius: 12px;
      border: none;
      font-size: 15px;
      font-weight: 600;
      background: #e0e7ff;
      color: #3730a3;
      cursor: pointer;
    }

    button:hover:not(:disabled) {
      background: #c7d2fe;
    }

    button:disabled {
      background: #e5e7eb;
      color: #9ca3af;
      cursor: not-allowed;
    }

    @media (max-width: 600px) {
      .grid {
        grid-template-columns: 1fr;
      }
    }
  `]
})
export class BuyerAddressComponent {
  address = { line1: '', city: '', zip: '' };

  constructor(private paymentService: PaymentService) {}

  pay() {
    this.paymentService.createCheckoutSession().subscribe(res => {
      window.location.href = res.url;
    });
  }
}
