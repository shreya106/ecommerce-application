import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminService } from './services/admin.service';
import { RouterLink } from '@angular/router';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { ViewChild, ElementRef } from '@angular/core';

import { Chart, registerables } from 'chart.js';
Chart.register(...registerables);

@Component({
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="dashboard-container">
      <div class="dashboard-card">
      <!-- NAV BAR -->
<div class="admin-nav">
  <div class="nav-left">
    
  </div>

  <div class="nav-actions">
    <a routerLink="/admin/products" class="nav-btn">Manage Products</a>
    <a routerLink="/admin/users" class="nav-btn">Manage Users</a>
    <a routerLink="/admin/orders" class="nav-btn"> Manage Orders</a>
  </div>
</div>

        <h2 class="title">Admin Dashboard</h2>

        <!-- CORE STATS -->
        <div class="stats-grid" *ngIf="stats">
          <div class="stat-box">
            <span><strong>Total Users </strong></span>
            <strong>{{ stats.totalUsers }}</strong>
          </div>

          <div class="stat-box">
            <span><strong>Total Products </strong></span>
            <strong>{{ stats.totalProducts }}</strong>
          </div>

          <div class="stat-box">
            <span><strong>Total Orders </strong></span>
            <strong>{{ analytics?.totalOrders || stats.totalOrders }}</strong>
          </div>

          <div class="stat-box">
            <span><strong>Total Revenue </strong></span>
           <strong>{{ analytics?.totalRevenue || stats.totalRevenue | currency:'USD' }}</strong>
          </div>
        </div>

        <!-- CHARTS -->
        <div class="charts" *ngIf="analytics">
          <div class="chart-box">
            <h4>Orders by Status</h4>
            <div class="chart-wrapper">
    <canvas #statusChart></canvas>
  </div>
          </div>

          <div class="chart-box">
            <h4>Top Selling Products</h4>
            <canvas #topProductsChart></canvas>
          </div>
        </div>

        

      </div>
    </div>
  `,
  styles: [`
    .dashboard-container {
      min-height: 100vh;
      display: flex;
      justify-content: center;
      align-items: center;
      background: #f5f6fa;
      padding: 20px;
    }

    .dashboard-card {
     width: 100%;
  max-width: 1200px;        
  background: #fff;
  padding: 40px 50px;      
  border-radius: 16px;
  box-shadow: 0 12px 30px rgba(0,0,0,0.08);
    }
  .chart-wrapper {
  position: relative;
  width: 100%;
  height: 280px;   /* 🔥 controls pie size */
}

.chart-wrapper canvas {
  max-width: 100% !important;
  max-height: 100% !important;
}

    /* NAV BAR */
.admin-nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding-bottom: 14px;
  border-bottom: 1px solid #e5e7eb;
}

.brand {
  font-size: 18px;
  font-weight: 600;
  color: #111827;
}

.nav-actions {
  display: flex;
  gap: 12px;
}

.nav-btn {
  padding: 8px 14px;
  background: #eef2ff;
  color: #4338ca;
  border-radius: 8px;
  text-decoration: none;
  font-weight: 500;
  font-size: 14px;
  transition: background 0.2s ease;
}

.nav-btn:hover {
  background: #e0e7ff;
}

    .title {
      text-align: center;
      margin-bottom: 25px;
      font-weight: 600;
    }

    .stats-grid {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 16px;
      margin-bottom: 25px;
    }

    .stat-box {
      background: #f8f9fc;
      border-radius: 10px;
      padding: 16px;
      text-align: center;
    }

    .stat-box span {
      font-size: 13px;
      color: #666;
    }

    .stat-box strong {
      font-size: 20px;
      font-weight: 600;
    }

    .charts {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 20px;
      margin-bottom: 30px;
    }

    .chart-box {
  background: #f8f9fc;
  padding: 16px;
  border-radius: 12px;
  height: 360px;          
  display: flex;
  flex-direction: column;
}

    .chart-box canvas {
   flex: 1; 
}
    .chart-box h4 {
      text-align: center;
      margin-bottom: 10px;
      font-size: 14px;
    }

    .actions {
      display: flex;
      gap: 12px;
    }

    .action-btn {
      flex: 1;
      text-align: center;
      padding: 12px;
      background: #4f46e5;
      color: #fff;
      border-radius: 8px;
      text-decoration: none;
      font-weight: 500;
    }

    @media (max-width: 700px) {
      .charts {
        grid-template-columns: 1fr;
      }

      .stats-grid {
        grid-template-columns: 1fr;
      }

      .actions {
        flex-direction: column;
      }
    }

    @media (max-width: 700px) {
  .admin-nav {
    flex-direction: column;
    gap: 12px;
    align-items: flex-start;
  }

  .nav-actions {
    width: 100%;
    justify-content: space-between;
  }
}
  `]
})
export class AdminDashboardComponent implements OnInit, OnDestroy {

  stats: any;
  analytics: any;

  private stompClient!: Client;
  private statusChart: Chart | null = null;
  private topProductsChart: Chart | null = null;

  constructor(private adminService: AdminService) {}

  ngOnInit() {
    this.loadDashboard();
    this.loadAnalytics();
    this.connectLiveAnalytics();
  }

  ngOnDestroy() {
    this.stompClient?.deactivate();
  this.statusChart?.destroy();
  this.topProductsChart?.destroy();
  }
  @ViewChild('statusChart') statusChartRef!: ElementRef<HTMLCanvasElement>;
@ViewChild('topProductsChart') topProductsChartRef!: ElementRef<HTMLCanvasElement>;



  /* REST */
  loadDashboard() {
    this.adminService.getDashboard().subscribe(res => this.stats = res);
  }

  loadAnalytics() {
    this.adminService.getAnalytics().subscribe(res => {
      this.analytics = res;
      this.renderCharts();
    });
  }

  /* CHARTS */
  renderCharts() {
    setTimeout(() => {
      this.renderStatusChart();
      this.renderTopProductsChart();
    });
  }
  

  renderStatusChart() {
    const raw = this.analytics.ordersByStatus;
  
    const labels = ['PENDING', 'SHIPPED', 'DELIVERED', 'CANCELLED'];
    const values = labels.map(l => raw[l] || 0);
  
    this.statusChart?.destroy();
  
    const ctx = this.statusChartRef.nativeElement.getContext('2d')!;
    this.statusChart = new Chart(ctx, {
      type: 'pie',
      data: {
        labels,
        datasets: [{
          data: values,
          backgroundColor: ['#fde68a', '#93c5fd', '#86efac', '#fca5a5']
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false
      }
    });
  }
  
  

  renderTopProductsChart() {
    const products = this.analytics.topProducts;
  
    this.topProductsChart?.destroy();
  
    const ctx = this.topProductsChartRef.nativeElement.getContext('2d')!;
  
    this.topProductsChart = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: products.map((p: any) => p.productName),
        datasets: [{
          label: 'Units Sold',
          data: products.map((p: any) => p.quantitySold),
          backgroundColor: '#4f46e5'
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,   
        indexAxis: 'y',
        plugins: {
          legend: { display: false }
        },
        scales: {
          x: { beginAtZero: true },
          y: { ticks: { autoSkip: false } }
        }
      }
      
    });
  }
  
  
  
  

  /* LIVE ANALYTICS */
  connectLiveAnalytics() {
    this.stompClient = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws-stock'),
      reconnectDelay: 5000
    });

    this.stompClient.onConnect = () => {
      console.log('📊 Admin analytics WS connected');

      this.stompClient.subscribe('/topic/admin/analytics', msg => {
        this.analytics = JSON.parse(msg.body);
        this.renderCharts(); // 🔥 LIVE UPDATE
      });
    };

    this.stompClient.activate();
  }
}
