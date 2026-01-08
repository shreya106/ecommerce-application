import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'app-buyer-dashboard',
  imports: [CommonModule],
  template: `
    <h2>Buyer Dashboard</h2>
    <p>Welcome Buyer 🎉</p>
  `
})


export class BuyerDashboardComponent {}
