import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminService } from './services/admin.service';
import { RouterLink } from '@angular/router';

@Component({
  standalone: true,
  imports: [CommonModule, RouterLink],
  selector: 'app-admin-users',
  template: `
    <div class="page-container">
      <div class="card">

        <h3 class="title">All Users</h3>

        <div class="users-list">
          <div class="user-row" *ngFor="let u of users">
            <div class="user-info">
              <span class="name">{{ u.name }}</span>
              <span class="role">Role: {{ u.role }}</span>
            </div>

            <button
              class="toggle-btn"
              [class.disabled]="!u.isActive"
              (click)="toggle(u)">
              {{ u.isActive ? 'Disable' : 'Enable' }}
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
      max-width: 700px;
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

    .users-list {
      display: flex;
      flex-direction: column;
      gap: 14px;
      margin-bottom: 30px;
    }

    .user-row {
      display: flex;
      justify-content: space-between;
      align-items: center;
      background: #f8f9fc;
      padding: 14px 16px;
      border-radius: 10px;
    }

    .user-info {
      display: flex;
      flex-direction: column;
    }

    .name {
      font-weight: 600;
      color: #222;
    }

    .role {
      font-size: 13px;
      color: #666;
    }

    .toggle-btn {
      padding: 8px 14px;
      border: none;
      border-radius: 8px;
      background: #4f46e5;
      color: #fff;
      font-weight: 500;
      cursor: pointer;
      transition: background 0.2s ease;
    }

    .toggle-btn:hover {
      background: #4338ca;
    }

    .toggle-btn.disabled {
      background: #dc2626;
    }

    .toggle-btn.disabled:hover {
      background: #b91c1c;
    }

    .footer {
      text-align: center;
    }

    .back-btn {
      display: inline-block;
      padding: 10px 16px;
      background: #e5e7eb;
      color: #111;
      border-radius: 8px;
      text-decoration: none;
      font-weight: 500;
      transition: background 0.2s ease;
    }

    .back-btn:hover {
      background: #d1d5db;
    }

    @media (max-width: 600px) {
      .user-row {
        flex-direction: column;
        align-items: flex-start;
        gap: 10px;
      }

      .toggle-btn {
        width: 100%;
      }
    }
  `]
})
export class AdminUsersComponent implements OnInit {

  users: any[] = [];

  constructor(private adminService: AdminService) {}

  ngOnInit() {
    this.load();
  }

  load() {
    this.adminService.getUsers().subscribe(res => {
      this.users = res.map(u => ({
        ...u,
        isActive: u.isActive ?? true
      }));
    });
  }

  toggle(user: any) {
    this.adminService.toggleUser(user.userId).subscribe({
      next: () => user.isActive = !user.isActive,
      error: err => {
        if (err.status === 200) {
          user.isActive = !user.isActive;
        } else {
          console.error('Toggle failed', err);
        }
      }
    });
  }
}
